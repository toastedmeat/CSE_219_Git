package pathX.level_editor.model;

import java.awt.Image;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Iterator;
import pathX.level_editor.view.PXLE_View;
import static pathX.level_editor.PXLE_Constants.*;

/**
 * This class manages the data associated with the pathX level editor app. Note
 * that all the data associated with a given level that needs to be saved to
 * the XML file is inside the PXLE_Level file, and that this model class has
 * the level object currently being edited.
 *
 * @author Richard McKenna
 */
public class PXLE_Model
{
    // THIS IS THE LEVEL CURRENTLY BEING EDITING
    PXLE_Level level;
    
    // USED TO MANAGE WHAT THE USER IS CURRENTLY EDITING
    PXLE_EditMode editMode;

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
    
    // WE'LL USE THIS WHEN WE'RE ADDING A NEW ROAD
    Intersection startRoadIntersection;

    // IN CASE WE WANT TO TRACK MOVEMENTS
    int lastMouseX;
    int lastMouseY;    
    
    // THESE BOOLEANS HELP US KEEP TRACK OF
    // @todo DO WE NEED THESE?
    boolean isMousePressed;
    boolean isDragging;
    boolean dataUpdatedSinceLastSave;

    // THIS IS THE UI, WE'LL NOTIFY IT WHENEVER THE DATA CHANGES SO
    // THAT THE UI RENDERING CAN BE UPDATED AT THAT TIME
    PXLE_View view;
    
    /**
     * Default the constructor, it initializes the empty level
     * and all the data needed to start editing.
     */
    public PXLE_Model()
    {
        level = new PXLE_Level();
        editMode = PXLE_EditMode.NOTHING_SELECTED;
        viewport = new Viewport();
        levelBeingEdited = false;
        startRoadIntersection = null;
    }

    // ACCESSOR METHODS
    public PXLE_Level       getLevel()                  {   return level;                   }
    public PXLE_View        getView()                   {   return view;                    }
    public Viewport         getViewport()               {   return viewport;                }
    public PXLE_EditMode    getEditMode()               {   return editMode;                }
    public boolean          isLevelBeingEdited()        {   return levelBeingEdited;        }
    public Image            getBackgroundImage()        {   return backgroundImage;         }
    public Image            getStartingLocationImage()  {   return startingLocationImage;   }
    public Image            getDesinationImage()        {   return destinationImage;        }
    public Intersection     getSelectedIntersection()   {   return selectedIntersection;    }
    public Road             getSelectedRoad()           {   return selectedRoad;            }
    public Intersection     getStartRoadIntersection()  {   return startRoadIntersection;   }
    public int              getLastMouseX()             {   return lastMouseX;              }
    public int              getLastMouseY()             {   return lastMouseY;              }
    public Intersection     getStartingLocation()       {   return level.startingLocation;  }
    public Intersection     getDestination()            {   return level.destination;       }
    public boolean          isDataUpdatedSinceLastSave(){   return dataUpdatedSinceLastSave;}    
    public boolean          isStartingLocation(Intersection testInt)  
    {   return testInt == level.startingLocation;           }
    public boolean isDestination(Intersection testInt)
    {   return testInt == level.destination;                }
    public boolean isSelectedIntersection(Intersection testIntersection)
    {   return testIntersection == selectedIntersection;    }
    public boolean isSelectedRoad(Road testRoad)
    {   return testRoad == selectedRoad;                    }

    // ITERATOR METHODS FOR GOING THROUGH THE GRAPH

    public Iterator intersectionsIterator()
    {
        ArrayList<Intersection> intersections = level.getIntersections();
        return intersections.iterator();
    }
    public Iterator roadsIterator()
    {
        ArrayList<Road> roads = level.roads;
        return roads.iterator();
    }
    
    // THESE ARE FOR TESTING WHAT EDIT MODE THE APP CURRENTLY IS IN
    public boolean isNothingSelected()      { return editMode == PXLE_EditMode.NOTHING_SELECTED; }
    public boolean isIntersectionSelected() { return editMode == PXLE_EditMode.INTERSECTION_SELECTED; }
    public boolean isIntersectionDragged()  { return editMode == PXLE_EditMode.INTERSECTION_DRAGGED; }
    public boolean isRoadSelected()         { return editMode == PXLE_EditMode.ROAD_SELECTED; }
    public boolean isAddingIntersection()   { return editMode == PXLE_EditMode.ADDING_INTERSECTION; }
    public boolean isAddingRoadStart()      { return editMode == PXLE_EditMode.ADDING_ROAD_START; }
    public boolean isAddingRoadEnd()        { return editMode == PXLE_EditMode.ADDING_ROAD_END; }
       
    // MUTATOR METHODS

    public void setView(PXLE_View initView)
    {   view = initView;    }    
    public void setLevelBeingEdited(boolean initLevelBeingEdited)
    {   levelBeingEdited = initLevelBeingEdited;    }
    public void setLastMousePosition(int initX, int initY)
    {
        lastMouseX = initX;
        lastMouseY = initY;
        view.getCanvas().repaint();
    }    
    public void setSelectedIntersection(Intersection i)
    {
        selectedIntersection = i;
        selectedRoad = null;
        view.getCanvas().repaint();
    }    
    public void setSelectedRoad(Road r)
    {
        selectedRoad = r;
        selectedIntersection = null;
        view.getCanvas().repaint();
    }
    
    // AND THEN ALL THE SERVICE METHODS FOR UPDATING THE LEVEL
    // AND APP STATE

    /**
     * For selecting the first intersection when making a road. It will
     * find the road at the (canvasX, canvasY) location.
     */
    public void selectStartRoadIntersection(int canvasX, int canvasY)
    {
        startRoadIntersection = findIntersectionAtCanvasLocation(canvasX, canvasY);
        if (startRoadIntersection != null)
        {
            // NOW WE NEED THE SECOND INTERSECTION
            switchEditMode(PXLE_EditMode.ADDING_ROAD_END);
        }
    }
    
    /**
     * For selecting the second intersection when making a road. It will
     * find the road at the (canvasX, canvasY) location.
     */
    public void selectEndRoadIntersection(int canvasX, int canvasY)
    {
        Intersection endRoadIntersection = findIntersectionAtCanvasLocation(canvasX, canvasY);
        if (endRoadIntersection != null)
        {
            // MAKE AND ADD A NEW ROAD
            Road newRoad = new Road();
            newRoad.node1 = startRoadIntersection;
            newRoad.node2 = endRoadIntersection;
            newRoad.oneWay = false;
            newRoad.speedLimit = DEFAULT_SPEED_LIMIT;
            level.roads.add(newRoad);
            
            // AND LET'S GO BACK TO ADDING ANOTHER ROAD
            switchEditMode(PXLE_EditMode.ADDING_ROAD_START);
            startRoadIntersection = null;

            // RENDER
            view.getCanvas().repaint();
        }
    }
    
    /**
     * Sets up the model to edit a brand new level.
     */
    public void startNewLevel(String levelName)
    {
        // CLEAR OUT THE OLD GRAPH
        level.reset();
        
        // FIRST INITIALIZE THE LEVEL
        // WE ALWAYS START WITH A DEFAULT BACKGROUND,
        // AND START AND END LOCATIONS
        level.init( levelName,
                    DEFAULT_BG_IMG,
                    DEFAULT_START_IMG,
                    DEFAULT_START_X,
                    DEFAULT_START_Y,
                    DEFAULT_DEST_IMG,
                    DEFAULT_DEST_X, 
                    DEFAULT_DEST_Y);
        
        // NOW MAKE THE LEVEL IMAGES
        backgroundImage = view.loadImage(LEVELS_PATH + DEFAULT_BG_IMG);
        startingLocationImage = view.loadImage(LEVELS_PATH + DEFAULT_START_IMG);
        destinationImage = view.loadImage(LEVELS_PATH + DEFAULT_DEST_IMG);

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
        view.getCanvas().repaint();
    }

    /**
     * Updates the background image.
     */
    public void updateBackgroundImage(String newBgImage)
    {
        // UPDATE THE LEVEL TO FIT THE BACKGROUDN IMAGE SIZE
        level.backgroundImageFileName = newBgImage;
        backgroundImage = view.loadImage(LEVELS_PATH + level.backgroundImageFileName);
        int levelWidth = backgroundImage.getWidth(null);
        int levelHeight = backgroundImage.getHeight(null);
        viewport.setLevelDimensions(levelWidth, levelHeight);
        view.getCanvas().repaint();
    }

    /**
     * Updates the image used for the starting location and forces rendering.
     */
    public void updateStartingLocationImage(String newStartImage)
    {
        level.startingLocationImageFileName = newStartImage;
        startingLocationImage = view.loadImage(LEVELS_PATH + level.startingLocationImageFileName);
        view.getCanvas().repaint();
    }

    /**
     * Updates the image used for the destination and forces rendering.
     */
    public void updateDestinationImage(String newDestImage)
    {
        level.destinationImageFileName = newDestImage;
        destinationImage = view.loadImage(LEVELS_PATH + level.destinationImageFileName);
        view.getCanvas().repaint();
    }

    /**
     * Used for scrolling the viewport by (incX, incY). Note that it won't
     * let the viewport scroll off the level.
     */
    public void moveViewport(int incX, int incY)
    {
        // MOVE THE VIEWPORT
        viewport.move(incX, incY);

        // AND NOW FORCE A REDRAW
        view.getCanvas().repaint();
    }

    /**
     * For changing the edit mode, and thus what edit operations
     * the user may perform.
     */
    public void switchEditMode(PXLE_EditMode initEditMode)
    {
        if (levelBeingEdited)
        {
            // SET THE NEW EDIT MODE
            editMode = initEditMode;
            
            // UPDATE THE CURSOR
            view.updateCursor(editMode);

            // IF WE'RE ADDING A ROAD, THEN NOTHING SHOULD BE SELECTED 
            if (editMode == PXLE_EditMode.ADDING_ROAD_START)
            {
                selectedIntersection = null;
                selectedRoad = null;            
            }
            
            // RENDER
            view.getCanvas().repaint();
        }
    }

    /**
     * Adds an intersection to the graph
     */
    public void addIntersection(Intersection intToAdd)
    {
        ArrayList<Intersection> intersections = level.getIntersections();
        intersections.add(intToAdd);
        view.getCanvas().repaint();
    }

    /**
     * Calculates and returns the distance between two points.
     */
    public double calculateDistanceBetweenPoints(int x1, int y1, int x2, int y2)
    {
        double diffXSquared = Math.pow(x1 - x2, 2);
        double diffYSquared = Math.pow(y1 - y2, 2);
        return Math.sqrt(diffXSquared + diffYSquared);
    }

    /**
     * Moves the selected intersection to (canvasX, canvasY),
     * translating it into level coordinates.
     */
    public void moveSelectedIntersection(int canvasX, int canvasY)
    {
        selectedIntersection.x = canvasX + viewport.x;
        selectedIntersection.y = canvasY + viewport.y;
        view.getCanvas().repaint();
    }

    /**
     * Searches the level graph and finds and returns the intersection
     * that overlaps (canvasX, canvasY).
     */
    public Intersection findIntersectionAtCanvasLocation(int canvasX, int canvasY)
    {
        // CHECK TO SEE IF THE USER IS SELECTING AN INTERSECTION
        for (Intersection i : level.intersections)
        {
            double distance = calculateDistanceBetweenPoints(i.x, i.y, canvasX + viewport.x, canvasY + viewport.y);
            if (distance < INTERSECTION_RADIUS)
            {
                // MAKE THIS THE SELECTED INTERSECTION
                return i;
            }
        }
        return null;
    }

    /**
     * Deletes the selected item from the graph, which might be either
     * an intersection or a road.
     */
    public void deleteSelectedItem()
    {
        // DELETE THE SELECTED INTERSECTION, BUT MAKE SURE IT'S 
        // NOT THE STARTING LOCATION OR DESTINATION
        if ((selectedIntersection != null)
                && (selectedIntersection != level.startingLocation)
                && (selectedIntersection != level.destination))
        {
            // REMOVE ALL THE ROADS THE INTERSECTION IS CONNECTED TO
            ArrayList<Road> roadsMarkedForDeletion = new ArrayList();
            for (Road r : level.roads)
            {
                if ((r.node1 == selectedIntersection)
                        || (r.node2 == selectedIntersection))
                    roadsMarkedForDeletion.add(r);
            }
            
            // NOW REMOVE ALL THE ROADS MARKED FOR DELETION
            for (Road r : roadsMarkedForDeletion)
            {
                level.roads.remove(r);
            }
            
            // THEN REMOVE THE INTERSECTION ITSELF
            level.intersections.remove(selectedIntersection);
            
            // AND FINALLY NOTHING IS SELECTED ANYMORE
            selectedIntersection = null;
            switchEditMode(PXLE_EditMode.NOTHING_SELECTED);            
        }
        // THE SELECTED ITEM MIGHT BE A ROAD
        else if (selectedRoad != null)
        {
            // JUST REMOVE THE NODE, BUT NOT ANY OF THE INTERSECTIONS
            level.roads.remove(selectedRoad);
            selectedRoad = null;
            switchEditMode(PXLE_EditMode.NOTHING_SELECTED);
        }
    }
    
    /**
     * Unselects any intersection or road that might be selected.
     */
    public void unselectEverything()
    {
        selectedIntersection = null;
        selectedRoad = null;
        startRoadIntersection = null;
        view.getCanvas().repaint();
    }

    /**
     * Searches to see if there is a road at (canvasX, canvasY), and if
     * there is, it selects and returns it.
     */
    public Road selectRoadAtCanvasLocation(int canvasX, int canvasY)
    {
        Iterator<Road> it = level.roads.iterator();
        Line2D.Double tempLine = new Line2D.Double();
        while (it.hasNext())
        {
            Road r = it.next();
            tempLine.x1 = r.node1.x;
            tempLine.y1 = r.node1.y;
            tempLine.x2 = r.node2.x;
            tempLine.y2 = r.node2.y;
            double distance = tempLine.ptSegDist(canvasX+viewport.x, canvasY+viewport.y);
            
            // IS IT CLOSE ENOUGH?
            if (distance <= INT_STROKE)
            {
                // SELECT IT
                this.selectedRoad = r;
                this.switchEditMode(PXLE_EditMode.ROAD_SELECTED);
                return selectedRoad;
            }
        }
        return null;
    }

    /**
     * Checks to see if (canvasX, canvasY) is free (i.e. there isn't
     * already an intersection there, and if not, adds one.
     */
    public void addIntersectionAtCanvasLocation(int canvasX, int canvasY)
    {
        // FIRST MAKE SURE THE ENTIRE INTERSECTION IS INSIDE THE LEVEL
        if ((canvasX - INTERSECTION_RADIUS) < 0) return;
        if ((canvasY - INTERSECTION_RADIUS) < 0) return;
        if ((canvasX + INTERSECTION_RADIUS) > viewport.levelWidth) return;
        if ((canvasY + INTERSECTION_RADIUS) > viewport.levelHeight) return;
        
        // AND ONLY ADD THE INTERSECTION IF IT DOESN'T OVERLAP WITH
        // AN EXISTING INTERSECTION
        for(Intersection i : level.intersections)
        {
            double distance = calculateDistanceBetweenPoints(i.x-viewport.x, i.y-viewport.y, canvasX, canvasY);
            if (distance < INTERSECTION_RADIUS)
                return;
        }          
        
        // LET'S ADD A NEW INTERSECTION
        int intX = canvasX + viewport.x;
        int intY = canvasY + viewport.y;
        Intersection newInt = new Intersection(intX, intY);
        level.intersections.add(newInt);
        view.getCanvas().repaint();
    }
    
    /**
     * Retrieves the money, police, bandits, and zombies stats from
     * the view and uses it to refresh the level values.
     */
    public void refreshLevelStats()
    {
        if (!view.isRefreshingSpinners())
        {
            // GET THE DATA FROM THE VIEW
            int money = view.getCurrentMoney();
            int numPolice = view.getCurrentPolice();
            int numBandits = view.getCurrentBandits();
            int numZombies = view.getCurrentZombies();
            
            // AND USE IT TO UPDATE THE LEVEL
            level.setMoney(money);
            level.setNumPolice(numPolice);
            level.setNumBandits(numBandits);
            level.setNumZombies(numZombies);
        }
    }

    /**
     * Increases the speed limit on the selected road.
     */
    public void increaseSelectedRoadSpeedLimit()
    {
        if (selectedRoad != null)
        {
            int speedLimit = selectedRoad.getSpeedLimit();
            if (speedLimit < MAX_SPEED_LIMIT)
            {
                speedLimit += SPEED_LIMIT_STEP;
                selectedRoad.setSpeedLimit(speedLimit);
                view.getCanvas().repaint();
            }
        }
    }

    /**
     * Decreases the speed limit on the selected road.
     */
    public void decreaseSelectedRoadSpeedLimit()
    {
        if (selectedRoad != null)
        {
            int speedLimit = selectedRoad.getSpeedLimit();
            if (speedLimit > MIN_SPEED_LIMIT)
            {
                speedLimit -= SPEED_LIMIT_STEP;
                selectedRoad.setSpeedLimit(speedLimit);
                view.getCanvas().repaint();
            }
        }
    }    

    /**
     * Toggles the selected road, making it one way if it's currently
     * two-way, and two-way if it's currently one way.
     */
    public void toggleSelectedRoadOneWay()
    {
        if (selectedRoad != null)
        {
            selectedRoad.setOneWay(!selectedRoad.isOneWay());
            view.getCanvas().repaint();
        }
    }
}