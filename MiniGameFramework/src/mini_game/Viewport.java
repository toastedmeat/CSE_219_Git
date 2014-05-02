package mini_game;

/**
 *
 * @author McKillaGorilla
 */
public class Viewport
{
    // THIS IS THE VIEWPORT ZOOM LEVEL
    private float zoomLevel;
    
    // @todo - change this variable name
    private int northPanelHeight;
    
    // THIS IS THE FULL RENDERABLE PANEL DIMENSIONS, ON WHICH WE MAY RENDER
    // OUR GUI TOOLBARS AND CONTROLS AND THE ENTIRE GAME
    private int screenWidth;
    private int screenHeight;

    // THIS IS THE SIZE OF THE GAME WORLD, WHICH MAY BE MUCH
    // LARGER THAN THE SCREEN DIMENSIONS, SINCE WE CAN SCROLL
    // THE VIEWPORT TO SEE EVERYTHING
    private int gameWorldWidth;
    private int gameWorldHeight;

    // THIS IS THE SIZE OF THE VIEWPORT
    protected int viewportWidth;
    protected int viewportHeight;
    
    // THIS IS THE MARGIN OUTSIDE THE VIEWPORT BETWEEN AREA IT
    // AND THE EDGE OF THE RENDERABLE AREA
    protected int viewportMarginLeft;
    protected int viewportMarginRight;
    protected int viewportMarginTop;
    protected int viewportMarginBottom;

    // THIS IS THE POSITION OF THE VIEWPORT IN
    // GAME WORLD COORDINATES
    protected int viewportX;
    protected int viewportY;
    
    // THESE KEEP TRACK OF THE MIN/MAX GAME WORLD
    // COORDINATES FOR OUR VIEWPORT DURING SCROLLING
    protected int minViewportX;
    protected int maxViewportX;
    protected int minViewportY;
    protected int maxViewportY;
    // WIDTH AND HEIGHT OF THE VIEWPORT WINDOW
    public int width;
    public int height;

    public Viewport()
    {
        zoomLevel = 1.0f;
        viewportX = 0;
        viewportY = 0;
        minViewportX = 0;
        minViewportY = 0;
        width = 0;
        height = 0;
    }

    // ACCESSOR METHODS
    public int getScreenWidth()             {       return screenWidth;            }
    public int getScreenHeight()            {       return screenHeight;           }
    public int getGameWorldWidth()          {       return gameWorldWidth;         }
    public int getGameWorldHeight()         {       return gameWorldHeight;        }
    public int getViewportWidth()           {       return viewportWidth;          }
    public int getViewportHeight()          {       return viewportHeight;         }
    public int getViewportMarginLeft()      {       return viewportMarginLeft;     }
    public int getViewportMarginRight()     {       return viewportMarginRight;    }
    public int getViewportMarginTop()       {       return viewportMarginTop;      }
    public int getViewportMarginBottom()    {       return viewportMarginBottom;   }
    public int getViewportX()               {       return viewportX;              }
    public int getViewportY()               {       return viewportY;              }
    public int getMinViewportX()            {       return minViewportX;           }
    public int getMaxViewportX()            {       return maxViewportX;           }
    public int getMinViewportY()            {       return minViewportY;           }
    public int getMaxViewportY()            {       return maxViewportY;           }
    public float getZoomLevel()             {       return zoomLevel;              }
    
    public void setScreenSize(int initScreenWidth, int initScreenHeight)
    {
        screenWidth = initScreenWidth;
        screenHeight = initScreenHeight;
    }
    
    public void setGameWorldSize(int initGameWorldWidth, int initGameWorldHeight)
    {
        gameWorldWidth = initGameWorldWidth;            
        gameWorldHeight = initGameWorldHeight;        
    }
    
    public void setViewportSize(int initViewportWidth, int initViewportHeight)
    {
        viewportWidth = initViewportWidth;              
        viewportHeight = initViewportHeight;        
    }
    
    public void setNorthPanelHeight(int initNorthPanelHeight)
    {
        northPanelHeight = initNorthPanelHeight;
    }

    public void initViewportMargins()
    {
        viewportMarginLeft = (screenWidth/2) - (gameWorldWidth/2);
        viewportMarginRight = viewportMarginLeft;
        viewportHeight = screenHeight - northPanelHeight;
        viewportMarginTop = northPanelHeight + (viewportHeight/2) - (gameWorldHeight/2);
        viewportMarginBottom = viewportMarginTop;
    }
    
    public void updateViewportBoundaries()
    {
        viewportWidth = screenWidth - viewportMarginLeft - viewportMarginRight;
        viewportHeight = screenHeight - viewportMarginTop - viewportMarginBottom;
        maxViewportX = gameWorldWidth - viewportWidth - 1;
        maxViewportY = gameWorldHeight - viewportHeight - 1;
    }

    public void scroll(int incX, int incY)
    {
        // MOVE
        viewportX += incX;
        viewportY += incY;
    }
    public void setViewportDimensions(int initWidth, int initHeight)
    {
        width = initWidth;
        height = initHeight;
        
        if (width < screenWidth)
            screenWidth = width;
        if (height < screenHeight)
            screenHeight = height;
    }
    
    public void setLevelDimensions(int initLevelWidth, int initLevelHeight)
    {
        if ((initLevelWidth >= width) && (initLevelHeight >= height))
        {
            screenWidth = initLevelWidth;
            screenHeight = initLevelHeight;
        }
    }
    public void reset()
    {
        viewportX = 0;
        viewportY = 0;
    }

    public boolean isRectInsideViewport(int x1, int y1, int x2, int y2)
    {
        if (x2 < viewportX) return false;
        if (x1 > (viewportX + width)) return false;
        if (y2 < viewportY) return false;
        if (y1 > (viewportY + height)) return false;
        return true;
    }
    
     /**
     * Returns true if the circle argument's bounding box is inside the
     * viewport, false otherwise.
     */
    public boolean isCircleBoundingBoxInsideViewport(int centerX, int centerY, int radius)
    {
        return isRectInsideViewport(centerX-radius, centerY-radius, centerX+radius, centerY+radius);
    }
    
    public void move(int incX, int incY)
    {
        viewportX += incX;
        viewportY += incY;
        if (viewportX < 0) viewportX = 0;
        if (viewportY < -180) viewportY = -180;
        if (viewportX > (screenWidth - width))
            viewportX = screenWidth - width;
        if (viewportY > (screenHeight - height))
            viewportY = screenHeight - height;
    }
}