package pathX.level_editor.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import pathX.level_editor.files.PXLE_Files;
import static pathX.level_editor.PXLE_Constants.*;

/**
 * This class responds to interactions with the file controls, providing
 * the creation of new levels, as well as saving and opening existing ones.
 * 
 * @author Richard McKenna
 */
public class PXLE_FileController implements ActionListener
{
    // THE HANDLER WILL NEED TO RELAY THE RESPONSE
    // REQUEST TO THE FILE MANAGER
    private PXLE_Files fileManager;
    
    /**
     * Constructor, it will store the file manager for later.
     */
    public PXLE_FileController(PXLE_Files initFileManager)
    {
        // KEEP IT FOR LATER
        fileManager = initFileManager;
    }

    /**
     * This method is called in response to the user clicking on
     * one of the file buttons.
     */
    @Override
    public void actionPerformed(ActionEvent ae)
    {
        // EACH BUTTON STORES A COMMAND, WHICH REPRESENTS
        // WHICH BUTTON WAS CLICKED
        String command = ae.getActionCommand();
        
        // WHICH BUTTON WAS CLICKED?
        switch (command)
        {
            // THE NEW BUTTON?
            case NEW_COMMAND:
                fileManager.processNewLevelRequest();
                break;
            // THE OPEN BUTTON?
            case OPEN_COMMAND:
                fileManager.processOpenLevelRequest();
                break;
            // THE SAVE BUTTON?
            case SAVE_COMMAND:
                fileManager.processSaveLevelRequest();
                break;
            // THE SAVE AS BUTTON?
            case SAVE_AS_COMMAND:
                fileManager.processSaveAsLevelRequest();
                break;
            // THE EXIT BUTTON?
            case EXIT_COMMAND:
                fileManager.processExitRequest();
                break;
        }
    }
}