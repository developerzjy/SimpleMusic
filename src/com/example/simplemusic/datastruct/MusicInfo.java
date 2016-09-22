package com.example.simplemusic.datastruct;

public class MusicInfo {

    private int id = -1;
    private String title = null;
    private String album = null;
    private String uri = null;
    private int length = -1;
    private String icon = null;
    private String artist = null;
    private boolean isFavor = false;

    public boolean isFavor() {
        return isFavor;
    }

    public void setFavor(boolean isFavor) {
        this.isFavor = isFavor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
}
