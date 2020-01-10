package util;

public class PointNode extends Node {

    private  int startTime;

    public PointNode(int startTime, int intensity, LED holder)
    {
        super(holder);
        this.startTime = startTime;
        setIntensity(intensity);
    }

    @Override
    public int getStartTime() {
        return startTime;
    }

    @Override
    public int getEndTime() {
        return startTime;
    }

    @Override
    public void setStartTime(int a, int maxTimePermitted) {


    }

    @Override
    public void setEndTime(int a, int maxTimePermitted) {

    }
}
