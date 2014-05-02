package PathX;

import java.awt.Color;
import java.awt.Font;


/**
 * This class stores all the constants used by The Sorting Hat application. We'll
 * do this here rather than load them from files because many of these are
 * derived from each other.
 * 
 * @author Richard McKenna & Eric Loo
 */
public class PathXConstants
{
    // WE NEED THESE CONSTANTS JUST TO GET STARTED
    // LOADING SETTINGS FROM OUR XML FILES
    public static String PROPERTY_TYPES_LIST = "property_types.txt";
    public static String PROPERTIES_FILE_NAME = "properties.xml";
    public static String PROPERTIES_SCHEMA_FILE_NAME = "properties_schema.xsd";    
    public static String PATH_DATA = "./data/";
    public static String PATH_DATA_LEVELS = "data/";
    
     // FOR LOADING STUFF FROM OUR LEVEL XML FILES    
    // THIS IS THE NAME OF THE SCHEMA
    public static final String  LEVEL_SCHEMA = "PathXLevelSchema.xsd";
    
    // THESE ARE THE TYPES OF CONTROLS, WE USE THESE CONSTANTS BECAUSE WE'LL
    // STORE THEM BY TYPE, SO THESE WILL PROVIDE A MEANS OF IDENTIFYING THEM
    
    // EACH SCREEN HAS ITS OWN BACKGROUND TYPE
    public static final String BACKGROUND_TYPE = "BACKGROUND_TYPE";
    
    
    
    // THIS REPRESENTS THE BUTTONS ON THE MENU SCREEN FOR LEVEL SELECTION
    public static final String LEVEL_SELECT_BUTTON_TYPE = "LEVEL_SELECT_BUTTON_TYPE";
    public static final String GAME_LEVEL_SELECT_BUTTON_TYPE = "GAME_LEVEL_SELECT_BUTTON_TYPE";

    // IN-GAME UI CONTROL TYPES
    public static final String NEW_GAME_BUTTON_TYPE = "NEW_GAME_BUTTON_TYPE";
    public static final String BACK_BUTTON_TYPE = "BACK_BUTTON_TYPE";
    public static final String BACK_TO_LEVEL_SELECT_TYPE = "BACK_TO_LEVEL_SELECT_TYPE";
    public static final String PAUSE_BUTTON_TYPE = "PAUSE_BUTTON_TYPE";
    public static final String UP_BUTTON_TYPE = "UP_BUTTON_TYPE";
    public static final String DOWN_BUTTON_TYPE = "DOWN_BUTTON_TYPE";
    public static final String LEFT_BUTTON_TYPE = "LEFT_BUTTON_TYPE";
    public static final String RIGHT_BUTTON_TYPE = "RIGHT_BUTTON_TYPE";
    public static final String MISCASTS_COUNT_TYPE = "TILE_COUNT_TYPE";
    public static final String TIME_TYPE = "TIME_TYPE"; 
    public static final String STATS_BUTTON_TYPE = "STATS_BUTTON_TYPE";
    public static final String UNDO_BUTTON_TYPE = "UNDO_BUTTON_TYPE";
    public static final String ALGORITHM_TYPE = "ALGORITHM_TYPE";
    public static final String BACKGROUND_GAME_TYPE = "BACKGROUND_GAME_TYPE"; 
    public static final String LEVEL_GAME_TYPE = "LEVEL_GAME_TYPE";
    public static final String SBU_GAME_TYPE = "SBU_GAME_TYPE";
    public static final String LEVEL2_GAME_TYPE = "LEVEL2_GAME_TYPE";
    public static final String LEVEL3_GAME_TYPE = "LEVEL3_GAME_TYPE";
    public static final String LEVEL4_GAME_TYPE = "LEVEL4_GAME_TYPE";
    public static final String LEVEL5_GAME_TYPE = "LEVEL5_GAME_TYPE";
    public static final String LEVEL6_GAME_TYPE = "LEVEL6_GAME_TYPE";
    public static final String LEVEL7_GAME_TYPE = "LEVEL7_GAME_TYPE";
    public static final String LEVEL8_GAME_TYPE = "LEVEL8_GAME_TYPE";
    public static final String LEVEL9_GAME_TYPE = "LEVEL9_GAME_TYPE";
    public static final String LEVEL10_GAME_TYPE = "LEVEL10_GAME_TYPE";
    public static final String LEVEL11_GAME_TYPE = "LEVEL11_GAME_TYPE";
    public static final String LEVEL12_GAME_TYPE = "LEVEL12_GAME_TYPE";
    public static final String LEVEL13_GAME_TYPE = "LEVEL13_GAME_TYPE";
    public static final String LEVEL14_GAME_TYPE = "LEVEL14_GAME_TYPE";
    public static final String LEVEL15_GAME_TYPE = "LEVEL15_GAME_TYPE";
    public static final String LEVEL16_GAME_TYPE = "LEVEL16_GAME_TYPE";
    public static final String LEVEL17_GAME_TYPE = "LEVEL17_GAME_TYPE";
    public static final String LEVEL18_GAME_TYPE = "LEVEL18_GAME_TYPE";
    public static final String LEVEL19_GAME_TYPE = "LEVEL19_GAME_TYPE";
    public static final String LEVEL20_GAME_TYPE = "LEVEL20_GAME_TYPE";
    

    // DEFAULT IMAGE FILES
    public static final String DEFAULT_BG_IMG       = "DeathValleyBackground.png";
    public static final String DEFAULT_START_IMG    = "DefaultStartLocation.png";
    public static final String DEFAULT_DEST_IMG     = "DefaultDestination.png";
    
    
    
    // DIALOG TYPES
    public static final String WIN_DIALOG_TYPE = "WIN_DIALOG_TYPE";
    public static final String STATS_DIALOG_TYPE = "STATS_DIALOG_TYPE";
    
    // WE'LL USE THESE STATES TO CONTROL SWITCHING BETWEEN THE TWO
    public static final String MENU_SCREEN_STATE = "MENU_SCREEN_STATE";
    public static final String GAME_SCREEN_STATE = "GAME_SCREEN_STATE"; 
    public static final String GAMES_SCREEN_STATE = "GAMES_SCREEN_STATE";
    public static final String LEVEL_SCREEN_STATE = "LEVEL_SCREEN_STATE";
    public static final String HELP_SCREEN_STATE = "HELP_SCREEN_STATE";
    public static final String SETTINGS_SCREEN_STATE = "SETTINGS_SCREEN_STATE";
    
    public static final String SBU_SCREEN_STATE = "SBU_SCREEN_STATE";
    public static final String LEVEL2_SCREEN_STATE = "LEVEL2_SCREEN_STATE";
    public static final String LEVEL3_SCREEN_STATE = "LEVEL3_SCREEN_STATE";
    public static final String LEVEL4_SCREEN_STATE = "LEVEL4_SCREEN_STATE";
    public static final String LEVEL5_SCREEN_STATE = "LEVEL5_SCREEN_STATE";
    public static final String LEVEL6_SCREEN_STATE = "LEVEL6_SCREEN_STATE";
    public static final String LEVEL7_SCREEN_STATE = "LEVEL7_SCREEN_STATE";
    public static final String LEVEL8_SCREEN_STATE = "LEVEL8_SCREEN_STATE";
    public static final String LEVEL9_SCREEN_STATE = "LEVEL9_SCREEN_STATE";
    public static final String LEVEL10_SCREEN_STATE = "LEVEL10_SCREEN_STATE";
    public static final String LEVEL11_SCREEN_STATE = "LEVEL11_SCREEN_STATE";
    public static final String LEVEL12_SCREEN_STATE = "LEVEL12_SCREEN_STATE";
    public static final String LEVEL13_SCREEN_STATE = "LEVEL13_SCREEN_STATE";
    public static final String LEVEL14_SCREEN_STATE = "LEVEL14_SCREEN_STATE";
    public static final String LEVEL15_SCREEN_STATE = "LEVEL15_SCREEN_STATE";
    public static final String LEVEL16_SCREEN_STATE = "LEVEL16_SCREEN_STATE";
    public static final String LEVEL17_SCREEN_STATE = "LEVEL17_SCREEN_STATE";
    public static final String LEVEL18_SCREEN_STATE = "LEVEL18_SCREEN_STATE";
    public static final String LEVEL19_SCREEN_STATE = "LEVEL19_SCREEN_STATE";
    public static final String LEVEL20_SCREEN_STATE = "LEVEL20_SCREEN_STATE";
    
    //Level Information
    public static final String LEVEL1_INFO = "Level 1\nStony Brook University, NY\nRob the bank in SBU\nand make a getaway\nto earn $200";
    public static final String LEVEL2_INFO = "Level 2\nNew York, NY\nRob the bank in Manhatten\nand make a getaway\nto earn $200";
    public static final String LEVEL3_INFO = "Level 3\nPennsylvania\nRob the bank in Pennsylvania\nand make a getaway\nto earn $300";
    public static final String LEVEL4_INFO = "Level 4\nPennsylvania\nRob the bank in the City\nand make a getaway\nto earn $400";
    public static final String LEVEL5_INFO = "Level 5\nHarrisburg, PA\nRob the bank in Harrisburg\nand make a getaway\nto earn $500";
    public static final String LEVEL6_INFO = "Level 6\nPennsylvania\nRob the outskirts in Pennsylvania\nand make a getaway\nto earn $600";
    public static final String LEVEL7_INFO = "Level 7\nPennsylvania\nRob the Farm in Pennsylvania\nand make a getaway\nto earn $600";
    public static final String LEVEL8_INFO = "Level 8\nWest Virginia\nRob the bank in West Virginia\nand make a getaway\nto earn $700";
    public static final String LEVEL9_INFO = "Level 9\nOhio\nRob the bank in Ohio\nand make a getaway\nto earn $800";
    public static final String LEVEL10_INFO = "Level 10\nOhio State University\nRob the Money in OSU\nand make a getaway\nto earn $800";
    public static final String LEVEL11_INFO = "Level 11\nOhio\nRob the Farm House in Ohio\nand make a getaway\nto earn $800";
    public static final String LEVEL12_INFO = "Level 12\nIndiana\nRob the Campers in Indiana\nand make a getaway\nto earn $1000";
    public static final String LEVEL13_INFO = "Level 13\nIndiana\nRob the Church in Indiana\nand make a getaway\nto earn $1100";
    public static final String LEVEL14_INFO = "Level 14\nIndiana\nRob the bank in Indiana\nand make a getaway\nto earn $1100";
    public static final String LEVEL15_INFO = "Level 15\nIllinois\nRob the People in Illinois\nand make a getaway\nto earn $1300";
    public static final String LEVEL16_INFO = "Level 16\nUniversity of Illinois\nRob the students in UI\nand make a getaway\nto earn $1500";
    public static final String LEVEL17_INFO = "Level 17\nSt. Louis, MO\nRob the bank in St. Louis\nand make a getaway\nto earn $1700";
    public static final String LEVEL18_INFO = "Level 18\nBotanical Garden\nRob the Botanical Gardens in Missouri\nand make a getaway\nto earn $1700";
    public static final String LEVEL19_INFO = "Level 19\nUniversity of Missouri\nRob the students in UM\nand make a getaway\nto earn $1700";
    public static final String LEVEL20_INFO = "Level 20\nMissouri\nRob the natives in Missouri\nand make a getaway\nto earn $2500";

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
    
    
    
    // ANIMATION SPEED
    public static final int FPS = 30;

    // UI CONTROL SIZE AND POSITION SETTINGS
    public static final int WINDOW_WIDTH = 1280;
    public static final int WINDOW_HEIGHT = 720;
    public static final int VIEWPORT_MARGIN_LEFT = 20;
    public static final int VIEWPORT_MARGIN_RIGHT = 20;
    public static final int VIEWPORT_MARGIN_TOP = 20;
    public static final int VIEWPORT_MARGIN_BOTTOM = 20;
    public static final int LEVEL_BUTTON_WIDTH = 200;
    public static final int LEVEL_BUTTON_MARGIN = 5;
    public static final int LEVEL_BUTTON_Y = 570;
    public static final int VIEWPORT_INC = 5;
        
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
    
    
    // FOR TILE RENDERING
    public static final int NUM_TILES = 30;
    public static final int TILE_WIDTH = 135;
    public static final int TILE_HEIGHT = 126;
    public static final int TILE_IMAGE_OFFSET_X = 45;
    public static final int TILE_IMAGE_OFFSET_Y = 30;
    public static final String TILE_SPRITE_TYPE_PREFIX = "TILE_";
    public static final int COLOR_INC = 10;
    
    // FOR MOVING TILES AROUND
    public static final int MAX_TILE_VELOCITY = 20;
    
    // UI CONTROLS POSITIONS IN THE GAME SCREEN
    public static final int NORTH_PANEL_HEIGHT = 130;
    public static final int CONTROLS_MARGIN = 0;
    public static final int NEW_BUTTON_X = 1180;
    public static final int NEW_BUTTON_Y = 60;
    public static final int BACK_BUTTON_X = 1100;
    public static final int BACK_BUTTON_Y = 60;
    public static final int SELECT_LEVEL_X = 80;
    public static final int SELECT_LEVEL_Y = 155;
    public static final int PAUSE_X = 20;
    public static final int PAUSE_Y = 155;
    
    public static final int UP_BUTTON_X = 960;
    public static final int UP_BUTTON_Y = 15;
    public static final int DOWN_BUTTON_X = UP_BUTTON_X;
    public static final int DOWN_BUTTON_Y = UP_BUTTON_Y + 45;
    public static final int LEFT_BUTTON_X = UP_BUTTON_X - 50;
    public static final int LEFT_BUTTON_Y = UP_BUTTON_Y + 25;
    public static final int RIGHT_BUTTON_X = UP_BUTTON_X + 50;
    public static final int RIGHT_BUTTON_Y = UP_BUTTON_Y + 25;
    
    public static final int UP_BUTTON_GAME_X = 132;
    public static final int UP_BUTTON_GAME_Y = 525;
    public static final int DOWN_BUTTON_GAME_X = UP_BUTTON_GAME_X;
    public static final int DOWN_BUTTON_GAME_Y = UP_BUTTON_GAME_Y + 45;
    public static final int LEFT_BUTTON_GAME_X = UP_BUTTON_GAME_X - 50;
    public static final int LEFT_BUTTON_GAME_Y = UP_BUTTON_GAME_Y + 25;
    public static final int RIGHT_BUTTON_GAME_X = UP_BUTTON_GAME_X + 50;
    public static final int RIGHT_BUTTON_GAME_Y = UP_BUTTON_GAME_Y + 25;
    
    public static final int TILE_COUNT_X = NEW_BUTTON_X + 260 + CONTROLS_MARGIN;
    public static final int TILE_COUNT_Y = 0;
    public static final int TILE_COUNT_OFFSET = 145;
    public static final int TILE_TEXT_OFFSET = 60;
    public static final int TIME_X = TILE_COUNT_X + 232 + CONTROLS_MARGIN;
    public static final int TIME_Y = 0;
    public static final int TIME_OFFSET = 130;
    public static final int TIME_TEXT_OFFSET = 55;
    public static final int STATS_X = TIME_X + 310 + CONTROLS_MARGIN;
    public static final int STATS_Y = 0;
    public static final int UNDO_BUTTON_X = STATS_X + 160 + CONTROLS_MARGIN;
    public static final int UNDO_BUTTON_Y = 0;
    public static final int TEMP_TILE_X = STATS_X + 290 + CONTROLS_MARGIN;
    public static final int TEMP_TILE_Y = 0;
    public static final int TEMP_TILE_OFFSET_X = 30;
    public static final int TEMP_TILE_OFFSET_Y = 12;
    public static final int TEMP_TILE_OFFSET2 = 105;
    
    // STATS DIALOG COORDINATES
    public static final int STATS_LEVEL_INC_Y = 30;
    public static final int STATS_LEVEL_X = 460;
    public static final int STATS_LEVEL_Y = 300;
    public static final int STATS_ALGORITHM_Y = STATS_LEVEL_Y + (STATS_LEVEL_INC_Y * 2);
    public static final int STATS_GAMES_Y = STATS_ALGORITHM_Y + STATS_LEVEL_INC_Y;
    public static final int STATS_WINS_Y = STATS_GAMES_Y + STATS_LEVEL_INC_Y;
    public static final int STATS_PERFECT_WINS_Y = STATS_WINS_Y + STATS_LEVEL_INC_Y;
    public static final int STATS_FASTEST_PERFECT_WIN_Y = STATS_PERFECT_WINS_Y + STATS_LEVEL_INC_Y;
    
    // THESE ARE USED FOR FORMATTING THE TIME OF GAME
    public static final long MILLIS_IN_A_SECOND = 1000;
    public static final long MILLIS_IN_A_MINUTE = 1000 * 60;
    public static final long MILLIS_IN_AN_HOUR  = 1000 * 60 * 60;

    // USED FOR DOING OUR VICTORY ANIMATION
    public static final int WIN_PATH_NODES = 5;
    public static final int WIN_PATH_TOLERANCE = 20;
    public static final int WIN_PATH_COORD = 100;

    // COLORS USED FOR RENDERING VARIOUS THINGS, INCLUDING THE
    // COLOR KEY, WHICH REFERS TO THE COLOR TO IGNORE WHEN
    // LOADING ART.
    public static final Color COLOR_KEY = new Color(255, 174, 201);
    public static final Color COLOR_DEBUG_TEXT = Color.BLACK;
    public static final Color COLOR_TEXT_DISPLAY = new Color (10, 160, 10);
    public static final Color COLOR_STATS = new Color(0, 60, 0);
    public static final Color COLOR_ALGORITHM_HEADER = Color.WHITE;
    
    // DEFAULT COLORS
    public static final Color   INT_OUTLINE_COLOR   = Color.BLACK;
    public static final Color   HIGHLIGHTED_COLOR = Color.YELLOW;
    public static final Color   OPEN_INT_COLOR      = Color.GREEN;
    public static final Color   CLOSED_INT_COLOR    = Color.RED;

    // FONTS USED DURING FOR TEXTUAL GAME DISPLAYS
    public static final Font FONT_TEXT_DISPLAY = new Font(Font.SANS_SERIF, Font.BOLD, 48);
    public static final Font FONT_DEBUG_TEXT = new Font(Font.MONOSPACED, Font.BOLD, 14);
    public static final Font FONT_STATS = new Font(Font.MONOSPACED, Font.BOLD, 20);
}