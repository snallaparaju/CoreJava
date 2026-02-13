package encryptdecrypt;

public class Main {
    public static void main(String[] args) {

        String message = "we found a treasure!";
        StringBuilder cipherText = new StringBuilder();

        for (int i = 0; i < message.length(); i++) {
            char ch = message.charAt(i);

            if (ch >= 'a' && ch <= 'z') {
                // Atbash cipher logic
                char encryptedChar = (char) ('z' - (ch - 'a'));
                cipherText.append(encryptedChar);
            } else {
                // Keep spaces and special characters unchanged
                cipherText.append(ch);
            }
        }

        System.out.println(cipherText);
    }
}

