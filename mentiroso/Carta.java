package comdiegocano.mentiroso;

import javax.swing.*;

public class Carta {
    private String palo;
    private int valor;
    private JLabel imagenCarta;
    private JLabel imagenOculta;
    private boolean visibilidad;

    public Carta(String palo, int valor, boolean visibilidad) {
        this.palo = palo;
        this.valor = valor;
        this.visibilidad = visibilidad;
    }

    public Carta(String palo, int valor, boolean visibilidad, String rutaImagen) {
        this.palo = palo;
        this.valor = valor;
        this.visibilidad = visibilidad;
        this.imagenCarta = new JLabel(new ImageIcon(getClass().getResource(rutaImagen)));
        this.imagenOculta = new JLabel(new ImageIcon(getClass().getResource("/BarajaEspañola/CartaInversa.png")));
    }

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
        return visibilidad ? imagenCarta : imagenOculta;
    }

    public JLabel getImagenOculta() {
        return imagenOculta;
    }

   
    public void setVisibilidad(boolean visibilidad) {
        this.visibilidad = visibilidad;
    }

   public String toString(){
       return "Palo: " + palo + "Valor: " + valor;
   }
}
