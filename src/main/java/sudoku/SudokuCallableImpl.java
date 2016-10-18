package sudoku;

import experiment.CallableImpl;

/**
 * Created by spirals on 08/04/16.
 */
public class SudokuCallableImpl extends CallableImpl<int[][],int[][]> {

    public SudokuCallableImpl(int[][] input) {
        super(input);
    }

    @Override
    public int[][] call() throws Exception {
        SudokuInstr sudokuVar = new SudokuInstr(super.input);
        sudokuVar.initSubsets();
        sudokuVar.solve();
        return sudokuVar.getGrid();
    }
}
