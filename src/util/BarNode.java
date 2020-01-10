package util;

public class BarNode extends Node {

    private int startTime;
    private int endTime;

    public BarNode(int startTime, int intensity, LED holder)
    {
        super(holder);
        this.startTime = startTime;
        this.endTime = startTime;
        setIntensity(intensity);
    }

    @Override
    public int getStartTime() {
        return startTime;
    }

    @Override
    public int getEndTime() {
        return endTime;
    }

    @Override
    public void setStartTime(int a, int maxTimePermitted) {

    }

    /**
     * Checks holder if any node has a larger
     * @param a
     * @param maxTimePermitted
     */
    @Override
    public void setEndTime(int a, int maxTimePermitted) {

    }
}
