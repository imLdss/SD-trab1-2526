package sd2526.trab.server.persistence;

import java.util.LinkedHashSet;
import java.util.Set;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "INBOX_MESSAGES")
public class InboxMessageRecord {

  @Id
  private String entryId;

  @Column(nullable = false)
  private String owner;

  @Column(nullable = false)
  private String mid;

  @Column(nullable = false)
  private String sender;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "INBOX_MESSAGE_DESTINATIONS", joinColumns = @JoinColumn(name = "entry_id"))
  @Column(name = "destination", nullable = false)
  private Set<String> destinations;

  @Column(nullable = false)
  private long creationTime;

  @Column(nullable = false, length = 1024)
  private String subject;

  @Column(nullable = false, length = 65535)
  private String contents;

  public InboxMessageRecord() {
    this.destinations = new LinkedHashSet<>();
  }

  public InboxMessageRecord(String owner, String mid, String sender, Set<String> destinations, long creationTime,
      String subject, String contents) {
    this();
    this.entryId = key(owner, mid);
    this.owner = owner;
    this.mid = mid;
    this.sender = sender;
    this.destinations = new LinkedHashSet<>(destinations);
    this.creationTime = creationTime;
    this.subject = subject;
    this.contents = contents;
  }

  public static String key(String owner, String mid) {
    return owner + "\t" + mid;
  }

  public String getEntryId() {
    return entryId;
  }

  public String getOwner() {
    return owner;
  }

  public String getMid() {
    return mid;
  }

  public String getSender() {
    return sender;
  }

  public Set<String> getDestinations() {
    return destinations;
  }

  public long getCreationTime() {
    return creationTime;
  }

  public String getSubject() {
    return subject;
  }

  public String getContents() {
    return contents;
  }
}
