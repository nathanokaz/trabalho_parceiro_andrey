import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class GravadorCsv {

    private BufferedWriter escritor;

    public GravadorCsv(String caminho) throws IOException {
        this.escritor = new BufferedWriter(new FileWriter(caminho), 1 << 16);
    }


    public void escreverCampo(char[] texto, int tamanho) throws IOException {
        boolean precisaAspas = false;

        for (int i = 0; i < tamanho; i++) {
            char c = texto[i];
            if (c == ',' || c == '"' || c == '\n' || c == '\r') {
                precisaAspas = true;
            }
        }

        if (precisaAspas) {
            escritor.write('"');
            for (int i = 0; i < tamanho; i++) {
                if (texto[i] == '"') {
                    escritor.write('"');
                }
                escritor.write(texto[i]);
            }
            escritor.write('"');

        } else {
            for (int i = 0; i < tamanho; i++) {
                escritor.write(texto[i]);
            }
        }
    }


    public void escreverDecimal(double valor, int casas) throws IOException {
        int multiplicador = 1;
        for (int i = 0; i < casas; i++) {
            multiplicador = multiplicador * 10;
        }

        int escalado = (int) (valor * multiplicador + 0.5);
        int parteInteira = escalado / multiplicador;
        int parteFracionaria = escalado % multiplicador;

        escreverInteiro(parteInteira);

        if (casas > 0) {
            escritor.write('.');
            // zeros a esquerda: 0.05 com 4 casas -> fracao 500 -> "0500"
            int divisor = multiplicador / 10;
            while (divisor > 0) {
                escritor.write('0' + (parteFracionaria / divisor) % 10);
                divisor = divisor / 10;
            }
        }
    }

    public void escreverInteiro(int valor) throws IOException {
        if (valor == 0) {
            escritor.write('0');
            return;
        }

        char[] digitos = new char[12];
        int quantidade = 0;

        while (valor > 0) {
            digitos[quantidade] = (char) ('0' + valor % 10);
            quantidade++;
            valor = valor / 10;
        }

        for (int i = quantidade - 1; i >= 0; i--) {
            escritor.write(digitos[i]);
        }
    }

    public void escreverVirgula() throws IOException {
        escritor.write(',');
    }

    public void escreverFimDeLinha() throws IOException {
        escritor.write('\n');
    }

    public void escreverTextoFixo(String texto) throws IOException {
        escritor.write(texto);
    }

    public void fechar() throws IOException {
        escritor.close();
    }
}