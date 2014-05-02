package pathX.level_editor.controller;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import pathX.level_editor.model.PXLE_Model;

/**
 * This class responds to when the user interacts with one
 * of the application's spinners. It will result in immediately
 * updating the level data using the updated values.
 * 
 * @author Richard McKenna
 */
public class PXLE_SpinnerController implements ChangeListener
{
    // WE'LL UPDATE THE DATA VIA THIS MODEL
    PXLE_Model model;
    
    /**
     * This constructor just keeps the model for later.
     */
    public PXLE_SpinnerController(PXLE_Model initModel)
    {
        // KEEP THE REFERENCE FOR WHEN THE EVENTS ARE FIRED
        model = initModel;
    }

    /**
     * Called when the user interacts with one of the spinners,
     * it updates the related data in the model's level.
     */
    @Override
    public void stateChanged(ChangeEvent ce)
    {
        // GET THE CURRENT SETTINGS FROM ALL THERE SPINNERS
        // AND SEND THEM TO THE LEVEL
        model.refreshLevelStats();
    }
}