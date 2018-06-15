package debug;

public class Debug {
    private static final boolean DEBUG = true;

    public static void println (String string) {
        if (DEBUG) System.out.println(string);
    }
}
