package pathX.level_editor.view;

import java.awt.BasicStroke;
import java.awt.Color;
import pathX.level_editor.model.Viewport;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.JPanel;
import pathX.level_editor.model.Intersection;
import pathX.level_editor.model.PXLE_Model;
import static pathX.level_editor.PXLE_Constants.*;
import pathX.level_editor.model.Road;

/**
 * This class does all the rendering for the level editor.
 * 
 * @author Richard McKenna
 */
public class PXLE_Canvas extends JPanel
{
    // HAS THE DATA TO BE RENDERED AND THE RENDER SETTINGS
    PXLE_Model model;
    
    // MANAGES PORTION OF LEVEL TO RENDER
    Viewport viewport;

    // WE'LL RECYCLE THESE DURING RENDERING
    Ellipse2D.Double recyclableCircle;
    Line2D.Double recyclableLine;
    HashMap<Integer, BasicStroke> recyclableStrokes;
    int triangleXPoints[] = {-ONE_WAY_TRIANGLE_WIDTH/2,  -ONE_WAY_TRIANGLE_WIDTH/2,  ONE_WAY_TRIANGLE_WIDTH/2};
    int triangleYPoints[] = {ONE_WAY_TRIANGLE_WIDTH/2, -ONE_WAY_TRIANGLE_WIDTH/2, 0};
    GeneralPath recyclableTriangle;
    
    /**
     * Constructor prepares for rendering.
     */
    public PXLE_Canvas(PXLE_Model initModel)
    {
        // KEEP THESE FOR LATER
        model = initModel;
        viewport = model.getViewport();

        // MAKE THE RENDER OBJECTS TO BE RECYCLED
        recyclableCircle = new Ellipse2D.Double(0, 0, INTERSECTION_RADIUS * 2, INTERSECTION_RADIUS * 2);
        recyclableLine = new Line2D.Double(0,0,0,0);
        recyclableStrokes = new HashMap();
        for (int i = 1; i <= 10; i++)
        {
            recyclableStrokes.put(i, new BasicStroke(i*2));
        }
        
        // MAKING THE TRIANGLE FOR ONE WAY STREETS IS A LITTLE MORE INVOLVED
        recyclableTriangle =  new GeneralPath(   GeneralPath.WIND_EVEN_ODD,
                                                triangleXPoints.length);
        recyclableTriangle.moveTo(triangleXPoints[0], triangleYPoints[0]);
        for (int index = 1; index < triangleXPoints.length; index++) 
        {
            recyclableTriangle.lineTo(triangleXPoints[index], triangleYPoints[index]);
        };
        recyclableTriangle.closePath();
    }

    /**
     * Here's where all rendering happens.
     */
    @Override
    public void paintComponent(Graphics g)
    {
        // WE'LL USE THE Graphics2D FEATURES, WHICH IS 
        // THE ACTUAL TYPE OF THE g OBJECT
        Graphics2D g2 = (Graphics2D) g;

        // FIRST CLEAR ALL THE BACKGROUND
        super.paintComponent(g);

        // MAKE SURE THE CANVAS HAS BEEN SIZED
        if (getWidth() > 0)
        {
            viewport.width = getWidth();
            viewport.height = getHeight();
        }
        // IT HASN'T BEEN SIZED YET, SO JUMP OUT
        else
        {
            return;
        }

        // MAKE SURE WE'VE STARTED EDITING
        if (model.isLevelBeingEdited())
        {
            // RENDER THE BACKGROUND IMAGE
            renderLevelBackground(g2);

            // RENDER THE ROADS
            renderRoads(g2);

            // RENDER THE INTERSECTIONS
            renderIntersections(g2);

            // RENDERING STATS CAN HELP FIGURE OUT WHAT'S GOING ON
            renderStats(g2);
        }
    }

    // HELPER METHOD FOR RENDERING THE LEVEL BACKGROUND
    private void renderLevelBackground(Graphics2D g2)
    {
        Image backgroundImage = model.getBackgroundImage();
        g2.drawImage(backgroundImage, 0, 0, viewport.width, viewport.height, viewport.x, viewport.y, viewport.x + viewport.width, viewport.y + viewport.height, null);
    }

    // HELPER METHOD FOR RENDERING THE LEVEL ROADS
    private void renderRoads(Graphics2D g2)
    {
        // GO THROUGH THE ROADS AND RENDER ALL OF THEM
        Viewport viewport = model.getViewport();
        Iterator<Road> it = model.roadsIterator();
        g2.setStroke(recyclableStrokes.get(INT_STROKE));
        while (it.hasNext())
        {
            Road road = it.next();
            if (!model.isSelectedRoad(road))
                renderRoad(g2, road, INT_OUTLINE_COLOR);
        }
        
        // NOW DRAW THE LINE BEING ADDED, IF THERE IS ONE
        if (model.isAddingRoadEnd())
        {
            Intersection startRoadIntersection = model.getStartRoadIntersection();
            recyclableLine.x1 = startRoadIntersection.x-viewport.x;
            recyclableLine.y1 = startRoadIntersection.y-viewport.y;
            recyclableLine.x2 = model.getLastMouseX()-viewport.x;
            recyclableLine.y2 = model.getLastMouseY()-viewport.y;
            g2.draw(recyclableLine);
        }

        // AND RENDER THE SELECTED ONE, IF THERE IS ONE
        Road selectedRoad = model.getSelectedRoad();
        if (selectedRoad != null)
        {
            renderRoad(g2, selectedRoad, HIGHLIGHTED_COLOR);
        }
    }
    
    // HELPER METHOD FOR RENDERING A SINGLE ROAD
    private void renderRoad(Graphics2D g2, Road road, Color c)
    {
        g2.setColor(c);
        int strokeId = road.getSpeedLimit()/10;

        // CLAMP THE SPEED LIMIT STROKE
        if (strokeId < 1) strokeId = 1;
        if (strokeId > 10) strokeId = 10;
        g2.setStroke(recyclableStrokes.get(strokeId));

        // LOAD ALL THE DATA INTO THE RECYCLABLE LINE
        recyclableLine.x1 = road.getNode1().x-viewport.x;
        recyclableLine.y1 = road.getNode1().y-viewport.y;
        recyclableLine.x2 = road.getNode2().x-viewport.x;
        recyclableLine.y2 = road.getNode2().y-viewport.y;

        // AND DRAW IT
        g2.draw(recyclableLine);
        
        // AND IF IT'S A ONE WAY ROAD DRAW THE MARKER
        if (road.isOneWay())
        {
            this.renderOneWaySignalsOnRecyclableLine(g2);
        }
    }

    // HELPER METHOD FOR RENDERING AN INTERSECTION
    private void renderIntersections(Graphics2D g2)
    {
        Iterator<Intersection> it = model.intersectionsIterator();
        while (it.hasNext())
        {
            Intersection intersection = it.next();

            // ONLY RENDER IT THIS WAY IF IT'S NOT THE START OR DESTINATION
            // AND IT IS IN THE VIEWPORT
            if ((!model.isStartingLocation(intersection))
                    && (!model.isDestination(intersection))
                    && viewport.isCircleBoundingBoxInsideViewport(intersection.x, intersection.y, INTERSECTION_RADIUS))
            {
                // FIRST FILL
                if (intersection.isOpen())
                {
                    g2.setColor(OPEN_INT_COLOR);
                } else
                {
                    g2.setColor(CLOSED_INT_COLOR);
                }
                recyclableCircle.x = intersection.x - viewport.x - INTERSECTION_RADIUS;
                recyclableCircle.y = intersection.y - viewport.y - INTERSECTION_RADIUS;
                g2.fill(recyclableCircle);

                // AND NOW THE OUTLINE
                if (model.isSelectedIntersection(intersection))
                {
                    g2.setColor(HIGHLIGHTED_COLOR);
                } else
                {
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
    private void renderIntersectionImage(Graphics2D g2, Image img, Intersection i)
    {
        // CALCULATE WHERE TO RENDER IT
        int w = img.getWidth(null);
        int h = img.getHeight(null);
        int x1 = i.x-(w/2);
        int y1 = i.y-(h/2);
        int x2 = x1 + img.getWidth(null);
        int y2 = y1 + img.getHeight(null);
        
        // ONLY RENDER IF INSIDE THE VIEWPORT
        if (viewport.isRectInsideViewport(x1, y1, x2, y2))
        {
            g2.drawImage(img, x1 - viewport.x, y1 - viewport.y, null);
        }        
    }

    // HELPER METHOD FOR RENDERING SOME SCREEN STATS, WHICH CAN
    // HELP WITH DEBUGGING
    private void renderStats(Graphics2D g2)
    {
        Viewport viewport = model.getViewport();
        g2.setColor(STATS_TEXT_COLOR);
        g2.setFont(STATS_TEXT_FONT);
        g2.drawString(MOUSE_SCREEN_POSITION_TITLE + model.getLastMouseX() + ", " + model.getLastMouseY(),
                STATS_X, MOUSE_SCREEN_POSITION_Y);
        int levelMouseX = model.getLastMouseX() + viewport.x;
        int levelMouseY = model.getLastMouseY() + viewport.y;
        g2.drawString(MOUSE_LEVEL_POSITION_TITLE + levelMouseX + ", " + levelMouseY,
                STATS_X, MOUSE_LEVEL_POSITION_Y);
        g2.drawString(VIEWPORT_POSITION_TITLE + viewport.x + ", " + viewport.y,
                STATS_X, VIEWPORT_POSITION_Y);
    }
    
    // YOU'LL LIKELY AT THE VERY LEAST WANT THIS ONE. IT RENDERS A NICE
    // LITTLE POINTING TRIANGLE ON ONE-WAY ROADS
    private void renderOneWaySignalsOnRecyclableLine(Graphics2D g2)
    {
        // CALCULATE THE ROAD LINE SLOPE
        double diffX = recyclableLine.x2 - recyclableLine.x1;
        double diffY = recyclableLine.y2 - recyclableLine.y1;
        double slope = diffY/diffX;
        
        // AND THEN FIND THE LINE MIDPOINT
        double midX = (recyclableLine.x1 + recyclableLine.x2)/2.0;
        double midY = (recyclableLine.y1 + recyclableLine.y2)/2.0;
        
        // GET THE RENDERING TRANSFORM, WE'LL RETORE IT BACK
        // AT THE END
        AffineTransform oldAt = g2.getTransform();
        
        // CALCULATE THE ROTATION ANGLE
        double theta = Math.atan(slope);
        if (recyclableLine.x2 < recyclableLine.x1)
            theta = (theta + Math.PI);
        
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