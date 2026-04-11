package sd2526.trab.server.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "REMOVED_INBOX_MESSAGES")
public class RemovedInboxMessageRecord {

  @Id
  private String entryId;

  @Column(nullable = false)
  private String owner;

  @Column(nullable = false)
  private String mid;

  public RemovedInboxMessageRecord() {
  }

  public RemovedInboxMessageRecord(String owner, String mid) {
    this.entryId = key(owner, mid);
    this.owner = owner;
    this.mid = mid;
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
}
