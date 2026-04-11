package sd2526.trab.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

import sd2526.trab.api.User;
import sd2526.trab.api.java.Result;
import sd2526.trab.api.java.Users;
import sd2526.trab.server.persistence.Hibernate;

public class UsersService implements Users {

  private final String domain;
  private final List<Consumer<String>> deleteListeners;

  public UsersService(String domain) {
    this.domain = domain;
    this.deleteListeners = new CopyOnWriteArrayList<>();
  }

  @Override
  public synchronized Result<String> postUser(User user) {
    if (!isValidNewUser(user)) {
      return Result.error(Result.ErrorCode.BAD_REQUEST);
    }

    if (!domain.equals(user.getDomain())) {
      return Result.error(Result.ErrorCode.FORBIDDEN);
    }

    String name = user.getName();

    return Hibernate.execute(session -> {
      User existing = session.find(User.class, name);
      if (existing != null) {
        if (sameUser(existing, user)) {
          return Result.ok(userAddress(name));
        } else {
          return Result.error(Result.ErrorCode.CONFLICT);
        }
      }

      session.persist(copyUser(user));
      return Result.ok(userAddress(name));
    });
  }

  @Override
  public synchronized Result<User> getUser(String name, String pwd) {
    if (isBlank(name) || pwd == null) {
      return Result.error(Result.ErrorCode.BAD_REQUEST);
    }

    User user = Hibernate.execute(session -> session.find(User.class, name));
    if (user == null || !pwd.equals(user.getPwd())) {
      return Result.error(Result.ErrorCode.FORBIDDEN);
    }

    return Result.ok(copyUser(user));
  }

  @Override
  public synchronized Result<User> updateUser(String name, String pwd, User info) {
    if (isBlank(name) || pwd == null || info == null) {
      return Result.error(Result.ErrorCode.BAD_REQUEST);
    }

    return Hibernate.execute(session -> {
      User user = session.find(User.class, name);
      if (user == null || !pwd.equals(user.getPwd())) {
        return Result.error(Result.ErrorCode.FORBIDDEN);
      }

      if (info.getPwd() != null) {
        if (isBlank(info.getPwd())) {
          return Result.error(Result.ErrorCode.BAD_REQUEST);
        }
        user.setPwd(info.getPwd());
      }

      if (info.getDisplayName() != null) {
        if (isBlank(info.getDisplayName())) {
          return Result.error(Result.ErrorCode.BAD_REQUEST);
        }
        user.setDisplayName(info.getDisplayName());
      }

      if (info.getDomain() != null) {
        if (!domain.equals(info.getDomain())) {
          return Result.error(Result.ErrorCode.BAD_REQUEST);
        }
        user.setDomain(info.getDomain());
      }

      if (info.getName() != null && !name.equals(info.getName())) {
        return Result.error(Result.ErrorCode.BAD_REQUEST);
      }

      return Result.ok(copyUser(user));
    });
  }

  @Override
  public synchronized Result<User> deleteUser(String name, String pwd) {
    if (isBlank(name) || pwd == null) {
      return Result.error(Result.ErrorCode.BAD_REQUEST);
    }

    Result<User> result = Hibernate.execute(session -> {
      User user = session.find(User.class, name);
      if (user == null || !pwd.equals(user.getPwd())) {
        return Result.error(Result.ErrorCode.FORBIDDEN);
      }

      User copy = copyUser(user);
      session.remove(user);
      return Result.ok(copy);
    });

    if (result.isOK()) {
      notifyDeleteListeners(name);
    }
    return result;
  }

  @Override
  public synchronized Result<List<User>> searchUsers(String name, String pwd, String query) {
    if (isBlank(name) || pwd == null || query == null) {
      return Result.error(Result.ErrorCode.BAD_REQUEST);
    }

    return Hibernate.execute(session -> {
      User requester = session.find(User.class, name);
      if (requester == null || !pwd.equals(requester.getPwd())) {
        return Result.error(Result.ErrorCode.FORBIDDEN);
      }

      String q = query.toLowerCase(Locale.ROOT);
      List<User> result = new ArrayList<>();

      @SuppressWarnings("unchecked")
      List<User> users = session.createQuery("from User", User.class).list();
      for (User user : users) {
        String username = user.getName();
        if (username != null && username.toLowerCase(Locale.ROOT).contains(q)) {
          User copy = copyUser(user);
          copy.setPwd("");
          result.add(copy);
        }
      }

      return Result.ok(result);
    });
  }

  public synchronized boolean userExists(String name) {
    return !isBlank(name)
        && Hibernate.execute(session -> session.find(User.class, name) != null);
  }

  public synchronized boolean validatePassword(String name, String pwd) {
    if (isBlank(name) || pwd == null) {
      return false;
    }

    User user = Hibernate.execute(session -> session.find(User.class, name));
    return user != null && pwd.equals(user.getPwd());
  }

  public synchronized User getUserInternal(String name) {
    User user = Hibernate.execute(session -> session.find(User.class, name));
    return user == null ? null : copyUser(user);
  }

  private boolean isValidNewUser(User user) {
    return user != null
        && !isBlank(user.getName())
        && !isBlank(user.getPwd())
        && !isBlank(user.getDisplayName())
        && !isBlank(user.getDomain());
  }

  private boolean sameUser(User a, User b) {
    return safeEquals(a.getName(), b.getName())
        && safeEquals(a.getPwd(), b.getPwd())
        && safeEquals(a.getDisplayName(), b.getDisplayName())
        && safeEquals(a.getDomain(), b.getDomain());
  }

  private User copyUser(User user) {
    if (user == null) {
      return null;
    }
    return new User(
        user.getName(),
        user.getPwd(),
        user.getDisplayName(),
        user.getDomain());
  }

  private String userAddress(String name) {
    return name + "@" + domain;
  }

  private boolean isBlank(String s) {
    return s == null || s.isBlank();
  }

  private boolean safeEquals(String a, String b) {
    return a == null ? b == null : a.equals(b);
  }

  public synchronized Result<User> getUserInternalNoAuth(String name) {
    if (name == null || name.isBlank()) {
      return Result.error(Result.ErrorCode.BAD_REQUEST);
    }

    User user = Hibernate.execute(session -> session.find(User.class, name));
    if (user == null) {
      return Result.error(Result.ErrorCode.NOT_FOUND);
    }

    return Result.ok(copyUser(user));
  }

  public void onUserDeleted(Consumer<String> listener) {
    if (listener != null) {
      deleteListeners.add(listener);
    }
  }

  private void notifyDeleteListeners(String name) {
    for (Consumer<String> listener : deleteListeners) {
      try {
        listener.accept(name);
      } catch (RuntimeException e) {
        e.printStackTrace();
      }
    }
  }

}
