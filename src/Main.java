import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        String caminho = "Books_rating.csv";
        int capacidade = 1000003;

        TabelaLivros livros = new TabelaLivros(capacidade, TabelaHash.FNV1A);
        TabelaUsuarios usuarios = new TabelaUsuarios(capacidade, TabelaHash.FNV1A);

        int linhasLidas = 0;
        int linhasValidas = 0;
        int descartadasSemUsuario = 0;
        int descartadasSemTitulo = 0;
        int descartadasNotaInvalida = 0;

        long inicioLeituraLivros = System.nanoTime();

        LeitorCsv leitor = new LeitorCsv(caminho);
        pularCabecalho(leitor);

        while (leitor.temMaisDados()) {
            leitor.lerAvaliacao();

            linhasLidas++;

            if (leitor.tamanhoUsuario == 0) {
                descartadasSemUsuario++;
            } else if (leitor.tamanhoTitulo == 0) {
                descartadasSemTitulo++;
            } else if (leitor.nota < 1.0 || leitor.nota > 5.0) {
                descartadasNotaInvalida++;
            } else {
                livros.registrar(leitor.titulo, leitor.tamanhoTitulo, leitor.nota);
                linhasValidas++;
            }
        }

        leitor.fechar();

        long finalLeituraLivros = System.nanoTime();
        long tempoLeituraLivros = (finalLeituraLivros - inicioLeituraLivros) / 1000000;

        long inicioLeituramelhorLivro = System.nanoTime();

        leitor = new LeitorCsv(caminho);
        pularCabecalho(leitor);

        while (leitor.temMaisDados()) {
            leitor.lerAvaliacao();

            if (leitor.tamanhoUsuario > 0 && leitor.tamanhoTitulo > 0 && leitor.nota >= 1.0 && leitor.nota <= 5.0) {
                int livro = livros.buscarIndice(leitor.titulo, leitor.tamanhoTitulo);
                usuarios.registrar(leitor.usuario, leitor.tamanhoUsuario, leitor.nome, leitor.tamanhoNome, livro, leitor.nota, livros);
            }
        }

        leitor.fechar();

        long fimLeituraMelhorLivro = System.nanoTime();
        long tempoLeituraMelhorLivro = (fimLeituraMelhorLivro - inicioLeituramelhorLivro) / 1000000;

        System.out.println("Linhas lidas: " + linhasLidas);
        System.out.println("Linhas validas: " + linhasValidas);
        System.out.println("Descartadas sem User_id: " + descartadasSemUsuario);
        System.out.println("Descartadas sem Title: " + descartadasSemTitulo);
        System.out.println("Descartadas com nota invalida: " + descartadasNotaInvalida);
        System.out.println("Livros distintos: " + livros.quantidadeLivros());
        System.out.println("Usuarios distintos: " + usuarios.quantidadeUsuarios());
        System.out.println();

        // Acréscimo referente ao tópico RF5 (5.3)
        ExperimentoHash.executar(livros, usuarios, capacidade);
        System.out.println();

        long inicioGravacaoDados = System.nanoTime();

        GravadorCsv gravador = new GravadorCsv("usuario_melhor_livro.csv");
        gravador.escreverCabecalhoUsuarios();

        for (int i = 0; i < usuarios.quantidadeUsuarios(); i++) {
            DadosUsuario d = usuarios.dados(i);
            gravador.escreverLinhaUsuario(d, livros);

            if (i < 10) {
                int livro = d.melhorLivro;
                No noLivro = livros.livro(livro);
                int n = livros.contagem(livro);
                double media = livros.soma(livro) / n;

                UtilitariosTexto.imprimir(d.no.chave, d.no.tamanho);
                System.out.print(" | ");
                UtilitariosTexto.imprimir(d.nome, d.tamanhoNome);
                System.out.print(" | ");
                UtilitariosTexto.imprimir(noLivro.chave, noLivro.tamanho);
                System.out.println(" | media: " + media + " | N: " + n + " | nota_usuario: " + d.notaUsuario);
            }
        }

        gravador.fechar();

        long fimGravacaodados = System.nanoTime();
        long tempoGravacaoDados = (fimGravacaodados - inicioGravacaoDados) / 1000000;

        System.out.println();

        long inicioConsultas = System.nanoTime();

        // Acréscimo referente ao tópico RF6
        ConsultaUsuarios.executar("user_id.txt", usuarios, livros);

        long fimConsultas = System.nanoTime();
        long tempoConsultas = (fimConsultas - inicioConsultas) / 1000000;


        System.out.println();

        long inicioRankingLivros = System.nanoTime();
        // Acréscimo referente ao tópico RF7
        RankingLivros.executar(livros);

        long fimRankingLivros = System.nanoTime();
        long tempoRankingLivros = (fimRankingLivros - inicioRankingLivros) / 1000000;

        long inicioOrdUsuarios = System.nanoTime();

        OrdenadorUsuarios ordenadorUsuarios = new OrdenadorUsuarios();
        ordenadorUsuarios.ordenar(usuarios,livros);

        long fimOrdenaçãoUsuarios = System.nanoTime();
        long tempoOrdenacaoUsuarios = (fimOrdenaçãoUsuarios - inicioOrdUsuarios) / 1000000;

        System.out.println();

        System.out.println("--------------------- tempos de execução ---------------------");
        System.out.println("tempo de execução da leitura dos livros:  " + tempoLeituraLivros);
        System.out.println("tempo execução da leitura melhor livro: " + tempoLeituraMelhorLivro);
        System.out.println("tempo de execução da gravação dos dados: " + tempoGravacaoDados);
        System.out.println("tempo das consultas: " + tempoConsultas);
        System.out.println("tempo rankeamento dos livros: " + tempoRankingLivros);
        System.out.println("tempo de ordenção dos usuários para gerar arquivo: " + tempoOrdenacaoUsuarios);
    }

    static void pularCabecalho(LeitorCsv leitor) throws IOException {
        leitor.lerCampo(null, 0);

        while (!leitor.terminouLinha()) {
            leitor.lerCampo(null, 0);
        }
    }
}