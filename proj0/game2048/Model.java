package game2048;

import java.util.Formatter;
import java.util.Observable;


/** The state of a game of 2048.
 *  @author TODO: YOUR NAME HERE
 */
public class Model extends Observable {
    /** Current contents of the board. */
    private Board board;
    /** Current score. */
    private int score;
    /** Maximum score so far.  Updated when game ends. */
    private int maxScore;
    /** True iff game is ended. */
    private boolean gameOver;

    /* Coordinate System: column C, row R of the board (where row 0,
     * column 0 is the lower-left corner of the board) will correspond
     * to board.tile(c, r).  Be careful! It works like (x, y) coordinates.
     */

    /** Largest piece value. */
    public static final int MAX_PIECE = 2048;

    /** A new 2048 game on a board of size SIZE with no pieces
     *  and score 0. */
    public Model(int size) {
        board = new Board(size);
        score = maxScore = 0;
        gameOver = false;
    }

    /** A new 2048 game where RAWVALUES contain the values of the tiles
     * (0 if null). VALUES is indexed by (row, col) with (0, 0) corresponding
     * to the bottom-left corner. Used for testing purposes. */
    public Model(int[][] rawValues, int score, int maxScore, boolean gameOver) {
        int size = rawValues.length;
        board = new Board(rawValues, score);
        this.score = score;
        this.maxScore = maxScore;
        this.gameOver = gameOver;
    }

    /** Return the current Tile at (COL, ROW), where 0 <= ROW < size(),
     *  0 <= COL < size(). Returns null if there is no tile there.
     *  Used for testing. Should be deprecated and removed.
     *  */
    public Tile tile(int col, int row) {
        return board.tile(col, row);
    }

    /** Return the number of squares on one side of the board.
     *  Used for testing. Should be deprecated and removed. */
    public int size() {
        return board.size();
    }

    /** Return true iff the game is over (there are no moves, or
     *  there is a tile with value 2048 on the board). */
    public boolean gameOver() {
        checkGameOver();
        if (gameOver) {
            maxScore = Math.max(score, maxScore);
        }
        return gameOver;
    }

    /** Return the current score. */
    public int score() {
        return score;
    }

    /** Return the current maximum game score (updated at end of game). */
    public int maxScore() {
        return maxScore;
    }

    /** Clear the board to empty and reset the score. */
    public void clear() {
        score = 0;
        gameOver = false;
        board.clear();
        setChanged();
    }

    /** Add TILE to the board. There must be no Tile currently at the
     *  same position. */
    public void addTile(Tile tile) {
        board.addTile(tile);
        checkGameOver();
        setChanged();
    }

    /** 检查此tile是否可以向上移动，如果可以进行移动，则返回true
     * 判断依据：
     *  1. 上方第一个是空白格
     *  2. 上方第一个是非空白格，且是相同数据
     *  3. 上方第一个是非空白格，且是非相同数据
     * @param t
     * @param col
     * @param row
     * @param board
     * @return
     */
    public static boolean checkMove(Tile t,int col,int row,Board board){
//        如果此tile自身是空格，则不符合条件
        if(t == null || row == board.size()-1){
           return false;
        }

        int currValue = t.value();
        Tile tile = board.tile(col, row+1);
        if ( tile == null){
            return true;
        }
        else if (tile.value() == currValue) {
            return true;
        }else {
            return false;
        }

    }

    /** 返回具体能到达的位置(行的位置）
     *
     * @param t
     * @param col
     * @param row
     * @param board
     * @param max_length:检测移动的上限
     * @return
     */
    public static int pathNumToMove(Tile t,int col,int row,Board board,int max_length){
        //                如果可以移动

        if(!checkMove(t, col, row, board)) {
            System.out.println("Error: this tile couldn't move!");
            return 0;
        }else{
            for (int i = row+1; i < max_length; i++) {
                /* 如果是空格，就直接continue */
                if ( board.tile(col, i) == null ){
                    if( i == max_length-1 ){
                        return i;
                    }
                /* 在上行时候如果遇到值相等，则返回此地址 */
                }else if (board.tile(col, i).value() == t.value() ) { return i; }
                /* 在上行时候如果遇到非空格且值不相等，则返回此地址-1 */
                 else { return i-1; }
            }

        }
        return 0;
    }

    /** 检测空格向上时是否会进行merge，若会merge则返回true
     *
     * @param t
     * @param col
     * @param row
     * @param board
     * @param flag_row
     * @return
     */
    public static boolean ifMerged(Tile t,int col,int row,Board board, int[] flag_row,int[] flag_col){
        /* 如果可以进行移动 */
        if(!checkMove(t, col, row, board)) {
            System.out.println("Error: this tile couldn't move!");

        }else{
            for (int i = row+1; i < board.size(); i++) {
                /* 只是移动至空白格，不会导致merge */
                if (board.tile(col, i) == null){
                    if( i == board.size()-1 ){
                        return false;
                    }
                /* 移动至非空白格，且数值相同，会导致merge */
                }else if (board.tile(col, i).value() == t.value() ) {
                    if(!contains(flag_row,i)){
                        return true;
                    }else {
                        return false;
                    }
                 }
                else { return false; }
            }
        }
        return false;
    }


    /**
     * 
     * @param arr
     * @param val
     * @return
     */
    public static boolean contains(int[] arr, int val){
        for (int i : arr) {
            if (i == val)
                return true;
        }
        return false;
    }

    /** Tilt the board toward SIDE. Return true iff this changes the board.
     *
     * 1. If two Tile objects are adjacent in the direction of motion and have
     *    the same value, they are merged into one Tile of twice the original
     *    value and that new value is added to the score instance variable
     * 2. A tile that is the result of a merge will not merge again on that
     *    tilt. So each move, every tile will only ever be part of at most one
     *    merge (perhaps zero).
     * 3. When three adjacent tiles in the direction of motion have the same
     *    value, then the leading two tiles in the direction of motion merge,
     *    and the trailing tile does not.
     * */
    public boolean tilt(Side side) {
        boolean changed;
        changed = false;

        board.setViewingPerspective(side);
//        用来标记已经merge过的方块
        int[] flag_col = new int[4];
        int[] flag_row = new int[4];
        int flag_num =0;
        for (int i = 0; i < 4; i++) {
            flag_col[i] = -1;
            flag_row[i] = -1;
        }


        for (int c = board.size() - 1 ; c >=0 ; c--) {
            for (int r = board.size() - 1; r >=0; r--) {

                Tile tile = board.tile(c, r);
                /* checkMove判断是否可以移动 */
                if(checkMove(tile, c, r, board)){

                    int resRow = pathNumToMove(tile, c, r, board,board.size());

                    /* 若是目的地被标记，则查看前一个位置是否可以移动,若是可以则用其替代 */
                    if(contains(flag_row,resRow) && contains(flag_col,c) ){
                        int resDownRow = pathNumToMove(tile, c, r, board, resRow);
                        resRow = resDownRow;
                    }

                    if(ifMerged(tile, c, r, board, flag_row, flag_col)){
                        /* 此目的地被标记 */

                        flag_col[flag_num] = c;
                        flag_row[flag_num] = resRow;
                        score += tile.value() *2;

                        flag_num +=1;   // 可以移动则记录
                    }

                    board.move(c, resRow, tile);
                    changed = true;
                }
            }
            for (int i = 0; i < 4; i++) {
                flag_col[i] = -1;
                flag_row[i] = -1;
            }
        }

        board.setViewingPerspective(Side.NORTH);
        // for the tilt to the Side SIDE. If the board changed, set the
        // changed local variable to true.

        checkGameOver();
        if (changed) {
            setChanged();
        }
        return changed;
    }

    /** Checks if the game is over and sets the gameOver variable
     *  appropriately.
     */
    private void checkGameOver() {
        gameOver = checkGameOver(board);
    }

    /** Determine whether game is over. */
    private static boolean checkGameOver(Board b) {
        return maxTileExists(b) || !atLeastOneMoveExists(b);
    }

    /** Returns true if at least one space on the Board is empty.
     *  Empty spaces are stored as null.
     * */
    public static boolean emptySpaceExists(Board b) {
        boolean flag = false;
        for (int i = 0; i < b.size(); i++) {
            for (int j = 0; j < b.size(); j++) {
                if (b.tile(i , j) == null){
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }

    /**
     * Returns true if any tile is equal to the maximum valid value.
     * Maximum valid value is given by MAX_PIECE. Note that
     * given a Tile object t, we get its value with t.value().
     */
    public static boolean maxTileExists(Board b) {

        boolean flag = false;
        for (int i = 0; i < b.size(); i++) {
            for (int j = 0; j < b.size(); j++) {
                if (b.tile(i , j) == null)
                    continue;

                if (b.tile(i, j).value() == MAX_PIECE ){
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }

    /**
     * Returns true if there are any valid moves on the board.
     * There are two ways that there can be valid moves:
     * 1. There is at least one empty space on the board.
     * 2. There are two adjacent tiles with the same value.
     */
    public static boolean atLeastOneMoveExists(Board b) {
        if(emptySpaceExists(b))
            return true;


        for (int i = 0; i < b.size(); i++) {
            for (int j = 0; j < b.size() ; j++) {
                int currentValue = b.tile(i, j).value();
                int size = b.size();
                //  循环中判断是否邻居和其值相当
                if (i > 0 && currentValue == b.tile(i - 1, j).value()) {
                    return true;
                }

                // Check below
                if (i < size - 1 && currentValue == b.tile(i + 1, j).value()) {
                    return true;
                }

                // Check left
                if (j > 0 && currentValue == b.tile(i, j - 1).value()) {
                    return true;
                }

                // Check right
                if (j < size - 1 && currentValue == b.tile(i, j + 1).value()) {
                    return true;
                }



            }
        }



        return false;
    }


    @Override
     /** Returns the model as a string, used for debugging. */
    public String toString() {
        Formatter out = new Formatter();
        out.format("%n[%n");
        for (int row = size() - 1; row >= 0; row -= 1) {
            for (int col = 0; col < size(); col += 1) {
                if (tile(col, row) == null) {
                    out.format("|    ");
                } else {
                    out.format("|%4d", tile(col, row).value());
                }
            }
            out.format("|%n");
        }
        String over = gameOver() ? "over" : "not over";
        out.format("] %d (max: %d) (game is %s) %n", score(), maxScore(), over);
        return out.toString();
    }

    @Override
    /** Returns whether two models are equal. */
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (getClass() != o.getClass()) {
            return false;
        } else {
            return toString().equals(o.toString());
        }
    }

    @Override
    /** Returns hash code of Model’s string. */
    public int hashCode() {
        return toString().hashCode();
    }
}
