import java.util.Random;


public class GeneticBot extends Bot {

    @Override
    public int[] move(Board board) {
        Random random = new Random();
        int n = 4;
        int[][] validMoves = board.getValidMoves();
        int[][] initialPopulations = new int[n][2];
        float[] fitness = new float[n];
        float[] probabilityList = calculateProbability(fitness);
        int[][] parents = new int[n][2];

        // Initial population
        for (int i = 0; i < n; i++) {
            int [] randomPopulation = validMoves[random.nextInt(validMoves.length)];
            initialPopulations[i][0] = randomPopulation[0];
            initialPopulations[i][1] = randomPopulation[1];
        }

        // Fitness Function
        for (int i = 0; i < n; i++) {
            Board newBoard = new Board(board);
            newBoard.addMove(initialPopulations[i][0], initialPopulations[i][1], Tile.BOT);
            fitness[i] = objectiveFunction(newBoard);
        }

        // Selection
        for (int i = 0; i < n; i++) {
            float randomRoulette = random.nextFloat();
            int[] move = initialPopulations[selectParent(probabilityList, randomRoulette)];
            parents[i][0] = move[0];
            parents[i][1] = move[1];
        }

        // Cross-Over
        for (int i = 0; i < n; i++) {
            if (i % 2 == 1) {
                int[] tempAction1 = {parents[i][0], parents[i-1][1]};
                int[] tempAction2 = {parents[i-1][0], parents[i][1]};
                if (isValidMove(validMoves, tempAction1) && isValidMove(validMoves, tempAction2)) {
                    parents[i][1] = tempAction1[1];
                    parents[i-1][1] = tempAction2[1];
                }
                else {
                    int[] tempAction3 = {parents[i-1][0], parents[i][1]};
                    int[] tempAction4 = {parents[i][0], parents[i-1][1]};
                    if (isValidMove(validMoves, tempAction3) && isValidMove(validMoves, tempAction4)) {
                        parents[i][0] = tempAction3[0];
                        parents[i-1][0] = tempAction4[0];
                    }
                }
            }
        }

        // Mutation
        for (int i = 0; i < n; i++) {
            int mutationCoordinate = random.nextInt(8);
            int[] mutatedAction = {parents[i][0], mutationCoordinate};

            for (int j = 0; j < 3; j++) {
                if (isValidMove(validMoves, mutatedAction)) {
                    parents[i][1] = mutatedAction[1];
                    break;
                }
                mutatedAction[1] = random.nextInt(8);
            }
        }

        int[] bestMove = parents[0];
        Board newBoard = new Board(board);
        newBoard.addMove(parents[0][0], parents[0][1], Tile.BOT);
        float bestScore = objectiveFunction(newBoard);

        // Memilih anak yang paling tinggi fitnessnya
        for (int i = 1; i < n; i++) {
            Board tempBoard = new Board(board);
            tempBoard.addMove(parents[i][0], parents[i][1], Tile.BOT);
            float tempFitness = objectiveFunction(tempBoard);
            if (bestScore < tempFitness) {
                bestMove = parents[i];
                bestScore = tempFitness;
            }
        }


        return bestMove;
    }

    private boolean isValidMove(int[][] validMoves, int[] move) {
        boolean isValid = false;

        for (int i = 0; i < validMoves.length; i++) {
             if (move[0] == validMoves[i][0] && move[1] == validMoves[i][1]) {
                 isValid = true;
                 break;
             }
        }

        return isValid;
    }

    private int selectParent(float[] probabilityList, float randomRoulette) {
        int n = probabilityList.length;
        int i = 0;
        while (i < n && randomRoulette > probabilityList[i]) {
            i++;
        }
        return i;
    }

    private float[] calculateProbability(float[] fitness) {
        float sumFitness = 0;
        for (int i = 0; i < fitness.length; i++) {
            sumFitness += fitness[i];
        }
        float[] probabilityList = new float[4];

        float prevProbability = 0;
        for (int i = 0; i < fitness.length; i++) {
            probabilityList[i] += prevProbability + fitness[i]/sumFitness;
            prevProbability = probabilityList[i];
        }

        return probabilityList;
    }


    public float heuristic(Board board) {
        int[][] ownedCells = board.getOwnedCells(Tile.BOT);
        float val = 0;
        for (int[] cell : ownedCells) {
            val += Math.abs(
                    Math.sqrt(Math.pow(cell[0]/3.5 - 1, 2) + Math.pow(cell[1]/3.5 - 1, 2)) / 17
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
