package th.ac.udru.seksil;

public class MyConstant {

    private int[] iconInts = new int[]{
            R.drawable.ic_action_menu1,
            R.drawable.ic_action_menu2,
            R.drawable.ic_action_menu3,
            R.drawable.ic_action_menu4};

    private String[] titleStrings = new String[]{
            "My Friend",
            "My Map",
            "About Me",
            "Sign Out"};

    public int[] getIconInts() {
        return iconInts;
    }

    public String[] getTitleStrings() {
        return titleStrings;
    }
}