import javafx.scene.control.Button;

public class PruneBot extends Bot{
    private boolean bot_turn;

    public PruneBot(Button[][] field, int init_rounds) {
        super(field, init_rounds);
    }

    private boolean isBotTurn() { return this.bot_turn; }
    public int[] move(){
        System.out.println("ROUNDS LEFT: "+getRoundsLeft());
        int max = -999;
        int beta = 999;
        int alpha = -999;

        int actionsLeft = getRoundsLeft() * 2;
        int[][] emptyCells = getEmptyCells(getField());
        if(!isBotTurn()) actionsLeft--;
        int[] result = new int[3];

//        System.out.println("Initial alpha: "+max);

        int[] coor = new int[2];
        for (int[] cell:
                emptyCells) {
            result = action(getField(), cell, actionsLeft, !isBotTurn(), alpha, beta);
            if(max < result[2]){
                coor = cell;
                System.out.println("Got cell: " + coor[0] + " | " + coor[1] + "\nMax: " + result[2]);
            }else{
                System.out.println("MAX: "+max+" result: "+result[2]);
            }
            max = Math.max(result[2], max);
            alpha = Math.max(max, alpha);

            if(beta <= alpha){
                break;
            }
        }

        // Empty cells testing
        System.out.println("Ini X: " + coor[1] + " Ini Y: " + coor[0]);
//        for (int[] cell:
//                emptyCells) {
//            System.out.println(cell[0] + " | " + cell[1]);
//
//        }

        return  new int[]{coor[0], coor[1]};
    }

    private int scoreFunction(MarkType[][] field){
        int score = 0;
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if(field[i][j]==MarkType.MARK_BOT) score++;
                else if (field[i][j]==MarkType.MARK_PLAYER) score--;
            }
        }
        return score;
    }

    private MarkType[][] updateField(MarkType[][] field, int i, int j, MarkType val){
        field[i][j] = val;
        if(i - 1 >= 0 && field[i - 1][j]!= MarkType.EMPTY){
            field[i - 1][j] = val;
        }
        if(i + 1 <= 7 && field[i + 1][j]!= MarkType.EMPTY){
            field[i + 1][j] = val;
        }
        if(j - 1 >= 0 && field[i][j - 1]!= MarkType.EMPTY){
            field[i][j - 1] = val;
        }
        if(j + 1 <= 7 && field[i][j + 1]!= MarkType.EMPTY){
            field[i][j + 1] = val;
        }
        return  field;
    }

    private int[] action(MarkType[][]copyField, int[] chosenCell, int actionsLeft, boolean botTurn, int alpha, int beta){
//        System.out.println("NODE ID: " + id);
        if(isBotTurn()) copyField = updateField(copyField, chosenCell[0], chosenCell[1], MarkType.MARK_BOT);
        else copyField = updateField(copyField, chosenCell[0], chosenCell[1], MarkType.MARK_PLAYER);
        int[][] emptyCells = getEmptyCells(copyField);
        actionsLeft--;

        System.out.println("botTurn: " + botTurn);
        System.out.println("Actions left: "+actionsLeft);
        System.out.println("ChosenCell: "+chosenCell[0]+" | "+chosenCell[1]);

        if(actionsLeft == 0) {
            System.out.println("Leaf score: "+scoreFunction(copyField));
            return new int[]{chosenCell[0], chosenCell[1], scoreFunction(copyField)};
        }

        //Maximizer
        if(botTurn){
            int max = -999;
            for (int[] cell: emptyCells) {
                int[] node = action(copyField, cell, actionsLeft, !botTurn, alpha, beta);
                max = Math.max(node[2], max);
                alpha = Math.max(max, alpha);

                if(beta <= alpha){
                    break;
                }
            }
            return new int[]{chosenCell[0], chosenCell[1], max};
        }

        //Minimizer
        int min = 999;
        for (int[] cell: emptyCells) {
            int[] node = action(copyField, cell, actionsLeft, !botTurn, alpha, beta);
            min = Math.min(node[2], min);
            beta = Math.min(min, beta);

            if(beta <= alpha){
                break;
            }
        }
        return new int[]{chosenCell[0], chosenCell[1], min};
    }

    private int[][] getEmptyCells(MarkType[][] field){
        int count = 0;
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if(field[i][j]== MarkType.EMPTY){
                    count++;
                }
            }
        }

        int[][] listOfEmpty = new int[count][2];
        count = 0;
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if(field[i][j]== MarkType.EMPTY){
                    listOfEmpty[count] = new int[]{i, j};
                    count++;
                }
            }
        }
        return listOfEmpty;
    }
}