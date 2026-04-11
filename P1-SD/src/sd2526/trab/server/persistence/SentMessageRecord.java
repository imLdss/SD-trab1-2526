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
@Table(name = "SENT_MESSAGES")
public class SentMessageRecord {

  @Id
  private String id;

  @Column(nullable = false)
  private String senderName;

  @Column(nullable = false)
  private long creationTime;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "SENT_MESSAGE_DESTINATIONS", joinColumns = @JoinColumn(name = "mid"))
  @Column(name = "destination", nullable = false)
  private Set<String> destinations;

  public SentMessageRecord() {
    this.destinations = new LinkedHashSet<>();
  }

  public SentMessageRecord(String id, String senderName, long creationTime, Set<String> destinations) {
    this();
    this.id = id;
    this.senderName = senderName;
    this.creationTime = creationTime;
    this.destinations = new LinkedHashSet<>(destinations);
  }

  public String getId() {
    return id;
  }

  public String getSenderName() {
    return senderName;
  }

  public long getCreationTime() {
    return creationTime;
  }

  public Set<String> getDestinations() {
    return destinations;
  }
}
