/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PathX.ui;

import PathX.data.Intersection;
import PathX.data.Road;
import java.util.Iterator;
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
    private boolean reachedDestination;

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

    public boolean isReachedDestination() {
        return reachedDestination;
    }

    public void setReachedDestination(boolean reachedDestination) {
        this.reachedDestination = reachedDestination;
    }

    public void setNextIntersection(Intersection nextIntersection) {
        this.nextIntersection = nextIntersection;
    }

    public boolean isMovingToTarget() {
        return movingToTarget;
    }
    
    public void setMovingToTarget(boolean m) {
        movingToTarget = m;
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
    //@Override
    public void update(PathXMiniGame game) {
        //System.out.println("X: " + x + " TargetX: " + targetX + " Y: " + y + " TargetY: " + targetY);
        if ((x >= targetX - 20 && x <= targetX + 20) && (y >= targetY - 50 && y <= targetY + 50)) {
            reachedDestination = true;
            movingToTarget = false;
            currentIntersection = nextIntersection;
        } else {
            // MOVE THE SPRITE USING ITS VELOCITY
            //System.out.println("vX: " + vX + " vY: " + vY);
            x += vX;
            y += vY;
        }

    }
}
