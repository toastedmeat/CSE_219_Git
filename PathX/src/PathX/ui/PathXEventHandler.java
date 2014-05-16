package PathX.ui;

import java.awt.event.KeyEvent;
import static PathX.PathXConstants.*;
import static PathX.PathXConstants.MENU_SCREEN_STATE;
import PathX.PathX;
import PathX.PathX.pathXPropertyType;
import PathX.data.PathXDataModel;
import PathX.data.Road;
import PathX.file.PathXFileManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
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
        if (!game.isSoundMuted()) {
            game.getAudio().play(pathXPropertyType.AUDIO_CUE_SELECT_TILE.toString(), false);
        }
        game.switchToSplashScreen();
        game.getInsideCanvas().setVisible(false);
    }

    public void respondToBackToLevelSelectRequest() {
        if (!game.isSoundMuted()) {
            game.getAudio().play(pathXPropertyType.AUDIO_CUE_SELECT_TILE.toString(), false);
        }
        game.switchToGameScreen();
    }

    /**
     * Called when the user clicks a button to select a level.
     */
    public void respondToSelectLevelRequest() {
        // WE ONLY LET THIS HAPPEN IF THE MENU SCREEN IS VISIBLE
        if (game.isCurrentScreenState(MENU_SCREEN_STATE)) {
            if (!game.isSoundMuted()) {
                game.getAudio().play(pathXPropertyType.AUDIO_CUE_SELECT_TILE.toString(), false);
            }
            game.switchToGameScreen();
        }
    }

    public void respondToLevel1() {
        if (!game.isSoundMuted()) {
            game.getAudio().play(pathXPropertyType.AUDIO_CUE_SELECT_TILE.toString(), false);
        }
        game.switchToLevel1();
    }

    public void respondToLevel2() {
        if (!game.isSoundMuted()) {
            game.getAudio().play(pathXPropertyType.AUDIO_CUE_SELECT_TILE.toString(), false);
        }
        game.switchToLevel2();
    }

    public void respondToLevel3() {
        if (!game.isSoundMuted()) {
            game.getAudio().play(pathXPropertyType.AUDIO_CUE_SELECT_TILE.toString(), false);
        }
        game.switchToLevel3();
    }

    public void respondToLevel4() {
        if (!game.isSoundMuted()) {
            game.getAudio().play(pathXPropertyType.AUDIO_CUE_SELECT_TILE.toString(), false);
        }
        game.switchToLevel4();
    }

    public void respondToLevel5() {
        if (!game.isSoundMuted()) {
            game.getAudio().play(pathXPropertyType.AUDIO_CUE_SELECT_TILE.toString(), false);
        }
        game.switchToLevel5();
    }

    public void respondToLevel6() {
        if (!game.isSoundMuted()) {
            game.getAudio().play(pathXPropertyType.AUDIO_CUE_SELECT_TILE.toString(), false);
        }
        game.switchToLevel6();
    }

    public void respondToLevel7() {
        if (!game.isSoundMuted()) {
            game.getAudio().play(pathXPropertyType.AUDIO_CUE_SELECT_TILE.toString(), false);
        }
        game.switchToLevel7();
    }

    public void respondToLevel8() {
        if (!game.isSoundMuted()) {
            game.getAudio().play(pathXPropertyType.AUDIO_CUE_SELECT_TILE.toString(), false);
        }
        game.switchToLevel8();
    }

    public void respondToLevel9() {
        if (!game.isSoundMuted()) {
            game.getAudio().play(pathXPropertyType.AUDIO_CUE_SELECT_TILE.toString(), false);
        }
        game.switchToLevel9();
    }

    public void respondToLevel10() {
        if (!game.isSoundMuted()) {
            game.getAudio().play(pathXPropertyType.AUDIO_CUE_SELECT_TILE.toString(), false);
        }
        game.switchToLevel10();
    }

    public void respondToLevel11() {
        if (!game.isSoundMuted()) {
            game.getAudio().play(pathXPropertyType.AUDIO_CUE_SELECT_TILE.toString(), false);
        }
        game.switchToLevel11();
    }

    public void respondToLevel12() {
        if (!game.isSoundMuted()) {
            game.getAudio().play(pathXPropertyType.AUDIO_CUE_SELECT_TILE.toString(), false);
        }
        game.switchToLevel12();
    }

    public void respondToLevel13() {
        if (!game.isSoundMuted()) {
            game.getAudio().play(pathXPropertyType.AUDIO_CUE_SELECT_TILE.toString(), false);
        }
        game.switchToLevel13();
    }

    public void respondToLevel14() {
        if (!game.isSoundMuted()) {
            game.getAudio().play(pathXPropertyType.AUDIO_CUE_SELECT_TILE.toString(), false);
        }
        game.switchToLevel14();
    }

    public void respondToLevel15() {
        if (!game.isSoundMuted()) {
            game.getAudio().play(pathXPropertyType.AUDIO_CUE_SELECT_TILE.toString(), false);
        }
        game.switchToLevel15();
    }

    public void respondToLevel16() {
        if (!game.isSoundMuted()) {
            game.getAudio().play(pathXPropertyType.AUDIO_CUE_SELECT_TILE.toString(), false);
        }
        game.switchToLevel16();
    }

    public void respondToLevel17() {
        if (!game.isSoundMuted()) {
            game.getAudio().play(pathXPropertyType.AUDIO_CUE_SELECT_TILE.toString(), false);
        }
        game.switchToLevel17();
    }

    public void respondToLevel18() {
        if (!game.isSoundMuted()) {
            game.getAudio().play(pathXPropertyType.AUDIO_CUE_SELECT_TILE.toString(), false);
        }
        game.switchToLevel18();
    }

    public void respondToLevel19() {
        if (!game.isSoundMuted()) {
            game.getAudio().play(pathXPropertyType.AUDIO_CUE_SELECT_TILE.toString(), false);
        }
        game.switchToLevel19();
    }

    public void respondToLevel20() {
        if (!game.isSoundMuted()) {
            game.getAudio().play(pathXPropertyType.AUDIO_CUE_SELECT_TILE.toString(), false);
        }
        game.switchToLevel20();
    }

    public void respondToResetRequest() {
        if (!game.isSoundMuted()) {
            game.getAudio().play(pathXPropertyType.AUDIO_CUE_SELECT_TILE.toString(), false);
        }
    }

    public void respondToHelpRequest() {
        if (!game.isSoundMuted()) {
            game.getAudio().play(pathXPropertyType.AUDIO_CUE_SELECT_TILE.toString(), false);
        }
        game.switchToHelpScreen();
    }

    public void respondToSettingsRequest() {
        if (!game.isSoundMuted()) {
            game.getAudio().play(pathXPropertyType.AUDIO_CUE_SELECT_TILE.toString(), false);
        }

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
            if (game.getPXG().getViewport().getViewportY() > -100) {
                game.getPXG().getViewport().move(0, -20);
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
            if (game.getPXG().getViewport().getViewportY() < 300) {
                game.getPXG().getViewport().move(0, 20);
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
            if (game.getPXG().getViewport().getViewportX() > 0) {
                game.getPXG().getViewport().move(-20, 0);
            }

        }
    }

    public void respondToRightRequest() {
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

    public void respondToPauseRequest() {
        if (game.getDataModel().isPaused()) {
            game.getDataModel().unpause();
        } else {
            game.getDataModel().pause();
        }
    }

    public void respondToMuteSoundRequest() {
        game.getAudio().play(pathXPropertyType.AUDIO_CUE_SELECT_TILE.toString(), false);
        if (game.isSoundMuted()) {
            game.setSoundMuted(false);
        } else {
            game.setSoundMuted(true);
        }
        game.switchToSettingScreen();
    }

    public void respondToMuteMusicRequest() {
        game.getAudio().play(pathXPropertyType.AUDIO_CUE_SELECT_TILE.toString(), false);
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
        game.switchToSettingScreen();
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
        if (keyCode == KeyEvent.VK_F4) {
            //Dialog Box
            JOptionPane jop = new JOptionPane();
            jop.setMessage(HELP_INFO);
            jop.setMessageType(JOptionPane.INFORMATION_MESSAGE);
            JDialog dialog = jop.createDialog(game.getPXG(), "HELP");
            dialog.setVisible(true);
        }
        if (keyCode == KeyEvent.VK_F3) {
            if (game.getDataModel().isPaused()) {
                game.getDataModel().unpause();
            } // TOGGLE IT ON
            else {
                game.getDataModel().pause();
            }
        }
        if (keyCode == KeyEvent.VK_F2) {
            if (!game.isSoundMuted()) {
                game.getAudio().play(pathXPropertyType.AUDIO_CUE_CHEAT.toString(), false);
            }
            game.getData().setTotalMoney(game.getData().getTotalMoney() + 5000);
        }
        if (keyCode == KeyEvent.VK_F1) {
            if (!game.isSoundMuted()) {
                game.getAudio().play(pathXPropertyType.AUDIO_CUE_CHEAT.toString(), false);
            }
            for (int i = 0; i < game.getData().getLevelsLocked().length; i++) {
                game.getData().setLevelsLocked(false, i);
            }
            game.switchToGameScreen();
        }

        if (keyCode == KeyEvent.VK_F) { // FREEZE TIME
            if (!game.isSoundMuted()) {
                game.getAudio().play(pathXPropertyType.AUDIO_CUE_GOOD_MOVE.toString(), false);
            }
            if (game.getData().getTotalMoney() > 10) {
                if (game.getDataModel().isPaused()) {
                    game.getDataModel().unpause();
                    game.getData().setTotalMoney(game.getData().getTotalMoney() - 10);
                } else {
                    game.getDataModel().pause();
                    game.getData().setTotalMoney(game.getData().getTotalMoney() - 10);
                }
            } else {
                if (!game.isSoundMuted()) {
                    game.getAudio().play(pathXPropertyType.AUDIO_CUE_BAD_MOVE.toString(), false);
                }
            }
        }
        if (keyCode == KeyEvent.VK_G) { // MAKE LIGHTS GREEN
            if (!game.isSoundMuted()) {
                game.getAudio().play(pathXPropertyType.AUDIO_CUE_GOOD_MOVE.toString(), false);
            }
            if (game.getData().getSelectedIntersection() != null) {
                if (game.getData().getTotalMoney() > 5) {
                    game.getData().getSelectedIntersection().setOpen(true);
                    game.getData().setTotalMoney(game.getData().getTotalMoney() - 5);
                } else {
                    if (!game.isSoundMuted()) {
                        game.getAudio().play(pathXPropertyType.AUDIO_CUE_BAD_MOVE.toString(), false);
                    }
                }
            }
        }
        if (keyCode == KeyEvent.VK_R) { // MAKE LIGHTS RED
            if (!game.isSoundMuted()) {
                game.getAudio().play(pathXPropertyType.AUDIO_CUE_GOOD_MOVE.toString(), false);
            }
            if (game.getData().getSelectedIntersection() != null) {
                if (game.getData().getTotalMoney() > 5) {
                    game.getData().getSelectedIntersection().setOpen(false);
                    game.getData().setTotalMoney(game.getData().getTotalMoney() - 5);
                } else {
                    if (!game.isSoundMuted()) {
                        game.getAudio().play(pathXPropertyType.AUDIO_CUE_BAD_MOVE.toString(), false);
                    }

                }
            }
        }
        if (keyCode == KeyEvent.VK_X) { // INCREASE SPEED LIMIT
            if (!game.isSoundMuted()) {
                game.getAudio().play(pathXPropertyType.AUDIO_CUE_GOOD_MOVE.toString(), false);
            }
            if (game.getData().getSelectedRoad() != null) {
                if (game.getData().getTotalMoney() > 15) {
                    game.getData().getSelectedRoad().setSpeedLimit(game.getData().getSelectedRoad().getSpeedLimit() * 2);
                    game.getData().setTotalMoney(game.getData().getTotalMoney() - 15);
                } else {
                    if (!game.isSoundMuted()) {
                        game.getAudio().play(pathXPropertyType.AUDIO_CUE_BAD_MOVE.toString(), false);
                    }

                }
            }
        }
        if (keyCode == KeyEvent.VK_Z) { // DECREASE SPEED LIMIT
            if (!game.isSoundMuted()) {
                game.getAudio().play(pathXPropertyType.AUDIO_CUE_GOOD_MOVE.toString(), false);
            }
            if (game.getData().getSelectedRoad() != null) {
                if (game.getData().getTotalMoney() > 15) {
                    game.getData().getSelectedRoad().setSpeedLimit(game.getData().getSelectedRoad().getSpeedLimit() / 2);
                    game.getData().setTotalMoney(game.getData().getTotalMoney() - 15);
                } else {
                    if (!game.isSoundMuted()) {
                        game.getAudio().play(pathXPropertyType.AUDIO_CUE_BAD_MOVE.toString(), false);
                    }

                }
            }
        }
        if (keyCode == KeyEvent.VK_P) { // INCREASE PLAYER SPEED
            if (!game.isSoundMuted()) {
                game.getAudio().play(pathXPropertyType.AUDIO_CUE_GOOD_MOVE.toString(), false);
            }

            if (game.getData().getTotalMoney() > 20) {
                game.getData().setSpeed(game.getData().getSpeed() + game.getData().getSpeed() * .2);
                game.getData().setTotalMoney(game.getData().getTotalMoney() - 20);
            } else {
                if (!game.isSoundMuted()) {
                    game.getAudio().play(pathXPropertyType.AUDIO_CUE_BAD_MOVE.toString(), false);
                }
            }

        }
        if (keyCode == KeyEvent.VK_T) { // FLAT TIRE
        }
        if (keyCode == KeyEvent.VK_E) { // EMPTY GAS TANK
        }
        if (keyCode == KeyEvent.VK_H) { // CLOSE ROAD
            if (!game.isSoundMuted()) {
                game.getAudio().play(pathXPropertyType.AUDIO_CUE_GOOD_MOVE.toString(), false);
            }
            if (game.getData().getSelectedRoad() != null) {
                if (game.getData().getTotalMoney() > 25) {
                    game.getData().getSelectedRoad().setIsClosed(true);
                    game.getData().setTotalMoney(game.getData().getTotalMoney() - 25);
                } else {
                    if (!game.isSoundMuted()) {
                        game.getAudio().play(pathXPropertyType.AUDIO_CUE_BAD_MOVE.toString(), false);
                    }

                }
            }
        }
        if (keyCode == KeyEvent.VK_O) { // OPEN INTERSECTION
            if (!game.isSoundMuted()) {
                game.getAudio().play(pathXPropertyType.AUDIO_CUE_GOOD_MOVE.toString(), false);
            }
            if (game.getData().getSelectedIntersection() != null) {
                if (game.getData().getTotalMoney() > 25) {
                    game.getData().getSelectedIntersection().setOpen(true);

                    Iterator<Road> it = game.getData().roadsIterator();
                    while (it.hasNext()) {
                        Road road = it.next();
                        if (game.getData().getSelectedIntersection().equals(road.getNode1())) {
                            road.setIsClosed(false);
                        } else if (game.getData().getSelectedIntersection().equals(road.getNode2())) {
                            road.setIsClosed(false);
                        }
                    }

                    game.getData().setTotalMoney(game.getData().getTotalMoney() - 25);
                } else {
                    if (!game.isSoundMuted()) {
                        game.getAudio().play(pathXPropertyType.AUDIO_CUE_BAD_MOVE.toString(), false);
                    }
                }
            }
        }
        if (keyCode == KeyEvent.VK_C) { // CLOSE INTERSECTION
            if (!game.isSoundMuted()) {
                game.getAudio().play(pathXPropertyType.AUDIO_CUE_GOOD_MOVE.toString(), false);
            }
            if (game.getData().getSelectedIntersection() != null) {
                if (game.getData().getTotalMoney() > 25) {
                    game.getData().getSelectedIntersection().setOpen(false);

                    Iterator<Road> it = game.getData().roadsIterator();
                    while (it.hasNext()) {
                        Road road = it.next();
                        if (game.getData().getSelectedIntersection().equals(road.getNode1())) {
                            road.setIsClosed(true);
                        } else if (game.getData().getSelectedIntersection().equals(road.getNode2())) {
                            road.setIsClosed(true);
                        }
                    }

                    game.getData().setTotalMoney(game.getData().getTotalMoney() - 25);
                } else {
                    if (!game.isSoundMuted()) {
                        game.getAudio().play(pathXPropertyType.AUDIO_CUE_BAD_MOVE.toString(), false);
                    }
                }
            }
        }
        if (keyCode == KeyEvent.VK_Q) { // STEAL
        }
        if (keyCode == KeyEvent.VK_M) { // MIND CONTROL
        }
        if (keyCode == KeyEvent.VK_Y) { // FLYING
        }
        if (keyCode == KeyEvent.VK_V) { // INVINCIBILITY
            if (!game.isSoundMuted()) {
                game.getAudio().play(pathXPropertyType.AUDIO_CUE_GOOD_MOVE.toString(), false);
            }
            if (game.getData().getTotalMoney() > 40) {
                game.getGUIEnemies().get(PLAYER_TYPE).setInvincibile(true);
                game.getData().setTotalMoney(game.getData().getTotalMoney() - 40);
            } else {
                if (!game.isSoundMuted()) {
                    game.getAudio().play(pathXPropertyType.AUDIO_CUE_BAD_MOVE.toString(), false);
                }
            }
        }
        if (keyCode == KeyEvent.VK_L) { // MINDLESS TERROR
        }
        if (keyCode == KeyEvent.VK_B) { // INTANGABILITY
            if (!game.isSoundMuted()) {
                game.getAudio().play(pathXPropertyType.AUDIO_CUE_GOOD_MOVE.toString(), false);
            }
            if (game.getData().getTotalMoney() > 30) {
                game.getGUIEnemies().get(PLAYER_TYPE).setIntangeable(true);
                game.getData().setTotalMoney(game.getData().getTotalMoney() - 30);
            } else {
                if (!game.isSoundMuted()) {
                    game.getAudio().play(pathXPropertyType.AUDIO_CUE_BAD_MOVE.toString(), false);
                }
            }
        }
    }

}
