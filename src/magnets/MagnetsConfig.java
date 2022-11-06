package magnets;

import backtracking.Configuration;
import test.IMagnetTest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

/**
 * The representation of a magnet configuration, including the ability
 * to backtrack and also give information to the JUnit tester.
 *
 * This implements a more optimal pruning strategy in isValid():
 * - Pair checked each time a new cell is populated
 * - Polarity checked each time a new cell is populated
 * - When last column or row is populated, the pos/neg counts are checked
 *
 * @author RIT CS
 */
public class MagnetsConfig implements Configuration, IMagnetTest {
    /** a cell that has not been assigned a value yet */
    private final static char EMPTY = '.';
    /** a blank cell */
    private final static char BLANK = 'X';
    /** a positive cell */
    private final static char POS = '+';
    /** a negative cell */
    private final static char NEG = '-';
    /** left pair value */
    private final static char LEFT = 'L';
    /** right pair value */
    private final static char RIGHT = 'R';
    /** top pair value */
    private final static char TOP = 'T';
    /** bottom pair value */
    private final static char BOTTOM = 'B';
    /** and ignored count for pos/neg row/col */
    private final static int IGNORED = -1;
    private int amountOfRows;
    private int amountOfCols;
    private int cursorRow;
    private int cursorCol;
    private char[][] board;
    private char[][] grid;
    private int[][] cursor;
    private int[] posRow;
    private int[] posCol;
    private int[] negRow;
    private int[] negCol;


    // TODO
    // add private state here

    /**
     * Read in the magnet puzzle from the filename.  After reading in, it should display:
     * - the filename
     * - the number of rows and columns
     * - the grid of pairs
     * - the initial config with all empty cells
     *
     * @param filename the name of the file
     * @throws IOException thrown if there is a problem opening or reading the file
     */
    public MagnetsConfig(String filename) throws IOException {
        System.out.println("File: " + filename);
        try (BufferedReader in = new BufferedReader(new FileReader(filename)))
        {
            // read first line: rows cols
            String[] fields = in.readLine().split("\\s+");
            this.amountOfRows = Integer.parseInt(fields[0]);
            this.amountOfCols = Integer.parseInt(fields[1]);
            this.board = new char[amountOfRows][amountOfCols];
            this.grid = new char[amountOfRows][amountOfCols];
            fields = in.readLine().split("\\s+");
            this.posRow = new int[amountOfRows];
            for (int i = 0; i < amountOfRows; i++)
            {
                this.posRow[i] = Integer.parseInt(fields[i]);
            }

            fields = in.readLine().split("\\s+");
            this.posCol = new int[amountOfCols];
            for (int i = 0; i < amountOfCols; i++)
            {
                this.posCol[i] = Integer.parseInt(fields[i]);
            }

            fields = in.readLine().split("\\s+");
            this.negRow = new int[amountOfRows];
            for (int i = 0; i < amountOfRows; i++)
            {
                this.negRow[i] = Integer.parseInt(fields[i]);
            }

            fields = in.readLine().split("\\s+");
            this.negCol = new int[amountOfCols];
            for (int i = 0; i < amountOfCols; i++)
            {
                this.negCol[i] = Integer.parseInt(fields[i]);
            }


            for (int row = 0; row < amountOfRows; row++) {
                fields = in.readLine().split("\\s+");
                char[] c = new char[amountOfCols];
                for (int i = 0; i < amountOfCols; i++)
                    c[i] = fields[i].charAt(0);
                for (int col = 0; col < amountOfCols; col++)
                    grid[row][col] = c[col];
            }

            for (int row = 0; row < amountOfRows; row++)
                for (int col = 0; col < amountOfCols; col++)
                    board[row][col] = EMPTY;

            this.cursorRow = 0;
            this.cursorCol = -1;

        } // <3 Jim
    }

    /**
     * The copy constructor which advances the cursor, creates a new grid,
     * and populates the grid at the cursor location with val
     * @param other the board to copy
     * @param val the value to store at new cursor location
     */
    private MagnetsConfig(MagnetsConfig other, char val) {
       this.amountOfRows = other.amountOfRows;
       this.amountOfCols = other.amountOfCols;
       this.cursorRow = other.cursorRow;
       this.cursorCol = other.cursorCol;

       this.cursorCol += 1;
       if (this.cursorCol == amountOfCols)
       {
           this.cursorRow += 1;
           this.cursorCol = 0;
       }
       this.board = new char[amountOfRows][amountOfCols];
       for (int row = 0; row < amountOfRows; row++)
           System.arraycopy(other.board[row], 0, this.board[row], 0, amountOfCols);

       if (val == POS)
       {
           this.board[this.cursorRow][this.cursorCol] = POS;
       }
       if (val == NEG)
       {
           this.board[this.cursorRow][cursorCol] = NEG;
       }
       if (val == BLANK)
       {
           this.board[cursorRow][cursorCol] = BLANK;
       }
    }


    /**
     * Generate the successor configs.  For minimal pruning, this should be
     * done in the order: +, - and X.
     *
     * @return the collection of successors
     */
    @Override
    public List<Configuration> getSuccessors() {
        List<Configuration> successors = new ArrayList<>();
        MagnetsConfig child = new MagnetsConfig(this, '+');
        MagnetsConfig child2 = new MagnetsConfig(this, '-');
        MagnetsConfig child3 = new MagnetsConfig(this, 'X');
        successors.add(child);
        successors.add(child2);
        successors.add(child3);
        return successors;
    }


    /**
     * Checks to make sure a successor is valid or not.  For minimal pruning,
     * each newly placed cell at the cursor needs to make sure its pair
     * is valid, and there is no polarity violation.  When the last cell is
     * populated, all row/col pos/negative counts are checked.
     *
     * @return whether this config is valid or not
     */
    @Override
    public boolean isValid() {
        // TODO
        return false;
    }

    @Override
    public boolean isGoal() {
        // TODO
        return false;
    }

    /**
     * Returns a string representation of the puzzle including all necessary info.
     *
     * @return the string
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        // top row
        result.append("+ ");
        for (int col = 0; col < getCols(); ++col) {
            result.append(getPosColCount(col) != IGNORED ? getPosColCount(col) : " ");
            if (col < getCols() - 1) {
                result.append(" ");
            }
        }
        result.append(System.lineSeparator());
        result.append("  ");
        for (int col = 0; col < getCols(); ++col) {
            if (col != getCols() - 1) {
                result.append("--");
            } else {
                result.append("-");
            }
        }
        result.append(System.lineSeparator());

        // middle rows
        for (int row = 0; row < getRows(); ++row) {
            result.append(getPosRowCount(row) != IGNORED ? getPosRowCount(row) : " ").append("|");
            for (int col = 0; col < getCols(); ++col) {
                result.append(getVal(row, col));
                if (col < getCols() - 1) {
                    result.append(" ");
                }
            }
            result.append("|").append(getNegRowCount(row) != IGNORED ? getNegRowCount(row) : " ");
            result.append(System.lineSeparator());
        }

        // bottom row
        result.append("  ");
        for (int col = 0; col < getCols(); ++col) {
            if (col != getCols() - 1) {
                result.append("--");
            } else {
                result.append("-");
            }
        }
        result.append(System.lineSeparator());

        result.append("  ");
        for (int col = 0; col < getCols(); ++col) {
            result.append(getNegColCount(col) != IGNORED ? getNegColCount(col) : " ").append(" ");
        }
        result.append(" -").append(System.lineSeparator());
        return result.toString();
    }

    // IMagnetTest

    @Override
    public int getRows() {
        return this.amountOfRows;
    }

    @Override
    public int getCols() {
        return this.amountOfCols;
    }

    @Override
    public int getPosRowCount(int row) {
        return this.posRow[row];
    }

    @Override
    public int getPosColCount(int col) {
        return this.posCol[col];
    }

    @Override
    public int getNegRowCount(int row) {
        return this.negRow[row];
    }

    @Override
    public int getNegColCount(int col) {
        return this.negCol[col];
    }

    @Override
    public char getPair(int row, int col) {
        return this.grid[row][col];
    }

    @Override
    public char getVal(int row, int col) {
        return this.board[row][col];
    }

    @Override
    public int getCursorRow() {
        return this.cursorRow;
    }

    @Override
    public int getCursorCol() {
        return this.cursorCol;
    }
}
