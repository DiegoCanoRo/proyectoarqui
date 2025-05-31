

package comdiegocano.prueba;


public class Prueba {
 private native void print();
 private native int sumar(int a, int b);
 private native int restar(int a, int b);
 private native int multiplicar(int a, int b);
 
 
 static {
 System.loadLibrary("Prueba");
 }
 
 
 public static void main(String[] args) {
 Prueba prueba = new Prueba();
 

      System.out.println(System.getProperty("java.library.path"));


        System.out.println("Sumar 1: " + prueba.sumar(2, 202));
        System.out.println("Sumar 2: " + prueba.sumar(123, 432));

        System.out.println("Restar 1: " + prueba.restar(12312, 2));
        System.out.println("Restar 2: " + prueba.restar(3, 4));

        System.out.println("Multiplicar 1: " + prueba.multiplicar(5, 5));
        System.out.println("Multiplicar 2: " + prueba.multiplicar(4, 1));
 }
 
 
 
}