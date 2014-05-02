package pathX.level_editor;

import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.border.Border;

/**
 * This class provides all the constants to be used by the pathX level editor
 * application, including those for sizing and positioning the UI as well as
 * any text or other settings used by the application.
 * 
 * @author Richard McKenna
 */
public class PXLE_Constants
{
    // LEVEL EDITOR PATHS
    public static final String  DATA_PATH               = "data/";
    public static final String  BUTTONS_PATH            = DATA_PATH + "buttons/";
    public static final String  CURSORS_PATH            = DATA_PATH + "cursors/";
    public static final String  LEVELS_PATH             = DATA_PATH + "levels/";

    // BUTTON IMAGES
    public static final String  NEW_BUTTON_FILE     = BUTTONS_PATH + "New.png";
    public static final String  OPEN_BUTTON_FILE    = BUTTONS_PATH + "Open.png";
    public static final String  SAVE_BUTTON_FILE    = BUTTONS_PATH + "Save.png";
    public static final String  SAVE_AS_BUTTON_FILE = BUTTONS_PATH + "SaveAs.png";
    public static final String  EXIT_BUTTON_FILE    = BUTTONS_PATH + "Exit.png";
    public static final String  BG_BUTTON_FILE      = BUTTONS_PATH + "ChangeBackground.png";
    public static final String  START_BUTTON_FILE   = BUTTONS_PATH + "SelectStartLocation.png";
    public static final String  DEST_BUTTON_FILE    = BUTTONS_PATH + "SelectDestination.png";
    public static final String  MONEY_LABEL_FILE   = BUTTONS_PATH + "Money.png";
    public static final String  POLICE_LABEL_FILE   = BUTTONS_PATH + "Police.png";
    public static final String  BANDITS_LABEL_FILE  = BUTTONS_PATH + "Bandits.png";
    public static final String  ZOMBIES_LABEL_FILE  = BUTTONS_PATH + "Zombies.png";
        
    // BUTTON MOUSE OVER TEXT/ACTION COMMANDS
    public static final String NEW_COMMAND          = "New Level";
    public static final String OPEN_COMMAND         = "Open Level";
    public static final String SAVE_COMMAND         = "Save Level";
    public static final String SAVE_AS_COMMAND      = "Save As Level";
    public static final String EXIT_COMMAND         = "Exit";
    public static final String BG_IMAGE_COMMAND     = "Select Background Image";
    public static final String START_IMAGE_COMMAND  = "Select Start Image";
    public static final String DEST_IMAGE_COMMAND   = "Selecct Destination Image"; 
    public static final String MONEY_COMMAND        = "Update $ Value of Level";
    public static final String POLICE_COMMAND       = "Inc/Dec Number of Police";
    public static final String BANDITS_COMMAND      = "Inc/Dec Number of Bandits";
    public static final String ZOMBIES_COMMAND      = "Inc/Dec Number of Zombies";
    
    // CURSOR FILES
    public static final String DEFAULT_CURSOR_IMG   = "DefaultCursor.png";
    public static final String ADD_INT_CURSOR_IMG   = "AddIntersectionCursor.png";
    public static final String ADD_ROAD_CURSOR_IMG  = "AddRoadCursor.png";
    
    // DEFAULT IMAGE FILES
    public static final String DEFAULT_BG_IMG       = "DeathValleyBackground.png";
    public static final String DEFAULT_START_IMG    = "DefaultStartLocation.png";
    public static final String DEFAULT_DEST_IMG     = "DefaultDestination.png";
        
    /***** DIALOG MESSAGES AND TITLES *****/
    // DIALOG BOX MESSAGES TO GIVE FEEDBACK BACK TO THE USER
    public static final String LEVEL_NAME_REQUEST_TEXT              = "What do you want to name your level?";
    public static final String LEVEL_NAME_REQUEST_TITLE_TEXT        = "Enter Level File Name";
    public static final String OVERWRITE_FILE_REQUEST_TEXT_A        = "There is already a file called \n";
    public static final String OVERWRITE_FILE_REQUEST_TEXT_B        = "\nWould you like to overwrite it?";
    public static final String OVERWRITE_FILE_REQUEST_TITLE_TEXT    = "Overwrite File?";
    public static final String NO_FILE_SELECTED_TEXT                = "No File was Selected to Open";
    public static final String NO_FILE_SELECTED_TITLE_TEXT          = "No File Selected";
    public static final String LEVEL_LOADED_TEXT                    = "Level File has been Loaded";
    public static final String LEVEL_LOADED_TITLE_TEXT              = "Level File Loaded";
    public static final String LEVEL_LOADING_ERROR_TEXT             = "An Error Occured While Loading the Level";
    public static final String LEVEL_LOADING_ERROR_TITLE_TEXT       = "Level Loading Error";
    public static final String LEVEL_SAVED_TEXT                     = "Level File has been Saved";
    public static final String LEVEL_SAVED_TITLE_TEXT               = "Level File Saved";
    public static final String LEVEL_SAVING_ERROR_TEXT              = "An Error Occured While Saving the Level";
    public static final String LEVEL_SAVING_ERROR_TITLE_TEXT        = "Level Saving Error";    
    public static final String PROMPT_TO_SAVE_TEXT                  = "Would you like to save your Level?";
    public static final String PROMPT_TO_SAVE_TITLE_TEXT            = "Save your Level?";
    
    public static final String PROMPT_TO_SELECT_FORMAT  = "Select your Level Format";
    public static final String TITLE_SELECT_FORMAT = "Select Level Format";
    
    // WE'LL NEED THESE TO DYNAMICALLY BUILD TEXT
    public static final String TITLE                        = "pathX Level Editor";
    public static final String EMPTY_TEXT                   = "";
    public static final String XML_LEVEL_FILE_EXTENSION         = ".xml";
    public static final String BIN_LEVEL_FILE_EXTENSION         = ".bin";
    public static final String APP_NAME                     = "PathX Level Editor";
    public static final String APP_NAME_FILE_NAME_SEPARATOR = " - ";
    public static final String PNG_FORMAT_NAME              = "png";
    public static final String PNG_FILE_EXTENSION           = "." + PNG_FORMAT_NAME;   
    
    // LEVEL EDITOR UI DIMENSIONS
    public static final int WINDOW_WIDTH = 1000;
    public static final int WINDOW_HEIGHT = 650;
    public static final int BUTTON_INSETS = 2;
    public static final int BUTTON_WIDTH = 32;
    public static final int BUTTON_HEIGHT = 32;
    
    // FOR SCROLLING THE VIEWPORT
    public static final int SCROLL_SPEED = 6;
    
    // RENDERING SETTINGS
    public static final int INTERSECTION_RADIUS = 20;
    public static final int INT_STROKE = 3;
    public static final int ONE_WAY_TRIANGLE_HEIGHT = 40;
    public static final int ONE_WAY_TRIANGLE_WIDTH = 60;

    // INITIAL START/DEST LOCATIONS
    public static final int DEFAULT_START_X = 32;
    public static final int DEFAULT_START_Y = 100;
    public static final int DEFAULT_DEST_X = 650;
    public static final int DEFAULT_DEST_Y = 100;
    
    // FOR INITIALIZING THE SPINNERS
    public static final int MIN_BOTS_PER_LEVEL = 0;
    public static final int MAX_BOTS_PER_LEVEL = 50;
    public static final int BOTS_STEP = 1;
    public static final int MIN_MONEY = 100;
    public static final int MAX_MONEY = 10000;
    public static final int STEP_MONEY = 100;
    public static final int DEFAULT_MONEY = 100;
    
    // AND FOR THE ROAD SPEED LIMITS
    public static final int DEFAULT_SPEED_LIMIT = 30;
    public static final int MIN_SPEED_LIMIT = 10;
    public static final int MAX_SPEED_LIMIT = 100;
    public static final int SPEED_LIMIT_STEP = 10;
    
    // OTHER UI SETTINGS
    public static final Border  TOOLBAR_BORDER                  = BorderFactory.createEtchedBorder();
    public static final Border  LABELED_SPINNER_PANEL_BORDER    = BorderFactory.createEtchedBorder();

    // DEFAULT COLORS
    public static final Color   INT_OUTLINE_COLOR   = Color.BLACK;
    public static final Color   HIGHLIGHTED_COLOR = Color.YELLOW;
    public static final Color   OPEN_INT_COLOR      = Color.GREEN;
    public static final Color   CLOSED_INT_COLOR    = Color.RED;
    
    // FOR RENDERING STATS
    public static final Color STATS_TEXT_COLOR = Color.ORANGE;
    public static final Font STATS_TEXT_FONT = new Font("Monospace", Font.BOLD, 16);
    public static final String MOUSE_SCREEN_POSITION_TITLE = "Screen Mouse Position: ";
    public static final String MOUSE_LEVEL_POSITION_TITLE = "Level Mouse Position: ";
    public static final String VIEWPORT_POSITION_TITLE = "Viewport Position: ";

    // FOR POSITIONING THE STATS
    public static final int STATS_X = 20;
    public static final int STATS_Y_DIFF = 20;
    public static final int MOUSE_SCREEN_POSITION_Y = 20;
    public static final int MOUSE_LEVEL_POSITION_Y = MOUSE_SCREEN_POSITION_Y + STATS_Y_DIFF;
    public static final int VIEWPORT_POSITION_Y = MOUSE_LEVEL_POSITION_Y + STATS_Y_DIFF;

    // FOR LOADING STUFF FROM OUR LEVEL XML FILES    
    // THIS IS THE NAME OF THE SCHEMA
    public static final String  LEVEL_SCHEMA = "PathXLevelSchema.xsd";
    
    // CONSTANTS FOR LOADING DATA FROM THE XML FILES
    // THESE ARE THE XML NODES
    public static final String LEVEL_NODE = "level";
    public static final String INTERSECTIONS_NODE = "intersections";
    public static final String INTERSECTION_NODE = "intersection";
    public static final String ROADS_NODE = "roads";
    public static final String ROAD_NODE = "road";
    public static final String START_INTERSECTION_NODE = "start_intersection";
    public static final String DESTINATION_INTERSECTION_NODE = "destination_intersection";
    public static final String MONEY_NODE = "money";
    public static final String POLICE_NODE = "police";
    public static final String BANDITS_NODE = "bandits";
    public static final String ZOMBIES_NODE = "zombies";

    // AND THE ATTRIBUTES FOR THOSE NODES
    public static final String NAME_ATT = "name";
    public static final String IMAGE_ATT = "image";
    public static final String ID_ATT = "id";
    public static final String X_ATT = "x";
    public static final String Y_ATT = "y";
    public static final String OPEN_ATT = "open";
    public static final String INT_ID1_ATT = "int_id1";
    public static final String INT_ID2_ATT = "int_id2";
    public static final String SPEED_LIMIT_ATT = "speed_limit";
    public static final String ONE_WAY_ATT = "one_way";
    public static final String AMOUNT_ATT = "amount";
    public static final String NUM_ATT = "num";

    // FOR NICELY FORMATTED XML OUTPUT
    public static final String XML_INDENT_PROPERTY = "{http://xml.apache.org/xslt}indent-amount";
    public static final String XML_INDENT_VALUE = "5";
    public static final String YES_VALUE = "Yes";
 
    // HTML FOR THE HELP DIALOG
    public static final String HELP_DISPLAY = 
                "<html>"
            +   " <body>"
            +   "  <h2>Help for the PathX Level Editor</h2>"
            +   "  <p>Key Commands</p>"
            +   "  <table border=\"0\">"
            +   "   <tr><td><strong>A</strong></td><td>Add a New Intersection (i.e. node)</td></tr>"
            +   "   <tr><td><strong>Delete</strong></td><td>Delete Selected Road or Intersection</td></tr>"
            +   "   <tr><td><strong>Escape</strong></td><td>Exit Current Edit Operation</td></tr>"
            +   "   <tr><td><strong>H</strong></td><td>Open Help Dialog</td></tr>"
            +   "   <tr><td><strong>R</strong></td><td>Add a New Road</td></tr>"
            +   "   <tr><td><strong>S</strong></td><td>Decrease Speed Limit for Selected Road</td></tr>"
            +   "   <tr><td><strong>W</strong></td><td>Increase Speed Limit for Selected Road</td></tr>"
            +   "   <tr><td><strong>Z</strong></td><td>Make Selected Road One Way</td></tr>"
            +   "  </table>"
            +   " </body>"
            +   "</html>";
}