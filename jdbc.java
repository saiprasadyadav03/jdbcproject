
package org.example;

import java.sql.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        String dbUrl = "jdbc:mysql://localhost:3306/jdbc";
        String dbUser = "root";
        String dbPassword = "root";

        try (
                Connection connection = DriverManager.getConnection(
                        dbUrl, dbUser, dbPassword);
                Scanner scanner = new Scanner(System.in)
        ) {
            System.out.println("MySQL connection established!");

            while (true) {
                System.out.println("\n===== STUDENT RECORD SYSTEM =====");
                System.out.println("1. Register New Student");
                System.out.println("2. Show All Students");
                System.out.println("3. Search Student by ID");
                System.out.println("4. Modify Student Details");
                System.out.println("5. Remove Student Record");
                System.out.println("6. Exit");

                System.out.print("Select an option: ");

                int option;
                try {
                    option = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid number.");
                    continue;
                }

                switch (option) {

                    // ADD STUDENT
                    case 1:
                        System.out.print("Student name: ");
                        String studentName = scanner.nextLine();

                        System.out.print("Student age: ");
                        int studentAge =
                                Integer.parseInt(scanner.nextLine());

                        System.out.print("Student gender: ");
                        String studentGender = scanner.nextLine();

                        System.out.print("Enter a 12-digit test value: ");
                        String aadharNumber = scanner.nextLine();

                        if (!aadharNumber.matches("\\d{12}")) {
                            System.out.println(
                                    "The value must contain exactly 12 digits.");
                            break;
                        }

                        String insertQuery =
                                "INSERT INTO student (name, age, gender, aadhar) "
                                + "VALUES (?, ?, ?, ?)";

                        try (PreparedStatement ps =
                                     connection.prepareStatement(insertQuery)) {

                            ps.setString(1, studentName);
                            ps.setInt(2, studentAge);
                            ps.setString(3, studentGender);
                            ps.setString(4, aadharNumber);

                            ps.executeUpdate();
                            System.out.println("New student registered!");
                        }
                        break;

                    // DISPLAY ALL STUDENTS
                    case 2:
                        String selectQuery =
                                "SELECT id, name, age, gender FROM student";

                        try (Statement statement =
                                     connection.createStatement();
                             ResultSet records =
                                     statement.executeQuery(selectQuery)) {

                            boolean found = false;

                            while (records.next()) {
                                found = true;

                                System.out.println(
                                        "ID: " + records.getInt("id")
                                        + " | Name: " + records.getString("name")
                                        + " | Age: " + records.getInt("age")
                                        + " | Gender: " + records.getString("gender"));
                            }

                            if (!found) {
                                System.out.println("No student records found.");
                            }
                        }
                        break;

                    // SEARCH STUDENT
                    case 3:
                        System.out.print("Enter the student ID: ");
                        int studentId =
                                Integer.parseInt(scanner.nextLine());

                        String searchQuery =
                                "SELECT id, name, age, gender "
                                + "FROM student WHERE id = ?";

                        try (PreparedStatement ps =
                                     connection.prepareStatement(searchQuery)) {

                            ps.setInt(1, studentId);

                            try (ResultSet record = ps.executeQuery()) {
                                if (record.next()) {
                                    System.out.println("Student details:");
                                    System.out.println("ID: " + record.getInt("id"));
                                    System.out.println("Name: " + record.getString("name"));
                                    System.out.println("Age: " + record.getInt("age"));
                                    System.out.println("Gender: " + record.getString("gender"));
                                } else {
                                    System.out.println("No student exists with that ID.");
                                }
                            }
                        }
                        break;

                    // MODIFY STUDENT
                    case 4:
                        System.out.print("Student ID to modify: ");
                        int editId = Integer.parseInt(scanner.nextLine());

                        System.out.print("Updated name: ");
                        String updatedName = scanner.nextLine();

                        System.out.print("Updated age: ");
                        int updatedAge = Integer.parseInt(scanner.nextLine());

                        System.out.print("Updated gender: ");
                        String updatedGender = scanner.nextLine();

                        System.out.print("Updated 12-digit test value: ");
                        String updatedAadhar = scanner.nextLine();

                        if (!updatedAadhar.matches("\\d{12}")) {
                            System.out.println(
                                    "The value must contain exactly 12 digits.");
                            break;
                        }

                        String updateQuery =
                                "UPDATE student SET name = ?, age = ?, "
                                + "gender = ?, aadhar = ? WHERE id = ?";

                        try (PreparedStatement ps =
                                     connection.prepareStatement(updateQuery)) {

                            ps.setString(1, updatedName);
                            ps.setInt(2, updatedAge);
                            ps.setString(3, updatedGender);
                            ps.setString(4, updatedAadhar);
                            ps.setInt(5, editId);

                            int changed = ps.executeUpdate();

                            if (changed > 0) {
                                System.out.println("Student details modified!");
                            } else {
                                System.out.println("Student ID was not found.");
                            }
                        }
                        break;

                    // REMOVE STUDENT
                    case 5:
                        System.out.print("Student ID to remove: ");
                        int removeId = Integer.parseInt(scanner.nextLine());

                        String deleteQuery =
                                "DELETE FROM student WHERE id = ?";

                        try (PreparedStatement ps =
                                     connection.prepareStatement(deleteQuery)) {

                            ps.setInt(1, removeId);

                            int deleted = ps.executeUpdate();

                            if (deleted > 0) {
                                System.out.println("Student record removed!");
                            } else {
                                System.out.println("Student ID was not found.");
                            }
                        }
                        break;

                    case 6:
                        System.out.println("Thank you for using the system!");
                        return;

                    default:
                        System.out.println("Choose an option between 1 and 6.");
                }
            }

        } catch (SQLException e) {
            System.out.println("Database operation failed: " + e.getMessage());
        }
    }
}
