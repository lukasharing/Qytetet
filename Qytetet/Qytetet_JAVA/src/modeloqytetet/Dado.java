package modeloqytetet;
public class Dado {
	private static final Dado singleton = new Dado();
	public int ultimo = 1;
    public static Dado getInstance() {
        return singleton; 
    };
    int tirar(){
        return (ultimo = ((int)(Math.random() * 6.0) + 1));
    };
}
