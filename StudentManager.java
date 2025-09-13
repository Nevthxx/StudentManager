
import java.sql.*;
import java.util.Scanner;

public class StudentManager {
    private static final String URL = "jdbc:mysql://localhost:3306/testdb?useSSL=false&serverTimezone=UTC";
    private static final String USER = "javaprogram";
    private static final String PASSWORD = "mypassword";


    private Connection conn;
    private Scanner scanner;

    public StudentManager() {
        scanner = new Scanner(System.in);
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to database successfully!");
            createTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Create table if not exists
    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS students ("
                + "id INT PRIMARY KEY AUTO_INCREMENT, "
                + "name VARCHAR(100) NOT NULL, "
                + "age INT NOT NULL)";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Insert new student
    private void addStudent() {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter age: ");
        int age = Integer.parseInt(scanner.nextLine());

        String sql = "INSERT INTO students (name, age) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            pstmt.executeUpdate();
            System.out.println("Student added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // View all students
    private void viewStudents() {
        String sql = "SELECT * FROM students";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n--- Student List ---");
            while (rs.next()) {
                System.out.printf("ID: %d | Name: %s | Age: %d%n",
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age"));
            }
            System.out.println("--------------------\n");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Update student
    private void updateStudent() {
        System.out.print("Enter student ID to update: ");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter new name: ");
        String name = scanner.nextLine();
        System.out.print("Enter new age: ");
        int age = Integer.parseInt(scanner.nextLine());

        String sql = "UPDATE students SET name=?, age=? WHERE id=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            pstmt.setInt(3, id);
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Student updated successfully!");
            } else {
                System.out.println("Student not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete student
    private void deleteStudent() {
        System.out.print("Enter student ID to delete: ");
        int id = Integer.parseInt(scanner.nextLine());

        String sql = "DELETE FROM students WHERE id=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Student deleted successfully!");
            } else {
                System.out.println("Student not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Main menu loop
    public void run() {
        while (true) {
            System.out.println("===== Student Manager =====");
            System.out.println("1. Add Student");
            System.out.println("2. View Students");
            System.out.println("3. Update Student");
            System.out.println("4. Delete Student");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1": addStudent(); break;
                case "2": viewStudents(); break;
                case "3": updateStudent(); break;
                case "4": deleteStudent(); break;
                case "5":
                    System.out.println("Goodbye!");
                    close();
                    return;
                default: System.out.println("Invalid choice. Try again.");
            }
        }
    }

    // Close resources
    private void close() {
        try {
            if (conn != null && !conn.isClosed()) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        scanner.close();
    }

    public static void main(String[] args) {
        StudentManager app = new StudentManager();
        app.run();
    }
}
