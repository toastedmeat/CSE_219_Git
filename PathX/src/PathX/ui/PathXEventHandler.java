package PathX.ui;

import java.awt.event.KeyEvent;
import static PathX.PathXConstants.*;
import static PathX.PathXConstants.MENU_SCREEN_STATE;
import PathX.PathX;
import PathX.data.PathXDataModel;
import PathX.file.PathXFileManager;
import java.util.ArrayList;
import javax.swing.JPanel;
import properties_manager.PropertiesManager;

/**
 *
 * @author Richard McKenna & Eric Loo
 */
public class PathXEventHandler{
    

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
        game.switchToSplashScreen();
        game.getInsideCanvas().setVisible(false);
    }

    public void respondToBackToLevelSelectRequest() {
        game.switchToGameScreen();
    }

    /**
     * Called when the user clicks a button to select a level.
     */
    public void respondToSelectLevelRequest() {
        // WE ONLY LET THIS HAPPEN IF THE MENU SCREEN IS VISIBLE
        if (game.isCurrentScreenState(MENU_SCREEN_STATE)) {
            // GET THE GAME'S DATA MODEL, WHICH IS ALREADY LOCKED FOR US
            

            // UPDATE THE DATA
            PathXFileManager fileManager = game.getFileManager();
            //fileManager.loadLevel(levelFile);
            //data.reset(game);

            // GO TO THE GAME
            game.switchToGameScreen();
        }
    }

    public void respondToLevel1() {
        game.switchToLevel1();
    }

    public void respondToLevel2() {
        game.switchToLevel2();
    }

    public void respondToLevel3() {
        game.switchToLevel3();
    }

    public void respondToLevel4() {
        game.switchToLevel4();
    }

    public void respondToLevel5() {
        game.switchToLevel5();
    }

    public void respondToLevel6() {
        game.switchToLevel6();
    }

    public void respondToLevel7() {
        game.switchToLevel7();
    }

    public void respondToLevel8() {
        game.switchToLevel8();
    }

    public void respondToLevel9() {
        game.switchToLevel9();
    }

    public void respondToLevel10() {
        game.switchToLevel10();
    }

    public void respondToLevel11() {
        game.switchToLevel11();
    }

    public void respondToLevel12() {
        game.switchToLevel12();
    }

    public void respondToLevel13() {
        game.switchToLevel13();
    }

    public void respondToLevel14() {
        game.switchToLevel14();
    }

    public void respondToLevel15() {
        game.switchToLevel15();
    }

    public void respondToLevel16() {
        game.switchToLevel16();
    }

    public void respondToLevel17() {
        game.switchToLevel17();
    }

    public void respondToLevel18() {
        game.switchToLevel18();
    }

    public void respondToLevel19() {
        game.switchToLevel19();
    }

    public void respondToLevel20() {
        game.switchToLevel20();
    }

    public void respondToResetRequest() {
    }

    public void respondToHelpRequest() {
        game.switchToHelpScreen();
    }

    public void respondToSettingsRequest() {
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
        } else if (game.getPXG().getRenderedBackground().equals(SBU_GAME_TYPE)&& game.getGUIDecor().get(SBU_GAME_TYPE).getY() < 120) {
            game.getGUIDecor().get(SBU_GAME_TYPE).setY(game.getGUIDecor().get(SBU_GAME_TYPE).getY() + 20);
            game.getPXG().viewport.move(0, -20);
        } else {
            for (int i = 2; i <= 20; i++) {
                if (game.getPXG().getRenderedBackground().equals("LEVEL" + i + "_GAME_TYPE")&& game.getGUIDecor().get("LEVEL" + i + "_GAME_TYPE").getY() < 120) {
                    game.getGUIDecor().get("LEVEL" + i + "_GAME_TYPE").setY(game.getGUIDecor().get("LEVEL" + i + "_GAME_TYPE").getY() + 20);
                    game.getPXG().viewport.move(0, -20);
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
        }else if (game.getPXG().getRenderedBackground().equals(SBU_GAME_TYPE)&& game.getGUIDecor().get(SBU_GAME_TYPE).getY() > -300) {
            game.getGUIDecor().get(SBU_GAME_TYPE).setY(game.getGUIDecor().get(SBU_GAME_TYPE).getY() - 20);
            game.getPXG().viewport.move(0, 20);
        } else {
            for (int i = 2; i <= 20; i++) {
                if (game.getPXG().getRenderedBackground().equals("LEVEL" + i + "_GAME_TYPE")&& game.getGUIDecor().get("LEVEL" + i + "_GAME_TYPE").getY() > -300) {
                    game.getGUIDecor().get("LEVEL" + i + "_GAME_TYPE").setY(game.getGUIDecor().get("LEVEL" + i + "_GAME_TYPE").getY() - 20);
                    game.getPXG().viewport.move(0, 20);
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
        }else if (game.getPXG().getRenderedBackground().equals(SBU_GAME_TYPE)&& game.getGUIDecor().get(SBU_GAME_TYPE).getX() < 0) {
            game.getGUIDecor().get(SBU_GAME_TYPE).setX(game.getGUIDecor().get(SBU_GAME_TYPE).getX() + 20);
            game.getPXG().viewport.move(-20, 0);
        } else {
            for (int i = 2; i <= 20; i++) {
                if (game.getPXG().getRenderedBackground().equals("LEVEL" + i + "_GAME_TYPE")&& game.getGUIDecor().get("LEVEL" + i + "_GAME_TYPE").getX() < 0) {
                    game.getGUIDecor().get("LEVEL" + i + "_GAME_TYPE").setX(game.getGUIDecor().get("LEVEL" + i + "_GAME_TYPE").getX() + 20);
                    game.getPXG().viewport.move(-20, 0);
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
        }else if (game.getPXG().getRenderedBackground().equals(SBU_GAME_TYPE)&& game.getGUIDecor().get(SBU_GAME_TYPE).getX() > -600) {
            game.getGUIDecor().get(SBU_GAME_TYPE).setX(game.getGUIDecor().get(SBU_GAME_TYPE).getX() - 20);
            game.getPXG().viewport.move(20, 0);
        } else {
            for (int i = 2; i <= 20; i++) {
                if (game.getPXG().getRenderedBackground().equals("LEVEL" + i + "_GAME_TYPE")&& game.getGUIDecor().get("LEVEL" + i + "_GAME_TYPE").getX() > -600) {
                    game.getGUIDecor().get("LEVEL" + i + "_GAME_TYPE").setX(game.getGUIDecor().get("LEVEL" + i + "_GAME_TYPE").getX() - 20);
                    game.getPXG().viewport.move(20, 0);
                }
            }
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
            }else if (game.getPXG().getRenderedBackground().equals(SBU_GAME_TYPE) && game.getGUIDecor().get(SBU_GAME_TYPE).getY() < 120) {
            game.getGUIDecor().get(SBU_GAME_TYPE).setY(game.getGUIDecor().get(SBU_GAME_TYPE).getY() + 20);
            game.getPXG().viewport.move(0, -20);
            
        } else {
            for (int i = 2; i <= 20; i++) {
                if (game.getPXG().getRenderedBackground().equals("LEVEL" + i + "_GAME_TYPE")&& game.getGUIDecor().get("LEVEL" + i + "_GAME_TYPE").getY() < 120) {
                    game.getGUIDecor().get("LEVEL" + i + "_GAME_TYPE").setY(game.getGUIDecor().get("LEVEL" + i + "_GAME_TYPE").getY() + 20);
                    game.getPXG().viewport.move(0, -20);
                }
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
            }else if (game.getPXG().getRenderedBackground().equals(SBU_GAME_TYPE) && game.getGUIDecor().get(SBU_GAME_TYPE).getY() > -300) {
            game.getGUIDecor().get(SBU_GAME_TYPE).setY(game.getGUIDecor().get(SBU_GAME_TYPE).getY() - 20);
            game.getPXG().viewport.move(0, 20);
        } else {
            for (int i = 2; i <= 20; i++) {
                if (game.getPXG().getRenderedBackground().equals("LEVEL" + i + "_GAME_TYPE")&& game.getGUIDecor().get("LEVEL" + i + "_GAME_TYPE").getY() > -300) {
                    game.getGUIDecor().get("LEVEL" + i + "_GAME_TYPE").setY(game.getGUIDecor().get("LEVEL" + i + "_GAME_TYPE").getY() - 20);
                    game.getPXG().viewport.move(0, 20);
                }
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
            }else if (game.getPXG().getRenderedBackground().equals(SBU_GAME_TYPE) && game.getGUIDecor().get(SBU_GAME_TYPE).getX() < 0) {
            game.getGUIDecor().get(SBU_GAME_TYPE).setX(game.getGUIDecor().get(SBU_GAME_TYPE).getX() + 20);
            game.getPXG().viewport.move(-20, 0);
        } else {
            for (int i = 2; i <= 20; i++) {
                if (game.getPXG().getRenderedBackground().equals("LEVEL" + i + "_GAME_TYPE")&& game.getGUIDecor().get("LEVEL" + i + "_GAME_TYPE").getX() < 0) {
                    game.getGUIDecor().get("LEVEL" + i + "_GAME_TYPE").setX(game.getGUIDecor().get("LEVEL" + i + "_GAME_TYPE").getX() + 20);
                    game.getPXG().viewport.move(-20, 0);
                }
            }
        }
        }
        if (keyCode == KeyEvent.VK_RIGHT) {
            if (game.getGUIDecor().get(BACKGROUND_GAME_TYPE).getX() - 20 > -160 && game.getInsideCanvas().getRenderedBackground().equals(BACKGROUND_GAME_TYPE)) {
                game.getGUIDecor().get(BACKGROUND_GAME_TYPE).setX(game.getGUIDecor().get(BACKGROUND_GAME_TYPE).getX() - 20);
                ArrayList<String> gameLevels = props.getPropertyOptionsList(PathX.pathXPropertyType.GAME_LEVELS);
                for (String level : gameLevels) {
                    game.getGUIButtons().get(level).setX(game.getGUIButtons().get(level).getX() - 20);
                }
            }else if (game.getPXG().getRenderedBackground().equals(SBU_GAME_TYPE) && game.getGUIDecor().get(SBU_GAME_TYPE).getX() > -600) {
            game.getGUIDecor().get(SBU_GAME_TYPE).setX(game.getGUIDecor().get(SBU_GAME_TYPE).getX() - 20);
            game.getPXG().viewport.move(20, 0);
        } else {
            for (int i = 2; i <= 20; i++) {
                if (game.getPXG().getRenderedBackground().equals("LEVEL" + i + "_GAME_TYPE")&& game.getGUIDecor().get("LEVEL" + i + "_GAME_TYPE").getX() > -600) {
                    game.getGUIDecor().get("LEVEL" + i + "_GAME_TYPE").setX(game.getGUIDecor().get("LEVEL" + i + "_GAME_TYPE").getX() - 20);
                    game.getPXG().viewport.move(20, 0);
                }
            }
        }
        }
    }

    
}
