/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PathX.ui;

import PathX.PathX;
import static PathX.PathXConstants.*;
import PathX.data.Intersection;
import PathX.data.Road;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
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
    
    private boolean gasTank;
    private boolean tireCondition;

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

        int playerX = (int) game.getGUIEnemies().get(PLAYER_TYPE).getX();
        int playerY = (int) game.getGUIEnemies().get(PLAYER_TYPE).getY();

        if ((x >= targetX - 20 && x <= targetX + 20) && (y >= targetY - 30 && y <= targetY + 30)) {
            reachedDestination = true;
            movingToTarget = false;
            currentIntersection = nextIntersection;
        } else {
            // MOVE THE SPRITE USING ITS VELOCITY
            //System.out.println("vX: " + vX + " vY: " + vY);
            x += vX;
            y += vY;
        }
        if (game.getData().calculateDistanceBetweenPoints(playerX, playerY, (int) game.getData().getDestination().getX(), (int) game.getData().getDestination().getY()) < 20) {
            Object[] options = {"TRY AGAIN", "LEAVE TOWN"};
            if(!game.isSoundMuted()){
                game.getAudio().play(PathX.pathXPropertyType.AUDIO_CUE_WIN.toString(), false);
            }
            
            if (JOptionPane.showOptionDialog(null, "Good News! \nYou've robbed the Bank. That means\nYou have earned $" + game.getData().getLevel().getMoney(), "You got Away!",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                    null, options, options[0]) == 0) {
                game.getData().setTotalMoney(game.getData().getTotalMoney() + game.getData().getLevel().getMoney());
                int currentLevel = Integer.parseInt(game.getData().getCurrentLevel());
                game.getData().getLevelsRobbed()[currentLevel - 1] = true;
                game.getData().getLevelsLocked()[currentLevel] = false;
                game.switchToGameScreen();
                switcher(game);
            } else {
                game.getData().setTotalMoney(game.getData().getTotalMoney() + game.getData().getLevel().getMoney());
                int currentLevel = Integer.parseInt(game.getData().getCurrentLevel());
                game.getData().getLevelsRobbed()[currentLevel - 1] = true;
                game.getData().getLevelsLocked()[currentLevel] = false;
                game.switchToGameScreen();
            }
        }
        Collection<carSprite> buttonSprites = game.getGUIEnemies().values();
        for (carSprite s : buttonSprites) {
            if (s.getSpriteType().getSpriteTypeID().equals(POLICE_TYPE)) {
                if (game.getData().calculateDistanceBetweenPoints(playerX, playerY, (int) s.getX(), (int) s.getY()) < 30
                        || game.getData().calculateDistanceBetweenPoints((int) s.getX(), (int) s.getY(), playerX, playerY) < 30) {
                    Object[] options = {"TRY AGAIN", "LEAVE TOWN"};
                    if (!game.isSoundMuted()) {
                        game.getAudio().play(PathX.pathXPropertyType.AUDIO_CUE_CRASH.toString(), false);
                    }

                    if (JOptionPane.showOptionDialog(null, "Bad News! \nYou've been caught. That means\nYou have some legal bills to pay.", "You have been caught!",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                            null, options, options[0]) == 0) {
                        double money = game.getData().getTotalMoney() - (game.getData().getTotalMoney() * .1);
                        game.getData().setTotalMoney((int) money);
                        switcher(game);
                    } else {
                        double money = game.getData().getTotalMoney() - (game.getData().getTotalMoney() * .1);
                        game.getData().setTotalMoney((int) money);
                        game.switchToGameScreen();
                    }
                }
            }
        }
        for (carSprite s : buttonSprites) {
            if (s.getSpriteType().getSpriteTypeID().equals(ZOMBIE_TYPE)) {
                if ((game.getData().calculateDistanceBetweenPoints(playerX, playerY, (int) s.getX(), (int) s.getY()) < 30
                        || game.getData().calculateDistanceBetweenPoints((int) s.getX(), (int) s.getY(), playerX, playerY) < 30)
                        && !game.getData().isHitOnce()) {
                    if (!game.isSoundMuted()) {
                        game.getAudio().play(PathX.pathXPropertyType.AUDIO_CUE_CRASH.toString(), false);
                    }

                    game.getData().setSpeed((int) (game.getData().getSpeed() - game.getData().getSpeed() * .1));
                    game.getData().setHitOnce(true);
                }
                if (game.getData().calculateDistanceBetweenPoints(playerX, playerY, (int) s.getX(), (int) s.getY()) > 91
                        && game.getData().calculateDistanceBetweenPoints((int) s.getX(), (int) s.getY(), playerX, playerY) > 91) {
                    game.getData().setHitOnce(false);
                }
            }
        }
        for (carSprite s : buttonSprites) {
            if (s.getSpriteType().getSpriteTypeID().equals(BANDIT_TYPE)) {

                if ((game.getData().calculateDistanceBetweenPoints(playerX, playerY, (int) s.getX(), (int) s.getY()) < 40
                        || game.getData().calculateDistanceBetweenPoints((int) s.getX(), (int) s.getY(), playerX, playerY) < 40)
                        && !game.getData().isHitOnce()) {
                    if (!game.isSoundMuted()) {
                        game.getAudio().play(PathX.pathXPropertyType.AUDIO_CUE_CRASH.toString(), false);
                    }

                    game.getData().getLevel().setMoney((int) (game.getData().getLevel().getMoney() - game.getData().getLevel().getMoney() * .1));
                    game.getData().setHitOnce(true);
                }
                if (game.getData().calculateDistanceBetweenPoints(playerX, playerY, (int) s.getX(), (int) s.getY()) > 91
                        && game.getData().calculateDistanceBetweenPoints((int) s.getX(), (int) s.getY(), playerX, playerY) > 91) {
                    game.getData().setHitOnce(false);
                }
            }
        }

    }

    public void switcher(PathXMiniGame game) {
        switch (game.getData().getCurrentLevel()) {
            case "1":
                game.levelSetup();
                game.switchToLevel1();
                break;
            case "2":
                game.levelSetup();
                game.switchToLevel2();
                break;
            case "3":
                game.levelSetup();
                game.switchToLevel3();
                break;
            case "4":
                game.levelSetup();
                game.switchToLevel4();
                break;
            case "5":
                game.levelSetup();
                game.switchToLevel5();
                break;
            case "6":
                game.levelSetup();
                game.switchToLevel6();
                break;
            case "7":
                game.levelSetup();
                game.switchToLevel7();
                break;
            case "8":
                game.levelSetup();
                game.switchToLevel8();
                break;
            case "9":
                game.levelSetup();
                game.switchToLevel9();
                break;
            case "10":
                game.levelSetup();
                game.switchToLevel10();
                break;
            case "11":
                game.levelSetup();
                game.switchToLevel11();
                break;
            case "12":
                game.levelSetup();
                game.switchToLevel12();
                break;
            case "13":
                game.levelSetup();
                game.switchToLevel13();
                break;
            case "14":
                game.levelSetup();
                game.switchToLevel14();
                break;
            case "15":
                game.levelSetup();
                game.switchToLevel15();
                break;
            case "16":
                game.levelSetup();
                game.switchToLevel16();
                break;
            case "17":
                game.levelSetup();
                game.switchToLevel17();
                break;
            case "18":
                game.levelSetup();
                game.switchToLevel18();
                break;
            case "19":
                game.levelSetup();
                game.switchToLevel19();
                break;
            case "20":
                game.levelSetup();
                game.switchToLevel20();
                break;
            default:
                game.switchToGameScreen();
        }
    }
}
