package de.voidplus.soundcloud;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import com.google.gson.Gson;

import com.soundcloud.api.ApiWrapper;
import com.soundcloud.api.Endpoints;
import com.soundcloud.api.Http;
import com.soundcloud.api.Params;
import com.soundcloud.api.Request;
import com.soundcloud.api.Token;


public class SoundCloud {
    
    
    public static final String VERSION = "0.1.4";
    
    
    private enum Type {
        USER,
        TRACK,
        PLAYLIST,
        COMMENT,
        GROUP
    }
    
    private enum Rest {
        GET,
        PUT,
        POST,
        DELETE
    }
    
    
    protected final String app_client_id;
    protected final String app_client_secret;
    protected File app_serial;
    
    protected Token token;
    protected ApiWrapper wrapper;
    
    protected JSONParser parser;
    protected Gson gson;
    

    /**
     * Constructor of the SoundCloud wrapper, which creates the API wrapper and generates the Access Token.
     * 
     * @param _app_client_id        Application Client ID
     * @param _app_client_secret    Application Client Secret
     * @param _login_name           SoundCloud Login Name
     * @param _login_password       SoundCloud Login Pass
     */
    public SoundCloud( String _app_client_id, String _app_client_secret )
    {
        this.app_client_id = _app_client_id;
        this.app_client_secret = _app_client_secret;
        
        File data_dir = new File("."+File.separator+"data");
        this.app_serial = new File(data_dir,"serial.ser");
        
        if(!data_dir.exists()){
            data_dir.mkdirs();
        }
        if(!app_serial.exists()){
            try {
                app_serial.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        this.parser = new JSONParser();
        this.gson = new Gson();

        if (app_serial.length() == 0) {
            wrapper = new ApiWrapper(app_client_id, app_client_secret, null, null);
            try {
                wrapper.toFile(app_serial);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                wrapper = ApiWrapper.fromFile(app_serial);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        wrapper.setToken(null);
        wrapper.setDefaultContentType("application/json");
    }
    

    /**
     * Constructor of the SoundCloud wrapper, which creates the API wrapper and generates the Access Token.
     * 
     * @param _app_client_id        Application Client ID
     * @param _app_client_secret    Application Client Secret
     */
    public SoundCloud( String _app_client_id, String _app_client_secret, String _login_name, String _login_password )
    {
        this(_app_client_id, _app_client_secret);
        this.login(_login_name, _login_password);
    }
    
    
    /**
     * Login to get a valid token.
     * 
     * @param _login_name           SoundCloud Login Name
     * @param _login_password       SoundCloud Login Pass
     * @return
     */
    public void login( String _login_name, String _login_password ){
        try {
            this.token = wrapper.login(_login_name, _login_password, Token.SCOPE_NON_EXPIRING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    /**
     * The core of the library.
     * 
     * @param api
     * @param rest
     * @param value
     * @param filters
     * @return
     */
    private <T> T api(String api, Rest rest, Object value, String[] filters)
    {
        if (api.length() > 0) {
            if (api.startsWith("/") == false) {
                api = "/" + api;
            }
            if (api.charAt(api.length() - 1) == '/') {
                api = api.substring(0, api.length() - 1);
            }
            api = api.replace(".format", ".json").replace(".xml", ".json");
            if (api.indexOf(".json") == -1) {
                api += ".json";
            }
        } else {
            return null;
        }
        
        // https://apigee.com/embed/console/soundcloud
        Type type = null;
        if (
                api.matches("^/me.json") ||
                api.matches("^/me/(followings(/[0-9]+)?|followers(/[0-9]+)?).json") ||
                api.matches("^/users(/[0-9]+)?.json") ||
                api.matches("^/users/([0-9]+)/(followings|followers).json") ||
                api.matches("^/groups/([0-9]+)/(users|moderators|members|contributors).json")
        ){
            type = Type.USER;
        } else if(
                api.matches("^/tracks(/[0-9]+)?.json") ||
                api.matches("^/me/(tracks|favorites)(/[0-9]+)?.json") ||
                api.matches("^/users/([0-9]+)/(tracks|favorites).json")
        ){
            type = Type.TRACK;
        } else if(
                api.matches("^/playlists(/[0-9]+)?.json") ||
                api.matches("^/me/playlists.json") ||
                api.matches("^/users/([0-9]+)/playlists.json") ||
                api.matches("^/groups/([0-9]+)/tracks.json")
        ){
            type = Type.PLAYLIST;
        } else if(
                api.matches("^/comments/([0-9]+).json") ||
                api.matches("^/me/comments.json") ||
                api.matches("^/tracks/([0-9]+)/comments.json")
        ){
            type = Type.COMMENT;
        } else if(
                api.matches("^/groups(/[0-9]+)?.json") ||
                api.matches("^/me/groups.json") ||
                api.matches("^/users/([0-9]+)/groups.json")
        ){
            type = Type.GROUP;
        }
        if (type == null) {
            return null;
        }
        
        if (filters != null) {
            if (filters.length > 0 && filters.length % 2 == 0) {
                api += "?";
                for (int i = 0, l = filters.length; i < l; i += 2) {
                    if (i != 0) {
                        api += "&";
                    }
                    api += (filters[i] + "=" + filters[i + 1]);
                }
                if(this.token == null){
                	api += ("&consumer_key="+this.app_client_id);
                }
            }
        } else {
        	api += "?consumer_key="+this.app_client_id;
        }
        
        try {
            
            Request resource;
            HttpResponse response;
            String klass, content;
            
            switch(rest){           
                case GET:
                    response = wrapper.get(Request.to(api));
                    
                    if(response.getStatusLine().getStatusCode()==303){ // recursive better?
                        // System.out.println( "303: "+response.getFirstHeader("Location").getValue() );
                        api = (String)(response.getFirstHeader("Location").getValue()+".json").replace("https://api.soundcloud.com","");
                        response = wrapper.get( Request.to(api) );
                    }
                    
                    if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                        String json = (String)(Http.formatJSON(Http.getString(response))).trim();
                        
                        if (json.startsWith("[") && json.endsWith("]")) {
                            JSONArray data = (JSONArray) parser.parse(json);
                            
                            if (data.size() > 0) {
                                switch(type){
                                    case USER:
                                        ArrayList<User> users = new ArrayList<User>();
                                        for (int i = 0, l = data.size(); i < l; i++) {
                                            String tuple = data.get(i).toString();                                  
                                            User user = gson.fromJson(tuple, User.class);
                                            users.add(user);
                                        }
                                        return (T) users;
                                    case TRACK:
                                        ArrayList<Track> tracks = new ArrayList<Track>();
                                        for (int i = 0, l = data.size(); i < l; i++) {
                                            String tuple = data.get(i).toString();
                                            Track track = gson.fromJson(tuple, Track.class);
                                            track.setSoundCloud(this);
                                            tracks.add(track);
                                        }
                                        return (T) tracks;
                                    case PLAYLIST:
                                        ArrayList<Playlist> playlists = new ArrayList<Playlist>();
                                        for (int i = 0, l = data.size(); i < l; i++) {
                                            String tuple = data.get(i).toString();
                                            Playlist playlist = gson.fromJson(tuple, Playlist.class);
                                            playlists.add(playlist);
                                        }
                                        return (T) playlists;
                                    case COMMENT:
                                        ArrayList<Comment> comments = new ArrayList<Comment>();
                                        for (int i = 0, l = data.size(); i < l; i++) {
                                            String tuple = data.get(i).toString();
                                            Comment comment = gson.fromJson(tuple, Comment.class);
                                            comments.add(comment);
                                        }
                                        return (T) comments;
                                    case GROUP:
                                        ArrayList<Group> groups = new ArrayList<Group>();
                                        for (int i = 0, l = data.size(); i < l; i++) {
                                            String tuple = data.get(i).toString();
                                            Group group = gson.fromJson(tuple, Group.class);
                                            groups.add(group);
                                        }
                                        return (T) groups;
                                    default: return null;
                                }
                                
                            }
                            
                        } else {
                            
                            switch(type){
                                case USER:
                                    User user = gson.fromJson(json, User.class);
                                    return (T) user;
                                case TRACK:
                                    Track track = gson.fromJson(json, Track.class);
                                    track.setSoundCloud(this);
                                    return (T) track;
                                case PLAYLIST:
                                    Playlist playlist = gson.fromJson(json, Playlist.class);
                                    return (T) playlist;
                                case COMMENT:
                                    Comment comment = gson.fromJson(json, Comment.class);
                                    return (T) comment;
                                case GROUP:
                                    Group group = gson.fromJson(json, Group.class);
                                    return (T) group;
                                default: return null;
                            }
                            
                        }
                    } else {
                        System.err.println("Invalid status received: " + response.getStatusLine());
                    }
                break;
                case POST:
                    
                    klass = value.getClass().getName();
                    klass = klass.substring((klass.lastIndexOf('.')+1));

                    if(klass.equals("Track")){
                        Track track = ((Track)value);
                        resource = Request.to(Endpoints.TRACKS)
                                .add(Params.Track.TITLE,track.getTitle())
                                .add(Params.Track.TAG_LIST,track.getTagList())
                                .withFile(Params.Track.ASSET_DATA,new File(track.asset_data));
                    } else if(klass.equals("Comment")){
                        content = gson.toJson(value);
                        content = "{\"comment\":"+content+"}";
                        resource = Request.to(api.replace(".json",""))
                                .withContent(content, "application/json");
                    } else {
                        return null;
                    }
                    
                    response = wrapper.post(resource);
                    
                    if (response.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED) {
                        String json = (String)(Http.formatJSON(Http.getString(response))).trim();
                        switch(type){
                            case TRACK:
                                Track track = gson.fromJson(json, Track.class);
                                track.setSoundCloud(this);
                                return (T) track;
                            case COMMENT:
                                Comment comment = gson.fromJson(json, Comment.class);
                                return (T) comment;
                            default: return null;
                        }
                        
                    } else {
                        System.err.println("Invalid status received: " + response.getStatusLine());
                    }
                    
                break;
                case PUT:
                    
                    if( value!=null ){
                    
                        klass = value.getClass().getName();
                        klass = klass.substring((klass.lastIndexOf('.')+1));
                        
                        content = gson.toJson(value);
    
                        if(klass.equals("User")){
                            content = "{\"user\":"+content+"}";
                        } else if(klass.equals("Track")){
                            content = "{\"track\":"+content+"}";
                        } else {
                            return null;
                        }
                        
                        resource = Request.to(api.replace(".json","")).withContent(content, "application/json");
                    } else {
                        resource = Request.to(api.replace(".json",""));
                    }
                    
                    response = wrapper.put(resource);
                    
                    if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                        String json = (String)(Http.formatJSON(Http.getString(response))).trim();
                        
                        switch(type){
                            case USER:
                                User user = gson.fromJson(json, User.class);
                                return (T) user;
                            case TRACK:
                                Track track = gson.fromJson(json, Track.class);
                                track.setSoundCloud(this);
                                return (T) track;
                            default: return null;
                        }
                    } else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED) {
                        return (T) new Boolean(true);
                    } else{
                        System.err.println("Invalid status received: " + response.getStatusLine());
                    }
                    
                break;
                case DELETE:
                    response = wrapper.delete( Request.to(api) );
                    
                    if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                        return (T) new Boolean(true);
                    }
                    return (T) new Boolean(false);
            }           

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    
    /**
     * API access to GET (REST) data.
     * 
     * @param api
     * @return
     */
    public <T> T get(String api) {
        return this.api(api, Rest.GET, null, null);
    }
    
    /**
     * API access with filters to GET (REST) data.
     * 
     * @param api
     * @param filters
     * @return
     */
    public <T> T get(String api, String[] filters) {
        return this.api(api, Rest.GET, null, filters);
    }
    
    
    /**
     * API access to POST (REST) new data.
     * 
     * @param api
     * @return
     */
    public <T> T post(String api, Object value){
        return this.api(api, Rest.POST, value, null);
    }
    
    
    /**
     * API access to PUT (REST) new data.
     * 
     * @param api
     * @return
     */
    public <T> T put(String api){
        return this.api(api, Rest.PUT, null, null);
    }
    
    /**
     * API access to PUT (REST) new data.
     * 
     * @param api
     * @return
     */
    public <T> T put(String api, Object value){
        return this.api(api, Rest.PUT, value, null);
    }
    

    /**
     * API access to DELETE (REST) data.
     * 
     * @param api
     * @return
     */
    public Boolean delete(String api){
        return (Boolean) this.api(api, Rest.DELETE, null, null);
    }

    
    /**
     * Get data about you.
     * 
     * @return
     */
    public User getMe(){
        return this.get("me");
    }
    
    /**
     * Get your followings.
     * 
     * @param offset
     * @param limit
     * @return
     */
    public ArrayList<User> getMeFollowings(Integer offset, Integer limit){
        return this.get("me/followings", new String[]{
            "limit",Integer.toString(limit),
            "offset",Integer.toString(offset)
        });
    }
    
    /**
     * Get your last 50 followings.
     * 
     * @return
     */
    public ArrayList<User> getMeFollowings(){
        return this.getMeFollowings(0, 50);
    }
    
    /**
     * Get a specific following.
     * 
     * @param contact_id
     * @return
     */
    public User getMeFollowing(Integer contact_id){
        return this.get("me/followings/"+Integer.toString(contact_id));
    }
    
    /**
     * Get your followers.
     * 
     * @param offset
     * @param limit
     * @return
     */
    public ArrayList<User> getMeFollowers(Integer offset, Integer limit){
        return this.get("me/followers", new String[]{
            "limit",Integer.toString(limit),
            "offset",Integer.toString(offset)
        });
    }
    
    /**
     * Get your last 50 followers.
     * 
     * @return
     */
    public ArrayList<User> getMeFollowers(){
        return this.getMeFollowers(0, 50);
    }
    
    /**
     * Get a specific follower.
     * 
     * @param contact_id
     * @return
     */
    public User getMeFollower(Integer contact_id){
        return this.get("me/followers/"+Integer.toString(contact_id));
    }
    
    /**
     * Get your favorite tracks.
     * 
     * @param limit
     * @param offset
     * @return
     */
    public ArrayList<Track> getMeFavorites(Integer offset, Integer limit){
        return this.get("me/favorites", new String[]{
            "order","created_at",
            "limit",Integer.toString(limit),
            "offset",Integer.toString(offset)
        });
    }

    /**
     * Get your last 50 favorite tracks.
     * 
     * @return
     */
    public ArrayList<Track> getMeFavorites(){
        return this.getMeFavorites(0,50);
    }
    
    /**
     * Get your last tracks.
     * 
     * @param offset
     * @param limit
     * @return
     */
    public ArrayList<Track> getMeTracks(Integer offset, Integer limit){
        return this.get("me/tracks", new String[]{
            "order","created_at",
            "limit",Integer.toString(limit),
            "offset",Integer.toString(offset)
        });
    }
    
    /**
     * Get your last 50 tracks.
     * 
     * @return
     */
    public ArrayList<Track> getMeTracks(){
        return this.getMeTracks(0, 50);
    }
    
    /**
     * Get your playlists.
     * 
     * @return
     */
    public ArrayList<Playlist> getMePlaylists(Integer offset, Integer limit){
        return this.get("me/playlists", new String[]{
            "order","created_at",
            "limit",Integer.toString(limit),
            "offset",Integer.toString(offset)
        });
    }
    
    /**
     * Get your last 50 playlists.
     * 
     * @return
     */
    public ArrayList<Playlist> getMePlaylists(){
        return this.getMePlaylists(0,50);
    }
    
    /**
     * Get your groups.
     * 
     * @return
     */
    public ArrayList<Group> getMeGroups(Integer offset, Integer limit){
        return this.get("me/groups", new String[]{
            "order","created_at",
            "limit",Integer.toString(limit),
            "offset",Integer.toString(offset)
        });
    }

    /**
     * Get your last 50 groups.
     * 
     * @return
     */
    public ArrayList<Group> getMeGroups(){
        return this.getMeGroups(0,50);
    }

    /**
     * Get your comments.
     * 
     * @return
     */
    public ArrayList<Comment> getMeComments(Integer offset, Integer limit){
        return this.get("me/comments", new String[]{
            "order","created_at",
            "limit",Integer.toString(limit),
            "offset",Integer.toString(offset)
        });
    }
    
    /**
     * Get your last 50 comments.
     * 
     * @return
     */
    public ArrayList<Comment> getMeComments(){
        return this.getMeComments(0, 50);
    }
    
    /**
     * Get comments from specific track.
     * 
     * @param track_id
     * @return
     */
    public ArrayList<Comment> getCommentsFromTrack(Integer track_id){
        return this.get("tracks/"+Integer.toString(track_id)+"/comments");       
    }
    
    /**
     * Get a specific playlist.
     * 
     * @param id
     * @return
     */
    public Playlist getPlaylist(Integer playlist_id){
        return this.get("playlists/"+Integer.toString(playlist_id));
    }
    
    /**
     * Get last playlists.
     * 
     * @param offset
     * @param limit
     * @return
     */
    public ArrayList<Playlist> getPlaylists(Integer offset, Integer limit){
        return this.get("playlists", new String[]{
            "order","created_at",
            "limit",Integer.toString(limit),
            "offset",Integer.toString(offset)
        });
    }
    
    /**
     * Get last 50 playlists.
     * 
     * @return
     */
    public ArrayList<Playlist> getPlaylists(){
        return this.getPlaylists(0, 50);
    }
    
    /**
     * Get a specific user.
     * 
     * @param id
     * @return
     */
    public User getUser(Integer contact_id){
        return this.get("users/"+Integer.toString(contact_id));
    }

    /**
     * Get last users.
     * 
     * @param limit
     * @param offset
     * @return
     */
    public ArrayList<User> getUsers(Integer offset, Integer limit){
        return this.get("users", new String[]{
            "order","created_at",
            "limit",Integer.toString(limit),
            "offset",Integer.toString(offset)
        });
    }
    
    /**
     * Get last users.
     * 
     * @return
     */
    public ArrayList<User> getUsers(){
        return this.getUsers(0,50);
    }
    
    /**
     * Simple user search.
     * 
     * @param username
     * @return
     */
    public ArrayList<User> findUser(String username){
        return this.get("users", new String[]{
            "q",username
        });
    }
    
    /**
     * Get a specific track.
     * 
     * @param id
     * @return
     */
    public Track getTrack(Integer track_id){
        return this.get("tracks/"+Integer.toString(track_id));
    }

    /**
     * Get last tracks.
     * 
     * @param offset
     * @param limit
     * @return
     */
    public ArrayList<Track> getTracks(Integer offset, Integer limit){
        return this.get("tracks", new String[]{
            "order","created_at",
            "limit",Integer.toString(limit),
            "offset",Integer.toString(offset)
        });
    }
    
    /**
     * Get last 50 tracks.
     * 
     * @return
     */
    public ArrayList<Track> getTracks(){
        return this.getTracks(0, 50);
    }
    
    /**
     * Get tracks from specific group.
     * 
     * @param group_id
     * @return
     */
    public ArrayList<Track> getTracksFromGroup(Integer group_id){
        return this.get("groups/"+Integer.toString(group_id)+"/tracks");
    }
    
    /**
     * Simple track search.
     * 
     * @param title
     * @return
     */
    public ArrayList<Track> findTrack(String title){
        return this.get("tracks", new String[]{
            "q",title
        });
    }
    
    /**
     * Get a specific group.
     * 
     * @param id
     * @return
     */
    public Group getGroup(Integer id){
        return this.get("groups/"+Integer.toString(id));
    }
    
    /**
     * Get last groups.
     * 
     * @param offset
     * @param limit
     * @return
     */
    public ArrayList<Group> getGroups(Integer offset, Integer limit){
        return this.get("groups", new String[]{
            "order","created_at",
            "limit",Integer.toString(limit),
            "offset",Integer.toString(offset)
        });
    }
    
    /**
     * Get last 50 groups.
     * 
     * @return
     */
    public ArrayList<Group> getGroups(){
        return this.getGroups(0, 50);
    }
    
    /**
     * Simple user search.
     * 
     * @param name
     * @return
     */
    public ArrayList<Group> findGroup(String name){
        return this.get("groups", new String[]{
            "q",name
        });
    }
    
    /**
     * Update your account.
     * 
     * @param user
     * @return
     */
    public User putMe(User user){
        return this.put("me", user);
    }
    
    /**
     * Favor a specific track.
     * 
     * @param track_id
     * @return
     */
    public Boolean putFavoriteTrack(Integer track_id){
        return this.put("me/favorites/"+Integer.toString(track_id));
    }
    
    /**
     * Post a new track.
     * 
     * @param track
     * @return
     */
    public Track postTrack(Track track){
        return this.post("tracks", track);
    }
    
    /**
     * Post a new comment to a specific track.
     * 
     * @param track_id
     * @param comment
     * @return
     */
    public Comment postCommentToTrack(Integer track_id, Comment comment){
        return this.post("tracks/"+Integer.toString(track_id)+"/comments", comment);
    }
    
    /**
     * Delete a specific track.
     * 
     * @param track_id
     * @return
     */
    public Boolean deleteTrack(Integer track_id){
        return this.delete("tracks/"+Integer.toString(track_id));
    }
    
    /**
     * Remove a specific favorite track.
     * 
     * @param track_id
     * @return
     */
    public Boolean deleteFavoriteTrack(Integer track_id){
        return this.delete("me/favorites/"+Integer.toString(track_id));
    }
    
}