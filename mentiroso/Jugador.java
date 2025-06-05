package comdiegocano.mentiroso;

public class Jugador {

    private String nombre;
    private Baraja mano;
    private int puntuacion;

    public Jugador(String nombre) {
        this.nombre = nombre;
        this.mano = new Baraja();
        this.puntuacion = 0;
    }
    
    
     static {
        System.load("C:\\Users\\diego\\source\\arqui\\ProyectoFinalArqui\\ProyectoFinalArqui\\Jugador.dll"); 
    }

    public native void agregarPuntos(int puntos);
    public native void quitarPuntos(int puntos);
    
    
    public int getCartasRestantes() {
        return mano.tamanoBaraja();
    }

    public String getNombre() {
        return nombre;
    }

    public Baraja getBarajaMano() {
        return mano;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

}
