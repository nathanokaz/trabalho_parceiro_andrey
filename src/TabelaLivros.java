public class TabelaLivros {

    private TabelaHash tabela;
    private No[] nos;
    private double[] somas;
    private int[] contagens;
    private int capacidadeVetores;

    public TabelaLivros(int capacidadeTabela, int tipoFuncao) {
        this.tabela = new TabelaHash(capacidadeTabela, tipoFuncao);
        this.capacidadeVetores = 1024;
        this.nos = new No[capacidadeVetores];
        this.somas = new double[capacidadeVetores];
        this.contagens = new int[capacidadeVetores];
    }


    public void registrar(char[] titulo, int tamanho, double nota) {
        int proximoIndice = tabela.getQuantidadeChaves();

        if (proximoIndice == capacidadeVetores) {
            aumentarVetores();
        }

        No no = tabela.inserir(titulo, tamanho, proximoIndice);
        int indice = no.indice;

        if (indice == proximoIndice) {
            nos[indice] = no;
        }

        somas[indice] = somas[indice] + nota;
        contagens[indice]++;
    }

    private void aumentarVetores() {
        int novaCapacidade = capacidadeVetores * 2;
        No[] novosNos = new No[novaCapacidade];
        double[] novasSomas = new double[novaCapacidade];
        int[] novasContagens = new int[novaCapacidade];

        for (int i = 0; i < capacidadeVetores; i++) {
            novosNos[i] = nos[i];
            novasSomas[i] = somas[i];
            novasContagens[i] = contagens[i];
        }

        nos = novosNos;
        somas = novasSomas;
        contagens = novasContagens;
        capacidadeVetores = novaCapacidade;
    }

    public int quantidadeLivros() {
        return tabela.getQuantidadeChaves();
    }

    public No livro(int indice) {
        return nos[indice];
    }

    public double soma(int indice) {
        return somas[indice];
    }

    public int contagem(int indice) {
        return contagens[indice];
    }

    public int buscarIndice(char[] titulo, int tamanho) {
        No no = tabela.buscar(titulo, tamanho);
        if (no == null) {
            return -1;
        }
        return no.indice;
    }


    public boolean eMelhor(int a, int b) {
        double esquerda = somas[a] * contagens[b];
        double direita = somas[b] * contagens[a];

        if (esquerda != direita) {
            return esquerda > direita;
        }

        if (contagens[a] != contagens[b]) {
            return contagens[a] > contagens[b];
        }

        return UtilitariosTexto.compararOrdemAlfabetica(
                nos[a].chave, nos[a].tamanho, nos[b].chave, nos[b].tamanho) < 0;
    }
}