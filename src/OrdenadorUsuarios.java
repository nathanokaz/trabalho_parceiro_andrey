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
        gravador.escreverTextoFixo("User_id,profileName,Title,media_livro,N_livro,nota_usuario");
        gravador.escreverFimDeLinha();

        for (int i = 0; i < totalUsuarios; i++) {
            DadosUsuario dadosUsuario = usuarios.dados(indices[i]);
            int livro = dadosUsuario.melhorLivro;
            No noLivro = livros.livro(livro);
            int quantidadeAvaliacoes = livros.contagem(livro);
            double media = livros.soma(livro) / quantidadeAvaliacoes;

            gravador.escreverCampo(dadosUsuario.no.chave, dadosUsuario.no.tamanho);
            gravador.escreverVirgula();
            gravador.escreverCampo(dadosUsuario.nome, dadosUsuario.tamanhoNome);
            gravador.escreverVirgula();
            gravador.escreverCampo(noLivro.chave, noLivro.tamanho);
            gravador.escreverVirgula();
            gravador.escreverDecimal(media, 4);
            gravador.escreverVirgula();
            gravador.escreverInteiro(quantidadeAvaliacoes);
            gravador.escreverVirgula();
            gravador.escreverDecimal(dadosUsuario.notaUsuario, 1);
            gravador.escreverFimDeLinha();
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

        return compararTexto(usuarioA.no.chave, usuarioA.no.tamanho, usuarioB.no.chave, usuarioB.no.tamanho) <= 0;
    }

    private int compararTexto(char[] textoA, int tamanhoA, char[] textoB, int tamanhoB) {
        int menor;

        if (tamanhoA < tamanhoB) {
            menor = tamanhoA;
        } else {
            menor = tamanhoB;
        }

        for (int i = 0; i < menor; i++) {
            if (textoA[i] != textoB[i]) {
                return textoA[i] - textoB[i];

            }
        }


        return tamanhoA - tamanhoB;
    }
}