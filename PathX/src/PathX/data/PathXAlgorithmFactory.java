package PathX.data;

import java.util.ArrayList;
import java.util.HashMap;
import PathX.ui.PathXTile;
import static PathX.PathXConstants.*;

/**
 * This factory class builds the sorting algorithm objects to be used for
 * sorting in the game.
 *
 * @author Richard McKenna & Eric Loo
 */
public class PathXAlgorithmFactory {
    // STORES THE SORTING ALGORITHMS WE MAY WISH TO USE

    static HashMap<PathXAlgorithmType, PathXAlgorithm> premadeSortingHatAlgorithms = null;

    /**
     * For getting a particular sorting algorithm. Note that the first time it
     * is called it initializes all the sorting algorithms and puts them in a
     * hash map to be retrieved as needed to setup levels when loaded.
     */
    public static PathXAlgorithm buildSortingHatAlgorithm(PathXAlgorithmType algorithmType,
            ArrayList<PathXTile> initDataToSort) {
        // INIT ALL THE ALGORITHMS WE'LL USE IF IT HASN'T DONE SO ALREADY
        if (premadeSortingHatAlgorithms == null) {
            premadeSortingHatAlgorithms = new HashMap();
            premadeSortingHatAlgorithms.put(PathXAlgorithmType.BUBBLE_SORT,
                    new BubbleSortAlgorithm(initDataToSort,
                    PathXAlgorithmType.BUBBLE_SORT.toString()));
            premadeSortingHatAlgorithms.put(PathXAlgorithmType.SELECTION_SORT,
                    new SelectionSortAlgorithm(initDataToSort,
                    PathXAlgorithmType.SELECTION_SORT.toString()));
        }
        // RETURN THE REQUESTED ONE
        return premadeSortingHatAlgorithms.get(algorithmType);
    }
}

/**
 * This class builds all the transactions necessary for performing bubble sort
 * on the data structure. This can then be used to compare to student moves
 * during the game.
 */
class BubbleSortAlgorithm extends PathXAlgorithm {

    /**
     * Constructor only needs to init the inherited stuff.
     */
    public BubbleSortAlgorithm(ArrayList<PathXTile> initDataToSort, String initName) {
        // INVOKE THE PARENT CONSTRUCTOR
        super(initDataToSort, initName);
    }

    /**
     * Build and return all the transactions necessary to sort using bubble
     * sort.
     */
    public ArrayList<SortTransaction> generateSortTransactions() {
        // HERE'S THE LIST OF TRANSACTIONS
        ArrayList<SortTransaction> transactions = new ArrayList();

        // FIRST LET'S COPY THE DATA TO A TEMPORARY ArrayList
        ArrayList<PathXTile> copy = new ArrayList();
        for (int i = 0; i < dataToSort.size(); i++) {
            copy.add(dataToSort.get(i));
        }

        // NOW SORT THE TEMPORARY DATA STRUCTURE
        for (int i = copy.size() - 1; i > 0; i--) {
            for (int j = 0; j < i; j++) {
                // TEST j VERSUS j+1
                if (copy.get(j).getID() > copy.get(j + 1).getID()) {
                    // BUILD AND KEEP THE TRANSACTION
                    SortTransaction sT = new SortTransaction(j, j + 1);
                    transactions.add(sT);

                    // SWAP
                    PathXTile temp = copy.get(j);
                    copy.set(j, copy.get(j + 1));
                    copy.set(j + 1, temp);
                }
            }
        }
        return transactions;
    }
}

class SelectionSortAlgorithm extends PathXAlgorithm {

    /**
     * Constructor only needs to init the inherited stuff.
     */
    public SelectionSortAlgorithm(ArrayList<PathXTile> initDataToSort, String initName) {
        // INVOKE THE PARENT CONSTRUCTOR
        super(initDataToSort, initName);
    }

    /**
     * Build and return all the transactions necessary to sort using Selection
     * sort.
     */
    public ArrayList<SortTransaction> generateSortTransactions() {
        // HERE'S THE LIST OF TRANSACTIONS
        ArrayList<SortTransaction> transactions = new ArrayList();

        // FIRST LET'S COPY THE DATA TO A TEMPORARY ArrayList
        ArrayList<PathXTile> copy = new ArrayList();
        for (int i = 0; i < dataToSort.size(); i++) {
            copy.add(dataToSort.get(i));
        }

        // NOW SORT THE TEMPORARY DATA STRUCTURE
        int min = 0;
        int minSpot = 0;
        for (int i = 0; i < copy.size() - 1; i++) {
            min = copy.get(i).getTileId();
            minSpot = i;
            for (int j = i + 1; j < copy.size(); j++) {
                if (copy.get(j).getTileId() < min) {
                    min = copy.get(j).getTileId();
                    minSpot = j;
                } else {
                    
                }
            }
            // BUILD AND KEEP THE TRANSACTION
            if (minSpot != i) {
                SortTransaction sT = new SortTransaction(i, minSpot);
                transactions.add(sT);

                // SWAP
                PathXTile temp = copy.get(i);
                copy.set(i, copy.get(minSpot));
                copy.set(minSpot, temp);
            }

        }
        return transactions;
    }
}
