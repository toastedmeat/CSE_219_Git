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
import PathX.ui.carSprite;
import java.awt.Image;
import java.awt.geom.Line2D;
import javax.swing.JPanel;
import mini_game.Viewport;

/**
 * This class manages the game data for The Sorting Hat.
 *
 * @author Richard McKenna & Eric Loo
 */
public class PathXDataModel extends MiniGameDataModel {

    // THIS CLASS HAS A REFERERENCE TO THE MINI GAME SO THAT IT
    // CAN NOTIFY IT TO UPDATE THE DISPLAY WHEN THE DATA MODEL CHANGES
    private PathXMiniGame miniGame;

    // THESE ARE USED FOR TIMING THE GAME
    private GregorianCalendar startTime;
    private GregorianCalendar endTime;

    // LEVEL
    private String currentLevel;

    private int money;

    // THIS IS THE LEVEL CURRENTLY BEING EDITING
    PathXLevel level;

    // DATA FOR RENDERING
    Viewport viewport;

    // WE ONLY NEED TO TURN THIS ON ONCE
    boolean levelBeingEdited;
    Image backgroundImage;
    Image startingLocationImage;
    Image destinationImage;
    Image playerImage;

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

    boolean[] levelsLocked = new boolean[20];
    boolean[] levelsRobbed = new boolean[20];
    String[] levelsNames = {"./pathX/Level1.xml","./pathX/Level2.xml",
        "./pathX/Level3.xml","./pathX/Level4.xml","./pathX/Level5.xml",
        "./pathX/Level6.xml","./pathX/Level7.xml","./pathX/Level8.xml",
            "./pathX/Level9.xml","./pathX/Level10.xml",
            "./pathX/Level11.xml","./pathX/Level12.xml",
            "./pathX/Level13.xml","./pathX/Level14.xml",
            "./pathX/Level15.xml","./pathX/Level16.xml",
            "./pathX/Level17.xml", "./pathX/Level18.xml",
            "./pathX/Level19.xml","./pathX/Level20.xml"};

    PathXEditMode editMode;

    carSprite player;

    /**
     * Constructor for initializing this data model, it will create the data
     * structures for storing tiles, but not the tile grid itself, that is
     * dependent on file loading, and so should be subsequently initialized.
     *
     * @param initMiniGame The PathXGame
     */
    public PathXDataModel(PathXMiniGame initMiniGame) {
        // KEEP THE GAME FOR LATER
        miniGame = initMiniGame;
        level = new PathXLevel();
        viewport = new Viewport();
        levelBeingEdited = false;
        editMode = PathXEditMode.NOTHING_SELECTED;
        viewport.setScreenSize(1280, 720);
        //startRoadIntersection = null;
        levelsLocked[0] = false;
        for (int i = 1; i < levelsLocked.length; i++) {
            levelsLocked[i] = true;
        }
        for (int i = 0; i < levelsRobbed.length; i++) {
            levelsRobbed[i] = false;
        }

    }

    // ACCESSOR METHODS
    public PathXMiniGame getGame() {
        return miniGame;
    }

    public int getTotalMoney() {
        return money;
    }

    public void setTotalMoney(int money) {
        this.money = money;
    }

    public PathXLevel getLevel() {
        return level;
    }

    public void setLevel(PathXLevel l) {
        level = l;
    }

    public boolean getLoadedLevel() {
        return loadedLevel;
    }

    public void setLoadedLevel(boolean loadedLevel) {
        this.loadedLevel = loadedLevel;
    }

    public PathXEditMode getEditMode() {
        return editMode;
    }

    @Override
    public Viewport getViewport() {
        return viewport;
    }

    public boolean isLevelBeingEdited() {
        return levelBeingEdited;
    }

    public Image getStartingLocationImage() {
        return startingLocationImage;
    }

    public Image getDesinationImage() {
        return destinationImage;
    }

    public Image getPlayerLocationImage() {
        return playerImage;
    }

    public Intersection getSelectedIntersection() {
        return selectedIntersection;
    }

    public Road getSelectedRoad() {
        return selectedRoad;
    }

    public Intersection getStartingLocation() {
        return level.startingLocation;
    }

    public Intersection getDestination() {
        return level.destination;
    }

    public boolean isDataUpdatedSinceLastSave() {
        return dataUpdatedSinceLastSave;
    }

    public boolean isStartingLocation(Intersection testInt) {
        return testInt == level.startingLocation;
    }

    public boolean isDestination(Intersection testInt) {
        return testInt == level.destination;
    }

    public boolean isSelectedIntersection(Intersection testIntersection) {
        return testIntersection == selectedIntersection;
    }

    public boolean isSelectedRoad(Road testRoad) {
        return testRoad == selectedRoad;
    }

    public boolean[] getLevelsLocked() {
        return levelsLocked;
    }

    public void setLevelsLocked(boolean[] levelsLocked) {
        this.levelsLocked = levelsLocked;
    }

    public boolean[] getLevelsRobbed() {
        return levelsRobbed;
    }

    public void setLevelsRobbed(boolean[] levelsRobbed) {
        this.levelsRobbed = levelsRobbed;
    }

    // THESE ARE FOR TESTING WHAT EDIT MODE THE APP CURRENTLY IS IN
    public boolean isNothingSelected() {
        return editMode == PathXEditMode.NOTHING_SELECTED;
    }

    public boolean isIntersectionSelected() {
        return editMode == PathXEditMode.INTERSECTION_SELECTED;
    }

    public boolean isIntersectionDragged() {
        return editMode == PathXEditMode.INTERSECTION_DRAGGED;
    }

    public boolean isRoadSelected() {
        return editMode == PathXEditMode.ROAD_SELECTED;
    }

    public boolean isAddingIntersection() {
        return editMode == PathXEditMode.ADDING_INTERSECTION;
    }

    public boolean isAddingRoadStart() {
        return editMode == PathXEditMode.ADDING_ROAD_START;
    }

    public boolean isAddingRoadEnd() {
        return editMode == PathXEditMode.ADDING_ROAD_END;
    }

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

    public String[] getLevelsNames() {
        return levelsNames;
    }

    public void setLevelsNames(String[] levelsNames) {
        this.levelsNames = levelsNames;
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
     * @param game The game.
     *
     * @param x The x-axis pixel location of the mouse click.
     *
     * @param y The y-axis pixel location of the mouse click.
     */
    @Override

    public void checkMousePressOnSprites(MiniGame game, int x, int y) {

        // MAKE SURE THE CANVAS HAS FOCUS SO THAT IT
        // WILL PROCESS THE PROPER KEY PRESSES
        player = miniGame.getGUIEnemies().get(PLAYER_TYPE);
        // THESE ARE CANVAS COORDINATES
        int canvasX = (x - 274);
        int canvasY = (y - 17);

        // IF WE ARE IN ONE OF THESE MODES WE MAY WANT TO SELECT
        // ANOTHER INTERSECTION ROAD
        if (this.isNothingSelected()
                || this.isIntersectionSelected()
                || this.isRoadSelected()) {
            // CHECK TO SEE IF THE USER IS SELECTING AN INTERSECTION
            Intersection i = this.findIntersectionAtCanvasLocation(canvasX, canvasY);
            if (i != null) {
                // MAKE THIS THE SELECTED INTERSECTION
                this.setSelectedIntersection(i);
                if (player.isEnabled()) {
                    player.setTarget(i.getX(), i.getY());
                    player.startMovingToTarget(10);

                    player.update(miniGame);
                }
                return;
            }

            // IF NO INTERSECTION WAS SELECTED THEN CHECK TO SEE IF 
            // THE USER IS SELECTING A ROAD
            Road r = this.selectRoadAtCanvasLocation(canvasX, canvasY);
            if (r != null) {
                // MAKE THIS THE SELECTED ROAD
                this.setSelectedRoad(r);
                return;
            }

            // OTHERWISE DESELECT EVERYTHING
            this.unselectEverything();
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
        lastMouseX = initX;
        lastMouseY = initY;
        miniGame.getCanvas().repaint();
    }

    public void setSelectedIntersection(Intersection i) {
        selectedIntersection = i;
        selectedRoad = null;
        miniGame.getCanvas().repaint();
    }

    public void setSelectedRoad(Road r) {
        selectedRoad = r;
        selectedIntersection = null;
        miniGame.getCanvas().repaint();
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
        startingLocationImage = miniGame.loadImage(imgPath + "/pathX/" + DEFAULT_START_IMG);
        destinationImage = miniGame.loadImage(imgPath + "/pathX/" + DEFAULT_DEST_IMG);
        playerImage = miniGame.loadImage(imgPath + "/pathX/" + DEFAULT_DEST_IMG);

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
     * Updates the image used for the starting location and forces rendering.
     */
    public void updateStartingLocationImage(String newStartImage) {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty(pathXPropertyType.PATH_IMG);
        level.startingLocationImageFileName = newStartImage;
        startingLocationImage = miniGame.loadImageWithColorKey(imgPath + "/pathX/" + level.startingLocationImageFileName, COLOR_KEY);
        miniGame.getCanvas().repaint();
    }

    /**
     * Updates the image used for the destination and forces rendering.
     */
    public void updateDestinationImage(String newDestImage) {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty(pathXPropertyType.PATH_IMG);
        level.destinationImageFileName = newDestImage;
        destinationImage = miniGame.loadImageWithColorKey(imgPath + "/pathX/" + level.destinationImageFileName, COLOR_KEY);
        miniGame.getCanvas().repaint();
    }

    /**
     * Used for scrolling the miniGameport by (incX, incY). Note that it won't
     * let the viewport scroll off the level.
     */
    public void moveViewport(int incX, int incY) {
        // MOVE THE VIEWPORT
        viewport.move(incX, incY);

        // AND NOW FORCE A REDRAW
        miniGame.getCanvas().repaint();
    }

    public void switchEditMode(PathXEditMode initEditMode) {
        if (levelBeingEdited) {
            // SET THE NEW EDIT MODE
            editMode = initEditMode;

            // IF WE'RE ADDING A ROAD, THEN NOTHING SHOULD BE SELECTED 
            if (editMode == PathXEditMode.ADDING_ROAD_START) {
                selectedIntersection = null;
                selectedRoad = null;
            }

            // RENDER
            miniGame.getCanvas().repaint();
        }
    }

    public Road selectRoadAtCanvasLocation(int canvasX, int canvasY) {
        Iterator<Road> it = level.roads.iterator();
        Line2D.Double tempLine = new Line2D.Double();
        while (it.hasNext()) {
            Road r = it.next();
            tempLine.x1 = r.node1.x;
            tempLine.y1 = r.node1.y;
            tempLine.x2 = r.node2.x;
            tempLine.y2 = r.node2.y;
            double distance = tempLine.ptSegDist(canvasX + viewport.getViewportX(), canvasY + viewport.getViewportY());

            // IS IT CLOSE ENOUGH?
            if (distance <= INT_STROKE) {
                // SELECT IT
                this.selectedRoad = r;
                this.switchEditMode(PathXEditMode.ROAD_SELECTED);
                return selectedRoad;
            }
        }
        return null;
    }

    public Intersection findIntersectionAtCanvasLocation(int canvasX, int canvasY) {
        // CHECK TO SEE IF THE USER IS SELECTING AN INTERSECTION
        for (Intersection i : level.intersections) {
            double distance = calculateDistanceBetweenPoints(i.x, i.y, canvasX + viewport.getViewportX(), canvasY + viewport.getViewportY());
            if (distance < INTERSECTION_RADIUS) {
                // MAKE THIS THE SELECTED INTERSECTION
                return i;
            }
        }
        return null;
    }

    public double calculateDistanceBetweenPoints(int x1, int y1, int x2, int y2) {
        double diffXSquared = Math.pow(x1 - x2, 2);
        double diffYSquared = Math.pow(y1 - y2, 2);
        return Math.sqrt(diffXSquared + diffYSquared);
    }

    public void unselectEverything() {
        selectedIntersection = null;
        selectedRoad = null;
        //startRoadIntersection = null;
        miniGame.getCanvas().repaint();
    }

    @Override
    public boolean isPaused() {
        return super.isPaused();
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void unpause() {
        super.unpause();
    }
}
