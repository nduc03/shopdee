public class Shopdee {

    private Shopdee(){}

    private static final class ShopdeeHolder {
        private static final Shopdee instance = new Shopdee();
    }

    public static Shopdee getInstance(){
        return ShopdeeHolder.instance;
    }

}
