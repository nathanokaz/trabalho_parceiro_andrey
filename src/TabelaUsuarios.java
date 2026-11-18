public class TabelaUsuarios {

    private TabelaHash tabela;
    private DadosUsuario[] dados;
    private int capacidadeVetores;

    public TabelaUsuarios(int capacidadeTabela, int tipoFuncao) {
        this.tabela = new TabelaHash(capacidadeTabela, tipoFuncao);
        this.capacidadeVetores = 1024;
        this.dados = new DadosUsuario[capacidadeVetores];
    }

    // Registra uma avaliação válida do usuário e atualiza o melhor livro dele.
    public void registrar(char[] usuario, int tamanhoUsuario,
                          char[] nome, int tamanhoNome,
                          int livro, double nota, TabelaLivros livros) {

        int proximoIndice = tabela.getQuantidadeChaves();

        if (proximoIndice == capacidadeVetores) {
            aumentarVetores();
        }

        No no = tabela.inserir(usuario, tamanhoUsuario, proximoIndice);
        int indice = no.indice;

        if (indice == proximoIndice) {          // usuário novo
            dados[indice] = new DadosUsuario(no);
        }

        DadosUsuario d = dados[indice];

        // profileName: vale o primeiro não vazio
        if (d.tamanhoNome == 0 && tamanhoNome > 0) {
            d.nome = UtilitariosTexto.copiarTexto(nome, tamanhoNome);
            d.tamanhoNome = tamanhoNome;
        }

        // melhor livro
        if (d.melhorLivro == -1 || livros.eMelhor(livro, d.melhorLivro)) {
            d.melhorLivro = livro;
            d.notaUsuario = nota;
        } else if (livro == d.melhorLivro) {
            if (nota > d.notaUsuario) {
                d.notaUsuario = nota;           // mesma avaliação repetida: vale a maior
            }
        }
    }

    private void aumentarVetores() {
        int novaCapacidade = capacidadeVetores * 2;
        DadosUsuario[] novosDados = new DadosUsuario[novaCapacidade];

        for (int i = 0; i < capacidadeVetores; i++) {
            novosDados[i] = dados[i];
        }

        dados = novosDados;
        capacidadeVetores = novaCapacidade;
    }

    public int quantidadeUsuarios() {
        return tabela.getQuantidadeChaves();
    }

    public DadosUsuario dados(int indice) {
        return dados[indice];
    }

    // Devolve os dados do usuário, ou null se não existir (usado na consulta, RF6).
    public DadosUsuario buscar(char[] usuario, int tamanho) {
        No no = tabela.buscar(usuario, tamanho);
        if (no == null) {
            return null;
        }
        return dados[no.indice];
    }
}