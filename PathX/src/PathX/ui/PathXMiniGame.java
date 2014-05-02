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
import PathX.file.PathXFileManager;
import PathX.data.PathXRecord;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Random;
import javax.swing.JScrollPane;

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
    // ACCESSOR METHODS
    // - getPlayerRecord
    // - getErrorHandler
    // - getFileManager
    // - isCurrentScreenState
    /**
     * Accessor method for getting the player record object, which summarizes
     * the player's record on all levels.
     *
     * @return The player's complete record.
     */
    public PathXRecord getPlayerRecord() {
        return record;
    }

    /**
     * Accessor method for getting the application's error handler.
     *
     * @return The error handler.
     */
    public PathXErrorHandler getErrorHandler() {
        return errorHandler;
    }

    /**
     * Accessor method for getting the app's file manager.
     *
     * @return The file manager.
     */
    public PathXFileManager getFileManager() {
        return fileManager;
    }

    /**
     * Used for testing to see if the current screen state matches the
     * testScreenState argument. If it mates, true is returned, else false.
     *
     * @param testScreenState Screen state to test against the current state.
     *
     * @return true if the current state is testScreenState, false otherwise.
     */
    public boolean isCurrentScreenState(String testScreenState) {
        return testScreenState.equals(currentScreenState);
    }

    // VIEWPORT UPDATE METHODS
    // - initViewport
    // - scroll
    // - moveViewport
    // SERVICE METHODS
    // - displayStats
    // - savePlayerRecord
    // - switchToGameScreen
    // - switchToSplashScreen
    // - updateBoundaries
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
     * This method switches the application to the game screen, making all the
     * appropriate UI controls visible & invisible.
     */
    public void switchToGameScreen() {
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
        
        //guiButtons.get(LEVEL_GAME_TYPE).setState(PathXTileState.VISIBLE_STATE.toString());
        //guiButtons.get(LEVEL_GAME_TYPE).setEnabled(true);

        // DEACTIVATE THE LEVEL SELECT BUTTONS
        ArrayList<String> levels = props.getPropertyOptionsList(pathXPropertyType.LEVEL_OPTIONS);
        for (String level : levels) {
            guiButtons.get(level).setState(PathXTileState.INVISIBLE_STATE.toString());
            guiButtons.get(level).setEnabled(false);
        }
        
        ArrayList<String> gameLevels = props.getPropertyOptionsList(pathXPropertyType.GAME_LEVELS);
        for (String level : gameLevels) {
            guiButtons.get(level).setState(PathXTileState.VISIBLE_STATE.toString());
            guiButtons.get(level).setEnabled(true);
        }

        // AND CHANGE THE SCREEN STATE
        currentScreenState = GAME_SCREEN_STATE;
        
        insideCanvas.setVisible(true);
        canvas.setLayout(null);
        insideCanvas.setBounds(17, 129, 1247, 551);
        insideCanvas.setRenderedBackground(BACKGROUND_GAME_TYPE);
        
        
        // PLAY THE GAMEPLAY SCREEN SONG
        //audio.stop(pathXPropertyType.SONG_CUE_MENU_SCREEN.toString()); 
        //audio.play(pathXPropertyType.SONG_CUE_GAME_SCREEN.toString(), true); 
    }
    public void switchToSettingScreen(){
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        guiDecor.get(BACKGROUND_TYPE).setState(SETTINGS_SCREEN_STATE);
        
         // ACTIVATE THE TOOLBAR AND ITS CONTROLS
        guiButtons.get(NEW_GAME_BUTTON_TYPE).setState(PathXTileState.VISIBLE_STATE.toString());
        guiButtons.get(NEW_GAME_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(BACK_BUTTON_TYPE).setState(PathXTileState.VISIBLE_STATE.toString());
        guiButtons.get(BACK_BUTTON_TYPE).setEnabled(true);

        // DEACTIVATE THE LEVEL SELECT BUTTONS
        ArrayList<String> levels = props.getPropertyOptionsList(pathXPropertyType.LEVEL_OPTIONS);
        for (String level : levels) {
            guiButtons.get(level).setState(PathXTileState.INVISIBLE_STATE.toString());
            guiButtons.get(level).setEnabled(false);
        }
        
        currentScreenState = SETTINGS_SCREEN_STATE;
    }
    public void switchToHelpScreen(){
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        guiDecor.get(BACKGROUND_TYPE).setState(HELP_SCREEN_STATE);
        
         // ACTIVATE THE TOOLBAR AND ITS CONTROLS
        guiButtons.get(NEW_GAME_BUTTON_TYPE).setState(PathXTileState.VISIBLE_STATE.toString());
        guiButtons.get(NEW_GAME_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(BACK_BUTTON_TYPE).setState(PathXTileState.VISIBLE_STATE.toString());
        guiButtons.get(BACK_BUTTON_TYPE).setEnabled(true);
        // DEACTIVATE THE LEVEL SELECT BUTTONS
        ArrayList<String> levels = props.getPropertyOptionsList(pathXPropertyType.LEVEL_OPTIONS);
        for (String level : levels) {
            guiButtons.get(level).setState(PathXTileState.INVISIBLE_STATE.toString());
            guiButtons.get(level).setEnabled(false);
        }
        
        currentScreenState = HELP_SCREEN_STATE;
    }
    /**
     * This method switches the application to the menu screen, making all the
     * appropriate UI controls visible & invisible.
     */
    public void switchToSplashScreen() {
        // CHANGE THE BACKGROUND
        guiDecor.get(BACKGROUND_TYPE).setState(MENU_SCREEN_STATE);

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
        
        //guiButtons.get(LEVEL_GAME_TYPE).setState(PathXTileState.INVISIBLE_STATE.toString());
        //guiButtons.get(LEVEL_GAME_TYPE).setEnabled(false);

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
        ((PathXDataModel) data).enableTiles(false);

        // MAKE THE CURRENT SCREEN THE MENU SCREEN
        currentScreenState = MENU_SCREEN_STATE;

        // AND UPDATE THE DATA GAME STATE
        data.setGameState(MiniGameState.NOT_STARTED);

        // PLAY THE WELCOME SCREEN SONG
        //audio.stop(pathXPropertyType.AUDIO_CUE_WIN.toString());
        //audio.play(pathXPropertyType.SONG_CUE_MENU_SCREEN.toString(), true); 
        ///audio.stop(pathXPropertyType.SONG_CUE_GAME_SCREEN.toString());
    }
    
    public void switchToGameLevel() {
        // CHANGE THE BACKGROUND
        guiDecor.get(BACKGROUND_TYPE).setState(LEVEL_SCREEN_STATE);

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
        
       // guiButtons.get(LEVEL_GAME_TYPE).setState(PathXTileState.INVISIBLE_STATE.toString());
       // guiButtons.get(LEVEL_GAME_TYPE).setEnabled(false);

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
        ((PathXDataModel) data).enableTiles(false);

        // MAKE THE CURRENT SCREEN THE MENU SCREEN
        currentScreenState = MENU_SCREEN_STATE;

        // AND UPDATE THE DATA GAME STATE
        data.setGameState(MiniGameState.NOT_STARTED);
        insideCanvas.setBounds(274, 19, 978, 667);
        insideCanvas.setRenderedBackground(SBU_GAME_TYPE);
        
        
        // PLAY THE WELCOME SCREEN SONG
        //audio.stop(pathXPropertyType.AUDIO_CUE_WIN.toString());
        //audio.play(pathXPropertyType.SONG_CUE_MENU_SCREEN.toString(), true); 
        ///audio.stop(pathXPropertyType.SONG_CUE_GAME_SCREEN.toString());
    }

    // METHODS OVERRIDDEN FROM MiniGame
    // - initAudioContent
    // - initData
    // - initGUIControls
    // - initGUIHandlers
    // - reset
    // - updateGUI
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

            // PLAY THE WELCOME SCREEN SONG
            //audio.play(pathXPropertyType.SONG_CUE_MENU_SCREEN.toString(), true);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException | InvalidMidiDataException | MidiUnavailableException e) {
            errorHandler.processError(pathXPropertyType.TEXT_ERROR_LOADING_AUDIO);
        }
    }

    /**
     * This helper method loads the audio file associated with audioCueType,
     * which should have been specified via an XML properties file.
     */
    private void loadAudioCue(pathXPropertyType audioCueType)
            throws UnsupportedAudioFileException, IOException, LineUnavailableException,
            InvalidMidiDataException, MidiUnavailableException {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String audioPath = props.getProperty(pathXPropertyType.PATH_AUDIO);
        String cue = props.getProperty(audioCueType.toString());
        audio.loadAudio(audioCueType.toString(), audioPath + cue);
    }

    /**
     * Initializes the game data used by the application. Note that it is this
     * method's obligation to construct and set this Game's custom GameDataModel
     * object as well as any other needed game objects.
     */
    @Override
    public void initData() {
        // INIT OUR ERROR HANDLER
        errorHandler = new PathXErrorHandler(window);

        // INIT OUR FILE MANAGER
        fileManager = new PathXFileManager(this);

        // LOAD THE PLAYER'S RECORD FROM A FILE
        record = fileManager.loadRecord();

        // INIT OUR DATA MANAGER
        data = new PathXDataModel(this);
        dataCopy = (PathXDataModel) data;
    }

    public static PathXDataModel getData() {
        return dataCopy;
    }
    
    public PathXGamePanel getInsideCanvas(){
        return insideCanvas;
    }

    /**
     * Initializes the game controls, like buttons, used by the game
     * application. Note that this includes the tiles, which serve as buttons of
     * sorts.
     */
    @Override
    public void initGUIControls() {
        // WE'LL USE AND REUSE THESE FOR LOADING STUFF
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
        
        insideCanvas = new PathXGamePanel(this);
        canvas.add(insideCanvas);
        insideCanvas.setVisible(false);
        
        
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
        
        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_BACKGROUND_SBU)); // Level SBU
        sT.addState(SBU_SCREEN_STATE, img);
        s = new Sprite(sT, 0, 0, 0, 0, SBU_SCREEN_STATE);
        guiDecor.put(SBU_GAME_TYPE, s);
        
        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_BACKGROUND_GAME_MENU)); // Level MENU
        sT.addState(LEVEL_SCREEN_STATE, img);
        s = new Sprite(sT, 0, 0, 0, 0, GAMES_SCREEN_STATE);
        guiDecor.put(LEVEL_GAME_TYPE, s);

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
        int x_add = 0;
        int y_add = 0;
        for (int i = 0; i < levels.size(); i++) {
            if(i == 0){
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
            s = new Sprite(sT, 1060 - x_add, 315 - y_add, 0, 0, PathXTileState.INVISIBLE_STATE.toString());
            guiButtons.put(levels.get(i), s);
        }

        // ADD THE CONTROLS ALONG THE GAME SCREEN
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

        // THEN THE TILES STACKED TO THE TOP LEFT
        ((PathXDataModel) data).initTiles();

    }

    /**
     * Initializes the game event handlers for things like game gui buttons.
     */
    @Override
    public void initGUIHandlers() {
        // WE'LL RELAY UI EVENTS TO THIS OBJECT FOR HANDLING
        eventHandler = new PathXEventHandler(this);

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
            if (levelFile.equalsIgnoreCase("./pathX/SelectionSnake.sort")) {
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
        //guiButtons.get(LEVEL_GAME_TYPE).setActionListener(new ActionListener() {
          //  public void actionPerformed(ActionEvent ae) {
           //     eventHandler.respondToGameRequest();
         //   }
        //});


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
