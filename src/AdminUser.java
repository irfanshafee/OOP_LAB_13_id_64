public class AdminUser extends AbstractUser {
    private static final int MAX_ADMINS = 10;
    private static String[][] adminCredentials = new String[MAX_ADMINS][2];
    private static int adminCount = 0;

    static {
        // Initialize default admin
        adminCredentials[0][0] = "root";
        adminCredentials[0][1] = "root";
        adminCount = 1;
    }

    public AdminUser(String username, String password) {
        super(username, password);
    }

    @Override
    public boolean login(String username, String password) {
        for (int i = 0; i < adminCount; i++) {
            if (username.equals(adminCredentials[i][0]) && 
                password.equals(adminCredentials[i][1])) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void displayMenu() {
        System.out.printf("\n\n%-60s+++++++++ Admin Menu +++++++++%50sLogged in as \"%s\"\n", "", "", this.username);
        System.out.printf("%-30s (a) Enter 1 to add new Passenger....\n", "");
        System.out.printf("%-30s (b) Enter 2 to search a Passenger....\n", "");
        System.out.printf("%-30s (c) Enter 3 to update the Data of the Passenger....\n", "");
        System.out.printf("%-30s (d) Enter 4 to delete a Passenger....\n", "");
        System.out.printf("%-30s (e) Enter 5 to Display all Passengers....\n", "");
        System.out.printf("%-30s (f) Enter 6 to Display all flights registered by a Passenger...\n", "");
        System.out.printf("%-30s (g) Enter 7 to Display all registered Passengers in a Flight....\n", "");
        System.out.printf("%-30s (h) Enter 8 to Delete a Flight....\n", "");
        System.out.printf("%-30s (i) Enter 0 to Go back to the Main Menu/Logout....\n", "");
    }

    @Override
    public void handleMenuOption(int option) {
        Customer customer = new Customer();
        FlightReservation flightReservation = new FlightReservation();
        Flight flight = new Flight();
        Scanner scanner = new Scanner(System.in);

        switch (option) {
            case 1:
                customer.addNewCustomer();
                break;
            case 2:
                customer.displayCustomersData(false);
                System.out.print("Enter the CustomerID to Search:\t");
                String searchId = scanner.nextLine();
                customer.searchUser(searchId);
                break;
            case 3:
                customer.displayCustomersData(false);
                System.out.print("Enter the CustomerID to Update its Data:\t");
                String updateId = scanner.nextLine();
                customer.editUserInfo(updateId);
                break;
            case 4:
                customer.displayCustomersData(false);
                System.out.print("Enter the CustomerID to Delete its Data:\t");
                String deleteId = scanner.nextLine();
                customer.deleteUser(deleteId);
                break;
            case 5:
                customer.displayCustomersData(false);
                break;
            case 6:
                customer.displayCustomersData(false);
                System.out.print("\n\nEnter the ID of the user to display all flights registered by that user...");
                String userId = scanner.nextLine();
                flightReservation.displayFlightsRegisteredByOneUser(userId);
                break;
            case 7:
                System.out.print("Display all flights (Y) or specific flight (N)? ");
                char choice = scanner.nextLine().charAt(0);
                if (Character.toLowerCase(choice) == 'y') {
                    flightReservation.displayRegisteredUsersForAllFlight();
                } else {
                    flight.displayFlightSchedule();
                    System.out.print("Enter Flight Number: ");
                    String flightNum = scanner.nextLine();
                    flightReservation.displayRegisteredUsersForASpecificFlight(flightNum);
                }
                break;
            case 8:
                flight.displayFlightSchedule();
                System.out.print("Enter Flight Number to delete: ");
                String flightNum = scanner.nextLine();
                flight.deleteFlight(flightNum);
                break;
        }
    }

    @Override
    public boolean hasPermission(String operation) {
        // Admin has all permissions
        return true;
    }

    public static boolean registerAdmin(String username, String password) {
        if (adminCount >= MAX_ADMINS) {
            return false;
        }
        adminCredentials[adminCount][0] = username;
        adminCredentials[adminCount][1] = password;
        adminCount++;
        return true;
    }
}