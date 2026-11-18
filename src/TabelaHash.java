public class TabelaHash {

    public static final int FNV1A = 0;
    public static final int POLINOMIAL31 = 1;
    public static final int DJB2 = 2;

    private No[] posicoes;
    private int capacidade;
    private int tipoFuncao;
    private int quantidadeChaves;

    public int colisoes;
    public int sondagensUltimaOperacao;
    public int piorCaso;

    public TabelaHash(int capacidade, int tipoFuncao) {
        this.capacidade = capacidade;
        this.tipoFuncao = tipoFuncao;
        this.posicoes = new No[capacidade];
        this.quantidadeChaves = 0;
        this.colisoes = 0;
        this.piorCaso = 0;
    }

    private int calcularHash(char[] chave, int tamanho) {
        if (tipoFuncao == FNV1A) {
            return FuncoesHash.hashFnv1a(chave, tamanho);
        } else if (tipoFuncao == POLINOMIAL31) {
            return FuncoesHash.hashPolinomialBase31(chave, tamanho);
        }
        return FuncoesHash.hashDjb2(chave, tamanho);
    }


    public No buscar(char[] chave, int tamanho) {
        int posicao = posicaoDaChave(chave, tamanho);
        int sondagens = 0;
        No atual = posicoes[posicao];

        while (atual != null) {
            sondagens++;
            if (UtilitariosTexto.saoIguais(atual.chave, atual.tamanho, chave, tamanho)) {
                break;
            }
            atual = atual.proximo;
        }

        sondagensUltimaOperacao = sondagens;
        if (sondagens > piorCaso) {
            piorCaso = sondagens;
        }
        return atual;
    }


    public No inserir(char[] chave, int tamanho, int indice) {
        No existente = buscar(chave, tamanho);
        if (existente != null) {
            return existente;
        }

        int posicao = posicaoDaChave(chave, tamanho);
        if (posicoes[posicao] != null) {
            colisoes++;
        }

        char[] copia = UtilitariosTexto.copiarTexto(chave, tamanho);
        posicoes[posicao] = new No(copia, tamanho, indice, posicoes[posicao]);
        quantidadeChaves++;
        return posicoes[posicao];
    }

    public int getQuantidadeChaves() {
        return quantidadeChaves;
    }

    private int posicaoDaChave(char[] chave, int tamanho) {
        return FuncoesHash.calcularPosicao(calcularHash(chave, tamanho), capacidade);
    }
}