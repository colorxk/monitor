package encryption;

import java.io.UnsupportedEncodingException;

public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String content = "张晓坤";
        String password = "123";
        System.out.println("加密之前：" + content);
        // 加密
        byte[] encrypt = AESEn.encrypt(content, password);
        System.out.println("加密后的内容：" + encrypt);
        System.out.println("加密后的内容：" + new String(encrypt));
        for(int i=0;i<encrypt.length;i++) {
        	System.out.print(encrypt[i]+" ");
        }
        System.out.println();
        
        // 解密
        byte[] decrypt = AESEn.decrypt(encrypt, password);
        try {
			System.out.println("解密后的内容：" + new String(decrypt,"utf-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    
	}

}
