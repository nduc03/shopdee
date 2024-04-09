public class MainMenu {

    private MainMenu(){}

    private static final class ShopdeeHolder {
        private static final MainMenu instance = new MainMenu();
    }

    public static MainMenu getInstance(){
        return ShopdeeHolder.instance;
    }

}
