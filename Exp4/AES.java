import javax.crypto.Cipher;  
import javax.crypto.SecretKey;  
import javax.crypto.SecretKeyFactory;  
import javax.crypto.spec.IvParameterSpec;  
import javax.crypto.spec.PBEKeySpec;  
import javax.crypto.spec.SecretKeySpec;  
import java.nio.charset.StandardCharsets;  
import java.security.InvalidAlgorithmParameterException;  
import java.security.InvalidKeyException;  
import java.security.NoSuchAlgorithmException;  
import java.security.spec.InvalidKeySpecException;  
import java.security.spec.KeySpec;  
import java.util.Base64;
import java.util.Scanner;

import javax.crypto.BadPaddingException;  
import javax.crypto.IllegalBlockSizeException;  
import javax.crypto.NoSuchPaddingException;  

public class AES {  

     
  /* Encryption Method */  
  public static String encrypt(String strToEncrypt,String SECRET_KEY, String SALTVALUE) {  
    try {  
      /* Declare a byte array. */  
      byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};  
      IvParameterSpec ivspec = new IvParameterSpec(iv);        
      /* Create factory for secret keys. */  
      SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");  
      /* PBEKeySpec class implements KeySpec interface. */  
      KeySpec spec = new PBEKeySpec(SECRET_KEY.toCharArray(), SALTVALUE.getBytes(), 65536, 256);  
      SecretKey tmp = factory.generateSecret(spec);  
      SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");  
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");  
      cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);  
      /* Returns encrypted value. */  
      return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));  
    } catch (InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e) {  
      System.out.println("Error occurred during encryption: " + e.toString());  
    }  
    return null;  
  }  
    
  /* Decryption Method */  
  public static String decrypt(String strToDecrypt,String SECRET_KEY, String SALTVALUE) {  
    try {  
      /* Declare a byte array. */  
      byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};  
      IvParameterSpec ivspec = new IvParameterSpec(iv);  
      /* Create factory for secret keys. */  
      SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");  
      /* PBEKeySpec class implements KeySpec interface. */  
      KeySpec spec = new PBEKeySpec(SECRET_KEY.toCharArray(), SALTVALUE.getBytes(), 65536, 256);  
      SecretKey tmp = factory.generateSecret(spec);  
      SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");  
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");  
      cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);  
      /* Returns decrypted value. */  
      return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));  
    } catch (InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e) {  
      System.out.println("Error occurred during decryption: " + e.toString());  
    }  
    return null;  
  }  
  
  /* Driver Code */  
  public static void main(String[] args) {  
    /* Message to be encrypted. */  

        Scanner scanner =new Scanner(System.in);
        
  System.out.println("Enter the key to be encrypted");
  
  //  String SECRET_KEY = "123456789";  
   String SECRET_KEY =scanner.nextLine();  
     System.out.println("Enter the salt to be encrypted");
  //  String SALTVALUE = "abcdefg";  
   String SALTVALUE =scanner.nextLine();  



    System.out.println("Enter the message to be encrypted");

    
    String originalval = scanner.nextLine(); 

    /* Call the encrypt() method and store result of encryption. */  
    String encryptedval = encrypt(originalval,SECRET_KEY,SALTVALUE);  
    /* Call the decrypt() method and store result of decryption. */  
    String decryptedval = decrypt(encryptedval,SECRET_KEY,SALTVALUE);  
    /* Display the original message, encrypted message and decrypted message on the console. */  
    System.out.println("Original value: " + originalval);  
    System.out.println("Encrypted value: " + encryptedval);  
    System.out.println("Decrypted value: " + decryptedval);  
  }  
}