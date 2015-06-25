package it.xpug.todolists.main;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

public class TodoListsServlet extends HttpServlet {

	private TodoListRepository todoLists;
	private AuthenticationFilter authenticationFilter;
	private SessionRepository sessions;

	public TodoListsServlet(TodoListRepository todoLists, SessionRepository sessions) {
		this.todoLists = todoLists;
		this.sessions = sessions;
		this.authenticationFilter = new AuthenticationFilter(sessions);
    }

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Serving: " + request.getMethod() + " " + request.getRequestURI());
		//delay();

		Resource resource = getResource(request, response);
		resource.service();
	}

	protected void delay() {
	    try {
	        Thread.sleep(1000);
        } catch (InterruptedException e) {
	        throw new RuntimeException(e);
        }
    }

	private Resource getResource(HttpServletRequest request, HttpServletResponse response) {
		if (request.getRequestURI().matches("/login")) {
			return new LoginResource(request, response, sessions);
		}
		if (request.getRequestURI().matches("/register")) {
			return new LoginResource(request, response, sessions);
		}

		TodoListSession session = authenticationFilter.getSession(request.getCookies());
		if (null == session) {
			return new NotAuthorized(response);
		}

		if (request.getRequestURI().matches("/todolists/\\d+/items(/\\d+)?")) {
			return new TodoItemsResource(request, response, todoLists);
		}
		if (request.getRequestURI().matches("/todolists(/\\d+)?")) {
			return new TodoListsResource(request, response, todoLists);
		}
		if ("/".equals(request.getRequestURI())) {
			return new HelloWorldResource(response);
		}
	    return new Notfound(response);
    }

}
