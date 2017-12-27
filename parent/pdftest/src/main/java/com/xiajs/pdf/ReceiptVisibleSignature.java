package com.xiajs.pdf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import org.apache.pdfbox.pdmodel.PDDocument;

public class ReceiptVisibleSignature {

	static String keystorePath = "D:\\GitHub\\MavenDemo\\khbz-parent\\khbz-app\\src\\test\\resources\\keystore.p12";
	static String password = "123456";
//	static String filename = "D:\\22.PDF";
	static String jpegPath = "D:\\GitHub\\pdfbox-2.0.8\\examples\\src\\test\\resources\\org\\apache\\pdfbox\\examples\\signature\\stamp.jpg";
//	static String filenameSigned1 = "D:\\22.PDF";
	static String filenameSigned2 = "D:\\22_signed2.PDF";
	
	public static void sign(PDDocument doc) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException, UnrecoverableKeyException{

//        KeyStore keystore = KeyStore.getInstance("PKCS12");
//        keystore.load(new FileInputStream(keystorePath), password.toCharArray());
//        
//        FileInputStream fis = new FileInputStream(jpegPath);
//        CreateVisibleSignature signing2 = new CreateVisibleSignature(keystore, password.toCharArray());
//        signing2.setVisibleSignDesigner(doc, 20, 30, -50, fis, 1);
//        signing2.setVisibleSignatureProperties("name", "location", "Security", 0, 1, true);
//        signing2.setExternalSigning(false);
//        signing2.signPDF(doc, "Signature1");
//        fis.close();
	}
	
	public static void sign(String file) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException, UnrecoverableKeyException{
		String filenameSigned1 = file;
        KeyStore keystore = KeyStore.getInstance("PKCS12");
        keystore.load(new FileInputStream(keystorePath), password.toCharArray());

        FileInputStream fis = new FileInputStream(jpegPath);
        CreateVisibleSignature signing2 = new CreateVisibleSignature(keystore, password.toCharArray());
        signing2.setVisibleSignDesigner(filenameSigned1, 420, 300, -50, fis, 1);
        signing2.setVisibleSignatureProperties("name", "location", "Security", 0, 1, true);
        signing2.setExternalSigning(false);
        signing2.signPDF(new File(filenameSigned1), new File(filenameSigned2), null, "Signature1");
        fis.close();
	}
}
