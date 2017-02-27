# SoundCloud Java library [![Build Status](https://travis-ci.org/nok/soundcloud-java-library.svg?branch=dev)](https://travis-ci.org/nok/soundcloud-java-library) [![API Testing](https://img.shields.io/badge/API%20Test-RapidAPI-blue.svg)](https://rapidapi.com/package/Soundcloud/functions?utm_source=SoundcloudGithub&utm_medium=button&utm_content=Vender_GitHub)

Unofficial Java library, which simplifies the use of the official [SoundCloud Java API wrapper](https://github.com/soundcloud/java-api-wrapper).


## Table of Contents

- [**Notice**](#notice)
- [Download](#download)
- [Dependencies](#dependencies)
- [Usage](#usage)
	- [Initialization](#initialization)
	- [API](#api)
	- [REST](#rest)
		-  [GET](#get)
		-  [PUT](#put)
		-  [POST](#post)
		-  [DELETE](#delete)
- [Examples](#examples)
- [Processing](#processing)
- [Questions?](#questions)
- [License](#license)


## Notice

**SoundCloud [dropped the support](https://github.com/soundcloud/java-api-wrapper#important-notice) for the official [SoundCloud Java API wrapper](https://github.com/soundcloud/java-api-wrapper), which is a primary dependency.**


## Download

- [soundcloud-0.2.1-jar-with-dependencies.jar](target/soundcloud-0.2.1-jar-with-dependencies.jar?raw=true) (see the list of [dependencies](#dependencies))
- [soundcloud-0.2.1.jar](target/soundcloud-0.2.1.jar?raw=true)


## Dependencies

Open and read the [pom.xml file](pom.xml) for all necessary dependencies.

There are several internal dependencies:

- [java-api-wrapper-1.3.1-all.jar](https://github.com/soundcloud/java-api-wrapper/) (```gradle jarAll```) by [SoundCloud](https://github.com/soundcloud)
- [com.google.code.gson](http://code.google.com/p/google-gson/) by [Google](https://github.com/google)
- [com.googlecode.json-simple](http://code.google.com/p/json-simple/)


## Usage

### Initialization

Add the library jar to your classpath, import it and create a new instance of `SoundCloud`. Finally connect with specified [application settings](http://soundcloud.com/you/apps) and your personal [login](https://soundcloud.com/login/forgot) data.

```java
import de.voidplus.soundcloud.*;
```
```java
SoundCloud soundcloud = new SoundCloud(
    "appClientId",
    "appClientSecret"
);

// Or, if you need any permissions:

SoundCloud soundcloud = new SoundCloud(
    "appClientId",
    "appClientSecret",
    "userLoginMail",
    "userLoginPass"
);

// Or:

SoundCloud soundcloud = new SoundCloud(
    "appClientId",
    "appClientSecret"
);
soundcloud.login(
    "userLoginMail",
    "userLoginPass"
);
```

### API

The service of [Apigee](https://apigee.com/embed/console/soundcloud) is proper for learning and testing the official [SoundCloud API](http://developers.soundcloud.com/docs/api/reference). All API calls are mapped to REST methods:

```java
public  get( String path [, String[] filters] )  <T>:T   // GET
public  put( String path [, Object value] )  <T>:T       // PUT
public  post( String path, Object value )  <T>:T         // POST
public  delete( String path )  Boolean                   // DELETE
```

### REST

You can use the following API calls and/or helpers ("→" ⇔ HTTP 303 See Other Redirect):

#### GET

User[.java](src/de/voidplus/soundcloud/User.java):

```java
User                 get("me")                                             // /me
                     getMe()

ArrayList<User>      get("users")                                          // /users
                     getUsers([Integer offset, Integer limit])

User                 get("users/{contact_id}")                             // /users/{contact_id}
                     getUser({contact_id})

ArrayList<User>      get("me/followings")                                  // /me/followings
                     getMeFollowing([Integer offset, Integer limit])
User                 get("me/followings/{contact_id}")                     // /me/followings/{contact_id}
                     getMeFollowing({contact_id})                          // → /users/{contact_id}

ArrayList<User>      get("me/followers")                                   // /me/followers
                     getMeFollowers([Integer offset, Integer limit])
User                 get("me/followers/{contact_id}")                      // /me/followers/{contact_id}
                     getMeFollower({contact_id})                           // → /users/{contact_id}

ArrayList<User>      get("groups/{group_id}/users")                        // /groups/{group_id}/users
ArrayList<User>      get("groups/{group_id}/moderators")                   // /groups/{group_id}/moderators
ArrayList<User>      get("groups/{group_id}/members")                      // /groups/{group_id}/members
ArrayList<User>      get("groups/{group_id}/contributors")                 // /groups/{group_id}/contributors
                                                                           // 2271 = SoundCloud Sweetness ;)
```

Track[.java](src/de/voidplus/soundcloud/Track.java):

```java
Track                get("tracks/{track_id}")                              // /tracks/{track_id}
                     getTrack({track_id})

ArrayList<Track>     get("tracks")                                         // /tracks
                     getTracks([Integer offset, Integer limit])

ArrayList<Track>     get("me/tracks")                                      // /me/tracks
                     getMeTracks([Integer offset, Integer limit])

ArrayList<Track>     get("me/favorites")                                   // /me/favorites
                     getMeFavorites([Integer offset, Integer limit])

ArrayList<Track>     get("groups/{group_id}/tracks")                       // /groups/{group_id}/tracks
                     getTracksFromGroup({group_id})
```

Playlist[.java](src/de/voidplus/soundcloud/Playlist.java):

```java
Playlist             get("playlists/{playlist_id}")                        // /playlists/{playlist_id}
                     getPlaylist({playlist_id})

ArrayList<Playlist>  get("playlists")                                      // /playlists
                     getPlaylists([Integer offset, Integer limit])

ArrayList<PlayList>  get("me/playlists")                                   // /me/playlists
                     getMePlaylists([Integer offset, Integer limit])
```

Group[.java](src/de/voidplus/soundcloud/Group.java):

```java
Group                get("groups/{group_id}")                              // /groups/{group_id}
                     getGroup({group_id})

ArrayList<Group>     get("groups")                                         // /groups
                     getGroups([Integer offset, Integer limit])

ArrayList<Group>     get("me/groups")                                      // /me/groups
                     getMeGroups([Integer offset, Integer limit])
```

Comment[.java](src/de/voidplus/soundcloud/Comment.java):

```java
ArrayList<Comment>   get("me/comments")                                    // /me/comments
                     getMeComments([Integer offset, Integer limit])

ArrayList<Comment>   get("tracks/{track_id}/comments")                     // /tracks/{track_id}/comments
                     getCommentsFromTrack({track_id})
```

#### PUT

User[.java](src/de/voidplus/soundcloud/User.java):

```java
User                 put("me", User user)                                  // /me
                     putMe(User user)
```

Track[.java](src/de/voidplus/soundcloud/Track.java):

```java
Track                put("tracks/{track_id}", Track track)                 // /tracks/{track_id}

Boolean              put("me/favorites/{track_id}")                        // /me/favorites/{track_id}
                     putFavoriteTrack({track_id})
```

#### POST

Track[.java](src/de/voidplus/soundcloud/Track.java):

```java
Track                post("tracks", Track track))                          // /tracks
                     postTrack(Track track)
```

Comment[.java](src/de/voidplus/soundcloud/Comment.java):

```java
Comment              post("tracks/{track_id}/comments", Comment comment)   // /tracks/{track_id}/comments
                     postCommentToTrack({track_id}, Comment comment)
```

#### DELETE

User[.java](src/de/voidplus/soundcloud/User.java):

```java
Boolean              delete("me/followings/{contact_id}")                  // /me/followings/{contact_id}
Boolean              delete("users/{user_id}/followings/{contact_id}")     // /users/{user_id}/followings/{contact_id}
```

Track[.java](src/de/voidplus/soundcloud/Track.java):

```java
Boolean              delete("tracks/{track_id}")                           // /tracks/{track_id}
                     deleteTrack({track_id})

Boolean              delete("me/favorites/{track_id}")                     // /me/favorites/{track_id}
                     deleteFavoriteTrack({track_id})

Boolean              delete("users/{user_id}/favorites/{track_id}")        // /users/{user_id}/favorites/{track_id}
```

## Examples

### User & Me

Read user details:

```java
User me = soundcloud.getMe();
System.out.println(me);

System.out.println("ID: "+me.getId());
System.out.println("Username: "+me.getUsername());
System.out.println("Avatar-URL: "+me.getAvatarUrl());
```

Update user details:

```java
User me = soundcloud.getMe();
// OR
// me = soundcloud.get("me");

me.setCity("Berlin");
me.setCountry("Germany");
me.setDescription("Text of description.");
me.setWebsite("http://www.soundcloud.de/");
me.setWebsiteTitle("SoundCloud");

me = soundcloud.putMe(me);
// OR
// me = soundcloud.put("me", me);

System.out.println(me);
```

Which sound do you like? Show all your favorites:

```java
User me = soundcloud.getMe();

Integer count = me.getPublicFavoritesCount();
Integer limit = 50; // = max
Integer pages = ((int)count/limit)+1;

ArrayList<Track> all_tracks = new ArrayList<Track>();

for(int i=0; i<pages; i++){
    ArrayList<Track> temp_tracks = soundcloud.getMeFavorites((i*limit),limit);
    // OR
    // ArrayList<Track> temp_tracks = soundcloud.get("me/favorites", new String[] {
    //     "order", "created_at",
    //     "offset", Integer.toString(i*limit),
    //     "limit", Integer.toString(limit)
    // });

    all_tracks.addAll(temp_tracks);
}
for(Track track : all_tracks){
    System.out.println(track.getTitle()+" (#"+track.getId()+")");
}
```

Like ~ Add a new track to your favorites:

```java
ArrayList<Track> tracks = soundcloud.getTracks(0,3);
// OR
// ArrayList<Track> tracks = soundcloud.get("tracks", new String[] {
//     "order", "created_at",
//     "offset", "0",
//     "limit", "3"
// });

Track track = tracks.get(0); // = ~random

Boolean add = soundcloud.putFavoriteTrack(track.getId());
// OR
// Boolean add = soundcloud.put("me/favorites/"+track.getId());
```

Unlike ~ Remove a track from your favorites:

```java
Boolean removing = soundcloud.deleteFavoriteTrack(72688617);
// OR
// Boolean removing = soundcloud.delete("me/favorites/72688617")

if(removing){
    System.out.println("Successful removing.");
}
```

### Track

Upload a new track:

```java
Track track = soundcloud.postTrack(new Track("titel of the song", "path/to/file.mp3"));
// OR
// Track track = soundcloud.post("tracks", new Track("titel of the song!", "path/to/file.mp3"));

System.out.println(track.getTitle()+" (#"+track.getId()+")");
```

Delete a track (Bad example, because we love your music!):

```java
Boolean deletion = soundcloud.deleteTrack(track.getId());
// OR
// Boolean deletion = soundcloud.delete("tracks/"+track.getId());

if(deletion){
    System.out.println("Successful deletion.");
}
```

List the last ten streamable tracks:

```java
ArrayList<Track> streamable_tracks = soundcloud.get("tracks", new String[] {
    "order","created_at",
    "filter","streamable",
    "limit","10"
});

for(Track track : streamable_tracks){
    System.out.println(track.getTitle()+" (#"+track.getId()+")");
}
```

### Group

Get the moderators of a group:

```java
ArrayList<User> moderators = soundcloud.get("groups/2271/moderators");
for(User user : moderators){
    System.out.println(user.getId());
}
```

### Comment

Post a new comment:

```java
Comment comment = new Comment("Nice track!"); // new Comment("Nice track!", 120) // +timestamp

comment = soundcloud.postCommentToTrack(70734856, comment);
// OR
// comment = soundcloud.post("tracks/70734856/comments", comment);

System.out.println(comment);
```

### Search

Search track:

```java
ArrayList<Track> result = soundcloud.findTrack("Chromatics");
if(result!=null){
    System.out.println("Tracks: "+result.size());
    for(Track track:result){
        System.out.println(track);
    }
}
```

Search user:

```java
ArrayList<User> result = soundcloud.findUser("damovi");
if(result!=null){
    System.out.println("Users: "+result.size());
}
```

Search group:

```java
ArrayList<Group> result = soundcloud.findGroup("SoundCloud");
if(result!=null){
    System.out.println("Groups: "+result.size());
}
```


## Questions?

Don't be shy and feel free to contact me on Twitter: [@darius_morawiec](https://twitter.com/darius_morawiec)


## License

[MIT License by SoundCloud](https://raw.github.com/soundcloud/java-api-wrapper/master/LICENSE). The library is Open Source Software released under the [MIT License](MIT-LICENSE.txt).

---

## Processing

You can find the **SoundCloud for Processing** library in a [separate repository](https://github.com/nok/soundcloud-processing). That library is the core dependency of it.
