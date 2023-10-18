import java.util.Random;
import java.util.Arrays;

public class SidewayHCBot extends Bot{
    private Random random = new Random();
    
    public int[] move(Board board) {
        
        int[][] validMoves = board.getValidMoves();
        int[][] bestMoves = new int[validMoves.length][2];
        int i = -1;
        float bestScore = -999;
        
        for (int[] move : validMoves) {
            Board newBoard = new Board(board);
            newBoard.addMove(move[0], move[1], Tile.BOT);
  
            float score = objectiveFunction(newBoard);
            if (score >= bestScore) {
                if (score == bestScore) {
                    i++;
                    bestMoves[i] = move;
                } else {
                    i = 0;
                    bestScore = score;
                    bestMoves = new int[validMoves.length][2];
                    bestMoves[i] = move;
                }
            }
        }


        if (i > 0) {
            int[] move = bestMoves[random.nextInt(i)];

            System.out.println("%%%\nBot chooses: " + move[0] + " " + move[1]);
            System.out.println("Evaluation: " + bestScore + "\n%%%\n");

            return move;
        }

        if (i == 0) {
            int[] move = bestMoves[0];
            
            System.out.println("%%%\nBot chooses: " + move[0] + " " + move[1]);
            System.out.println("Evaluation: " + bestScore + "\n%%%\n");

            return move;
        }
        
        return null;
    }
}
