import javafx.scene.control.Button;

public abstract class Bot {
    private int rounds_left;
    private MarkType[][] field;

    public Bot(Button[][] field, int init_rounds){
        updateGameState(field, init_rounds);
    }
    public void updateGameState(Button[][] field, int rounds_left){
        this.field = parseField(field);
        this.rounds_left = rounds_left;
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
