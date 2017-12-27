
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.List;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.examples.signature.CreateSignature;
import org.apache.pdfbox.examples.signature.CreateVisibleSignature;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.interactive.form.PDSignatureField;
import org.apache.pdfbox.text.PDFTextStripper;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.util.Store;


/***
 * https://www.programcreek.com/java-api-examples/index.php?api=org.apache.pdfbox.pdmodel.PDDocument
 * https://pdfbox.apache.org/1.8/cookbook/documentcreation.html
 * @author xiajs
 *
 */
public class PdfMain {

	public static void main(String[] args) throws Exception {

		String keystorePath = "D:\\GitHub\\MavenDemo\\khbz-parent\\khbz-app\\src\\test\\resources\\keystore.p12";
		String password = "123456";
		String filename = "D:\\22.PDF";
		String jpegPath = "D:\\GitHub\\pdfbox-2.0.8\\examples\\src\\test\\resources\\org\\apache\\pdfbox\\examples\\signature\\stamp.jpg";
		String filenameSigned1 = "D:\\22.PDF";
		String filenameSigned2 = "D:\\22_signed2.PDF";
		// load the keystore
        KeyStore keystore = KeyStore.getInstance("PKCS12");
        keystore.load(new FileInputStream(keystorePath), password.toCharArray());

        // create file with empty signature
//        CreateEmptySignatureForm.main(new String[]{filename});

        // sign PDF
//        CreateSignature signing1 = new CreateSignature(keystore, password.toCharArray());
//        signing1.setExternalSigning(false);
//        signing1.signDetached(new File(filename), new File(filenameSigned1));
//
//        checkSignature(new File(filenameSigned1));
//
//        PDDocument doc1 = PDDocument.load(new File(filenameSigned1));
//        List<PDSignature> signatureDictionaries = doc1.getSignatureDictionaries();
////        Assert.assertEquals(1, signatureDictionaries.size());
//        for(PDSignature pds : signatureDictionaries){
//        	System.out.println(pds);
//        }
//        doc1.close();

        // do visual signing in the field
        FileInputStream fis = new FileInputStream(jpegPath);
        CreateVisibleSignature signing2 = new CreateVisibleSignature(keystore, password.toCharArray());
        signing2.setVisibleSignDesigner(filenameSigned1, 0, 0, -50, fis, 1);
        signing2.setVisibleSignatureProperties("name", "location", "Security", 0, 1, true);
        signing2.setExternalSigning(false);
        signing2.signPDF(new File(filenameSigned1), new File(filenameSigned2), null, "Signature1");
        fis.close();

        checkSignature(new File(filenameSigned2));

        PDDocument doc2 = PDDocument.load(new File(filenameSigned2));
        List<PDSignature> signatureDictionaries = doc2.getSignatureDictionaries();
        for(PDSignature pds : signatureDictionaries){
        	System.out.println(pds);
        }
        doc2.close();
	}
	 // This check fails with a file created with the code before PDFBOX-3011 was solved.
    private static void checkSignature(File file)
            throws Exception
    {
        PDDocument document = PDDocument.load(file);
        List<PDSignature> signatureDictionaries = document.getSignatureDictionaries();
        if (signatureDictionaries.isEmpty())
        {
//            Assert.fail("no signature found");
        	throw new  Exception("no signature found");
        }
        for (PDSignature sig : document.getSignatureDictionaries())
        {
            COSString contents = (COSString) sig.getCOSObject().getDictionaryObject(COSName.CONTENTS);
            
            FileInputStream fis = new FileInputStream(file);
            byte[] buf = sig.getSignedContent(fis);
            fis.close();

            // inspiration:
            // http://stackoverflow.com/a/26702631/535646
            // http://stackoverflow.com/a/9261365/535646
            CMSSignedData signedData = new CMSSignedData(new CMSProcessableByteArray(buf), contents.getBytes());
            Store certificatesStore = signedData.getCertificates();
            Collection<SignerInformation> signers = signedData.getSignerInfos().getSigners();
            SignerInformation signerInformation = signers.iterator().next();
            Collection matches = certificatesStore.getMatches(signerInformation.getSID());
            X509CertificateHolder certificateHolder = (X509CertificateHolder) matches.iterator().next();
            X509Certificate certFromSignedData = new JcaX509CertificateConverter().getCertificate(certificateHolder);

//            Assert.assertEquals(certificate, certFromSignedData);

            // CMSVerifierCertificateNotValidException means that the keystore wasn't valid at signing time
            if (!signerInformation.verify(new JcaSimpleSignerInfoVerifierBuilder().build(certFromSignedData)))
            {
//                Assert.fail("Signature verification failed");
            	throw new Exception("Signature verification failed");
            }
            break;
        }
        document.close();
    }
}
