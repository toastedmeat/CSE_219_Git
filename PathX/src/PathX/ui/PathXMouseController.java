/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PathX.ui;

import PathX.data.Intersection;
import PathX.data.PathXDataModel;
import PathX.data.PathXEditMode;
import PathX.data.Road;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JPanel;

/**
 *
 * @author Eric
 */
public class PathXMouseController implements MouseListener, MouseMotionListener
{
    // WE'LL NEED TO UPDATE THE MODEL BASED ON THE INTERSECTIONS
    PathXDataModel model;
    
    /**
     * Constructor for initializing this controller. It will update the
     * model (i.e. the app data), so it needs a reference to it.
     */
    public PathXMouseController(PathXDataModel initModel)
    {
        // KEEP THIS TO USE WHEN THE INTERACTIONS OCCUR
        model = initModel;
    }

    /**
     * Responds to when the user presses the mouse button on the canvas,
     * it may respond in a few different ways depending on what the 
     * current edit mode is.
     */
    @Override
    public void mousePressed(MouseEvent me)
    {
        // MAKE SURE THE CANVAS HAS FOCUS SO THAT IT
        // WILL PROCESS THE PROPER KEY PRESSES
        ((JPanel)me.getSource()).requestFocusInWindow();
        
        // THESE ARE CANVAS COORDINATES
        int canvasX = me.getX();
        int canvasY = me.getY();
        
        // IF WE ARE IN ONE OF THESE MODES WE MAY WANT TO SELECT
        // ANOTHER INTERSECTION ROAD
        if (model.isNothingSelected()
                || model.isIntersectionSelected()
                || model.isRoadSelected())
        {
            // CHECK TO SEE IF THE USER IS SELECTING AN INTERSECTION
            Intersection i = model.findIntersectionAtCanvasLocation(canvasX, canvasY);
            if (i != null)
            {
                // MAKE THIS THE SELECTED INTERSECTION
                model.setSelectedIntersection(i);
                model.switchEditMode(PathXEditMode.INTERSECTION_DRAGGED);
                return;
            }                      
            
            // IF NO INTERSECTION WAS SELECTED THEN CHECK TO SEE IF 
            // THE USER IS SELECTING A ROAD
            Road r = model.selectRoadAtCanvasLocation(canvasX, canvasY);
            if (r != null)
            {
                // MAKE THIS THE SELECTED ROAD
                model.setSelectedRoad(r);
                model.switchEditMode(PathXEditMode.ROAD_SELECTED);
                return;
            }

            // OTHERWISE DESELECT EVERYTHING
            model.unselectEverything();            
        }
    }
    
    /**
     * This method is called when the user releases the mouse button
     * over the canvas. This will end intersection dragging if that is
     * currently happening.
     */
    @Override
    public void mouseReleased(MouseEvent me)
    {
        // IF WE ARE CURRENTLY DRAGGING AN INTERSECTION
        PathXEditMode editMode = model.getEditMode();
        if (editMode == PathXEditMode.INTERSECTION_DRAGGED)
        {
            // RELEASE IT, BUT KEEP IT AS THE SELECTED ITEM
            model.switchEditMode(PathXEditMode.INTERSECTION_SELECTED);
        }
    }

    /**
     * This method will be used to respond to right-button mouse clicks
     * on intersections so that we can toggle them open or closed.
     */
    @Override
    public void mouseClicked(MouseEvent me)
    {
        // RIGHT MOUSE BUTTON IS TO TOGGLE OPEN/CLOSE INTERSECTION
        if (me.getButton() == MouseEvent.BUTTON3)
        {
            // SEE IF WE CLICKED ON AN INTERSECTION
            Intersection i = model.findIntersectionAtCanvasLocation(me.getX(), me.getY());
            if (i != null)
            {
                // TOGGLE THE INTERSECTION
                i.toggleOpen();
                model.switchEditMode(PathXEditMode.NOTHING_SELECTED);
            }            
        }
    }

    /**
     * This method is called every time the user moves the mouse. We
     * use this to keep track of where the mouse is at all times on
     * the canvas, which helps us render the road being added after
     * the user has selected the first intersection.
     */
    @Override
    public void mouseMoved(MouseEvent me)
    {
        // UPDATE THE POSITION
        model.setLastMousePosition(me.getX(), me.getY());
    }
    
    /**
     * This function is called when we drag the mouse across the canvas with
     * the mouse button pressed. We use this to drag items intersections
     * across the canvas.
     */
    @Override
    public void mouseDragged(MouseEvent me) {}
    
    // WE WON'T BE USING THESE METHODS, BUT NEED TO OVERRIDE
    // THEM TO KEEP THE COMPILER HAPPY
    @Override
    public void mouseEntered(MouseEvent me)     {}    
    public void mouseExited(MouseEvent me)      {}    
}