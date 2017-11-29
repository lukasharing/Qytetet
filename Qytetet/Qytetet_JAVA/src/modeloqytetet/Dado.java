package modeloqytetet;

public class Dado {
	private static final Dado singleton = new Dado();
    public static Dado getInstance() {
        return singleton; 
    };
    public static int tirar(){
        return ((int)(Math.random() * 6.0) + 1);
    };
}
