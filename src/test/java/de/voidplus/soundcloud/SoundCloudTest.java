package de.voidplus.soundcloud;

import static org.junit.Assert.*;

import org.junit.Test;

public class SoundCloudTest {

	@Test
	public void testLogin() {
		SoundCloud sc = new SoundCloud(Settings.APP_ID, Settings.APP_SECRET);
		boolean login = sc.login(Settings.LOGIN_MAIL, Settings.LOGIN_PASS);
		assertTrue("login", login);
	}

}
