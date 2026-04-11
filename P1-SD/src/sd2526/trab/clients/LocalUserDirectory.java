package sd2526.trab.clients;

import sd2526.trab.api.User;
import sd2526.trab.api.java.Result;
import sd2526.trab.server.UsersService;

public class LocalUserDirectory implements UserDirectory {

  private final UsersService usersService;

  public LocalUserDirectory(UsersService usersService) {
    this.usersService = usersService;
  }

  @Override
  public Result<User> getUser(String name, String pwd) {
    return usersService.getUser(name, pwd);
  }

  @Override
  public Result<User> getUserNoAuth(String name) {
    return usersService.getUserInternalNoAuth(name);
  }
}
