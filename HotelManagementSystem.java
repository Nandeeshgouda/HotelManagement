import java.io.*;
import java.util.Scanner;

class Room {
    private int roomNumber;
    private String roomType;
    private boolean isAvailable;

    public Room(int roomNumber, String roomType) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.isAvailable = true;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public String getRoomType() {
        return roomType;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public void bookRoom() {
        if (isAvailable) {
            isAvailable = false;
            System.out.println("Room " + roomNumber + " booked successfully.");
            saveToFile("Booked");
        } else {
            System.out.println("Room " + roomNumber + " is already booked.");
        }
    }

    public void cancelBooking() {
        if (!isAvailable) {
            isAvailable = true;
            System.out.println("Room " + roomNumber + " booking canceled.");
            saveToFile("Canceled");
        } else {
            System.out.println("Room " + roomNumber + " is not booked.");
        }
    }

    private void saveToFile(String action) {
        try (FileWriter fw = new FileWriter("bookingData.txt", true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println("Room Number: " + roomNumber + ", Action: " + action + ", Type: " + roomType);
        } catch (IOException e) {
            System.out.println("An error occurred while saving the data.");
        }
    }
}

class Hotel {
    private String name;
    private Room[] rooms;

    public Hotel(String name, int numberOfRooms) {
        this.name = name;
        rooms = new Room[numberOfRooms];
        initializeRooms();
        loadDataFromFile();
    }

    private void initializeRooms() {
        for (int i = 0; i < rooms.length; i++) {
            rooms[i] = new Room(i + 1, "Standard");
        }
    }

    private void loadDataFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader("bookingData.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(", ");
                int roomNumber = Integer.parseInt(parts[0].split(": ")[1]);
                String action = parts[1].split(": ")[1];
                if (action.equals("Booked")) {
                    rooms[roomNumber - 1].setAvailable(false);
                } else if (action.equals("Canceled")) {
                    rooms[roomNumber - 1].setAvailable(true);
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while loading the data.");
        }
    }

    public void showAvailableRooms() {
        System.out.println("Available Rooms in " + name + ":");
        for (Room room : rooms) {
            if (room.isAvailable()) {
                System.out.println("Room Number: " + room.getRoomNumber() + ", Type: " + room.getRoomType());
            }
        }
    }

    public void bookRoom(int roomNumber) {
        if (roomNumber > 0 && roomNumber <= rooms.length) {
            rooms[roomNumber - 1].bookRoom();
        } else {
            System.out.println("Invalid room number.");
        }
    }

    public void cancelBooking(int roomNumber) {
        if (roomNumber > 0 && roomNumber <= rooms.length) {
            rooms[roomNumber - 1].cancelBooking();
        } else {
            System.out.println("Invalid room number.");
        }
    }

    public String getName() {
        return name;
    }
}

public class HotelManagementSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Hotel hotel = new Hotel("Sunrise Inn", 10);
        boolean exit = false;

        while (!exit) {
            System.out.println("\nWelcome to " + hotel.getName());
            System.out.println("1. Show Available Rooms");
            System.out.println("2. Book a Room");
            System.out.println("3. Cancel Booking");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    hotel.showAvailableRooms();
                    break;
                case 2:
                    System.out.print("Enter room number to book: ");
                    int roomNumberToBook = scanner.nextInt();
                    hotel.bookRoom(roomNumberToBook);
                    break;
                case 3:
                    System.out.print("Enter room number to cancel booking: ");
                    int roomNumberToCancel = scanner.nextInt();
                    hotel.cancelBooking(roomNumberToCancel);
                    break;
                case 4:
                    exit = true;
                    System.out.println("Thank you for using the Hotel Management System. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        scanner.close();
    }
}
