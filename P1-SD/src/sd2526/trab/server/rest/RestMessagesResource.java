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
import sd2526.trab.server.MessagesService;

@Path(RestMessages.PATH)
public class RestMessagesResource {

  private static MessagesService messagesService;

  public static void setMessagesService(MessagesService service) {
    messagesService = service;
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public String postMessage(@QueryParam(RestMessages.PWD) String pwd, Message msg) {
    return RestExceptionMapper.resultOrThrow(messagesService.postMessage(pwd, msg));
  }

  @POST
  @Path("/internal/{" + RestMessages.NAME + "}")
  @Consumes(MediaType.APPLICATION_JSON)
  public void deliverMessageInternal(@PathParam(RestMessages.NAME) String name, Message msg) {
      RestExceptionMapper.voidResultOrThrow(messagesService.deliverMessageToLocalInbox(name, msg));
  }

  @GET
  @Path(RestMessages.MBOX + "/{" + RestMessages.NAME + "}/{" + RestMessages.MID + "}")
  @Produces(MediaType.APPLICATION_JSON)
  public Message getMessage(@PathParam(RestMessages.NAME) String name,
      @PathParam(RestMessages.MID) String mid,
      @QueryParam(RestMessages.PWD) String pwd) {
    return RestExceptionMapper.resultOrThrow(messagesService.getInboxMessage(name, mid, pwd));
  }

  @GET
  @Path(RestMessages.MBOX + "/{" + RestMessages.NAME + "}")
  @Produces(MediaType.APPLICATION_JSON)
  public List<String> getMessages(@PathParam(RestMessages.NAME) String name,
      @QueryParam(RestMessages.PWD) String pwd,
      @QueryParam(RestMessages.QUERY) @DefaultValue("") String query) {
    if (query == null || query.isBlank()) {
      return RestExceptionMapper.resultOrThrow(messagesService.getAllInboxMessages(name, pwd));
    }
    return RestExceptionMapper.resultOrThrow(messagesService.searchInbox(name, pwd, query));
  }

  @DELETE
  @Path(RestMessages.MBOX + "/{" + RestMessages.NAME + "}/{" + RestMessages.MID + "}")
  public void removeFromUserInbox(@PathParam(RestMessages.NAME) String name,
      @PathParam(RestMessages.MID) String mid,
      @QueryParam(RestMessages.PWD) String pwd) {
    RestExceptionMapper.voidResultOrThrow(messagesService.removeInboxMessage(name, mid, pwd));
  }

  @DELETE
  @Path("/{" + RestMessages.NAME + "}/{" + RestMessages.MID + "}")
  public void deleteMessage(@PathParam(RestMessages.NAME) String name,
      @PathParam(RestMessages.MID) String mid,
      @QueryParam(RestMessages.PWD) String pwd) {
    RestExceptionMapper.voidResultOrThrow(messagesService.deleteMessage(name, mid, pwd));
  }

  @DELETE
  @Path("/internal/{" + RestMessages.NAME + "}/{" + RestMessages.MID + "}")
  public void deleteMessageInternal(@PathParam(RestMessages.NAME) String name,
          @PathParam(RestMessages.MID) String mid) {
      RestExceptionMapper.voidResultOrThrow(messagesService.removeDeliveredMessageFromInbox(name, mid));
  }



}
