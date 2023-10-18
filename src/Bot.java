import javafx.scene.control.Button;

public abstract class Bot {
    private int rounds_left;
    private MarkType[][] field;

public abstract class Bot {
    // public int[] move() {
    //     // create random move
    //     int randomX = (int) (Math.random()*8);
    //     int randomY = (int) (Math.random()*8);
    //     System.out.println("Ini X: " + randomX + " Ini Y: " + randomY);
    //     return new int[]{randomY, randomX};
    // }
    public abstract int[] move(Board board);
}

class PruneBot {
    private boolean botTurn = true;
    private String[][] field = new String[8][8];
    public int nodeID = 1;

    //Getter
    public boolean isBotTurn() { return this.botTurn; }
    public String[][] getField(){ return field; }

    //Setter
    public void toggleBotTurn(){ botTurn = !botTurn; }
    public void setField(int i, int j, String val){
        field[i][j] = val;
    }
    public abstract int[] move();
    protected int getRoundsLeft() { return this.rounds_left; }
    protected MarkType[][] getField(){ return field; }
    private MarkType[][] parseField(Button[][] field){
        MarkType[][] parsedField = new MarkType[8][8];
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if(field[i][j].getText().equals("")) parsedField[i][j] = MarkType.EMPTY;
                else if (field[i][j].getText().equals("X")) parsedField[i][j] = MarkType.MARK_PLAYER;
                else parsedField[i][j] = MarkType.MARK_BOT;
            }
        }
        return parsedField;
    }
}
