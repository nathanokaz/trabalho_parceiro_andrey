import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        String caminho = "teste.csv";
        int capacidade = 1000003;

        char[] titulo = new char[2000];
        char[] usuario = new char[200];
        char[] nome = new char[500];
        char[] nota = new char[20];

        TabelaLivros livros = new TabelaLivros(capacidade, TabelaHash.FNV1A);
        TabelaUsuarios usuarios = new TabelaUsuarios(capacidade, TabelaHash.FNV1A);

        int linhasLidas = 0;
        int linhasValidas = 0;
        int descartadasSemUsuario = 0;
        int descartadasSemTitulo = 0;
        int descartadasNotaInvalida = 0;

        LeitorCsv leitor = new LeitorCsv(caminho);
        pularCabecalho(leitor);

        while (leitor.temMaisDados()) {
            leitor.lerCampo(null, 0);
            int tamanhoTitulo = leitor.lerCampo(titulo, 2000);
            leitor.lerCampo(null, 0);
            int tamanhoUsuario = leitor.lerCampo(usuario, 200);
            leitor.lerCampo(null, 0);
            leitor.lerCampo(null, 0);
            int tamanhoNota = leitor.lerCampo(nota, 20);
            leitor.lerCampo(null, 0);
            leitor.lerCampo(null, 0);
            leitor.lerCampo(null, 0);

            linhasLidas++;

            double notaConvertida = UtilitariosTexto.converterParaDouble(nota, tamanhoNota);

            if (tamanhoUsuario == 0) {
                descartadasSemUsuario++;
            } else if (tamanhoTitulo == 0) {
                descartadasSemTitulo++;
            } else if (notaConvertida < 1.0 || notaConvertida > 5.0) {
                descartadasNotaInvalida++;
            } else {
                livros.registrar(titulo, tamanhoTitulo, notaConvertida);
                linhasValidas++;
            }
        }

        leitor.fechar();

        leitor = new LeitorCsv(caminho);
        pularCabecalho(leitor);

        while (leitor.temMaisDados()) {
            leitor.lerCampo(null, 0);
            int tamanhoTitulo = leitor.lerCampo(titulo, 2000);
            leitor.lerCampo(null, 0);
            int tamanhoUsuario = leitor.lerCampo(usuario, 200);
            int tamanhoNome = leitor.lerCampo(nome, 500);
            leitor.lerCampo(null, 0);
            int tamanhoNota = leitor.lerCampo(nota, 20);
            leitor.lerCampo(null, 0);
            leitor.lerCampo(null, 0);
            leitor.lerCampo(null, 0);

            double notaConvertida = UtilitariosTexto.converterParaDouble(nota, tamanhoNota);

            boolean valida = tamanhoUsuario > 0 && tamanhoTitulo > 0
                    && notaConvertida >= 1.0 && notaConvertida <= 5.0;

            if (valida) {
                int livro = livros.buscarIndice(titulo, tamanhoTitulo);
                usuarios.registrar(usuario, tamanhoUsuario, nome, tamanhoNome, livro, notaConvertida, livros);
            }
        }

        leitor.fechar();

        System.out.println("Linhas lidas: " + linhasLidas);
        System.out.println("Linhas validas: " + linhasValidas);
        System.out.println("Descartadas sem User_id: " + descartadasSemUsuario);
        System.out.println("Descartadas sem Title: " + descartadasSemTitulo);
        System.out.println("Descartadas com nota invalida: " + descartadasNotaInvalida);
        System.out.println("Livros distintos: " + livros.quantidadeLivros());
        System.out.println("Usuarios distintos: " + usuarios.quantidadeUsuarios());
        System.out.println();

        ExperimentoHash.executar(livros, usuarios, capacidade);                     // Acréscimo referente ao tópico RF5 (5.3)
        System.out.println();

        GravadorCsv gravador = new GravadorCsv("usuario_melhor_livro.csv");
        gravador.escreverTextoFixo("User_id,profileName,Title,media_livro,N_livro,nota_usuario");
        gravador.escreverFimDeLinha();

        for (int i = 0; i < usuarios.quantidadeUsuarios(); i++) {
            DadosUsuario d = usuarios.dados(i);
            int livro = d.melhorLivro;
            No noLivro = livros.livro(livro);
            int n = livros.contagem(livro);
            double media = livros.soma(livro) / n;

            gravador.escreverCampo(d.no.chave, d.no.tamanho);
            gravador.escreverVirgula();
            gravador.escreverCampo(d.nome, d.tamanhoNome);
            gravador.escreverVirgula();
            gravador.escreverCampo(noLivro.chave, noLivro.tamanho);
            gravador.escreverVirgula();
            gravador.escreverDecimal(media, 4);
            gravador.escreverVirgula();
            gravador.escreverInteiro(n);
            gravador.escreverVirgula();
            gravador.escreverDecimal(d.notaUsuario, 1);
            gravador.escreverFimDeLinha();

            if (i < 10) {
                UtilitariosTexto.imprimir(d.no.chave, d.no.tamanho);
                System.out.print(" | ");
                UtilitariosTexto.imprimir(d.nome, d.tamanhoNome);
                System.out.print(" | ");
                UtilitariosTexto.imprimir(noLivro.chave, noLivro.tamanho);
                System.out.println(" | media: " + media + " | N: " + n + " | nota_usuario: " + d.notaUsuario);
            }
        }

        gravador.fechar();
        System.out.println();
        ConsultaUsuarios.executar("user_id.txt", usuarios, livros);     // Acréscimo referente ao tópico RF6

        System.out.println();
        RankingLivros.executar(livros);                                               // Acréscimo referente ao tópico RF7
    }

    static void pularCabecalho(LeitorCsv leitor) throws IOException {
        leitor.lerCampo(null, 0);
        while (!leitor.terminouLinha()) {
            leitor.lerCampo(null, 0);
        }
    }
}