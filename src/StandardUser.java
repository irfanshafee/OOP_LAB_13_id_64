public class StandardUser extends AbstractUser {
    public StandardUser(String username, String password) {
        super(username, password);
    }

    @Override
    public boolean login(String username, String password) {
        return username.equals("root") && password.equals("root");
    }

    @Override
    public void displayMenu() {
        System.out.printf("\n\n%-60s+++++++++ Standard User Menu +++++++++%50sLogged in as \"%s\"\n", "", "", this.username);
        System.out.printf("%-30s (a) Enter 1 to Display all Passengers....\n", "");
        System.out.printf("%-30s (b) Enter 0 to Go back to the Main Menu/Logout....\n", "");
    }

    @Override
    public void handleMenuOption(int option) {
        Customer customer = new Customer();
        switch (option) {
            case 1:
                customer.displayCustomersData(true);
                break;
        }
    }

    @Override
    public boolean hasPermission(String operation) {
        // Standard user can only view data
        return operation.equals("view");
    }
}