package PathX.ui;

import java.awt.event.KeyEvent;
import static PathX.PathXConstants.*;
import static PathX.PathXConstants.MENU_SCREEN_STATE;
import PathX.PathX;
import PathX.PathX.pathXPropertyType;
import PathX.data.PathXDataModel;
import PathX.file.PathXFileManager;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.JPanel;
import mini_game.Sprite;
import properties_manager.PropertiesManager;

/**
 *
 * @author Richard McKenna & Eric Loo
 */
public class PathXEventHandler {

    private PathXMiniGame game;

    private PropertiesManager props = PropertiesManager.getPropertiesManager();

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
        game.getAudio().play(pathXPropertyType.AUDIO_CUE_SELECT_TILE.toString(), false);
        game.switchToSplashScreen();
        game.getInsideCanvas().setVisible(false);
    }

    public void respondToBackToLevelSelectRequest() {
        game.getAudio().play(pathXPropertyType.AUDIO_CUE_SELECT_TILE.toString(), false);
        game.switchToGameScreen();
    }

    /**
     * Called when the user clicks a button to select a level.
     */
    public void respondToSelectLevelRequest() {
        // WE ONLY LET THIS HAPPEN IF THE MENU SCREEN IS VISIBLE
        if (game.isCurrentScreenState(MENU_SCREEN_STATE)) {
            game.getAudio().play(pathXPropertyType.AUDIO_CUE_SELECT_TILE.toString(), false);
            game.switchToGameScreen();
        }
    }

    public void respondToLevel1() {
        game.getAudio().play(pathXPropertyType.AUDIO_CUE_SELECT_TILE.toString(), false);
        game.switchToLevel1();
    }

    public void respondToLevel2() {
        game.getAudio().play(pathXPropertyType.AUDIO_CUE_SELECT_TILE.toString(), false);
        game.switchToLevel2();
    }

    public void respondToLevel3() {
        game.getAudio().play(pathXPropertyType.AUDIO_CUE_SELECT_TILE.toString(), false);
        game.switchToLevel3();
    }

    public void respondToLevel4() {
        game.getAudio().play(pathXPropertyType.AUDIO_CUE_SELECT_TILE.toString(), false);
        game.switchToLevel4();
    }

    public void respondToLevel5() {
        game.getAudio().play(pathXPropertyType.AUDIO_CUE_SELECT_TILE.toString(), false);
        game.switchToLevel5();
    }

    public void respondToLevel6() {
        game.getAudio().play(pathXPropertyType.AUDIO_CUE_SELECT_TILE.toString(), false);
        game.switchToLevel6();
    }

    public void respondToLevel7() {
        game.getAudio().play(pathXPropertyType.AUDIO_CUE_SELECT_TILE.toString(), false);
        game.switchToLevel7();
    }

    public void respondToLevel8() {
        game.getAudio().play(pathXPropertyType.AUDIO_CUE_SELECT_TILE.toString(), false);
        game.switchToLevel8();
    }

    public void respondToLevel9() {
        game.getAudio().play(pathXPropertyType.AUDIO_CUE_SELECT_TILE.toString(), false);
        game.switchToLevel9();
    }

    public void respondToLevel10() {
        game.getAudio().play(pathXPropertyType.AUDIO_CUE_SELECT_TILE.toString(), false);
        game.switchToLevel10();
    }

    public void respondToLevel11() {
        game.getAudio().play(pathXPropertyType.AUDIO_CUE_SELECT_TILE.toString(), false);
        game.switchToLevel11();
    }

    public void respondToLevel12() {
        game.getAudio().play(pathXPropertyType.AUDIO_CUE_SELECT_TILE.toString(), false);
        game.switchToLevel12();
    }

    public void respondToLevel13() {
        game.getAudio().play(pathXPropertyType.AUDIO_CUE_SELECT_TILE.toString(), false);
        game.switchToLevel13();
    }

    public void respondToLevel14() {
        game.getAudio().play(pathXPropertyType.AUDIO_CUE_SELECT_TILE.toString(), false);
        game.switchToLevel14();
    }

    public void respondToLevel15() {
        game.getAudio().play(pathXPropertyType.AUDIO_CUE_SELECT_TILE.toString(), false);
        game.switchToLevel15();
    }

    public void respondToLevel16() {
        game.getAudio().play(pathXPropertyType.AUDIO_CUE_SELECT_TILE.toString(), false);
        game.switchToLevel16();
    }

    public void respondToLevel17() {
        game.getAudio().play(pathXPropertyType.AUDIO_CUE_SELECT_TILE.toString(), false);
        game.switchToLevel17();
    }

    public void respondToLevel18() {
        game.getAudio().play(pathXPropertyType.AUDIO_CUE_SELECT_TILE.toString(), false);
        game.switchToLevel18();
    }

    public void respondToLevel19() {
        game.getAudio().play(pathXPropertyType.AUDIO_CUE_SELECT_TILE.toString(), false);
        game.switchToLevel19();
    }

    public void respondToLevel20() {
        game.getAudio().play(pathXPropertyType.AUDIO_CUE_SELECT_TILE.toString(), false);
        game.switchToLevel20();
    }

    public void respondToResetRequest() {
        game.getAudio().play(pathXPropertyType.AUDIO_CUE_SELECT_TILE.toString(), false);
    }

    public void respondToHelpRequest() {
        game.getAudio().play(pathXPropertyType.AUDIO_CUE_SELECT_TILE.toString(), false);
        game.switchToHelpScreen();
    }

    public void respondToSettingsRequest() {
        game.getAudio().play(pathXPropertyType.AUDIO_CUE_SELECT_TILE.toString(), false);
        game.switchToSettingScreen();
    }

    /**
     * Called when the user clicks the Stats button.
     */
    public void respondToDisplayStatsRequest() {
        // DISPLAY THE STATS
        game.displayStats();
    }

    public void respondToUpRequest() {
        if (game.getGUIDecor().get(BACKGROUND_GAME_TYPE).getY() <= 360 && game.getInsideCanvas().getRenderedBackground().equals(BACKGROUND_GAME_TYPE)) {
            game.getGUIDecor().get(BACKGROUND_GAME_TYPE).setY(game.getGUIDecor().get(BACKGROUND_GAME_TYPE).getY() + 20);
            ArrayList<String> gameLevels = props.getPropertyOptionsList(PathX.pathXPropertyType.GAME_LEVELS);
            for (String level : gameLevels) {
                game.getGUIButtons().get(level).setY(game.getGUIButtons().get(level).getY() + 20);
            }
        } else if (!game.getInsideCanvas().isEnabled()) {
            for (int i = 1; i <= 20; i++) {
                if (game.getPXG().getViewport().getViewportY() > -100) {
                    game.getPXG().getViewport().move(0, -20);
                }
            }
        }
    }

    public void respondToDownRequest() {
        if (game.getGUIDecor().get(BACKGROUND_GAME_TYPE).getY() >= -800 && game.getInsideCanvas().getRenderedBackground().equals(BACKGROUND_GAME_TYPE)) {
            game.getGUIDecor().get(BACKGROUND_GAME_TYPE).setY(game.getGUIDecor().get(BACKGROUND_GAME_TYPE).getY() - 20);
            ArrayList<String> gameLevels = props.getPropertyOptionsList(PathX.pathXPropertyType.GAME_LEVELS);
            for (String level : gameLevels) {
                game.getGUIButtons().get(level).setY(game.getGUIButtons().get(level).getY() - 20);
            }
        } else if (!game.getInsideCanvas().isEnabled()) {
            for (int i = 1; i <= 20; i++) {
                if (game.getPXG().getViewport().getViewportY() < 300) {
                    game.getPXG().getViewport().move(0, 20);
                }
            }
        }
    }

    public void respondToLeftRequest() {
        if (game.getGUIDecor().get(BACKGROUND_GAME_TYPE).getX() <= 1780 && game.getInsideCanvas().getRenderedBackground().equals(BACKGROUND_GAME_TYPE)) {
            game.getGUIDecor().get(BACKGROUND_GAME_TYPE).setX(game.getGUIDecor().get(BACKGROUND_GAME_TYPE).getX() + 20);
            ArrayList<String> gameLevels = props.getPropertyOptionsList(PathX.pathXPropertyType.GAME_LEVELS);
            for (String level : gameLevels) {
                game.getGUIButtons().get(level).setX(game.getGUIButtons().get(level).getX() + 20);
            }
        } else if (!game.getInsideCanvas().isEnabled()) {
            for (int i = 1; i <= 20; i++) {
                if (game.getPXG().getViewport().getViewportX() > 0) {
                    game.getPXG().getViewport().move(-20, 0);
                }
            }
        }
    }

    public void respondToRightRequest() {
        if (game.getGUIDecor().get(BACKGROUND_GAME_TYPE).getX() - 20 > -160 && game.getInsideCanvas().getRenderedBackground().equals(BACKGROUND_GAME_TYPE)) {
            game.getGUIDecor().get(BACKGROUND_GAME_TYPE).setX(game.getGUIDecor().get(BACKGROUND_GAME_TYPE).getX() - 20);
            ArrayList<String> gameLevels = props.getPropertyOptionsList(PathX.pathXPropertyType.GAME_LEVELS);
            for (String level : gameLevels) {
                game.getGUIButtons().get(level).setX(game.getGUIButtons().get(level).getX() - 20);
            }
        } else if (!game.getInsideCanvas().isEnabled()) {
            for (int i = 1; i <= 20; i++) {
                if (game.getPXG().getViewport().getViewportX() < 600) {
                    game.getPXG().getViewport().move(20, 0);
                }
            }
        }

    }

    public void respondToPauseRequest() {
        if (game.getDataModel().isPaused()) {
            game.getDataModel().unpause();
        } else {
            game.getDataModel().pause();
        }
    }

    /**
     * Called when the user presses a key on the keyboard.
     */
    public void respondToKeyPress(int keyCode) {

        // CHEAT BY ONE MOVE. NOTE THAT IF WE HOLD THE C
        // KEY DOWN IT WILL CONTINUALLY CHEAT
        if (keyCode == KeyEvent.VK_UP) {
            if (game.getGUIDecor().get(BACKGROUND_GAME_TYPE).getY() <= 360 && game.getInsideCanvas().getRenderedBackground().equals(BACKGROUND_GAME_TYPE)) {
                game.getGUIDecor().get(BACKGROUND_GAME_TYPE).setY(game.getGUIDecor().get(BACKGROUND_GAME_TYPE).getY() + 20);
                ArrayList<String> gameLevels = props.getPropertyOptionsList(PathX.pathXPropertyType.GAME_LEVELS);
                for (String level : gameLevels) {
                    game.getGUIButtons().get(level).setY(game.getGUIButtons().get(level).getY() + 20);
                }
            } else if (!game.getInsideCanvas().isEnabled()) {
                if (game.getPXG().getViewport().getViewportY() > -100) {
                    game.getPXG().getViewport().move(0, -20);
                }
            }
        }

        if (keyCode == KeyEvent.VK_DOWN) {
            if (game.getGUIDecor().get(BACKGROUND_GAME_TYPE).getY() >= -800 && game.getInsideCanvas().getRenderedBackground().equals(BACKGROUND_GAME_TYPE)) {
                game.getGUIDecor().get(BACKGROUND_GAME_TYPE).setY(game.getGUIDecor().get(BACKGROUND_GAME_TYPE).getY() - 20);
                ArrayList<String> gameLevels = props.getPropertyOptionsList(PathX.pathXPropertyType.GAME_LEVELS);
                for (String level : gameLevels) {
                    game.getGUIButtons().get(level).setY(game.getGUIButtons().get(level).getY() - 20);
                }
            } else if (!game.getInsideCanvas().isEnabled()) {
                if (game.getPXG().getViewport().getViewportY() < 300) {
                    game.getPXG().getViewport().move(0, 20);
                }
            }
        }
        if (keyCode == KeyEvent.VK_LEFT) {
            if (game.getGUIDecor().get(BACKGROUND_GAME_TYPE).getX() <= 1780 && game.getInsideCanvas().getRenderedBackground().equals(BACKGROUND_GAME_TYPE)) {
                game.getGUIDecor().get(BACKGROUND_GAME_TYPE).setX(game.getGUIDecor().get(BACKGROUND_GAME_TYPE).getX() + 20);
                ArrayList<String> gameLevels = props.getPropertyOptionsList(PathX.pathXPropertyType.GAME_LEVELS);
                for (String level : gameLevels) {
                    game.getGUIButtons().get(level).setX(game.getGUIButtons().get(level).getX() + 20);
                }
            } else if (!game.getInsideCanvas().isEnabled()) {
                if (game.getPXG().getViewport().getViewportX() > 0) {
                    game.getPXG().getViewport().move(-20, 0);
                }

            }
        }
        if (keyCode == KeyEvent.VK_RIGHT) {
            if (game.getGUIDecor().get(BACKGROUND_GAME_TYPE).getX() - 20 > -160
                    && game.getInsideCanvas().getRenderedBackground().equals(BACKGROUND_GAME_TYPE)) {
                game.getGUIDecor().get(BACKGROUND_GAME_TYPE).setX(game.getGUIDecor().get(BACKGROUND_GAME_TYPE).getX() - 20);
                ArrayList<String> gameLevels = props.getPropertyOptionsList(PathX.pathXPropertyType.GAME_LEVELS);
                for (String level : gameLevels) {
                    game.getGUIButtons().get(level).setX(game.getGUIButtons().get(level).getX() - 20);
                }
            } else if (!game.getInsideCanvas().isEnabled()) {
                if (game.getPXG().getViewport().getViewportX() < 600) {
                    game.getPXG().getViewport().move(20, 0);
                }

            }
        }
        if (keyCode == KeyEvent.VK_P) {
            if (game.getDataModel().isPaused()) {
                game.getDataModel().unpause();
            } // TOGGLE IT ON
            else {
                game.getDataModel().pause();
            }
        }
        if (keyCode == KeyEvent.VK_M) {
            if (game.getAudio().isPlaying(PathX.pathXPropertyType.SONG_CUE_MENU_SCREEN.toString())
                    || game.getAudio().isPlaying(PathX.pathXPropertyType.SONG_CUE_GAME_SCREEN.toString())) {
                game.getAudio().stop(PathX.pathXPropertyType.SONG_CUE_MENU_SCREEN.toString());
                game.getAudio().stop(PathX.pathXPropertyType.SONG_CUE_GAME_SCREEN.toString());
                game.setMuted(true);
            } else {
                game.setMuted(false);
                if (game.getInsideCanvas().getRenderedBackground().equals(BACKGROUND_GAME_TYPE)) {
                    game.getAudio().play(PathX.pathXPropertyType.SONG_CUE_MENU_SCREEN.toString(), true);
                } else if (!game.isIsOnLevelSelect() && game.getPXG().isEnabled()) {
                    game.getAudio().play(PathX.pathXPropertyType.SONG_CUE_GAME_SCREEN.toString(), true);
                } else {
                    game.getAudio().play(PathX.pathXPropertyType.SONG_CUE_MENU_SCREEN.toString(), true);
                }
            }
        }
        if (keyCode == KeyEvent.VK_C) {
            for (int i = 0; i < game.getData().getLevelsLocked().length; i++) {
                game.getData().setLevelsLocked(false, i);
            }
            game.switchToGameScreen();
        }

    }

}
