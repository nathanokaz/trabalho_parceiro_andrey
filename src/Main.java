import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        String caminho = "Books_rating.csv";
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
            leitor.lerCampo(null, 0);                                   // 0 Id
            int tamanhoTitulo = leitor.lerCampo(titulo, 2000);          // 1 Title
            leitor.lerCampo(null, 0);                                   // 2 Price
            int tamanhoUsuario = leitor.lerCampo(usuario, 200);         // 3 User_id
            leitor.lerCampo(null, 0);                                   // 4 profileName
            leitor.lerCampo(null, 0);                                   // 5 helpfulness
            int tamanhoNota = leitor.lerCampo(nota, 20);                // 6 score
            leitor.lerCampo(null, 0);                                   // 7 time
            leitor.lerCampo(null, 0);                                   // 8 summary
            leitor.lerCampo(null, 0);                                   // 9 text

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

        for (int i = 0; i < usuarios.quantidadeUsuarios() && i < 10; i++) {
            DadosUsuario d = usuarios.dados(i);
            int livro = d.melhorLivro;
            No noLivro = livros.livro(livro);
            double media = livros.soma(livro) / livros.contagem(livro);

            imprimir(d.no.chave, d.no.tamanho);
            System.out.print(" | ");
            imprimir(d.nome, d.tamanhoNome);
            System.out.print(" | ");
            imprimir(noLivro.chave, noLivro.tamanho);
            System.out.println(" | media: " + media + " | N: " + livros.contagem(livro) + " | nota_usuario: " + d.notaUsuario);
        }
    }

    static void pularCabecalho(LeitorCsv leitor) throws IOException {
        leitor.lerCampo(null, 0);
        while (!leitor.terminouLinha()) {
            leitor.lerCampo(null, 0);
        }
    }

    static void imprimir(char[] texto, int tamanho) {
        for (int i = 0; i < tamanho; i++) {
            System.out.print(texto[i]);
        }
    }
}