package pagamento;

/**
 *
 * @author pedro
 */
public class Produto {

    private int quantidade;
    private final float preco;
    private final String nome;

    public Produto(float preco, String nome) {
        this.quantidade = 1;
        this.preco = preco;
        this.nome = nome;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public float getPreco() {
        return preco;
    }

    public String getNome() {
        return nome;
    }

    public void addQuantidade() {
        this.quantidade++;
    }
    
    

}
