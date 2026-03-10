import java.io.*;
import java.util.*;

public class HotelRestaurantManagementSystem {

    static final String CUSTOMER_FILE = "Customer_Records.txt";
    static final String RESERVATION_FILE = "Reservation_Records.txt";
    static final String BILL_FILE = "bills.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean exitSystem = false;

        while (!exitSystem) {
            System.out.println("\n=== HOTEL AND RESTAURANT MANAGEMENT SYSTEM ===");
            System.out.println("A - Create Customer Record");
            System.out.println("B - Create Reservation Record");
            System.out.println("C - Bill Out Module");
            System.out.println("X - Exit System");
            System.out.print("Select an option: ");

            String choice = scanner.nextLine().toUpperCase();

            switch (choice) {
                case "A":
                    customerRecordModule();
                    break;
                case "B":
                    reservationRecordModule();
                    break;
                case "C":
                    billOutModule();
                    break;
                case "X":
                    System.out.println("Exiting the system. Goodbye!");
                    exitSystem = true;
                    break;
                default:
                    System.out.println("Invalid selection. Please try again.");
            }
        }
    }

    // CUSTOMER RECORD MODULE
    static void customerRecordModule() {
        Scanner scanner = new Scanner(System.in);
        boolean returnToMain = false;

        while (!returnToMain) {
            System.out.println("\n=== CUSTOMER RECORD MODULE ===");
            System.out.println("A - Create Customer Record");
            System.out.println("B - Update Customer Record");
            System.out.println("C - View Customer Record");
            System.out.println("X - Return to Main Menu");
            System.out.print("Select an option: ");

            String choice = scanner.nextLine().toUpperCase();

            switch (choice) {
                case "A":
                    createCustomerRecord();
                    break;
                case "B":
                    updateCustomerRecord();
                    break;
                case "C":
                    viewCustomerRecord();
                    break;
                case "X":
                    returnToMain = true;
                    break;
                default:
                    System.out.println("Invalid selection. Please try again.");
            }
        }
    }

    static void createCustomerRecord() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Customer Name: ");
        String customerName = scanner.nextLine();
        System.out.print("Enter Contact Number: ");
        String contactNo = scanner.nextLine();
        System.out.print("Enter Date of Reservation (YYYY-MM-DD): ");
        String dateOfReservation = scanner.nextLine();
        System.out.print("Enter Time of Reservation (HH:MM): ");
        String timeOfReservation = scanner.nextLine();
        System.out.print("Enter Number of Guests: ");
        int noOfGuests = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CUSTOMER_FILE, true))) {
            writer.write(customerName + "," + contactNo + "," + dateOfReservation + "," + timeOfReservation + "," + noOfGuests);
            writer.newLine();
            System.out.println("Customer record saved successfully!");
        } catch (IOException e) {
            System.out.println("Error saving customer record: " + e.getMessage());
        }
    }

    static void updateCustomerRecord() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the name of the customer to update: ");
        String searchName = scanner.nextLine();

        File inputFile = new File(CUSTOMER_FILE);
        File tempFile = new File("Temp_Customer_Records.txt");

        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                if (details[0].equalsIgnoreCase(searchName)) {
                    found = true;
                    System.out.println("Record Found: " + line);

                    System.out.print("Enter New Contact Number: ");
                    details[1] = scanner.nextLine();
                    System.out.print("Enter New Date of Reservation (YYYY-MM-DD): ");
                    details[2] = scanner.nextLine();
                    System.out.print("Enter New Time of Reservation (HH:MM): ");
                    details[3] = scanner.nextLine();
                    System.out.print("Enter New Number of Guests: ");
                    details[4] = scanner.nextLine();

                    writer.write(String.join(",", details));
                    writer.newLine();
                } else {
                    writer.write(line);
                    writer.newLine();
                }
            }

            if (!found) {
                System.out.println("Customer not found.");
            } else {
                System.out.println("Customer record updated successfully.");
            }
        } catch (IOException e) {
            System.out.println("Error updating record: " + e.getMessage());
        }

        inputFile.delete();
        tempFile.renameTo(inputFile);
    }

    static void viewCustomerRecord() {
        try (BufferedReader reader = new BufferedReader(new FileReader(CUSTOMER_FILE))) {
            String line;
            System.out.println("\n=== CUSTOMER RECORDS ===");
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading customer records: " + e.getMessage());
        }
    }

    // RESERVATION RECORD MODULE
    static void reservationRecordModule() {
        Scanner scanner = new Scanner(System.in);
        boolean returnToMain = false;

        while (!returnToMain) {
            System.out.println("\n=== RESERVATION RECORD MODULE ===");
            System.out.println("A - Create Orders Record");
            System.out.println("B - View Orders Record");
            System.out.println("X - Return to Main Menu");
            System.out.print("Select an option: ");

            String choice = scanner.nextLine().toUpperCase();

            switch (choice) {
                case "A":
                    createOrdersRecord();
                    break;
                case "B":
                    viewOrdersRecord();
                    break;
                case "X":
                    returnToMain = true;
                    break;
                default:
                    System.out.println("Invalid selection. Please try again.");
            }
        }
    }

   static void createOrdersRecord() {
        Map<String, List<String>> menuCategories = new HashMap<>();
        Scanner scanner = new Scanner(System.in);

        // Define menu categories and items
        menuCategories.put("Meals", Arrays.asList("1. Chicken Curry - $7.99", "2. Grilled Fish - $9.99",
                "3. Beef Steak - $12.99", "4. Veggie Pasta - $6.49", "5. BBQ Ribs - $10.99"));
        menuCategories.put("Drinks", Arrays.asList("1. Lemonade - $1.99", "2. Coffee - $2.49",
                "3. Iced Tea - $1.79", "4. Smoothie - $3.99", "5. Soda - $1.49"));
        menuCategories.put("Desserts", Arrays.asList("1. Chocolate Cake - $3.99", "2. Ice Cream - $2.99",
                "3. Apple Pie - $4.49", "4. Cheesecake - $5.49", "5. Brownie - $3.49"));
        menuCategories.put("Side Dishes", Arrays.asList("1. Fries - $2.99", "2. Garlic Bread - $3.49",
                "3. Onion Rings - $2.79", "4. Salad - $3.99", "5. Mashed Potatoes - $3.29"));
        menuCategories.put("Bundled Menu", Arrays.asList("1. Burger + Fries + Soda - $10.99",
                "2. Pizza + Salad + Soda - $12.99", "3. Steak + Dessert + Drink - $15.99",
                "4. Veggie Pasta + Garlic Bread + Drink - $11.49", "5. BBQ Ribs + Fries + Drink - $14.99"));

        double totalOrderAmount = 0;
        List<String> orderDetails = new ArrayList<>();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RESERVATION_FILE, true))) {
            System.out.print("Enter Reservation ID: ");
            String reservationID = scanner.nextLine();

            while (true) {
                // Display main order menu
                System.out.println("\n=== Create Orders ===");
                System.out.println("A - Meals Menu");
                System.out.println("B - Drinks Menu");
                System.out.println("C - Dessert Menu");
                System.out.println("D - Side Dishes Menu");
                System.out.println("E - Bundled Menu");
                System.out.println("X - Return to Main Menu");

                System.out.print("\nSelect a menu category: ");
                String menuChoice = scanner.nextLine().toUpperCase();

                if (menuChoice.equals("X")) {
                    System.out.println("Returning to main menu...");
                    break;
                }

                String category = "";
                switch (menuChoice) {
                    case "A":
                        category = "Meals";
                        break;
                    case "B":
                        category = "Drinks";
                        break;
                    case "C":
                        category = "Desserts";
                        break;
                    case "D":
                        category = "Side Dishes";
                        break;
                    case "E":
                        category = "Bundled Menu";
                        break;
                    default:
                        System.out.println("Invalid choice. Please select a valid category.");
                        continue;
                }

                // Display selected menu
                System.out.println("\n=== " + category + " ===");
                List<String> items = menuCategories.get(category);
                for (String item : items) {
                    System.out.println(item);
                }

                System.out.print("\nEnter the item number to order (or type 'back' to return to categories): ");
                String itemChoice = scanner.nextLine();

                if (itemChoice.equalsIgnoreCase("back")) {
                    continue;
                }

                int itemIndex;
                try {
                    itemIndex = Integer.parseInt(itemChoice) - 1;
                    if (itemIndex < 0 || itemIndex >= items.size()) {
                        System.out.println("Invalid item number. Please try again.");
                        continue;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid number.");
                    continue;
                }

                // Get item details
                String selectedItem = items.get(itemIndex);
                String[] itemDetails = selectedItem.split(" - ");
                String itemName = itemDetails[0];
                double itemPrice = Double.parseDouble(itemDetails[1].replace("$", ""));

                // Ask for quantity
                int quantity;
                System.out.print("Enter quantity for " + itemName + ": ");
                try {
                    quantity = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                } catch (InputMismatchException e) {
                    System.out.println("Invalid quantity. Please try again.");
                    scanner.nextLine(); // Clear invalid input
                    continue;
                }

                if (quantity <= 0) {
                    System.out.println("Quantity must be greater than zero. Please try again.");
                    continue;
                }

                // Add to order summary and file
                double itemTotal = itemPrice * quantity;
                totalOrderAmount += itemTotal;
                orderDetails.add(String.format("%s, Quantity: %d, Total: $%.2f", itemName, quantity, itemTotal));

                String record = reservationID + "," + itemName + "," + quantity + "," + itemPrice;
                writer.write(record);
                writer.newLine();

                System.out.println("Item added to order successfully!");
            }

            // Display final order summary
            System.out.println("\n=== Final Order Summary ===");
            for (String detail : orderDetails) {
                System.out.println(detail);
            }
            System.out.printf("Total Amount: $%.2f%n", totalOrderAmount);
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

static void viewOrdersRecord() {
        try (BufferedReader reader = new BufferedReader(new FileReader(RESERVATION_FILE))) {
            String line;
            System.out.println("\n=== RESERVATION RECORDS ===");
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading reservation records: " + e.getMessage());
        }
    }

static void billOutModule() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n=== Bill Out Module ===");
        System.out.print("Enter Reservation ID: ");
        String reservationID = scanner.nextLine();

        double totalAmount = 0;
        boolean reservationFound = false;
        List<String> orderDetails = new ArrayList<>();
        String customerInfo = "";

        // Fetch customer information
        try (BufferedReader customerReader = new BufferedReader(new FileReader(CUSTOMER_FILE))) {
            String line;
            while ((line = customerReader.readLine()) != null) {
                String[] details = line.split(",");
                if (details[0].equalsIgnoreCase(reservationID)) {
                    customerInfo = String.format("Customer Name: %s\nContact Number: %s\nDate of Reservation: %s\nTime of Reservation: %s\nNumber of Guests: %s",
                            details[1], details[2], details[3], details[4], details[5]);
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading customer records: " + e.getMessage());
            return;
        }

        // Fetch reservation orders
        try (BufferedReader reservationReader = new BufferedReader(new FileReader(RESERVATION_FILE))) {
            String line;
            while ((line = reservationReader.readLine()) != null) {
                String[] details = line.split(",");
                if (details[0].equalsIgnoreCase(reservationID)) {
                    reservationFound = true;
                    String itemName = details[1];
                    int quantity = Integer.parseInt(details[2]);
                    double price = Double.parseDouble(details[3]);
                    double itemTotal = quantity * price;

                    totalAmount += itemTotal;
                    orderDetails.add(String.format("Item: %s, Quantity: %d, Price: $%.2f, Total: $%.2f",
                            itemName, quantity, price, itemTotal));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading reservation records: " + e.getMessage());
            return;
        }

        if (!reservationFound) {
            System.out.println("No reservation found for the provided Reservation ID.");
            return;
        }

        // Display bill details
        System.out.println("\n=== Customer Information ===");
        System.out.println(customerInfo);

        System.out.println("\n=== Order Details ===");
        for (String order : orderDetails) {
            System.out.println(order);
        }
        System.out.printf("Total Amount Due: $%.2f%n", totalAmount);

        // Accept payment
        double paymentAmount = 0;
        while (true) {
            System.out.print("\nEnter Payment Amount: $");
            try {
                paymentAmount = scanner.nextDouble();
                scanner.nextLine(); // Consume newline

                if (paymentAmount < totalAmount) {
                    System.out.println("Insufficient payment. Please enter an amount equal to or greater than the total due.");
                } else {
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid numeric value.");
                scanner.nextLine(); // Clear invalid input
            }
        }

        // Compute change
        double change = paymentAmount - totalAmount;
        System.out.printf("Payment Accepted: $%.2f%n", paymentAmount);
        System.out.printf("Change: $%.2f%n", change);

        // Save bill to file
        try (BufferedWriter billWriter = new BufferedWriter(new FileWriter("bills.txt", true))) {
            billWriter.write("=== Bill for Reservation ID: " + reservationID + " ===");
            billWriter.newLine();
            billWriter.write(customerInfo);
            billWriter.newLine();
            for (String order : orderDetails) {
                billWriter.write(order);
                billWriter.newLine();
            }
            billWriter.write(String.format("Total Amount Due: $%.2f", totalAmount));
            billWriter.newLine();
            billWriter.write(String.format("Payment: $%.2f", paymentAmount));
            billWriter.newLine();
            billWriter.write(String.format("Change: $%.2f", change));
            billWriter.newLine();
            billWriter.newLine();
            System.out.println("Bill saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving bill: " + e.getMessage());
        }
    }
}



