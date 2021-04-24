package net.blueheart.hdebug.utils;

public class TimerUtil {
    private long lastMS;

    private long getCurrentMS() {
        return System.nanoTime() / 1000000L;
    }

    public boolean hasReached(double milliseconds) {
        return (double)(this.getCurrentMS() - this.lastMS) >= milliseconds;
    }

    public boolean hasReached(long milliseconds) {
        return (double)(this.getCurrentMS() - this.lastMS) >= (double)milliseconds;
    }

    public boolean reach(final Float time) {
        return time() >= time;
    }
    public void reset() {
        this.lastMS = this.getCurrentMS();
    }
    public long time() {
        return System.nanoTime() / 1000000L - time();
    }
    public boolean delay(float milliSec) {
        return (float)(getTime() - this.lastMS) >= milliSec;
    }

    public static long getTime() {
        return System.nanoTime() / 1000000L;
    }

    public boolean sleep(long time) {
        if(getTime() >= time) {
            this.reset();
            return true;
        } else {
            return false;
        }
    }

    public boolean sleep(double time) {
        if((double)getTime() >= time) {
            this.reset();
            return true;
        } else {
            return false;
        }
    }
}