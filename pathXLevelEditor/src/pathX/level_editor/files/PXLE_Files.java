package pathX.level_editor.files;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import pathX.level_editor.model.PXLE_Model;
import static pathX.level_editor.PXLE_Constants.*;
import pathX.level_editor.view.PXLE_View;

/**
 * This class manages the interactions with the user during level creation,
 * opening, exiting, and saving.
 * 
 * @author Richard McKenna
 */
public class PXLE_Files
{
    // THIS DOEST THE ACTUAL FILE I/O
    PXLE_LevelIO levelIO;
    String levelFileExtension;
    FileFilter fileFilter;

    // THE VIEW AND DATA TO BE UPDATED DURING LOADING
    PXLE_View view;
    PXLE_Model model;

    // WE'LL STORE THE FILE CURRENTLY BEING WORKED ON
    // AND THE NAME OF THE FILE
    private File currentFile;
    private String currentFileName;

    // WE WANT TO KEEP TRACK OF WHEN SOMETHING HAS NOT BEEN SAVED
    private boolean saved;

    /**
     * This default constructor starts the program without a level file being
     * edited.
     */
    public PXLE_Files(PXLE_View initView, PXLE_Model initModel, String levelFileFormat)
    {
        // INIT THE FILE I/O COMPONENT
        levelFileExtension = levelFileFormat;
        if (levelFileFormat.equals(XML_LEVEL_FILE_EXTENSION))
        {
            levelIO = new PXLE_XMLLevelIO(new File(LEVELS_PATH + LEVEL_SCHEMA));
            fileFilter = new XMLFilter();
        }                    
        else
        {
            levelIO = new PXLE_BinLevelIO();
            fileFilter = new BINFilter();
        }

        // KEEP THESE REFERENCE FOR LATER
        view = initView;
        model = initModel;

        // NOTHING YET
        currentFile = null;
        currentFileName = null;
        saved = true;
    }

    /**
     * This method starts the process of editing a new level. If a level is
     * already being edited, it will prompt the user to save it first.
     */
    public void processNewLevelRequest()
    {
        // WE MAY HAVE TO SAVE CURRENT WORK
        boolean continueToMakeNew = true;
        if (!saved)
        {
            // THE USER CAN OPT OUT HERE WITH A CANCEL
            continueToMakeNew = promptToSave();
        }

        // IF THE USER REALLY WANTS TO MAKE A NEW LEVEL
        if (continueToMakeNew)
        {
            // GO AHEAD AND PROCEED MAKING A NEW LEVEL
            continueToMakeNew = promptForNew(true);
            if (continueToMakeNew)
            {
                view.enableSaveButton(true);
                view.enableSaveAsButton(true);
                view.enableEditButtons(true);
            }
        }
    }

    /**
     * This method lets the user open a level saved to a file. It will also make
     * sure data for the current level is not lost.
     */
    public void processOpenLevelRequest()
    {
        // WE MAY HAVE TO SAVE CURRENT WORK
        boolean continueToOpen = true;
        if (!saved)
        {
            // THE USER CAN OPT OUT HERE WITH A CANCEL
            continueToOpen = promptToSave();
        }

        // IF THE USER REALLY WANTS TO OPEN A LEVEL
        if (continueToOpen)
        {
            // GO AHEAD AND PROCEED MAKING A NEW LEVEL
            continueToOpen = promptToOpen();
            if (continueToOpen)
            {
                view.enableSaveButton(true);
                view.enableSaveAsButton(true);
                view.getCanvas().repaint();
            }
        }
    }

    /**
     * This method will save the current level to a file. Note that we already
     * know the name of the file, so we won't need to prompt the user.
     */
    public void processSaveLevelRequest()
    {
        // DON'T ASK, JUST SAVE
        boolean savedSuccessfully = save(currentFile);
        if (savedSuccessfully)
        {
            // MARK IT AS SAVED
            saved = true;

            // AND REFRESH THE GUI
            view.enableSaveButton(true);
        }
    }

    /**
     * This method will save the current level as a named file provided by the
     * user.
     */
    public void processSaveAsLevelRequest()
    {
        // ASK THE USER FOR A FILE NAME
        promptForNew(false);
    }

    /**
     * This method will exit the application, making sure the user doesn't lose
     * any data first.
     */
    public void processExitRequest()
    {
        // WE MAY HAVE TO SAVE CURRENT WORK
        boolean continueToExit = true;
        if (!saved)
        {
            // THE USER CAN OPT OUT HERE
            continueToExit = promptToSave();
        }

        // IF THE USER REALLY WANTS TO EXIT THE APP
        if (continueToExit)
        {
            // EXIT THE APPLICATION
            System.exit(0);
        }
    }

    /**
     * This helper method asks the user for a name for the level about to be
     * created. Note that when the level is created, a corresponding .xml file
     * is also created.
     *
     * @return true if the user goes ahead and provides a good name false if
     * they cancel.
     */
    private boolean promptForNew(boolean brandNew)
    {
        // SO NOW ASK THE USER FOR A LEVEL NAME
        String levelName = JOptionPane.showInputDialog(
                view,
                LEVEL_NAME_REQUEST_TEXT,
                LEVEL_NAME_REQUEST_TITLE_TEXT,
                JOptionPane.QUESTION_MESSAGE);

        // IF THE USER CANCELLED, THEN WE'LL GET A fileName
        // OF NULL, SO LET'S MAKE SURE THE USER REALLY
        // WANTS TO DO THIS ACTION BEFORE MOVING ON
        if ((levelName != null)
                && (levelName.length() > 0))
        {
            // WE ARE STILL IN DANGER OF AN ERROR DURING THE WRITING
            // OF THE INITIAL FILE, SO WE'LL NOT FINALIZE ANYTHING
            // UNTIL WE KNOW WE CAN WRITE TO THE FILE
            String fileNameToTry = levelName + levelFileExtension;
            File fileToTry = new File(LEVELS_PATH + fileNameToTry);
            if (fileToTry.isDirectory())
            {
                return false;
            }
            int selection = JOptionPane.OK_OPTION;
            if (fileToTry.exists())
            {
                selection = JOptionPane.showConfirmDialog(view,
                        OVERWRITE_FILE_REQUEST_TEXT_A + fileNameToTry + OVERWRITE_FILE_REQUEST_TEXT_B,
                        OVERWRITE_FILE_REQUEST_TITLE_TEXT,
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
            }
            if (selection == JOptionPane.OK_OPTION)
            {
                // MAKE OUR NEW LEVEL
                if (brandNew)
                {
                    model.startNewLevel(levelName);
                }

                // NOW SAVE OUR NEW LEVEL
                save(fileToTry);

                // NO ERROR, SO WE'RE HAPPY
                saved = true;

                // UPDATE THE FILE NAMES AND FILE
                currentFileName = fileNameToTry;
                currentFile = fileToTry;

                // SELECT THE ROOT NODE, WHICH SHOULD FORCE A
                // TRANSITION INTO THE REGION VIEWING STATE
                // AND PUT THE FILE NAME IN THE TITLE BAR
                view.setTitle(APP_NAME + APP_NAME_FILE_NAME_SEPARATOR + currentFileName);

                // WE DID IT!
                return true;
            }
        }
        // USER DECIDED AGAINST IT
        return false;
    }

    /**
     * This helper method verifies that the user really wants to save their
     * unsaved work, which they might not want to do. Note that it could be used
     * in multiple contexts before doing other actions, like creating a new
     * level, or opening another level, or exiting. Note that the user will be
     * presented with 3 options: YES, NO, and CANCEL. YES means the user wants
     * to save their work and continue the other action (we return true to
     * denote this), NO means don't save the work but continue with the other
     * action (true is returned), CANCEL means don't save the work and don't
     * continue with the other action (false is returned).
     */
    private boolean promptToSave()
    {
        // PROMPT THE USER TO SAVE UNSAVED WORK
        int selection = JOptionPane.showOptionDialog(
                view,
                PROMPT_TO_SAVE_TEXT, PROMPT_TO_SAVE_TITLE_TEXT,
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, null, null);

        // IF THE USER SAID YES, THEN SAVE BEFORE MOVING ON
        if (selection == JOptionPane.YES_OPTION)
        {
            boolean saveSucceeded = save(currentFile);
            if (saveSucceeded)
            {
                // WE MADE IT THIS FAR WITH NO ERRORS
                JOptionPane.showMessageDialog(
                        view,
                        LEVEL_SAVED_TEXT,
                        LEVEL_SAVED_TITLE_TEXT,
                        JOptionPane.INFORMATION_MESSAGE);
                saved = true;
            } else
            {
                // SOMETHING WENT WRONG WRITING THE XML FILE
                JOptionPane.showMessageDialog(
                        view,
                        LEVEL_SAVING_ERROR_TEXT,
                        LEVEL_SAVING_ERROR_TITLE_TEXT,
                        JOptionPane.ERROR_MESSAGE);
            }
        } // IF THE USER SAID CANCEL, THEN WE'LL TELL WHOEVER
        // CALLED THIS THAT THE USER IS NOT INTERESTED ANYMORE
        else
        {
            if (selection == JOptionPane.CANCEL_OPTION)
            {
                return false;
            }
        }

        // IF THE USER SAID NO, WE JUST GO ON WITHOUT SAVING
        // BUT FOR BOTH YES AND NO WE DO WHATEVER THE USER
        // HAD IN MIND IN THE FIRST PLACE
        return true;
    }

    /**
     * This helper method asks the user for a file to open. The user-selected
     * file is then loaded and the GUI updated. Note that if the user cancels
     * the open process, nothing is done. If an error occurs loading the file, a
     * message is displayed, but nothing changes.
     */
    private boolean promptToOpen()
    {
        // ASK THE USER FOR THE LEVEL TO OPEN
        JFileChooser levelFileChooser = new JFileChooser(LEVELS_PATH);
        levelFileChooser.setFileFilter(fileFilter);
        int buttonPressed = levelFileChooser.showOpenDialog(view);

        // ONLY OPEN A NEW FILE IF THE USER SAYS OK
        if (buttonPressed == JFileChooser.APPROVE_OPTION)
        {
            // GET THE FILE THE USER ENTERED
            File testFile = levelFileChooser.getSelectedFile();
            if (testFile == null)
            {
                // TELL THE USER ABOUT THE ERROR
                JOptionPane.showMessageDialog(
                        view,
                        NO_FILE_SELECTED_TEXT,
                        NO_FILE_SELECTED_TITLE_TEXT,
                        JOptionPane.INFORMATION_MESSAGE);

                return false;
            }

            // AND LOAD THE LEVEL (XML FORMAT) FILE
            boolean loadedSuccessfully = load(testFile);

            if (loadedSuccessfully)
            {
                currentFile = testFile;
                currentFileName = currentFile.getName();
                saved = true;

                // AND PUT THE FILE NAME IN THE TITLE BAR
                view.setTitle(APP_NAME + APP_NAME_FILE_NAME_SEPARATOR + currentFileName);
                
                // MAKE SURE THE SPINNERS HAVE THE CORRRECT INFO
                view.refreshSpinners(model.getLevel());
                
                view.enableEditButtons(true);
                model.setLevelBeingEdited(true);
                
                view.getCanvas().repaint();

                // TELL THE USER ABOUT OUR SUCCESS
                JOptionPane.showMessageDialog(
                        view,
                        LEVEL_LOADED_TEXT,
                        LEVEL_LOADED_TITLE_TEXT,
                        JOptionPane.INFORMATION_MESSAGE);

                return true;
            } else
            {
                // TELL THE USER ABOUT THE ERROR
                JOptionPane.showMessageDialog(
                        view,
                        LEVEL_LOADING_ERROR_TEXT,
                        LEVEL_LOADING_ERROR_TITLE_TEXT,
                        JOptionPane.INFORMATION_MESSAGE);
                return false;
            }
        }
        return false;
    }

    /**
     * This mutator method marks the file as not saved, which means that when
     * the user wants to do a file-type operation, we should prompt the user to
     * save current work first. Note that this method should be called any time
     * the level is changed in some way.
     */
    public void markFileAsNotSaved()
    {
        saved = false;
    }

    /**
     * Accessor method for checking to see if the current level has been saved
     * since it was last editing. If the current file matches the level data,
     * we'll return true, otherwise false.
     *
     * @return true if the current level is saved to the file, false otherwise.
     */
    public boolean isSaved()
    {
        return saved;
    }

    // HELPER METHOD, THIS EMPLOYS THE FILE I/O COMPONENT
    // TO DO THE ACTUAL WORK OF LEVEL SAVING
    private boolean save(File fileToTry)
    {
        return levelIO.saveLevel(fileToTry, model.getLevel());
    }

    // HELPER METHOD, THIS EMPLOYS THE FILE I/O COMPONENT
    // TO DO THE ACTUAL WORK OF LEVEL LOADING
    private boolean load(File testFile)
    {
        return levelIO.loadLevel(testFile, model);
    }
    
    /**
     * This helper class is so that the user only selects
     * xml files.
     */
    class XMLFilter extends FileFilter
    {
        @Override
        public boolean accept(File f)
        {
            String fileName = f.getName().toLowerCase();
            if (fileName.endsWith(".xml"))
            {
                return true;
            }
            else
                return false;
        }

        @Override
        public String getDescription()
        {
            return "Select XML Level File";
        }       
    }
    
    /**
     * This helper class is so that the user only selects
     * bin files.
     */
    class BINFilter extends FileFilter
    {
        @Override
        public boolean accept(File f)
        {
            String fileName = f.getName().toLowerCase();
            if (fileName.endsWith(".bin"))
            {
                return true;
            }
            else
                return false;
        }

        @Override
        public String getDescription()
        {
            return "Select BIN Level File";
        }       
    }    
}