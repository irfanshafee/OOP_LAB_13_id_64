# Airline Reservation System Refactoring

This document outlines the six major refactoring steps implemented in the Airline Reservation System to improve code quality and maintainability.

## 1. Pull Up Method/Field
Moved common methods and fields from subclasses to the parent class to eliminate code duplication.
- Implemented in `AbstractUser` class as the parent class
- Common fields like userID, name, and email moved from `Customer`, `AdminUser`, and `StandardUser` to `AbstractUser`
- Shared methods for user management consolidated in the parent class

## 2. Rename Method
Improved method naming for better clarity and understanding of their purpose.
- Changed `flightStatus` to `getFlightStatus` for consistency with getter methods
- Renamed `displayHeaderForUsers` to `displayCustomerTableHeader` to better reflect its purpose
- Updated `randomIDDisplay` to `generateRandomUserID` for clearer functionality description

## 3. Replace Magic Literal
Replaced hard-coded values with named constants to improve code maintainability.
- Introduced constants for flight status messages ("As Per Schedule", "Cancelled")
- Defined maximum seat capacity constants
- Created constants for table formatting characters and spacing

## 4. Extract Method
Broke down complex methods into smaller, more focused methods to improve readability.
- Split `bookFlight` into `findFlightByNumber`, `findCustomerByID`, and `updateFlightBooking`
- Extracted `processTicketCancellation` from `cancelFlight`
- Created separate methods for table formatting (`printFlightTableHeader`, `printFlightTableBorder`)

## 5. Replace Conditional with Polymorphism
Replaced conditional logic with polymorphic behavior using inheritance.
- Created `User` interface with common user operations
- Implemented specific behavior in `AdminUser` and `StandardUser` classes
- Eliminated role-based conditional statements by using polymorphism

## 6. Decompose Conditional
Simplified complex conditional expressions by extracting them into well-named methods.
- Created `isFlightAlreadyAddedToCustomerList` for flight existence check
- Extracted `validateTicketCancellation` for ticket validation logic
- Implemented `isCustomerAlreadyAdded` for customer verification
