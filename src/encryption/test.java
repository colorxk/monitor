package encryption;

import java.io.UnsupportedEncodingException;

public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String content = "������";
        String password = "123";
        System.out.println("����֮ǰ��" + content);
        // ����
        byte[] encrypt = AESEn.encrypt(content, password);
        System.out.println("���ܺ�����ݣ�" + encrypt);
        System.out.println("���ܺ�����ݣ�" + new String(encrypt));
        for(int i=0;i<encrypt.length;i++) {
        	System.out.print(encrypt[i]+" ");
        }
        System.out.println();
        
        // ����
        byte[] decrypt = AESEn.decrypt(encrypt, password);
        try {
			System.out.println("���ܺ�����ݣ�" + new String(decrypt,"utf-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    
	}

}
