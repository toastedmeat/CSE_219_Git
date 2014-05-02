package pathX.level_editor;

import javax.swing.JOptionPane;
import pathX.level_editor.files.PXLE_Files;
import pathX.level_editor.view.PXLE_View;
import pathX.level_editor.model.PXLE_Model;
import static pathX.level_editor.PXLE_Constants.*;

/**
 * This application lets the user make levels for the pathX game.
 * 
 * @author Richard McKenna
 */
public class PathXLevelEditor
{
    PXLE_View view;
    PXLE_Model model;
    PXLE_Files files;
    String levelFileFormat;

    /**
     * Default constructor, it initializes everything needed to start up
     * this application's user interface, including the data and file
     * management stuff. After constructed, we can start the user interface
     * window.
     */
    public PathXLevelEditor()
    {
        // SELECT A LEVEL FILE FORMAT
        initLevelFileFormat();
        
        // FIRST MAKE THE MODEl
        model = new PXLE_Model();

        // THEN THE VIEW
        view = new PXLE_View();

        // THEN CONSTRUCT THE FILE MANAGER
        files = new PXLE_Files(view, model, levelFileFormat);
        
        // GIVE THE VIEW TO THE MODEL
        model.setView(view);
        
        // AND INITIALIZE THE REST OF THE VIEW
        view.init(model, files);
    }
    
    private void initLevelFileFormat()
    {
        String[] selections = new String[2];
        selections[0] = XML_LEVEL_FILE_EXTENSION;
        selections[1] = BIN_LEVEL_FILE_EXTENSION;
        levelFileFormat = (String)JOptionPane.showInputDialog(view, 
                PROMPT_TO_SELECT_FORMAT, 
                TITLE_SELECT_FORMAT, 
                JOptionPane.QUESTION_MESSAGE, 
                null, selections, XML_LEVEL_FILE_EXTENSION);
    }

    /**
     * This is the starting point for this application. Once the window
     * is set visible it will be in event handling mode.
     */
    public static void main(String[] args)    
    {
        // MAKE AND START THE UI
        PathXLevelEditor pxle = new PathXLevelEditor();
        pxle.view.setVisible(true);
    }
}