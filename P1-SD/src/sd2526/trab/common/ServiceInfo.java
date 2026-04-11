package sd2526.trab.common;

import java.util.Objects;

public class ServiceInfo {

    private final String serviceName;
    private final String domain;
    private final String uri;
    private volatile long lastSeen;

    public ServiceInfo(String serviceName, String domain, String uri) {
        this(serviceName, domain, uri, System.currentTimeMillis());
    }

    public ServiceInfo(String serviceName, String domain, String uri, long lastSeen) {
        this.serviceName = serviceName;
        this.domain = domain;
        this.uri = uri;
        this.lastSeen = lastSeen;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getDomain() {
        return domain;
    }

    public String getUri() {
        return uri;
    }

    public long getLastSeen() {
        return lastSeen;
    }

    public void touch() {
        this.lastSeen = System.currentTimeMillis();
    }

    public void touch(long timestamp) {
        this.lastSeen = timestamp;
    }

    public String key() {
        return serviceName + "@" + domain;
    }

    @Override
    public String toString() {
        return "ServiceInfo{" +
                "serviceName='" + serviceName + '\'' +
                ", domain='" + domain + '\'' +
                ", uri='" + uri + '\'' +
                ", lastSeen=" + lastSeen +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceName, domain, uri);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof ServiceInfo other))
            return false;
        return Objects.equals(serviceName, other.serviceName)
                && Objects.equals(domain, other.domain)
                && Objects.equals(uri, other.uri);
    }
}
