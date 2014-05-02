package PathX.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import mini_game.Viewport;
import PathX.PathX.pathXPropertyType;
import PathX.data.PathXLevelRecord;
import PathX.data.PathXDataModel;
import PathX.data.PathXRecord;
import PathX.ui.PathXMiniGame;
import properties_manager.PropertiesManager;
import static PathX.PathXConstants.*;
import PathX.data.Intersection;
import PathX.data.PathXAlgorithmType;
import PathX.data.PathXLevel;
import PathX.data.Road;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import xml_utilities.XMLUtilities;

/**
 * @author Richard McKenna & Eric Loo
 */
public class PathXFileManager
{
    // WE'LL LET THE GAME KNOW WHEN DATA LOADING IS COMPLETE
    private PathXMiniGame miniGame;
    
     // THIS WILL HELP US PARSE THE XML FILES
    private XMLUtilities xmlUtil;
    
    // THIS IS THE SCHEMA WE'LL USE
    private File levelSchema;
    /**
     * Constructor for initializing this file manager, it simply keeps
     * the game for later.
     * 
     * @param initMiniGame The game for which this class loads data.
     */
    public PathXFileManager(PathXMiniGame initMiniGame, File initLevelSchema)
    {
        // KEEP IT FOR LATER
        miniGame = initMiniGame;
        xmlUtil = new XMLUtilities();
        
         // WE'LL USE THE SCHEMA FILE TO VALIDATE THE XML FILES
        levelSchema = initLevelSchema;
    }

    /**
     * This method loads the contents of the levelFile argument so that
     * the player may then play that level. 
     * 
     * @param levelFile Level to load.
     */
    public void loadLevel(File levelFile, PathXDataModel model)
    {
        // LOAD THE RAW DATA SO WE CAN USE IT
        // OUR LEVEL FILES WILL HAVE THE DIMENSIONS FIRST,
        // FOLLOWED BY THE GRID VALUES
        try
        {
            // WE'LL FILL IN SOME OF THE LEVEL OURSELVES
            PathXLevel levelToLoad = model.getLevel();
            levelToLoad.reset();
            
            // FIRST LOAD ALL THE XML INTO A TREE
            Document doc = xmlUtil.loadXMLDocument( levelFile.getAbsolutePath(), 
                                                    levelSchema.getAbsolutePath());
            // FIRST LOAD THE LEVEL INFO
            Node levelNode = doc.getElementsByTagName(LEVEL_NODE).item(0);
            NamedNodeMap attributes = levelNode.getAttributes();
            String levelName = attributes.getNamedItem(NAME_ATT).getNodeValue();
            levelToLoad.setLevelName(levelName);

            // THEN LET'S LOAD THE LIST OF ALL THE REGIONS
            loadIntersectionsList(doc, levelToLoad);
            ArrayList<Intersection> intersections = levelToLoad.getIntersections();
            
            // AND NOW CONNECT ALL THE REGIONS TO EACH OTHER
            loadRoadsList(doc, levelToLoad);
            
            // LOAD THE START INTERSECTION
            Node startIntNode = doc.getElementsByTagName(START_INTERSECTION_NODE).item(0);
            attributes = startIntNode.getAttributes();
            String startIdText = attributes.getNamedItem(ID_ATT).getNodeValue();
            int startId = Integer.parseInt(startIdText);
            String startImageName = attributes.getNamedItem(IMAGE_ATT).getNodeValue();
            Intersection startingIntersection = intersections.get(startId);
            levelToLoad.setStartingLocation(startingIntersection);
            model.updateStartingLocationImage(startImageName);
            
            // LOAD THE DESTINATION
            Node destIntNode = doc.getElementsByTagName(DESTINATION_INTERSECTION_NODE).item(0);
            attributes = destIntNode.getAttributes();
            String destIdText = attributes.getNamedItem(ID_ATT).getNodeValue();
            int destId = Integer.parseInt(destIdText);
            String destImageName = attributes.getNamedItem(IMAGE_ATT).getNodeValue();
            levelToLoad.setDestination(intersections.get(destId));
            model.updateDestinationImage(destImageName);
            
            // LOAD THE MONEY
            Node moneyNode = doc.getElementsByTagName(MONEY_NODE).item(0);
            attributes = moneyNode.getAttributes();
            String moneyText = attributes.getNamedItem(AMOUNT_ATT).getNodeValue();
            int money = Integer.parseInt(moneyText);
            levelToLoad.setMoney(money);
            
            // LOAD THE NUMBER OF POLICE
            Node policeNode = doc.getElementsByTagName(POLICE_NODE).item(0);
            attributes = policeNode.getAttributes();
            String policeText = attributes.getNamedItem(NUM_ATT).getNodeValue();
            int numPolice = Integer.parseInt(policeText);
            levelToLoad.setNumPolice(numPolice);
            
            // LOAD THE NUMBER OF BANDITS
            Node banditsNode = doc.getElementsByTagName(BANDITS_NODE).item(0);
            attributes = banditsNode.getAttributes();
            String banditsText = attributes.getNamedItem(NUM_ATT).getNodeValue();
            int numBandits = Integer.parseInt(banditsText);
            levelToLoad.setNumBandits(numBandits);
            
            // LOAD THE NUMBER OF ZOMBIES
            Node zombiesNode = doc.getElementsByTagName(ZOMBIES_NODE).item(0);
            attributes = zombiesNode.getAttributes();
            String zombiesText = attributes.getNamedItem(NUM_ATT).getNodeValue();
            int numZombies = Integer.parseInt(zombiesText);
            levelToLoad.setNumZombies(numZombies); 
        }
        catch(Exception e)
        {
            // LEVEL LOADING ERROR
            e.printStackTrace();
            //miniGame.getErrorHandler().processError(pathXPropertyType.TEXT_ERROR_LOADING_LEVEL);
        }
    }    
    // PRIVATE HELPER METHOD FOR LOADING INTERSECTIONS INTO OUR LEVEL
    private void loadIntersectionsList( Document doc, PathXLevel levelToLoad)
    {
        // FIRST GET THE REGIONS LIST
        Node intersectionsListNode = doc.getElementsByTagName(INTERSECTIONS_NODE).item(0);
        ArrayList<Intersection> intersections = levelToLoad.getIntersections();
        
        // AND THEN GO THROUGH AND ADD ALL THE LISTED REGIONS
        ArrayList<Node> intersectionsList = xmlUtil.getChildNodesWithName(intersectionsListNode, INTERSECTION_NODE);
        for (int i = 0; i < intersectionsList.size(); i++)
        {
            // GET THEIR DATA FROM THE DOC
            Node intersectionNode = intersectionsList.get(i);
            NamedNodeMap intersectionAttributes = intersectionNode.getAttributes();
            String idText = intersectionAttributes.getNamedItem(ID_ATT).getNodeValue();
            String openText = intersectionAttributes.getNamedItem(OPEN_ATT).getNodeValue();
            String xText = intersectionAttributes.getNamedItem(X_ATT).getNodeValue();
            int x = Integer.parseInt(xText);
            String yText = intersectionAttributes.getNamedItem(Y_ATT).getNodeValue();
            int y = Integer.parseInt(yText);
            
            // NOW MAKE AND ADD THE INTERSECTION
            Intersection newIntersection = new Intersection(x, y);
            newIntersection.open = Boolean.parseBoolean(openText);
            intersections.add(newIntersection);
        }
    }

    // PRIVATE HELPER METHOD FOR LOADING ROADS INTO OUR LEVEL
    private void loadRoadsList( Document doc, PathXLevel levelToLoad)
    {
        // FIRST GET THE REGIONS LIST
        Node roadsListNode = doc.getElementsByTagName(ROADS_NODE).item(0);
        ArrayList<Road> roads = levelToLoad.getRoads();
        ArrayList<Intersection> intersections = levelToLoad.getIntersections();
        
        // AND THEN GO THROUGH AND ADD ALL THE LISTED REGIONS
        ArrayList<Node> roadsList = xmlUtil.getChildNodesWithName(roadsListNode, ROAD_NODE);
        for (int i = 0; i < roadsList.size(); i++)
        {
            // GET THEIR DATA FROM THE DOC
            Node roadNode = roadsList.get(i);
            NamedNodeMap roadAttributes = roadNode.getAttributes();
            String id1Text = roadAttributes.getNamedItem(INT_ID1_ATT).getNodeValue();
            int int_id1 = Integer.parseInt(id1Text);
            String id2Text = roadAttributes.getNamedItem(INT_ID2_ATT).getNodeValue();
            int int_id2 = Integer.parseInt(id2Text);
            String oneWayText = roadAttributes.getNamedItem(ONE_WAY_ATT).getNodeValue();
            boolean oneWay = Boolean.parseBoolean(oneWayText);
            String speedLimitText = roadAttributes.getNamedItem(SPEED_LIMIT_ATT).getNodeValue();
            int speedLimit = Integer.parseInt(speedLimitText);
            
            // NOW MAKE AND ADD THE ROAD
            Road newRoad = new Road();
            newRoad.setNode1(intersections.get(int_id1));
            newRoad.setNode2(intersections.get(int_id2));
            newRoad.setOneWay(oneWay);
            newRoad.setSpeedLimit(speedLimit);
            roads.add(newRoad);
        }
    }
    
    /**
     * This method saves the record argument to the player records file.
     * 
     * @param record The complete player record, which has the records
     * on all levels.
     */
    public void saveRecord(PathXRecord record)
    {
        try
        {
                        
        }
        catch(Exception e)
        {
            // THERE WAS NO RECORD TO LOAD, SO WE'LL JUST RETURN AN
            // EMPTY ONE AND SQUELCH THIS EXCEPTION
        }
    }

    /**
     * This method loads the player record from the records file
     * so that the user may view stats.
     * 
     * @return The fully loaded record from the player record file.
     */
    public PathXRecord loadRecord()
    {
        PathXRecord recordToLoad = new PathXRecord();
        
        // LOAD THE RAW DATA SO WE CAN USE IT
        // OUR LEVEL FILES WILL HAVE THE DIMENSIONS FIRST,
        // FOLLOWED BY THE GRID VALUES
        try
        {
            
        }
        catch(Exception e)
        {
            // THERE WAS NO RECORD TO LOAD, SO WE'LL JUST RETURN AN
            // EMPTY ONE AND SQUELCH THIS EXCEPTION
        }        
        return recordToLoad;
    }
}