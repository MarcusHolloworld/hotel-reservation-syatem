package www.hotelreservation.in;

import java.sql.*;
import java.util.Scanner;

public class HotelReservationSystem {

	private static final String url = "jdbc:mysql://localhost:3306/hotel_db";

	private  static final String userName = "root";

	private static final String password = "rohit";

	public static void main(String[] args) throws ClassNotFoundException , SQLException{

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		try{
			Connection con = DriverManager.getConnection(url,userName,password);
			while (true) {
				System.out.println();
				System.out.println("HOTEL RESERVATION SYSTEM");
				Scanner scanner = new Scanner(System.in);
				System.out.println("1. Reserve a room");
				System.out.println("2. View reservation");
				System.out.println("3. Get room number");
				System.out.println("4. Update reservation");
				System.out.println("5. Delete reservation");
				System.out.println("0. Exit");
				System.out.print("Choose as option: ");
				int choice = scanner.nextInt();
				switch (choice) {
					case 1:
						reserveRoom(con,scanner);
						break;
					case 2:
						viewReservations(con);
						break;
					case 3:
						getRoomNumber(con,scanner);
						break;
					case 4:
						updateReservation(con,scanner);
						break;
					case 5:
						deleteReservation(con,scanner);
						break;
					case 0:
						exit();
						scanner.close();
						return;
					default:
						System.out.println("Invalid Choice. Try again");
				}
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	private static void reserveRoom(Connection con , Scanner scanner) {
		try {
			System.out.print("Enter guest name: ");
			String guestName = scanner.next();
			scanner.nextLine();
			System.out.print("Enter room number: ");
			int roomNumber = scanner.nextInt();
			System.out.print("Enter contact number: ");
			String contactNumber = scanner.next();

			String query = "insert into reservations (guest_name, room_number, contact_number) values(?,?,?)";

			PreparedStatement pstmt = con.prepareStatement(query);

			pstmt.setString(1,guestName);
			pstmt.setInt(2,roomNumber);
			pstmt.setString(3,contactNumber);

			pstmt.executeUpdate();

			System.out.println("Reservation successful..");

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static void viewReservations(Connection con) throws SQLException {
		String query = "select * from reservations;";

		try (Statement statement = con.createStatement();
		     ResultSet resultset = statement.executeQuery(query)) {

			System.out.println("Current Reservations:");
			System.out.println("+----------------+-----------------+---------------+----------------------+-------------------------");
			System.out.println("| Reservation ID | Guest           | Room Number   | Contact number       | Reservation Date        ");
			System.out.println("+----------------+-----------------+---------------+----------------------+-------------------------");


			while (resultset.next()) {
				int reservationId = resultset.getInt(1);
				String guestName = resultset.getString(2);
				int roomNumber = resultset.getInt(3);
				String contactNumber = resultset.getString(4);
				String reservationDate = resultset.getTimestamp(5).toString();

				System.out.printf(" %-14d | %-15s | %-13d | %-20s | %-19s  |\n",reservationId,guestName,roomNumber,contactNumber,reservationDate);

			}

			System.out.println("+----------------+-----------------+---------------+----------------------+-------------------------");

		}
	}

	private static void getRoomNumber(Connection con , Scanner scanner) {
		try {
			System.out.println("Enter reservation ID: ");
			int reservationId = scanner.nextInt();
			System.out.println("Enter guest name: ");
			String guestName = scanner.next();

			String query = "select room_number from reservations where reservation_id = " + reservationId + "and guest_name = '" + guestName + "';";

			try (Statement statement = con.createStatement();
			     ResultSet resultSet = statement.executeQuery(query)) {

				if (resultSet.next()) {
					int roomNumber = resultSet.getInt("room_number");
					System.out.println("Room number for Reservation id " + reservationId + "and Guest " + guestName + " is: " + roomNumber);
				} else {
					System.out.println("Reservation not found for the given id and guest name.");
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static void updateReservation(Connection con, Scanner scanner) {

	}

	private static void deleteReservation(Connection con, Scanner scanner) {

	}

	private static void exit(){

	}
}
