package ps.jmagna.enums;

public enum UserRole {
  ADMIN("admin"),
  DELIVERY("delivery"),
  USER("user");

  private String role;

  UserRole(String role) {
    this.role = role;
  }

  public String getValue() {
    return role;
  }
}
