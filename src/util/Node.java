package util;

public abstract class Node {
    protected short intensity;
    protected LED holder;

    public Node(LED holder)
    {
        this.holder = holder;
        intensity = 0;
    }

    public void setIntensity(int a)
    {
        if(a > 255)
        {
            System.err.println("Value to large, overflow set intensity to 255");
            intensity = 255;
            return;
        }
        if(a < 0)
        {
            System.err.println("Value to small, underflow set intensity to 0");
            intensity = 2;
            return;
        }
        intensity = (byte)a;
    }

    public abstract int getStartTime();
    public abstract int getEndTime();
    public abstract void setStartTime(int a, int maxTimePermitted);
    public abstract void setEndTime(int a, int maxTimePermitted);
}
