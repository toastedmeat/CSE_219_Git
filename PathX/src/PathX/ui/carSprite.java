/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PathX.ui;

import PathX.data.Intersection;
import PathX.data.Road;
import mini_game.MiniGame;
import mini_game.Sprite;
import mini_game.SpriteType;

/**
 *
 * @author Eric
 */
public class carSprite extends Sprite {
    // EACH TILE HAS AN ID, WHICH WE'LL USE FOR SORTING

    private int tileId;

    private int gridColumn;
    private int gridRow;

    private boolean movingToTarget;

    private float targetX;
    private float targetY;

    private Intersection currentIntersection;
    private Intersection nextIntersection;
    private Road currentRoad;

    carSprite(SpriteType initSpriteType,
            float initX, float initY,
            float initVx, float initVy,
            String initState) {
        // SEND ALL THE Sprite DATA TO A Sprite CONSTRUCTOR
        super(initSpriteType, initX, initY, initVx, initVy, initState);

    }

    public int getTileId() {
        return tileId;
    }

    public int getGridColumn() {
        return gridColumn;
    }

    public int getGridRow() {
        return gridRow;
    }

    public float getTargetX() {
        return targetX;
    }

    public float getTargetY() {
        return targetY;
    }

    public Road getCurrentRoad() {
        return currentRoad;
    }

    public Intersection getCurrentIntersection() {
        return currentIntersection;
    }

    public Intersection getNextIntersection() {
        return nextIntersection;
    }

    public void setNextIntersection(Intersection nextIntersection) {
        this.nextIntersection = nextIntersection;
    }

    public boolean isMovingToTarget() {
        return movingToTarget;
    }

    public void setTarget(float initTargetX, float initTargetY) {
        targetX = initTargetX;
        targetY = initTargetY;
    }

    public void setCurrentIntersection(Intersection i) {
        currentIntersection = i;
    }

    public void setCurrentRoad(Road r) {
        currentRoad = r;
    }

    public float calculateDistanceToTarget() {
        // GET THE X-AXIS DISTANCE TO GO
        float diffX = targetX - x;

        // AND THE Y-AXIS DISTANCE TO GO
        float diffY = targetY - y;

        // AND EMPLOY THE PYTHAGOREAN THEOREM TO CALCULATE THE DISTANCE
        float distance = (float) Math.sqrt((diffX * diffX) + (diffY * diffY));

        // AND RETURN THE DISTANCE
        return distance;
    }

    public void startMovingToTarget(int maxVelocity) {
        // LET ITS POSITIONG GET UPDATED
        movingToTarget = true;

        // CALCULATE THE ANGLE OF THE TRAJECTORY TO THE TARGET
        float diffX = targetX - x;
        float diffY = targetY - y;
        float tanResult = diffY / diffX;
        float angleInRadians = (float) Math.atan(tanResult);

        // COMPUTE THE X VELOCITY COMPONENT
        vX = (float) (maxVelocity * Math.cos(angleInRadians));

        // CLAMP THE VELOCTY IN CASE OF NEGATIVE ANGLES
        if ((diffX < 0) && (vX > 0)) {
            vX *= -1;
        }
        if ((diffX > 0) && (vX < 0)) {
            vX *= -1;
        }

        // COMPUTE THE Y VELOCITY COMPONENT
        vY = (float) (maxVelocity * Math.sin(angleInRadians));

        // CLAMP THE VELOCITY IN CASE OF NEGATIVE ANGLES
        if ((diffY < 0) && (vY > 0)) {
            vY *= -1;
        }
        if ((diffY > 0) && (vY < 0)) {
            vY *= -1;
        }
    }

    /**
     * This is good ^^
     *
     * @param game
     */
    @Override
    public void update(MiniGame game) {
        if ((x >= targetX - 5 && x <= targetX + 5) && (y >= targetY - 10 && y <= targetY + 10)) {
            movingToTarget = false;
            currentIntersection = nextIntersection;
        } else {
            // MOVE THE SPRITE USING ITS VELOCITY
            x += vX;
            y += vY;
        }

    }
}
