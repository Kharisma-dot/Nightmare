package nightmare.utils.animation;

public class Animation
{
    private float x;
    private float y;
    private long lastMS;

    public Animation(final float x, final float y)
    {
        this.x = x;
        this.y = y;
        this.lastMS = System.currentTimeMillis();
    }
    
    public void interpolate(final float targetX, final float targetY, final double speed)
    {
        final long currentMS = System.currentTimeMillis();
        final long delta = currentMS - this.lastMS;
        this.lastMS = currentMS;
        double deltaX = 0.0;
        double deltaY = 0.0;

        if (speed != 0.0)
        {
            deltaX = Math.abs(targetX - this.x) * 0.35f / (10.0 / speed);
            deltaY = Math.abs(targetY - this.y) * 0.35f / (10.0 / speed);
        }

        this.x = AnimationUtils.calculateCompensation(targetX, this.x, delta, deltaX);
        this.y = AnimationUtils.calculateCompensation(targetY, this.y, delta, deltaY);
    }

    public float getX()
    {
        return this.x;
    }

    public void setX(final float x)
    {
        this.x = x;
    }

    public float getY()
    {
        return this.y;
    }

    public void setY(final float y)
    {
        this.y = y;
    }
}