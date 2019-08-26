package encryption;

public class AES {
	
	private static String AESPrivateKey = "1234567891234567";
	
	public static String getAESPrivateKey() {
		return AESPrivateKey;
	}
	
	public static void setAESPrivateKey(String key) {
		AESPrivateKey = key;
	}
	
}
