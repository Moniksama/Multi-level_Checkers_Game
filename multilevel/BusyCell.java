package checkers.multilevel;


public class BusyCell extends Exception {

    private static final long serialVersionUID = 1L;

    public BusyCell(String message) {
        super(message);
    }

}
