package PathX.ui;

import java.awt.event.KeyEvent;
import static PathX.PathXConstants.*;
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
        
        // AND CLOSE THE ALL
        System.exit(0);
    }

    /**
     * Called when the user clicks the X button.
     */
    public void respondToExitGameRequest() {
        System.exit(0);
    }

    public void respondToBackRequest() {
        // RESET THE GAME AND ITS DATA
        //game.reset();
        game.switchToSplashScreen();
        game.getInsideCanvas().setVisible(false);
    }
    
    public void respondToUpRequest(){
        if(game.getGUIDecor().get(BACKGROUND_GAME_TYPE).getY() <= 360){
           game.getGUIDecor().get(BACKGROUND_GAME_TYPE).setY(game.getGUIDecor().get(BACKGROUND_GAME_TYPE).getY() + 20);
        }
    }
    
    public void respondToDownRequest(){
        if(game.getGUIDecor().get(BACKGROUND_GAME_TYPE).getY() >= -800){
           game.getGUIDecor().get(BACKGROUND_GAME_TYPE).setY(game.getGUIDecor().get(BACKGROUND_GAME_TYPE).getY() - 20);
        }
    }
    
    public void respondToLeftRequest(){
        if(game.getGUIDecor().get(BACKGROUND_GAME_TYPE).getX() <= 1780){
            game.getGUIDecor().get(BACKGROUND_GAME_TYPE).setX(game.getGUIDecor().get(BACKGROUND_GAME_TYPE).getX() + 20);
        }
    }
    
    public void respondToRightRequest(){
        if(game.getGUIDecor().get(BACKGROUND_GAME_TYPE).getX() - 20 > -160){
            game.getGUIDecor().get(BACKGROUND_GAME_TYPE).setX(game.getGUIDecor().get(BACKGROUND_GAME_TYPE).getX() - 20);
        }
        
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
                        game.getAudio().play(PathX.pathXPropertyType.AUDIO_CUE_UNDO.toString(), false);
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
        if (keyCode == KeyEvent.VK_UP) {
            if(game.getGUIDecor().get(BACKGROUND_GAME_TYPE).getY() <= 360){
           game.getGUIDecor().get(BACKGROUND_GAME_TYPE).setY(game.getGUIDecor().get(BACKGROUND_GAME_TYPE).getY() + 20);
        }
        }

        if (keyCode == KeyEvent.VK_DOWN) {
            if(game.getGUIDecor().get(BACKGROUND_GAME_TYPE).getY() >= -800){
           game.getGUIDecor().get(BACKGROUND_GAME_TYPE).setY(game.getGUIDecor().get(BACKGROUND_GAME_TYPE).getY() - 20);
        }
        }
        if (keyCode == KeyEvent.VK_LEFT) {
            if(game.getGUIDecor().get(BACKGROUND_GAME_TYPE).getX() <= 1780){
            game.getGUIDecor().get(BACKGROUND_GAME_TYPE).setX(game.getGUIDecor().get(BACKGROUND_GAME_TYPE).getX() + 20);
        }
        }
        if (keyCode == KeyEvent.VK_RIGHT) {
            if(game.getGUIDecor().get(BACKGROUND_GAME_TYPE).getX() - 20 > -160){
            game.getGUIDecor().get(BACKGROUND_GAME_TYPE).setX(game.getGUIDecor().get(BACKGROUND_GAME_TYPE).getX() - 20);
        }
        }
    }
}

