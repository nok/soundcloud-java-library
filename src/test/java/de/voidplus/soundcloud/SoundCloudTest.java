package de.voidplus.soundcloud;

import static org.junit.Assert.*;

import org.junit.Test;

public class SoundCloudTest {
	
	@Test
	public void testLogin() {
		SoundCloud sc = new SoundCloud(System.getenv("APP_ID"), System.getenv("APP_SECRET"));
		boolean login = sc.login(System.getenv("LOGIN_MAIL"), System.getenv("LOGIN_PASS"));
		assertTrue("login", login);
	}

}
