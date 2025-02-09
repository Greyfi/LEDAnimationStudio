package util;

import javafx.scene.media.AudioSpectrumListener;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.util.Observable;
import java.util.Scanner;

/**
 * @author Greyfire
 * @version
 */
public class LEDStudioMediaPlayerHolder extends Observable implements Runnable
{
    //These will be implemented in an interface
    private final String HELP = "help";
    private final String LOAD = "load";
    private final String PLAY = "play";
    private final String PAUSE = "pause";
    private final String QUIT = "quit";
    private final String DIRECTORY = "bin/media/";

    private boolean debug;
    private boolean isActive;
    private boolean firstStart;
    private boolean isPlaying;
    private boolean isLoading;
    private boolean isValidating;
    private boolean hasInitialized;
    private boolean firstLoad;
    private MediaPlayer mediaPlayer;
    private Media song;

    private int volume;
    private String songName;
    private String songDir;
    private String inputStr;
    private Scanner input;
    private AudioSpectrumListener audioSpectrumListener;

    /**
     * Constructor that sets up a scanner for use, initial bool values, and a non-null string.
     */
    public LEDStudioMediaPlayerHolder(boolean debug) {
        this.debug = debug;
        songName = "";
        songDir = "";
        input = new Scanner(System.in);
        isActive = false;
        firstStart = true;
        isPlaying = false;
        isLoading = false;
        hasInitialized = false;
        isValidating = false;
        volume = 100;
    }

    /**
     * Returns the integer value of the MediaPlayerHolder's volume distribution for all MediaPlayers
     * @return integer
     */
    public int getVolume()
    {
        return volume;
    }


    /**
     * Trims all the spaces before and after the the first and last characters respectively, limits the spaces between characters to 1,
     * sets it to lower case to make commands non-case sensitive
     * @param a String command via user input
     * @return String with extra whitespace removed, and set to lower
     */
    private String normalize(String a) {
        //Create a new string to hold a String without leading, extra() and trailing blanks
        String normalized = a.trim();
        normalized = normalized.replaceAll("( )+", " ");
        //with the final step of normalization, change to lower case and save to the string the class returns
        normalized = normalized.toLowerCase();
        return  normalized;
    }

    /**
     * pauses and discards the current MediaPlayer object in this class
     */
    private void deInitialize() {
        if(isPlaying&&hasInitialized){
            mediaPlayer.pause();
            isPlaying = false;
        }
        mediaPlayer = null;
        hasInitialized = false;
    }

    /**
     *
     * @return MediaPlayer's currentTime double value of the file loaded.
     */
    public double getEndTime()
    {
        if(mediaPlayer != null) {
            return mediaPlayer.getStopTime().toMillis();
        }
        return 0;
    }

    /**
     * Returns the existing media player's current position of loaded .mp3 file
     * @return MediaPlayer's currentTime double value of the file loaded.
     */
    public double getCurrentTime()
    {
        if(mediaPlayer != null)
        {
            return mediaPlayer.getCurrentTime().toMillis();
        }
        return 0;
    }

    /**
     * Sets the the position of the the duration of the current file
     * @param requestedTime duration requested by the user
     */
    public void setSeek(Duration requestedTime)
    {
        if (mediaPlayer != null) {
            mediaPlayer.seek(requestedTime);
        }
    }



    /**
     * Will try to load the file directory indicated
     * @param attempt a file directory in a string
     */
    public void load(String attempt) throws MediaException{
            if (!isLoading && !isValidating) {
                    // This local variable will throw an exception without interrupting the current playback if no file is found
                    Media tempPointer = new Media(new File(attempt).toURI().toString());
                    deInitialize();
                    song = tempPointer;
                    //System.out.println("TIME: " + song.getDuration());
                    mediaPlayer = new MediaPlayer(song);
                    hasInitialized = true;
                    //Waiting done through a listener.
                    isLoading = true;
                    mediaPlayer.setOnReady(() -> {
                        mediaPlayer.setAudioSpectrumListener(audioSpectrumListener);
                        mediaPlayer.setVolume(volume / 100.0);
                        songDir = attempt;
                        mediaPlayer.setAudioSpectrumNumBands(80);
                        SongEntry newestSong = new SongEntry(attempt);
                        songName = newestSong.getSongName();
                        System.out.println("File loaded!");
                        isLoading = false;
                        play();

                    });
                    //For now -Note scanner hogs the thread therefore
                    mediaPlayer.setOnEndOfMedia(() -> {
                        //Pausing then setting the media back to 0 instead of using stop -> stop causes the Duration to be NULL until media starts playing again
                        mediaPlayer.seek(new Duration(0));
                        isPlaying = false;
                        mediaPlayer.pause();
                    });
                }
        }

    /**
     * Will run the first time the Java Application thread is fed and load a song into the mediaPlayer
     */
    private void firstTimeLoad() throws MediaException
    {
        //incorporate or discard
    }

    /**
     * Requests sting input from the user in Debug mode before attempting file Load
     */
    private void loadCMD()
    {
        //future specification, custom exception to prevent unloading the current song if it is not found or is the same song.
        System.out.println("Type the name of the song file with it's extension. Note this is case sensitive!");
        String songStr = input.nextLine();
        songStr = songStr.trim();
        load(DIRECTORY + songStr);
    }

    /**
     * sets the MediaPlayer class to PLAYING state.
     */
    public void play() {

            if (!hasInitialized) {
                System.err.println("There is no song loaded; please load a song before playing."); //Exception throw idea
            } else if (isPlaying) {
                System.out.println("Song is already playing");
            } else {
                mediaPlayer.play();
                isPlaying = true;
                System.out.println("\"" + songName + "\" is now playing.");
            }
    }

    /**
     * Sets the media player class to a PAUSED state
     */
    public void pause() {
            if (!hasInitialized) {
                System.err.println("There is no song loaded; please load a song before attempting to pause"); //Exception throw idea
            } else if (!isPlaying) {
                System.out.println("Song is already paused");
            } else {
                mediaPlayer.pause();
                isPlaying = false;
                System.out.println("Song paused.");
            }

    }

    /**
     * a method that calls a set of operations depending on user input. This input is not case sensitive
     */
    private void start()
    {
        if(firstStart) {
            System.out.println("Type \"help\" for a list of commands to get started");
            firstStart = false;
        }
            inputStr = input.nextLine();

        switch (normalize(inputStr)) {
            case HELP:
                System.out.println("help - Prints a list of commands. \n" +
                        "load - prepares the program to load a song, then requests file name located in the media folder.\n" +
                        "play - starts playing the audio of loaded media.\n" +
                        "pause - pauses the audio of the loaded media.\n" +
                        "quit - De-initializes the program, and exits.");
                break;
            case LOAD:
                loadCMD();
                break;
            case PLAY:
                play();
                break;
            case PAUSE:
                pause();
                break;
            case QUIT:
                deInitialize();
                System.exit(0);
            default:
                System.out.println("Unrecognized command \"" + inputStr + "\". Type \"help\" for a list of commands");
                break;
        }
    }

    //Mainly for testing, due to absolute directories
    public String getSongDir()
    {
        return songDir;
    }

    /**
     * Shows if the util.LEDStudioMediaPlayerHolder
     * @return a Boolean which shows if the class is currently being threaded.
     */
    public boolean inUse()
    {
        return isActive;
    }

    public  boolean isLoading()
    {
        return isLoading;
    }

    public boolean isPlaying(){ return isPlaying; }

    public void setAudioSpectrumListener(AudioSpectrumListener audioSpectrumListener)
    {
        this.audioSpectrumListener = audioSpectrumListener;
    }

    /**
     * The start point of util.LEDStudioMediaPlayerHolder Runnable when threaded.
     */
    @Override
    public void run() {
        isActive = true;
        if (firstLoad) {
            firstTimeLoad();
            firstLoad = false;
        }
        if(debug) {
            //System.out.println("First");
            start();
            //System.out.println("Second");
        }
        isActive = false;
    }

}
