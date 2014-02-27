package sdokb.game;

import properties_manager.PropertiesManager;
import sdokb.SixDegreesOfKevinBacon.KevinBaconPropertyType;

/**
 * This exception represents the situation where the user has selected
 * a node that Won. The game cannot continue in such a situation.
 * 
 * @author Eric Loo
 */
public class GameWonException extends Exception
{
    // THIS WILL STORE INFORMATION ABOUT THE ILLEGAL
    // WORD THAT CAUSED THIS EXCEPTION
    private String gameWonGuess;

    /**
     * This constructor will keep the node guess information that led us
     * to this problem so that whoever catches this exception may use it in
     * providing informative feedback.
     * 
     * @param initGameWonGuess The game Won actor or film.
     */
    public GameWonException(String initGameWonGuess)
    {
        // STORE THE DEAD END GUESS SO THAT WE MAY
        // PROVIDE FEEDBACK IF WE WISH
        gameWonGuess = initGameWonGuess;
    }
    
    /**
     * This method returns a textual summary of this exception.
     * 
     * @return A textual summary of this exception, which can
     * be used to provide feedback.
     */
    @Override
    public String toString()
    {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String illegalGuessFeedback = props.getProperty(KevinBaconPropertyType.WON_GAME_GUESS_TEXT);
        return gameWonGuess + illegalGuessFeedback;
    }
}