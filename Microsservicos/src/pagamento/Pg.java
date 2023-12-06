package pagamento;

import java.io.IOException;

/**
 *
 * @author pedro
 */
public class Pg {

    public static void main(String[] args) throws IOException {
        PagamentoController pc = new PagamentoController();
        pc.start();
    }
    
}
