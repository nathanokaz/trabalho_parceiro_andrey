public class RankingLivros {

    public static void executar(TabelaLivros livros) {
        int[] top = new int[10];
        int quantidade = 0;

        for (int i = 0; i < livros.quantidadeLivros(); i++) {
            if (livros.contagem(i) >= 100) {
                int posicao = quantidade;

                for (int p = 0; p < quantidade; p++) {
                    if (livros.eMelhor(i, top[p])) {
                        posicao = p;
                        break;
                    }
                }

                if (posicao < 10) {
                    if (quantidade < 10) {
                        quantidade++;
                    }
                    for (int j = quantidade - 1; j > posicao; j--) {
                        top[j] = top[j - 1];
                    }
                    top[posicao] = i;
                }
            }
        }

        System.out.println("| Ranking Top 10 Livros (N >= 100) |");

        if (quantidade == 0) {
            System.out.println("Nenhum livro possui 100 ou mais avaliacoes.");
            return;
        }

        for (int i = 0; i < quantidade; i++) {
            int indiceLivro = top[i];
            No noLivro = livros.livro(indiceLivro);
            int n = livros.contagem(indiceLivro);

            System.out.print((i + 1) + ". ");
            UtilitariosTexto.imprimir(noLivro.chave, noLivro.tamanho);
            System.out.print(" | media: ");
            UtilitariosTexto.imprimirDecimal(livros.soma(indiceLivro) / n, 4);
            System.out.println(" | N: " + n);
        }
    }
}