package comdiegocano.mentiroso;



public class Baraja {

    Carta[] baraja = new Carta[40]; 
    String[] bancoPalos = {"oros", "copas", "espadas", "bastos"};
    
    
    static {
        //System.loadLibrary("Baraja"); 
        System.load("C:\\Users\\diego\\source\\arqui\\ProyectoFinalArqui\\ProyectoFinalArqui\\Baraja.dll");

        
    }
    
    
    
    public native void barajar();
    
   
    public native void eliminarCarta(int posicion);

    public native void vaciarBaraja();

    public native int tamanoBaraja();

    public native void agregarCarta(Carta carta);
    
    public Baraja(int numero) {
        //vaciarBaraja();
    for (int i = 0; i < bancoPalos.length; i++) {
        for (int j = 1; j <= 10; j++) {
            agregarCarta(new Carta(
                bancoPalos[i],
                j,
                true,
                "/BarajaEspañola/" + j + " " + bancoPalos[i] + ".png"
            ));
        }
    }
}


    /**
     * Constructor vacío (se puede usar para manos)
     */
    public Baraja() {
        baraja = new Carta[40]; // Se inicializa como un arreglo vacío
    }

    /**
     * Devuelve la baraja como un arreglo
     */
    public Carta[] getBaraja() {
        return baraja;
    }
    
    public Carta getCarta(int i){
    int tam = tamanoBaraja();
    if (i < 0 || i >= tam) {
        throw new IndexOutOfBoundsException("Índice fuera de rango: " + i);
    }
    return baraja[i];
}


    /**
     * Establece la visibilidad de todas las cartas en la baraja
     */
    public void setVisibilidad(boolean visibilidad) {
        for (Carta carta : baraja) {
            if (carta != null) {
                carta.setVisibilidad(visibilidad);
            }
        }
    }

   
}
