package org.nuxeo.runtime.examplewebapp;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Simplest servlet we could come up with.
 * 
 * @author hmalphettes
 * @author Intalio
 */
public class MyServlet extends HttpServlet {
	
	@Override
	public void init() throws ServletException {
		System.err.println("initializing");
		super.init();
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.err.println("aha");
		resp.getWriter().write("aha a lovely servlet indeed");
	}

}
