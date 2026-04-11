package sd2526.trab.api.rest;

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

@Path(RestMessages.PATH)
public interface RestMessages {
	
	final String PATH = "/messages";
	final String QUERY = "query";
	final String NAME = "name";
	final String PWD = "pwd";
	final String MID = "mid";
	final String MBOX = "/mbox";

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	String postMessage(@QueryParam(PWD) String pwd, Message msg);

	@GET
	@Path(MBOX + "/{" + NAME + "}/{" + MID + "}")
	@Produces(MediaType.APPLICATION_JSON)
	Message getMessage(@PathParam(NAME) String name, @PathParam(MID) String mid, @QueryParam(PWD) String pwd);

	/**
	 * Returns the list of message ids in the user's inbox that match the query. 
	 * If the query is empty, returns all message ids in the user's inbox.
	 * Otherwise, a message matches when the query is a substring of the subject 
	 * or contents, case-insensitive. 
	 * This method will use two service methods - getAllInboxMessages and searchInbox - 
	 * depending on the query parameter.
	 * 
	 * @param name Owner of the inbox
	 * @param pwd Password of the owner of the inbox
	 * @param query Query string to match the messages. If empty, matches all messages.
	 */
	@GET
	@Path(MBOX + "/{" + NAME + "}")
	@Produces(MediaType.APPLICATION_JSON)
	List<String> getMessages(@PathParam(NAME) String name, @QueryParam(PWD) String pwd, @QueryParam(QUERY) @DefaultValue("") String query);

	@DELETE
	@Path(MBOX + "/{" + NAME + "}/{" + MID + "}")
	void removeFromUserInbox(@PathParam(NAME) String name, @PathParam(MID) String mid, @QueryParam(PWD) String pwd);

	@DELETE
	@Path("/{" + NAME + "}/{" + MID + "}")
	void deleteMessage(@PathParam(NAME) String name, @PathParam(MID) String mid, @QueryParam(PWD) String pwd);
}
