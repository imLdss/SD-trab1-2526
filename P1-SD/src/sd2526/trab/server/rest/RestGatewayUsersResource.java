package sd2526.trab.server.rest;

import java.util.List;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import sd2526.trab.api.User;
import sd2526.trab.api.rest.RestUsers;
import sd2526.trab.server.GatewayService;

@Path(RestUsers.PATH)
public class RestGatewayUsersResource {

  private static GatewayService gatewayService;

  public static void setGatewayService(GatewayService service) {
    gatewayService = service;
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public String postUser(User user) {
    return RestExceptionMapper.resultOrThrow(gatewayService.postUser(user));
  }

  @GET
  @Path("/{" + RestUsers.NAME + "}")
  @Produces(MediaType.APPLICATION_JSON)
  public User getUser(@PathParam(RestUsers.NAME) String name,
      @QueryParam(RestUsers.PWD) String pwd) {
    return RestExceptionMapper.resultOrThrow(gatewayService.getUser(name, pwd));
  }

  @PUT
  @Path("/{" + RestUsers.NAME + "}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public User updateUser(@PathParam(RestUsers.NAME) String name,
      @QueryParam(RestUsers.PWD) String pwd,
      User info) {
    return RestExceptionMapper.resultOrThrow(gatewayService.updateUser(name, pwd, info));
  }

  @DELETE
  @Path("/{" + RestUsers.NAME + "}")
  @Produces(MediaType.APPLICATION_JSON)
  public User deleteUser(@PathParam(RestUsers.NAME) String name,
      @QueryParam(RestUsers.PWD) String pwd) {
    return RestExceptionMapper.resultOrThrow(gatewayService.deleteUser(name, pwd));
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<User> searchUsers(@QueryParam(RestUsers.NAME) String name,
      @QueryParam(RestUsers.PWD) String pwd,
      @QueryParam(RestUsers.QUERY) String pattern) {
    return RestExceptionMapper.resultOrThrow(gatewayService.searchUsers(name, pwd, pattern));
  }
}
