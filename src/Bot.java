import javafx.scene.control.Button;

import java.util.Arrays;

public class Bot {
    public int[] move() {
        // create random move
        int randomX = (int) (Math.random()*8);
        int randomY = (int) (Math.random()*8);
        System.out.println("Ini X: " + randomX + " Ini Y: " + randomY);
        return new int[]{randomY, randomX};
    }
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
    public void setField(String[][] newVal){
        field = newVal;
    }

    public int[] move(Button[][] field, boolean isBotFirst, int roundsLeft){
        System.out.println("ROUNDS LEFT: "+roundsLeft);
        int max = -999;
        int beta = 999;
        int alpha = -999;

        setField(parseField(field));
        int actionsLeft = roundsLeft * 2;
        int[][] emptyCells = getEmptyCells(getField());
        if(!isBotFirst) actionsLeft--;
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

    public int scoreFunction(String[][] field){
        int score = 0;
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if(field[i][j].equals("O")) score++;
                else if (field[i][j].equals("X")) score--;
            }
        }
        return score;
    }

    public int[] action(String[][]copyField, int[] chosenCell, int actionsLeft, boolean botTurn, int alpha, int beta){
//        System.out.println("NODE ID: " + id);
        if(isBotTurn()) copyField = updateField(copyField, chosenCell[0], chosenCell[1], "0");
        else copyField = updateField(copyField, chosenCell[0], chosenCell[1], "X");
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



    public String[][] updateField(String[][] field, int i, int j, String val){
        field[i][j] = val;
        if(i - 1 >= 0 && !field[i - 1][j].equals("")){
            field[i - 1][j] = val;
        }
        if(i + 1 <= 7 && !field[i + 1][j].equals("")){
            field[i + 1][j] = val;
        }
        if(j - 1 >= 0 && !field[i][j - 1].equals("")){
            field[i][j - 1] = val;
        }
        if(j + 1 <= 7 && !field[i][j + 1].equals("")){
            field[i][j + 1] = val;
        }
        return  field;
    }

    public int[][] getEmptyCells(String[][] field){
        int count = 0;
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if(field[i][j].equals("")){
                    count++;
                }
            }
        }

        int[][] listOfEmpty = new int[count][2];
        count = 0;
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if(field[i][j].equals("")){
                    listOfEmpty[count] = new int[]{i, j};
                    count++;
                }
            }
        }
        return listOfEmpty;
    }

    public String[][] parseField(Button[][] field){
        String[][] parsedField = new String[8][8];
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if(field[i][j].getText().equals("")) parsedField[i][j] = "";
                else if (field[i][j].getText().equals("X")) parsedField[i][j] = "X";
                else parsedField[i][j] = "O";
            }
        }
        return parsedField;
    }
}