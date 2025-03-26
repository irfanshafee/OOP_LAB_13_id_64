public abstract class AbstractUser {
    protected String username;
    protected String password;

    public AbstractUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Abstract methods that will be implemented by concrete user classes
    public abstract boolean login(String username, String password);
    public abstract void displayMenu();
    public abstract void handleMenuOption(int option);
    public abstract boolean hasPermission(String operation);
}