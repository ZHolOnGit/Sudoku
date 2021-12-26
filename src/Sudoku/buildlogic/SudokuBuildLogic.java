package Sudoku.buildlogic;

import Sudoku.Persistance.LocalStorageImpl;
import Sudoku.complogic.GameLogic;
import Sudoku.problemdomain.IStorage;
import Sudoku.problemdomain.SudokuGame;
import Sudoku.userinterface.IUserInterfaceContract;
import Sudoku.userinterface.logic.ControlLogic;

import java.io.IOException;
import java.util.Arrays;

public class SudokuBuildLogic {
    public static void build (IUserInterfaceContract.View userInterface) throws IOException{
        System.out.println("At build");
        SudokuGame initialState;
        IStorage storage = new LocalStorageImpl();

        try {
            //checks to see if there is a game in storage
            initialState = storage.getGameData();
        }catch (IOException e){
            //if not, creates a new game
            initialState = GameLogic.getNewGame();
            storage.updateGameData(initialState);

        }

        IUserInterfaceContract.EventListener uiLogic =
                new ControlLogic(storage,userInterface);

        userInterface.setListener(uiLogic);
        userInterface.updateBoard(initialState);

    }


}
