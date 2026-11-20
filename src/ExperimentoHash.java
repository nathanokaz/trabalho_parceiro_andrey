public class ExperimentoHash {

    public static void executar(TabelaLivros livros, TabelaUsuarios usuarios, int capacidade) {
        int totalLivros = livros.quantidadeLivros();
        No[] chavesTitulos = new No[totalLivros];
        for (int i = 0; i < totalLivros; i++) {
            chavesTitulos[i] = livros.livro(i);
        }

        int totalUsuarios = usuarios.quantidadeUsuarios();
        No[] chavesUsuarios = new No[totalUsuarios];
        for (int i = 0; i < totalUsuarios; i++) {
            chavesUsuarios[i] = usuarios.dados(i).no;
        }

        System.out.println("| Parte Experimental |");
        System.out.println();

        executarConjunto("Usuarios", chavesUsuarios, totalUsuarios, capacidade);
        System.out.println();
        executarConjunto("Titulos", chavesTitulos, totalLivros, capacidade);
    }

    private static void executarConjunto(String nomeConjunto, No[] chaves, int totalChaves, int capacidade) {
        int[] funcoes = {TabelaHash.FNV1A, TabelaHash.POLINOMIAL31, TabelaHash.DJB2};
        String[] nomesFuncoes = {"FNV1a        ", "Polinomial 31", "djb2         "};
        char[] chaveInexistente = new char[2005];

        System.out.println("Tabela de resultados - " + nomeConjunto + " (" + totalChaves + " chaves distintas)");
        System.out.println("Funcao        | Colisoes | Sond. Sucesso | Sond. Falha | Pior Caso | Tempo Ins. (ms) | Tempo Busca (ms)");
        System.out.println("---------------------------------------------------------------------------------------------------------");

        for (int f = 0; f < 3; f++) {
            int colisoes = 0;
            int piorCaso = 0;
            double mediaSondagensSucesso = 0.0;
            double mediaSondagensFalha = 0.0;
            double somaTempoInsercao = 0.0;
            double somaTempoBusca = 0.0;

            for (int execucao = 0; execucao < 3; execucao++) {
                TabelaHash tabela = new TabelaHash(capacidade, funcoes[f]);

                // Realiza a inserção de todas as chaves distintas, uma vez cada
                long inicioInsercao = System.nanoTime();
                for (int i = 0; i < totalChaves; i++) {
                    tabela.inserir(chaves[i].chave, chaves[i].tamanho, i);
                }
                somaTempoInsercao += (System.nanoTime() - inicioInsercao) / 1000000.0;

                // Faz a busca com sucesso (cada chave inserida uma vez)
                double totalSondagensSucesso = 0.0;
                long inicioBusca = System.nanoTime();
                for (int i = 0; i < totalChaves; i++) {
                    tabela.buscar(chaves[i].chave, chaves[i].tamanho);
                    totalSondagensSucesso += tabela.sondagensUltimaOperacao;
                }
                somaTempoBusca += (System.nanoTime() - inicioBusca) / 1000000.0;

                // Faz a busca sem sucesso (chave + caractere extra de codigo 1 ao final)
                double totalSondagensFalha = 0.0;
                for (int i = 0; i < totalChaves; i++) {
                    No atual = chaves[i];
                    for (int j = 0; j < atual.tamanho; j++) {
                        chaveInexistente[j] = atual.chave[j];
                    }
                    chaveInexistente[atual.tamanho] = (char) 1;

                    tabela.buscar(chaveInexistente, atual.tamanho + 1);
                    totalSondagensFalha += tabela.sondagensUltimaOperacao;
                }

                // As contagens estruturais são determinístas nas três execuções
                if (execucao == 0) {
                    colisoes = tabela.colisoes;
                    piorCaso = tabela.piorCaso;
                    if (totalChaves > 0) {
                        mediaSondagensSucesso = totalSondagensSucesso / totalChaves;
                        mediaSondagensFalha = totalSondagensFalha / totalChaves;
                    }
                }
            }

            System.out.print(nomesFuncoes[f] + " | " + colisoes + " | ");
            UtilitariosTexto.imprimirDecimal(mediaSondagensSucesso, 4);
            System.out.print(" | ");
            UtilitariosTexto.imprimirDecimal(mediaSondagensFalha, 4);
            System.out.print(" | " + piorCaso + " | ");
            UtilitariosTexto.imprimirDecimal(somaTempoInsercao / 3.0, 4);
            System.out.print(" | ");
            UtilitariosTexto.imprimirDecimal(somaTempoBusca / 3.0, 4);
            System.out.println();
        }
    }
}