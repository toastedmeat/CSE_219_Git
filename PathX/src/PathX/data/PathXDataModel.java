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

/**
 * This class manages the game data for The Sorting Hat.
 *
 * @author Richard McKenna & Eric Loo
 */
public class PathXDataModel extends MiniGameDataModel {

    // THIS CLASS HAS A REFERERENCE TO THE MINI GAME SO THAT IT
    // CAN NOTIFY IT TO UPDATE THE DISPLAY WHEN THE DATA MODEL CHANGES
    private MiniGame miniGame;

    // THIS STORES THE TILES ON THE GRID DURING THE GAME
    private ArrayList<PathXTile> tilesToSort;

    // THE LEGAL TILES IN ORDER FROM LOW SORT INDEX TO HIGH
    //private ArrayList<SnakeCell> snake;

    // GAME GRID AND TILE DATA
    private int gameTileWidth;
    private int gameTileHeight;
    private int numGameGridColumns;
    private int numGameGridRows;

    // THESE ARE THE TILES STACKED AT THE START OF THE GAME
    private ArrayList<PathXTile> stackTiles;
    private int stackTilesX;
    private int stackTilesY;

    // THESE ARE THE TILES THAT ARE MOVING AROUND, AND SO WE HAVE TO UPDATE
    private ArrayList<PathXTile> movingTiles;

    // THIS IS THE TILE THE USER IS DRAGGING
    private PathXTile selectedTile;
    private int selectedTileIndex;

    // THIS IS THE TEMP TILE
    private PathXTile tempTile;

    // KEEPS TRACK OF HOW MANY BAD SPELLS WERE CAST
    private int badSpellsCounter;

    // THESE ARE USED FOR TIMING THE GAME
    private GregorianCalendar startTime;
    private GregorianCalendar endTime;

    // LEVEL
    private String currentLevel;

    // THE SORTING ALGORITHM WHICH GENERATES THE PROPER TRANSACTIONS

    // THE PROPER TRANSACTIONS TO USE FOR COMPARISION AGAINST PLAYER MOVES
    private int transactionCounter;
    boolean previous = true;

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

        // INIT THESE FOR HOLDING MATCHED AND MOVING TILES
        stackTiles = new ArrayList();
        movingTiles = new ArrayList();
        tilesToSort = new ArrayList();

        // NOTHING IS BEING DRAGGED YET
        selectedTile = null;
        selectedTileIndex = -1;
        tempTile = null;
    }

    // ACCESSOR METHODS
    

    public int getBadSpellsCounter() {
        return badSpellsCounter;
    }

    public int getGameTileWidth() {
        return gameTileWidth;
    }

    public int getGameTileHeight() {
        return gameTileHeight;
    }

    public int getNumGameGridColumns() {
        return numGameGridColumns;
    }

    public int getNumGameGridRows() {
        return numGameGridRows;
    }

    public PathXTile getSelectedTile() {
        return selectedTile;
    }

    public String getCurrentLevel() {
        return currentLevel;
    }

    public ArrayList<PathXTile> getTilesToSort() {
        return tilesToSort;
    }

    public ArrayList<PathXTile> getStackTiles() {
        return stackTiles;
    }

    public Iterator<PathXTile> getMovingTiles() {
        return movingTiles.iterator();
    }
    
    public long getTimeInMillis(){
        return endTime.getTimeInMillis() - startTime.getTimeInMillis();
    }

    // MUTATOR METHODS
    public void setCurrentLevel(String initCurrentLevel) {
        currentLevel = initCurrentLevel;
    }

    // INIT METHODS - AFTER CONSTRUCTION, THESE METHODS SETUP A GAME FOR USE
    // - initLevel
    // - initTiles
    // - initTile
    /**
     * Called after a level has been selected, it initializes the grid so that
     * it is the proper dimensions.
     */
    

    /**
     * This method loads the tiles, creating an individual sprite for each. Note
     * that tiles may be of various types, which is important during the tile
     * matching tests.
     */
    public void initTiles() {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty(pathXPropertyType.PATH_IMG);
        SpriteType sT;

        // WE'LL RENDER ALL THE TILES ON TOP OF THE BLANK TILE
        String blankTileFileName = props.getProperty(pathXPropertyType.IMAGE_TILE_BACKGROUND);
        BufferedImage blankTileImage = miniGame.loadImageWithColorKey(imgPath + blankTileFileName, COLOR_KEY);
        ((PathXPanel) (miniGame.getCanvas())).setBlankTileImage(blankTileImage);

        // THIS IS A HIGHLIGHTED BLANK TILE FOR WHEN THE PLAYER SELECTS ONE
        String blankTileSelectedFileName = props.getProperty(pathXPropertyType.IMAGE_TILE_BACKGROUND_SELECTED);
        BufferedImage blankTileSelectedImage = miniGame.loadImageWithColorKey(imgPath + blankTileSelectedFileName, COLOR_KEY);
        ((PathXPanel) (miniGame.getCanvas())).setBlankTileSelectedImage(blankTileSelectedImage);

        // THIS IS A MOUSE-OVER BLANK TILE
        String blankTileMouseOverFileName = props.getProperty(pathXPropertyType.IMAGE_TILE_BACKGROUND_MOUSE_OVER);
        BufferedImage blankTileMouseOverImage = miniGame.loadImageWithColorKey(imgPath + blankTileMouseOverFileName, COLOR_KEY);
        ((PathXPanel) (miniGame.getCanvas())).setBlankTileMouseOverImage(blankTileMouseOverImage);

        // NOW LOAD ALL THE TILES FROM A SPRITE SHEET
        String tilesSpriteSheetFile = props.getProperty(pathXPropertyType.IMAGE_SPRITE_SHEET_CHARACTER_TILES);
        ArrayList<BufferedImage> tileImages = miniGame.loadSpriteSheetImagesWithColorKey(imgPath + tilesSpriteSheetFile,
                68, 14, 5, 18, 5, COLOR_KEY);

        for (int i = 0; i < tileImages.size(); i++) {
            BufferedImage img = tileImages.get(i);

            // WE'LL MAKE A NEW SPRITE TYPE FOR EACH GROUP OF SIMILAR LOOKING TILES
            sT = new SpriteType(TILE_SPRITE_TYPE_PREFIX + (i + 1));
            addSpriteType(sT);

            // LET'S GENERATE AN IMAGE FOR EACH STATE FOR EACH SPRITE
            sT.addState(PathXTileState.INVISIBLE_STATE.toString(), buildTileImage(img, img)); // DOESN'T MATTER
            sT.addState(PathXTileState.VISIBLE_STATE.toString(), buildTileImage(blankTileImage, img));
            sT.addState(PathXTileState.SELECTED_STATE.toString(), buildTileImage(blankTileSelectedImage, img));
            sT.addState(PathXTileState.MOUSE_OVER_STATE.toString(), buildTileImage(blankTileMouseOverImage, img));
            PathXTile newTile = new PathXTile(sT, stackTilesX, stackTilesY, 0, 0, PathXTileState.INVISIBLE_STATE.toString(), i + 1);

            // AND ADD IT TO THE STACK
            stackTiles.add(newTile);
        }
    }

    // HELPER METHOD FOR INITIALIZING OUR WIZARD AND WITCHES TRADING CARD TILES
    private BufferedImage buildTileImage(BufferedImage backgroundImage, BufferedImage spriteImage) {
        // BASICALLY THIS RENDERS TWO IMAGES INTO A NEW ONE, COMBINING THEM, AND THEN
        // RETURNING THE RESULTING IMAGE
        BufferedImage bi = new BufferedImage(TILE_WIDTH, TILE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.getGraphics();
        g.drawImage(backgroundImage, 0, 0, null);
        g.drawImage(spriteImage, TILE_IMAGE_OFFSET_X, TILE_IMAGE_OFFSET_Y, null);
        return bi;
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

    // GAME DATA SERVICE METHODS
    // -enableTiles
    // -moveAllTilesToStack
    // -moveTiles
    // -playWinAnimation
    // -processMove
    // -selectTile
    // -undoLastMove
    /**
     * This method can be used to make all of the tiles either visible (true) or
     * invisible (false). This should be used when switching between the menu
     * and game screens.
     *
     * @param enable Specifies whether the tiles should be made visible or not.
     */
    public void enableTiles(boolean enable) {
        // PUT ALL THE TILES IN ONE PLACE WHERE WE CAN PROCESS THEM TOGETHER
        moveAllTilesToStack();

        // GO THROUGH ALL OF THEM 
        for (PathXTile tile : stackTiles) {
            // AND SET THEM PROPERLY
            if (enable) {
                tile.setState(PathXTileState.VISIBLE_STATE.toString());
            } else {
                tile.setState(PathXTileState.INVISIBLE_STATE.toString());
            }
        }
    }

    /**
     * This method moves all the tiles not currently in the stack to the stack.
     */
    public void moveAllTilesToStack() {
        moveTiles(movingTiles, stackTiles);
        moveTiles(tilesToSort, stackTiles);
    }

    /**
     * This method removes all the tiles in from argument and moves them to
     * argument.
     *
     * @param from The source data structure of tiles.
     *
     * @param to The destination data structure of tiles.
     */
    private void moveTiles(ArrayList<PathXTile> from, ArrayList<PathXTile> to) {
        // GO THROUGH ALL THE TILES, TOP TO BOTTOM
        for (int i = from.size() - 1; i >= 0; i--) {
            PathXTile tile = from.remove(i);

            // ONLY ADD IT IF IT'S NOT THERE ALREADY
            if (!to.contains(tile)) {
                to.add(tile);
            }
        }
    }

    /**
     * This method sets up and starts the animation shown after a game is won.
     */
    public void playWinAnimation() {
        // MAKE A NEW PATH
        ArrayList<Integer> winPath = new ArrayList();

        // THIS HAS THE APPROXIMATE PATH NODES, WHICH WE'LL SLIGHTLY
        // RANDOMIZE FOR EACH TILE FOLLOWING THE PATH.
        winPath.add(WIN_PATH_COORD);
        winPath.add(WINDOW_HEIGHT - WIN_PATH_COORD);
        winPath.add((WINDOW_WIDTH / 2) - 100);
        winPath.add(WIN_PATH_COORD);
        winPath.add(WINDOW_WIDTH - (WIN_PATH_COORD * 2));
        winPath.add(WINDOW_HEIGHT - WIN_PATH_COORD);
        winPath.add(WIN_PATH_COORD);
        winPath.add((WINDOW_HEIGHT / 2) - 100);
        winPath.add(WINDOW_WIDTH - (WIN_PATH_COORD * 2));
        winPath.add((WINDOW_HEIGHT / 2) - 100);

        moveAllTilesToStack();

        // START THE ANIMATION FOR ALL THE TILES
        for (int i = 0; i < stackTiles.size(); i++) {
            // GET EACH TILE
            PathXTile tile = stackTiles.get(i);

            // MAKE SURE IT'S MOVED EACH FRAME
            movingTiles.add(tile);

            // AND GET IT ON A PATH
            tile.initWinPath(winPath);
        }
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
            if (selectedTile != null) {
                selectedTile.setState(PathXTileState.VISIBLE_STATE.toString());
                selectedTile = null;
                selectedTileIndex = -1;
                miniGame.getAudio().play(pathXPropertyType.AUDIO_CUE_DESELECT_TILE.toString(), false);
            }
        } // IT'S IN THE GRID
        else {
            // SELECT THE TILE IF NONE IS SELECTED
            if (selectedTile == null) {
                selectedTile = tilesToSort.get(index);
                selectedTile.setState(PathXTileState.SELECTED_STATE.toString());
                selectedTileIndex = index;
                miniGame.getAudio().play(pathXPropertyType.AUDIO_CUE_SELECT_TILE.toString(), false);
            } // A TILE WAS ALREADY SELECTED, SO THIS MUST HAVE BEEN THE SECOND TILE
            // SELECTED, SO SWAP THEM
            else {
                //processSwap(index, selectedTileIndex);
            }
        }
    }

    /**
     * Called when the game is won, it will record the ending game time, update
     * the player record, display the win dialog, and play the win animation.
     */
    @Override
    public void endGameAsWin() {

        // UPDATE THE GAME STATE USING THE INHERITED FUNCTIONALITY
        super.endGameAsWin();

        // RECORD THE TIME IT TOOK TO COMPLETE THE GAME
        long gameTime = endTime.getTimeInMillis() - startTime.getTimeInMillis();

        // RECORD IT AS A WIN
        ((PathXMiniGame) miniGame).getPlayerRecord().addWin(currentLevel);

        // SAVE PLAYER DATA
        ((PathXMiniGame) miniGame).savePlayerRecord();

        // DISPLAY THE WIN DIALOG
        miniGame.getGUIDialogs().get(WIN_DIALOG_TYPE).setState(PathXTileState.VISIBLE_STATE.toString());

        // AND PLAY THE WIN ANIMATION
        playWinAnimation();

        // AND PLAY THE WIN AUDIO
        miniGame.getAudio().stop(pathXPropertyType.SONG_CUE_MENU_SCREEN.toString());
        miniGame.getAudio().stop(pathXPropertyType.SONG_CUE_GAME_SCREEN.toString());
        miniGame.getAudio().play(pathXPropertyType.AUDIO_CUE_WIN.toString(), false);
    }

    /**
     * Updates the player record, adding a game without a win.
     */
    public void endGameAsLoss() {
        // ADD A LOSS
        ((PathXMiniGame) miniGame).getPlayerRecord().addLoss(currentLevel);

        // SAVE PLAYER DATA
        ((PathXMiniGame) miniGame).savePlayerRecord();
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
            for (int i = 0; i < movingTiles.size(); i++) {
                // GET THE NEXT TILE
                PathXTile tile = movingTiles.get(i);

                // THIS WILL UPDATE IT'S POSITION USING ITS VELOCITY
                tile.update(game);

                // IF IT'S REACHED ITS DESTINATION, REMOVE IT
                // FROM THE LIST OF MOVING TILES
                if (!tile.isMovingToTarget()) {
                    movingTiles.remove(tile);
                }
            }

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
}
