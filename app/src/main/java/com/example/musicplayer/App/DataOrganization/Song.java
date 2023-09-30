package com.example.musicplayer.App.DataOrganization;

import android.net.Uri;

public class Song {
    private String songId;
    private String songName;
    private boolean hasPicture;
    private Uri imageUri;

    public Song(String songId, String songName, boolean hasPicture) {
        this.songId = songId;
        this.songName = songName;
        this.hasPicture = hasPicture;
    }

    // Getters and setters for all fields
    public String getSongId() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public boolean hasPicture() {
        return hasPicture;
    }

    public void setHasPicture(boolean hasPicture) {
        this.hasPicture = hasPicture;
    }
    public void setImageUri(Uri imageUri) { this.imageUri = imageUri; }
    public Uri getImageUri() { return imageUri; }
}

