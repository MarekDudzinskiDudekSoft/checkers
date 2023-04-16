package shared;


public class GameParameters {

    private static int staticSizeOfBoard = 8;

    public static int getStaticSizeOfBoard() {
        return staticSizeOfBoard;
    }

    public void setStaticSizeOfBoard(int sizeOfBoard) {
        staticSizeOfBoard = sizeOfBoard;
    }
}
