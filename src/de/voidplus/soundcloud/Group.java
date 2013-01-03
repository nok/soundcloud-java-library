package de.voidplus.soundcloud;

public class Group {

    private Integer id;
    private String created_at;
    private String permalink;
    private String name;
    private String short_description;
    private String description;
    private String uri;
    private String permalink_url;
    private String artwork_url;
    private User creator;

    public Group()
    {
    }

    public Integer getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedAt() {
        return created_at;
    }

    public String getPermalink() {
        return permalink;
    }

    public String getShortDescription() {
        return short_description;
    }

    public void setShortDescription(String short_description) {
        this.short_description = short_description;
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

    public User getCreator() {
        return creator;
    }

    @Override
    public String toString() {
        return "Group [id=" + id + ", created_at=" + created_at
                + ", permalink=" + permalink + ", name=" + name + ", uri="
                + uri + ", permalink_url=" + permalink_url + ", artwork_url="
                + artwork_url + ", creator=" + creator + "]";
    }
    
}