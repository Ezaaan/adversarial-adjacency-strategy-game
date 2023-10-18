public abstract class Bot {
    // public int[] move() {
    //     // create random move
    //     int randomX = (int) (Math.random()*8);
    //     int randomY = (int) (Math.random()*8);
    //     System.out.println("Ini X: " + randomX + " Ini Y: " + randomY);
    //     return new int[]{randomY, randomX};
    // }
    public abstract int[] move(Board board);

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

