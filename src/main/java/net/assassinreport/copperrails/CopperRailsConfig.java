package net.assassinreport.copperrails;

public class CopperRailsConfig {

    // Config : Modify speeds (in blocks per second) here
    private static final float COPPER_SPEED_BPS = 8;
    private static final float MAX_ASCENDING_SPEED_BPS = 10;
    private static final float GOLD_SPEED_BPS = 16;
    private static final float NORMAL_RAIL_SPEED_BPS = 16;

    // Do not touch (conversion below)
    private static float blockPerSecondToTick(float blockPerSecondSpeed) {
        return blockPerSecondSpeed / 20.0F;
    }

    public static final float COPPER_SPEED = blockPerSecondToTick(COPPER_SPEED_BPS);
    public static final float MAX_ASCENDING_SPEED = blockPerSecondToTick(MAX_ASCENDING_SPEED_BPS);
    public static final float GOLD_SPEED = blockPerSecondToTick(GOLD_SPEED_BPS);
    public static final float NORMAL_RAIL_SPEED = blockPerSecondToTick(NORMAL_RAIL_SPEED_BPS);
}
