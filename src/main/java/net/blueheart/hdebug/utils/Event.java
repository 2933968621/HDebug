package net.blueheart.hdebug.utils;

public abstract class Event {
    private boolean cancelled;
    private boolean skipFutureCalls;
    public static byte type;

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public boolean skippingFutureCalls() {
        return this.skipFutureCalls;
    }

    public void skipFutureCalls(boolean skipFutureCalls) {
        this.skipFutureCalls = skipFutureCalls;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        Event.type = type;
    }
}
