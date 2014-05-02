package PathX.data;

import java.awt.image.ImageObserver;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import PathX.ui.PathXMiniGame;

/**
 * This class represents the complete playing history for the player since
 * originally starting the application. Note that it stores stats separately for
 * different levels.
 *
 * @author Richard McKenna & Eric Loo
 */
public class PathXRecord {
    // HERE ARE ALL THE RECORDS
    private HashMap<String, PathXLevelRecord> levelRecords;
    
    /**
     * Default constructor, it simply creates the hash table for storing all the
     * records stored by level.
     */
    public PathXRecord() {
        levelRecords = new HashMap();
    }

    // GET METHODS
    // - getAlgorithm
    // - getGamesPlayed
    // - getWins
    // - getPerfectWins
    // - getFastestPerfectWinTime
    // - hasLevel
    /**
     * This method gets the algorithm for a given level.
     *
     * @param levelName Level for the request.
     *
     * @return The number of games played for the levelName level.
     */
    public String getAlgorithm(String levelName) {
        PathXLevelRecord rec = levelRecords.get(levelName);

        // IF levelName ISN'T IN THE RECORD OBJECT
        // THEN SIMPLY RETURN 0
        if (rec == null) {
            return null;
        } // OTHERWISE RETURN THE GAMES PLAYED
        else {
            return rec.algorithm;
        }
    }

    /**
     * This method gets the games played for a given level.
     *
     * @param levelName Level for the request.
     *
     * @return The number of games played for the levelName level.
     */
    public int getGamesPlayed(String levelName) {
        PathXLevelRecord rec = levelRecords.get(levelName);

        // IF levelName ISN'T IN THE RECORD OBJECT
        // THEN SIMPLY RETURN 0
        if (rec == null) {
            return 0;
        } // OTHERWISE RETURN THE GAMES PLAYED
        else {
            return rec.gamesPlayed;
        }
    }

    /**
     * This method gets the wins for a given level.
     *
     * @param levelName Level for the request.
     *
     * @return The wins the player has earned for the levelName level.
     */
    public int getWins(String levelName, boolean isPerfect) {
        PathXLevelRecord rec = levelRecords.get(levelName);

        // IF levelName ISN'T IN THE RECORD OBJECT
        // THEN SIMPLY RETURN 0        
        if (rec == null) {
            return 0;
        } // OTHERWISE RETURN THE WINS
        else if (!isPerfect) {
            return rec.wins;
        } else {
            return rec.perfectWins;
        }
    }
    
    public long getFastestWinTime(String levelName){
        PathXLevelRecord rec = levelRecords.get(levelName);
        return rec.fastestPerfectWinTime;
    }

    /**
     * Returns true if there is already a level called levelName, false
     * otherwise.
     */
    public boolean hasLevel(String levelName) {
        return levelRecords.containsKey(levelName);
    }


    // ADD METHODS
    // -addLevel
    // -addLoss
    // -addSortingHatLevelRecord
    // -addWin
    // -addPerfectWin
    /**
     * Adds a level record, which will be used to store the game results for a
     * particular level. This is used when adding a brand new level that hasn't
     * been played yet.
     */
    public void addLevel(String levelName, String algorithmName) {
        // MAKE A NEW RECORD FOR THIS LEVEL, SINCE THIS IS
        // THE FIRST TIME WE'VE PLAYED IT
        PathXLevelRecord rec = new PathXLevelRecord();
        rec.algorithm = algorithmName;
        rec.gamesPlayed = 0;
        rec.wins = 0;
        rec.perfectWins = 0;
        rec.fastestPerfectWinTime = 0;
        levelRecords.put(levelName, rec);
    }

    /**
     * We don't count losses, so all this does is add a game without adding a
     * win.
     */
    public void addLoss(String levelName) {
        levelRecords.get(levelName).gamesPlayed++;
    }

    /**
     * Adds the record for a level. This is used when loading records full of
     * data loaded from files.
     *
     * @param levelName
     *
     * @param rec
     */
    public void addSortingHatLevelRecord(String levelName, PathXLevelRecord rec) {
        levelRecords.put(levelName, rec);
    }

    // ADDITIONAL SERVICE METHODS
    // -toByteArray
    /**
     * This method constructs and fills in a byte array with all the necessary
     * data stored by this object. We do this because writing a byte array all
     * at once to a file is fast. Certainly much faster than writing to a file
     * across many write operations.
     *
     * @return A byte array filled in with all the data stored in this object,
     * which means all the player records in all the levels.
     *
     * @throws IOException Note that this method uses a stream that writes to an
     * internal byte array, not a file. So this exception should never happen.
     */
    public byte[] toByteArray() throws IOException {
        Iterator<String> keysIt = levelRecords.keySet().iterator();
        int numLevels = levelRecords.keySet().size();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(numLevels);
        while (keysIt.hasNext()) {
            // PACK IT WITH ALL THE DATA FOR THE RECORDS
            String key = keysIt.next();
            dos.writeUTF(key);
            PathXLevelRecord rec = levelRecords.get(key);
            dos.writeUTF(rec.algorithm.toString());
            dos.writeInt(rec.gamesPlayed);
            dos.writeInt(rec.wins);
            dos.writeInt(rec.perfectWins);
            dos.writeLong(rec.fastestPerfectWinTime);
        }
        // AND THEN RETURN IT
        return baos.toByteArray();
    }
}
