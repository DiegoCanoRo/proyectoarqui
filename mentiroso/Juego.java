package comdiegocano.mentiroso;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Juego extends javax.swing.JFrame implements MouseListener {

    private Jugador[] jugadores;
    private Mesa mesa;
    private int turnoActual;
    private Carta cartaBocaAbajo;
    private Baraja baraja;
    private Baraja cartasEleccion;
    private boolean veredictoFinal;
    private Baraja cartasEliminar = new Baraja();
    private int cantidadJugadores;
    private int cantCartasJugador;
    private int puntosParaGanar = 30;
    private int ronda = 1;
    boolean anteriorMintio = false;

    private JFrame ventana;

    private JTextArea infoJugadores;
    private JPanel panelMano;
    private JPanel panelPozo;
    private JPanel panelCartas;
    private JPanel panelArriba;
    private JPanel panelArribaIzquierda;
    private JPanel panelArribaDerecha;
    private JPanel panelAbajo;
    private JPanel panelDeCartasSeleccionadas;

    private JLabel infoJuego;
    private JLabel turno;
    private JLabel mentiraOverdad;

    private JButton botonMentira;
    private JButton botonVerdad;
    private JButton botonColocarPozo;
    private JButton botonVoltear;

    public Juego(int cantidadJugadores) {
        this.cantidadJugadores = cantidadJugadores;
        this.jugadores = new Jugador[cantidadJugadores];
        this.mesa = new Mesa();
        this.baraja = new Baraja(52);

        cartaBocaAbajo = new Carta("diamante", 1, false, "volteada.png");
        cartasEleccion = new Baraja();
        turnoActual = 0;
        veredictoFinal = false;

    }

    static {
        System.load("C:\\Users\\diego\\source\\arqui\\ProyectoFinalArqui\\ProyectoFinalArqui\\Juego.dll");
    }

    public native void cambiarTurno();
    public native void incrementarRonda();
    public native boolean esGanadorJuego(int puntos);
    public native int obtenerTurnoAnterior();

    
    //checa si los jugadores aun tiene cartas restantes
    private boolean esFinDelJuego() {
        for (int i = 0; i < cantidadJugadores; i++) {
            if (jugadores[i].getCartasRestantes() == 0) {
                return true;
            }
        }
        return false;
    }
    
    
    //devuevle al jugador que ya no tiene cartas, y si llego al puntaje maximo
    //gana la partida
    private Jugador determinarGanador() {
        for (int i = 0; i < cantidadJugadores; i++) {
            if (jugadores[i].getCartasRestantes() == 0) {
                if (esGanadorJuego(jugadores[i].getPuntuacion())) {
                    JOptionPane.showMessageDialog(null,
                            jugadores[i].getNombre() + " ha ganado la partida.",
                            "¡Fin del juego!", JOptionPane.INFORMATION_MESSAGE);

                    terminarJuego();
                }
                return jugadores[i];
            }
        }
        return null;
    }
    
    
    //si se llego al puntaje maximo pregunta si quiere iniciar una nueva partida o salir
    public void terminarJuego() {
        ronda = 1;
        int respuesta = JOptionPane.showConfirmDialog(this, 
                "¿Deseas jugar otra vez?", "Fin del juego", 
                JOptionPane.YES_NO_OPTION);

        if (respuesta == JOptionPane.YES_OPTION) {
            // SwingUtilities.invokeLater(() -> {
            this.dispose();

            iniciarJuego();
            return;
            // });
        } else {
            //SwingUtilities.invokeLater(() -> {
            this.dispose();
            System.exit(0);
            //});
        }
        return;

    }
    
    
    //configura para inicar una nueva ronda
    public void iniciarRonda() {
        veredicto = null;
        cartaBocaAbajo = new Carta("diamante", 1, false, 
                "volteada.png");
        botonColocarPozo.setEnabled(false);
        veredictoFinal = false;

        turnoActual = 0;
        baraja.vaciarBaraja();
        cartasEliminar.vaciarBaraja();
        cartasEleccion.vaciarBaraja();
        mesa.getPozo().vaciarBaraja();
        mesa.getCementerio().vaciarBaraja();
        baraja.crearBaraja();
        repartirCartas();

        mostrarManos(getJugador(turnoActual));
        panelCartas.add(panelMano, BorderLayout.SOUTH);
        panelCartas.add(panelPozo, BorderLayout.CENTER);
        panelCartas.add(panelDeCartasSeleccionadas, BorderLayout.NORTH);
        actualizarInterfaz();
    }

    public void repartirCartas() {

        //se baraja con el metodo nativo
        baraja.barajar();

        //
        cantCartasJugador = 2;
        //asignar cartas a cada jugador
        for (int j = 0; j < cantidadJugadores; j++) {
            for (int i = 0; i < cantCartasJugador; i++) {
                Carta carta = baraja.getCarta(0);
                carta.getImagenCarta().addMouseListener(this);

                //se le agrega la carta a la mano del jugador
                jugadores[j].getBarajaMano().agregarCarta(carta);
                //se elimina de la baraja
                baraja.eliminarCarta(0);

            }
        }

        //si sobran cartas se van pal cementerio
        if (baraja.tamanoBaraja() != 0) {
            for (int i = 0; i < baraja.tamanoBaraja(); ++i) {
                baraja.getCarta(0);

                mesa.getCementerio().agregarCarta(baraja.getCarta(0));
                baraja.eliminarCarta(0);
            }
        }

    }

    //muestra la mano de los jugadores en la ventana
    public void mostrarManos(Jugador jugador) {
        panelMano.removeAll();

        Baraja mano = jugador.getBarajaMano();

        for (int i = 0; i < mano.tamanoBaraja(); i++) {
            Carta carta = mano.getCarta(i);
            panelMano.add(carta.getImagenCarta());

        }

        panelMano.repaint();
        panelMano.revalidate();
    }

    public Jugador getJugador(int i) {
        return jugadores[i];
    }
    
   
    //mueve las cartas de la mano cuando se seleccionan hacia el panel 
    //de las cartas seleccionadas
    public void cartasPanelSeleccionadas() {
        panelDeCartasSeleccionadas.removeAll();
        if (veredicto == Veredicto.MENTIRA) {
            if (getJugador(obtenerTurnoAnterior()).getBarajaMano().tamanoBaraja() < 4) {
                Baraja baraja = new Baraja(52);
                baraja.barajar();

                for (int i = 0; i < cartasEleccion.tamanoBaraja(); ++i) {
                    panelDeCartasSeleccionadas.add(baraja.getCarta(i).getImagenCarta());
                }
            } else {
                Baraja manoClon = getJugador(obtenerTurnoAnterior()).getBarajaMano();
                manoClon.barajar();

                for (int i = 0; i < cartasEleccion.tamanoBaraja(); ++i) {
                    if (manoClon.tamanoBaraja() > 0 && manoClon.getCarta(0) != null) {
                        panelDeCartasSeleccionadas.add(manoClon.getCarta(0).getImagenCarta());
                        manoClon.eliminarCarta(0);
                    }
                }

            }

        } else {
            for (int i = 0; i < cartasEleccion.tamanoBaraja(); ++i) {
                panelDeCartasSeleccionadas.add(cartasEleccion.getCarta(i).getImagenCarta());
            }
        }
    }

    public void iniciarJuego() {
        //SwingUtilities.invokeLater(() -> {
        hacerFrame();
        for (int i = 0; i < cantidadJugadores; ++i) {
            String nombre = JOptionPane.showInputDialog("Nombre del jugador " + (i + 1) + ":");
            jugadores[i] = new Jugador(nombre);
        }

        iniciarRonda();
        mostrarManos(getJugador(turnoActual));
        actualizarInterfaz();
        //});
    }

    //se usara para controlar los botones y limitar las interaccions
    // por ejemplo si el jugador tiene que decidir si es verdad o mentira
    //que no pueda colocar en el pozo
    public enum Veredicto {
        MENTIRA, VERDAD, NEUTRO
    }

    public Veredicto veredicto;

    //este solo activa o desactiva los botones de verdad o mentira
    public void procesarVeredicto(Veredicto veredicto) {
        botonVerdad.setEnabled(false);
        botonMentira.setEnabled(false);
        this.veredicto = veredicto;
        anteriorMintio = (veredicto == Veredicto.MENTIRA);
veredictoFinal = true;

    }
    
    
 public void botonMentira() {

    botonVerdad.setEnabled(false);
    botonMentira.setEnabled(false);

    if (veredictoFinal) {
        int turnoAnterior = obtenerTurnoAnterior();
        panelMano.setEnabled(true);

        if (anteriorMintio) { 
            JOptionPane.showMessageDialog(null,
                    jugadores[turnoAnterior].getNombre()
                    + " ha mentido, acertaste.\n "
                    + jugadores[turnoAnterior].getNombre()
                    + " Obtendrá todas las cartas del pozo.",
                    "¡Acierto!", JOptionPane.INFORMATION_MESSAGE);

            for (int i = 0; i < mesa.getPozo().tamanoBaraja(); i++) {
                getJugador(turnoAnterior).getBarajaMano()
                        .agregarCarta(mesa.getPozo().getCarta(0));
                mesa.getPozo().eliminarCarta(0);
            }
            getJugador(turnoAnterior).getBarajaMano()
                    .agregarCarta(mesa.getPozo().getCarta(0));
            mesa.getPozo().eliminarCarta(0);

            panelMano.removeAll();
            mesa.getPozo().vaciarBaraja();

        } else {
            JOptionPane.showMessageDialog(null,
                    jugadores[turnoAnterior].getNombre()
                    + " ha dicho la verdad, no acertaste.\n"
                    + jugadores[turnoActual].getNombre()
                    + " obtendrá todas las cartas del pozo.",
                    "¡Fallaste!", JOptionPane.INFORMATION_MESSAGE);

            for (int i = 0; i < mesa.getPozo().tamanoBaraja(); ++i) {
                getJugador(turnoActual).getBarajaMano()
                        .agregarCarta(mesa.getPozo().getCarta(0));
                mesa.getPozo().eliminarCarta(0);
            }
            getJugador(turnoActual).getBarajaMano()
                    .agregarCarta(mesa.getPozo().getCarta(0));
            mesa.getPozo().eliminarCarta(0);

            panelMano.removeAll();
            mesa.getPozo().vaciarBaraja();
        }

        panelPozo.removeAll();

        veredictoFinal = false;
        mostrarManos(getJugador(turnoActual));
        panelDeCartasSeleccionadas.removeAll();
        infoJuego.setText("Escoge máximo tres cartas");

        actualizarInterfaz();

        veredicto = null;

    } else {
        botonColocarPozo.setEnabled(true);
        procesarVeredicto(Veredicto.MENTIRA);
    }
    actualizarInterfaz();
}

    
    /**
     * boton que procesa cuando se selecciona la verdad
     * 
     * si el jugador anterior dijo mentira, y el de este turno dice que fue verdad
     * se le dan las cartas del pozo.
     * 
     * si el anterior dijo verdad y el actual tambien, no sucede nada
     */
    public void botonVerdad() {

    botonVerdad.setEnabled(false);
    botonMentira.setEnabled(false);

    if (veredictoFinal) {
        int turnoAnterior = obtenerTurnoAnterior();
        panelMano.setEnabled(true);

        if (anteriorMintio) {
            JOptionPane.showMessageDialog(null,
                jugadores[turnoAnterior].getNombre()
                + " ha mentido, pero decidiste creerle.\n"
                + jugadores[turnoActual].getNombre()
                + " se lleva todas las cartas del pozo.",
                "Caiste", JOptionPane.INFORMATION_MESSAGE);

            for (int i = 0; i < mesa.getPozo().tamanoBaraja(); i++) {
                getJugador(turnoActual).getBarajaMano()
                    .agregarCarta(mesa.getPozo().getCarta(0));
                mesa.getPozo().eliminarCarta(0);
            }
            getJugador(turnoActual).getBarajaMano()
                .agregarCarta(mesa.getPozo().getCarta(0));
            mesa.getPozo().eliminarCarta(0);

            panelMano.removeAll();
            mesa.getPozo().vaciarBaraja();

        } else {
            JOptionPane.showMessageDialog(null,
                jugadores[turnoAnterior].getNombre()
                + " ha dicho la verdad, acertaste al creerle.\n"
                + "No pasa nada, el juego continúa.",
                "Todo bien", JOptionPane.INFORMATION_MESSAGE);

            // No se da el pozo a nadie
        }

        panelPozo.removeAll();

        veredictoFinal = false;
        mostrarManos(getJugador(turnoActual));
        panelDeCartasSeleccionadas.removeAll();
        infoJuego.setText("Escoge máximo tres cartas");
        veredicto = null;

    } else {
        procesarVeredicto(Veredicto.VERDAD);
        botonColocarPozo.setEnabled(true);
    }

    actualizarInterfaz();
}

    
       /**
    * coloca en el pozo las cartas seleccionadas
    */
    public void botonColocarPozo() {
        if (ronda > 0 && esFinDelJuego()) {
            //checa si un jugador ya no tiene cartas y se le agregan 10 puntos
            getJugador(turnoActual).agregarPuntos(10);
            actualizarInterfaz();
            JOptionPane.showMessageDialog(null, determinarGanador()
                    .getNombre() + " ha ganado la ronda y obtiene 10 puntos", "Ronda terminada",
                    JOptionPane.INFORMATION_MESSAGE);
            
            //termina la ronda
            incrementarRonda();
            panelCartas.removeAll();
            panelMano.removeAll();
            panelPozo.removeAll();
            panelDeCartasSeleccionadas.removeAll();
            //inicia una nueva ronda
            iniciarRonda();

            actualizarInterfaz();

            panelCartas.add(panelMano, BorderLayout.SOUTH);
            panelCartas.add(panelPozo, BorderLayout.CENTER);
            panelCartas.add(panelDeCartasSeleccionadas, BorderLayout.NORTH);
            return;
        }
        
        botonColocarPozo.setEnabled(false);
        botonMentira.setEnabled(false);
        botonVerdad.setEnabled(false);
        botonVoltear.setEnabled(true);
        veredictoFinal = true;
        
        if (cartasEleccion.tamanoBaraja() != 0) {
            for (int i = 0; i < cartasEleccion.tamanoBaraja(); ++i) {
                mesa.getPozo().agregarCarta(cartasEleccion.getCarta(i));
            }
        }
        if (mesa.getPozo().tamanoBaraja() != 0) {
            panelPozo.add(cartaBocaAbajo.getImagenCarta());
            panelPozo.repaint();
            panelPozo.revalidate();
        }
        veredicto = null;

        cambiarTurno();
        
        panelMano.removeAll();
        Jugador jugador = getJugador(turnoActual);
        Baraja mano = jugador.getBarajaMano();
        for (int i = 0; i < mano.tamanoBaraja(); i++) {
            Carta carta = mano.getCarta(i);
            panelMano.add(carta.getImagenBocaAbajo());

        }

        panelMano.repaint();
        panelMano.revalidate();
        infoJuego.setText("Elige: ¿Verdad o mentira?");
        cartasPanelSeleccionadas();
        panelMano.setEnabled(false);
        cartasEleccion = new Baraja();
        actualizarInterfaz();

    }
    
    
    //gira las cartas boca abajo
    public void botonVoltear() {
        botonVoltear.setEnabled(false);
        botonMentira.setEnabled(true);
        botonVerdad.setEnabled(true);
        mostrarManos(getJugador(turnoActual));
        actualizarInterfaz();
    }

    //actualiza los paneles y la informacion del jugador
    public void actualizarInterfaz() {
        turno.setText("Turno de jugador: " + getJugador(turnoActual).getNombre()
                + " | Cartas restantes: " + getJugador(turnoActual)
                        .getBarajaMano().tamanoBaraja() + " | Puntos: " + getJugador(turnoActual).getPuntuacion() + " |");
        mentiraOverdad.setText("| Ronda: " + ronda + " | " + " Pozo: "  + mesa.getPozo().tamanoBaraja());
        actualizarPaneles();

    }
    
    

    
    //procesa los click a las imagenes de las cartas, y asegura que solo seleccione
    //3 cartas
    @Override
    public void mouseClicked(MouseEvent e) {
        cartasEliminar.vaciarBaraja();

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
                        panelDeCartasSeleccionadas.add(carta.getImagenCarta());

                        panelDeCartasSeleccionadas.repaint();
                        panelDeCartasSeleccionadas.revalidate();
                    } else {
                        infoJuego.setText("Solo puedes elegir 3 cartas");
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

    public void hacerFrame() {
        //hace la ventana
        ventana = new JFrame("El mentiroso");
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setLayout(new BorderLayout());

        //paneles 
        panelCartas = new JPanel();
        panelCartas.setLayout(new BorderLayout());
        panelMano = new JPanel();
        panelPozo = new JPanel(new BorderLayout());
        panelDeCartasSeleccionadas = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelMano.setLayout(new FlowLayout(FlowLayout.CENTER));
        panelCartas.add(panelMano, BorderLayout.SOUTH);
        panelCartas.add(panelPozo, BorderLayout.CENTER);
        panelCartas.add(panelDeCartasSeleccionadas, BorderLayout.NORTH);
        panelArriba = new JPanel();
        panelAbajo = new JPanel();
        panelArribaIzquierda = new JPanel();
        panelArribaDerecha = new JPanel();

        botonMentira = new JButton("Mentira");
        botonVerdad = new JButton("Verdad");
        botonColocarPozo = new JButton("Colocar en Pozo");
        botonVoltear = new JButton("Voltear");

        botonMentira.setFont(new Font("Arial", Font.BOLD, 17));
        botonVerdad.setFont(new Font("Arial", Font.BOLD, 17));
        botonColocarPozo.setFont(new Font("Arial", Font.BOLD, 17));
        botonVoltear.setFont(new Font("Arial", Font.BOLD, 17));

        botonVoltear.addActionListener(evento -> botonVoltear());
        botonMentira.addActionListener(evento -> botonMentira());
        botonVerdad.addActionListener(evento -> botonVerdad());
        botonColocarPozo.addActionListener(evento -> botonColocarPozo());

        if (cartasEleccion.tamanoBaraja() == 0) {
            botonMentira.setEnabled(false);
            botonVerdad.setEnabled(false);
            botonColocarPozo.setEnabled(false);
            botonVoltear.setEnabled(false);
        }

        infoJuego = new JLabel("");
        turno = new JLabel("Turno");
        mentiraOverdad = new JLabel("| Ronda: " + ronda + "| " + "Pozo: "  + mesa.getPozo().tamanoBaraja());

        configurarLabels();

        //configuraciones de los paneles
        panelMano.setBackground(new Color(53, 33, 111));
        panelCartas.setBackground(new Color(53, 33, 111));
        panelDeCartasSeleccionadas.setBackground(new Color(53, 33, 111));
        panelPozo.setBackground(new Color(53, 33, 111));
        
        panelArribaIzquierda.setLayout(new FlowLayout());
        panelArribaDerecha.setLayout(new FlowLayout());
        panelArribaIzquierda.add(turno, BorderLayout.WEST);
        panelAbajo.add(botonVerdad, BorderLayout.EAST);
        panelArribaDerecha.add(infoJuego, BorderLayout.EAST);
        panelArribaDerecha.add(mentiraOverdad, BorderLayout.EAST);
        panelArriba.add(panelArribaIzquierda, BorderLayout.WEST);
        panelArriba.add(panelArribaDerecha, BorderLayout.EAST);
        panelAbajo.add(botonMentira, BorderLayout.CENTER);
        panelAbajo.add(botonColocarPozo, BorderLayout.CENTER);
        panelAbajo.add(botonVoltear, BorderLayout.CENTER);

        ventana.add(panelCartas);
        ventana.add(panelArriba, BorderLayout.NORTH);
        ventana.add(panelAbajo, BorderLayout.SOUTH);

        //Hacer el ventana visible junto a modificaciones
        ventana.setSize(1368, 768);
        ventana.setExtendedState(JFrame.MAXIMIZED_BOTH);
        ventana.setVisible(true);
        ventana.setLocationRelativeTo(null);
        actualizarPaneles();
    }
    
    //cambia la fuente y tamaño a las labels
    public void configurarLabels() {
        Font fuenteNueva = new Font("Arial", Font.PLAIN, 20);
        turno.setFont(fuenteNueva);
        infoJuego.setFont(fuenteNueva);
        mentiraOverdad.setFont(fuenteNueva);

    }
    
    //actualiza los paneles para mostrar el estado actual de las cartas
    //y no se bugee con cartas que ya no estan
    public void actualizarPaneles() {
        panelMano.revalidate();
        panelMano.repaint();
        panelCartas.revalidate();
        panelCartas.repaint();
        panelDeCartasSeleccionadas.revalidate();
        panelDeCartasSeleccionadas.repaint();
        panelPozo.revalidate();
        panelPozo.repaint();

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
