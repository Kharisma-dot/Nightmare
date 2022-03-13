package nightmare.utils;

import net.minecraft.util.MathHelper;

public class TimerUtils {
	
    private long lastMS;
    private long previousTime;

    public TimerUtils() {
        this.lastMS = 0L;
        this.previousTime = -1L;
    }

    public boolean sleep(long time) {
        if (time() >= time) {
            reset();
            return true;
        }

        return false;
    }

    public boolean check(float milliseconds) {
        return System.currentTimeMillis() - previousTime >= milliseconds;
    }

    public boolean delay(double milliseconds) {
        return MathHelper.clamp_float(getCurrentMS() - lastMS, 0, (float) milliseconds) >= milliseconds;
    }

    public void reset() {
        this.previousTime = System.currentTimeMillis();
        this.lastMS = getCurrentMS();
    }

    public long time() {
        return System.nanoTime() / 1000000L - lastMS;
    }

    public long getCurrentMS() {
        return System.nanoTime() / 1000000L;
    }

    public double getLastDelay () {
        return getCurrentMS() - getLastMS();
    }
    
    public boolean hasReached(double d) {
        return getCurrentMS() - lastMS >= d;
    }

    public long getLastMS() {
        return lastMS;
    }
}