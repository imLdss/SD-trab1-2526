package sd2526.trab.api;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a message in the system.
 */
public class Message {

	private String id;	
	private String sender;
	
	private Set<String> destination;
	private long creationTime;
	private String subject;	
	private String contents;
	
	public Message() {
		this(null, null, Collections.emptySet(), null, null);
	}
	
	public Message(String sender, String destination, String subject, String contents) {
		this(null, sender, Set.of(destination), subject, contents);
	}
	
	public Message(String sender, Set<String> destinations, String subject, String contents) {
		this(null, sender, destinations, subject, contents);
	}

	public Message(String id, String sender, String destination, String subject, String contents) {
		this(id, sender, Set.of(destination), subject, contents);
	}

	public Message(String id, String sender, Set<String> destinations, String subject, String contents) {
		this.id = id;
		this.sender = sender;
		this.subject = subject;
		this.contents = contents;
		this.creationTime = System.currentTimeMillis();
		this.destination = new HashSet<String>(destinations);
	}

	public String getSender() {
		return sender;
	}
	
	public void setSender(String sender) {
		this.sender = sender;
	}
	
	public Set<String> getDestination() {
		return destination;
	}
	
	public void setDestination(Set<String> destination) {
		this.destination = destination;
	}
	
	public void addDestination(String destination) {
		this.destination.add(destination);
	}

	public long getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(long creationTime) {
		this.creationTime = creationTime;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Message{" +
				"id=" + id +
				", sender='" + sender + '\'' +
				", destination=" + destination +
				", creationTime=" + creationTime +
				", subject='" + subject + '\'' +
				", contents=" + (contents.length() > 20? contents.substring(0,20) : contents )+
				'}';
	}
}
