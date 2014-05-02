package PathX.data;

import java.awt.Graphics;
import PathX.ui.PathXTile;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Iterator;
import PathX.PathX.pathXPropertyType;
import mini_game.MiniGame;
import mini_game.MiniGameDataModel;
import mini_game.SpriteType;
import properties_manager.PropertiesManager;
import static PathX.PathXConstants.*;
import PathX.ui.PathXMiniGame;
import PathX.ui.PathXPanel;
import PathX.ui.PathXTileState;
import java.awt.Image;
import mini_game.Viewport;

/**
 * This class manages the game data for The Sorting Hat.
 *
 * @author Richard McKenna & Eric Loo
 */
public class PathXDataModel extends MiniGameDataModel {

    // THIS CLASS HAS A REFERERENCE TO THE MINI GAME SO THAT IT
    // CAN NOTIFY IT TO UPDATE THE DISPLAY WHEN THE DATA MODEL CHANGES
    private MiniGame miniGame;

    // THESE ARE USED FOR TIMING THE GAME
    private GregorianCalendar startTime;
    private GregorianCalendar endTime;

    // LEVEL
    private String currentLevel;

    // THIS IS THE LEVEL CURRENTLY BEING EDITING
    PathXLevel level;

    // DATA FOR RENDERING
    Viewport viewport;

    // WE ONLY NEED TO TURN THIS ON ONCE
    boolean levelBeingEdited;
    Image backgroundImage;
    Image startingLocationImage;
    Image destinationImage;

    // THE SELECTED INTERSECTION OR ROAD MIGHT BE EDITED OR DELETED
    // AND IS RENDERED DIFFERENTLY
    Intersection selectedIntersection;
    Road selectedRoad;


    // IN CASE WE WANT TO TRACK MOVEMENTS
    int lastMouseX;
    int lastMouseY;

    // THESE BOOLEANS HELP US KEEP TRACK OF
    // @todo DO WE NEED THESE?
    boolean isMousePressed;
    boolean isDragging;
    boolean dataUpdatedSinceLastSave;
    boolean loadedLevel;
    
    /**
     * Constructor for initializing this data model, it will create the data
     * structures for storing tiles, but not the tile grid itself, that is
     * dependent on file loading, and so should be subsequently initialized.
     *
     * @param initMiniGame The Sorting Hat game UI.
     */
    public PathXDataModel(MiniGame initMiniGame) {
        // KEEP THE GAME FOR LATER
        miniGame = initMiniGame;
        level = new PathXLevel();
        viewport = new Viewport();
        levelBeingEdited = false;
        //startRoadIntersection = null;

    }
    

    // ACCESSOR METHODS
    public PathXLevel getLevel() {        return level;    }
    public void setLevel(PathXLevel l) { level = l; }
    public boolean getLoadedLevel() {        return loadedLevel;    }
    public void setLoadedLevel(boolean loadedLevel) {        this.loadedLevel = loadedLevel;    }
    //public Viewport         getViewport()               {   return viewport;                }
    public boolean isLevelBeingEdited() {        return levelBeingEdited;    }
    public Image getBackgroundImage() {        return backgroundImage;    }
    public Image getStartingLocationImage() {        return startingLocationImage;    }
    public Image getDesinationImage() {        return destinationImage;    }
    public Intersection getSelectedIntersection() {        return selectedIntersection;    }
    public Road getSelectedRoad() {        return selectedRoad;    }
    //public Intersection getStartRoadIntersection() {        return startRoadIntersection;    }
    //public int getLastMouseX() {        return lastMouseX;    }
    //public int getLastMouseY() {        return lastMouseY;    }
    public Intersection getStartingLocation() {        return level.startingLocation;    }
    public Intersection getDestination() {        return level.destination;    }
    public boolean isDataUpdatedSinceLastSave() {        return dataUpdatedSinceLastSave;    }
    public boolean isStartingLocation(Intersection testInt) {        return testInt == level.startingLocation;    }
    public boolean isDestination(Intersection testInt) {        return testInt == level.destination;    }
    public boolean isSelectedIntersection(Intersection testIntersection) {return testIntersection == selectedIntersection;}
    public boolean isSelectedRoad(Road testRoad) {return testRoad == selectedRoad;}


    public String getCurrentLevel() {
        return currentLevel;
    }

    public long getTimeInMillis() {
        return endTime.getTimeInMillis() - startTime.getTimeInMillis();
    }

    // MUTATOR METHODS
    public void setCurrentLevel(String initCurrentLevel) {
        currentLevel = initCurrentLevel;
    }

    /**
     * Used to calculate the x-axis pixel location in the game grid for a tile
     * placed at column with stack position z.
     *
     * @param column The column in the grid the tile is located.
     *
     * @return The x-axis pixel location of the tile
     */
    public int calculateGridTileX(int column) {
        return viewport.getViewportMarginLeft() + (column * TILE_WIDTH) - viewport.getViewportX();
    }

    /**
     * Used to calculate the y-axis pixel location in the game grid for a tile
     * placed at row.
     *
     * @param row The row in the grid the tile is located.
     *
     * @return The y-axis pixel location of the tile
     */
    public int calculateGridTileY(int row) {
        return viewport.getViewportMarginTop() + (row * TILE_HEIGHT) - viewport.getViewportY();
    }

    /**
     * Used to calculate the grid column for the x-axis pixel location.
     *
     * @param x The x-axis pixel location for the request.
     *
     * @return The column that corresponds to the x-axis location x.
     */
    public int calculateGridCellColumn(int x) {
        // ADJUST FOR THE MARGIN
        x -= viewport.getViewportMarginLeft();

        // ADJUST FOR THE VIEWPORT
        x = x + viewport.getViewportX();

        if (x < 0) {
            return -1;
        }

        // AND NOW GET THE COLUMN
        return x / TILE_WIDTH;
    }

    /**
     * Used to calculate the grid row for the y-axis pixel location.
     *
     * @param y The y-axis pixel location for the request.
     *
     * @return The row that corresponds to the y-axis location y.
     */
    public int calculateGridCellRow(int y) {
        // ADJUST FOR THE MARGIN
        y -= viewport.getViewportMarginTop();

        // ADJUST FOR THE VIEWPORT
        y = y + viewport.getViewportY();

        if (y < 0) {
            return -1;
        }

        // AND NOW GET THE ROW
        return y / TILE_HEIGHT;
    }

    // TIME TEXT METHODS
    // - timeToText
    // - gameTimeToText
    /**
     * This method creates and returns a textual description of the timeInMillis
     * argument as a time duration in the format of (H:MM:SS).
     *
     * @param timeInMillis The time to be represented textually.
     *
     * @return A textual representation of timeInMillis.
     */
    public String timeToText(long timeInMillis) {
        // FIRST CALCULATE THE NUMBER OF HOURS,
        // SECONDS, AND MINUTES
        long hours = timeInMillis / MILLIS_IN_AN_HOUR;
        timeInMillis -= hours * MILLIS_IN_AN_HOUR;
        long minutes = timeInMillis / MILLIS_IN_A_MINUTE;
        timeInMillis -= minutes * MILLIS_IN_A_MINUTE;
        long seconds = timeInMillis / MILLIS_IN_A_SECOND;

        // THEN ADD THE TIME OF GAME SUMMARIZED IN PARENTHESES
        String minutesText = "" + minutes;
        if (minutes < 10) {
            minutesText = "0" + minutesText;
        }
        String secondsText = "" + seconds;
        if (seconds < 10) {
            secondsText = "0" + secondsText;
        }
        return hours + ":" + minutesText + ":" + secondsText;
    }

    /**
     * This method builds and returns a textual representation of the game time.
     * Note that the game may still be in progress.
     *
     * @return The duration of the current game represented textually.
     */
    public String gameTimeToText() {
        // CALCULATE GAME TIME USING HOURS : MINUTES : SECONDS
        if ((startTime == null) || (endTime == null)) {
            return "";
        }
        long timeInMillis = endTime.getTimeInMillis() - startTime.getTimeInMillis();
        return timeToText(timeInMillis);
    }

    // OVERRIDDEN METHODS
    // - checkMousePressOnSprites
    // - endGameAsWin
    // - reset
    // - updateAll
    // - updateDebugText
    /**
     * This method provides a custom game response for handling mouse clicks on
     * the game screen. We'll use this to close game dialogs as well as to
     * listen for mouse clicks on grid cells.
     *
     * @param game The Sorting Hat game.
     *
     * @param x The x-axis pixel location of the mouse click.
     *
     * @param y The y-axis pixel location of the mouse click.
     */
    @Override

    public void checkMousePressOnSprites(MiniGame game, int x, int y) {
        // FIGURE OUT THE CELL IN THE GRID
        int col = calculateGridCellColumn(x);
        int row = calculateGridCellRow(y);

        // DISABLE THE STATS DIALOG IF IT IS OPEN
        if (game.getGUIDialogs().get(STATS_DIALOG_TYPE).getState().equals(PathXTileState.VISIBLE_STATE.toString())) {
            game.getGUIDialogs().get(STATS_DIALOG_TYPE).setState(PathXTileState.INVISIBLE_STATE.toString());
            return;
        }

        // CHECK THE CELL AT col, row
        int index = 0;//getSnakeIndex(col, row);

        // IT'S OUTSIDE THE GRID
        if (index < 0) {
            // DESELECT A TILE IF ONE IS SELECTED
        } // IT'S IN THE GRID
        else {
            // SELECT THE TILE IF NONE IS SELECTED
        }
    }

    /**
     * Called when a game is started, the game grid is reset.
     *
     * @param game
     */
    @Override
    public void reset(MiniGame game) {
    }

    /**
     * Called each frame, this method updates all the game objects.
     *
     * @param game The Sorting Hat game to be updated.
     */
    @Override
    public void updateAll(MiniGame game) {
        try {
            // MAKE SURE THIS THREAD HAS EXCLUSIVE ACCESS TO THE DATA
            game.beginUsingData();

            // WE ONLY NEED TO UPDATE AND MOVE THE MOVING TILES
            // IF THE GAME IS STILL ON, THE TIMER SHOULD CONTINUE
            if (inProgress()) {
                // KEEP THE GAME TIMER GOING IF THE GAME STILL IS
                endTime = new GregorianCalendar();

                // FIGURE OUT THE CELL IN THE GRID
                int col = calculateGridCellColumn(getLastMouseX());
                int row = calculateGridCellRow(getLastMouseY());

            }
        } finally {
            // MAKE SURE WE RELEASE THE LOCK WHETHER THERE IS
            // AN EXCEPTION THROWN OR NOT
            game.endUsingData();
        }
    }

    /**
     * This method is for updating any debug text to present to the screen. In a
     * graphical application like this it's sometimes useful to display data in
     * the GUI.
     *
     * @param game The Sorting Hat game about which to display info.
     */
    @Override
    public void updateDebugText(MiniGame game) {
    }
    
    

    // ITERATOR METHODS FOR GOING THROUGH THE GRAPH
    public Iterator intersectionsIterator() {
        ArrayList<Intersection> intersections = level.getIntersections();
        return intersections.iterator();
    }

    public Iterator roadsIterator() {
        ArrayList<Road> roads = level.roads;
        return roads.iterator();
    }

    // THESE ARE FOR TESTING WHAT EDIT MODE THE APP CURRENTLY IS IN
    // MUTATOR METHODS
    public void setLevelBeingEdited(boolean initLevelBeingEdited) {
        levelBeingEdited = initLevelBeingEdited;
    }

    public void setLastMousePosition(int initX, int initY) {
        //lastMouseX = initX;
       // lastMouseY = initY;
        //view.getCanvas().repaint();
    }

    public void setSelectedIntersection(Intersection i) {
        selectedIntersection = i;
        selectedRoad = null;
        //view.getCanvas().repaint();
    }

    public void setSelectedRoad(Road r) {
        selectedRoad = r;
        selectedIntersection = null;
        //view.getCanvas().repaint();
    }

    // AND THEN ALL THE SERVICE METHODS FOR UPDATING THE LEVEL
    // AND APP STATE
    /**
     * Sets up the model to edit a brand new level.
     */
    public void startNewLevel(String levelName) {
        // CLEAR OUT THE OLD GRAPH
        level.reset();

        // FIRST INITIALIZE THE LEVEL
        // WE ALWAYS START WITH A DEFAULT BACKGROUND,
        // AND START AND END LOCATIONS
        level.init(levelName,
                DEFAULT_BG_IMG,
                DEFAULT_START_IMG,
                DEFAULT_START_X,
                DEFAULT_START_Y,
                DEFAULT_DEST_IMG,
                DEFAULT_DEST_X,
                DEFAULT_DEST_Y);

        // NOW MAKE THE LEVEL IMAGES
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty(pathXPropertyType.PATH_IMG);
        backgroundImage = miniGame.loadImage(imgPath + DEFAULT_BG_IMG);
        startingLocationImage = miniGame.loadImage(imgPath + DEFAULT_START_IMG);
        destinationImage = miniGame.loadImage(imgPath + DEFAULT_DEST_IMG);

        // NOW RESET THE VIEWPORT
        viewport.reset();
        viewport.setLevelDimensions(backgroundImage.getWidth(null), backgroundImage.getHeight(null));

        // INTERACTIVE SETTINGS
        isMousePressed = false;
        isDragging = false;
        selectedIntersection = null;
        selectedRoad = null;
        dataUpdatedSinceLastSave = false;

        // THIS LETS THE LEVEL BE RENDERED
        levelBeingEdited = true;

        // AND NOW MAKE SURE IT GETS RENDERED FOR THE FIRST TIME
        miniGame.getCanvas().repaint();
    }

    /**
     * Updates the background image.
     */
    public void updateBackgroundImage(String newBgImage) {
        // UPDATE THE LEVEL TO FIT THE BACKGROUDN IMAGE SIZE
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty(pathXPropertyType.PATH_IMG);
        level.backgroundImageFileName = newBgImage;
        backgroundImage = miniGame.loadImage(imgPath + level.backgroundImageFileName);
        int levelWidth = backgroundImage.getWidth(null);
        int levelHeight = backgroundImage.getHeight(null);
        viewport.setLevelDimensions(levelWidth, levelHeight);
        miniGame.getCanvas().repaint();
    }

    /**
     * Updates the image used for the starting location and forces rendering.
     */
    public void updateStartingLocationImage(String newStartImage) {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty(pathXPropertyType.PATH_IMG);
        level.startingLocationImageFileName = newStartImage;
        startingLocationImage = miniGame.loadImage(imgPath + "/pathX/" + level.startingLocationImageFileName);
        miniGame.getCanvas().repaint();
    }

    /**
     * Updates the image used for the destination and forces rendering.
     */
    public void updateDestinationImage(String newDestImage) {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty(pathXPropertyType.PATH_IMG);
        level.destinationImageFileName = newDestImage;
        destinationImage = miniGame.loadImage(imgPath + "/pathX/"+ level.destinationImageFileName);
        miniGame.getCanvas().repaint();
    }

    /**
     * Used for scrolling the miniGameport by (incX, incY). Note that it won't let
     * the viewport scroll off the level.
     */
    public void moveViewport(int incX, int incY) {
        // MOVE THE VIEWPORT
        viewport.move(incX, incY);

        // AND NOW FORCE A REDRAW
        miniGame.getCanvas().repaint();
    }


}
