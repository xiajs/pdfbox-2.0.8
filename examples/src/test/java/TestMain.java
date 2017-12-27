import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.util.Enumeration;

public class TestMain {

	static String rootPath = "D:\\GitHub\\MavenDemo\\khbz-parent\\khbz-app\\src\\test\\resources\\keystore.p12";
	
	
	public static void main(String[] args) throws Exception {
		//rootPath = "src/test/resources/org/apache/pdfbox/examples/signature/keystore.p12";
		// PKCS12
		KeyStore ks = KeyStore.getInstance("PKCS12");
		InputStream is = null;
		try {
			is = new java.io.FileInputStream(rootPath);
			String password = "123456";//
			ks.load(is, password.toCharArray());
			Enumeration<String> e = ks.aliases();
			while (e.hasMoreElements()) {
				String alias = e.nextElement();
				System.out.println(alias);
				 java.security.cert.Certificate c = ks.getCertificate(alias);
				 System.out.println("加载证书" + c.toString());
				 System.out.println(ks.getKey(alias, password.toCharArray()));
			}
		} finally {
			if (is != null) {
				is.close();
			}
		}
	}

}
