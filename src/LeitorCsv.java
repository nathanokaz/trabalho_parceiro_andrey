import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LeitorCsv {

    private BufferedReader leitor;
    private int caractereEspiado;
    private boolean fimDeLinha;

    public char[] titulo = new char[2000];
    public char[] usuario = new char[200];
    public char[] nome = new char[500];
    public int tamanhoTitulo;
    public int tamanhoUsuario;
    public int tamanhoNome;
    public double nota;
    private char[] textoNota = new char[20];

    public LeitorCsv(String caminho) throws IOException {
        this.leitor = new BufferedReader(new FileReader(caminho), 1 << 16);
        this.caractereEspiado = -2;
        this.fimDeLinha = false;
    }

    private int ler() throws IOException {
        if (caractereEspiado != -2) {
            int c = caractereEspiado;
            caractereEspiado = -2;
            return c;
        }
        return leitor.read();
    }

    private int espiar() throws IOException {
        if (caractereEspiado == -2) {
            caractereEspiado = leitor.read();
        }
        return caractereEspiado;
    }


    public boolean temMaisDados() throws IOException {
        return espiar() != -1;
    }


    public boolean terminouLinha() {
        return fimDeLinha;
    }

    // Lê uma linha do Books_rating.csv e preenche titulo, usuario, nome e nota
    public void lerAvaliacao() throws IOException {
        lerCampo(null, 0);
        tamanhoTitulo = lerCampo(titulo, 2000);
        lerCampo(null, 0);
        tamanhoUsuario = lerCampo(usuario, 200);
        tamanhoNome = lerCampo(nome, 500);
        lerCampo(null, 0);
        int tamanhoNota = lerCampo(textoNota, 20);
        lerCampo(null, 0);
        lerCampo(null, 0);
        lerCampo(null, 0);

        nota = UtilitariosTexto.converterParaDouble(textoNota, tamanhoNota);
    }


    public int lerCampo(char[] destino, int capacidade) throws IOException {
        int tamanho = 0;
        boolean dentroAspas = false;
        boolean inicioCampo = true;
        fimDeLinha = false;

        while (true) {
            int c = ler();

            if (c == -1) {
                fimDeLinha = true;
                break;
            }

            if (dentroAspas) {
                if (c == '"') {
                    if (espiar() == '"') {
                        ler();
                        if (destino != null && tamanho < capacidade) {
                            destino[tamanho] = '"';
                            tamanho++;
                        }
                    } else {
                        dentroAspas = false;
                    }
                } else {
                    if (destino != null && tamanho < capacidade) {
                        destino[tamanho] = (char) c;
                        tamanho++;
                    }
                }
            } else {
                if (c == '"' && inicioCampo) {
                    dentroAspas = true;
                } else if (c == ',') {
                    break;
                } else if (c == '\n') {
                    fimDeLinha = true;
                    break;
                } else if (c != '\r') {
                    if (destino != null && tamanho < capacidade) {
                        destino[tamanho] = (char) c;
                        tamanho++;
                    }
                }
            }

            inicioCampo = false;
        }

        return tamanho;
    }

    public void fechar() throws IOException {
        leitor.close();
    }
}