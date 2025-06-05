package comdiegocano.mentiroso;

import javax.swing.*;

public class Carta {

    private String palo;
    private int valor;
    private boolean visibilidad;
    private JLabel imagenBocaArriba;
    private JLabel imagenBocaAbajo;

    public Carta(String palo, int valor, boolean visibilidad) {
        this.palo = palo;
        this.valor = valor;
        this.visibilidad = visibilidad;
    }

    public Carta(String palo, int valor, boolean visibilidad, String rutaImagen) {
        this.palo = palo;
        this.valor = valor;
        this.visibilidad = visibilidad;
        this.imagenBocaArriba = new JLabel(new ImageIcon("C:\\Users\\diego\\Documents\\NetBeansProjects"
                + "\\mentiroso\\src\\main\\resources\\baraja" + rutaImagen));
        this.imagenBocaAbajo = new JLabel(new ImageIcon("C:\\Users\\diego\\Documents\\NetBeansProjects"
                + "\\mentiroso\\src\\main\\resources\\baraja\\volteada.png"));
    }

    //getters
    public int getValor() {
        return valor;
    }

    public String getPalo() {
        return palo;
    }

    public boolean getVisibilidad() {
        return visibilidad;
    }

    public JLabel getImagenCarta() {
        return visibilidad ? imagenBocaArriba : imagenBocaAbajo;
    }

    public JLabel getImagenBocaAbajo() {
        return imagenBocaAbajo;
    }

    public String toString() {
        return "Palo: " + palo + "Valor: " + valor;
    }
}
