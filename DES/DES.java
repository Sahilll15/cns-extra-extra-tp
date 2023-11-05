import java.util.Arrays;

public class DES {

    private static String[] roundKeys = new String[16];
    private static String plainText;

    public static void main(String[] args) {
        String key = "1010101010111011000010010001100000100111001101101100110011011101";
        plainText = "1010101111001101111001101010101111001101000100110010010100110110";
        System.out.println("Plain text: " + plainText);

        generateKeys(key);

        String cipherText = encrypt();
        System.out.println("Cipher text: " + cipherText);

        // Reverse the roundKeys array for decryption
        for (int i = 0; i < 8; i++) {
            String temp = roundKeys[i];
            roundKeys[i] = roundKeys[15 - i];
            roundKeys[15 - i] = temp;
        }

        plainText = cipherText;
        String decryptedText = decrypt();
        System.out.println("Decrypted text: " + decryptedText);

        if (decryptedText.equals(plainText)) {
            System.out.println("Plain text encrypted and decrypted successfully.");
        }
    }

    private static void generateKeys(String key) {
        int[] pc1 = {
            57, 49, 41, 33, 25, 17, 9, 1,
            58, 50, 42, 34, 26, 18, 10, 2,
            59, 51, 43, 35, 27, 19, 11, 3,
            60, 52, 44, 36, 63, 55, 47, 39,
            31, 23, 15, 7, 62, 54, 46, 38,
            30, 22, 14, 6, 61, 53, 45, 37,
            29, 21, 13, 5, 28, 20, 12, 4
        };

        int[] pc2 = {
            14, 17, 11, 24, 1, 5, 3, 28,
            15, 6, 21, 10, 23, 19, 12, 4,
            26, 8, 16, 7, 27, 20, 13, 2,
            41, 52, 31, 37, 47, 55, 30, 40,
            51, 45, 33, 48, 44, 49, 39, 56,
            34, 53, 46, 42, 50, 36, 29, 32
        };

        String permKey = "";
        for (int i = 0; i < pc1.length; i++) {
            permKey += key.charAt(pc1[i] - 1);
        }

        String left = permKey.substring(0, 28);
        String right = permKey.substring(28);

        for (int i = 0; i < 16; i++) {
            int shiftCount = (i == 0 || i == 1 || i == 8 || i == 15) ? 1 : 2;
            left = shiftLeft(left, shiftCount);
            right = shiftLeft(right, shiftCount);
            String combinedKey = left + right;
            String roundKey = "";
            for (int j = 0; j < pc2.length; j++) {
                roundKey += combinedKey.charAt(pc2[j] - 1);
            }
            roundKeys[i] = roundKey;
        }
    }

    private static String shiftLeft(String keyChunk, int shiftCount) {
        return keyChunk.substring(shiftCount) + keyChunk.substring(0, shiftCount);
    }

    private static String encrypt() {
        int[] initialPermutation = {
            58, 50, 42, 34, 26, 18, 10, 2,
            60, 52, 44, 36, 28, 20, 12, 4,
            62, 54, 46, 38, 30, 22, 14, 6,
            64, 56, 48, 40, 32, 24, 16, 8,
            57, 49, 41, 33, 25, 17, 9, 1,
            59, 51, 43, 35, 27, 19, 11, 3,
            61, 53, 45, 37, 29, 21, 13, 5,
            63, 55, 47, 39, 31, 23, 15, 7
        };

        int[] expansionTable = {
            32, 1, 2, 3, 4, 5, 4, 5,
            6, 7, 8, 9, 8, 9, 10, 11,
            12, 13, 12, 13, 14, 15, 16, 17,
            16, 17, 18, 19, 20, 21, 20, 21,
            22, 23, 24, 25, 24, 25, 26, 27,
            28, 29, 28, 29, 30, 31, 32, 1
        };

        int[][][] substitutionBoxes = {
            {
                {14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7},
                {0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8},
                {4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0},
                {15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13}
            },
            {
                {15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10},
                {3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10,
