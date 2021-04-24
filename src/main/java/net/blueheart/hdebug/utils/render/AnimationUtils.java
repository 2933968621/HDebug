package net.blueheart.hdebug.utils.render;

public class AnimationUtils {

    public static float easeOut(float t, float d) {
        return (t = t / d - 1) * t * t + 1;
    }
}
