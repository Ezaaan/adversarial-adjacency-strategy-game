import java.util.Random;

public class SidewayHCBot extends Bot{
    private Random random = new Random();
    
    public int[] move(Board board) {
        
        int[][] validMoves = board.getValidMoves();
        int[][] bestMoves = new int[validMoves.length][2];
        int i = 0;
        float bestScore = -999;
        
        for (int[] move : validMoves) {
            Board newBoard = board.copy();
            newBoard.addMove(move[0], move[1], Tile.BOT);
            float score = objectiveFunction(newBoard);
            if (score >= bestScore) {
                bestScore = score;

                if (score == bestScore) {
                    bestMoves[i] = move;
                    i++;
                } else {
                    i = 0;
                    bestMoves = new int[][]{move};
                }
            }
        }

        if (i > 0) {
            int[] move = bestMoves[random.nextInt(i)];

            System.out.println("Bot chooses: " + move[0] + " " + move[1]);
            System.out.println("Evaluation: " + bestScore + "\n");

            return move;
        }
        
        return null;
    }

    public float heuristic(Board board) {
        int[][] ownedCells = board.getOwnedCells(Tile.BOT);
        float val = 0;
        for (int[] cell : ownedCells) {
            val += Math.abs(
                Math.sqrt(Math.pow(cell[0]/3.5 - 1, 2) + Math.pow(cell[1]/3.5 - 1, 2)) / 16
            );
        }

        return val;
    } 

    public float objectiveFunction(Board board){
        float main = board.oScore - board.xScore;     
        float heur = heuristic(board);   
        
        return main + heur;
    }
    
}
