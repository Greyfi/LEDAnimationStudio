package util;

import java.io.Serializable;
import java.util.ArrayList;

public class FileSession implements Serializable {
    private SongEntry sessionSong;
    private ArrayList<LED> unnameds;

    public FileSession()
    {

    }

}
