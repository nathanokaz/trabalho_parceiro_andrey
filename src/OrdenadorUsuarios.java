import java.io.IOException;

public class OrdenadorUsuarios {

    public void ordenar(TabelaUsuarios usuarios, TabelaLivros livros) throws IOException {
        int totalUsuarios = usuarios.quantidadeUsuarios();
        int[] indices = new int[totalUsuarios];
        int[] aux = new int[totalUsuarios];


        for (int i = 0; i < totalUsuarios; i++) {
            indices[i] = i;
        }

        if (totalUsuarios > 1) {
            mergeSort(indices, aux, 0, totalUsuarios - 1, usuarios, livros);
        }

        GravadorCsv gravador = new GravadorCsv("usuario_melhor_livro_ordenado.csv");
        gravador.escreverCabecalhoUsuarios();

        for (int i = 0; i < totalUsuarios; i++) {
            gravador.escreverLinhaUsuario(usuarios.dados(indices[i]), livros);
        }
        gravador.fechar();
    }

    private void mergeSort(int[] indices, int[] aux, int inicio, int fim,TabelaUsuarios usuarios, TabelaLivros livros) {
        if (inicio >= fim) {
            return;
        }

        int meio = (inicio + fim) / 2;
        mergeSort(indices, aux, inicio, meio, usuarios, livros);
        mergeSort(indices, aux, meio + 1, fim, usuarios, livros);
        intercalar(indices, aux, inicio, meio, fim, usuarios, livros);
    }

    private void intercalar(int[] indices, int[] aux, int inicio, int meio, int fim,TabelaUsuarios usuarios, TabelaLivros livros) {
        for (int i = inicio; i <= fim; i++) {
            aux[i] = indices[i];
        }

        int indiceEsquerda = inicio;
        int indiceDireita = meio + 1;
        int indiceDestino = inicio;

        while (indiceEsquerda <= meio && indiceDireita <= fim) {
            if (vemAntes(aux[indiceEsquerda], aux[indiceDireita], usuarios, livros)) {
                indices[indiceDestino++] = aux[indiceEsquerda++];

            } else {
                indices[indiceDestino++] = aux[indiceDireita++];
            }
        }

        while (indiceEsquerda <= meio) {
            indices[indiceDestino++] = aux[indiceEsquerda++];
        }

        while (indiceDireita <= fim) {
            indices[indiceDestino++] = aux[indiceDireita++];
        }
    }

    private boolean vemAntes(int indiceUsuarioA, int indiceUsuarioB, TabelaUsuarios usuarios, TabelaLivros livros) {
        DadosUsuario usuarioA = usuarios.dados(indiceUsuarioA);
        DadosUsuario usuarioB = usuarios.dados(indiceUsuarioB);

        double somaA = livros.soma(usuarioA.melhorLivro);
        int quantidadeAvaliacoesA = livros.contagem(usuarioA.melhorLivro);
        double somaB = livros.soma(usuarioB.melhorLivro);
        int quantidadeAvaliacoesB = livros.contagem(usuarioB.melhorLivro);

        if (somaA * quantidadeAvaliacoesB > somaB * quantidadeAvaliacoesA) {
            return true;
        }
        if (somaA * quantidadeAvaliacoesB < somaB * quantidadeAvaliacoesA) {
            return false;
        }

        if (quantidadeAvaliacoesA > quantidadeAvaliacoesB) {
            return true;
        }

        if (quantidadeAvaliacoesA < quantidadeAvaliacoesB) {
            return false;
        }

        return UtilitariosTexto.compararOrdemAlfabetica(usuarioA.no.chave, usuarioA.no.tamanho, usuarioB.no.chave, usuarioB.no.tamanho) <= 0;
    }
}