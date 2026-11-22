import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

public class StudentGradeTracker {

    static ArrayList<String> names = new ArrayList<>();
    static ArrayList<Double> marks = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);

    static final String FILE_NAME = "students.txt";

    public static void main(String[] args) {

        loadStudents();

        int choice;

        do {
            System.out.println("\n===== STUDENT GRADE TRACKER =====");
            System.out.println("1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. View Average Marks");
            System.out.println("4. View Highest Marks");
            System.out.println("5. View Lowest Marks");
            System.out.println("6. Search Student");
            System.out.println("7. Delete Student");
            System.out.println("8. Update Student Marks");
            System.out.println("9. Exit");

            choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1:
                    addStudent();
                    break;
                case 2:
                    viewStudents();
                    break;
                case 3:
                    viewAverage();
                    break;
                case 4:
                    viewHighest();
                    break;
                case 5:
                    viewLowest();
                    break;
                case 6:
                    searchStudent();
                    break;
                case 7:
                    deleteStudent();
                    break;
                case 8:
                    updateStudentMarks();
                    break;
                case 9:
                    saveStudents();
                    System.out.println("Thank you for using Student Grade Tracker!");
                    break;
                default:
                    System.out.println("Invalid choice! Enter 1-9.");
            }

        } while (choice != 9);

        sc.close();
    }

    // Read integer safely
    public static int readInt(String message) {

        while (true) {
            System.out.print(message);
            String input = sc.nextLine();

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number!");
            }
        }
    }

    // Read marks safely
    public static double readMarks() {

        while (true) {
            System.out.print("Enter marks (0-100): ");
            String input = sc.nextLine();

            try {
                double mark = Double.parseDouble(input);

                if (mark >= 0 && mark <= 100) {
                    return mark;
                }

                System.out.println("Marks must be between 0 and 100!");

            } catch (NumberFormatException e) {
                System.out.println("Please enter numeric marks!");
            }
        }
    }

    // Add Student
    public static void addStudent() {

        System.out.print("Enter student name: ");
        String name = sc.nextLine().trim();

        if (name.isEmpty() || name.contains("|")) {
            System.out.println("Invalid name!");
            return;
        }

        double mark = readMarks();

        names.add(name);
        marks.add(mark);

        saveStudents();

        System.out.println("Student added successfully!");
    }

    // View All Students
    public static void viewStudents() {

        if (names.isEmpty()) {
            System.out.println("No students found!");
            return;
        }

        System.out.println("\n========== STUDENT REPORT ==========");

        for (int i = 0; i < names.size(); i++) {
            System.out.println(
                (i + 1) + ". " + names.get(i)
                + " | Marks: " + marks.get(i)
                + " | Grade: " + getGrade(marks.get(i))
            );
        }

        System.out.println("Total Students: " + names.size());
    }

    // Average Marks
    public static void viewAverage() {

        if (marks.isEmpty()) {
            System.out.println("No marks available!");
            return;
        }

        double total = 0;

        for (double mark : marks) {
            total += mark;
        }

        System.out.printf("Average Marks: %.2f%n", total / marks.size());
    }

    // Highest Marks
    public static void viewHighest() {

        if (marks.isEmpty()) {
            System.out.println("No students found!");
            return;
        }

        int index = 0;

        for (int i = 1; i < marks.size(); i++) {
            if (marks.get(i) > marks.get(index)) {
                index = i;
            }
        }

        System.out.println("Highest Marks: " + marks.get(index));
        System.out.println("Student Name: " + names.get(index));
    }

    // Lowest Marks
    public static void viewLowest() {

        if (marks.isEmpty()) {
            System.out.println("No students found!");
            return;
        }

        int index = 0;

        for (int i = 1; i < marks.size(); i++) {
            if (marks.get(i) < marks.get(index)) {
                index = i;
            }
        }

        System.out.println("Lowest Marks: " + marks.get(index));
        System.out.println("Student Name: " + names.get(index));
    }

    // Search Student
    public static void searchStudent() {

        System.out.print("Enter student name to search: ");
        String searchName = sc.nextLine().trim();

        int index = findStudent(searchName);

        if (index == -1) {
            System.out.println("Student not found!");
        } else {
            showStudent(index);
        }
    }

    // Find student index
    public static int findStudent(String studentName) {

        for (int i = 0; i < names.size(); i++) {
            if (names.get(i).equalsIgnoreCase(studentName)) {
                return i;
            }
        }

        return -1;
    }

    // Display one student
    public static void showStudent(int index) {

        System.out.println("\n===== STUDENT DETAILS =====");
        System.out.println("Name: " + names.get(index));
        System.out.println("Marks: " + marks.get(index));
        System.out.println("Grade: " + getGrade(marks.get(index)));
    }

    // Delete Student
    public static void deleteStudent() {

        System.out.print("Enter student name to delete: ");
        String studentName = sc.nextLine().trim();

        int index = findStudent(studentName);

        if (index == -1) {
            System.out.println("Student not found!");
            return;
        }

        names.remove(index);
        marks.remove(index);

        saveStudents();

        System.out.println("Student deleted successfully!");
    }

    // Update Student Marks
    public static void updateStudentMarks() {

        System.out.print("Enter student name: ");
        String studentName = sc.nextLine().trim();

        int index = findStudent(studentName);

        if (index == -1) {
            System.out.println("Student not found!");
            return;
        }

        System.out.println("Current Marks: " + marks.get(index));

        double newMarks = readMarks();

        marks.set(index, newMarks);

        saveStudents();

        System.out.println("Marks updated successfully!");
        System.out.println("New Grade: " + getGrade(newMarks));
    }

    // Save data to file
    public static void saveStudents() {

        try (BufferedWriter writer =
                 new BufferedWriter(new FileWriter(FILE_NAME))) {

            for (int i = 0; i < names.size(); i++) {
                writer.write(names.get(i) + "|" + marks.get(i));
                writer.newLine();
            }

        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    // Load data from file
    public static void loadStudents() {

        File file = new File(FILE_NAME);

        if (!file.exists()) {
            System.out.println("No saved data. Starting fresh.");
            return;
        }

        try (BufferedReader reader =
                 new BufferedReader(new FileReader(FILE_NAME))) {

            String line;

            while ((line = reader.readLine()) != null) {

                String[] data = line.split("\\|", 2);

                if (data.length == 2) {

                    try {
                        String name = data[0];
                        double mark = Double.parseDouble(data[1]);

                        if (mark >= 0 && mark <= 100) {
                            names.add(name);
                            marks.add(mark);
                        }

                    } catch (NumberFormatException e) {
                        System.out.println("Skipping invalid record.");
                    }
                }
            }

            System.out.println("Saved student data loaded!");

        } catch (IOException e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    // Grade Calculation
    public static String getGrade(double marks) {

        if (marks >= 90) {
            return "A";
        } else if (marks >= 80) {
            return "B";
        } else if (marks >= 70) {
            return "C";
        } else if (marks >= 60) {
            return "D";
        } else {
            return "F";
        }
    }
}