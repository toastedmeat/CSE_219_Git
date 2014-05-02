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
import PathX.data.PathXAlgorithmType;

/**
 * This class provides services for efficiently loading and saving
 * binary files for The Sorting Hat game application.
 * 
 * @author Richard McKenna & Eric Loo
 */
public class PathXFileManager
{
    // WE'LL LET THE GAME KNOW WHEN DATA LOADING IS COMPLETE
    private PathXMiniGame miniGame;
    
    /**
     * Constructor for initializing this file manager, it simply keeps
     * the game for later.
     * 
     * @param initMiniGame The game for which this class loads data.
     */
    public PathXFileManager(PathXMiniGame initMiniGame)
    {
        // KEEP IT FOR LATER
        miniGame = initMiniGame;
    }

    /**
     * This method loads the contents of the levelFile argument so that
     * the player may then play that level. 
     * 
     * @param levelFile Level to load.
     */
    public void loadLevel(String levelFile)
    {
         // LOAD THE RAW DATA SO WE CAN USE IT
        // OUR LEVEL FILES WILL HAVE THE DIMENSIONS FIRST,
        // FOLLOWED BY THE GRID VALUES
        try
        {
            File fileToOpen = new File(levelFile);

            // LET'S USE A FAST LOADING TECHNIQUE. WE'LL LOAD ALL OF THE
            // BYTES AT ONCE INTO A BYTE ARRAY, AND THEN PICK THAT APART.
            // THIS IS FAST BECAUSE IT ONLY HAS TO DO FILE READING ONCE
            byte[] bytes = new byte[Long.valueOf(fileToOpen.length()).intValue()];
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            FileInputStream fis = new FileInputStream(fileToOpen);
            BufferedInputStream bis = new BufferedInputStream(fis);
            
            // HERE IT IS, THE ONLY READY REQUEST WE NEED
            bis.read(bytes);
            bis.close();
            
            // NOW WE NEED TO LOAD THE DATA FROM THE BYTE ARRAY
            DataInputStream dis = new DataInputStream(bais);
            
            // NOTE THAT WE NEED TO LOAD THE DATA IN THE SAME
            // ORDER AND FORMAT AS WE SAVED IT
            
            // FIRST READ THE ALGORITHM NAME TO USE FOR THE LEVEL
            String algorithmName = dis.readUTF();
            PathXAlgorithmType algorithmTypeToUse = PathXAlgorithmType.valueOf(algorithmName);
            // THEN READ THE GRID DIMENSIONS
            // WE DON'T ACTUALLY USE THESE
            int initGridColumns = dis.readInt();
            int initGridRows = dis.readInt();
            
            
            
            
            // EVERYTHING WENT AS PLANNED SO LET'S MAKE IT PERMANENT
            PathXDataModel dataModel = (PathXDataModel)miniGame.getDataModel();
            Viewport viewport = dataModel.getViewport();
            viewport.setNorthPanelHeight(NORTH_PANEL_HEIGHT);
            viewport.initViewportMargins();
            dataModel.setCurrentLevel(levelFile);
        }
        catch(Exception e)
        {
            // LEVEL LOADING ERROR
            miniGame.getErrorHandler().processError(pathXPropertyType.TEXT_ERROR_LOADING_LEVEL);
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
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            String recordPath = PATH_DATA + props.getProperty(pathXPropertyType.FILE_PLAYER_RECORD);
            File fileToSave = new File(recordPath);

            byte[] bytes = record.toByteArray();
            FileOutputStream fos = new FileOutputStream(fileToSave);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            
            // HERE IT IS, THE ONLY READY REQUEST WE NEED
            bos.write(bytes);
            bos.close();            
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
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            String recordPath = PATH_DATA + props.getProperty(pathXPropertyType.FILE_PLAYER_RECORD);
            File fileToOpen = new File(recordPath);

            // LET'S USE A FAST LOADING TECHNIQUE. WE'LL LOAD ALL OF THE
            // BYTES AT ONCE INTO A BYTE ARRAY, AND THEN PICK THAT APART.
            // THIS IS FAST BECAUSE IT ONLY HAS TO DO FILE READING ONCE
            byte[] bytes = new byte[Long.valueOf(fileToOpen.length()).intValue()];
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            FileInputStream fis = new FileInputStream(fileToOpen);
            BufferedInputStream bis = new BufferedInputStream(fis);
            
            // HERE IT IS, THE ONLY READY REQUEST WE NEED
            bis.read(bytes);
            bis.close();
            
            // NOW WE NEED TO LOAD THE DATA FROM THE BYTE ARRAY
            DataInputStream dis = new DataInputStream(bais);
            
            // NOTE THAT WE NEED TO LOAD THE DATA IN THE SAME
            // ORDER AND FORMAT AS WE SAVED IT
            // FIRST READ THE NUMBER OF LEVELS
            int numLevels = dis.readInt();

            for (int i = 0; i < numLevels; i++)
            {
                String levelName = dis.readUTF();
                PathXLevelRecord rec = new PathXLevelRecord();
                rec.algorithm = dis.readUTF();
                rec.gamesPlayed = dis.readInt();
                rec.wins = dis.readInt();
                rec.perfectWins = dis.readInt();
                rec.fastestPerfectWinTime = dis.readLong();
                recordToLoad.addSortingHatLevelRecord(levelName, rec);
            }
        }
        catch(Exception e)
        {
            // THERE WAS NO RECORD TO LOAD, SO WE'LL JUST RETURN AN
            // EMPTY ONE AND SQUELCH THIS EXCEPTION
        }        
        return recordToLoad;
    }
}