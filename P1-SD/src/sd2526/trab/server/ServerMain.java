package sd2526.trab.server;

import java.net.InetAddress;
import java.net.URI;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJsonProvider;
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import com.sun.net.httpserver.HttpServer;

import sd2526.trab.api.java.Messages;
import sd2526.trab.api.java.Users;
import sd2526.trab.clients.LocalUserDirectory;
import sd2526.trab.clients.RestUserDirectory;
import sd2526.trab.clients.UserDirectory;
import sd2526.trab.server.grpc.GrpcMessagesResource;
import sd2526.trab.server.grpc.GrpcUsersResource;
import sd2526.trab.server.persistence.Hibernate;
import sd2526.trab.server.rest.RestGatewayMessagesResource;
import sd2526.trab.server.rest.RestGatewayUsersResource;
import sd2526.trab.server.rest.RestMessagesResource;
import sd2526.trab.server.rest.TrailingSlashFilter;
import sd2526.trab.server.rest.RestUsersResource;

public class ServerMain {

  public static void main(String[] args) throws Exception {

    String service = args.length > 0 ? args[0].toLowerCase() : "all";
    String host = InetAddress.getLocalHost().getHostName();
    String domain = args.length > 1 ? args[1] : extractDomain(host);

    Hibernate.init(databasePath(service, domain).toString());

    int port = switch (service) {
      case "messages" -> 8080;
      case "users" -> 8081;
      case "users-grpc" -> 8082;
      case "messages-grpc" -> 8083;
      case "gateway" -> 8082;
      case "all" -> 8080;
      default -> throw new IllegalArgumentException("Unknown service: " + service);
    };

    UsersService usersService = new UsersService(domain);
    Discovery discovery = new Discovery();

    MessagesService messagesService = null;

    ResourceConfig config = new ResourceConfig();
    config.register(JacksonJsonProvider.class);
    config.register(TrailingSlashFilter.class);
    HttpServer restServer = null;
    ExecutorService restExecutor = null;
    Server grpcServer = null;

    boolean announceUsers = false;
    boolean announceMessages = false;
    boolean announceGateway = false;
    boolean discoveryStarted = false;

    switch (service) {
      case "users" -> {
        config.register(RestUsersResource.class);
        RestUsersResource.setUsersService(usersService);
        announceUsers = true;
      }

      case "messages" -> {
        discovery.start();
        discoveryStarted = true;

        UserDirectory userDirectory = new RestUserDirectory(discovery, domain, false);
        messagesService = new MessagesService(domain, userDirectory, discovery);

        config.register(RestMessagesResource.class);
        RestMessagesResource.setMessagesService(messagesService);

        announceMessages = true;
      }

      case "all" -> {
        UserDirectory userDirectory = new LocalUserDirectory(usersService);
        messagesService = new MessagesService(domain, userDirectory, discovery);
        usersService.onUserDeleted(messagesService::deleteUserInbox);

        config.register(RestUsersResource.class);
        config.register(RestMessagesResource.class);

        RestUsersResource.setUsersService(usersService);
        RestMessagesResource.setMessagesService(messagesService);

        announceUsers = true;
        announceMessages = true;

        discovery.start();
        discoveryStarted = true;
      }

      case "users-grpc" -> {
        grpcServer = ServerBuilder.forPort(port)
            .addService(new GrpcUsersResource(usersService))
            .build()
            .start();
        announceUsers = true;
      }

      case "messages-grpc" -> {
        discovery.start();
        discoveryStarted = true;

        UserDirectory userDirectory = new RestUserDirectory(discovery, domain, true);
        messagesService = new MessagesService(domain, userDirectory, discovery, true);

        grpcServer = ServerBuilder.forPort(port)
            .addService(new GrpcMessagesResource(messagesService))
            .build()
            .start();
        announceMessages = true;
      }

      case "gateway" -> {
        discovery.start();
        discoveryStarted = true;

        GatewayService gatewayService = new GatewayService(discovery, domain);

        config.register(RestGatewayUsersResource.class);
        config.register(RestGatewayMessagesResource.class);

        RestGatewayUsersResource.setGatewayService(gatewayService);
        RestGatewayMessagesResource.setGatewayService(gatewayService);

        announceGateway = true;
      }
    }

    String baseUri = null;
    if (service.equals("users") || service.equals("messages") || service.equals("all") || service.equals("gateway")) {
      baseUri = "http://0.0.0.0:" + port + "/rest";
      restServer = JdkHttpServerFactory.createHttpServer(URI.create(baseUri), config, false);
      restExecutor = service.equals("users")
          ? Executors.newSingleThreadExecutor()
          : Executors.newCachedThreadPool();
      restServer.setExecutor(restExecutor);
      restServer.start();
    }

    boolean grpcMode = service.endsWith("grpc");

    if (!discoveryStarted) {
      discovery.start();
      discoveryStarted = true;
    }

    if (announceUsers) {
      discovery.addLocalService(
          Users.SERVICE_NAME,
          domain,
          grpcMode
              ? "grpc://" + host + ":" + port + "/grpc"
              : "http://" + host + ":" + port + "/rest");
    }

    if (announceMessages) {
      discovery.addLocalService(
          Messages.SERVICE_NAME,
          domain,
          grpcMode
              ? "grpc://" + host + ":" + port + "/grpc"
              : "http://" + host + ":" + port + "/rest");
    }

    if (announceGateway) {
      discovery.addLocalService(
          "Gateway",
          domain,
          "http://" + host + ":" + port + "/rest");
    }

    HttpServer finalRestServer = restServer;
    ExecutorService finalRestExecutor = restExecutor;
    Server finalGrpcServer = grpcServer;

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      try {
        discovery.close();
      } catch (Exception ignored) {
      }
      Hibernate.close();
      if (finalRestServer != null) {
        finalRestServer.stop(0);
      }
      if (finalRestExecutor != null) {
        finalRestExecutor.shutdownNow();
      }
      if (finalGrpcServer != null) {
        finalGrpcServer.shutdown();
      }
    }));

    System.out.println("Server running:");
    System.out.println("  host   = " + host);
    System.out.println("  domain = " + domain);
    System.out.println("  mode   = " + service);
    System.out.println("  base   = " + (baseUri != null ? baseUri : "grpc://" + host + ":" + port + "/grpc"));

    if (grpcServer != null) {
      grpcServer.awaitTermination();
    }
  }

  private static String extractDomain(String host) {
    int idx = host.indexOf('.');
    if (idx < 0 || idx == host.length() - 1) {
      throw new IllegalStateException(
          "Hostname must be in the form server.domain. Hostname was: " + host);
    }
    return host.substring(idx + 1);
  }

  private static Path databasePath(String service, String domain) {
    return Path.of("db", domain, service, "state");
  }
}
