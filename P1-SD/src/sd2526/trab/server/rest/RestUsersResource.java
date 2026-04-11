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
import sd2526.trab.server.UsersService;

@Path(RestUsers.PATH)
public class RestUsersResource {

  private static UsersService usersService;

  public static void setUsersService(UsersService service) {
    usersService = service;
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public String postUser(User user) {
    return RestExceptionMapper.resultOrThrow(usersService.postUser(user));
  }

  @GET
  @Path("/{" + RestUsers.NAME + "}")
  @Produces(MediaType.APPLICATION_JSON)
  public User getUser(@PathParam(RestUsers.NAME) String name,
      @QueryParam(RestUsers.PWD) String pwd) {
    return RestExceptionMapper.resultOrThrow(usersService.getUser(name, pwd));
  }

  @GET
  @Path("/internal/{" + RestUsers.NAME + "}")
  @Produces(MediaType.APPLICATION_JSON)
  public User getUserInternal(@PathParam("name") String name) {
    return RestExceptionMapper.resultOrThrow(usersService.getUserInternalNoAuth(name));
  }

  @PUT
  @Path("/{" + RestUsers.NAME + "}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public User updateUser(@PathParam(RestUsers.NAME) String name,
      @QueryParam(RestUsers.PWD) String pwd,
      User info) {
    return RestExceptionMapper.resultOrThrow(usersService.updateUser(name, pwd, info));
  }

  @DELETE
  @Path("/{" + RestUsers.NAME + "}")
  @Produces(MediaType.APPLICATION_JSON)
  public User deleteUser(@PathParam(RestUsers.NAME) String name,
      @QueryParam(RestUsers.PWD) String pwd) {
    return RestExceptionMapper.resultOrThrow(usersService.deleteUser(name, pwd));
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<User> searchUsers(@QueryParam(RestUsers.NAME) String name,
      @QueryParam(RestUsers.PWD) String pwd,
      @QueryParam(RestUsers.QUERY) String pattern) {
    return RestExceptionMapper.resultOrThrow(usersService.searchUsers(name, pwd, pattern));
  }
}
