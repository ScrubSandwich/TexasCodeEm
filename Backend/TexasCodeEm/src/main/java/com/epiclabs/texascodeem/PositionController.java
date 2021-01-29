package com.epiclabs.texascodeem;

import com.epiclabs.texascodeem.api.Values;

import javax.swing.text.Position;
import java.util.List;
import java.util.ArrayList;

public class PositionController {

    private static PositionController instance;

    // Index of the List denotes the table position
    // Integer of the List @ a position denotes the userId
    private List<Integer> takenPositions;

    private PositionController() {
        takenPositions = new ArrayList<>();

        // Initialize takePositions to 0's
        for (int i = 0; i < Values.NUMBER_OF_PLAYERS; i++) {
            takenPositions.add(new Integer(0));
        }
    }

    public static PositionController getInstance() {
        if (instance == null) {
            instance = new PositionController();
        }

        return instance;
    }

    public int findPosition(int userId) throws Exception {
        for (int i = 0; i < takenPositions.size(); i++) {
            if (takenPositions.get(i) == 0) {
                takenPositions.add(i, userId);

                return i;
            }
        }

        throw new Exception("Unable to get user " + userId + " a position");
    }

    public int getPosition(int userId) {
        return takenPositions.indexOf(new Integer(userId));
    }

    public void clearPositions() {
        takenPositions = new ArrayList<>(Values.NUMBER_OF_PLAYERS);
    }
}
