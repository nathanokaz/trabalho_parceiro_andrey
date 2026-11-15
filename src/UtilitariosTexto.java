public class UtilitariosTexto {

    public boolean saoIguais(char[] texto1, int tamanho1, char[] texto2, int tamanho2) {
        if (tamanho1 != tamanho2) {
            return false;
        }

        for (int i = 0; i < tamanho1; i++) {
            if (texto1[i] != texto2[i]) {
                return false;
            }
        }

        return true;
    }

    public char[] copiarTexto(char[] origem, int tamanho) {
        char[] destino = new char[tamanho];

        for (int i = 0; i < tamanho; i++) {
            destino[i] = origem[i];
        }

        return destino;
    }

    public double converterParaDouble(char[] texto, int tamanho) {
        if (tamanho == 0) {
            return -1.0;
        }

        double resultado = 0.0;
        double divisorDecimal = 1.0;
        boolean passouDoPonto = false;

        for (int i = 0; i < tamanho; i++) {
            char c = texto[i];

            if (c == '.') {
                if (passouDoPonto) {
                    return -1.0;
                }

                passouDoPonto = true;

            } else if (c >= '0' && c <= '9') {
                int digito = c - '0';

                if (!passouDoPonto) {
                    resultado = (resultado * 10.0) + digito;

                } else {
                    divisorDecimal *= 10.0;
                    resultado = resultado + (digito / divisorDecimal);
                }

            } else {
                return -1.0;
            }
        }

        return resultado;
    }

    public int compararOrdemAlfabetica(char[] texto1, int tamanho1, char[] texto2, int tamanho2) {
        int menorTamanho;

        if (tamanho1 < tamanho2) {
            menorTamanho = tamanho1;
        } else {
            menorTamanho = tamanho2;
        }

        for (int i = 0; i < menorTamanho; i++) {
            if (texto1[i] != texto2[i]) {
                return texto1[i] - texto2[i];
            }
        }

        return tamanho1 - tamanho2;
    }

}
