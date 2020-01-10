package util;

import javafx.scene.media.AudioSpectrumListener;
import javafx.util.Duration;

import static java.lang.Thread.sleep;

/**
 * A class responsible for initiating the Application Thread of util.LEDStudioMediaPlayerHolder
 * The Class is responsible as a communication interface for the MediaQueuer, VisualizerComponent with some of its children, and the util.LEDStudioMediaPlayerHolder
 */
public class StudioApplication implements Runnable {
    private LEDStudioMediaPlayerHolder player;
    private boolean isPlaying = false;
    private boolean debug;


    /**
     * Default constructor, which requires a boolean to check if it will start the util.StudioApplication in debug mode or not
     *
     * @param debug boolean value that determine if debug mode turned on.
     */
    public StudioApplication(boolean debug) {
        this.debug = debug;
        player = new LEDStudioMediaPlayerHolder(debug);
    }

    /**
     * gets the current of the util.LEDStudioMediaPlayerHolder
     * @return int
     */
    public int getVolume()
    {
        return player.getVolume();
    }

    /**
     * gets the total duration of the current load player in util.LEDStudioMediaPlayerHolder as a double
     * @return double
     */
    public double getEndTime()
    {
        return player.getEndTime();
    }

    /**
     * gets the current duration as a double of the util.LEDStudioMediaPlayerHolder MediaPlayer
     * @return double
     */
    public double getCurrentTime()
    {
        return player.getCurrentTime();
    }

    /**
     * Transfers a requested Duration to the util.LEDStudioMediaPlayerHolder
     * @param requestedTime Duration requested from a time interval
     */
    public void setSeek(Duration requestedTime)
    {
        player.setSeek(requestedTime);
    }

    /**
     * Prepares a Runnable class to function with the Java util.StudioApplication platform in threading
     * @param r Class that implements the Runnable interface
     */
    private static void startup(Runnable r)
    {
        com.sun.javafx.application.PlatformImpl.startup(r);
    }

    /**
     * Changes the play state of the MediaPlayerHolder to its opposite state, and then updates the util.StudioApplication with the MediaPlayer's play state.
     */
    public void togglePlayState()
    {
        if(isPlaying)
        {
            player.pause();
        }else
        {
            player.play();
        }//Only request once, so that it does not get hogged by the J.AWT thread
       isPlaying = player.isPlaying();
    }

    /**
     * Endlessly feeds the Application thread to get a new set of instructions via String input.
     */
    private void start()
    {
        while(true) {
            startup(player);

            //Wait for player to change to inUse
            try {
                sleep(200);
            } catch(InterruptedException e) {
                System.out.println("Main thread sleep was interrupted");
            }
            while(inUse() || isLoading()) {

                try {
                    sleep(200);
                    isPlaying = player.isPlaying();
                    //System.out.println("Waiting till free");
                } catch (InterruptedException e) {
                    System.out.println("Main thread sleep was interrupted");
                }
            }
        }
    }

    /**
     * checks if the MediaPlayerHolder is busy with a loading task
     * @return boolean
     */
    public boolean isLoading() {
        return player.isLoading();
    }

    /**
     * checks if the MediaPlayerHolder is busy with a task via terminal input( -debug argument)
     * @return boolean
     */
    public boolean inUse() {
        return player.inUse();
    }

    /**
     * Returns the playing status
     * @return Boolean which determines if the util.LEDStudioMediaPlayerHolder is Playing
     */
    public boolean isPlaying()
    {
        return isPlaying;
    }

    /**
     * Requests the util.LEDStudioMediaPlayerHolder to load a file
     * @param path string containing the Absolute directory and file name
     */
    public void load(String path)
    {
        player.load(path);
        isPlaying = player.isPlaying();
    }

    /**
     *
     */

    public void setAudioSpectrumListener(AudioSpectrumListener audioSpectrumListener)
    {
        player.setAudioSpectrumListener(audioSpectrumListener);
    }


    /**
     * Overridden method for Runnable interface via threading.
     * If in debug mode util.StudioApplication will request string input until quit.
     * Otherwise Java Application thread will be fed, and util.StudioApplication will consistently request and save the playing status of util.LEDStudioMediaPlayerHolder
     */
    @Override
    public void run() {
        if(debug) {
            start();
        }
        else
        {
            startup(player);
            while (true) {
                try {
                    sleep(200);
                } catch (InterruptedException e) {
                    System.out.println("Main thread sleep was interrupted");
                }
                isPlaying = player.isPlaying();
            }
        }
    }
}
