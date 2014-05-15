package PathX;

import PathX.ui.PathXMiniGame;
import PathX.ui.PathXErrorHandler;
import xml_utilities.InvalidXMLFileFormatException;
import properties_manager.PropertiesManager;
import static PathX.PathXConstants.*;

/**
 * The Sorting Hat is a game application that's ready to be customized
 * to play different flavors of the game. It has been setup using art
 * from Harry Potter, but it could easily be swapped out just by changing
 * the artwork and audio files.
 * 
 * @author Richard McKenna 
 */
public class PathX
{
    // THIS HAS THE FULL USER INTERFACE AND ONCE IN EVENT
    // HANDLING MODE, BASICALLY IT BECOMES THE FOCAL
    // POINT, RUNNING THE UI AND EVERYTHING ELSE
    static PathXMiniGame miniGame = new PathXMiniGame();

    /**
     * This is where The Sorting Hat game application starts execution. We'll
     * load the application properties and then use them to build our
     * user interface and start the window in real-time mode.
     */
    public static void main(String[] args)
    {
        try
        {
            // LOAD THE SETTINGS FOR STARTING THE APP
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            props.addProperty(PropertiesManager.DATA_PATH_PROPERTY, PATH_DATA);
            props.loadProperties(PROPERTIES_FILE_NAME, PROPERTIES_SCHEMA_FILE_NAME);
            
            // THEN WE'LL LOAD THE GAME FLAVOR AS SPECIFIED BY THE PROPERTIES FILE
            String gameFlavorFile = props.getProperty(pathXPropertyType.FILE_GAME_PROPERTIES);
            props.loadProperties(gameFlavorFile, PROPERTIES_SCHEMA_FILE_NAME);

            // NOW WE CAN LOAD THE UI, WHICH WILL USE ALL THE FLAVORED CONTENT
            String appTitle = props.getProperty(pathXPropertyType.TEXT_TITLE_BAR_GAME);
            miniGame.initMiniGame(appTitle, FPS, WINDOW_WIDTH, WINDOW_HEIGHT);
            
            // GET THE PROPER WINDOW DIMENSIONS
            miniGame.startGame();
        }
        // THERE WAS A PROBLEM LOADING THE PROPERTIES FILE
        catch(InvalidXMLFileFormatException ixmlffe)
        {
            // LET THE ERROR HANDLER PROVIDE THE RESPONSE
            PathXErrorHandler errorHandler = miniGame.getErrorHandler();
            errorHandler.processError(pathXPropertyType.TEXT_ERROR_LOADING_XML_FILE);
        }
    }
    
    /**
     * pathXPropertyType represents the types of data that will need
     * to be extracted from XML files.
     */
    public enum pathXPropertyType
    {
        // LOADED FROM properties.xml
        
        /* SETUP FILE NAMES */
        FILE_GAME_PROPERTIES,
        FILE_PLAYER_RECORD,

        /* DIRECTORY PATHS FOR FILE LOADING */
        PATH_AUDIO,
        PATH_IMG,
        
        // LOADED FROM THE GAME FLAVOR PROPERTIES XML FILE
            // sorting_hat_properties.xml
                
        /* IMAGE FILE NAMES */
        IMAGE_PLAYER,
        IMAGE_POLICE,
        IMAGE_ZOMBIE,
        IMAGE_BANDIT,
        IMAGE_BACKGROUND_GAME,
        IMAGE_BACKGROUND_GAME_MENU,
        IMAGE_BACKGROUND_GAMEMAP,
        IMAGE_BACKGROUND_LEVEL1,
        IMAGE_BACKGROUND_LEVEL2,
        IMAGE_BACKGROUND_LEVEL3,
        IMAGE_BACKGROUND_LEVEL4,
        IMAGE_BACKGROUND_LEVEL5,
        IMAGE_BACKGROUND_LEVEL6,
        IMAGE_BACKGROUND_LEVEL7,
        IMAGE_BACKGROUND_LEVEL8,
        IMAGE_BACKGROUND_LEVEL9,
        IMAGE_BACKGROUND_LEVEL10,
        IMAGE_BACKGROUND_LEVEL11,
        IMAGE_BACKGROUND_LEVEL12,
        IMAGE_BACKGROUND_LEVEL13,
        IMAGE_BACKGROUND_LEVEL14,
        IMAGE_BACKGROUND_LEVEL15,
        IMAGE_BACKGROUND_LEVEL16,
        IMAGE_BACKGROUND_LEVEL17,
        IMAGE_BACKGROUND_LEVEL18,
        IMAGE_BACKGROUND_LEVEL19,
        IMAGE_BACKGROUND_LEVEL20,
        IMAGE_LEVEL_UNROBBED,
        IMAGE_BUTTON_LEVEL_MOUSE_OVER,
        IMAGE_BACKGROUND_MENU,
        IMAGE_BACKGROUND_HELP,
        IMAGE_BACKGROUND_SETTINGS,
        IMAGE_BUTTON_NEW,
        IMAGE_BUTTON_NEW_MOUSE_OVER,
        IMAGE_BUTTON_BACK,
        IMAGE_BUTTON_BACK_MOUSE_OVER,
        IMAGE_BACK_TO_LEVEL_SELECT,
        IMAGE_BACK_TO_LEVEL_SELECT_MOUSE_OVER,
        IMAGE_PAUSE,
        IMAGE_PAUSE_MOUSE_OVER,
        IMAGE_BUTTON_UP,
        IMAGE_BUTTON_UP_MOUSE_OVER,
        IMAGE_BUTTON_DOWN,
        IMAGE_BUTTON_DOWN_MOUSE_OVER,
        IMAGE_BUTTON_LEFT,
        IMAGE_BUTTON_LEFT_MOUSE_OVER,
        IMAGE_BUTTON_RIGHT,
        IMAGE_BUTTON_RIGHT_MOUSE_OVER,
        IMAGE_BUTTON_STATS,
        IMAGE_BUTTON_STATS_MOUSE_OVER,
        IMAGE_BUTTON_UNDO,
        IMAGE_BUTTON_UNDO_MOUSE_OVER,
        IMAGE_BUTTON_TEMP_TILE,
        IMAGE_BUTTON_TEMP_TILE_MOUSE_OVER,
        IMAGE_CURSOR_WAND,
        IMAGE_DECOR_TIME,      
        IMAGE_DECOR_MISCASTS,
        IMAGE_DIALOG_STATS,
        IMAGE_DIALOG_WIN,
        IMAGE_SPRITE_SHEET_CHARACTER_TILES,        
        IMAGE_TILE_BACKGROUND,
        IMAGE_TILE_BACKGROUND_SELECTED,
        IMAGE_TILE_BACKGROUND_MOUSE_OVER,
        IMAGE_WINDOW_ICON,
        
        /* GAME TEXT */
        TEXT_ERROR_LOADING_AUDIO,
        TEXT_ERROR_LOADING_LEVEL,
        TEXT_ERROR_LOADING_RECORD,
        TEXT_ERROR_LOADING_XML_FILE,
        TEXT_ERROR_SAVING_RECORD,
        TEXT_LABEL_STATS_ALGORITHM,
        TEXT_LABEL_STATS_GAMES,
        TEXT_LABEL_STATS_WINS,
        TEXT_LABEL_STATS_PERFECT_WINS,
        TEXT_LABEL_STATS_FASTEST_PERFECT_WIN,
        TEXT_PROMPT_EXIT,
        TEXT_TITLE_BAR_GAME,
        TEXT_TITLE_BAR_ERROR,
        
        /* AUDIO CUES */
        AUDIO_CUE_BAD_MOVE,
        AUDIO_CUE_CHEAT,
        AUDIO_CUE_DESELECT_TILE,
        AUDIO_CUE_GOOD_MOVE,
        AUDIO_CUE_SELECT_TILE,
        AUDIO_CUE_UNDO,
        AUDIO_CUE_WIN,
        SONG_CUE_GAME_SCREEN,
        SONG_CUE_MENU_SCREEN,
        
        /* TILE LOADING STUFF */
        LEVEL_OPTIONS,
        LEVEL_IMAGE_OPTIONS,
        LEVEL_MOUSE_OVER_IMAGE_OPTIONS,
        GAME_LEVELS,
        GAME_LEVEL_IMAGE_OPTIONS,
        GAME_LEVEL_MOUSE_OVER_IMAGE_OPTIONS,
        GAME_LEVEL_LOCKED,
        GAME_LEVEL_ROBBED
    }
}