package sd2526.trab.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import sd2526.trab.common.ServiceInfo;

public class Discovery implements AutoCloseable {

    public static final String DEFAULT_MULTICAST_ADDRESS = "226.226.226.226";
    public static final int DEFAULT_MULTICAST_PORT = 2266;

    private static final long DEFAULT_ANNOUNCE_PERIOD_MS = 1000L;

    private static final long DEFAULT_ENTRY_TTL_MS = 5000L;

    private static final int SOCKET_TIMEOUT_MS = 1000;

    private final InetSocketAddress groupAddress;
    private final NetworkInterface networkInterface;
    private final long announcePeriodMs;
    private final long entryTtlMs;

    private final Map<String, ServiceInfo> knownServices;
    private final List<ServiceInfo> localServices;

    private volatile boolean running;
    private Thread receiverThread;
    private Thread announcerThread;
    private Thread cleanerThread;

    public Discovery() {
        this(DEFAULT_MULTICAST_ADDRESS, DEFAULT_MULTICAST_PORT, null,
                DEFAULT_ANNOUNCE_PERIOD_MS, DEFAULT_ENTRY_TTL_MS);
    }

    public Discovery(String multicastAddress, int multicastPort) {
        this(multicastAddress, multicastPort, null,
                DEFAULT_ANNOUNCE_PERIOD_MS, DEFAULT_ENTRY_TTL_MS);
    }

    public Discovery(String multicastAddress,
            int multicastPort,
            NetworkInterface networkInterface,
            long announcePeriodMs,
            long entryTtlMs) {

        this.groupAddress = new InetSocketAddress(multicastAddress, multicastPort);
        this.networkInterface = networkInterface;
        this.announcePeriodMs = announcePeriodMs;
        this.entryTtlMs = entryTtlMs;

        this.knownServices = new ConcurrentHashMap<>();
        this.localServices = new CopyOnWriteArrayList<>();
    }

    public synchronized void start() {
        if (running) {
            return;
        }

        running = true;

        receiverThread = new Thread(this::runReceiver, "discovery-receiver");
        receiverThread.setDaemon(true);
        receiverThread.start();

        announcerThread = new Thread(this::runAnnouncer, "discovery-announcer");
        announcerThread.setDaemon(true);
        announcerThread.start();

        cleanerThread = new Thread(this::runCleaner, "discovery-cleaner");
        cleanerThread.setDaemon(true);
        cleanerThread.start();
    }

    public synchronized void stop() {
        running = false;

        if (receiverThread != null)
            receiverThread.interrupt();
        if (announcerThread != null)
            announcerThread.interrupt();
        if (cleanerThread != null)
            cleanerThread.interrupt();

        joinQuietly(receiverThread);
        joinQuietly(announcerThread);
        joinQuietly(cleanerThread);

        receiverThread = null;
        announcerThread = null;
        cleanerThread = null;
    }

    @Override
    public void close() {
        stop();
    }

    public void addLocalService(String serviceName, String domain, String uri) {
        validateService(serviceName, domain, uri);

        ServiceInfo info = new ServiceInfo(serviceName, domain, uri);
        localServices.removeIf(s -> sameEndpoint(s, info));
        localServices.add(info);

        knownServices.put(info.key(), info);
        if (running) {
            sendAnnouncement(info);
            if (announcerThread != null) {
                announcerThread.interrupt();
            }
        }
    }

    public void removeLocalService(String serviceName, String domain) {
        if (isBlank(serviceName) || isBlank(domain)) {
            return;
        }

        String key = key(serviceName, domain);
        localServices.removeIf(s -> key.equals(s.key()));
        knownServices.remove(key);
    }

    public Optional<String> getServiceURI(String serviceName, String domain) {
        if (isBlank(serviceName) || isBlank(domain)) {
            return Optional.empty();
        }

        cleanupExpiredEntries();

        ServiceInfo info = knownServices.get(key(serviceName, domain));
        return info == null ? Optional.empty() : Optional.of(info.getUri());
    }

    public Optional<ServiceInfo> getService(String serviceName, String domain) {
        if (isBlank(serviceName) || isBlank(domain)) {
            return Optional.empty();
        }

        cleanupExpiredEntries();

        ServiceInfo info = knownServices.get(key(serviceName, domain));
        return Optional.ofNullable(info);
    }

    public List<ServiceInfo> knownServices() {
        cleanupExpiredEntries();
        return Collections.unmodifiableList(new ArrayList<>(knownServices.values()));
    }

    public List<ServiceInfo> servicesByName(String serviceName) {
        cleanupExpiredEntries();

        List<ServiceInfo> result = new ArrayList<>();
        for (ServiceInfo info : knownServices.values()) {
            if (Objects.equals(serviceName, info.getServiceName())) {
                result.add(info);
            }
        }
        return result;
    }

    private void runReceiver() {
        try (MulticastSocket socket = new MulticastSocket(groupAddress.getPort())) {
            socket.setSoTimeout(SOCKET_TIMEOUT_MS);
            socket.setReuseAddress(true);

            if (networkInterface != null) {
                socket.setNetworkInterface(networkInterface);
            }

            InetAddress group = InetAddress.getByName(groupAddress.getHostString());
            InetSocketAddress isa = new InetSocketAddress(group, groupAddress.getPort());

            if (networkInterface != null) {
                socket.joinGroup(isa, networkInterface);
            } else {
                socket.joinGroup(group);
            }

            byte[] buffer = new byte[65535];

            while (running) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                try {
                    socket.receive(packet);

                    String message = new String(
                            packet.getData(),
                            packet.getOffset(),
                            packet.getLength(),
                            StandardCharsets.UTF_8);

                    ServiceInfo info = parseAnnouncement(message);
                    if (info != null) {
                        info.touch();
                        knownServices.merge(
                                info.key(),
                                info,
                                (oldValue, newValue) -> {
                                    oldValue.touch(newValue.getLastSeen());
                                    if (!Objects.equals(oldValue.getUri(), newValue.getUri())) {
                                        return new ServiceInfo(
                                                newValue.getServiceName(),
                                                newValue.getDomain(),
                                                newValue.getUri(),
                                                newValue.getLastSeen());
                                    }
                                    return oldValue;
                                });
                    }
                } catch (java.net.SocketTimeoutException ignored) {
                } catch (Exception ignored) {
                }
            }

            try {
                if (networkInterface != null) {
                    socket.leaveGroup(isa, networkInterface);
                } else {
                    socket.leaveGroup(group);
                }
            } catch (Exception ignored) {
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void runAnnouncer() {
        try (MulticastSocket socket = new MulticastSocket()) {
            socket.setTimeToLive(1);

            if (networkInterface != null) {
                socket.setNetworkInterface(networkInterface);
            }

            InetAddress group = InetAddress.getByName(groupAddress.getHostString());

            while (running) {
                try {
                    for (ServiceInfo info : localServices) {
                        byte[] data = encodeAnnouncement(info).getBytes(StandardCharsets.UTF_8);
                        DatagramPacket packet = new DatagramPacket(
                                data,
                                data.length,
                                group,
                                groupAddress.getPort());

                        socket.send(packet);

                        info.touch();
                        knownServices.put(info.key(),
                                new ServiceInfo(
                                        info.getServiceName(),
                                        info.getDomain(),
                                        info.getUri(),
                                        info.getLastSeen()));
                    }

                    Thread.sleep(announcePeriodMs);
                } catch (InterruptedException e) {
                    if (!running) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                } catch (Exception ignored) {
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendAnnouncement(ServiceInfo info) {
        try (MulticastSocket socket = new MulticastSocket()) {
            socket.setTimeToLive(1);

            if (networkInterface != null) {
                socket.setNetworkInterface(networkInterface);
            }

            InetAddress group = InetAddress.getByName(groupAddress.getHostString());
            byte[] data = encodeAnnouncement(info).getBytes(StandardCharsets.UTF_8);
            DatagramPacket packet = new DatagramPacket(
                    data,
                    data.length,
                    group,
                    groupAddress.getPort());

            socket.send(packet);

            info.touch();
            knownServices.put(info.key(),
                    new ServiceInfo(
                            info.getServiceName(),
                            info.getDomain(),
                            info.getUri(),
                            info.getLastSeen()));
        } catch (Exception ignored) {
        }
    }

    private void runCleaner() {
        while (running) {
            try {
                cleanupExpiredEntries();
                Thread.sleep(Math.max(1000L, entryTtlMs / 2));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception ignored) {
            }
        }
    }

    private void cleanupExpiredEntries() {
        long now = System.currentTimeMillis();
        knownServices.entrySet().removeIf(entry -> now - entry.getValue().getLastSeen() > entryTtlMs);
    }

    private String encodeAnnouncement(ServiceInfo info) {
        return info.getServiceName() + "@" + info.getDomain() + "\t" + info.getUri();
    }

    private ServiceInfo parseAnnouncement(String raw) {
        if (raw == null) {
            return null;
        }

        String msg = raw.trim();
        int tabIndex = msg.indexOf('\t');
        int atIndex = msg.indexOf('@');

        if (tabIndex <= 0 || atIndex <= 0 || atIndex >= tabIndex - 1) {
            return null;
        }

        String service = msg.substring(0, atIndex).trim();
        String domain = msg.substring(atIndex + 1, tabIndex).trim();
        String uri = msg.substring(tabIndex + 1).trim();

        if (isBlank(service) || isBlank(domain) || isBlank(uri)) {
            return null;
        }

        if (!uri.startsWith("http:") && !uri.startsWith("grpc:")) {
            return null;
        }

        return new ServiceInfo(service, domain, uri);
    }

    private void validateService(String serviceName, String domain, String uri) {
        if (isBlank(serviceName) || isBlank(domain) || isBlank(uri)) {
            throw new IllegalArgumentException("serviceName, domain and uri must be non-empty");
        }

        if (!uri.startsWith("http:") && !uri.startsWith("grpc:")) {
            throw new IllegalArgumentException("uri must start with http: or grpc:");
        }
    }

    private boolean sameEndpoint(ServiceInfo a, ServiceInfo b) {
        return Objects.equals(a.getServiceName(), b.getServiceName())
                && Objects.equals(a.getDomain(), b.getDomain());
    }

    private String key(String serviceName, String domain) {
        return serviceName + "@" + domain;
    }

    private boolean isBlank(String s) {
        return s == null || s.isBlank();
    }

    private void joinQuietly(Thread thread) {
        if (thread == null)
            return;
        try {
            thread.join(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
