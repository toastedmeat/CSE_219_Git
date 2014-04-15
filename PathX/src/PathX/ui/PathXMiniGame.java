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
import PathX.PathX.SortingHatPropertyType;
import PathX.file.PathXFileManager;
import PathX.data.PathXRecord;

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
        guiButtons.get(BACK_BUTTON_TYPE).setState(PathXTileState.VISIBLE_STATE.toString());
        guiButtons.get(BACK_BUTTON_TYPE).setEnabled(true);
        guiDecor.get(MISCASTS_COUNT_TYPE).setState(PathXTileState.VISIBLE_STATE.toString());
        guiDecor.get(TIME_TYPE).setState(PathXTileState.VISIBLE_STATE.toString());
        guiButtons.get(STATS_BUTTON_TYPE).setState(PathXTileState.VISIBLE_STATE.toString());
        guiButtons.get(STATS_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(UNDO_BUTTON_TYPE).setState(PathXTileState.VISIBLE_STATE.toString());
        guiButtons.get(UNDO_BUTTON_TYPE).setEnabled(true);
        guiDecor.get(ALGORITHM_TYPE).setState(PathXTileState.VISIBLE_STATE.toString());

        // DEACTIVATE THE LEVEL SELECT BUTTONS
        ArrayList<String> levels = props.getPropertyOptionsList(SortingHatPropertyType.LEVEL_OPTIONS);
        for (String level : levels) {
            guiButtons.get(level).setState(PathXTileState.INVISIBLE_STATE.toString());
            guiButtons.get(level).setEnabled(false);
        }

        // AND CHANGE THE SCREEN STATE
        currentScreenState = GAME_SCREEN_STATE;

        // PLAY THE GAMEPLAY SCREEN SONG
        //audio.stop(SortingHatPropertyType.SONG_CUE_MENU_SCREEN.toString()); 
        //audio.play(SortingHatPropertyType.SONG_CUE_GAME_SCREEN.toString(), true); 
    }
    public void switchToSettingScreen(){
        guiDecor.get(BACKGROUND_TYPE).setState(SETTINGS_SCREEN_STATE);
        currentScreenState = SETTINGS_SCREEN_STATE;
    }
    public void switchToHelpScreen(){
        guiDecor.get(BACKGROUND_TYPE).setState(HELP_SCREEN_STATE);
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
        guiDecor.get(MISCASTS_COUNT_TYPE).setState(PathXTileState.INVISIBLE_STATE.toString());
        guiDecor.get(TIME_TYPE).setState(PathXTileState.INVISIBLE_STATE.toString());
        guiButtons.get(STATS_BUTTON_TYPE).setState(PathXTileState.INVISIBLE_STATE.toString());
        guiButtons.get(STATS_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(UNDO_BUTTON_TYPE).setState(PathXTileState.INVISIBLE_STATE.toString());
        guiButtons.get(UNDO_BUTTON_TYPE).setEnabled(false);
        guiDecor.get(ALGORITHM_TYPE).setState(PathXTileState.INVISIBLE_STATE.toString());

        // ACTIVATE THE LEVEL SELECT BUTTONS
        // DEACTIVATE THE LEVEL SELECT BUTTONS
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        ArrayList<String> levels = props.getPropertyOptionsList(SortingHatPropertyType.LEVEL_OPTIONS);
        for (String level : levels) {
            guiButtons.get(level).setState(PathXTileState.VISIBLE_STATE.toString());
            guiButtons.get(level).setEnabled(true);
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
        //audio.stop(SortingHatPropertyType.AUDIO_CUE_WIN.toString());
        //audio.play(SortingHatPropertyType.SONG_CUE_MENU_SCREEN.toString(), true); 
        ///audio.stop(SortingHatPropertyType.SONG_CUE_GAME_SCREEN.toString());
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
            String audioPath = props.getProperty(SortingHatPropertyType.PATH_AUDIO);

            //LOAD ALL THE AUDIO
            loadAudioCue(SortingHatPropertyType.AUDIO_CUE_SELECT_TILE);
            loadAudioCue(SortingHatPropertyType.AUDIO_CUE_DESELECT_TILE);
            loadAudioCue(SortingHatPropertyType.AUDIO_CUE_GOOD_MOVE);
            loadAudioCue(SortingHatPropertyType.AUDIO_CUE_BAD_MOVE);
            loadAudioCue(SortingHatPropertyType.AUDIO_CUE_CHEAT);
            loadAudioCue(SortingHatPropertyType.AUDIO_CUE_UNDO);
            loadAudioCue(SortingHatPropertyType.AUDIO_CUE_WIN);
            loadAudioCue(SortingHatPropertyType.SONG_CUE_MENU_SCREEN);
            loadAudioCue(SortingHatPropertyType.SONG_CUE_GAME_SCREEN);

            // PLAY THE WELCOME SCREEN SONG
            //audio.play(SortingHatPropertyType.SONG_CUE_MENU_SCREEN.toString(), true);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException | InvalidMidiDataException | MidiUnavailableException e) {
            errorHandler.processError(SortingHatPropertyType.TEXT_ERROR_LOADING_AUDIO);
        }
    }

    /**
     * This helper method loads the audio file associated with audioCueType,
     * which should have been specified via an XML properties file.
     */
    private void loadAudioCue(SortingHatPropertyType audioCueType)
            throws UnsupportedAudioFileException, IOException, LineUnavailableException,
            InvalidMidiDataException, MidiUnavailableException {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String audioPath = props.getProperty(SortingHatPropertyType.PATH_AUDIO);
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
        String imgPath = props.getProperty(SortingHatPropertyType.PATH_IMG);
        String windowIconFile = props.getProperty(SortingHatPropertyType.IMAGE_WINDOW_ICON);
        img = loadImage(imgPath + windowIconFile);
        window.setIconImage(img);

        // CONSTRUCT THE PANEL WHERE WE'LL DRAW EVERYTHING
        canvas = new PathXPanel(this, (PathXDataModel) data);

        // LOAD THE BACKGROUNDS, WHICH ARE GUI DECOR
        currentScreenState = MENU_SCREEN_STATE;
        img = loadImage(imgPath + props.getProperty(SortingHatPropertyType.IMAGE_BACKGROUND_MENU));
        sT = new SpriteType(BACKGROUND_TYPE);
        sT.addState(MENU_SCREEN_STATE, img);
        img = loadImage(imgPath + props.getProperty(SortingHatPropertyType.IMAGE_BACKGROUND_GAME)); // Level BaCKGROUND
        sT.addState(GAME_SCREEN_STATE, img);
        s = new Sprite(sT, 0, 0, 0, 0, MENU_SCREEN_STATE);
        guiDecor.put(BACKGROUND_TYPE, s);

        // LOAD THE WAND CURSOR
        String cursorName = props.getProperty(SortingHatPropertyType.IMAGE_CURSOR_WAND);
        img = loadImageWithColorKey(imgPath + cursorName, COLOR_KEY);
        Point cursorHotSpot = new Point(0, 0);
        Cursor wandCursor = Toolkit.getDefaultToolkit().createCustomCursor(img, cursorHotSpot, cursorName);
        window.setCursor(wandCursor);

        // ADD A BUTTON FOR EACH LEVEL AVAILABLE
        ArrayList<String> levels = props.getPropertyOptionsList(SortingHatPropertyType.LEVEL_OPTIONS);
        ArrayList<String> levelImageNames = props.getPropertyOptionsList(SortingHatPropertyType.LEVEL_IMAGE_OPTIONS);
        ArrayList<String> levelMouseOverImageNames = props.getPropertyOptionsList(SortingHatPropertyType.LEVEL_MOUSE_OVER_IMAGE_OPTIONS);
        float totalWidth = levels.size() * (LEVEL_BUTTON_WIDTH + LEVEL_BUTTON_MARGIN) - LEVEL_BUTTON_MARGIN;
        Viewport viewport = data.getViewport();
        x = (viewport.getScreenWidth() - totalWidth) / 2.0f;
        for (int i = 0; i < levels.size(); i++) {
            sT = new SpriteType(LEVEL_SELECT_BUTTON_TYPE);
            img = loadImageWithColorKey(imgPath + levelImageNames.get(i), COLOR_KEY);
            sT.addState(PathXTileState.VISIBLE_STATE.toString(), img);
            img = loadImageWithColorKey(imgPath + levelMouseOverImageNames.get(i), COLOR_KEY);
            sT.addState(PathXTileState.MOUSE_OVER_STATE.toString(), img);
            s = new Sprite(sT, x, LEVEL_BUTTON_Y, 0, 0, PathXTileState.VISIBLE_STATE.toString());
            guiButtons.put(levels.get(i), s);
            x += LEVEL_BUTTON_WIDTH + LEVEL_BUTTON_MARGIN;
        }

        // ADD THE CONTROLS ALONG THE NORTH OF THE GAME SCREEN
        // THEN THE NEW BUTTON
        String newButton = props.getProperty(SortingHatPropertyType.IMAGE_BUTTON_NEW);
        sT = new SpriteType(NEW_GAME_BUTTON_TYPE);
        img = loadImage(imgPath + newButton);
        sT.addState(PathXTileState.VISIBLE_STATE.toString(), img);
        String newMouseOverButton = props.getProperty(SortingHatPropertyType.IMAGE_BUTTON_NEW_MOUSE_OVER);
        img = loadImage(imgPath + newMouseOverButton);
        sT.addState(PathXTileState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, NEW_BUTTON_X, NEW_BUTTON_Y, 0, 0, PathXTileState.INVISIBLE_STATE.toString());
        guiButtons.put(NEW_GAME_BUTTON_TYPE, s);

        // THEN THE BACK BUTTON
        String backButton = props.getProperty(SortingHatPropertyType.IMAGE_BUTTON_BACK);
        sT = new SpriteType(BACK_BUTTON_TYPE);
        img = loadImage(imgPath + backButton);
        sT.addState(PathXTileState.VISIBLE_STATE.toString(), img);
        String backMouseOverButton = props.getProperty(SortingHatPropertyType.IMAGE_BUTTON_BACK_MOUSE_OVER);
        img = loadImage(imgPath + backMouseOverButton);
        sT.addState(PathXTileState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, BACK_BUTTON_X, BACK_BUTTON_Y, 0, 0, PathXTileState.INVISIBLE_STATE.toString());
        guiButtons.put(BACK_BUTTON_TYPE, s);

        // AND THE MISCASTS COUNT
        String miscastCountContainer = props.getProperty(SortingHatPropertyType.IMAGE_DECOR_MISCASTS);
        sT = new SpriteType(MISCASTS_COUNT_TYPE);
        img = loadImage(imgPath + miscastCountContainer);
        sT.addState(PathXTileState.VISIBLE_STATE.toString(), img);
        s = new Sprite(sT, TILE_COUNT_X, TILE_COUNT_Y, 0, 0, PathXTileState.INVISIBLE_STATE.toString());
        guiDecor.put(MISCASTS_COUNT_TYPE, s);

        // AND THE TIME DISPLAY
        String timeContainer = props.getProperty(SortingHatPropertyType.IMAGE_DECOR_TIME);
        sT = new SpriteType(TIME_TYPE);
        img = loadImage(imgPath + timeContainer);
        sT.addState(PathXTileState.VISIBLE_STATE.toString(), img);
        s = new Sprite(sT, TIME_X, TIME_Y, 0, 0, PathXTileState.INVISIBLE_STATE.toString());
        guiDecor.put(TIME_TYPE, s);

        // AND THE STATS BUTTON
        String statsButton = props.getProperty(SortingHatPropertyType.IMAGE_BUTTON_STATS);
        sT = new SpriteType(STATS_BUTTON_TYPE);
        img = loadImage(imgPath + statsButton);
        sT.addState(PathXTileState.VISIBLE_STATE.toString(), img);
        String statsMouseOverButton = props.getProperty(SortingHatPropertyType.IMAGE_BUTTON_STATS_MOUSE_OVER);
        img = loadImage(imgPath + statsMouseOverButton);
        sT.addState(PathXTileState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, STATS_X, STATS_Y, 0, 0, PathXTileState.INVISIBLE_STATE.toString());
        guiButtons.put(STATS_BUTTON_TYPE, s);

        // AND THE STATS BUTTON
        String undoButton = props.getProperty(SortingHatPropertyType.IMAGE_BUTTON_UNDO);
        sT = new SpriteType(UNDO_BUTTON_TYPE);
        img = loadImage(imgPath + undoButton);
        sT.addState(PathXTileState.VISIBLE_STATE.toString(), img);
        String undoMouseOverButton = props.getProperty(SortingHatPropertyType.IMAGE_BUTTON_UNDO_MOUSE_OVER);
        img = loadImage(imgPath + undoMouseOverButton);
        sT.addState(PathXTileState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, UNDO_BUTTON_X, UNDO_BUTTON_Y, 0, 0, PathXTileState.INVISIBLE_STATE.toString());
        guiButtons.put(UNDO_BUTTON_TYPE, s);

        // AND THE TILE STACK
        String tileStack = props.getProperty(SortingHatPropertyType.IMAGE_BUTTON_TEMP_TILE);
        sT = new SpriteType(ALGORITHM_TYPE);
        img = loadImageWithColorKey(imgPath + tileStack, COLOR_KEY);
        sT.addState(PathXTileState.VISIBLE_STATE.toString(), img);
        s = new Sprite(sT, TEMP_TILE_X, TEMP_TILE_Y, 0, 0, PathXTileState.INVISIBLE_STATE.toString());
        guiDecor.put(ALGORITHM_TYPE, s);

        // NOW ADD THE DIALOGS
        // AND THE STATS DISPLAY
        String statsDialog = props.getProperty(SortingHatPropertyType.IMAGE_DIALOG_STATS);
        sT = new SpriteType(STATS_DIALOG_TYPE);
        img = loadImageWithColorKey(imgPath + statsDialog, COLOR_KEY);
        sT.addState(PathXTileState.VISIBLE_STATE.toString(), img);
        x = (viewport.getScreenWidth() / 2) - (img.getWidth(null) / 2);
        y = (viewport.getScreenHeight() / 2) - (img.getHeight(null) / 2);
        s = new Sprite(sT, x, y, 0, 0, PathXTileState.INVISIBLE_STATE.toString());
        guiDialogs.put(STATS_DIALOG_TYPE, s);

        // AND THE WIN CONDITION DISPLAY
        String winDisplay = props.getProperty(SortingHatPropertyType.IMAGE_DIALOG_WIN);
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
        ArrayList<String> levels = props.getPropertyOptionsList(SortingHatPropertyType.LEVEL_OPTIONS);
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
            } else if (levelFile.equalsIgnoreCase("./pathX/BubbleSortCoil.sort")) {
            } else if (levelFile.equalsIgnoreCase("./pathX/SelectionL.sort")) {
            } else if (levelFile.equalsIgnoreCase("./pathX/BubbleSortO.sort")) {
            }
        }

        // NEW GAME EVENT HANDLER
        guiButtons.get(NEW_GAME_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                //audio.play(SortingHatPropertyType.SONG_CUE_GAME_SCREEN.toString(), true);    
                eventHandler.respondToNewGameRequest();
            }
        });

        guiButtons.get(BACK_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToBackRequest();
            }
        });

        // STATS BUTTON EVENT HANDLER
        guiButtons.get(STATS_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToDisplayStatsRequest();
            }
        });

        guiButtons.get(UNDO_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToUndoRequest();
            }
        });

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
        //audio.stop(SortingHatPropertyType.AUDIO_CUE_WIN.toString());
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
