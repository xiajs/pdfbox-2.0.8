package com.xiajs.pdf;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.xml.transform.TransformerException;

import org.apache.pdfbox.examples.pdmodel.CreatePDFA;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDMetadata;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.color.PDOutputIntent;
import org.apache.xmpbox.XMPMetadata;
import org.apache.xmpbox.schema.DublinCoreSchema;
import org.apache.xmpbox.schema.PDFAIdentificationSchema;
import org.apache.xmpbox.type.BadFieldValueException;
import org.apache.xmpbox.xml.XmpSerializer;

/**
 * Hello world!
 *
 */
public class App {
	private final static String outDir = "../../examples/target/test-output";

	public static void main(String[] args) throws IOException, TransformerException, UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, CertificateException {

		String file = outDir + "/PDFA-xiajs.pdf";
		String fontfile = outDir + "/STLITI.TTF";

		PDDocument doc = new PDDocument();
		try {
			float width = 580f;
			float height = 380f;
			PDRectangle pdr = new PDRectangle(width, height);
			PDPage page = new PDPage(pdr);
			doc.addPage(page);

			// load the font as this needs to be embedded
			PDFont font = PDType0Font.load(doc, new File(fontfile));

			// A PDF/A file needs to have the font embedded if the font is used
			// for text rendering
			// in rendering modes other than text rendering mode 3.
			//
			// This requirement includes the PDF standard fonts, so don't use
			// their static PDFType1Font classes such as
			// PDFType1Font.HELVETICA.
			//
			// As there are many different font licenses it is up to the
			// developer to check if the license terms for the
			// font loaded allows embedding in the PDF.
			//
			if (!font.isEmbedded()) {
				throw new IllegalStateException("PDF/A compliance requires that all fonts used for"
						+ " text rendering in rendering modes other than rendering mode 3 are embedded.");
			}

			// create a page with the message
			PDPageContentStream contents = new PDPageContentStream(doc, page);
			ReceiptTemplate receipt = new ReceiptTemplate(contents);
			receipt.setFont(font);
			receipt.draw();
			contents.saveGraphicsState();
			contents.close();

			
			// add XMP metadata
			XMPMetadata xmp = XMPMetadata.createXMPMetadata();

			try {
				DublinCoreSchema dc = xmp.createAndAddDublinCoreSchema();
				dc.setTitle(file);

				PDFAIdentificationSchema id = xmp.createAndAddPFAIdentificationSchema();
				id.setPart(1);
				id.setConformance("B");

				XmpSerializer serializer = new XmpSerializer();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				serializer.serialize(xmp, baos, true);

				PDMetadata metadata = new PDMetadata(doc);
				metadata.importXMPMetadata(baos.toByteArray());
				doc.getDocumentCatalog().setMetadata(metadata);
			} catch (BadFieldValueException e) {
				// won't happen here, as the provided value is valid
				throw new IllegalArgumentException(e);
			}

			// sRGB output intent
			InputStream colorProfile = CreatePDFA.class
					.getResourceAsStream("/org/apache/pdfbox/resources/pdfa/sRGB.icc");
			PDOutputIntent intent = new PDOutputIntent(doc, colorProfile);
			intent.setInfo("sRGB IEC61966-2.1");
			intent.setOutputCondition("sRGB IEC61966-2.1");
			intent.setOutputConditionIdentifier("sRGB IEC61966-2.1");
			intent.setRegistryName("http://www.color.org");
			doc.getDocumentCatalog().addOutputIntent(intent);


			doc.save(file);
		} finally {
			doc.close();
		}
		//开始签名
		//ReceiptVisibleSignature.sign(file);
	}
}
