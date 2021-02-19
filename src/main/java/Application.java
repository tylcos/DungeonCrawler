public class Application {
    /**
     * Bootstraps main application and runs the main window class
     * @param args CLI args (ignored)
     */
    public static void main(String[] args) {
        CalculatorWindowFormatted window = new CalculatorWindowFormatted();
        window.show();
    }
}