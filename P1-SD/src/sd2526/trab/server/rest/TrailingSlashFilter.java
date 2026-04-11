package sd2526.trab.server.rest;

import java.net.URI;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.PreMatching;
import jakarta.ws.rs.ext.Provider;

@Provider
@PreMatching
public class TrailingSlashFilter implements ContainerRequestFilter {

  @Override
  public void filter(ContainerRequestContext requestContext) {
    URI requestUri = requestContext.getUriInfo().getRequestUri();
    String path = requestUri.getPath();

    if (path == null || path.length() <= 1 || !path.endsWith("/")) {
      return;
    }

    URI normalizedUri = URI.create(requestUri.toString().substring(0, requestUri.toString().length() - 1));
    requestContext.setRequestUri(normalizedUri);
  }
}
