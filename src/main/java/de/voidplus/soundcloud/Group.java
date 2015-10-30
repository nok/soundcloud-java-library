package de.voidplus.soundcloud;

public class Group {

    private Integer id;
    private String createdAt;
    private String permalink;
    private String name;
    private String shortDescription;
    private String description;
    private String uri;
    private String permalinkUrl;
    private String artworkUrl;
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
        return createdAt;
    }

    public String getPermalink() {
        return permalink;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getUri() {
        return uri;
    }

    public String getPermalinkUrl() {
        return permalinkUrl;
    }

    public String getArtworkUrl() {
        return artworkUrl;
    }

    public User getCreator() {
        return creator;
    }

    @Override
    public String toString() {
        return "Group [id=" + id + ", createdAt=" + createdAt
                + ", permalink=" + permalink + ", name=" + name + ", uri="
                + uri + ", permalinkUrl=" + permalinkUrl + ", artworkUrl="
                + artworkUrl + ", creator=" + creator + "]";
    }
    
}
