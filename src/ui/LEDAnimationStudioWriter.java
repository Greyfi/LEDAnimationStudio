package ui;

import util.StudioApplication;

import javax.swing.*;
import java.awt.*;

//To play an audio file
//CustomClasses
//import java.awt.image.BufferedImage;
//import java.awt.Point;


/**
 * A class that creates a JFrame, and a util.LEDStudioMediaPlayerHolder and prepares them for use.
 * @author Grey
 */
public class LEDAnimationStudioWriter {
    private static Thread mediaThread;
    private static StudioApplication studioCore;

    /**
     * constructs a new JFrame and sets up additional settings
     * @return Returns a JFrame with a set title "Audio ui.LEDAnimationStudioWriter" with a starting size of 640x400 pixels.
     */
    private static JFrame createFrame()
    {
        //System.out.println("Creating a frame with XxY size.");//for debug
        JFrame frame = new JFrame();
        frame.setMinimumSize(new Dimension(1000, 800));
        frame.setSize(1000,800);
        frame.setTitle("Studio");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        return frame;
    }

    /**
     * sets a frame to visible and also adds the visualizer component to the frame. This allows the user to start interacting with the UI
     * @param frame a pre-created frame that will be modified to be visible and hold the visualizer component
     */
    private static void openUI(JFrame frame)
    {
        JSlider slider = new JSlider(0,100,0);
        JPanel buttonsForNow = new JPanel();
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

        //Adds Slider to the bottom
        panel.add(slider, BorderLayout.PAGE_END);

        //Frame initialization
        frame.add(panel);
        frame.setVisible(true);
    }


    public static void main(String[] args)
    {
        //System.setProperty("sun.java2d.opengl", "true");
        boolean debug = false;
        //lineArguments bool setter
        for(int i=0; i<args.length; i++)
        {
            debug = args[i].equals("-debug");
        }
        studioCore = new StudioApplication(debug);
        mediaThread = new Thread(studioCore);
        mediaThread.start();
        JFrame fr = createFrame();
        openUI(fr);


    }
}
