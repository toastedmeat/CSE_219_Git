package pathX.level_editor.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import static pathX.level_editor.PXLE_Constants.*;
import pathX.level_editor.model.PXLE_Model;
import pathX.level_editor.model.PXLE_EditMode;

/**
 * This class responds to keyboard interactions, which is needed for
 * doing much of the editing.
 * 
 * @author Richard McKenna
 */
public class PXLE_KeyController implements KeyListener
{
    // MODEL TO UPDATE
    PXLE_Model model;

    /**
     * This constructor just keeps the model for later.
     */
    public PXLE_KeyController(PXLE_Model initModel)
    {
        // KEEP IT FOR WHEN WE NEED TO RESPOND
        model = initModel;
    }

    /**
     * This function responds to when the user presses a keyboard key.
     */
    @Override
    public void keyPressed(KeyEvent ke)
    {
        // SCROLL LEFT
        if (ke.getKeyCode() == KeyEvent.VK_LEFT)
        {
            model.moveViewport(-SCROLL_SPEED, 0);
        }
        // SCROLL RIGHT
        else if (ke.getKeyCode() == KeyEvent.VK_RIGHT)
        {
            model.moveViewport(SCROLL_SPEED, 0);
        }
        // SCROLL UP
        else if (ke.getKeyCode() == KeyEvent.VK_UP)
        {
            model.moveViewport(0, -SCROLL_SPEED);
        }
        // SCROLL DOWN
        else if (ke.getKeyCode() == KeyEvent.VK_DOWN)
        {
            model.moveViewport(0, SCROLL_SPEED);
        }
        // ADD AN INTERSECTION
        else if (ke.getKeyCode() == KeyEvent.VK_A)
        {
            // CHANGE THE APP MODE
            model.switchEditMode(PXLE_EditMode.ADDING_INTERSECTION);
        }
        // ADD A ROAD
        else if (ke.getKeyCode() == KeyEvent.VK_R)
        {
            // CHANGE THE APP MODE
            model.switchEditMode(PXLE_EditMode.ADDING_ROAD_START);
        }
        // ESCAPE FROM THE CURRENT EDIT OPERATION
        else if (ke.getKeyCode() == KeyEvent.VK_ESCAPE)
        {
            // CHANGE THE APP MODE
            model.unselectEverything();
            model.switchEditMode(PXLE_EditMode.NOTHING_SELECTED);
        }
        // DELETE THE SELECTED ITEM
        else if (ke.getKeyCode() == KeyEvent.VK_DELETE)
        {
            model.deleteSelectedItem();
        }
        // INCREASE THE SPEED LIMIT ON THE SELECTED ROAD
        else if (ke.getKeyCode() == KeyEvent.VK_W)
        {
            model.increaseSelectedRoadSpeedLimit();
        }
        // DECREASE THE SPEED LIMIT ON THE SELECTED ROAD
        else if (ke.getKeyCode() == KeyEvent.VK_S)
        {
            model.decreaseSelectedRoadSpeedLimit();
        }
        // TOGGLE THE SELECTED ROAD AS ONE WAY
        else if (ke.getKeyCode() == KeyEvent.VK_Z)
        {
            model.toggleSelectedRoadOneWay();
        }
        // OPEN THE HELP DIALOG
        else if (ke.getKeyCode() == KeyEvent.VK_H)
        {
            JOptionPane jop = new JOptionPane();
            jop.setMessage(HELP_DISPLAY);
            jop.setMessageType(JOptionPane.INFORMATION_MESSAGE);
            JDialog dialog = jop.createDialog(model.getView(), "Help");
            dialog.setVisible(true);
        }
    }

    // WE WON'T DEFINE THESE ONES BUT NEED TO INCLUDE IT
    // TO MAKE THE COMPILER HAPPY
    @Override
    public void keyTyped(KeyEvent e)    {}
    @Override
    public void keyReleased(KeyEvent e) {}    
}
