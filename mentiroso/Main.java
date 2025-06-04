package comdiegocano.mentiroso;




public class Main {

    public static void main(String[] args) {

        new Juego(2).jugar();

   Baraja baraja = new Baraja(1); // crea la baraja con 40 cartas
//
//        System.out.println("Cartas antes de eliminar:");
    imprimirCartas(baraja);
//
//        System.out.println("Total antes de eliminar: " + baraja.tamanoBaraja());
//
//        // Eliminar 30 cartas por índice
//     for (int i = 0; i < 35; i++) {
//    if (baraja.tamanoBaraja() > 0) {
//        Carta carta = baraja.getCarta(0);
//        if (carta != null) {
//            System.out.println("Eliminando carta: " + carta.toString());
//            baraja.eliminarCarta(0);
//        } else {
//            System.out.println("Intento de acceso a carta NULL en índice 0.");
//        }
//        System.out.println("Tamaño actual de la baraja: " + baraja.tamanoBaraja());
//        System.out.println("Índice actual del bucle: " + i);  // Imprime el índice correcto
//    } else {
//        System.out.println("Error: No hay suficientes cartas en la baraja.");
//        break;
//    }
//}
// 
//        
//        imprimirCartas(baraja);
        
        
        
        
        
baraja.barajar();
//        System.out.println("\nCartas después de eliminar 30:");
      imprimirCartas(baraja);
//        System.out.println("Total después de eliminar: " + baraja.tamanoBaraja());

//        // Agregar 15 cartas recreando la baraja con nuevas cartas (no nativo, solo para ejemplo)
//        // Suponiendo que Baraja tiene un método para agregar cartas o recreamos la baraja parcialmente.
//        // Aquí recreamos un nuevo objeto con 15 cartas (por ejemplo)
//        baraja = new Baraja(1); // nueva baraja completa
//        // Eliminar 25 para que queden 15
        for (int i = 0; i <= 40; i++) {
            System.out.println("" + i);
            baraja.eliminarCarta(0);
        }

//        System.out.println("\nCartas después de agregar 15 (recreando):");
//        Carta carta = new Carta("oro", 5, true);
//        baraja.agregarCarta(carta);
//        //baraja.agregarCarta(carta);
                imprimirCartas(baraja);
baraja.barajar();
                imprimirCartas(baraja);

//        System.out.println("Total después de agregar: " + baraja.tamanoBaraja());
//        // Vaciar baraja
//        baraja.vaciarBaraja();
//
//        System.out.println("\nCartas después de vaciar:");
//        imprimirCartas(baraja);
//        System.out.println("Total después de vaciar: " + baraja.tamanoBaraja());




//
// Jugador jugador = new Jugador("Diego");
//
//        System.out.println("Jugador: " + jugador.getNombre());
//
//        System.out.println("Incrementando puntuación:");
//        for (int i = 0; i < 6; i++) {
//            jugador.agregarPunto();
//            System.out.println("Puntuación actual: " + jugador.getPuntuacion());
//        }
//
//        System.out.println("Decrementando puntuación:");
//        for (int i = 0; i < 3; i++) {
//            jugador.quitarPunto();
//            System.out.println("Puntuación actual: " + jugador.getPuntuacion());
//        }
//        
//        
//        
//                System.out.println("" + baraja.getCarta(0).toString());
//                imprimirCartas(baraja);
//                
//                baraja.barajar();
//                imprimirCartas(baraja);

    }

    private static void imprimirCartas(Baraja baraja) {
        for (Carta carta : baraja.getBaraja()) {
            if (carta != null) {
                System.out.println(carta.getValor() + " de " + carta.getPalo());
            }
        }
    }
}
