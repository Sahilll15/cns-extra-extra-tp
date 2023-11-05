import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Scanner;

class RSA {
    public static void main(String args[]) {
        int p, q, n, z, d = 0, e, i;
        // The number to be encrypted and decrypted
        int msg ;
        Scanner scanner=new Scanner(System.in);
        System.out.println("Enter the number to be encrypted and decrypted");
        msg=scanner.nextInt();
        double c;
        BigInteger msgback;
        // 1st prime number p
         System.out.println("Enter the value of p");
         p=scanner.nextInt();
        // 2nd prime number q
        
         System.out.println("Enter the value of p");
        q=scanner.nextInt();
        n = p * q;
        z = (p - 1) * (q - 1);
        System.out.println("the value of z = " + z);
        for (e = 2; e < z; e++) {
            // e is for the public key exponent
            if (gcd(e, z) == 1) {
                System.out.println(e);
                break;
            }
        }

        System.out.println("the value of e = " + e);
        for (i = 0; i <= 9; i++) {
            int x = 1 + (i * z);
            // d is for the private key exponent
            if (x % e == 0) {
                d = x / e;
                System.out.println(d);
                break;
            }
        }
        System.out.println("the value of d = " + d);
        c = (Math.pow(msg, e)) % n;
        System.out.println("Encrypted message is : " + c);
        // Converting int value of n to BigInteger
        BigInteger N = BigInteger.valueOf(n);
        // Converting double value of c to BigInteger
        BigInteger C = BigDecimal.valueOf(c).toBigInteger();
        msgback = (C.pow(d)).mod(N);
        System.out.println("Decrypted message is : " + msgback);
    }

    static int gcd(int e, int z) {
        if (e == 0)
            return z;
        else
            return gcd(z % e, e);
    }
}
