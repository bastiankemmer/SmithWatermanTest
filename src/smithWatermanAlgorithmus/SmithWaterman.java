package smithWatermanAlgorithmus;


/**
 * lokales paarweises Alignment mit dem Smith-Waterman-Algorithmus
 */

import gui.BackTrackException;
import gui.EmptyAlignmentException;

/**
 * Design Note: this class implements AminoAcids interface: a simple fix customized to amino acids, since that is all we deal with in this class
 * Supporting both DNA and Aminoacids, will require a more general design.
 */

public class SmithWaterman {

    /**
     * The first input string
     */
    private String str1;


    /**
     * The second input String
     */
    private String str2;

    /**
     * The lengths of the input strings
     */
    private int length1, length2;

    /**
     * The score matrix. The true scores should be divided by the normalization
     * factor.
     */
    private double[][] score;

    /**
     * The normalization factor. To get the true score, divide the integer score
     * used in computation by the normalization factor.
     */
    static final double NORM_FACTOR = 1.0;

    /**
     * The similarity function constants. They are amplified by the
     * normalization factor to be integers.
     */
    static final int MATCH_SCORE = 2;
    static final int MISMATCH_SCORE = -1;
    static final int INDEL_SCORE = -1;

    /**
     * Constants of directions. Multiple directions are stored by bits. The zero
     * direction is the starting point.
     */
    static final int DR_LEFT = 1; // 0001
    static final int DR_UP = 2; // 0010
    static final int DR_DIAG = 4; // 0100
    static final int DR_ZERO = 8; // 1000

    /**
     * The directions pointing to the cells that give the maximum score at the
     * current cell. The first index is the column index. The second index is
     * the row index.
     */
    private int[][] prevCells;

    public SmithWaterman(String str1, String str2) {

        this.str1 = str1;
        this.str2 = str2;

        length1 = str1.length();
        length2 = str2.length();

        score = new double[length1 + 1][length2 + 1];
        prevCells = new int[length1 + 1][length2 + 1];

        buildMatrix();
    }

    /**
     * Compute the similarity score of substitution: use a substitution matrix
     * if the cost model The position of the first character is 1. A position of
     * 0 represents a gap.
     *
     * @param i
     *            Position of the character in sequence1
     * @param j
     *            Position of the character in sequence2
     * @return Cost of substitution of the character in sequence1 by the one in sequence2
     */
    private double similarity(int i, int j) {
        if (i == 0 || j == 0) {
            // it's a gap (indel)
            return INDEL_SCORE;
        }

        return (str1.charAt(i - 1) == str2.charAt(j - 1)) ? MATCH_SCORE : MISMATCH_SCORE;
    }

    /**
     * Build the score matrix using dynamic programming. Note: The indel scores
     * must be negative. Otherwise, the part handling the first row and column
     * has to be modified.
     */
    private void buildMatrix() {
        if (INDEL_SCORE >= 0) {
            throw new Error("Indel score must be negative");
        }

        // if (isDistanceMatrixNull()) {
        // throw new Error ("Distance Matrix is NULL");
        // }

        int i; // length of prefix substring of sequence1
        int j; // length of prefix substring of sequence2

        // base case
        score[0][0] = 0;
        prevCells[0][0] = DR_ZERO; // starting point

        // the first row
        for (i = 1; i <= length1; i++) {
            score[i][0] = 0;
            prevCells[i][0] = DR_ZERO;
        }

        // the first column
        for (j = 1; j <= length2; j++) {
            score[0][j] = 0;
            prevCells[0][j] = DR_ZERO;
        }

        // the rest of the matrix
        for (i = 1; i <= length1; i++) {
            for (j = 1; j <= length2; j++) {
                    /*
					 * 4 Cycle Method
					 * double diagScore = score[i - 1][j - 1] + similarity(i, j);
					 * double upScore = score[i][j - 1] + similarity(0, j); double
					 * leftScore = score[i - 1][j] + similarity(i, 0);
					 * 
					 * score[i][j] = Math.max(diagScore, Math.max(upScore,
					 * Math.max(leftScore, 0)));
					 */

                // 1. Cycle
                double diagScore = score[i - 1][j - 1] + similarity(i, j);
                double upScore = score[i][j - 1];
                double leftScore = score[i - 1][j];
                double compareUpAndLeft = Math.max(upScore, leftScore) + INDEL_SCORE;
                if (compareUpAndLeft < 0)
                    compareUpAndLeft = 0;
                score[i][j] = Math.max(compareUpAndLeft, diagScore);
                prevCells[i][j] = 0;

                // find the directions that give the maximum scores.
                // the bitwise OR operator is used to record multiple
                // directions.
                if (diagScore == score[i][j]) {
                    prevCells[i][j] |= DR_DIAG;
                }
                if (leftScore == score[i][j] - INDEL_SCORE) {
                    prevCells[i][j] |= DR_LEFT;
                }
                if (upScore == score[i][j] - INDEL_SCORE) {
                    prevCells[i][j] |= DR_UP;
                }
                if (0 == score[i][j]) {
                    prevCells[i][j] |= DR_ZERO;
                }
            }
        }
    }

    /**
     * Get the maximum value in the score matrix.
     */
    private double getMaxScore() {
        double maxScore = 0;

        // skip the first row and column
        for (int i = 1; i <= length1; i++) {
            for (int j = 1; j <= length2; j++) {
                if (score[i][j] > maxScore) {
                    maxScore = score[i][j];
                }
            }
        }

        return maxScore;
    }

    /**
     * Output the local alignments ending in the (i, j) cell. aligned1 and
     * aligned2 are suffixes of final aligned strings found in backtracking
     * before calling this function. Note: the strings are replicated at each
     * recursive call. Use buffers or stacks to improve efficiency.
     */

    private String printAlignments(int i, int j, String aligned1, String aligned2) throws BackTrackException {
        // we've reached the starting point, so print the alignments

        if ((prevCells[i][j] & DR_ZERO) > 0) {
            System.out.println(aligned1);
            System.out.println(aligned2);
            System.out.println("");

            // Note: we could check other directions for longer alignments
            // with the same score. we don't do it here.
            return aligned1 + "\n" + aligned2;
        } else {
            // find out which directions to backtrack
            if ((prevCells[i][j] & DR_LEFT) > 0) {
                return printAlignments(i - 1, j, str1.charAt(i - 1) + aligned1, "_" + aligned2);
            }
            if ((prevCells[i][j] & DR_UP) > 0) {
                return printAlignments(i, j - 1, "_" + aligned1, str2.charAt(j - 1) + aligned2);
            }
            if ((prevCells[i][j] & DR_DIAG) > 0) {
                return printAlignments(i - 1, j - 1, str1.charAt(i - 1) + aligned1, str2.charAt(j - 1) + aligned2);
            }
            throw new BackTrackException("");
        }

    }

    /**
     * Output the local alignments with the maximum score.
     */
    public String printAlignments() throws EmptyAlignmentException, BackTrackException {

        // find the cell with the maximum score
        double maxScore = getMaxScore();

			/*
			 * for (int i = 0; i < matches.length; i++) {
			 * System.out.println("Match #" + i + ":" + matches.get(i)); }
			 */

        // skip the first row and column
        for (int i = 1; i <= length1; i++) {
            for (int j = 1; j <= length2; j++) {
                if (score[i][j] == maxScore) {
                    return printAlignments(i, j, "", "");
                }
            }
        }
        // Note: empty alignments are not printed.
        throw new EmptyAlignmentException("No Alignments found for:\nSequence 1: " + str1 + "\nSequence 2: " + str2);
    }

    /**
     * return the dynamic programming matrix
     */
    public String printDPMatrix() {
        // Replace System.out.print with StringBuilder.append to create a dynamic programming matrix
        System.out.print("   ");
        StringBuilder dpMatrix = new StringBuilder("   ");
        for (int j = 1; j <= length2; j++) {
            System.out.print("   " + str2.charAt(j - 1));
            dpMatrix.append("   ").append(str2.charAt(j - 1));
        }
        System.out.println();
        dpMatrix.append("\n");
        for (int i = 0; i <= length1; i++) {
            if (i > 0) {
                System.out.print(str1.charAt(i - 1) + " ");
                dpMatrix.append(str1.charAt(i - 1)).append(" ");
            } else {
                System.out.print("  ");
                dpMatrix.append("  ");
            }
            for (int j = 0; j <= length2; j++) {
                System.out.print(score[i][j] / NORM_FACTOR + " ");
                dpMatrix.append(score[i][j] / NORM_FACTOR).append(" ");
            }
            System.out.println();
            dpMatrix.append("\n");
        }
        return dpMatrix.toString();
    }
}