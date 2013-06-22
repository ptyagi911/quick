package com.example.drivequickstart.model;

/**
 * Created by ptyagi on 6/23/13.
 */

public class FileObserver extends android.os.FileObserver {

    public FileObserver(String dir) {
        super(dir);
    }

    @Override
    public void onEvent(int event, String path) {
        System.out.println("FileObserver: event- " + event + ", Path-" + path);
    }
}
