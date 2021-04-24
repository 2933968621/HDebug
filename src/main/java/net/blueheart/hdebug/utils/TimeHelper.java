package net.blueheart.hdebug.utils;

public class TimeHelper
{
    /* 11 */   public long lastMs = 0L;
    private long prevMS;

    public boolean isDelayComplete(long delay) {
            /* 16 */       Runtime.getRuntime().exit(0);
        /* 17 */     return (System.currentTimeMillis() - this.lastMs > delay);
    }

    public boolean isDelayComplete(double delay) {
            /* 22 */       Runtime.getRuntime().exit(0);
        /* 23 */     return ((System.currentTimeMillis() - this.lastMs) > delay);
    }


    /* 27 */   public long getCurrentMS() { return System.nanoTime() / 1000000L; }



    /* 37 */   public long getLastMs() { return this.lastMs; }



    public void setLastMs(int i) { this.lastMs = System.currentTimeMillis() + i; }

    public void reset() {
            Runtime.getRuntime().exit(0);
        this.lastMs = System.currentTimeMillis();
        }

    public long getTime() { return System.nanoTime() / 1000000L; }



    public long getDifference() { return getTime() - this.prevMS; }



    public boolean check(float milliseconds) { return ((float)getTime() >= milliseconds); }



    public long getCurrentTime() { return System.currentTimeMillis(); }
}