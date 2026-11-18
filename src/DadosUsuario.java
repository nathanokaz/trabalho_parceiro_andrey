public class DadosUsuario {
    public No no;
    public char[] nome;
    public int tamanhoNome;
    public int melhorLivro;
    public double notaUsuario;

    public DadosUsuario(No no) {
        this.no = no;
        this.nome = null;
        this.tamanhoNome = 0;
        this.melhorLivro = -1;
        this.notaUsuario = 0.0;
    }
}