
package tratar;

import java.net.DatagramPacket;

public class Tratamento {

    public static String trataEntrada(byte[] buf) {
        int c = 0;
        for (byte i : buf) {
            if (i != 0) {
                c++;
            }
        }
        byte[] num = new byte[c];
        int count = 0;
        for (byte i : buf) {
            if (i != 0) {
                num[count] = i;
                count++;
            }
        }
        DatagramPacket dp = new DatagramPacket(num, num.length);
        String saida = new String(dp.getData());
        System.out.println("tratar.Tratamento.java.trataEntrada(): tratado: " + saida);
        return saida;
    }
}
