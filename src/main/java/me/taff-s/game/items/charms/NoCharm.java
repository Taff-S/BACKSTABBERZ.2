public class NoCharm extends Charm{
    private static final NoCharm instance = new NoCharm();

    public NoCharm(){
        super("No Charm","This charm slot is empty", 0);
    }

    public static NoCharm getInstance() {
        return instance;
     }

}