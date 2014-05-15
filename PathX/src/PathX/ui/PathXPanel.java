package PathX.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.JPanel;
import mini_game.MiniGame;
import mini_game.Sprite;
import mini_game.SpriteType;
import mini_game.Viewport;
import properties_manager.PropertiesManager;
import PathX.data.PathXDataModel;
import static PathX.PathXConstants.*;
import PathX.PathX.pathXPropertyType;
import PathX.data.PathXRecord;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;

/**
 * This class performs all of the rendering for The Sorting Hat game
 * application.
 *
 * @author Richard McKenna & Eric Loo
 */
public class PathXPanel extends JPanel {
    // THIS IS ACTUALLY OUR Sorting Hat APP, WE NEED THIS
    // BECAUSE IT HAS THE GUI STUFF THAT WE NEED TO RENDER

    private MiniGame game;

    // AND HERE IS ALL THE GAME DATA THAT WE NEED TO RENDER
    private PathXDataModel data;

    // WE'LL USE THIS TO FORMAT SOME TEXT FOR DISPLAY PURPOSES
    private NumberFormat numberFormatter;

    // WE'LL USE THIS AS THE BASE IMAGE FOR RENDERING UNSELECTED TILES
    private BufferedImage blankTileImage;

    // WE'LL USE THIS AS THE BASE IMAGE FOR RENDERING SELECTED TILES
    private BufferedImage blankTileSelectedImage;

    // THIS IS FOR WHEN THE USE MOUSES OVER A TILE
    private BufferedImage blankTileMouseOverImage;

    /**
     * This constructor stores the game and data references, which we'll need
     * for rendering.
     *
     * @param initGame The Sorting Hat game that is using this panel for
     * rendering.
     *
     * @param initData The Sorting Hat game data.
     */
    public PathXPanel(MiniGame initGame, PathXDataModel initData) {
        game = initGame;
        data = initData;
        numberFormatter = NumberFormat.getNumberInstance();
        numberFormatter.setMinimumFractionDigits(3);
        numberFormatter.setMaximumFractionDigits(3);
    }

    // MUTATOR METHODS
    // -setBlankTileImage
    // -setBlankTileSelectedImage
    /**
     * This mutator method sets the base image to use for rendering tiles.
     *
     * @param initBlankTileImage The image to use as the base for rendering
     * tiles.
     */
    public void setBlankTileImage(BufferedImage initBlankTileImage) {
        blankTileImage = initBlankTileImage;
    }

    /**
     * This mutator method sets the base image to use for rendering selected
     * tiles.
     *
     * @param initBlankTileSelectedImage The image to use as the base for
     * rendering selected tiles.
     */
    public void setBlankTileSelectedImage(BufferedImage initBlankTileSelectedImage) {
        blankTileSelectedImage = initBlankTileSelectedImage;
    }

    public void setBlankTileMouseOverImage(BufferedImage initBlankTileMouseOverImage) {
        blankTileMouseOverImage = initBlankTileMouseOverImage;
    }

    /**
     * This is where rendering starts. This method is called each frame, and the
     * entire game application is rendered here with the help of a number of
     * helper methods.
     *
     * @param g The Graphics context for this panel.
     */
    @Override
    public void paintComponent(Graphics g) {
        try {
            // MAKE SURE WE HAVE EXCLUSIVE ACCESS TO THE GAME DATA
            game.beginUsingData();

            // CLEAR THE PANEL
            super.paintComponent(g);

            // RENDER THE BACKGROUND, WHICHEVER SCREEN WE'RE ON
            renderBackground(g);

            // ONLY RENDER THIS STUFF IF WE'RE ACTUALLY IN-GAME
            if (!data.notStarted()) {

                // AND THE DIALOGS, IF THERE ARE ANY
                renderDialogs(g);

            }

            // AND THE BUTTONS AND DECOR
            renderGUIControls(g);

            if (!data.notStarted()) {
                // AND THE TIME AND TILES STATS
                renderStats(g);
            }

            PointerInfo a = MouseInfo.getPointerInfo();
            Point b = a.getLocation();
            g.drawString((b.getX() - getLocationOnScreen().getX()) + ", " + (b.getY() - getLocationOnScreen().getY()), (int) b.getX() + 20, (int) b.getY() + 20);
            // AND FINALLY, TEXT FOR DEBUGGING
            renderDebuggingText(g);
            if (data.getGame().isIsOnLevelSelect()) {
                g.setFont(FONT_TEXT_DISPLAY);
                g.setColor(INT_OUTLINE_COLOR);
                g.drawString("$20,000", 480, 110);
                g.drawString("$" + data.getTotalMoney(), 520, 60);
            }
        } finally {
            // RELEASE THE LOCK
            game.endUsingData();
        }
    }

    // RENDERING HELPER METHODS
    // - renderBackground
    // - renderGUIControls
    // - renderSnake
    // - renderTiles
    // - renderDialogs
    // - renderGrid
    // - renderDebuggingText
    /**
     * `
     * Renders the background image, which is different depending on the screen.
     *
     * @param g the Graphics context of this panel.
     */
    public void renderBackground(Graphics g) {
        // THERE IS ONLY ONE CURRENTLY SET
        Sprite bg = game.getGUIDecor().get(BACKGROUND_TYPE);
        renderSprite(g, bg);
    }

    /**
     * Renders all the GUI decor and buttons.
     *
     * @param g this panel's rendering context.
     */
    public void renderGUIControls(Graphics g) {
        // GET EACH DECOR IMAGE ONE AT A TIME
        Collection<Sprite> decorSprites = game.getGUIDecor().values();
        for (Sprite s : decorSprites) {
            if (s.getSpriteType().getSpriteTypeID() != BACKGROUND_TYPE) {
                renderSprite(g, s);
            }
        }

        // AND NOW RENDER THE BUTTONS
        Collection<Sprite> buttonSprites = game.getGUIButtons().values();
        for (Sprite s : buttonSprites) {
            if (!s.getSpriteType().getSpriteTypeID().equals(LEVEL_GAME_TYPE)
                    && !s.getSpriteType().getSpriteTypeID().equals(PLAYER_TYPE)) {
                renderSprite(g, s);
            }
        }
    }

    /**
     * This method renders the on-screen stats that change as the game
     * progresses. This means things like the game time and the number of tiles
     * remaining.
     *
     * @param g the Graphics context for this panel
     */
    public void renderStats(Graphics g) {

        // RENDER THE GAME TIME AND THE TILES LEFT FOR IN-GAME
        if (((PathXMiniGame) game).isCurrentScreenState(GAME_SCREEN_STATE)
                && data.inProgress() || data.isPaused()) {
            g.setFont(FONT_TEXT_DISPLAY);
            g.setColor(Color.BLACK);
            // RENDER THE TIME
            String time = data.gameTimeToText();
            int x = TIME_X + TIME_OFFSET;
            int y = TIME_Y + TIME_TEXT_OFFSET;
            g.drawString(time, x, y);

            // Render the Miscasts
            x = TILE_COUNT_X + TILE_COUNT_OFFSET;
            y = TILE_COUNT_Y + TILE_TEXT_OFFSET;
            //g.drawString(Integer.toString(data.getBadSpellsCounter()), x, y);

        }

        // IF THE STATS DIALOG IS VISIBLE, ADD THE TEXTUAL STATS
        if (game.getGUIDialogs().get(STATS_DIALOG_TYPE).getState().equals(PathXTileState.VISIBLE_STATE.toString())) {
            g.setFont(FONT_STATS);
            g.setColor(COLOR_STATS);

        }
    }

    /**
     * Helper method for rendering the tiles that are currently moving.
     *
     * @param g Rendering context for this panel.
     *
     * @param tileToRender Tile to render to this panel.
     */
    public void renderTile(Graphics g, PathXTile tileToRender) {
        // ONLY RENDER VISIBLE TILES
        if (!tileToRender.getState().equals(PathXTileState.INVISIBLE_STATE.toString())) {
            Viewport viewport = data.getViewport();
            int correctedTileX = (int) (tileToRender.getX());
            int correctedTileY = (int) (tileToRender.getY());

            // THEN THE TILE IMAGE
            SpriteType bgST = tileToRender.getSpriteType();
            Image img = bgST.getStateImage(tileToRender.getState());
            g.drawImage(img, correctedTileX,
                    correctedTileY,
                    bgST.getWidth(), bgST.getHeight(), null);
        }
    }

    /**
     * Renders the game dialog boxes.
     *
     * @param g This panel's graphics context.
     */
    public void renderDialogs(Graphics g) {
        // GET EACH DECOR IMAGE ONE AT A TIME
        Collection<Sprite> dialogSprites = game.getGUIDialogs().values();
        for (Sprite s : dialogSprites) {
            // RENDER THE DIALOG, NOTE IT WILL ONLY DO IT IF IT'S VISIBLE
            renderSprite(g, s);
        }
    }

    /**
     * Renders the s Sprite into the Graphics context g. Note that each Sprite
     * knows its own x,y coordinate location.
     *
     * @param g the Graphics context of this panel
     *
     * @param s the Sprite to be rendered
     */
    public void renderSprite(Graphics g, Sprite s) {
        // ONLY RENDER THE VISIBLE ONES
        if (!s.getState().equals(PathXTileState.INVISIBLE_STATE.toString())) {
            SpriteType bgST = s.getSpriteType();
            Image img = bgST.getStateImage(s.getState());
            g.drawImage(img, (int) s.getX(), (int) s.getY(), bgST.getWidth(), bgST.getHeight(), null);
        }
    }

    /**
     * Renders the debugging text to the panel. Note that the rendering will
     * only actually be done if data has activated debug text rendering.
     *
     * @param g the Graphics context for this panel
     */
    public void renderDebuggingText(Graphics g) {
        // IF IT'S ACTIVATED
        if (data.isDebugTextRenderingActive()) {
            // ENABLE PROPER RENDER SETTINGS
            g.setFont(FONT_DEBUG_TEXT);
            g.setColor(COLOR_DEBUG_TEXT);

            // GO THROUGH ALL THE DEBUG TEXT
            Iterator<String> it = data.getDebugText().iterator();
            int x = data.getDebugTextX();
            int y = data.getDebugTextY();
            while (it.hasNext()) {
                // RENDER THE TEXT
                String text = it.next();
                g.drawString(text, x, y);
                y += 20;
            }
        }
    }
}
