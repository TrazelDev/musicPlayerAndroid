package com.example.musicplayer;

public class Playlist {
    private int imageResource;
    private String name;

    public Playlist(int imageResource, String name) {
        this.imageResource = imageResource;
        this.name = name;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getName() {
        return name;
    }
}
