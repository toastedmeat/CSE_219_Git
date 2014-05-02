package pathX.level_editor.model;

/**
 * This enum helps the model keep track of what edit operation
 * the user is currently doing so that the UI can provide the
 * appropriate response.
 * 
 * @author Richard McKenna
 */
public enum PXLE_EditMode
{
    NOTHING_SELECTED,
    INTERSECTION_SELECTED,
    INTERSECTION_DRAGGED,
    ROAD_SELECTED,
    ADDING_INTERSECTION,
    ADDING_ROAD_START,
    ADDING_ROAD_END
}
