public class No {
    public char[] chave;
    public int tamanho;
    public int indice;
    public No proximo;

    public No(char[] chave, int tamanho, int indice, No proximo) {
        this.chave = chave;
        this.tamanho = tamanho;
        this.indice = indice;
        this.proximo = proximo;
    }
}
