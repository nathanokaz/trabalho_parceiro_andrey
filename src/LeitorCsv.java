import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LeitorCsv {

    private BufferedReader leitor;
    private int caractereEspiado;
    private boolean fimDeLinha;

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