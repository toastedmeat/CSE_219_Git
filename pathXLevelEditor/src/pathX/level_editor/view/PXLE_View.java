package pathX.level_editor.view;

import pathX.level_editor.files.PXLE_Files;
import pathX.level_editor.controller.PXLE_ImageSelectController;
import pathX.level_editor.controller.PXLE_FileController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import pathX.level_editor.controller.PXLE_KeyController;
import static pathX.level_editor.PXLE_Constants.*;
import pathX.level_editor.controller.PXLE_EditLevelController;
import pathX.level_editor.controller.PXLE_SpinnerController;
import pathX.level_editor.controller.PXLE_WindowController;
import pathX.level_editor.model.PXLE_EditMode;
import pathX.level_editor.model.PXLE_Level;
import pathX.level_editor.model.PXLE_Model;

/**
 * This class provides the User Interface for the level editor.
 * 
 * @author Richard McKenna
 */
public class PXLE_View extends JFrame
{
    // THESE WILL STORE OUR BUTTONS
    JPanel northToolbar;

    // FILE CONTROLS
    JButton newButton;
    JButton openButton;
    JButton saveButton;
    JButton saveAsButton;
    JButton exitButton;

    // LEVEL IAMGE CONTENT
    JButton selectBackgroundButton;
    JButton selectStartLocationButton;
    JButton selectDestinationButton;

    // OTHER LEVEL SETTINGS
    JSpinner moneySpinner;
    JSpinner policeSpinner;
    JSpinner banditsSpinner;
    JSpinner zombiesSpinner;

    // LEVEL EDITOR RENDERING SURFACE
    PXLE_Canvas canvas;
    
    // THESE ARE THE CURSORS TO BE USED WHILE EDITING A LEVEL
    HashMap<PXLE_EditMode, Cursor> cursors;

    // HELPS US COORDINATE SPINNER UPDATING WHEN WE LOAD A LEVEL FROM A FILE
    boolean refreshingSpinners;

    /**
     * Default constructor, it does no initialization.
     */
    public PXLE_View()    {}

    /**
     * This method initilializes the UI for use.
     */
    public void init(PXLE_Model model, PXLE_Files files)
    {
        // FIRST SETUP THE APP WINDOW
        initWindow();

        // THEN INIT THE NORTH TOOLBAR
        initNorthToolbar();

        // THEN INIT THE CANVAS
        initCanvas(model);
        
        // INITIALIZE ALL THE CURSORS
        initCursors(model);

        // AND NOW INITIALIZE ALL THE EVENT HANDLERS
        registerWindowController(files);
        registerKeyController(model);
        registerFileController(files);
        registerImageSelectController(model);
        registerEditLevelController(model);
        registerSpinnerController(model);
    }

    // HELPER METHODS FOR INITIALIZING UI COMPONENTS
    // INITIALIZES THE WINDOW
    private void initWindow()
    {
        setTitle(TITLE);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }

    // INITIALIZES ALL THE COMPONENTS IN THE NORTH TOOLBAR
    private void initNorthToolbar()
    {
        // WE'LL PUT ALL THE BUTTONS IN THE TOOLBARS
        northToolbar = new JPanel();
        northToolbar.setLayout(new FlowLayout(FlowLayout.LEFT));

        // WE'LL USE THIS TO HELP LOAD OUR IMAGES
        MediaTracker tracker = new MediaTracker(this);

        // THE TOOLBAR CONTROLS
        northToolbar.setBorder(TOOLBAR_BORDER);
        
        // FIRST THE FILE STUFF
        newButton       = initToolbarButton(NEW_BUTTON_FILE,        NEW_COMMAND);
        openButton      = initToolbarButton(OPEN_BUTTON_FILE,       OPEN_COMMAND);
        saveButton      = initToolbarButton(SAVE_BUTTON_FILE,       SAVE_COMMAND);
        saveAsButton    = initToolbarButton(SAVE_AS_BUTTON_FILE,    SAVE_AS_COMMAND);
        exitButton      = initToolbarButton(EXIT_BUTTON_FILE,       EXIT_COMMAND);

        // THEN THE IMAGE SELECT STUFF
        selectBackgroundButton      = initToolbarButton(BG_BUTTON_FILE,     BG_IMAGE_COMMAND);
        selectStartLocationButton   = initToolbarButton(START_BUTTON_FILE,  START_IMAGE_COMMAND);
        selectDestinationButton     = initToolbarButton(DEST_BUTTON_FILE,   DEST_IMAGE_COMMAND);
        
        // AND THEN THE LEVEL SETTINGS STUFF
        moneySpinner = initToolbarLabeledSpinner(MONEY_LABEL_FILE, MONEY_COMMAND,
                MIN_MONEY, MAX_MONEY, STEP_MONEY, DEFAULT_MONEY);
        policeSpinner = initToolbarLabeledSpinner(POLICE_LABEL_FILE, POLICE_COMMAND,
                MIN_BOTS_PER_LEVEL, MAX_BOTS_PER_LEVEL, BOTS_STEP, MIN_BOTS_PER_LEVEL);
        banditsSpinner = initToolbarLabeledSpinner(BANDITS_LABEL_FILE, BANDITS_COMMAND,
                MIN_BOTS_PER_LEVEL, MAX_BOTS_PER_LEVEL, BOTS_STEP, MIN_BOTS_PER_LEVEL);
        zombiesSpinner = initToolbarLabeledSpinner(ZOMBIES_LABEL_FILE, ZOMBIES_COMMAND,
                MIN_BOTS_PER_LEVEL, MAX_BOTS_PER_LEVEL, BOTS_STEP, MIN_BOTS_PER_LEVEL);
        refreshingSpinners = false;

        // MAKE SURE THE BUTTONS START OFF PROPERLY ENABLED/DISABLED
        resetButtons();

        // LAYOUT THE COMPONENTS INSIDE THE WINDOW
        this.add(northToolbar, BorderLayout.NORTH);
    }

    // INITIALIZES THE CANVAS
    private void initCanvas(PXLE_Model model)
    {
        // INIT THE RENDER AREA
        canvas = new PXLE_Canvas(model);
        this.add(canvas, BorderLayout.CENTER);
    }
    
    // INITIALIZES ALL THE CURSORS TO BE SWITCHED ON DEMAND
    private void initCursors(PXLE_Model model)
    {
        Cursor defaultCursor = loadCursor(DEFAULT_CURSOR_IMG);
        Cursor addIntCursor = loadCursor(ADD_INT_CURSOR_IMG);
        Cursor addRoadCursor = loadCursor(ADD_ROAD_CURSOR_IMG);
        cursors = new HashMap();
        cursors.put(PXLE_EditMode.NOTHING_SELECTED, defaultCursor);
        cursors.put(PXLE_EditMode.INTERSECTION_SELECTED, defaultCursor);
        cursors.put(PXLE_EditMode.INTERSECTION_DRAGGED, defaultCursor);
        cursors.put(PXLE_EditMode.ROAD_SELECTED, defaultCursor);
        cursors.put(PXLE_EditMode.ADDING_INTERSECTION, addIntCursor);
        cursors.put(PXLE_EditMode.ADDING_ROAD_END, addRoadCursor);
        cursors.put(PXLE_EditMode.ADDING_ROAD_START, addRoadCursor);
        
        // START WITH THE CORRECT CURSOR
        updateCursor(model.getEditMode());
    }
    
    // HELPER METHOD FOR LOADING ONE OF THE CUSTOM CURSORS
    private Cursor loadCursor(String cursorImageFile)
    {
        Image img = loadImage(CURSORS_PATH + cursorImageFile);
        Point hotSpot = new Point(0,0);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Cursor cursor = toolkit.createCustomCursor(img, hotSpot, cursorImageFile);
        return cursor;
    }

    // HELPER METHODS FOR INITIALIZING ALL THE EVENT HANDLERS
    // REGISTERS A LISTENER TO HANDLER WINDOW EVENTS
    private void registerWindowController(PXLE_Files files)
    {
        PXLE_WindowController windowHandler = new PXLE_WindowController(files);
        addWindowListener(windowHandler);
    }

    // REGISTERS A LISTENER TO HANDLE KEYBOARD EVENTS
    private void registerKeyController(PXLE_Model model)
    {
        PXLE_KeyController keyHandler = new PXLE_KeyController(model);
        addKeyListener(keyHandler);
        northToolbar.addKeyListener(keyHandler);
        newButton.addKeyListener(keyHandler);
        openButton.addKeyListener(keyHandler);
        saveButton.addKeyListener(keyHandler);
        saveAsButton.addKeyListener(keyHandler);
        exitButton.addKeyListener(keyHandler);
        selectBackgroundButton.addKeyListener(keyHandler);
        selectStartLocationButton.addKeyListener(keyHandler);
        selectDestinationButton.addKeyListener(keyHandler);
        canvas.addKeyListener(keyHandler);
    }

    // REGISTERS A LISTENER FOR HANDLING EVENTS ASSOCIATED WITH THE FILE BUTTONS
    private void registerFileController(PXLE_Files files)
    {
        // THIS WILL HANDLE EVENTS FOR OUR FILE BUTTONS
        PXLE_FileController fileHandler = new PXLE_FileController(files);
        newButton.addActionListener(fileHandler);
        openButton.addActionListener(fileHandler);
        saveButton.addActionListener(fileHandler);
        saveAsButton.addActionListener(fileHandler);
        exitButton.addActionListener(fileHandler);
    }

    // REGISTERS HANDLER FOR THE IMAGE SELECT BUTTONS
    private void registerImageSelectController(PXLE_Model model)
    {
        PXLE_ImageSelectController imageHandler = new PXLE_ImageSelectController(model, this);
        selectBackgroundButton.addActionListener(imageHandler);
        selectStartLocationButton.addActionListener(imageHandler);
        selectDestinationButton.addActionListener(imageHandler);
    }

    // REGISTERS HANDLER FOR EDITING THE LEVEL VIA THE CANVAS
    private void registerEditLevelController(PXLE_Model model)
    {
        PXLE_EditLevelController editLevelHandler = new PXLE_EditLevelController(model);
        canvas.addMouseListener(editLevelHandler);
        canvas.addMouseMotionListener(editLevelHandler);
    }
    
    // REGISTERS HANDLER FOR RESPONDING TO SPINNER INTERACTIONS
    private void registerSpinnerController(PXLE_Model model)
    {
        PXLE_SpinnerController spinnerHandler = new PXLE_SpinnerController(model);
        moneySpinner.addChangeListener(spinnerHandler);
        policeSpinner.addChangeListener(spinnerHandler);
        banditsSpinner.addChangeListener(spinnerHandler);
        zombiesSpinner.addChangeListener(spinnerHandler);
    }

    // ACCESSOR METHODS
    public PXLE_Canvas getCanvas()          { return canvas;                                                    }
    public int getCurrentMoney()            { return Integer.parseInt(moneySpinner.getValue().toString());      }
    public int getCurrentPolice()           { return Integer.parseInt(policeSpinner.getValue().toString());     }
    public int getCurrentBandits()          { return Integer.parseInt(banditsSpinner.getValue().toString());    }
    public int getCurrentZombies()          { return Integer.parseInt(zombiesSpinner.getValue().toString());    }
    public boolean isRefreshingSpinners()   { return refreshingSpinners;                                        }

    /**
     * GUI setup methods can be quite lengthy and repetitive so it helps to
     * create helper methods that can do a bunch of things at once. This method
     * creates a button with a bunch of pre-made values.
     */
    private JButton initToolbarButton(  String imageFile,
                                        String tooltip)
    {
        // LOAD THE IMAGE AND MAKE AN ICON
        Image img = loadImage(imageFile);
        ImageIcon ii = new ImageIcon(img);

        // MAKE THE BUTTON THAT WE'LL END UP RETURNING
        JButton createdButton = new JButton();

        // AND THEN PUT IT IN THE TOOLBAR
        northToolbar.add(createdButton);
        
        // NOW SETUP OUR BUTTON FOR USE
        // GIVE IT THE ICON
        createdButton.setIcon(ii);

        // GIVE IT THE MOUSE-OVER TEXT
        createdButton.setToolTipText(tooltip);

        // AND THIS WILL HELP US KNOW WHICH BUTTON IT IS WHEN PRESSED
        createdButton.setActionCommand(tooltip);

        // INSETS ARE SPACING INSIDE THE BUTTON,
        // TOP LEFT RIGHT BOTTOM
        Insets buttonMargin = new Insets(
                BUTTON_INSETS, BUTTON_INSETS, BUTTON_INSETS, BUTTON_INSETS);
        createdButton.setMargin(buttonMargin);

        // AND RETURN THE CREATED BUTTON
        return createdButton;
    }
    
    // HELPER METHOD FOR MAKING THE SPINNERS
    private JSpinner initToolbarLabeledSpinner(  String labelImageFile, String command,
                                                int min, int max, int step, int value)
    {
        // WE'll PUT THE LABEL AND THE SPINNER IN HERE
        JPanel labeledSpinnerPanel = new JPanel();
        labeledSpinnerPanel.setBorder(LABELED_SPINNER_PANEL_BORDER);
        labeledSpinnerPanel.setBackground(Color.WHITE);
        
        // FIRST MAKE AND ADD THE LABEL
        Image labelImage = loadImage(labelImageFile);
        ImageIcon ii = new ImageIcon(labelImage);
        JLabel label = new JLabel(ii);
        labeledSpinnerPanel.add(label);

        // THEN THE SPINNER
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel();
        spinnerModel.setMinimum(min);
        spinnerModel.setMaximum(max);
        spinnerModel.setStepSize(step);
        spinnerModel.setValue(value);
        JSpinner spinner = new JSpinner(spinnerModel);
        spinner.setToolTipText(command);
        labeledSpinnerPanel.add(spinner);
        
        // NOW ADD THE PANEL TO THE TOOLBAR
        northToolbar.add(labeledSpinnerPanel);
        
        // AND THE ONLY THING WE NEED TO ACCESS FROM HERE ON
        // OUT IS THE SPINNER WHEN RESPONDING TO INTERACTIONS AS
        // WELL AS TO ENABLE/DISABLE IT
        return spinner;
    }

    /**
     * Resets the buttons in the toolbar as though the app just loaded.
     */
    public void resetButtons()
    {
        // MAKE SURE THE BUTTONS START IN THE CORRECT ENABLED/DISABLED STATES
        newButton.setEnabled(true);
        openButton.setEnabled(true);
        saveButton.setEnabled(false);
        saveAsButton.setEnabled(false);
        exitButton.setEnabled(true);
        enableEditButtons(false);
    }

    /**
     * Enables/disables the save as button.
     */
    public void enableSaveAsButton(boolean b)
    {
        saveAsButton.setEnabled(b);
    }

    /**
     * Enables/disables the save button.
     */
    public void enableSaveButton(boolean b)
    {
        saveButton.setEnabled(b);
    }

    /**
     * Updates the UI cursor according to the current edit mode.
     */
    public void updateCursor(PXLE_EditMode editMode)
    {
        Cursor cursorToUse = cursors.get(editMode);
        this.setCursor(cursorToUse);
    }

    /**
     * Enables/disables the edit buttons.
     */
    public void enableEditButtons(boolean enable)
    {
        selectBackgroundButton.setEnabled(enable);
        selectStartLocationButton.setEnabled(enable);
        selectDestinationButton.setEnabled(enable);
        moneySpinner.setEnabled(enable);
        policeSpinner.setEnabled(enable);
        banditsSpinner.setEnabled(enable);
        zombiesSpinner.setEnabled(enable);
    }
    
    /**
     * Loads and returns an image.
     */
    public Image loadImage(String imageFileNameWithPath)
    {
        Toolkit tk = Toolkit.getDefaultToolkit();
        Image img = tk.getImage(imageFileNameWithPath);
        MediaTracker mt = new MediaTracker(this);
        mt.addImage(img, 0);
        try { mt.waitForID(0); }
        catch(InterruptedException ie)         
        { /* THIS SHOULD NEVER HAPPEN */ ie.printStackTrace(); }
        return img;
    }
    
    /**
     * Refreshes the spinners with the latest data from the level
     * being loaded.
     */
    public void refreshSpinners(PXLE_Level level)
    {
        refreshingSpinners = true;
        moneySpinner.setValue(level.getMoney());
        policeSpinner.setValue(level.getNumPolice());
        banditsSpinner.setValue(level.getNumBandits());
        zombiesSpinner.setValue(level.getNumZombies());
        refreshingSpinners = false;
    }            
}