package PathX.ui;

import java.awt.event.KeyEvent;
import static PathX.PathXConstants.GAME_SCREEN_STATE;
import static PathX.PathXConstants.MENU_SCREEN_STATE;
import static PathX.PathXConstants.VIEWPORT_INC;
import PathX.PathX;
import PathX.data.SortTransaction;
import PathX.data.PathXDataModel;
import PathX.file.PathXFileManager;

/**
 *
 * @author Richard McKenna & Eric Loo
 */
public class PathXEventHandler {
    // THE SORTING HAT GAME, IT PROVIDES ACCESS TO EVERYTHING

    private PathXMiniGame game;

    /**
     * Constructor, it just keeps the game for when the events happen.
     */
    public PathXEventHandler(PathXMiniGame initGame) {
        game = initGame;
    }

    /**
     * Called when the user clicks the close window button.
     */
    public void respondToExitRequest() {
        // IF THE GAME IS STILL GOING ON, END IT AS A LOSS
        if (game.getDataModel().inProgress()) {
            game.getDataModel().endGameAsLoss();
        }
        // AND CLOSE THE ALL
        System.exit(0);
    }

    /**
     * Called when the user clicks the New button.
     */
    public void respondToNewGameRequest() {
        // IF THERE IS A GAME UNDERWAY, COUNT IT AS A LOSS
        if (game.getDataModel().inProgress()) {
            game.getDataModel().endGameAsLoss();
        }
        // RESET THE GAME AND ITS DATA
        game.reset();
    }

    public void respondToBackRequest() {
        // IF THERE IS A GAME UNDERWAY, COUNT IT AS A LOSS
        if (game.getDataModel().inProgress()) {
            game.getDataModel().endGameAsLoss();
        }
        // RESET THE GAME AND ITS DATA
        //game.reset();
        game.switchToSplashScreen();
    }

    /**
     * Called when the user clicks a button to select a level.
     */
    public void respondToSelectLevelRequest() {
        // WE ONLY LET THIS HAPPEN IF THE MENU SCREEN IS VISIBLE
        if (game.isCurrentScreenState(MENU_SCREEN_STATE)) {
            // GET THE GAME'S DATA MODEL, WHICH IS ALREADY LOCKED FOR US
            PathXDataModel data = (PathXDataModel) game.getDataModel();

            // UPDATE THE DATA
            PathXFileManager fileManager = game.getFileManager();
            //fileManager.loadLevel(levelFile);
            //data.reset(game);

            // GO TO THE GAME
            game.switchToGameScreen();
        }
    }
    public void respondToResetRequest(){
        
    }
    public void respondToHelpRequest(){
        game.switchToHelpScreen();
    }
    public void respondToSettingsRequest(){
        game.switchToSettingScreen();
    }
    
    /**
     * Called when the user clicks the Stats button.
     */
    public void respondToDisplayStatsRequest() {
        // DISPLAY THE STATS
        game.displayStats();
    }

    public void respondToUndoRequest() {
        PathXDataModel data = (PathXDataModel) game.getDataModel();
        if (data.inProgress()) {
                if (data.processUndo()) {
                    // FIND A MOVE IF THERE IS ONE
                    SortTransaction move = data.getPreviousSwapTransaction();
                    if (move != null) {
                        data.swapTiles(move.getFromIndex(), move.getToIndex());
                        game.getAudio().play(PathX.SortingHatPropertyType.AUDIO_CUE_UNDO.toString(), false);
                    }
                }
            }
    }
    /**
     * Called when the user presses a key on the keyboard.
     */
    public void respondToKeyPress(int keyCode) {
        PathXDataModel data = (PathXDataModel) game.getDataModel();

        // CHEAT BY ONE MOVE. NOTE THAT IF WE HOLD THE C
        // KEY DOWN IT WILL CONTINUALLY CHEAT
        if (keyCode == KeyEvent.VK_C) {
            // ONLY DO THIS IF THE GAME IS NO OVER
            if (data.inProgress()) {
                // FIND A MOVE IF THERE IS ONE
                SortTransaction move = data.getNextSwapTransaction();
                if (move != null) {
                    data.swapTiles(move.getFromIndex(), move.getToIndex());
                    game.getAudio().play(PathX.SortingHatPropertyType.AUDIO_CUE_CHEAT.toString(), false);
                }
            }
        }

        if (keyCode == KeyEvent.VK_U) {
            // ONLY DO THIS IF THE GAME IS NO OVER
            if (data.inProgress()) {
                if (data.processUndo()) {
                    // FIND A MOVE IF THERE IS ONE
                    SortTransaction move = data.getPreviousSwapTransaction();
                    if (move != null) {
                        data.swapTiles(move.getFromIndex(), move.getToIndex());
                        game.getAudio().play(PathX.SortingHatPropertyType.AUDIO_CUE_UNDO.toString(), false);
                    }
                }
            }
        }
    }
}
