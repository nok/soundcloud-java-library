package de.voidplus.soundcloud;

import java.util.ArrayList;

public class Playlist {

    private Integer id;
    private String created_at;
    private Integer user_id;
    private User user;
    private Integer duration;
    private String sharing;
    private String tag_list;
    private String permalink;
    private String description;
    private Boolean streamable;
    private Boolean downloadable; 
    private String genre;
    private String release;
    private String purchase_url;
    private Integer label_id;
    private String label_name;
    private String type;
    private String playlist_type;
    private Integer ean;
    private String title;
    private Integer release_year;
    private Integer release_month;
    private Integer release_day;
    private String license;
    private String uri;
    private String permalink_url;
    private String artwork_url;
    private ArrayList<Track> tracks;
    
    public Playlist()
    {
    }
    
    public Integer getId() {
        return id;
    }
    
    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreatedAt() {
        return created_at;
    }

    public Integer getUserId() {
        return user_id;
    }

    public User getUser() {
        return user;
    }

    public Integer getDuration() {
        return duration;
    }

    public String getSharing() {
        return sharing;
    }

    public String getTagList() {
        return tag_list;
    }

    public String getPermalink() {
        return permalink;
    }

    public String getDescription() {
        return description;
    }

    public Boolean isStreamable() {
        return streamable;
    }

    public Boolean isDownloadable() {
        return downloadable;
    }

    public String getRelease() {
        return release;
    }

    public String getPurchaseUrl() {
        return purchase_url;
    }

    public Integer getLabelId() {
        return label_id;
    }

    public String getLabelName() {
        return label_name;
    }

    public String getType() {
        return type;
    }

    public String getPlaylistType() {
        return playlist_type;
    }

    public Integer getEan() {
        return ean;
    }

    public Integer getReleaseYear() {
        return release_year;
    }

    public Integer getReleaseMonth() {
        return release_month;
    }

    public Integer getReleaseDay() {
        return release_day;
    }

    public String getLicense() {
        return license;
    }

    public String getUri() {
        return uri;
    }

    public String getPermalinkUrl() {
        return permalink_url;
    }

    public String getArtworkUrl() {
        return artwork_url;
    }

    public ArrayList<Track> getTracks() {
        return tracks;
    }

    @Override
    public String toString() {
        return "Playlist [id=" + id + ", created_at=" + created_at
                + ", user_id=" + user_id + ", user=" + user + ", duration="
                + duration + ", sharing=" + sharing + ", tag_list=" + tag_list
                + ", permalink=" + permalink + ", description=" + description
                + ", streamable=" + streamable + ", downloadable="
                + downloadable + ", genre=" + genre + ", release=" + release
                + ", purchase_url=" + purchase_url + ", label_id=" + label_id
                + ", label_name=" + label_name + ", type=" + type
                + ", playlist_type=" + playlist_type + ", ean=" + ean
                + ", title=" + title + ", release_year=" + release_year
                + ", release_month=" + release_month + ", release_day="
                + release_day + ", license=" + license + ", uri=" + uri
                + ", permalink_url=" + permalink_url + ", artwork_url="
                + artwork_url + ", tracks=" + tracks + "]";
    }
    
}