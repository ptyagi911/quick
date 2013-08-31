package com.example.drivequickstart.model;

import java.util.ArrayList;

public final class MediaStore {

    private ArrayList<MediaFile> mediaStore = new ArrayList<MediaFile>();

    public static final String[] TITLES =
    {
            "Henry IV (1)",
            "Henry V",
            "Henry VIII",
            "Richard II",
            "Richard III",
            "Merchant of Venice",
            "Othello",
            "King Lear"
    };

    public static ArrayList<MediaFile> getMediaStore(DBController dbController) {
        return dbController.getAllMediaFiles();
    }
    /**
     * Our data, part 2.
     */
    public static final String[] DIALOGUE = 
    {

    };

    class Media {

    }
}
