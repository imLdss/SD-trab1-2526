package sd2526.trab.server.rest;

import java.util.List;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import sd2526.trab.api.Message;
import sd2526.trab.api.rest.RestMessages;
import sd2526.trab.server.GatewayService;

@Path(RestMessages.PATH)
public class RestGatewayMessagesResource {

  private static GatewayService gatewayService;

  public static void setGatewayService(GatewayService service) {
    gatewayService = service;
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public String postMessage(@QueryParam(RestMessages.PWD) String pwd, Message msg) {
    return RestExceptionMapper.resultOrThrow(gatewayService.postMessage(pwd, msg));
  }

  @GET
  @Path(RestMessages.MBOX + "/{" + RestMessages.NAME + "}/{" + RestMessages.MID + "}")
  @Produces(MediaType.APPLICATION_JSON)
  public Message getMessage(@PathParam(RestMessages.NAME) String name,
      @PathParam(RestMessages.MID) String mid,
      @QueryParam(RestMessages.PWD) String pwd) {
    return RestExceptionMapper.resultOrThrow(gatewayService.getInboxMessage(name, mid, pwd));
  }

  @GET
  @Path(RestMessages.MBOX + "/{" + RestMessages.NAME + "}")
  @Produces(MediaType.APPLICATION_JSON)
  public List<String> getMessages(@PathParam(RestMessages.NAME) String name,
      @QueryParam(RestMessages.PWD) String pwd,
      @QueryParam(RestMessages.QUERY) @DefaultValue("") String query) {
    if (query == null || query.isBlank()) {
      return RestExceptionMapper.resultOrThrow(gatewayService.getAllInboxMessages(name, pwd));
    }
    return RestExceptionMapper.resultOrThrow(gatewayService.searchInbox(name, pwd, query));
  }

  @DELETE
  @Path(RestMessages.MBOX + "/{" + RestMessages.NAME + "}/{" + RestMessages.MID + "}")
  public void removeFromUserInbox(@PathParam(RestMessages.NAME) String name,
      @PathParam(RestMessages.MID) String mid,
      @QueryParam(RestMessages.PWD) String pwd) {
    RestExceptionMapper.voidResultOrThrow(gatewayService.removeInboxMessage(name, mid, pwd));
  }

  @DELETE
  @Path("/{" + RestMessages.NAME + "}/{" + RestMessages.MID + "}")
  public void deleteMessage(@PathParam(RestMessages.NAME) String name,
      @PathParam(RestMessages.MID) String mid,
      @QueryParam(RestMessages.PWD) String pwd) {
    RestExceptionMapper.voidResultOrThrow(gatewayService.deleteMessage(name, mid, pwd));
  }
}
