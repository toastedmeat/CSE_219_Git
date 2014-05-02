package pathX.level_editor.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import static pathX.level_editor.PXLE_Constants.*;
import pathX.level_editor.model.PXLE_Model;
import pathX.level_editor.view.PXLE_View;

/**
 * This class provides a response for when the user decides to 
 * change the start, destination or background images.
 * 
 * @author Richard McKenna
 */
public class PXLE_ImageSelectController implements ActionListener
{
    // APP MODEL AND VIEW
    PXLE_Model model;
    PXLE_View view;

    /**
     * This constructor just keeps the model and view for later.
     */
    public PXLE_ImageSelectController(  PXLE_Model initModel,
                                        PXLE_View initView)
    {
        model = initModel;
        view = initView;
    }
    
    /**
     * This method is called when the user clicks on the buttons
     * to change the background or source/dest images.
     */
    @Override
    public void actionPerformed(ActionEvent ae)
    {
        // WHICH BUTTON WAS PRESSED?
        String command = ae.getActionCommand();
        
        // CHANGE THE BACKGROUND IMAGE?
        if (command.equals(BG_IMAGE_COMMAND))
        {
            // UPDATE THE BACKGROUND IMAGE IF THE USER
            // DOESN'T BACK OUT
            String bgImage = selectImage();
            if (bgImage != null)
            {
                // GO AHEAD AND CHANGE IT
                 model.updateBackgroundImage(bgImage);
            }
        }
        // CHANGE THE START NODE IMAGE?
        else if (command.equals(START_IMAGE_COMMAND))
        {
            // UPDATE THE START IMAGE IF THE USER 
            // DOESN'T BACK OUT
            String startImage = selectImage();
            if (startImage != null)
            {
                // GO AHEAD AND CHANGE IT
                model.updateStartingLocationImage(startImage);
            }
        }
        // CHANGE THE DESTINATION NODE IMAGE?
        else if (command.equals(DEST_IMAGE_COMMAND))
        {
            // UPDATE THE DEST IMAGE IF THE USER
            // DOESN'T BACK OUT
            String destImage = selectImage();
            if (destImage != null)
            {
                // GO AHEAD AND CHANGE IT
                model.updateDestinationImage(destImage);
            }
        }
    }
    
    // HELPER METHOD FOR GETTING THE USER TO PICK AN IMAGE
    private String selectImage()
    {
        // PROMPT THE USER FOR AN IMAGE
        JFileChooser jfc = new JFileChooser(LEVELS_PATH);
        jfc.setFileFilter(new ImageFilter());
        int result = jfc.showOpenDialog(view);
        
        // MAKE SURE THE USER VERIFIES THEIR SELECTION
        if (result == JFileChooser.APPROVE_OPTION)
        {
            File selectedImageFile = jfc.getSelectedFile();
            if ((selectedImageFile == null) || 
                    (!selectedImageFile.exists()))
            {
                // ERROR, INVALID IMAGE
                JOptionPane.showMessageDialog(view, 
                        "ERROR LOADING " + selectedImageFile, 
                        "Error Loading Image", 
                        JOptionPane.ERROR_MESSAGE);
            }
            else
            {
                // IT'S A GOOD IMAGE SO SEND IT BACK
                return selectedImageFile.getName();
            }
        }
        // NO FILE WAS SELECTED
        return null;
    }
    
    /**
     * This helper class is so that the user only selects
     * image files.
     */
    class ImageFilter extends FileFilter
    {
        @Override
        public boolean accept(File f)
        {
            String fileName = f.getName().toLowerCase();
            if (fileName.endsWith(".png")
                    || (fileName.endsWith(".jpg")
                    || (fileName.endsWith(".gif"))))
            {
                return true;
            }
            else
                return false;
        }

        @Override
        public String getDescription()
        {
            return "Select Image File";
        }       
    }
}