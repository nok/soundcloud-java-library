package de.voidplus.soundcloud;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

public class SoundCloudTest {

	private static Map<String, String> ENV;
	
    @BeforeClass
    public static void setUp() {
    	ENV = System.getenv();
    }
	
	@Test
	public void testLogin() {
		SoundCloud sc = new SoundCloud(ENV.get("APP_ID"), ENV.get("APP_SECRET"));
		boolean login = sc.login(ENV.get("LOGIN_MAIL"), ENV.get("LOGIN_PASS"));
		assertTrue("login", login);
	}

}
