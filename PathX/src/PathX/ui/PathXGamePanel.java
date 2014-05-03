/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import PathX.data.Intersection;
import PathX.data.PathXRecord;
import PathX.data.Road;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.util.HashMap;

/**
 * This class performs all of the rendering for PATHX game application.
 *
 * @author Richard McKenna & Eric Loo
 */
public class PathXGamePanel extends JPanel {

    private MiniGame game;

    // AND HERE IS ALL THE GAME DATA THAT WE NEED TO RENDER
    private PathXDataModel model;

    // WE'LL USE THIS TO FORMAT SOME TEXT FOR DISPLAY PURPOSES
    private NumberFormat numberFormatter;

    // WE'LL USE THIS AS THE BASE IMAGE FOR RENDERING UNSELECTED TILES
    private BufferedImage blankTileImage;

    // WE'LL USE THIS AS THE BASE IMAGE FOR RENDERING SELECTED TILES
    private BufferedImage blankTileSelectedImage;

    // THIS IS FOR WHEN THE USE MOUSES OVER A TILE
    private BufferedImage blankTileMouseOverImage;

    private String renderedBackground;

    public Viewport viewport;

    // WE'LL RECYCLE THESE DURING RENDERING
    Ellipse2D.Double recyclableCircle;
    Line2D.Double recyclableLine;
    HashMap<Integer, BasicStroke> recyclableStrokes;
    int triangleXPoints[] = {-ONE_WAY_TRIANGLE_WIDTH / 2, -ONE_WAY_TRIANGLE_WIDTH / 2, ONE_WAY_TRIANGLE_WIDTH / 2};
    int triangleYPoints[] = {ONE_WAY_TRIANGLE_WIDTH / 2, -ONE_WAY_TRIANGLE_WIDTH / 2, 0};
    GeneralPath recyclableTriangle;

    /**
     * This constructor stores the game and data references, which we'll need
     * for rendering.
     *
     * @param initGame The Sorting Hat game that is using this panel for
     * rendering.
     *
     */
    public PathXGamePanel(MiniGame initGame, PathXDataModel m) {
        game = initGame;
        model = m;
        viewport = model.getViewport();
        numberFormatter = NumberFormat.getNumberInstance();
        numberFormatter.setMinimumFractionDigits(3);
        numberFormatter.setMaximumFractionDigits(3);
        renderedBackground = BACKGROUND_GAME_TYPE;

        // MAKE THE RENDER OBJECTS TO BE RECYCLED
        recyclableCircle = new Ellipse2D.Double(0, 0, INTERSECTION_RADIUS * 2, INTERSECTION_RADIUS * 2);
        recyclableLine = new Line2D.Double(0, 0, 0, 0);
        recyclableStrokes = new HashMap();
        for (int i = 1; i <= 10; i++) {
            recyclableStrokes.put(i, new BasicStroke(i * 2));
        }

        // MAKING THE TRIANGLE FOR ONE WAY STREETS IS A LITTLE MORE INVOLVED
        recyclableTriangle = new GeneralPath(GeneralPath.WIND_EVEN_ODD,
                triangleXPoints.length);
        recyclableTriangle.moveTo(triangleXPoints[0], triangleYPoints[0]);
        for (int index = 1; index < triangleXPoints.length; index++) {
            recyclableTriangle.lineTo(triangleXPoints[index], triangleYPoints[index]);
        };
        recyclableTriangle.closePath();
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

            Graphics2D g2 = (Graphics2D) g;
            // CLEAR THE PANEL
            super.paintComponent(g);

            // RENDER THE BACKGROUND, WHICHEVER SCREEN WE'RE ON
            renderBackground(g2, renderedBackground);
            if (model.getLoadedLevel()) {
                // RENDER THE ROADS
                renderRoads(g2);

                // RENDER THE INTERSECTIONS
                renderIntersections(g2);
            }
            if (game.getGUIDecor().get(BACKGROUND_TYPE).getState().equals(GAME_SCREEN_STATE)) {
                renderGUIControls(g2);
            } else {
                renderGUIControls2(g2);
            }

            // AND THE BUTTONS AND DECOR
        } finally {
            // RELEASE THE LOCK
            game.endUsingData();
        }
    }

    public void setRenderedBackground(String toRender) {
        renderedBackground = toRender;
    }

    public String getRenderedBackground() {
        return renderedBackground;
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
     * @param toRender the background to render
     */
    public void renderBackground(Graphics g, String toRender) {
        // THERE IS ONLY ONE CURRENTLY SET
        Sprite bg = game.getGUIDecor().get(toRender);
        if (toRender.equals(BACKGROUND_GAME_TYPE)) {
            renderSprite(g, bg);
        } else {
            renderSprite2(g, bg);
        }
    }

    public void renderGUIControls(Graphics g) {
        // AND NOW RENDER THE BUTTONS
        Collection<Sprite> buttonSprites = game.getGUIButtons().values();
        for (Sprite s : buttonSprites) {
            if (s.getSpriteType().getSpriteTypeID().equals(LEVEL_GAME_TYPE)) {
                renderSprite2(g, s);
            }
        }

    }

    public void renderGUIControls2(Graphics g) {
        // AND NOW RENDER THE BUTTONS
        Collection<Sprite> buttonSprites = game.getGUIButtons().values();
        for (Sprite s : buttonSprites) {
            if (s.getSpriteType().getSpriteTypeID().equals(LEVEL_GAME_TYPE)) {
                renderSprite3(g, s);
            } else if (s.getSpriteType().getSpriteTypeID().equals(PLAYER_TYPE)) {
                renderPlayer(g, s);
            }
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
           g.drawImage(img, (int) s.getX() - 1800, (int) s.getY() - 400, null);
        }
    }

    public void renderSprite2(Graphics g, Sprite s) {
        // ONLY RENDER THE VISIBLE ONES
        if (!s.getState().equals(PathXTileState.INVISIBLE_STATE.toString())) {
            SpriteType bgST = s.getSpriteType();
            Image img = bgST.getStateImage(s.getState());
            g.drawImage(img, (int) s.getX() - 17, (int) s.getY() - 129, null);
        }
    }

    public void renderSprite3(Graphics g, Sprite s) {
        // ONLY RENDER THE VISIBLE ONES
        if (!s.getState().equals(PathXTileState.INVISIBLE_STATE.toString())) {
            SpriteType bgST = s.getSpriteType();
            Image img = bgST.getStateImage(s.getState());
            g.drawImage(img, (int) s.getX() - 274, (int) s.getY() - 14, null);
        }
    }

    public void renderPlayer(Graphics g, Sprite s) {
        // ONLY RENDER THE VISIBLE ONES
        if (!s.getState().equals(PathXTileState.INVISIBLE_STATE.toString())) {
            SpriteType bgST = s.getSpriteType();
            Image img = bgST.getStateImage(s.getState());

            Image startImage = model.getStartingLocationImage();
            Intersection startInt = model.getStartingLocation();

            int w = startImage.getWidth(null);
            int h = startImage.getHeight(null);
            int x1 = startInt.x - (w / 2);
            int y1 = startInt.y - (h / 2);

            g.drawImage(img, x1 - viewport.getViewportX(), y1 - viewport.getViewportY(), null);
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
        if (model.isDebugTextRenderingActive()) {
            // ENABLE PROPER RENDER SETTINGS
            g.setFont(FONT_DEBUG_TEXT);
            g.setColor(COLOR_DEBUG_TEXT);

            // GO THROUGH ALL THE DEBUG TEXT
            Iterator<String> it = model.getDebugText().iterator();
            int x = model.getDebugTextX();
            int y = model.getDebugTextY();
            while (it.hasNext()) {
                // RENDER THE TEXT
                String text = it.next();
                g.drawString(text, x, y);
                y += 20;
            }
        }
    }

    // HELPER METHOD FOR RENDERING THE LEVEL ROADS
    private void renderRoads(Graphics2D g2) {
        // GO THROUGH THE ROADS AND RENDER ALL OF THEM
        Viewport viewport = model.getViewport();
        Iterator<Road> it = model.roadsIterator();
        g2.setStroke(recyclableStrokes.get(INT_STROKE));
        while (it.hasNext()) {
            Road road = it.next();
            if (!model.isSelectedRoad(road)) {
                renderRoad(g2, road, INT_OUTLINE_COLOR);
            }
        }

        // AND RENDER THE SELECTED ONE, IF THERE IS ONE
        Road selectedRoad = model.getSelectedRoad();
        if (selectedRoad != null) {
            renderRoad(g2, selectedRoad, HIGHLIGHTED_COLOR);
        }
    }

    // HELPER METHOD FOR RENDERING A SINGLE ROAD
    private void renderRoad(Graphics2D g2, Road road, Color c) {
        g2.setColor(c);
        int strokeId = road.getSpeedLimit() / 10;

        // CLAMP THE SPEED LIMIT STROKE
        if (strokeId < 1) {
            strokeId = 1;
        }
        if (strokeId > 10) {
            strokeId = 10;
        }
        g2.setStroke(recyclableStrokes.get(strokeId));

        // LOAD ALL THE DATA INTO THE RECYCLABLE LINE
        recyclableLine.x1 = road.getNode1().x - viewport.getViewportX();
        recyclableLine.y1 = road.getNode1().y - viewport.getViewportY();
        recyclableLine.x2 = road.getNode2().x - viewport.getViewportX();
        recyclableLine.y2 = road.getNode2().y - viewport.getViewportY();

        // AND DRAW IT
        g2.draw(recyclableLine);

        // AND IF IT'S A ONE WAY ROAD DRAW THE MARKER
        if (road.isOneWay()) {
            this.renderOneWaySignalsOnRecyclableLine(g2);
        }
    }

    // HELPER METHOD FOR RENDERING AN INTERSECTION
    private void renderIntersections(Graphics2D g2) {
        Iterator<Intersection> it = model.intersectionsIterator();
        while (it.hasNext()) {
            Intersection intersection = it.next();

            // ONLY RENDER IT THIS WAY IF IT'S NOT THE START OR DESTINATION
            // AND IT IS IN THE VIEWPORT
            if ((!model.isStartingLocation(intersection))
                    && (!model.isDestination(intersection))) {
                // FIRST FILL
                if (intersection.isOpen()) {
                    g2.setColor(OPEN_INT_COLOR);
                } else {
                    g2.setColor(CLOSED_INT_COLOR);
                }
                recyclableCircle.x = intersection.x - viewport.getViewportX() - INTERSECTION_RADIUS;
                recyclableCircle.y = intersection.y - viewport.getViewportY() - INTERSECTION_RADIUS;
                g2.fill(recyclableCircle);

                // AND NOW THE OUTLINE
                if (model.isSelectedIntersection(intersection)) {
                    g2.setColor(HIGHLIGHTED_COLOR);
                } else {
                    g2.setColor(INT_OUTLINE_COLOR);
                }
                Stroke s = recyclableStrokes.get(INT_STROKE);
                g2.setStroke(s);
                g2.draw(recyclableCircle);
            }
        }

        // AND NOW RENDER THE START AND DESTINATION LOCATIONS
        Image startImage = model.getStartingLocationImage();
        Intersection startInt = model.getStartingLocation();
        renderIntersectionImage(g2, startImage, startInt);

        Image destImage = model.getDesinationImage();
        Intersection destInt = model.getDestination();
        renderIntersectionImage(g2, destImage, destInt);
    }

    // HELPER METHOD FOR RENDERING AN IMAGE AT AN INTERSECTION, WHICH IS
    // NEEDED BY THE STARTING LOCATION AND THE DESTINATION
    private void renderIntersectionImage(Graphics2D g2, Image img, Intersection i) {
        // CALCULATE WHERE TO RENDER IT
        int w = img.getWidth(null);
        int h = img.getHeight(null);
        int x1 = i.x - (w / 2);
        int y1 = i.y - (h / 2);
        int x2 = x1 + img.getWidth(null);
        int y2 = y1 + img.getHeight(null);
        g2.drawImage(img, x1 - viewport.getViewportX(), y1 - viewport.getViewportY(), null);
        // ONLY RENDER IF INSIDE THE VIEWPORT
        //if (viewport.isRectInsideViewport(x1, y1, x2, y2)) {
        //   g2.drawImage(img, x1 - viewport.getViewportX(), y1 - viewport.getViewportY(), null);
        //}
    }

    // YOU'LL LIKELY AT THE VERY LEAST WANT THIS ONE. IT RENDERS A NICE
    // LITTLE POINTING TRIANGLE ON ONE-WAY ROADS
    private void renderOneWaySignalsOnRecyclableLine(Graphics2D g2) {
        // CALCULATE THE ROAD LINE SLOPE
        double diffX = recyclableLine.x2 - recyclableLine.x1;
        double diffY = recyclableLine.y2 - recyclableLine.y1;
        double slope = diffY / diffX;

        // AND THEN FIND THE LINE MIDPOINT
        double midX = 280 + ((recyclableLine.x1 + recyclableLine.x2) / 2.0);
        double midY = 18 + ((recyclableLine.y1 + recyclableLine.y2) / 2.0);

        // GET THE RENDERING TRANSFORM, WE'LL RETORE IT BACK
        // AT THE END
        AffineTransform oldAt = g2.getTransform();

        // CALCULATE THE ROTATION ANGLE
        double theta = Math.atan(slope);
        if (recyclableLine.x2 < recyclableLine.x1) {
            theta = (theta + Math.PI);
        }

        // MAKE A NEW TRANSFORM FOR THIS TRIANGLE AND SET IT
        // UP WITH WHERE WE WANT TO PLACE IT AND HOW MUCH WE
        // WANT TO ROTATE IT
        AffineTransform at = new AffineTransform();
        at.setToIdentity();
        at.translate(midX, midY);
        at.rotate(theta);
        g2.setTransform(at);

        // AND RENDER AS A SOLID TRIANGLE
        g2.fill(recyclableTriangle);

        // RESTORE THE OLD TRANSFORM SO EVERYTHING DOESN'T END UP ROTATED 0
        g2.setTransform(oldAt);
    }

}
