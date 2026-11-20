import java.io.FileNotFoundException;
import java.io.IOException;

public class ConsultaUsuarios {

    public static void executar(String caminhoArquivo, TabelaUsuarios usuarios, TabelaLivros livros) throws IOException {
        LeitorCsv leitor;
        try {
            leitor = new LeitorCsv(caminhoArquivo);
        } catch (FileNotFoundException e) {
            System.out.println("O arquivo de consulta nao foi encontrado: " + caminhoArquivo);
            return;
        }

        System.out.println("\n| Consulta de Usuários |\n");
        char[] bufferUsuario = new char[200];

        while (leitor.temMaisDados()) {
            int tamanho = leitor.lerCampo(bufferUsuario, 200);
            if (tamanho > 0) {
                DadosUsuario d = usuarios.buscar(bufferUsuario, tamanho);

                System.out.print("User_id: ");
                UtilitariosTexto.imprimir(bufferUsuario, tamanho);
                System.out.print(" | profileName: ");

                if (d == null) {
                    System.out.println("Usuario nao encontrado");
                } else {
                    if (d.tamanhoNome == 0) {
                        System.out.print("(vazio)");
                    } else {
                        UtilitariosTexto.imprimir(d.nome, d.tamanhoNome);
                    }
                    No noLivro = livros.livro(d.melhorLivro);
                    System.out.print(" | Melhor livro: ");
                    UtilitariosTexto.imprimir(noLivro.chave, noLivro.tamanho);
                    System.out.println();
                }
            }
        }

        leitor.fechar();
    }
}