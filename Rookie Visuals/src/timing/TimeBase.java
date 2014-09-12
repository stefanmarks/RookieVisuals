package timing;

/**
 * Class for managing the time and framerate.
 * 
 * @author  Stefan Marks
 */
public class TimeBase
{
    public TimeBase(int frameRate)
    {
        this.frameRate = frameRate;
        this.frameTime = 1.0f / frameRate;
    }
    
    
    public void tick()
    {
        // turn nanosecond
        absTime = System.currentTimeMillis()/ 1000.0f;
    }
    
    
    public final int   frameRate;
    public final float frameTime;
    public       float absTime;
}
