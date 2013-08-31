package com.example.drivequickstart.model;

import android.graphics.Bitmap;

import java.io.File;

/**
 * Created by ptyagi on 6/23/13.
 */

public class MediaFile {

    public int getMediaId() {
        return mediaId;
    }

    public void setMediaId(int mediaId) {
        this.mediaId = mediaId;
    }

    public int getMediaType() {
        return mediaType;
    }

    public void setMediaType(int mediaType) {
        this.mediaType = mediaType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Object getThumbnailBlob() {
        return thumbnailBlob;
    }

    public void setThumbnailBlob(Object thumbnailBlob) {
        this.thumbnailBlob = thumbnailBlob;
    }

    private int mediaId;
    private int mediaType;
    private String fileName;
    private Object thumbnailBlob;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    private File file;
    private String name;
    private String absolutePath;
    private boolean isUploaded = false;
    private Bitmap thumbnail;
    private long size;
    private File stateFile; /*File at internal storage which keeps contains mediaFile object*/

    public File getStateFile() {
        return stateFile;
    }

    public void setStateFile(File stateFile) {
        this.stateFile = stateFile;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public boolean isUploaded() {
        return isUploaded;
    }

    public void setUploaded(boolean uploaded) {
        isUploaded = uploaded;
    }

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }

}
