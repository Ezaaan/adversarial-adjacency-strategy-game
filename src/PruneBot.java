import javafx.scene.control.Button;

class PruneBot extends Bot{
    private int round_left;
    public int[] move(Board board){
        System.out.println("ROUNDS LEFT: "+round_left);
        float max = -999;
        float beta = 999;
        float alpha = -999;
        boolean botTurn = true;

        int actionsLeft = round_left * 2;
        int[][] emptyCells = board.getEmptyCells();
        if(!board.IS_BOT_FIRST) actionsLeft--;
        float[] result = new float[3];

//        System.out.println("Initial alpha: "+max);

        int[] coor = new int[2];
        for (int[] cell:
                emptyCells) {
            result = action(new Board(board), cell, actionsLeft, botTurn, alpha, beta);
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

    public float[] action(Board board, int[] chosenCell, int actionsLeft, boolean botTurn, float alpha, float beta){
//        System.out.println("NODE ID: " + id);
        board.addMove(chosenCell[0], chosenCell[1], Tile.BOT);
        int[][] emptyCells = board.getEmptyCells();
        actionsLeft--;

        System.out.println("botTurn: " + botTurn);
        System.out.println("Actions left: "+actionsLeft);
        System.out.println("ChosenCell: "+chosenCell[0]+" | "+chosenCell[1]);

        botTurn = !botTurn;

        if(actionsLeft == 0) {
            System.out.println("Leaf score: "+objectiveFunction(board));
            return new float[]{chosenCell[0], chosenCell[1], objectiveFunction(board)};
        }

        //Maximizer
        if(botTurn){
            float max = -999;
            for (int[] cell: emptyCells) {
                float[] node = action(new Board(board), cell, actionsLeft, botTurn, alpha, beta);
                max = Math.max(node[2], max);
                alpha = Math.max(max, alpha);

                if(beta <= alpha){
                    break;
                }
            }
            return new float[]{chosenCell[0], chosenCell[1], max};
        }

        //Minimizer
        float min = 999;
        for (int[] cell: emptyCells) {
            float[] node = action(new Board(board), cell, actionsLeft, botTurn, alpha, beta);
            min = Math.min(node[2], min);
            beta = Math.min(min, beta);

            if(beta <= alpha){
                break;
            }
        }
        return new float[]{chosenCell[0], chosenCell[1], min};
    }
}