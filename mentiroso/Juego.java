package comdiegocano.mentiroso;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Juego extends javax.swing.JFrame implements MouseListener {

    //private ArrayList<Jugador> jugadores;
    private Jugador[] jugadores;
    private Mesa mesa;
    private int turnoActual;
    private Carta cartaVolteada;
    private Baraja baraja;
    private Baraja cartasEleccion;
    private boolean veredictoFinal;
    private Baraja cartasEliminar = new Baraja();
    private int cantidadJugadores;
    
    private JFrame frame;
    private JPanel panelCartas;
    private JPanel panelControlArriba;
    private JPanel panelControlArribaIzquierda;
    private JPanel panelControlArribaDerecha;
    private JPanel panelControlAbajo;
    private JPanel panelMano;
    private JPanel panelPozo;
    private JPanel panelCartasSeleccionadas;

    private JMenuBar menuBar;
    private JMenu menuAyuda;
    private JMenuItem itemSalir;
    private JMenuItem itemCreditos;

    private JLabel estadoJuego;
    private JLabel turno;
    private JLabel mentiraOverdad;

    private JButton botonMentira;
    private JButton botonVerdad;
    private JButton botonColocarPozo;
    private JButton botonDesocultar;

    public Juego(int cantidadJugadores) {
        this.cantidadJugadores = cantidadJugadores;
        this.jugadores = new Jugador[cantidadJugadores];
        this.mesa = new Mesa();
        cartaVolteada = new Carta("oros", 1, false, "/BarajaEspañola/CartaInversa.png");
        cartasEleccion = new Baraja();
        turnoActual = 0;
        veredictoFinal = false;

        //El constructor por defecto se tiene que poner
        //48(que es la cantidad de cartas de una baraja española)
        this.baraja = new Baraja(40);

        //Se inicializa primero los jugadores para que no haya problemas
        //al pedirles el nombre
        for (int i = 0; i < cantidadJugadores; ++i) {
            String nombre = JOptionPane.showInputDialog("Nombre del jugador " + (i + 1) + ":");
            jugadores[i] = new Jugador(nombre);
        }

        //Método para hacer manos(explicado más adelante)
        hacerManos();
    }
    
      static {
        //System.loadLibrary("Baraja"); 
        System.load("C:\\Users\\diego\\source\\arqui\\ProyectoFinalArqui\\ProyectoFinalArqui\\Juego.dll");

        
    }
    
    public native boolean terminoJuego();
    public native Jugador determinarJugador();
    //public native int cantidadJugadores();
    public native void cambiarTurno();
    
    
    private boolean esFinDelJuego() {
        for (int i = 0; i < jugadores.length; i++) {
            if (jugadores[i].getBarajaMano().tamanoBaraja() == 0) {
                return true;
            }
        }
        return false;
    }

    private Jugador determinarGanador() {
        for (int i = 0; i < jugadores.length; i++) {
            if (jugadores[i].getBarajaMano().tamanoBaraja() == 0) {
                return jugadores[i];
            }
        }
        return null;
    }

    private void hacerManos() {

        //se baraja con el metodo nativo
        baraja.barajar();
        System.out.println("" + cantidadJugadores);
        //Para determinar las cartas por jugador se divide la cantidad de cartas entre
        //los jugadores
        int cartasPorJugador = baraja.tamanoBaraja() / 2;//jugadores.size();

        //El ciclo para asignar cartas consta del ciclo de cada jugador y el
        //ciclo para asignar cartas por jugador
        for (int j = 0; j < jugadores.length; j++) {
            for (int i = 0; i < cartasPorJugador; i++) {
                if (baraja.tamanoBaraja() > 0) {  // Asegura que haya cartas disponibles

                    Carta carta = baraja.getCarta(0);
                    System.out.println("Asignando carta: " + carta.toString());
                    carta.getImagenCarta().addMouseListener(this);
                    jugadores[j].getBarajaMano().agregarCarta(carta);
                    baraja.eliminarCarta(0);
                    System.out.println("Tamaño actual de la baraja: " + baraja.tamanoBaraja());
                } else {
                    System.out.println("Error: No hay suficientes cartas en la baraja.");
                    break;
                }
            }
        }

        //En dado caso que la asignación de cartas por jugador no sea entera y
        //quede un residuo en la baraja esta se alamacenará en el cementerio del juego
        if (baraja.tamanoBaraja() != 0) {
            for (int i = 0; i < baraja.tamanoBaraja(); ++i) {
                baraja.getCarta(0).getImagenCarta().addMouseListener(this);

                mesa.getCementerio().agregarCarta(baraja.getCarta(0));
                baraja.eliminarCarta(0);
            }
        }
    }

    

    private void mostrarMano(Jugador jugador) {
        panelMano.removeAll();

        if (jugador == null) {
            System.out.println("mostrarMano: jugador es null");
            return;
        }

        Baraja mano = jugador.getBarajaMano();
        if (mano == null) {
            System.out.println("mostrarMano: baraja del jugador es null");
            return;
        }

        for (int i = 0; i < mano.tamanoBaraja(); i++) {
            Carta carta = mano.getCarta(i);
            if (carta != null && carta.getImagenCarta() != null) {
                panelMano.add(carta.getImagenCarta());
            } else {
                System.out.println("mostrarMano: carta o imagen null en índice " + i);
            }
        }

        panelMano.repaint();
        panelMano.revalidate();
    }

    public Jugador getJugador(int i) {
        return jugadores[i];
    }

    private void cartasPanelCartasSeleccionadas() {
        panelCartasSeleccionadas.removeAll();

        if (veredicto == Veredicto.MENTIRA) {

            if (getJugador((turnoActual - 1 + jugadores.length) % jugadores.length).getBarajaMano().tamanoBaraja() < 4) {
                Baraja baraja = new Baraja(40);
                baraja.barajar();

                for (int i = 0; i < cartasEleccion.tamanoBaraja(); ++i) {
                    panelCartasSeleccionadas.add(baraja.getCarta(i).getImagenCarta());
                }
            } else {
                Baraja manoClon = getJugador((turnoActual - 1 + jugadores.length) % jugadores.length).getBarajaMano();
                manoClon.barajar();

                for (int i = 0; i < cartasEleccion.tamanoBaraja(); ++i) {
                    if (manoClon.tamanoBaraja() > 0 && manoClon.getCarta(0) != null) {
                        panelCartasSeleccionadas.add(manoClon.getCarta(0).getImagenCarta());
                        manoClon.eliminarCarta(0);
                    }
                }

            }

        } else {
            for (int i = 0; i < cartasEleccion.tamanoBaraja(); ++i) {
                panelCartasSeleccionadas.add(cartasEleccion.getCarta(i).getImagenCarta());
            }
        }
    }

    public void jugar() {
        SwingUtilities.invokeLater(() -> {
            hacerFrame();

            mostrarMano(getJugador(turnoActual));
        });
    }

//
//    //AREA DE BOTONES
//
//    //Definición del enum Veredicto
    public enum Veredicto {
        MENTIRA, VERDAD, NEUTRO
    }

    //Cambiar el atributo veredicto a tipo Veredicto
    private Veredicto veredicto;

    //Modificación del método auxiliar para usar el enum
    private void procesarVeredicto(Veredicto veredicto) {
        botonVerdad.setEnabled(false);
        botonMentira.setEnabled(false);
        //Almacena el veredicto usando el enum
        this.veredicto = veredicto;
    }

    // Métodos que llaman al auxiliar con el enum
    private void botonMentira() {

        botonVerdad.setEnabled(false);
        botonMentira.setEnabled(false);

        if (veredictoFinal) {
            int turnoAnterior = (turnoActual - 1 + jugadores.length) % jugadores.length;
            panelMano.setEnabled(true);

            if (veredicto == Veredicto.MENTIRA) {
                mentiraOverdad.setText("El jugador anterior ha mentirado, acertaste!!!");
                estadoJuego.setText("el jugador anterior obtendrá toda las cartas del pozo");

                for (int i = 0; i < mesa.getPozo().tamanoBaraja(); i++) {

                    getJugador(turnoAnterior).getBarajaMano().agregarCarta(mesa.getPozo().getCarta(0));
                    mesa.getPozo().eliminarCarta(0);
                }
                getJugador(turnoAnterior).getBarajaMano().agregarCarta(mesa.getPozo().getCarta(0));
                mesa.getPozo().eliminarCarta(0);

                panelMano.removeAll();
                mesa.getPozo().vaciarBaraja();

            } else {
                mentiraOverdad.setText("El jugador anterior ha verdado, no has acertado!!!");
                estadoJuego.setText("el jugador actual obtendrá toda las cartas del pozo");

                for (int i = 0; i < mesa.getPozo().tamanoBaraja(); ++i) {

                    getJugador(turnoActual).getBarajaMano().agregarCarta(mesa.getPozo().getCarta(0));
                    mesa.getPozo().eliminarCarta(0);
                }
                getJugador(turnoActual).getBarajaMano().agregarCarta(mesa.getPozo().getCarta(0));
                mesa.getPozo().eliminarCarta(0);

                panelMano.removeAll();
                mesa.getPozo().vaciarBaraja();
            }

            panelPozo.removeAll();

            veredictoFinal = false;
            mostrarMano(getJugador(turnoActual));
            panelCartasSeleccionadas.removeAll();
            estadoJuego.setText("Escoge máximo tres cartas");
            veredicto = null;

        } else {
            botonColocarPozo.setEnabled(true);
            procesarVeredicto(Veredicto.MENTIRA);
        }
        actualizarInterfaz();
    }

    private void botonVerdad() {

        botonVerdad.setEnabled(false);
        botonMentira.setEnabled(false);

        if (veredictoFinal) {
            panelMano.setEnabled(true);
            panelMano.removeAll();
            mostrarMano(getJugador(turnoActual));
            panelCartasSeleccionadas.removeAll();
            estadoJuego.setText("Escoge máximo tres cartas");

            veredictoFinal = false;
            veredicto = null;
        } else {
            procesarVeredicto(Veredicto.VERDAD);
            botonColocarPozo.setEnabled(true);
        }

        actualizarInterfaz();
    }

    private void botonDesocultar() {
        botonDesocultar.setEnabled(false);
        botonMentira.setEnabled(true);
        botonVerdad.setEnabled(true);
        mostrarMano(getJugador(turnoActual));
        actualizarInterfaz();
    }

    public void actualizarInterfaz() {
        turno.setText("Turno de jugador: " + (turnoActual + 1) + " Cartas restantes: " + getJugador(turnoActual).getBarajaMano().tamanoBaraja());
    }

    private void botonColocarPozo() {
        if (esFinDelJuego()) {
            panelControlArriba.removeAll();
            JLabel mensajeFinal = new JLabel("EL " + determinarGanador().getNombre() + " HA GANADO EL JUEGO!!!");
            panelControlArriba.add(mensajeFinal, BorderLayout.CENTER);
            panelMano.removeAll();
            panelPozo.removeAll();
            panelCartasSeleccionadas.removeAll();

            botonColocarPozo.setEnabled(false);
            botonVerdad.setEnabled(false);
            botonMentira.setEnabled(false);
            panelControlAbajo.setVisible(false);

            JButton botonFinal = new JButton("Salir");
            botonFinal.addActionListener(e -> botonSalir());
            panelControlArriba.add(botonFinal, BorderLayout.EAST);
        }
        botonColocarPozo.setEnabled(false);
        botonMentira.setEnabled(false);
        botonVerdad.setEnabled(false);
        botonDesocultar.setEnabled(true);
        veredictoFinal = true;
        mentiraOverdad.setText("Veredicto aun por determinar...");

        if (cartasEleccion.tamanoBaraja() != 0) {
            for (int i = 0; i < cartasEleccion.tamanoBaraja(); ++i) {
                mesa.getPozo().agregarCarta(cartasEleccion.getCarta(i));
            }
        }
        if (mesa.getPozo().tamanoBaraja() != 0) {
            panelPozo.add(cartaVolteada.getImagenCarta());
            panelPozo.repaint();
            panelPozo.revalidate();
        }
        
        cambiarTurno();
        //turnoActual = (turnoActual + 1) % jugadores.length;
        turno.setText("Turno de jugador: " + (turnoActual + 1));

        panelMano.removeAll();
        Jugador jugador = getJugador(turnoActual);
        if (jugador != null && jugador.getBarajaMano() != null) {
            Baraja mano = jugador.getBarajaMano();
            for (int i = 0; i < mano.tamanoBaraja(); i++) {
                Carta carta = mano.getCarta(i);
                if (carta != null && carta.getImagenOculta() != null) {
                    panelMano.add(carta.getImagenOculta());
                } else {
                    System.out.println("Carta nula o sin imagen en índice " + i + " del jugador " + turnoActual);
                }
            }
        } else {
            System.out.println("Jugador o baraja null en turnoActual = " + turnoActual);
        }

        panelMano.repaint();
        panelMano.revalidate();
        estadoJuego.setText("Elige si es verdad o mentira las cartas ingresadas");
        cartasPanelCartasSeleccionadas();
        panelMano.setEnabled(false);
        cartasEleccion = new Baraja();
        actualizarInterfaz();

    }

//
//    /**
//     * Método que es el botón para salir del juego
//     */
    private void botonSalir() {
        System.exit(0);
    }
//
//    private void mostrarCreditos() {
//        System.out.println("\nHecho por: ");
//        System.out.println("Gael Jovani López García");
//        System.out.println("Saúl Iván Ramírez Heraldez");
//    }
//

    @Override
    public void mouseClicked(MouseEvent e) {
        cartasEliminar.vaciarBaraja();  // Vaciar la lista antes de cada uso

        if (!veredictoFinal) {
            Baraja mano = getJugador(turnoActual).getBarajaMano();
            for (int i = 0; i < mano.tamanoBaraja(); i++) {
                Carta carta = mano.getCarta(i);

                if (e.getSource() == carta.getImagenCarta()) {
                    if (veredicto == Veredicto.MENTIRA || veredicto == Veredicto.VERDAD) {
                        botonColocarPozo.setEnabled(true);
                    }
                    botonMentira.setEnabled(true);
                    botonVerdad.setEnabled(true);

                    if (cartasEleccion.tamanoBaraja() < 3) {
                        cartasEliminar.agregarCarta(carta);
                        cartasEleccion.agregarCarta(carta);
                        panelCartasSeleccionadas.add(carta.getImagenCarta());

                        panelCartasSeleccionadas.repaint();
                        panelCartasSeleccionadas.revalidate();
                    } else {
                        estadoJuego.setText("Has alcanzado el máximo de cartas");
                    }
                }
            }

            for (int j = 0; j < cartasEliminar.tamanoBaraja(); j++) {
                Carta carta = cartasEliminar.getCarta(j);
                for (int i = 0; i < getJugador(turnoActual).getBarajaMano().tamanoBaraja(); i++) {
                    if (getJugador(turnoActual).getBarajaMano().getCarta(i).equals(carta)) {
                        getJugador(turnoActual).getBarajaMano().eliminarCarta(i);
                        break;
                    }
                }
            }

        }
        actualizarInterfaz();

    }
    
    
    private void hacerFrame() {
        //Crea el frame
        frame = new JFrame("El mentiroso");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        //Crea los paneles del frame
        panelCartas = new JPanel();
        panelCartas.setLayout(new BorderLayout());
        panelMano = new JPanel();
        panelPozo = new JPanel(new BorderLayout());
        panelCartasSeleccionadas = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelMano.setLayout(new FlowLayout(FlowLayout.CENTER));
        panelCartas.add(panelMano, BorderLayout.SOUTH);
        panelCartas.add(panelPozo, BorderLayout.CENTER);
        panelCartas.add(panelCartasSeleccionadas, BorderLayout.NORTH);
        panelControlArriba = new JPanel();
        panelControlAbajo = new JPanel();
        panelControlArribaIzquierda = new JPanel();
        panelControlArribaDerecha = new JPanel();

        //Creaciones de botones
        botonMentira = new JButton("Mentira");
        botonVerdad = new JButton("Verdad");
        botonColocarPozo = new JButton("Colocar en Pozo");
        botonDesocultar = new JButton("Desocultar");
        botonDesocultar.addActionListener(evento -> botonDesocultar());
        botonMentira.addActionListener(evento -> botonMentira());
        botonVerdad.addActionListener(evento -> botonVerdad());
        botonColocarPozo.addActionListener(evento -> botonColocarPozo());

        if (cartasEleccion.tamanoBaraja() == 0) {
            botonMentira.setEnabled(false);
            botonVerdad.setEnabled(false);
            botonColocarPozo.setEnabled(false);
            botonDesocultar.setEnabled(false);
        }

        estadoJuego = new JLabel("Estado Juego");
        turno = new JLabel("Turno");
        mentiraOverdad = new JLabel("| Mentira o verdad");

        //Modificación de paneles
        panelMano.setBackground(new Color(53, 101, 77));
        panelCartas.setBackground(new Color(53, 101, 77));
        panelCartasSeleccionadas.setBackground(new Color(53, 101, 77));
        panelPozo.setBackground(new Color(53, 101, 77));

        panelControlArribaIzquierda.setLayout(new FlowLayout());
        panelControlArribaDerecha.setLayout(new FlowLayout());
        panelControlArribaIzquierda.add(turno, BorderLayout.WEST);
        panelControlAbajo.add(botonDesocultar, BorderLayout.EAST);
        panelControlArribaDerecha.add(estadoJuego, BorderLayout.EAST);
        panelControlArribaDerecha.add(mentiraOverdad, BorderLayout.EAST);
        panelControlArriba.add(panelControlArribaIzquierda, BorderLayout.WEST);
        panelControlArriba.add(panelControlArribaDerecha, BorderLayout.EAST);
        panelControlAbajo.add(botonMentira, BorderLayout.CENTER);
        panelControlAbajo.add(botonVerdad, BorderLayout.CENTER);
        panelControlAbajo.add(botonColocarPozo, BorderLayout.CENTER);

        //Crea la barra menú y menú, junto a sus actionListeners
//        menuBar = new JMenuBar();
//        menuAyuda = new JMenu("Ayuda");
//        itemSalir = new JMenuItem("Salir");
//        itemCreditos = new JMenuItem("Créditos");
//        itemSalir.addActionListener(evento -> botonSalir());
        //itemCreditos.addActionListener(evento -> mostrarCreditos());

        //Almacén de items, menús en la barra menú y añadir paneles a frame
//        menuAyuda.add(itemSalir);
//        menuAyuda.add(itemCreditos);
//        menuBar.add(menuAyuda);
//        frame.setJMenuBar(menuBar);
        frame.add(panelCartas);
        frame.add(panelControlArriba, BorderLayout.NORTH);
        frame.add(panelControlAbajo, BorderLayout.SOUTH);

        //Hacer el frame visible junto a modificaciones
        frame.setSize(1500, 900);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
