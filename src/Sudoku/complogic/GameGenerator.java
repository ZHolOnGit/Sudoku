package Sudoku.complogic;

import Sudoku.problemdomain.Coordinates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static Sudoku.problemdomain.SudokuGame.GRID_BOUNDARY;

public class GameGenerator {
    public static int[][] getNewGameGrid(){
        return unsolveGame(getSolvedGame());
    }

    private static int[][] unsolveGame(int[][] solvedGame) {
        Random random = new Random(System.currentTimeMillis());

        boolean solvable = false;
        int[][] solvableArray = new int[GRID_BOUNDARY][GRID_BOUNDARY];

        while (solvable == false) {
            SudokuUtilities.copySudokuArrayValues(solvedGame, solvableArray);

            int index = 0;

            while (index < 40) {
                int xCoordinate = random.nextInt(GRID_BOUNDARY);
                int yCoordinate = random.nextInt(GRID_BOUNDARY);

                if (solvableArray[xCoordinate][yCoordinate] != 0) {
                    solvableArray[xCoordinate][yCoordinate] = 0;
                    index++;
                }
            }
            int[][] toBeSolved = new int[GRID_BOUNDARY][GRID_BOUNDARY];
            SudokuUtilities.copySudokuArrayValues(solvableArray,toBeSolved);

            solvable = SudokuSolver.puzzleIsSolvable(toBeSolved);
            System.out.println(solvable);

        }
        System.out.println("solvable");
        System.out.println(Arrays.deepToString(solvableArray));
        return solvableArray;
    }

    /**
     * Generates a 9x9 array, for each value 1-9 allocate that value 9 times
     * Selects random value in array, if empty, allocates a value
     * if this allocation does not create errors, it is saved
     */

    private static int[][] getSolvedGame() {
        Random random = new Random(System.currentTimeMillis());
        int[][] newGrid = new int[GRID_BOUNDARY][GRID_BOUNDARY];

        //Value is the potential value for each square, each must be allocated 9 times.
        for (int value = 1; value <= GRID_BOUNDARY;value++){
            //allocations is the number of times a square is given a value
            int allocations = 0;
            //if too many allocations are made that result in an invalid game
            //we grab the most recent allocations and reset them
            int interrupt = 0;
            //keep track of current allocations
            List<Coordinates> allocTracker = new ArrayList<>();

            //Failsafe, if reaches 500 grid is nuked and process restarted.
            int attempts = 0;

            while (allocations < GRID_BOUNDARY){

                if (interrupt> 200){
                    allocTracker.forEach(coordinates -> {
                        newGrid[coordinates.getX()][coordinates.getY()] = 0;});

                    interrupt = 0;
                    allocations = 0;
                    allocTracker.clear();
                    attempts ++;

                    if (attempts > 500){
                        clearArray(newGrid);
                        attempts = 0;
                        value = 1;
                    }
                }
                int xCoordinate = random.nextInt(GRID_BOUNDARY);
                int yCoordinate = random.nextInt(GRID_BOUNDARY);

                if (newGrid[xCoordinate][yCoordinate] == 0){
                    newGrid[xCoordinate][yCoordinate] = value;

                    if (GameLogic.sudokuIsInvalid(newGrid)){
                        newGrid[xCoordinate][yCoordinate] = 0;
                        interrupt++;
                    }else{
                        allocTracker.add(new Coordinates(xCoordinate,yCoordinate));
                        allocations++;
                    }
                }

            }

        }

        return newGrid;
    }

    private static void clearArray(int[][] newGrid) {
        for (int xIndex = 0; xIndex < GRID_BOUNDARY; xIndex++){
            for (int yIndex = 0; yIndex < GRID_BOUNDARY; yIndex++){
                newGrid[xIndex][yIndex] = 0;
            }
        }
    }


}
