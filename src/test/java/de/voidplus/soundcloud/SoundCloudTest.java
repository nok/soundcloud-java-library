package de.voidplus.soundcloud;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.junit.Assume.assumeTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SoundCloudTest {

    public static String apiHost = "https://api.soundcloud.com";

    public static SoundCloud sc;
    public static boolean login;

    // reflection
    public static Method methodFilterApiStr, methodDefineApiType;

    // mock class
    public static SoundCloud mockedSc;

    @BeforeClass
    public static void setup() throws NoSuchMethodException {
        sc = new SoundCloud(System.getenv("APP_ID"), System.getenv("APP_SECRET"));
        login = sc.login(System.getenv("LOGIN_MAIL"), System.getenv("LOGIN_PASS"));

        // mock class for offline tests
        mockedSc = mock(SoundCloud.class);
        User mockedUser = new User();
        mockedUser.setUsername("Darius");
        when(mockedSc.getMe()).thenReturn(mockedUser);

        // test different kinds of api strings
        methodFilterApiStr = sc.getClass().getDeclaredMethod("filterApiString", String.class);
        methodFilterApiStr.setAccessible(true);

        // test different kinds of rest types
        methodDefineApiType = sc.getClass().getDeclaredMethod("defineApiType", String.class);
        methodDefineApiType.setAccessible(true);
    }

    /**
     * Test if specific API is reachable.
     *
     * @param url Without slash at the beginning!
     * @return
     * @throws IOException
     */
    public static boolean isReachable(String url) {
        url = apiHost + url;
        try {
            URL u = new URL(url);
            HttpURLConnection huc = (HttpURLConnection) u.openConnection();
            huc.setRequestMethod("GET");
            huc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US; rv:1.9.1.2) Gecko/20090729 Firefox/3.5.2 (.NET CLR 3.5.30729)");
            huc.connect();
            return huc.getResponseCode() == 200 || huc.getResponseCode() == 401;
        } catch (Exception e) {
//            e.printStackTrace();
            System.err.println("Specific API URL (" + url + ") isn't reachable.");
            return false;
        }
    }

    // --------------------------------------------
    // API

    // GET

    @Test
    public void testApiLogin() {
        assumeTrue(isReachable("/"));

        assertTrue(login);
    }

    @Test
    public void testApiGetMe() {
        assumeTrue(isReachable("/me"));
        assumeTrue(login);

        User a = sc.getMe();
        assertNotNull(a.getId());
    }

    @Test
    public void testApiGetMeEquality() {
        assumeTrue(isReachable("/me"));
        assumeTrue(login);

        User a = sc.getMe();
        User b = sc.get("/me");
        assertEquals(a.getId(), b.getId());
    }

    @Test
    public void testApiGetWithArgs() {
        assumeTrue(isReachable("/tracks"));
        assumeTrue(login);

        ArrayList<Track> tracks = sc.get("tracks", new String[]{
                "order", "created_at",
                "filter", "streamable",
                "limit", "10"
        });
        assertTrue(tracks.size() > 0 && tracks.get(0).isStreamable());
    }

    @Test
    public void testFindTrackShortcut() {
        assumeTrue(isReachable("/tracks"));
        assumeTrue(login);

        ArrayList<Track> tracks = sc.findTrack("I love you");
        assertTrue(tracks.size() > 0);
    }

    @Test
    public void testFindUserShortCut() {
        assumeTrue(isReachable("/users"));
        assumeTrue(login);

        ArrayList<User> users = sc.findUser("Björk");
        assertTrue(users.size() > 0);
    }

    // PUT

    @Test
    public void testApiPutMe() {
        assumeTrue(isReachable("/users"));
        assumeTrue(login);

        User me = sc.getMe();
        String city = me.getCity();
        city = city == null ? "Stuttgart" : city.equals("Stuttgart") ? "Berlin" : "Stuttgart";
        me.setCity(city);
        sc.putMe(me);
        assertTrue(me.getCity().equals(city));
    }


    // --------------------------------------------
    // Data-Preprocessing

    @Test
    public void testPreFilterApiStr1() throws InvocationTargetException, IllegalAccessException {
        String api = "/me";
        String str = (String) methodFilterApiStr.invoke(sc, api);
        assertEquals("/me.json", str);
    }

    @Test
    public void testPreFilterApiStr2() throws InvocationTargetException, IllegalAccessException {
        String api = "me/";
        String str = (String) methodFilterApiStr.invoke(sc, api);
        assertEquals("/me.json", str);
    }

    @Test
    public void testPreFilterApiStr3() throws InvocationTargetException, IllegalAccessException {
        String api = "me.format";
        String str = (String) methodFilterApiStr.invoke(sc, api);
        assertEquals("/me.json", str);
    }

    @Test
    public void testPreFilterApiStr4() throws InvocationTargetException, IllegalAccessException {
        String api = "/me.xml";
        String str = (String) methodFilterApiStr.invoke(sc, api);
        assertEquals("/me.json", str);
    }

    @Test
    public void testPreDefineApiType() throws InvocationTargetException, IllegalAccessException {
        String api = "/me.json";
        SoundCloud.Type type = (SoundCloud.Type) methodDefineApiType.invoke(sc, api);
        assertTrue(SoundCloud.Type.USER == type);
    }

    // --------------------------------------------
    // Mock (offline)

    @Test
    public void testMockApiGetMe() {
        User a = mockedSc.getMe();
        assertEquals(a.getUsername(), "Darius");
    }

}
