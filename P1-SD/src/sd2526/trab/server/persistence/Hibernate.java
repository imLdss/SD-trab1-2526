package sd2526.trab.server.persistence;

import java.nio.file.Files;
import java.nio.file.Path;

import jakarta.persistence.LockTimeoutException;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.PessimisticLockException;
import jakarta.persistence.EntityExistsException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.internal.util.config.ConfigurationException;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.LockAcquisitionException;

import sd2526.trab.api.User;

public class Hibernate {

  private static SessionFactory sessionFactory;
  private static final String JDBC_URL_PREFIX = "jdbc:hsqldb:file:";
  private static final String CONNECTION_POOL_SIZE = "32";
  private static final int MAX_TRANSACTION_RETRIES = 8;
  private static final long RETRY_SLEEP_MS = 25L;

  private Hibernate() {
  }

  public static synchronized void init(String dbPath) {
    if (sessionFactory != null) {
      return;
    }

    Configuration config = loadConfiguration();
    config.addAnnotatedClass(User.class);
    config.addAnnotatedClass(InboxMessageRecord.class);
    config.addAnnotatedClass(RemovedInboxMessageRecord.class);
    config.addAnnotatedClass(SentMessageRecord.class);
    config.setProperty("connection.pool_size", CONNECTION_POOL_SIZE);
    config.setProperty("hibernate.connection.pool_size", CONNECTION_POOL_SIZE);
    if (dbPath != null && !dbPath.isBlank()) {
      if (!dbPath.startsWith("jdbc:")) {
        Path filePath = Path.of(dbPath);
        Path parent = filePath.getParent();
        if (parent != null) {
          try {
            Files.createDirectories(parent);
          } catch (java.io.IOException e) {
            throw new RuntimeException(e);
          }
        }
      }
      String jdbcUrl = dbPath.startsWith("jdbc:") ? dbPath : JDBC_URL_PREFIX + dbPath;
      config.setProperty("connection.url", jdbcUrl);
      config.setProperty("hibernate.connection.url", jdbcUrl);
    }

    var serviceRegistry = new StandardServiceRegistryBuilder()
        .applySettings(config.getProperties())
        .build();

    sessionFactory = config.buildSessionFactory(serviceRegistry);
  }

  private static Configuration loadConfiguration() {
    try {
      return new Configuration().configure();
    } catch (ConfigurationException e) {
      Path filePath = Path.of("hibernate.cfg.xml");
      if (Files.exists(filePath)) {
        return new Configuration().configure(filePath.toFile());
      }
      throw e;
    }
  }

  public static SessionFactory getSessionFactory() {
    if (sessionFactory == null) {
      init(null);
    }
    return sessionFactory;
  }

  public static synchronized void close() {
    if (sessionFactory != null) {
      sessionFactory.close();
      sessionFactory = null;
    }
  }

  public static <T> T execute(TransactionCallback<T> callback) {
    for (int attempt = 1; attempt <= MAX_TRANSACTION_RETRIES; attempt++) {
      var session = getSessionFactory().openSession();
      var tx = session.beginTransaction();

      try {
        T result = callback.execute(session);
        tx.commit();
        return result;
      } catch (RuntimeException e) {
        if (tx != null && tx.isActive()) {
          tx.rollback();
        }
        if (attempt < MAX_TRANSACTION_RETRIES && isRetryableTransactionFailure(e)) {
          sleepQuietly(RETRY_SLEEP_MS);
          continue;
        }
        throw e;
      } finally {
        session.close();
      }
    }

    throw new IllegalStateException("Unreachable retry loop termination");
  }

  private static boolean isRetryableTransactionFailure(Throwable error) {
    Throwable current = error;
    while (current != null) {
      if (current instanceof PessimisticLockException
          || current instanceof LockTimeoutException
          || current instanceof LockAcquisitionException
          || current instanceof OptimisticLockException
          || current instanceof ConstraintViolationException
          || current instanceof EntityExistsException) {
        return true;
      }
      current = current.getCause();
    }
    return false;
  }

  private static void sleepQuietly(long millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  @FunctionalInterface
  public interface TransactionCallback<T> {
    T execute(org.hibernate.Session session);
  }
}
