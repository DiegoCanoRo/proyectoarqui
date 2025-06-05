package comdiegocano.mentiroso;

public class Baraja {

    Carta[] baraja = new Carta[52];
    String[] palos = {"corazon", "diamante", "trebol", "espada"};

    static {
        System.load("C:\\Users\\diego\\source\\arqui\\ProyectoFinalArqui\\ProyectoFinalArqui\\Baraja.dll");
    }
    
    public native void barajar();
    public native void eliminarCarta(int posicion);
    public native void agregarCarta(Carta carta);
    public native void vaciarBaraja();
    public native int tamanoBaraja();

    public Baraja(int numero) {
        crearBaraja();
    }

    
    /**
     * Constructor vacio, para manos, pozo, cementerio
     */
    public Baraja() {
        baraja = new Carta[52]; // Se inicializa como un arreglo vacío
    }
    public Carta[] getBaraja() {
        return baraja;
    }

    public Carta getCarta(int i) {
        return baraja[i];
    }
    
    //crea una baraja inglesa
    public void crearBaraja(){
        for (int i = 0; i < palos.length; i++) {
            for (int j = 1; j <= 13; j++) {
                agregarCarta(new Carta(
                        palos[i],
                        j,
                        true,
                        "\\" + j + " " + palos[i] + ".png"
                ));
            }
        }
    }

}
