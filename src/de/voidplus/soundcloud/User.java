package de.voidplus.soundcloud;

public class User {

    private Integer id;
    private String kind;
    private String permalink;
    private String username;
    private String uri;
    private String permalink_url;
    private String avatar_url;
    private String country;
    private String full_name;
    private String description;
    private String city;
    private String discogs_name;
    private String myspace_name;
    private String website;
    private String website_title;
    private Boolean online;
    private Integer track_count;
    private Integer playlist_count;
    private String plan;
    private Integer public_favorites_count;
    private Integer followers_count;
    private Integer followings_count;
    private Integer private_tracks_count;
    private Integer private_playlists_count;
    private Boolean primary_email_confirmed;

    public User()
    {
    }
    
    public Integer getId() {
        return id;
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getFullName() {
        return full_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDiscogsName() {
        return discogs_name;
    }

    public void setDiscogsName(String discogs_name) {
        this.discogs_name = discogs_name;
    }

    public String getMyspaceName() {
        return myspace_name;
    }

    public void setMyspaceName(String myspace_name) {
        this.myspace_name = myspace_name;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getWebsiteTitle() {
        return website_title;
    }

    public void setWebsiteTitle(String website_title) {
        this.website_title = website_title;
    }

    public String getKind() {
        return kind;
    }

    public String getPermalink() {
        return permalink;
    }

    public String getUri() {
        return uri;
    }

    public String getPermalinkUrl() {
        return permalink_url;
    }

    public String getAvatarUrl() {
        return avatar_url;
    }

    public Boolean isOnline() {
        return online;
    }

    public Integer getTrackCount() {
        return track_count;
    }

    public Integer getPlaylistCount() {
        return playlist_count;
    }

    public String getPlan() {
        return plan;
    }

    public Integer getPublicFavoritesCount() {
        return public_favorites_count;
    }

    public Integer getFollowersCount() {
        return followers_count;
    }

    public Integer getFollowingsCount() {
        return followings_count;
    }

    public Integer getPrivateTracksCount() {
        return private_tracks_count;
    }

    public Integer getPrivatePlaylistsCount() {
        return private_playlists_count;
    }

    public Boolean getPrimaryEmailConfirmed() {
        return primary_email_confirmed;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", kind=" + kind + ", permalink=" + permalink
                + ", username=" + username + ", uri=" + uri
                + ", permalink_url=" + permalink_url + ", avatar_url="
                + avatar_url + ", country=" + country + ", full_name="
                + full_name + ", description=" + description + ", city=" + city
                + ", discogs_name=" + discogs_name + ", myspace_name="
                + myspace_name + ", website=" + website + ", website_title="
                + website_title + ", online=" + online + ", track_count="
                + track_count + ", playlist_count=" + playlist_count
                + ", plan=" + plan + ", public_favorites_count="
                + public_favorites_count + ", followers_count="
                + followers_count + ", followings_count=" + followings_count
                + ", private_tracks_count=" + private_tracks_count
                + ", private_playlists_count=" + private_playlists_count
                + ", primary_email_confirmed=" + primary_email_confirmed + "]";
    }
    
}