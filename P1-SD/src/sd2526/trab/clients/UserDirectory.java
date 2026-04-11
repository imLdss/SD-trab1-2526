package sd2526.trab.clients;

import sd2526.trab.api.User;
import sd2526.trab.api.java.Result;

public interface UserDirectory {
  Result<User> getUser(String name, String pwd);

  Result<User> getUserNoAuth(String name);
}
