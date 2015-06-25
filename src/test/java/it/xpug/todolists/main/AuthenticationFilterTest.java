package it.xpug.todolists.main;

import static org.junit.Assert.*;

import javax.servlet.http.*;

import org.junit.*;


public class AuthenticationFilterTest {

	InMemorySessionRepository sessionRepository = new InMemorySessionRepository();
	AuthenticationFilter filter = new AuthenticationFilter(sessionRepository);
	TodoListSession session = new TodoListSession("1234", null);

	@Test
	public void accessWithoutCookie() {
		assertNull("null cookies", filter.getSession(null));
		assertNull("empty cookies", filter.getSession(new Cookie[] {} ));
		assertNull("cookie with bad id", filter.getSession(new Cookie[] { new Cookie("something", "something")} ));
	}

	@Test
    public void accessWithInvalidCookie() throws Exception {
		sessionRepository.add(session);

		Cookie cookie = new Cookie(AuthenticationFilter.SESSION_COOKIE, "4567");
		assertEquals("session not found", null, filter.getSession(new Cookie[] {cookie}));
    }

	@Test
    public void accessWithValidCookie() throws Exception {
		sessionRepository.add(session);

		Cookie cookie = new Cookie(AuthenticationFilter.SESSION_COOKIE, "1234");
		assertEquals(session, filter.getSession(new Cookie[] {cookie}));
    }

}
