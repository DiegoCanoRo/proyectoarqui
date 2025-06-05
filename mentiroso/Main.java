package comdiegocano.mentiroso;




public class Main {

    public static void main(String[] args) {

        new Juego(2).iniciarJuego();

    }

    private static void imprimirCartas(Baraja baraja) {
        for (Carta carta : baraja.getBaraja()) {
            if (carta != null) {
                System.out.println(carta.getValor() + " de " + carta.getPalo());
            }
        }
    }
}
