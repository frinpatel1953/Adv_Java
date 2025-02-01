package com.miniproject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

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

    public Instructor getInstructor() {
        return instructor;
    }

    public List<Student> getStudents() {
        return students;
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

class CourseInstructorHandler {
    private static final int RECORD_SIZE = 8;

    public static void saveCourseInstructor(String courseId, String instructorId, String filename) throws IOException {
        try (RandomAccessFile file = new RandomAccessFile(filename, "rw")) {
            file.seek(file.length());
            file.writeInt(Integer.parseInt(courseId));
            file.writeInt(Integer.parseInt(instructorId));
        }
    }

    public static void readCourseInstructor(String filename) throws IOException {
        try (RandomAccessFile file = new RandomAccessFile(filename, "r")) {
            while (file.getFilePointer() < file.length()) {
                int courseId = file.readInt();
                int instructorId = file.readInt();
                System.out.println("Course ID: " + courseId + ", Instructor ID: " + instructorId);
            }
        }
    }
}

class CourseStudentHandler {
    private static final int RECORD_SIZE = 8;

    public static void saveCourseStudent(String courseId, String studentId, String filename) throws IOException {
        try (RandomAccessFile file = new RandomAccessFile(filename, "rw")) {
            file.seek(file.length());
            file.writeInt(Integer.parseInt(courseId));
            file.writeInt(Integer.parseInt(studentId));
        }
    }

    public static void readCourseStudent(String filename) throws IOException {
        try (RandomAccessFile file = new RandomAccessFile(filename, "r")) {
            while (file.getFilePointer() < file.length()) {
                int courseId = file.readInt();
                int studentId = file.readInt();
                System.out.println("Course ID: " + courseId + ", Student ID: " + studentId);
            }
        }
    }
}

public class CourseManagementSystem {
    public static void main(String[] args) throws IOException {
        // Load data from CSV files
        List<Course> courses = loadCourses("src/data/course.csv");
        List<Instructor> instructors = loadInstructors("src/data/instructor.csv");
        List<Student> students = loadStudents("src/data/student.csv");

        // Associate instructors and students to courses
        for (Course course : courses) {
            // Find and assign the instructor for this course
            boolean instructorAssigned = false;
            for (Instructor instructor : instructors) {
                // Assuming courseId matches instructorId for this example
                if (instructor.getInstructorId().equals(course.getCourseId())) {
                    course.assignInstructor(instructor);
                    instructorAssigned = true;
                    break;
                }
            }
            if (!instructorAssigned) {
                System.out.println("Warning: No instructor assigned for course " + course.getCourseName());
            }

            // Enroll students in the course
            for (Student student : students) {
                if (student.getStudentId().equals(course.getCourseId())) {
                    course.addStudent(student);
                }
            }

            // Display the course details
            course.displayCourseDetails();

            // Save course-instructor and course-student relationships to binary files
            if (course.getInstructor() != null) {
                CourseInstructorHandler.saveCourseInstructor(course.getCourseId(), course.getInstructor().getInstructorId(),
                        "src/data/course_instructor.dat");
            }
            for (Student student : course.getStudents()) {
                CourseStudentHandler.saveCourseStudent(course.getCourseId(), student.getStudentId(),
                        "src/data/course_student.dat");
            }
        }

        // Read saved course-instructor and course-student relationships
        System.out.println("\nSaved Course-Instructor Relationships:");
        CourseInstructorHandler.readCourseInstructor("src/data/course_instructor.dat");

        System.out.println("\nSaved Course-Student Relationships:");
        CourseStudentHandler.readCourseStudent("src/data/course_student.dat");
    }

    // Method to load courses from CSV file
    public static List<Course> loadCourses(String filename) throws IOException {
        List<Course> courses = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                String courseId = fields[0].trim();
                String courseName = fields[1].trim();
                int credits = Integer.parseInt(fields[2].trim());
                courses.add(new Course(courseId, courseName, credits));
            }
        }
        return courses;
    }

    // Method to load instructors from CSV file
    public static List<Instructor> loadInstructors(String filename) throws IOException {
        List<Instructor> instructors = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length >= 4) {
                    String instructorId = fields[0].trim();
                    String name = fields[1].trim();
                    String email = fields[2].trim();
                    String department = fields[3].trim();
                    instructors.add(new Instructor(instructorId, name, email, department));
                } else {
                    System.out.println("Skipping malformed line: " + line);
                }
            }
        }
        return instructors;
    }

    // Method to load students from CSV file
    public static List<Student> loadStudents(String filename) throws IOException {
        List<Student> students = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                String studentId = fields[0].trim();
                String name = fields[1].trim();
                String email = fields[2].trim();
                students.add(new Student(studentId, name, email));
            }
        }
        return students;
    }
}
