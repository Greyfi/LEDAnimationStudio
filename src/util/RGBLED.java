package util;

public class RGBLED {
    int core;
    boolean isAnalog;
    LED r;
    LED g;
    LED b;

    public RGBLED(int core, boolean isAnalog)
    {
        this.core = core;
        this.isAnalog = isAnalog;
        r = new LED(0, core, isAnalog);
    }
}
