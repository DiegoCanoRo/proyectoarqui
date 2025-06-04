package comdiegocano.mentiroso;


public class Mesa {

  //pozo las cartas jugadas y cementerio las cartas sobrantes
    
    private Baraja pozo;
    private Baraja cementerio;

    public Mesa() {
        this.pozo = new Baraja();
        this.cementerio = new Baraja();
    }

    public Baraja getPozo()
    {
        return pozo;
    }

    public Baraja getCementerio()
    {
        return cementerio;
    }
    
}
