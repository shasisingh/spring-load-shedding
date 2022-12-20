package com.shashi.spring.springloadshedding.config;

import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import static java.nio.charset.StandardCharsets.UTF_8;

@Configuration
public class BookingRequestFilter implements Filter {

  private final int maxConcurrentRequest = 2;

  private final AtomicInteger currentRequests = new AtomicInteger(0);

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

    if (currentRequests.get() >= maxConcurrentRequest) {
      var response = (HttpServletResponse) servletResponse;
      sendUnavailable(response, "Server Unavailable. Please retry after sometime.");
      return;
    }
    currentRequests.incrementAndGet();
    try {
      filterChain.doFilter(servletRequest, servletResponse);
    } finally {
      currentRequests.decrementAndGet();
    }
  }

  private void sendUnavailable(HttpServletResponse response, final String messages) throws IOException {
    response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE, messages);
  }

  private void sendUnavailableAsBody(HttpServletResponse response) throws IOException {
    response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
    byte[] byteMessage = "Server Unavailable. Please retry after sometime..".getBytes(UTF_8);
    response.getOutputStream().write(byteMessage);
  }
}
