import java.util.ArrayList;
import java.io.*;
import java.util.List;

// Superclass Person
class Person {
    private String name;
    private String email;

    public Person(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void displayDetails() {
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
    }
}

// Subclass Student
class Student extends Person {
    private String studentId;

    public Student(String studentId, String name, String email) {
        super(name, email);
        this.studentId = studentId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    @Override
    public void displayDetails() {
        System.out.println("Student ID: " + studentId);
        super.displayDetails();
    }
}

// Subclass Instructor
class Instructor extends Person {
    private String instructorId;
    private String department;

    public Instructor(String instructorId, String name, String email, String department) {
        super(name, email);
        this.instructorId = instructorId;
        this.department = department;
    }

    public String getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(String instructorId) {
        this.instructorId = instructorId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public void displayDetails() {
        System.out.println("Instructor ID: " + instructorId);
        System.out.println("Department: " + department);
        super.displayDetails();
    }
}

// Class Course
class Course {
    private String courseId;
    private String courseName;
    private int credits;
    private Instructor instructor;
    private List<Student> students;

    public Course(String courseId, String courseName, int credits) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.credits = credits;
        this.students = new ArrayList<>();
    }

    public String getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public int getCredits() {
        return credits;
    }

    public void assignInstructor(Instructor instructor) {
        this.instructor = instructor;
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public void displayCourseDetails() {
        System.out.println("Course ID: " + courseId);
        System.out.println("Course Name: " + courseName);
        System.out.println("Credits: " + credits);
        if (instructor != null) {
            System.out.println("\nInstructor Details:");
            instructor.displayDetails();
        } else {
            System.out.println("\nNo instructor assigned yet.");
        }

        System.out.println("\nEnrolled Students:");
        if (students.isEmpty()) {
            System.out.println("No students enrolled yet.");
        } else {
            for (Student student : students) {
                student.displayDetails();
                System.out.println();
            }
        }
    }
}

// Main Class
public class CourseManagementSystem {
    public static void main(String[] args) {
        // Create Course
        Course course = new Course("101", "Object-Oriented Programming", 4);

        // Create Instructor
        Instructor instructor = new Instructor("501", "Dr. Emily White", "emily.white@university.com", "Computer Science");

        // File path to the CSV file
        String file = "C:\\Users\\frinp\\Desktop\\OneDrive\\OneDrive - Humber Polytechnic\\SEM 2\\Advance Java\\CourseManagementSystem\\Students.csv";
        BufferedReader reader = null;
        String line;

        try {
            // Initialize BufferedReader to read the file
            reader = new BufferedReader(new FileReader(file));
            while ((line = reader.readLine()) != null) {
                // Split each row by commas
                String[] row = line.split(",");
                if (row.length == 3) {
                    // Create a Student object from the CSV data
                    String studentId = row[0].trim();
                    String studentName = row[1].trim();
                    String studentEmail = row[2].trim();
                    Student student = new Student(studentId, studentName, studentEmail);

                    // Add the student to the course
                    course.addStudent(student);
                } else {
                    System.out.println("Invalid row format: " + line);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("The file was not found: " + file);
        } catch (IOException e) {
            System.err.println("An error occurred while reading the file: " + e.getMessage());
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        // Assign Instructor
        course.assignInstructor(instructor);

        // Display Course Details
        course.displayCourseDetails();
    }
}
