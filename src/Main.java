public class Main {
    public static void main(String[] args) {
        char[] teste = {'N', 'a', 't', 'h', 'a', 'n'};
        int tamanho = 3;
        int capacidade = 1000003;

        int hashFnv1a = FuncoesHash.hashFnv1a(teste, tamanho);
        int hashPolinomialBase31 = FuncoesHash.hashPolinomialBase31(teste, tamanho);
        int hashDjb2 = FuncoesHash.hashDjb2(teste, tamanho);

        System.out.println("FNV1a: " + hashFnv1a + " | Posicao: " + FuncoesHash.calcularPosicao(hashFnv1a, capacidade));
        System.out.println("Polinomial 31: " + hashPolinomialBase31 + " | Posicao: " + FuncoesHash.calcularPosicao(hashPolinomialBase31, capacidade));
        System.out.println("djb2: " + hashDjb2 + " | Posicao: " + FuncoesHash.calcularPosicao(hashDjb2, capacidade));
    }
}