public class Board {
    public Tile[][] data;
    public int xScore;
    public int oScore;

    public final int ROW;
    public final int COL;
    public int ROUNDS;
    public final boolean IS_BOT_FIRST;

    public Board(int r, int c, int rounds, boolean is_bot_first) {
        ROW = r;
        COL = c;
        ROUNDS = rounds;
        IS_BOT_FIRST = is_bot_first;

        this.data = new Tile[r][c];
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                data[i][j] = Tile.EMPTY;
            }
        }

        this.data[r - 1][0] = Tile.PLAYER;
        this.data[r - 2][1] = Tile.PLAYER;
        this.data[r - 2][0] = Tile.PLAYER;
        this.data[r - 1][1] = Tile.PLAYER;
        this.data[0][c - 1] = Tile.BOT;
        this.data[1][c - 2] = Tile.BOT;
        this.data[1][c - 1] = Tile.BOT;
        this.data[0][c - 2] = Tile.BOT;


        this.xScore = 4;
        this.oScore = 4;
    }

    public Board(Board ref) {
        ROW = ref.ROW;
        COL = ref.COL;
        ROUNDS = ref.ROUNDS;
        IS_BOT_FIRST = ref.IS_BOT_FIRST;

        this.data = new Tile[ROW][COL];
        for (int i = 0; i < ROW; i++) {
            System.arraycopy(ref.data[i], 0, this.data[i], 0, COL);
        }

        this.xScore = ref.xScore;
        this.oScore = ref.oScore;
    }

    public void setData(int i, int j, Tile val) {
        this.data[i][j] = val;
    }

    public void setROUNDS(int rounds) { this.ROUNDS = rounds; }
    public void addScore(int num, Tile type) {
        if (type == Tile.PLAYER) {
            xScore += num;
        } else if (type == Tile.BOT) {
            oScore += num;
        }
    }

    private void updateGameBoard(int i, int j, Tile val) {
        // Value of indices to control the lower/upper bound of rows and columns
        // in order to change surrounding/adjacent X's and O's only on the game board.
        // Four boundaries:  First & last row and first & last column.

        int startRow, endRow, startColumn, endColumn;

        if (i - 1 < 0)     // If clicked button in first row, no preceding row exists.
            startRow = i;
        else               // Otherwise, the preceding row exists for adjacency.
            startRow = i - 1;

        if (i + 1 >= ROW)  // If clicked button in last row, no subsequent/further row exists.
            endRow = i;
        else               // Otherwise, the subsequent row exists for adjacency.
            endRow = i + 1;

        if (j - 1 < 0)     // If clicked on first column, lower bound of the column has been reached.
            startColumn = j;
        else
            startColumn = j - 1;

        if (j + 1 >= COL)  // If clicked on last column, upper bound of the column has been reached.
            endColumn = j;
        else
            endColumn = j + 1;


        // Search for adjacency for X's and O's or vice versa, and replace them.
        // Update scores for X's and O's accordingly.
        for (int x = startRow; x <= endRow; x++) {
            this.setPlayerScore(x, j, val);
        }

        for (int y = startColumn; y <= endColumn; y++) {
            this.setPlayerScore(i, y, val);
        }
    }

    public int[][] getValidMoves() {
        int count = 0;
        int[][] validMoves = new int[ROW * COL][2];

        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++){
                if (this.data[i][j] == Tile.EMPTY) {
                    validMoves[count] = new int[]{i, j};
                    count++;
                }
            }
        }

        int[][] validMovesCopy = new int[count][2];
        System.arraycopy(validMoves, 0, validMovesCopy, 0, count);

        return validMovesCopy;
    }

    private void setPlayerScore(int i, int j, Tile val){
        if (val == Tile.PLAYER) {
            if (this.data[i][j] == Tile.BOT) {
                setData(i, j, Tile.PLAYER);
                addScore(1, Tile.PLAYER);
                addScore(-1, Tile.BOT);
            }

        } else if (this.data[i][j] == Tile.PLAYER) {
                setData(i, j, Tile.BOT);
                addScore(1, Tile.BOT);
                addScore(-1, Tile.PLAYER);
        }
    }

    public void addMove(int i, int j, Tile val) {
        setData(i, j, val);
        addScore(1, val);
        updateGameBoard(i, j, val);
    }

    public int[][] getOwnedCells(Tile owner) {
        int count = 0;
        int[][] ownedCells = new int[ROW * COL][2];

        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++){
                if (this.data[i][j] == owner) {
                    ownedCells[count] = new int[]{i, j};
                    count++;
                }
            }
        }

        int[][] ownedCellsCopy = new int[count][2];
        System.arraycopy(ownedCells, 0, ownedCellsCopy, 0, count);

        return ownedCellsCopy;
    }

    public int[][] getEmptyCells(){
        return getOwnedCells(Tile.EMPTY);
    }

    public void print() {
        System.out.println("X Score: " + this.xScore);
        System.out.println("O Score: " + this.oScore);
        System.out.println("Board: ");
        for (Tile[] datum : this.data) {
            for (Tile tile : datum) {
                System.out.print(tile + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
