package com.dhammika_dev.justgo.parse.models.local;

public class PrivateMedia {

    private long size;
    private String url;
    private int width;
    private int height;

    public PrivateMedia() {
    }

    public PrivateMedia(String path, int height, int width, long size) {
        this.url = path;
        this.size = size;
        this.width = width;
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public String getUrl() {
        return url;
    }

    public long getSize() {
        return size;
    }


}
