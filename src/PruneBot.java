import javafx.scene.control.Button;

class PruneBot extends Bot{
    private int round_left;
    public int[] move(Board board){
//        System.out.println("ROUNDS LEFT: "+board.ROUNDS);
        float max = -999;
        float beta = 999;
        float alpha = -999;
        boolean botTurn = true;

        int actionsLeft = board.ROUNDS * 2;
        int[][] emptyCells = board.getEmptyCells();
        if(!board.IS_BOT_FIRST) actionsLeft--;
        float[] result;

//        System.out.println("Initial alpha: "+max);
        Board defaultBoard = new Board(board);

        int[] coor = new int[2];
        for (int[] cell: emptyCells) {
            result = action(board, cell, actionsLeft, botTurn, alpha, beta);
            board.copyBoard(defaultBoard);
            if(max < result[2]){ coor = cell; }

            max = Math.max(result[2], max);
            alpha = Math.max(max, alpha);

            if(beta <= alpha){ break; }
        }

        return  new int[]{coor[0], coor[1]};
    }

    public float[] action(Board board, int[] chosenCell, int actionsLeft, boolean botTurn, float alpha, float beta){
        if(botTurn) { board.addMove(chosenCell[0], chosenCell[1], Tile.BOT); }
        else { board.addMove(chosenCell[0], chosenCell[1], Tile.PLAYER); }
        actionsLeft--;
        botTurn = !botTurn;

        if(actionsLeft == 0) {
//            System.out.println("Leaf score: "+objectiveFunction(board));
            return new float[]{chosenCell[0], chosenCell[1], objectiveFunction(board)};
        }

        int[][] emptyCells = board.getEmptyCells();
        Board defaultBoard = new Board(board);

        //Maximizer
        if(botTurn){
            float max = -999;
            for (int[] cell: emptyCells) {
                float[] node = action(board, cell, actionsLeft, botTurn, alpha, beta);
                board.copyBoard(defaultBoard);

                max = Math.max(node[2], max);
                alpha = Math.max(max, alpha);

                if(beta <= alpha){ break; }
            }
            return new float[]{chosenCell[0], chosenCell[1], max};
        }

        //Minimizer
        float min = 999;
        for (int[] cell: emptyCells) {
            float[] node = action(board, cell, actionsLeft, botTurn, alpha, beta);
            board.copyBoard(defaultBoard);

            min = Math.min(node[2], min);
            beta = Math.min(min, beta);

            if(beta <= alpha){ break; }
        }
        return new float[]{chosenCell[0], chosenCell[1], min};
    }
}