package PathX.ui;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import PathX.data.PathXDataModel;
import mini_game.MiniGame;
import mini_game.MiniGameState;
import static PathX.PathXConstants.*;
import mini_game.Sprite;
import mini_game.SpriteType;
import mini_game.Viewport;
import properties_manager.PropertiesManager;
import PathX.PathX.pathXPropertyType;
import PathX.data.Intersection;
import PathX.data.PathXEditMode;
import PathX.file.PathXFileManager;
import PathX.data.PathXRecord;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.File;
import java.util.Collection;
import java.util.Random;
import java.util.TreeMap;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import mini_game.MiniGameEventRelayer;

/**
 * This is the actual mini game, as extended from the mini game framework. It
 * manages all the UI elements.
 *
 * @author Richard McKenna & Eric Loo
 */
public class PathXMiniGame extends MiniGame {
    // THE PLAYER RECORD FOR EACH LEVEL, WHICH LIVES BEYOND ONE SESSION

    private PathXRecord record;

    // HANDLES GAME UI EVENTS
    private PathXEventHandler eventHandler;

    // HANDLES ERROR CONDITIONS
    private PathXErrorHandler errorHandler;

    // MANAGES LOADING OF LEVELS AND THE PLAYER RECORDS FILES
    private PathXFileManager fileManager;

    // THE SCREEN CURRENTLY BEING PLAYED
    private String currentScreenState;

    private static PathXDataModel dataCopy;

    private PathXGamePanel insideCanvas;

    private PathXGamePanel pxg;

    protected TreeMap<String, carSprite> guiEnemies;

    private boolean isOnLevelSelect;
    private boolean isOnGameLevel;

    private boolean muted;
    private boolean soundMuted;

    public boolean isMuted() {
        return muted;
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
    }

    public PathXRecord getPlayerRecord() {
        return record;
    }

    public TreeMap<String, carSprite> getGUIEnemies() {
        return guiEnemies;
    }

    public PathXDataModel getData() {
        return dataCopy;
    }

    public PathXGamePanel getInsideCanvas() {
        return insideCanvas;
    }

    public PathXGamePanel getPXG() {
        return pxg;
    }

    public PathXErrorHandler getErrorHandler() {
        return errorHandler;
    }

    public PathXFileManager getFileManager() {
        return fileManager;
    }

    public boolean isCurrentScreenState(String testScreenState) {
        return testScreenState.equals(currentScreenState);
    }

    public boolean isIsOnLevelSelect() {
        return isOnLevelSelect;
    }

    public boolean isIsOnGameLevel() {
        return isOnGameLevel;
    }

    public boolean isSoundMuted() {
        return soundMuted;
    }

    public void setSoundMuted(boolean soundMuted) {
        this.soundMuted = soundMuted;
    }

    public void setIsOnGameLevel(boolean isOnGameLevel) {
        this.isOnGameLevel = isOnGameLevel;
    }

    public void setIsOnLevelSelect(boolean isOnLevelSelect) {
        this.isOnLevelSelect = isOnLevelSelect;
    }

    /**
     * This method displays makes the stats dialog display visible, which
     * includes the text inside.
     */
    public void displayStats() {
        // MAKE SURE ONLY THE PROPER DIALOG IS VISIBLE
        guiDialogs.get(WIN_DIALOG_TYPE).setState(PathXTileState.INVISIBLE_STATE.toString());
        guiDialogs.get(STATS_DIALOG_TYPE).setState(PathXTileState.VISIBLE_STATE.toString());
    }

    /**
     * This method forces the file manager to save the current player record.
     */
    public void savePlayerRecord() {
        fileManager.saveRecord(record);
    }

    /**
     * This method switches the application to the menu screen, making all the
     * appropriate UI controls visible & invisible.
     */
    public void switchToSplashScreen() {
        // CHANGE THE BACKGROUND
        dataCopy.setLoadedLevel(false);
        guiDecor.get(BACKGROUND_TYPE).setState(MENU_SCREEN_STATE);
        guiEnemies.clear();
        dataCopy.getViewport().reset();

        // DEACTIVATE THE TOOLBAR CONTROLS
        guiButtons.get(NEW_GAME_BUTTON_TYPE).setState(PathXTileState.INVISIBLE_STATE.toString());
        guiButtons.get(NEW_GAME_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(BACK_BUTTON_TYPE).setState(PathXTileState.INVISIBLE_STATE.toString());
        guiButtons.get(BACK_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(BACK_TO_LEVEL_SELECT_TYPE).setState(PathXTileState.INVISIBLE_STATE.toString());
        guiButtons.get(BACK_TO_LEVEL_SELECT_TYPE).setEnabled(false);

        guiButtons.get(PAUSE_BUTTON_TYPE).setState(PathXTileState.INVISIBLE_STATE.toString());
        guiButtons.get(PAUSE_BUTTON_TYPE).setEnabled(false);

        guiButtons.get(UP_BUTTON_TYPE).setState(PathXTileState.INVISIBLE_STATE.toString());
        guiButtons.get(UP_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(DOWN_BUTTON_TYPE).setState(PathXTileState.INVISIBLE_STATE.toString());
        guiButtons.get(DOWN_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEFT_BUTTON_TYPE).setState(PathXTileState.INVISIBLE_STATE.toString());
        guiButtons.get(LEFT_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(RIGHT_BUTTON_TYPE).setState(PathXTileState.INVISIBLE_STATE.toString());
        guiButtons.get(RIGHT_BUTTON_TYPE).setEnabled(false);

        guiButtons.get(MUSIC_CHECK_BOX_TYPE).setState(PathXTileState.INVISIBLE_STATE.toString());
        guiButtons.get(MUSIC_CHECK_BOX_TYPE).setEnabled(false);

        guiButtons.get(SOUND_CHECK_BOX_TYPE).setState(PathXTileState.INVISIBLE_STATE.toString());
        guiButtons.get(SOUND_CHECK_BOX_TYPE).setEnabled(false);

        // ACTIVATE THE LEVEL SELECT BUTTONS
        // DEACTIVATE THE LEVEL SELECT BUTTONS
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        ArrayList<String> levels = props.getPropertyOptionsList(pathXPropertyType.LEVEL_OPTIONS);
        for (String level : levels) {
            guiButtons.get(level).setState(PathXTileState.VISIBLE_STATE.toString());
            guiButtons.get(level).setEnabled(true);
        }

        ArrayList<String> gameLevels = props.getPropertyOptionsList(pathXPropertyType.GAME_LEVELS);
        for (String level : gameLevels) {
            guiButtons.get(level).setState(PathXTileState.INVISIBLE_STATE.toString());
            guiButtons.get(level).setEnabled(false);
        }

        // DEACTIVATE ALL DIALOGS
        guiDialogs.get(WIN_DIALOG_TYPE).setState(PathXTileState.INVISIBLE_STATE.toString());
        guiDialogs.get(STATS_DIALOG_TYPE).setState(PathXTileState.INVISIBLE_STATE.toString());

        // HIDE THE TILES
        //((PathXDataModel) data).enableTiles(false);
        // MAKE THE CURRENT SCREEN THE MENU SCREEN
        currentScreenState = MENU_SCREEN_STATE;

        // AND UPDATE THE DATA GAME STATE
        data.setGameState(MiniGameState.NOT_STARTED);

        insideCanvas.setEnabled(false);
        insideCanvas.setVisible(false);
        pxg.setEnabled(false);
        pxg.setVisible(false);
        // PLAY THE WELCOME SCREEN SONG
        //audio.stop(pathXPropertyType.AUDIO_CUE_WIN.toString());
        if (!isMuted()) {
            audio.stop(pathXPropertyType.SONG_CUE_GAME_SCREEN.toString());
            audio.play(pathXPropertyType.SONG_CUE_MENU_SCREEN.toString(), true);
        }

        isOnGameLevel = false;
        isOnLevelSelect = false;
    }

    public void switchToSettingScreen() {
        guiDecor.get(BACKGROUND_TYPE).setState(SETTINGS_SCREEN_STATE);
        menuSetup();

        if (isMuted()) {
            guiButtons.get(MUSIC_CHECK_BOX_TYPE).setState(PathXTileState.VISIBLE_STATE.toString());
            guiButtons.get(MUSIC_CHECK_BOX_TYPE).setEnabled(true);
        } else {
            guiButtons.get(MUSIC_CHECK_BOX_TYPE).setState(PathXTileState.CHECKED_STATE.toString());
            guiButtons.get(MUSIC_CHECK_BOX_TYPE).setEnabled(true);
        }
        if (isSoundMuted()) {
            guiButtons.get(SOUND_CHECK_BOX_TYPE).setState(PathXTileState.VISIBLE_STATE.toString());
            guiButtons.get(SOUND_CHECK_BOX_TYPE).setEnabled(true);
        } else {
            guiButtons.get(SOUND_CHECK_BOX_TYPE).setState(PathXTileState.CHECKED_STATE.toString());
            guiButtons.get(SOUND_CHECK_BOX_TYPE).setEnabled(true);
        }

        currentScreenState = SETTINGS_SCREEN_STATE;
    }

    public void switchToHelpScreen() {
        guiDecor.get(BACKGROUND_TYPE).setState(HELP_SCREEN_STATE);
        menuSetup();
        currentScreenState = HELP_SCREEN_STATE;
    }

    /**
     * This method switches the application to the game screen, making all the
     * appropriate UI controls visible & invisible.
     */
    public void switchToGameScreen() {
        pxg.setEnabled(false);
        pxg.setVisible(false);
        insideCanvas.setEnabled(true);

        guiEnemies.clear();

        dataCopy.setLoadedLevel(false);
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        // CHANGE THE BACKGROUND
        guiDecor.get(BACKGROUND_TYPE).setState(GAME_SCREEN_STATE);

        // ACTIVATE THE TOOLBAR AND ITS CONTROLS
        guiButtons.get(NEW_GAME_BUTTON_TYPE).setState(PathXTileState.VISIBLE_STATE.toString());
        guiButtons.get(NEW_GAME_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(NEW_GAME_BUTTON_TYPE).setX(NEW_BUTTON_X);
        guiButtons.get(NEW_GAME_BUTTON_TYPE).setY(NEW_BUTTON_Y);
        guiButtons.get(BACK_BUTTON_TYPE).setState(PathXTileState.VISIBLE_STATE.toString());
        guiButtons.get(BACK_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(BACK_BUTTON_TYPE).setX(BACK_BUTTON_X);
        guiButtons.get(BACK_BUTTON_TYPE).setY(BACK_BUTTON_Y);
        guiButtons.get(BACK_TO_LEVEL_SELECT_TYPE).setState(PathXTileState.INVISIBLE_STATE.toString());
        guiButtons.get(BACK_TO_LEVEL_SELECT_TYPE).setEnabled(false);
        guiButtons.get(PAUSE_BUTTON_TYPE).setState(PathXTileState.INVISIBLE_STATE.toString());
        guiButtons.get(PAUSE_BUTTON_TYPE).setEnabled(false);

        guiButtons.get(UP_BUTTON_TYPE).setState(PathXTileState.VISIBLE_STATE.toString());
        guiButtons.get(UP_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(UP_BUTTON_TYPE).setX(UP_BUTTON_X);
        guiButtons.get(UP_BUTTON_TYPE).setY(UP_BUTTON_Y);
        guiButtons.get(DOWN_BUTTON_TYPE).setState(PathXTileState.VISIBLE_STATE.toString());
        guiButtons.get(DOWN_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(DOWN_BUTTON_TYPE).setX(DOWN_BUTTON_X);
        guiButtons.get(DOWN_BUTTON_TYPE).setY(DOWN_BUTTON_Y);
        guiButtons.get(LEFT_BUTTON_TYPE).setState(PathXTileState.VISIBLE_STATE.toString());
        guiButtons.get(LEFT_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEFT_BUTTON_TYPE).setX(LEFT_BUTTON_X);
        guiButtons.get(LEFT_BUTTON_TYPE).setY(LEFT_BUTTON_Y);
        guiButtons.get(RIGHT_BUTTON_TYPE).setState(PathXTileState.VISIBLE_STATE.toString());
        guiButtons.get(RIGHT_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(RIGHT_BUTTON_TYPE).setX(RIGHT_BUTTON_X);
        guiButtons.get(RIGHT_BUTTON_TYPE).setY(RIGHT_BUTTON_Y);

        // DEACTIVATE THE LEVEL SELECT BUTTONS
        ArrayList<String> levels = props.getPropertyOptionsList(pathXPropertyType.LEVEL_OPTIONS);
        for (String level : levels) {
            guiButtons.get(level).setState(PathXTileState.INVISIBLE_STATE.toString());
            guiButtons.get(level).setEnabled(false);
        }

        for (int i = 0; i < dataCopy.getLevelsLocked().length; i++) {
            if (dataCopy.getLevelsLocked()[i]) {
                guiButtons.get(dataCopy.getLevelsNames()[i]).setState(PathXTileState.LOCKED_STATE.toString());
                guiButtons.get(dataCopy.getLevelsNames()[i]).setEnabled(false);
            } else if (dataCopy.getLevelsRobbed()[i]) {
                guiButtons.get(dataCopy.getLevelsNames()[i]).setState(PathXTileState.ROBBED_STATE.toString());
                guiButtons.get(dataCopy.getLevelsNames()[i]).setEnabled(true);
            } else {
                guiButtons.get(dataCopy.getLevelsNames()[i]).setState(PathXTileState.VISIBLE_STATE.toString());
                guiButtons.get(dataCopy.getLevelsNames()[i]).setEnabled(true);
            }
        }

        // AND CHANGE THE SCREEN STATE
        currentScreenState = GAME_SCREEN_STATE;

        insideCanvas.setVisible(true);
        canvas.setLayout(null);
        insideCanvas.setBounds(17, 129, 1247, 551);
        insideCanvas.setRenderedBackground(BACKGROUND_GAME_TYPE);

        isOnLevelSelect = true;

        dataCopy.getViewport().reset();

        if (!audio.isPlaying(pathXPropertyType.SONG_CUE_MENU_SCREEN.toString()) && !isMuted()) {
            audio.play(pathXPropertyType.SONG_CUE_MENU_SCREEN.toString(), true);
            audio.stop(pathXPropertyType.SONG_CUE_GAME_SCREEN.toString());
            audio.stop(pathXPropertyType.AUDIO_CUE_WIN.toString());
        }

        isOnGameLevel = false;
    }

    public void switchToLevel1() {
        //Changing the background
        insideCanvas.setRenderedBackground(LEVEL1_GAME_TYPE);
        pxg.setRenderedBackground(LEVEL1_GAME_TYPE);

        //Level Loading
        fileManager.loadLevel(new File("data/pathX/Level1.xml"), dataCopy);
        dataCopy.setLoadedLevel(true);

        dataCopy.setCurrentLevel("1");
        levelSetup();

        //Dialog Box
        JOptionPane jop = new JOptionPane();
        jop.setMessage(LEVEL1_INFO);
        jop.setMessageType(JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = jop.createDialog(pxg, "Information about the Level");
        dialog.setVisible(true);
    }

    public void switchToLevel2() {
        pxg.setRenderedBackground(LEVEL2_GAME_TYPE);

        insideCanvas.setRenderedBackground(LEVEL2_GAME_TYPE);
        fileManager.loadLevel(new File("data/pathX/Level2.xml"), dataCopy);
        dataCopy.setLoadedLevel(true);
        dataCopy.setCurrentLevel("2");
        createEnemies();
        levelSetup();

        JOptionPane jop = new JOptionPane();
        jop.setMessage(LEVEL2_INFO);
        jop.setMessageType(JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = jop.createDialog(pxg, "Information about the Level");
        dialog.setVisible(true);

    }

    public void switchToLevel3() {
        insideCanvas.setRenderedBackground(LEVEL2_GAME_TYPE);
        fileManager.loadLevel(new File("data/pathX/Level3.xml"), dataCopy);
        dataCopy.setLoadedLevel(true);
        pxg.setRenderedBackground(LEVEL3_GAME_TYPE);
        dataCopy.setCurrentLevel("3");
        createEnemies();
        levelSetup();

        JOptionPane jop = new JOptionPane();
        jop.setMessage(LEVEL3_INFO);
        jop.setMessageType(JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = jop.createDialog(pxg, "Information about the Level");
        dialog.setVisible(true);

    }

    public void switchToLevel4() {
        insideCanvas.setRenderedBackground(LEVEL2_GAME_TYPE);
        fileManager.loadLevel(new File("data/pathX/Level4.xml"), dataCopy);
        dataCopy.setLoadedLevel(true);
        pxg.setRenderedBackground(LEVEL4_GAME_TYPE);
        dataCopy.setCurrentLevel("4");
        createEnemies();
        levelSetup();

        JOptionPane jop = new JOptionPane();
        jop.setMessage(LEVEL4_INFO);
        jop.setMessageType(JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = jop.createDialog(pxg, "Information about the Level");
        dialog.setVisible(true);

    }

    public void switchToLevel5() {
        insideCanvas.setRenderedBackground(LEVEL2_GAME_TYPE);
        fileManager.loadLevel(new File("data/pathX/Level5.xml"), dataCopy);
        dataCopy.setLoadedLevel(true);
        pxg.setRenderedBackground(LEVEL5_GAME_TYPE);
        dataCopy.setCurrentLevel("5");
        createEnemies();
        levelSetup();

        JOptionPane jop = new JOptionPane();
        jop.setMessage(LEVEL5_INFO);
        jop.setMessageType(JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = jop.createDialog(pxg, "Information about the Level");
        dialog.setVisible(true);

    }

    public void switchToLevel6() {
        insideCanvas.setRenderedBackground(LEVEL2_GAME_TYPE);
        fileManager.loadLevel(new File("data/pathX/Level6.xml"), dataCopy);
        dataCopy.setLoadedLevel(true);
        pxg.setRenderedBackground(LEVEL6_GAME_TYPE);
        dataCopy.setCurrentLevel("6");
        createEnemies();
        levelSetup();

        JOptionPane jop = new JOptionPane();
        jop.setMessage(LEVEL6_INFO);
        jop.setMessageType(JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = jop.createDialog(pxg, "Information about the Level");
        dialog.setVisible(true);

    }

    public void switchToLevel7() {
        insideCanvas.setRenderedBackground(LEVEL2_GAME_TYPE);
        fileManager.loadLevel(new File("data/pathX/Level7.xml"), dataCopy);
        dataCopy.setLoadedLevel(true);
        pxg.setRenderedBackground(LEVEL7_GAME_TYPE);
        dataCopy.setCurrentLevel("7");
        createEnemies();
        levelSetup();

        JOptionPane jop = new JOptionPane();
        jop.setMessage(LEVEL7_INFO);
        jop.setMessageType(JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = jop.createDialog(pxg, "Information about the Level");
        dialog.setVisible(true);

    }

    public void switchToLevel8() {
        insideCanvas.setRenderedBackground(LEVEL2_GAME_TYPE);
        fileManager.loadLevel(new File("data/pathX/Level8.xml"), dataCopy);
        dataCopy.setLoadedLevel(true);
        pxg.setRenderedBackground(LEVEL8_GAME_TYPE);
        dataCopy.setCurrentLevel("8");
        createEnemies();
        levelSetup();

        JOptionPane jop = new JOptionPane();
        jop.setMessage(LEVEL8_INFO);
        jop.setMessageType(JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = jop.createDialog(pxg, "Information about the Level");
        dialog.setVisible(true);

    }

    public void switchToLevel9() {
        insideCanvas.setRenderedBackground(LEVEL2_GAME_TYPE);
        fileManager.loadLevel(new File("data/pathX/Level9.xml"), dataCopy);
        dataCopy.setLoadedLevel(true);
        pxg.setRenderedBackground(LEVEL9_GAME_TYPE);
        dataCopy.setCurrentLevel("9");
        createEnemies();
        levelSetup();

        JOptionPane jop = new JOptionPane();
        jop.setMessage(LEVEL9_INFO);
        jop.setMessageType(JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = jop.createDialog(pxg, "Information about the Level");
        dialog.setVisible(true);

    }

    public void switchToLevel10() {
        insideCanvas.setRenderedBackground(LEVEL2_GAME_TYPE);
        fileManager.loadLevel(new File("data/pathX/Level10.xml"), dataCopy);
        dataCopy.setLoadedLevel(true);
        pxg.setRenderedBackground(LEVEL10_GAME_TYPE);
        dataCopy.setCurrentLevel("10");
        createEnemies();
        levelSetup();

        JOptionPane jop = new JOptionPane();
        jop.setMessage(LEVEL10_INFO);
        jop.setMessageType(JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = jop.createDialog(pxg, "Information about the Level");
        dialog.setVisible(true);

    }

    public void switchToLevel11() {
        insideCanvas.setRenderedBackground(LEVEL2_GAME_TYPE);
        fileManager.loadLevel(new File("data/pathX/Level11.xml"), dataCopy);
        dataCopy.setLoadedLevel(true);
        pxg.setRenderedBackground(LEVEL11_GAME_TYPE);
        dataCopy.setCurrentLevel("11");
        createEnemies();
        levelSetup();

        JOptionPane jop = new JOptionPane();
        jop.setMessage(LEVEL11_INFO);
        jop.setMessageType(JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = jop.createDialog(pxg, "Information about the Level");
        dialog.setVisible(true);

    }

    public void switchToLevel12() {
        insideCanvas.setRenderedBackground(LEVEL2_GAME_TYPE);
        fileManager.loadLevel(new File("data/pathX/Level12.xml"), dataCopy);
        dataCopy.setLoadedLevel(true);
        pxg.setRenderedBackground(LEVEL12_GAME_TYPE);
        dataCopy.setCurrentLevel("12");
        createEnemies();
        levelSetup();

        JOptionPane jop = new JOptionPane();
        jop.setMessage(LEVEL12_INFO);
        jop.setMessageType(JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = jop.createDialog(pxg, "Information about the Level");
        dialog.setVisible(true);

    }

    public void switchToLevel13() {
        insideCanvas.setRenderedBackground(LEVEL2_GAME_TYPE);
        fileManager.loadLevel(new File("data/pathX/Level13.xml"), dataCopy);
        dataCopy.setLoadedLevel(true);
        pxg.setRenderedBackground(LEVEL13_GAME_TYPE);
        dataCopy.setCurrentLevel("13");
        createEnemies();
        levelSetup();

        JOptionPane jop = new JOptionPane();
        jop.setMessage(LEVEL13_INFO);
        jop.setMessageType(JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = jop.createDialog(pxg, "Information about the Level");
        dialog.setVisible(true);

    }

    public void switchToLevel14() {
        insideCanvas.setRenderedBackground(LEVEL2_GAME_TYPE);
        fileManager.loadLevel(new File("data/pathX/Level14.xml"), dataCopy);
        dataCopy.setLoadedLevel(true);
        pxg.setRenderedBackground(LEVEL14_GAME_TYPE);
        dataCopy.setCurrentLevel("14");
        createEnemies();
        levelSetup();

        JOptionPane jop = new JOptionPane();
        jop.setMessage(LEVEL14_INFO);
        jop.setMessageType(JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = jop.createDialog(pxg, "Information about the Level");
        dialog.setVisible(true);

    }

    public void switchToLevel15() {
        insideCanvas.setRenderedBackground(LEVEL2_GAME_TYPE);
        fileManager.loadLevel(new File("data/pathX/Level15.xml"), dataCopy);
        dataCopy.setLoadedLevel(true);
        pxg.setRenderedBackground(LEVEL15_GAME_TYPE);
        dataCopy.setCurrentLevel("15");
        createEnemies();
        levelSetup();

        JOptionPane jop = new JOptionPane();
        jop.setMessage(LEVEL15_INFO);
        jop.setMessageType(JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = jop.createDialog(pxg, "Information about the Level");
        dialog.setVisible(true);

    }

    public void switchToLevel16() {
        insideCanvas.setRenderedBackground(LEVEL2_GAME_TYPE);
        fileManager.loadLevel(new File("data/pathX/Level16.xml"), dataCopy);
        dataCopy.setLoadedLevel(true);
        pxg.setRenderedBackground(LEVEL16_GAME_TYPE);
        dataCopy.setCurrentLevel("16");
        createEnemies();
        levelSetup();

        JOptionPane jop = new JOptionPane();
        jop.setMessage(LEVEL16_INFO);
        jop.setMessageType(JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = jop.createDialog(pxg, "Information about the Level");
        dialog.setVisible(true);

    }

    public void switchToLevel17() {
        insideCanvas.setRenderedBackground(LEVEL2_GAME_TYPE);
        fileManager.loadLevel(new File("data/pathX/Level17.xml"), dataCopy);
        dataCopy.setLoadedLevel(true);
        pxg.setRenderedBackground(LEVEL17_GAME_TYPE);
        dataCopy.setCurrentLevel("17");
        createEnemies();
        levelSetup();

        JOptionPane jop = new JOptionPane();
        jop.setMessage(LEVEL17_INFO);
        jop.setMessageType(JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = jop.createDialog(pxg, "Information about the Level");
        dialog.setVisible(true);

    }

    public void switchToLevel18() {
        insideCanvas.setRenderedBackground(LEVEL2_GAME_TYPE);
        fileManager.loadLevel(new File("data/pathX/Level18.xml"), dataCopy);
        dataCopy.setLoadedLevel(true);
        pxg.setRenderedBackground(LEVEL18_GAME_TYPE);
        dataCopy.setCurrentLevel("18");
        createEnemies();
        levelSetup();

        JOptionPane jop = new JOptionPane();
        jop.setMessage(LEVEL18_INFO);
        jop.setMessageType(JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = jop.createDialog(pxg, "Information about the Level");
        dialog.setVisible(true);

    }

    public void switchToLevel19() {
        insideCanvas.setRenderedBackground(LEVEL2_GAME_TYPE);
        fileManager.loadLevel(new File("data/pathX/Level19.xml"), dataCopy);
        dataCopy.setLoadedLevel(true);
        pxg.setRenderedBackground(LEVEL19_GAME_TYPE);
        dataCopy.setCurrentLevel("19");
        createEnemies();
        levelSetup();

        JOptionPane jop = new JOptionPane();
        jop.setMessage(LEVEL19_INFO);
        jop.setMessageType(JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = jop.createDialog(pxg, "Information about the Level");
        dialog.setVisible(true);

    }

    public void switchToLevel20() {
        insideCanvas.setRenderedBackground(LEVEL2_GAME_TYPE);
        fileManager.loadLevel(new File("data/pathX/Level20.xml"), dataCopy);
        dataCopy.setLoadedLevel(true);
        pxg.setRenderedBackground(LEVEL20_GAME_TYPE);
        dataCopy.setCurrentLevel("20");
        createEnemies();
        levelSetup();

        JOptionPane jop = new JOptionPane();
        jop.setMessage(LEVEL20_INFO);
        jop.setMessageType(JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = jop.createDialog(pxg, "Information about the Level");
        dialog.setVisible(true);

    }

    public void menuSetup() {
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        dataCopy.setLoadedLevel(false);
        // ACTIVATE THE TOOLBAR AND ITS CONTROLS
        guiButtons.get(NEW_GAME_BUTTON_TYPE).setState(PathXTileState.VISIBLE_STATE.toString());
        guiButtons.get(NEW_GAME_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(NEW_GAME_BUTTON_TYPE).setX(NEW_BUTTON_X);
        guiButtons.get(NEW_GAME_BUTTON_TYPE).setY(NEW_BUTTON_Y);
        guiButtons.get(BACK_BUTTON_TYPE).setState(PathXTileState.VISIBLE_STATE.toString());
        guiButtons.get(BACK_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(BACK_BUTTON_TYPE).setX(BACK_BUTTON_X);
        guiButtons.get(BACK_BUTTON_TYPE).setY(BACK_BUTTON_Y);
        // DEACTIVATE THE LEVEL SELECT BUTTONS
        ArrayList<String> levels = props.getPropertyOptionsList(pathXPropertyType.LEVEL_OPTIONS);
        for (String level : levels) {
            guiButtons.get(level).setState(PathXTileState.INVISIBLE_STATE.toString());
            guiButtons.get(level).setEnabled(false);
        }

        insideCanvas.setEnabled(false);
        insideCanvas.setVisible(false);
        pxg.setEnabled(false);
        pxg.setVisible(false);

        isOnLevelSelect = false;

        guiEnemies.clear();
        dataCopy.getViewport().reset();

    }

    public void levelSetup() {

        isOnLevelSelect = false;
        // CHANGE THE BACKGROUND
        guiDecor.get(BACKGROUND_TYPE).setState(LEVEL_SCREEN_STATE);

        createPlayer();

        // DEACTIVATE THE TOOLBAR CONTROLS
        guiButtons.get(NEW_GAME_BUTTON_TYPE).setState(PathXTileState.VISIBLE_STATE.toString());
        guiButtons.get(NEW_GAME_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(NEW_GAME_BUTTON_TYPE).setX(200);
        guiButtons.get(NEW_GAME_BUTTON_TYPE).setY(155);
        guiButtons.get(BACK_BUTTON_TYPE).setState(PathXTileState.VISIBLE_STATE.toString());
        guiButtons.get(BACK_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(BACK_BUTTON_TYPE).setX(140);
        guiButtons.get(BACK_BUTTON_TYPE).setY(155);
        guiButtons.get(BACK_TO_LEVEL_SELECT_TYPE).setState(PathXTileState.VISIBLE_STATE.toString());
        guiButtons.get(BACK_TO_LEVEL_SELECT_TYPE).setEnabled(true);
        guiButtons.get(PAUSE_BUTTON_TYPE).setState(PathXTileState.VISIBLE_STATE.toString());
        guiButtons.get(PAUSE_BUTTON_TYPE).setEnabled(true);

        guiButtons.get(UP_BUTTON_TYPE).setState(PathXTileState.VISIBLE_STATE.toString());
        guiButtons.get(UP_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(UP_BUTTON_TYPE).setX(UP_BUTTON_GAME_X);
        guiButtons.get(UP_BUTTON_TYPE).setY(UP_BUTTON_GAME_Y);
        guiButtons.get(DOWN_BUTTON_TYPE).setState(PathXTileState.VISIBLE_STATE.toString());
        guiButtons.get(DOWN_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(DOWN_BUTTON_TYPE).setX(DOWN_BUTTON_GAME_X);
        guiButtons.get(DOWN_BUTTON_TYPE).setY(DOWN_BUTTON_GAME_Y);
        guiButtons.get(LEFT_BUTTON_TYPE).setState(PathXTileState.VISIBLE_STATE.toString());
        guiButtons.get(LEFT_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEFT_BUTTON_TYPE).setX(LEFT_BUTTON_GAME_X);
        guiButtons.get(LEFT_BUTTON_TYPE).setY(LEFT_BUTTON_GAME_Y);
        guiButtons.get(RIGHT_BUTTON_TYPE).setState(PathXTileState.VISIBLE_STATE.toString());
        guiButtons.get(RIGHT_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(RIGHT_BUTTON_TYPE).setX(RIGHT_BUTTON_GAME_X);
        guiButtons.get(RIGHT_BUTTON_TYPE).setY(RIGHT_BUTTON_GAME_Y);

        guiEnemies.get(PLAYER_TYPE).setState(PathXTileState.VISIBLE_STATE.toString());
        guiEnemies.get(PLAYER_TYPE).setEnabled(true);

        for (int i = 0; i < dataCopy.getLevel().getNumBandits(); i++) {
            if (guiEnemies.containsKey(BANDIT_TYPE + i)) {
                guiEnemies.get(BANDIT_TYPE + i).setState(PathXTileState.VISIBLE_STATE.toString());
                guiEnemies.get(BANDIT_TYPE + i).setEnabled(true);
            }
        }
        for (int i = 0; i < dataCopy.getLevel().getNumPolice(); i++) {
            if (guiEnemies.containsKey(POLICE_TYPE + i)) {
                guiEnemies.get(POLICE_TYPE + i).setState(PathXTileState.VISIBLE_STATE.toString());
                guiEnemies.get(POLICE_TYPE + i).setEnabled(true);
            }
        }
        for (int i = 0; i < dataCopy.getLevel().getNumZombies(); i++) {
            if (guiEnemies.containsKey(ZOMBIE_TYPE + i)) {
                guiEnemies.get(ZOMBIE_TYPE + i).setState(PathXTileState.VISIBLE_STATE.toString());
                guiEnemies.get(ZOMBIE_TYPE + i).setEnabled(true);
            }
        }

        // ACTIVATE THE LEVEL SELECT BUTTONS
        // DEACTIVATE THE LEVEL SELECT BUTTONS
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        ArrayList<String> levels = props.getPropertyOptionsList(pathXPropertyType.LEVEL_OPTIONS);
        for (String level : levels) {
            guiButtons.get(level).setState(PathXTileState.INVISIBLE_STATE.toString());
            guiButtons.get(level).setEnabled(false);
        }

        ArrayList<String> gameLevels = props.getPropertyOptionsList(pathXPropertyType.GAME_LEVELS);
        for (String level : gameLevels) {
            guiButtons.get(level).setState(PathXTileState.INVISIBLE_STATE.toString());
            guiButtons.get(level).setEnabled(false);
        }

        // DEACTIVATE ALL DIALOGS
        guiDialogs.get(WIN_DIALOG_TYPE).setState(PathXTileState.INVISIBLE_STATE.toString());
        guiDialogs.get(STATS_DIALOG_TYPE).setState(PathXTileState.INVISIBLE_STATE.toString());

        // HIDE THE TILES
        //((PathXDataModel) data).enableTiles(false);
        // MAKE THE CURRENT SCREEN THE MENU SCREEN
        currentScreenState = MENU_SCREEN_STATE;

        // AND UPDATE THE DATA GAME STATE
        data.setGameState(MiniGameState.NOT_STARTED);

        insideCanvas.setEnabled(false);
        insideCanvas.setVisible(false);

        pxg.setBounds(274, 19, 978, 667);

        pxg.setEnabled(true);
        pxg.setVisible(true);

        guiEnemies.get(PLAYER_TYPE).setX(dataCopy.getLevel().getStartingLocation().getX());
        guiEnemies.get(PLAYER_TYPE).setY(dataCopy.getLevel().getStartingLocation().getY());

        // PLAY THE GAMEPLAY SCREEN SONG
        if (!isMuted()) {
            audio.stop(pathXPropertyType.SONG_CUE_MENU_SCREEN.toString());
            audio.play(pathXPropertyType.SONG_CUE_GAME_SCREEN.toString(), true);
        }
        dataCopy.setSpeed(26);

        isOnGameLevel = true;
    }

    public void createEnemies() {
        //Create enemies for the level.
        for (int i = 0; i < dataCopy.getLevel().getNumBandits(); i++) {
            createBandits("BANDIT_TYPE" + i);
            //System.out.println("BANDIT_TYPEs : " + i);
        }
        for (int i = 0; i < dataCopy.getLevel().getNumPolice(); i++) {
            createPolice("POLICE_TYPE" + i);
        }
        for (int i = 0; i < dataCopy.getLevel().getNumZombies(); i++) {
            createZombies("ZOMBIE_TYPE" + i);
        }
    }

    public void createBandits(String sprite) {
        // WE'LL USE AND REUSE THESE FOR LOADING STUFF

        BufferedImage img;
        SpriteType sT;
        carSprite s;

        // FIRST PUT THE ICON IN THE WINDOW
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty(pathXPropertyType.PATH_IMG);

        String newPlayer = props.getProperty(pathXPropertyType.IMAGE_BANDIT);
        sT = new SpriteType(BANDIT_TYPE);
        img = loadImageWithColorKey(imgPath + newPlayer, COLOR_KEY);
        sT.addState(PathXTileState.VISIBLE_STATE.toString(), img);

        Iterator<Intersection> it = dataCopy.getLevel().getIntersections().iterator();
        Intersection intersection = it.next();

        while (it.hasNext() && ((dataCopy.isStartingLocation(intersection))
                || (dataCopy.isDestination(intersection)) || intersection.getHasStarter())) {
            intersection = it.next();
        }
        s = new carSprite(sT, intersection.getX(), intersection.getY(), 0, 0, PathXTileState.INVISIBLE_STATE.toString());
        intersection.setHasStarter(true);
        s.setCurrentIntersection(intersection);
        guiEnemies.put(sprite, s);
    }

    public void createPolice(String sprite) {
        BufferedImage img;
        SpriteType sT;
        carSprite s;

        // FIRST PUT THE ICON IN THE WINDOW
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty(pathXPropertyType.PATH_IMG);

        String newPlayer = props.getProperty(pathXPropertyType.IMAGE_POLICE);
        sT = new SpriteType(POLICE_TYPE);
        img = loadImageWithColorKey(imgPath + newPlayer, COLOR_KEY);
        sT.addState(PathXTileState.VISIBLE_STATE.toString(), img);

        Iterator<Intersection> it = dataCopy.getLevel().getIntersections().iterator();
        Intersection intersection = it.next();

        while (it.hasNext() && ((dataCopy.isStartingLocation(intersection))
                || (dataCopy.isDestination(intersection)) || intersection.getHasStarter())) {
            intersection = it.next();
        }
        s = new carSprite(sT, intersection.getX(), intersection.getY(), 0, 0, PathXTileState.INVISIBLE_STATE.toString());
        intersection.setHasStarter(true);
        s.setCurrentIntersection(intersection);
        guiEnemies.put(sprite, s);
    }

    public void createZombies(String sprite) {
        BufferedImage img;
        SpriteType sT;
        carSprite s;

        // FIRST PUT THE ICON IN THE WINDOW
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty(pathXPropertyType.PATH_IMG);

        String newPlayer = props.getProperty(pathXPropertyType.IMAGE_ZOMBIE);
        sT = new SpriteType(ZOMBIE_TYPE);
        img = loadImageWithColorKey(imgPath + newPlayer, COLOR_KEY);
        sT.addState(PathXTileState.VISIBLE_STATE.toString(), img);

        Iterator<Intersection> it = dataCopy.getLevel().getIntersections().iterator();
        Intersection intersection = it.next();

        while (it.hasNext() && ((dataCopy.isStartingLocation(intersection))
                || (dataCopy.isDestination(intersection)) || intersection.getHasStarter())) {
            intersection = it.next();
        }
        s = new carSprite(sT, intersection.getX(), intersection.getY(), 0, 0, PathXTileState.INVISIBLE_STATE.toString());
        intersection.setHasStarter(true);
        s.setCurrentIntersection(intersection);
        guiEnemies.put(sprite, s);
    }

    public void createPlayer() {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        BufferedImage img;
        SpriteType sT;
        String imgPath = props.getProperty(pathXPropertyType.PATH_IMG);

        String newPlayer = props.getProperty(pathXPropertyType.IMAGE_PLAYER);
        sT = new SpriteType(PLAYER_TYPE);
        img = loadImageWithColorKey(imgPath + newPlayer, COLOR_KEY);
        sT.addState(PathXTileState.VISIBLE_STATE.toString(), img);
        String playerOverButton = props.getProperty(pathXPropertyType.IMAGE_PLAYER);
        img = loadImageWithColorKey(imgPath + playerOverButton, COLOR_KEY);
        sT.addState(PathXTileState.MOUSE_OVER_STATE.toString(), img);
        carSprite cs = new carSprite(sT, 0, 0, 0, 0, PathXTileState.INVISIBLE_STATE.toString());
        guiEnemies.put(PLAYER_TYPE, cs);
    }

    @Override
    /**
     * Initializes the sound and music to be used by the application.
     */
    public void initAudioContent() {
        try {
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            String audioPath = props.getProperty(pathXPropertyType.PATH_AUDIO);

            //LOAD ALL THE AUDIO
            loadAudioCue(pathXPropertyType.AUDIO_CUE_SELECT_TILE);
            loadAudioCue(pathXPropertyType.AUDIO_CUE_DESELECT_TILE);
            loadAudioCue(pathXPropertyType.AUDIO_CUE_GOOD_MOVE);
            loadAudioCue(pathXPropertyType.AUDIO_CUE_BAD_MOVE);
            loadAudioCue(pathXPropertyType.AUDIO_CUE_CHEAT);
            loadAudioCue(pathXPropertyType.AUDIO_CUE_UNDO);
            loadAudioCue(pathXPropertyType.AUDIO_CUE_WIN);
            loadAudioCue(pathXPropertyType.SONG_CUE_MENU_SCREEN);
            loadAudioCue(pathXPropertyType.SONG_CUE_GAME_SCREEN);
            loadAudioCue(pathXPropertyType.AUDIO_CUE_CRASH);

            // PLAY THE WELCOME SCREEN SONG
            audio.play(pathXPropertyType.SONG_CUE_MENU_SCREEN.toString(), true);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException | InvalidMidiDataException | MidiUnavailableException e) {
            errorHandler.processError(pathXPropertyType.TEXT_ERROR_LOADING_AUDIO);
        }
    }

    private void loadAudioCue(pathXPropertyType audioCueType)
            throws UnsupportedAudioFileException, IOException, LineUnavailableException,
            InvalidMidiDataException, MidiUnavailableException {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String audioPath = props.getProperty(pathXPropertyType.PATH_AUDIO);
        String cue = props.getProperty(audioCueType.toString());
        audio.loadAudio(audioCueType.toString(), audioPath + cue);
    }

    @Override
    public void initData() {
        // INIT OUR ERROR HANDLER

        errorHandler = new PathXErrorHandler(window);

        // INIT OUR FILE MANAGER
        fileManager = new PathXFileManager(this, new File(PATH_DATA_LEVELS + LEVEL_SCHEMA));

        // LOAD THE PLAYER'S RECORD FROM A FILE
        record = fileManager.loadRecord();

        // INIT OUR DATA MANAGER
        data = new PathXDataModel(this);
        dataCopy = (PathXDataModel) data;

    }

    @Override
    public void initGUIControls() {
        // WE'LL USE AND REUSE THESE FOR LOADING STUFF

        guiEnemies = new TreeMap();

        BufferedImage img;
        float x, y;
        SpriteType sT;
        Sprite s;

        // FIRST PUT THE ICON IN THE WINDOW
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty(pathXPropertyType.PATH_IMG);
        String windowIconFile = props.getProperty(pathXPropertyType.IMAGE_WINDOW_ICON);
        img = loadImage(imgPath + windowIconFile);
        window.setIconImage(img);

        // CONSTRUCT THE PANEL WHERE WE'LL DRAW EVERYTHING
        canvas = new PathXPanel(this, (PathXDataModel) data);

        insideCanvas = new PathXGamePanel(this, (PathXDataModel) data);
        canvas.add(insideCanvas);
        insideCanvas.setVisible(false);
        pxg = new PathXGamePanel(this, (PathXDataModel) data);
        //pxg.addMouseListener(new PathXMouseController((PathXDataModel) data));
        //pxg.addKeyListener(keyHandler);
        canvas.add(pxg);

        // LOAD THE BACKGROUNDS, WHICH ARE GUI DECOR
        currentScreenState = MENU_SCREEN_STATE;
        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_BACKGROUND_MENU));
        sT = new SpriteType(BACKGROUND_TYPE);
        sT.addState(MENU_SCREEN_STATE, img);
        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_BACKGROUND_GAME)); // Level BaCKGROUND
        sT.addState(GAME_SCREEN_STATE, img);

        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_BACKGROUND_HELP));
        sT.addState(HELP_SCREEN_STATE, img);
        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_BACKGROUND_SETTINGS));
        sT.addState(SETTINGS_SCREEN_STATE, img);
        s = new Sprite(sT, 0, 0, 0, 0, MENU_SCREEN_STATE);
        guiDecor.put(BACKGROUND_TYPE, s);

        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_BACKGROUND_GAMEMAP)); // Level BaCKGROUND
        sT.addState(GAMES_SCREEN_STATE, img);
        s = new Sprite(sT, 0, 0, 0, 0, GAMES_SCREEN_STATE);
        guiDecor.put(BACKGROUND_GAME_TYPE, s);

        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_BACKGROUND_GAME_MENU)); // Level MENU
        sT.addState(LEVEL_SCREEN_STATE, img);
        s = new Sprite(sT, 0, 0, 0, 0, GAMES_SCREEN_STATE);
        guiDecor.put(LEVEL_GAME_TYPE, s);

        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_BACKGROUND_LEVEL1)); // Level SBU
        sT.addState(LEVEL1_SCREEN_STATE, img);
        s = new Sprite(sT, 0, 0, 0, 0, LEVEL1_SCREEN_STATE);
        guiDecor.put(LEVEL1_GAME_TYPE, s);

        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_BACKGROUND_LEVEL2)); // Level SBU
        sT.addState(LEVEL2_SCREEN_STATE, img);
        s = new Sprite(sT, 0, 0, 0, 0, LEVEL2_SCREEN_STATE);
        guiDecor.put(LEVEL2_GAME_TYPE, s);

        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_BACKGROUND_LEVEL3)); // Level SBU
        sT.addState(LEVEL3_SCREEN_STATE, img);
        s = new Sprite(sT, 0, 0, 0, 0, LEVEL3_SCREEN_STATE);
        guiDecor.put(LEVEL3_GAME_TYPE, s);

        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_BACKGROUND_LEVEL4)); // Level SBU
        sT.addState(LEVEL4_SCREEN_STATE, img);
        s = new Sprite(sT, 0, 0, 0, 0, LEVEL4_SCREEN_STATE);
        guiDecor.put(LEVEL4_GAME_TYPE, s);

        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_BACKGROUND_LEVEL5)); // Level SBU
        sT.addState(LEVEL5_SCREEN_STATE, img);
        s = new Sprite(sT, 0, 0, 0, 0, LEVEL5_SCREEN_STATE);
        guiDecor.put(LEVEL5_GAME_TYPE, s);

        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_BACKGROUND_LEVEL6)); // Level SBU
        sT.addState(LEVEL6_SCREEN_STATE, img);
        s = new Sprite(sT, 0, 0, 0, 0, LEVEL6_SCREEN_STATE);
        guiDecor.put(LEVEL6_GAME_TYPE, s);

        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_BACKGROUND_LEVEL7)); // Level SBU
        sT.addState(LEVEL7_SCREEN_STATE, img);
        s = new Sprite(sT, 0, 0, 0, 0, LEVEL7_SCREEN_STATE);
        guiDecor.put(LEVEL7_GAME_TYPE, s);

        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_BACKGROUND_LEVEL8)); // Level SBU
        sT.addState(LEVEL8_SCREEN_STATE, img);
        s = new Sprite(sT, 0, 0, 0, 0, LEVEL8_SCREEN_STATE);
        guiDecor.put(LEVEL8_GAME_TYPE, s);

        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_BACKGROUND_LEVEL9)); // Level SBU
        sT.addState(LEVEL9_SCREEN_STATE, img);
        s = new Sprite(sT, 0, 0, 0, 0, LEVEL9_SCREEN_STATE);
        guiDecor.put(LEVEL9_GAME_TYPE, s);

        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_BACKGROUND_LEVEL10)); // Level SBU
        sT.addState(LEVEL10_SCREEN_STATE, img);
        s = new Sprite(sT, 0, 0, 0, 0, LEVEL10_SCREEN_STATE);
        guiDecor.put(LEVEL10_GAME_TYPE, s);

        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_BACKGROUND_LEVEL11)); // Level SBU
        sT.addState(LEVEL11_SCREEN_STATE, img);
        s = new Sprite(sT, 0, 0, 0, 0, LEVEL11_SCREEN_STATE);
        guiDecor.put(LEVEL11_GAME_TYPE, s);

        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_BACKGROUND_LEVEL12)); // Level SBU
        sT.addState(LEVEL12_SCREEN_STATE, img);
        s = new Sprite(sT, 0, 0, 0, 0, LEVEL12_SCREEN_STATE);
        guiDecor.put(LEVEL12_GAME_TYPE, s);

        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_BACKGROUND_LEVEL13)); // Level SBU
        sT.addState(LEVEL13_SCREEN_STATE, img);
        s = new Sprite(sT, 0, 0, 0, 0, LEVEL13_SCREEN_STATE);
        guiDecor.put(LEVEL13_GAME_TYPE, s);

        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_BACKGROUND_LEVEL14)); // Level SBU
        sT.addState(LEVEL14_SCREEN_STATE, img);
        s = new Sprite(sT, 0, 0, 0, 0, LEVEL14_SCREEN_STATE);
        guiDecor.put(LEVEL14_GAME_TYPE, s);

        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_BACKGROUND_LEVEL15)); // Level SBU
        sT.addState(LEVEL15_SCREEN_STATE, img);
        s = new Sprite(sT, 0, 0, 0, 0, LEVEL15_SCREEN_STATE);
        guiDecor.put(LEVEL15_GAME_TYPE, s);

        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_BACKGROUND_LEVEL16)); // Level SBU
        sT.addState(LEVEL16_SCREEN_STATE, img);
        s = new Sprite(sT, 0, 0, 0, 0, LEVEL16_SCREEN_STATE);
        guiDecor.put(LEVEL16_GAME_TYPE, s);

        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_BACKGROUND_LEVEL17)); // Level SBU
        sT.addState(LEVEL17_SCREEN_STATE, img);
        s = new Sprite(sT, 0, 0, 0, 0, LEVEL17_SCREEN_STATE);
        guiDecor.put(LEVEL17_GAME_TYPE, s);

        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_BACKGROUND_LEVEL18)); // Level SBU
        sT.addState(LEVEL18_SCREEN_STATE, img);
        s = new Sprite(sT, 0, 0, 0, 0, LEVEL18_SCREEN_STATE);
        guiDecor.put(LEVEL18_GAME_TYPE, s);

        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_BACKGROUND_LEVEL19)); // Level SBU
        sT.addState(LEVEL19_SCREEN_STATE, img);
        s = new Sprite(sT, 0, 0, 0, 0, LEVEL19_SCREEN_STATE);
        guiDecor.put(LEVEL19_GAME_TYPE, s);

        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_BACKGROUND_LEVEL20)); // Level SBU
        sT.addState(LEVEL20_SCREEN_STATE, img);
        s = new Sprite(sT, 0, 0, 0, 0, LEVEL20_SCREEN_STATE);
        guiDecor.put(LEVEL20_GAME_TYPE, s);

        // LOAD THE Arrow CURSOR
        String cursorName = props.getProperty(pathXPropertyType.IMAGE_CURSOR_WAND);
        img = loadImageWithColorKey(imgPath + cursorName, COLOR_KEY);
        Point cursorHotSpot = new Point(0, 0);
        Cursor arrowCursor = Toolkit.getDefaultToolkit().createCustomCursor(img, cursorHotSpot, cursorName);
        window.setCursor(arrowCursor);

        // ADD A BUTTON FOR EACH Button on the splash screen AVAILABLE
        ArrayList<String> buttons = props.getPropertyOptionsList(pathXPropertyType.LEVEL_OPTIONS);
        ArrayList<String> buttonImageNames = props.getPropertyOptionsList(pathXPropertyType.LEVEL_IMAGE_OPTIONS);
        ArrayList<String> buttonMouseOverImageNames = props.getPropertyOptionsList(pathXPropertyType.LEVEL_MOUSE_OVER_IMAGE_OPTIONS);
        float totalWidth = buttons.size() * (LEVEL_BUTTON_WIDTH + LEVEL_BUTTON_MARGIN) - LEVEL_BUTTON_MARGIN;
        Viewport viewport = data.getViewport();
        x = (viewport.getScreenWidth() - totalWidth) / 2.0f;
        for (int i = 0; i < buttons.size(); i++) {
            sT = new SpriteType(LEVEL_SELECT_BUTTON_TYPE);
            img = loadImageWithColorKey(imgPath + buttonImageNames.get(i), COLOR_KEY);
            sT.addState(PathXTileState.VISIBLE_STATE.toString(), img);
            img = loadImageWithColorKey(imgPath + buttonMouseOverImageNames.get(i), COLOR_KEY);
            sT.addState(PathXTileState.MOUSE_OVER_STATE.toString(), img);
            s = new Sprite(sT, x, LEVEL_BUTTON_Y, 0, 0, PathXTileState.VISIBLE_STATE.toString());
            guiButtons.put(buttons.get(i), s);
            x += LEVEL_BUTTON_WIDTH + LEVEL_BUTTON_MARGIN;
        }

        // ADD A BUTTON FOR EACH Game LEVEL AVAILABLE
        ArrayList<String> levels = props.getPropertyOptionsList(pathXPropertyType.GAME_LEVELS);
        ArrayList<String> levelImageNames = props.getPropertyOptionsList(pathXPropertyType.GAME_LEVEL_IMAGE_OPTIONS);
        ArrayList<String> levelMouseOverImageNames = props.getPropertyOptionsList(pathXPropertyType.GAME_LEVEL_MOUSE_OVER_IMAGE_OPTIONS);
        ArrayList<String> robbedImageNames = props.getPropertyOptionsList(pathXPropertyType.GAME_LEVEL_ROBBED);
        ArrayList<String> lockedImageNames = props.getPropertyOptionsList(pathXPropertyType.GAME_LEVEL_LOCKED);
        int x_add = 0;
        int y_add = 0;
        for (int i = 0; i < levels.size(); i++) {
            if (i == 0) {
                x_add = 0;
                y_add = 0;
            } else {
                x_add += 60;
                y_add += -10;
            }
            sT = new SpriteType(LEVEL_GAME_TYPE);
            img = loadImageWithColorKey(imgPath + levelImageNames.get(i), COLOR_KEY);
            sT.addState(PathXTileState.VISIBLE_STATE.toString(), img);
            img = loadImageWithColorKey(imgPath + levelMouseOverImageNames.get(i), COLOR_KEY);
            sT.addState(PathXTileState.MOUSE_OVER_STATE.toString(), img);
            img = loadImageWithColorKey(imgPath + robbedImageNames.get(i), COLOR_KEY);
            sT.addState(PathXTileState.ROBBED_STATE.toString(), img);
            img = loadImageWithColorKey(imgPath + lockedImageNames.get(i), COLOR_KEY);
            sT.addState(PathXTileState.LOCKED_STATE.toString(), img);
            s = new Sprite(sT, 1060 - x_add, 315 - y_add, 0, 0, PathXTileState.INVISIBLE_STATE.toString());
            guiButtons.put(levels.get(i), s);
        }

        // ADD THE CONTROLS ALONG THE GAME SCREEn
        // THEN THE NEW BUTTON
        String newButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_NEW);
        sT = new SpriteType(NEW_GAME_BUTTON_TYPE);
        img = loadImageWithColorKey(imgPath + newButton, COLOR_KEY);
        sT.addState(PathXTileState.VISIBLE_STATE.toString(), img);
        String newMouseOverButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_NEW_MOUSE_OVER);
        img = loadImageWithColorKey(imgPath + newMouseOverButton, COLOR_KEY);
        sT.addState(PathXTileState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, NEW_BUTTON_X, NEW_BUTTON_Y, 0, 0, PathXTileState.INVISIBLE_STATE.toString());
        guiButtons.put(NEW_GAME_BUTTON_TYPE, s);

        // THEN THE CHECK BUTTON FOR MUSIC
        String newButtonMusic = props.getProperty(pathXPropertyType.IMAGE_BUTTON_CHECK_BOX);
        sT = new SpriteType(MUSIC_CHECK_BOX_TYPE);
        img = loadImageWithColorKey(imgPath + newButtonMusic, COLOR_KEY);
        sT.addState(PathXTileState.VISIBLE_STATE.toString(), img);
        String newButtonMusicChecked = props.getProperty(pathXPropertyType.IMAGE_BUTTON_CHECK_BOX_CHECKED);
        img = loadImageWithColorKey(imgPath + newButtonMusicChecked, COLOR_KEY);
        sT.addState(PathXTileState.CHECKED_STATE.toString(), img);
        String newButtonMusicCheckedOver = props.getProperty(pathXPropertyType.IMAGE_BUTTON_CHECK_BOX);
        img = loadImageWithColorKey(imgPath + newButtonMusicCheckedOver, COLOR_KEY);
        sT.addState(PathXTileState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, MUSIC_X, MUSIC_Y, 0, 0, PathXTileState.INVISIBLE_STATE.toString());
        guiButtons.put(MUSIC_CHECK_BOX_TYPE, s);

        // THEN THE CHECK BUTTON FOR SOUND
        String newButtonSound = props.getProperty(pathXPropertyType.IMAGE_BUTTON_CHECK_BOX);
        sT = new SpriteType(SOUND_CHECK_BOX_TYPE);
        img = loadImageWithColorKey(imgPath + newButtonSound, COLOR_KEY);
        sT.addState(PathXTileState.VISIBLE_STATE.toString(), img);
        String newButtonSoundChecked = props.getProperty(pathXPropertyType.IMAGE_BUTTON_CHECK_BOX_CHECKED);
        img = loadImageWithColorKey(imgPath + newButtonSoundChecked, COLOR_KEY);
        sT.addState(PathXTileState.CHECKED_STATE.toString(), img);
        String newButtonSoundCheckedOver = props.getProperty(pathXPropertyType.IMAGE_BUTTON_CHECK_BOX);
        img = loadImageWithColorKey(imgPath + newButtonSoundCheckedOver, COLOR_KEY);
        sT.addState(PathXTileState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, SOUND_X, SOUND_Y, 0, 0, PathXTileState.INVISIBLE_STATE.toString());
        guiButtons.put(SOUND_CHECK_BOX_TYPE, s);

        // THEN THE BACK BUTTON
        String backButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_BACK);
        sT = new SpriteType(BACK_BUTTON_TYPE);
        img = loadImageWithColorKey(imgPath + backButton, COLOR_KEY);
        sT.addState(PathXTileState.VISIBLE_STATE.toString(), img);
        String backMouseOverButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_BACK_MOUSE_OVER);
        img = loadImageWithColorKey(imgPath + backMouseOverButton, COLOR_KEY);
        sT.addState(PathXTileState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, BACK_BUTTON_X, BACK_BUTTON_Y, 0, 0, PathXTileState.INVISIBLE_STATE.toString());
        guiButtons.put(BACK_BUTTON_TYPE, s);

        // THEN THE BACK TO LEVEL SELECT BUTTON
        String backButtonS = props.getProperty(pathXPropertyType.IMAGE_BACK_TO_LEVEL_SELECT);
        sT = new SpriteType(BACK_TO_LEVEL_SELECT_TYPE);
        img = loadImageWithColorKey(imgPath + backButtonS, COLOR_KEY);
        sT.addState(PathXTileState.VISIBLE_STATE.toString(), img);
        String backMouseOverButtonS = props.getProperty(pathXPropertyType.IMAGE_BACK_TO_LEVEL_SELECT_MOUSE_OVER);
        img = loadImageWithColorKey(imgPath + backMouseOverButtonS, COLOR_KEY);
        sT.addState(PathXTileState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, SELECT_LEVEL_X, SELECT_LEVEL_Y, 0, 0, PathXTileState.INVISIBLE_STATE.toString());
        guiButtons.put(BACK_TO_LEVEL_SELECT_TYPE, s);

        // THEN THE Pause
        String pButton = props.getProperty(pathXPropertyType.IMAGE_PAUSE);
        sT = new SpriteType(PAUSE_BUTTON_TYPE);
        img = loadImageWithColorKey(imgPath + pButton, COLOR_KEY);
        sT.addState(PathXTileState.VISIBLE_STATE.toString(), img);
        String pButtonMouseOver = props.getProperty(pathXPropertyType.IMAGE_PAUSE_MOUSE_OVER);
        img = loadImageWithColorKey(imgPath + pButtonMouseOver, COLOR_KEY);
        sT.addState(PathXTileState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, PAUSE_X, PAUSE_Y, 0, 0, PathXTileState.INVISIBLE_STATE.toString());
        guiButtons.put(PAUSE_BUTTON_TYPE, s);

        //up key
        String upButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_UP);
        sT = new SpriteType(UP_BUTTON_TYPE);
        img = loadImageWithColorKey(imgPath + upButton, COLOR_KEY);
        sT.addState(PathXTileState.VISIBLE_STATE.toString(), img);
        String upMouseOverButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_UP_MOUSE_OVER);
        img = loadImageWithColorKey(imgPath + upMouseOverButton, COLOR_KEY);
        sT.addState(PathXTileState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, UP_BUTTON_X, UP_BUTTON_Y, 0, 0, PathXTileState.INVISIBLE_STATE.toString());
        guiButtons.put(UP_BUTTON_TYPE, s);

        //down key
        String downButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_DOWN);
        sT = new SpriteType(DOWN_BUTTON_TYPE);
        img = loadImageWithColorKey(imgPath + downButton, COLOR_KEY);
        sT.addState(PathXTileState.VISIBLE_STATE.toString(), img);
        String downMouseOverButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_DOWN_MOUSE_OVER);
        img = loadImageWithColorKey(imgPath + downMouseOverButton, COLOR_KEY);
        sT.addState(PathXTileState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, DOWN_BUTTON_X, DOWN_BUTTON_Y, 0, 0, PathXTileState.INVISIBLE_STATE.toString());
        guiButtons.put(DOWN_BUTTON_TYPE, s);

        //left key
        String leftButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_LEFT);
        sT = new SpriteType(LEFT_BUTTON_TYPE);
        img = loadImageWithColorKey(imgPath + leftButton, COLOR_KEY);
        sT.addState(PathXTileState.VISIBLE_STATE.toString(), img);
        String leftMouseOverButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_LEFT_MOUSE_OVER);
        img = loadImageWithColorKey(imgPath + leftMouseOverButton, COLOR_KEY);
        sT.addState(PathXTileState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, LEFT_BUTTON_X, LEFT_BUTTON_Y, 0, 0, PathXTileState.INVISIBLE_STATE.toString());
        guiButtons.put(LEFT_BUTTON_TYPE, s);

        //right key
        String rightButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_RIGHT);
        sT = new SpriteType(RIGHT_BUTTON_TYPE);
        img = loadImageWithColorKey(imgPath + rightButton, COLOR_KEY);
        sT.addState(PathXTileState.VISIBLE_STATE.toString(), img);
        String rightMouseOverButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_RIGHT_MOUSE_OVER);
        img = loadImageWithColorKey(imgPath + rightMouseOverButton, COLOR_KEY);
        sT.addState(PathXTileState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, RIGHT_BUTTON_X, RIGHT_BUTTON_Y, 0, 0, PathXTileState.INVISIBLE_STATE.toString());
        guiButtons.put(RIGHT_BUTTON_TYPE, s);

        // NOW ADD THE DIALOGS
        // AND THE STATS DISPLAY
        String statsDialog = props.getProperty(pathXPropertyType.IMAGE_DIALOG_STATS);
        sT = new SpriteType(STATS_DIALOG_TYPE);
        img = loadImageWithColorKey(imgPath + statsDialog, COLOR_KEY);
        sT.addState(PathXTileState.VISIBLE_STATE.toString(), img);
        x = (viewport.getScreenWidth() / 2) - (img.getWidth(null) / 2);
        y = (viewport.getScreenHeight() / 2) - (img.getHeight(null) / 2);
        s = new Sprite(sT, x, y, 0, 0, PathXTileState.INVISIBLE_STATE.toString());
        guiDialogs.put(STATS_DIALOG_TYPE, s);

        // AND THE WIN CONDITION DISPLAY
        String winDisplay = props.getProperty(pathXPropertyType.IMAGE_DIALOG_WIN);
        sT = new SpriteType(WIN_DIALOG_TYPE);
        img = loadImageWithColorKey(imgPath + winDisplay, COLOR_KEY);
        sT.addState(PathXTileState.VISIBLE_STATE.toString(), img);
        x = (viewport.getScreenWidth() / 2) - (img.getWidth(null) / 2);
        y = (viewport.getScreenHeight() / 2) - (img.getHeight(null) / 2);
        s = new Sprite(sT, x, y, 0, 0, PathXTileState.INVISIBLE_STATE.toString());
        guiDialogs.put(WIN_DIALOG_TYPE, s);

        ArrayList<String> gameLevels = props.getPropertyOptionsList(pathXPropertyType.GAME_LEVELS);
        for (String level : gameLevels) {
            guiButtons.get(level).setState(PathXTileState.INVISIBLE_STATE.toString());
            guiButtons.get(level).setEnabled(false);
        }

        // THEN THE TILES STACKED TO THE TOP LEFT
        //((PathXDataModel) data).initTiles();
    }

    /**
     * Initializes the game event handlers for things like game gui buttons.
     */
    @Override
    public void initGUIHandlers() {
        // WE'LL RELAY UI EVENTS TO THIS OBJECT FOR HANDLING
        eventHandler = new PathXEventHandler(this);

        //insideCanvas.addMouseListener(eventHandler);
        //insideCanvas.addMouseMotionListener(eventHandler);
        // WE'LL HAVE A CUSTOM RESPONSE FOR WHEN THE USER CLOSES THE WINDOW
        window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        window.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                eventHandler.respondToExitRequest();
            }
        });

        // SEND ALL LEVEL SELECTION HANDLING OFF TO THE EVENT HANDLER
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        ArrayList<String> levels = props.getPropertyOptionsList(pathXPropertyType.LEVEL_OPTIONS);
        for (String levelFile : levels) {
            if (levelFile.equalsIgnoreCase("./pathX/SelectionSnake.sort")) { //Start
                Sprite levelButton = guiButtons.get(levelFile);
                levelButton.setActionCommand(PATH_DATA + levelFile);
                levelButton.setActionListener(new ActionListener() {
                    Sprite s;

                    public ActionListener init(Sprite initS) {
                        s = initS;
                        return this;
                    }

                    public void actionPerformed(ActionEvent ae) {
                        eventHandler.respondToSelectLevelRequest();
                    }
                }.init(levelButton));
            } else if (levelFile.equalsIgnoreCase("./pathX/BubbleSortCoil.sort")) { // Reset
                Sprite levelButton = guiButtons.get(levelFile);
                levelButton.setActionCommand(PATH_DATA + levelFile);
                levelButton.setActionListener(new ActionListener() {
                    Sprite s;

                    public ActionListener init(Sprite initS) {
                        s = initS;
                        return this;
                    }

                    public void actionPerformed(ActionEvent ae) {
                        eventHandler.respondToResetRequest();
                    }
                }.init(levelButton));
            } else if (levelFile.equalsIgnoreCase("./pathX/SelectionL.sort")) { // Settings
                Sprite levelButton = guiButtons.get(levelFile);
                levelButton.setActionCommand(PATH_DATA + levelFile);
                levelButton.setActionListener(new ActionListener() {
                    Sprite s;

                    public ActionListener init(Sprite initS) {
                        s = initS;
                        return this;
                    }

                    public void actionPerformed(ActionEvent ae) {
                        eventHandler.respondToSettingsRequest();
                    }
                }.init(levelButton));
            } else if (levelFile.equalsIgnoreCase("./pathX/BubbleSortO.sort")) { // Help
                Sprite levelButton = guiButtons.get(levelFile);
                levelButton.setActionCommand(PATH_DATA + levelFile);
                levelButton.setActionListener(new ActionListener() {
                    Sprite s;

                    public ActionListener init(Sprite initS) {
                        s = initS;
                        return this;
                    }

                    public void actionPerformed(ActionEvent ae) {
                        eventHandler.respondToHelpRequest();
                    }
                }.init(levelButton));
            }
        }

        // NEW GAME EVENT HANDLER
        guiButtons.get(NEW_GAME_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                //audio.play(pathXPropertyType.SONG_CUE_GAME_SCREEN.toString(), true);    
                eventHandler.respondToExitGameRequest();
            }
        });

        guiButtons.get(BACK_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToBackRequest();
            }
        });

        guiButtons.get(BACK_TO_LEVEL_SELECT_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToBackToLevelSelectRequest();
            }
        });

        guiButtons.get(UP_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToUpRequest();
            }
        });

        guiButtons.get(DOWN_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToDownRequest();
            }
        });

        guiButtons.get(LEFT_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToLeftRequest();
            }
        });

        guiButtons.get(RIGHT_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToRightRequest();
            }
        });

        guiButtons.get(PAUSE_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToPauseRequest();
            }
        });

        guiButtons.get(SOUND_CHECK_BOX_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToMuteSoundRequest();
            }
        });

        guiButtons.get(MUSIC_CHECK_BOX_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToMuteMusicRequest();
            }
        });

        ArrayList<String> gameLevels = props.getPropertyOptionsList(pathXPropertyType.GAME_LEVELS);
        for (String level : gameLevels) {
            switch (level) {
                case "./pathX/Level1.xml":
                    guiButtons.get(level).setActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        eventHandler.respondToLevel1();
                    }
                });
                    break;
                case "./pathX/Level2.xml":
                    guiButtons.get(level).setActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        eventHandler.respondToLevel2();
                    }
                });
                    break;
                case "./pathX/Level3.xml":
                    guiButtons.get(level).setActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        eventHandler.respondToLevel3();
                    }
                });
                    break;
                case "./pathX/Level4.xml":
                    guiButtons.get(level).setActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        eventHandler.respondToLevel4();
                    }
                });
                    break;
                case "./pathX/Level5.xml":
                    guiButtons.get(level).setActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        eventHandler.respondToLevel5();
                    }
                });
                    break;
                case "./pathX/Level6.xml":
                    guiButtons.get(level).setActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        eventHandler.respondToLevel6();
                    }
                });
                    break;
                case "./pathX/Level7.xml":
                    guiButtons.get(level).setActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        eventHandler.respondToLevel7();
                    }
                });
                    break;
                case "./pathX/Level8.xml":
                    guiButtons.get(level).setActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        eventHandler.respondToLevel8();
                    }
                });
                    break;
                case "./pathX/Level9.xml":
                    guiButtons.get(level).setActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        eventHandler.respondToLevel9();
                    }
                });
                    break;
                case "./pathX/Level10.xml":
                    guiButtons.get(level).setActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        eventHandler.respondToLevel10();
                    }
                });
                    break;

                case "./pathX/Level11.xml":
                    guiButtons.get(level).setActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        eventHandler.respondToLevel11();
                    }
                });
                    break;
                case "./pathX/Level12.xml":
                    guiButtons.get(level).setActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        eventHandler.respondToLevel12();
                    }
                });
                    break;
                case "./pathX/Level13.xml":
                    guiButtons.get(level).setActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        eventHandler.respondToLevel13();
                    }
                });
                    break;
                case "./pathX/Level14.xml":
                    guiButtons.get(level).setActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        eventHandler.respondToLevel14();
                    }
                });
                    break;
                case "./pathX/Level15.xml":
                    guiButtons.get(level).setActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        eventHandler.respondToLevel15();
                    }
                });
                    break;
                case "./pathX/Level16.xml":
                    guiButtons.get(level).setActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        eventHandler.respondToLevel16();
                    }
                });
                    break;
                case "./pathX/Level17.xml":
                    guiButtons.get(level).setActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        eventHandler.respondToLevel17();
                    }
                });
                    break;
                case "./pathX/Level18.xml":
                    guiButtons.get(level).setActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        eventHandler.respondToLevel18();
                    }
                });
                    break;
                case "./pathX/Level19.xml":
                    guiButtons.get(level).setActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        eventHandler.respondToLevel19();
                    }
                });
                    break;
                case "./pathX/Level20.xml":
                    guiButtons.get(level).setActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        eventHandler.respondToLevel20();
                    }
                });
                    break;

            }
        }

        // KEY LISTENER - LET'S US PROVIDE CUSTOM RESPONSES
        this.setKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent ke) {
                eventHandler.respondToKeyPress(ke.getKeyCode());
            }
        });
    }

    /**
     * Invoked when a new game is started, it resets all relevant game data and
     * gui control states.
     */
    @Override
    public void reset() {
        //audio.stop(pathXPropertyType.AUDIO_CUE_WIN.toString());
        data.reset(this);
    }

    /**
     * Updates the state of all gui controls according to the current game
     * conditions.
     */
    @Override
    public void updateGUI() {
        // GO THROUGH THE VISIBLE BUTTONS TO TRIGGER MOUSE OVERS
        Iterator<Sprite> buttonsIt = guiButtons.values().iterator();
        while (buttonsIt.hasNext()) {
            Sprite button = buttonsIt.next();

            // ARE WE ENTERING A BUTTON?
            if (button.getState().equals(PathXTileState.VISIBLE_STATE.toString())) {
                if (button.containsPoint(data.getLastMouseX(), data.getLastMouseY())) {
                    button.setState(PathXTileState.MOUSE_OVER_STATE.toString());
                }
            } // ARE WE EXITING A BUTTON?
            else if (button.getState().equals(PathXTileState.MOUSE_OVER_STATE.toString())) {
                if (!button.containsPoint(data.getLastMouseX(), data.getLastMouseY())) {
                    button.setState(PathXTileState.VISIBLE_STATE.toString());
                }
            }
        }
    }

}
