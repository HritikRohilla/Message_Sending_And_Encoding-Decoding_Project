import javax.net.ssl.HttpsURLConnection;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;



public class SMS {

    private static SecretKeySpec secretKey;
    private static byte[] key;


    private JTextField textField1;



    private JTextField textField2;


    private JButton sendButton;
    private JPanel PanelMain;
    private JButton encodeButton;
    private JButton decodeButton;
    private JButton encodeWithOtherMethodButton;
    private JButton decodeWithOtherMethodButton;
    private JTextField EncodedMsg;
    private JTextField DecodedMsg;
    private JTextField StatusField;
    private JPasswordField KeyText;

    // Encryption Decryption code

    public static void setKey(String myKey)
    {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static String encrypt(String strToEncrypt, String secret)
    {
        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        }
        catch (Exception e)
        {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }

    public static String decrypt(String strToDecrypt, String secret)
    {
        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        }
        catch (Exception e)
        {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }


                                      //  URL code  //




    public SMS() {
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

        try
        {

            String apiKey="uMz5HFpjokWEJ6D0U2Pn4KGl7Rdxy8VYTvXAmbfe39thqcZBr1WwZDzgaj6NTU7SJ9VfqAvtuhsGdxYk";
            String sendId="TXTIND";
            //important step...
            String message = URLEncoder.encode(String.valueOf(textField2.getText()), StandardCharsets.UTF_8);
            System.out.println(message);
            String number = URLEncoder.encode(String.valueOf(textField1.getText()), StandardCharsets.UTF_8);
            System.out.println(number);
            String language="english";

            String route="v3";


            String myUrl="https://www.fast2sms.com/dev/bulkV2?authorization="+apiKey+"&sender_id="+sendId+"&message="+message+"&language="+language+"&route="+route+"&numbers="+number;

            //sending get request using java..

            URL url=new URL(myUrl);

            HttpsURLConnection con=(HttpsURLConnection)url.openConnection();


            con.setRequestMethod("GET");

            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            con.setRequestProperty("cache-control", "no-cache");
            System.out.println("Wait..............");

            int code=con.getResponseCode();

            System.out.println("Response code : "+code);

            if(code==200){
                StatusField.setText("Message Sent Successfuly");
            }
            else{
                StatusField.setText("Some error occured");
            }

            StringBuffer response=new StringBuffer();

            BufferedReader br=new BufferedReader(new InputStreamReader(con.getInputStream()));

            while(true)
            {
                String line=br.readLine();
                if(line==null)
                {
                    break;
                }
                response.append(line);
            }

            System.out.println(response);


        }catch (Exception a) {
            // TODO: handle exception
            a.printStackTrace();

    }}
        });
        encodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                final String secretKey = KeyText.getText();

                String originalString = textField2.getText();
                String encryptedString = SMS.encrypt(originalString, secretKey) ;


                System.out.println(originalString);
                EncodedMsg.setText(encryptedString);
                System.out.println(encryptedString);

            }
        });
        decodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                final String secretKey = KeyText.getText();

                String originalString = textField2.getText();
                String encryptedString = SMS.encrypt(originalString, secretKey) ;
                String decryptedString = SMS.decrypt(encryptedString, secretKey) ;

                System.out.println(originalString);
                DecodedMsg.setText(decryptedString);


            }
        });


        encodeWithOtherMethodButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String BasicBase64format
                        = Base64.getEncoder()
                        .encodeToString(textField2.getText().getBytes());

                EncodedMsg.setText(BasicBase64format);
            }
        });


        decodeWithOtherMethodButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                byte[] actualByte = Base64.getDecoder()
                        .decode(EncodedMsg.getText());

                String actualString = new String(actualByte);

                DecodedMsg.setText(actualString);
            }
        });
    }

    public static void main(String[] args) {



        JFrame frame = new JFrame("SMS");
        frame.setContentPane(new SMS().PanelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        System.out.println("Program started.....");


    }
}