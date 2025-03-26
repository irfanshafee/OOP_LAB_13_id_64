/*
 * FlightReservation class allows the user to book, cancel and check the status of the registered flights.
 *
 *
 * */


import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class FlightReservation implements DisplayClass {

    //        ************************************************************ Fields ************************************************************
    Flight flight = new Flight();
    int flightIndexInFlightList;

    //        ************************************************************ Behaviours/Methods ************************************************************


    /**
     * Book the numOfTickets for said flight for the specified user. Update the available seats in main system by
     * Subtracting the numOfTickets from the main system. If a new customer registers for the flight, then it adds
     * the customer to that flight, else if the user is already added to that flight, then it just updates the
     * numOfSeats of that flight.
     *
     * @param flightNo     FlightID of the flight to be booked
     * @param numOfTickets number of tickets to be booked
     * @param userID       userID of the user which is booking the flight
     */
    private Flight findFlightByNumber(String flightNo) {
        for (Flight f1 : flight.getFlightList()) {
            if (flightNo.equalsIgnoreCase(f1.getFlightNumber())) {
                return f1;
            }
        }
        return null;
    }

    private Customer findCustomerByID(String userID) {
        for (Customer customer : Customer.customerCollection) {
            if (userID.equals(customer.getUserID())) {
                return customer;
            }
        }
        return null;
    }

    private void updateFlightBooking(Flight flight, Customer customer, int numOfTickets) {
        flight.setNoOfSeatsInTheFlight(flight.getNoOfSeats() - numOfTickets);
        if (!flight.isCustomerAlreadyAdded(flight.getListOfRegisteredCustomersInAFlight(), customer)) {
            flight.addNewCustomerToFlight(customer);
        }
    }

    private void updateCustomerFlights(Customer customer, Flight flight, int numOfTickets) {
        if (isFlightAlreadyAddedToCustomerList(customer.flightsRegisteredByUser, flight)) {
            addNumberOfTicketsToAlreadyBookedFlight(customer, numOfTickets);
            if (flightIndex(this.flight.getFlightList(), this.flight) != -1) {
                customer.addExistingFlightToCustomerList(flightIndex(this.flight.getFlightList(), this.flight), numOfTickets);
            }
        } else {
            customer.addNewFlightToCustomerList(flight);
            addNumberOfTicketsForNewFlight(customer, numOfTickets);
        }
    }

    void bookFlight(String flightNo, int numOfTickets, String userID) {
        Flight flightToBook = findFlightByNumber(flightNo);
        if (flightToBook == null) {
            System.out.println("Invalid Flight Number...! No flight with the  ID \"" + flightNo + "\" was found...");
            return;
        }
    
        Customer customer = findCustomerByID(userID);
        if (customer == null) {
            System.out.println("Invalid User ID...! No customer with the ID \"" + userID + "\" was found...");
            return;
        }
    
        updateFlightBooking(flightToBook, customer, numOfTickets);
        updateCustomerFlights(customer, flightToBook, numOfTickets);
        System.out.printf("\n %50s You've booked %d tickets for Flight \"%5s\"...", "", numOfTickets, flightNo.toUpperCase());
    }

    private void displayCustomerFlights(Customer customer) {
        if (customer.getFlightsRegisteredByUser().size() != 0) {
            System.out.printf("%50s %s Here is the list of all the Flights registered by you %s", " ", "+++++++++++++", "+++++++++++++");
            displayFlightsRegisteredByOneUser(customer.getUserID());
        } else {
            System.out.println("No flights registered for this user.");
        }
    }

    private int validateTicketCancellation(int requestedTickets, int bookedTickets) {
        Scanner read = new Scanner(System.in);
        while (requestedTickets > bookedTickets) {
            System.out.print("ERROR!!! Number of tickets cannot be greater than " + bookedTickets + " for this flight. Please enter the number of tickets again : ");
            requestedTickets = read.nextInt();
        }
        return requestedTickets;
    }

    private void processTicketCancellation(Customer customer, Flight flight, int index, int numOfTickets) {
        int numOfTicketsForFlight = customer.getNumOfTicketsBookedByUser().get(index);
        numOfTickets = validateTicketCancellation(numOfTickets, numOfTicketsForFlight);
    
        int ticketsToBeReturned;
        if (numOfTicketsForFlight == numOfTickets) {
            ticketsToBeReturned = flight.getNoOfSeats() + numOfTicketsForFlight;
            customer.numOfTicketsBookedByUser.remove(index);
            customer.getFlightsRegisteredByUser().remove(flight);
        } else {
            ticketsToBeReturned = numOfTickets + flight.getNoOfSeats();
            customer.numOfTicketsBookedByUser.set(index, (numOfTicketsForFlight - numOfTickets));
        }
        flight.setNoOfSeatsInTheFlight(ticketsToBeReturned);
    }

    void cancelFlight(String userID) {
        Customer customer = findCustomerByID(userID);
        if (customer == null) {
            System.out.println("Invalid User ID...! No customer found.");
            return;
        }
    
        displayCustomerFlights(customer);
        if (customer.getFlightsRegisteredByUser().size() == 0) {
            return;
        }
    
        Scanner read = new Scanner(System.in);
        System.out.print("Enter the Flight Number of the Flight you want to cancel : ");
        String flightNum = read.nextLine();
        System.out.print("Enter the number of tickets to cancel : ");
        int numOfTickets = read.nextInt();
    
        List<Flight> customerFlights = customer.getFlightsRegisteredByUser();
        for (int i = 0; i < customerFlights.size(); i++) {
            Flight flight = customerFlights.get(i);
            if (flightNum.equalsIgnoreCase(flight.getFlightNumber())) {
                processTicketCancellation(customer, flight, i, numOfTickets);
                return;
            }
        }
    
        System.out.println("ERROR!!! Couldn't find Flight with ID \"" + flightNum.toUpperCase() + "\".....");
    }

    void addNumberOfTicketsToAlreadyBookedFlight(Customer customer, int numOfTickets) {
        int newNumOfTickets = customer.numOfTicketsBookedByUser.get(flightIndexInFlightList) + numOfTickets;
        customer.numOfTicketsBookedByUser.set(flightIndexInFlightList, newNumOfTickets);
    }

    void addNumberOfTicketsForNewFlight(Customer customer, int numOfTickets) {
        customer.numOfTicketsBookedByUser.add(numOfTickets);
    }

    boolean isFlightAlreadyAddedToCustomerList(List<Flight> flightList, Flight flight) {
        boolean addedOrNot = false;
        for (Flight flight1 : flightList) {
            if (flight1.getFlightNumber().equalsIgnoreCase(flight.getFlightNumber())) {
                this.flightIndexInFlightList = flightList.indexOf(flight1);
                addedOrNot = true;
                break;
            }
        }
        return addedOrNot;
    }

    String flightStatus(Flight flight) {
        boolean isFlightAvailable = false;
        for (Flight list : flight.getFlightList()) {
            if (list.getFlightNumber().equalsIgnoreCase(flight.getFlightNumber())) {
                isFlightAvailable = true;
                break;
            }
        }
        if (isFlightAvailable) {
            return "As Per Schedule";
        } else {
            return "   Cancelled   ";
        }
    }

    private void printFlightTableHeader() {
        System.out.println();
        System.out.print("+------+-------------------------------------------+-----------+------------------+-----------------------+------------------------+---------------------------+-------------+--------+-----------------+\n");
        System.out.printf("| Num  | FLIGHT SCHEDULE\t\t\t   | FLIGHT NO |  Booked Tickets  | \tFROM ====>>       | \t====>> TO\t   | \t    ARRIVAL TIME       | FLIGHT TIME |  GATE  |  FLIGHT STATUS  |%n");
        System.out.print("+------+-------------------------------------------+-----------+------------------+-----------------------+------------------------+---------------------------+-------------+--------+-----------------+\n");
    }

    private void printFlightTableBorder() {
        System.out.print("+------+-------------------------------------------+-----------+------------------+-----------------------+------------------------+---------------------------+-------------+--------+-----------------+\n");
    }

    private String formatFlightInfo(int serialNum, Flight flights, Customer customer) {
        return String.format("| %-5d| %-41s | %-9s | \t%-9d | %-21s | %-22s | %-10s  |   %-6sHrs |  %-4s  | %-10s |", 
            serialNum, 
            flights.getFlightSchedule(), 
            flights.getFlightNumber(), 
            customer.numOfTicketsBookedByUser.get(serialNum - 1), 
            flights.getFromWhichCity(), 
            flights.getToWhichCity(), 
            flights.fetchArrivalTime(), 
            flights.getFlightTime(), 
            flights.getGate(), 
            flightStatus(flights));
    }

    @Override
    public void displayFlightsRegisteredByOneUser(String userID) {
        printFlightTableHeader();
        for (Customer customer : Customer.customerCollection) {
            List<Flight> f = customer.getFlightsRegisteredByUser();
            int size = customer.getFlightsRegisteredByUser().size();
            if (userID.equals(customer.getUserID())) {
                for (int i = 0; i < size; i++) {
                    System.out.println(formatFlightInfo((i + 1), f.get(i), customer));
                    printFlightTableBorder();
                }
            }
        }
    }

    private void printCustomerTableHeader(Flight flight) {
        System.out.printf("\n%65s Displaying Registered Customers for Flight No. \"%-6s\" %s \n\n", "+++++++++++++", flight.getFlightNumber(), "+++++++++++++");
        System.out.printf("%10s+------------+------------+----------------------------------+---------+-----------------------------+-------------------------------------+-------------------------+----------------+\n", "");
        System.out.printf("%10s| SerialNum  |   UserID   | Passenger Names                  | Age     | EmailID\t\t       | Home Address\t\t\t     | Phone Number\t       | Booked Tickets |%n", "");
        System.out.printf("%10s+------------+------------+----------------------------------+---------+-----------------------------+-------------------------------------+-------------------------+----------------+\n", "");
    }

    private void printCustomerTableBorder() {
        System.out.printf("%10s+------------+------------+----------------------------------+---------+-----------------------------+-------------------------------------+-------------------------+----------------+\n", "");
    }

    private String formatCustomerInfo(int serialNum, Customer customer, int index) {
        return String.format("%10s| %-10d | %-10s | %-32s | %-7s | %-27s | %-35s | %-23s |       %-7s  |", 
            "", 
            (serialNum + 1), 
            customer.randomIDDisplay(customer.getUserID()), 
            customer.getName(),
            customer.getAge(), 
            customer.getEmail(), 
            customer.getAddress(), 
            customer.getPhone(), 
            customer.numOfTicketsBookedByUser.get(index));
    }

    @Override
    public void displayHeaderForUsers(Flight flight, List<Customer> c) {
        printCustomerTableHeader(flight);
        int size = flight.getListOfRegisteredCustomersInAFlight().size();
        for (int i = 0; i < size; i++) {
            System.out.println(formatCustomerInfo(i, c.get(i), flightIndex(c.get(i).flightsRegisteredByUser, flight)));
            printCustomerTableBorder();
        }
    }

    @Override
    public void displayRegisteredUsersForAllFlight() {
        System.out.println();
        for (Flight flight : flight.getFlightList()) {
            List<Customer> c = flight.getListOfRegisteredCustomersInAFlight();
            int size = flight.getListOfRegisteredCustomersInAFlight().size();
            if (size != 0) {
                displayHeaderForUsers(flight, c);
            }
        }
    }

    int flightIndex(List<Flight> flightList, Flight flight) {
        int i = -1;
        for (Flight flight1 : flightList) {
            if (flight1.equals(flight)) {
                i = flightList.indexOf(flight1);
            }
        }
        return i;
    }

    @Override
    public void displayRegisteredUsersForASpecificFlight(String flightNum) {
        System.out.println();
        for (Flight flight : flight.getFlightList()) {
            List<Customer> c = flight.getListOfRegisteredCustomersInAFlight();
            if (flight.getFlightNumber().equalsIgnoreCase(flightNum)) {
                displayHeaderForUsers(flight, c);
            }
        }
    }


}
