import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.*;

// Abstract class
abstract class SchoolMember {
    String name;
    int age;

    SchoolMember(String name, int age) {
        this.name = name;
        this.age = age;
    }

    abstract void display();

    @Override
    public String toString() {
        return "Name: " + name + ", Age: " + age;
    }
}

// Main object class
class Student extends SchoolMember {
    String studentId;
    double gpa;

    Student(String name, int age, String studentId, double gpa) {
        super(name, age);
        this.studentId = studentId;
        this.gpa = gpa;
    }

    @Override
    void display() {
        System.out.println(this);
    }

    @Override
    public String toString() {
        return super.toString() + ", Student ID: " + studentId + ", GPA: " + gpa;
    }
}

// Subclasses
class Undergraduate extends Student {
    String major;

    Undergraduate(String name, int age, String studentId, double gpa, String major) {
        super(name, age, studentId, gpa);
        this.major = major;
    }

    @Override
    void display() {
        super.display();
        System.out.println(this);
    }

    @Override
    public String toString() {
        return super.toString() + ", Major: " + major;
    }
}

class Graduate extends Student {
    String researchTopic;

    Graduate(String name, int age, String studentId, double gpa, String researchTopic) {
        super(name, age, studentId, gpa);
        this.researchTopic = researchTopic;
    }

    @Override
    void display() {
        super.display();
        System.out.println(this);
    }

    @Override
    public String toString() {
        return super.toString() + ", Research Topic: " + researchTopic;
    }
}

// Collection class with Map and Iterator
class StudentCollection implements Iterable<Student> {
    private Map<String, Student> students;

    StudentCollection() {
        students = new HashMap<>();
    }

    void addStudent(Student student) {
        students.put(student.studentId, student);
    }

    void removeStudent(String studentId) {
        students.remove(studentId);
    }

    Student getStudent(String studentId) {
        return students.get(studentId);
    }

    void sortStudents(Comparator<Student> comparator) {
        List<Student> studentList = new ArrayList<>(students.values());
        Collections.sort(studentList, comparator);
        students.clear();
        for (Student student : studentList) {
            students.put(student.studentId, student);
        }
    }

    @Override
    public Iterator<Student> iterator() {
        return new StudentIterator();
    }

    private class StudentIterator implements Iterator<Student> {
        private Iterator<Student> iterator;

        StudentIterator() {
            this.iterator = students.values().iterator();
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public Student next() {
            return iterator.next();
        }
    }
}

// Comparator classes
class StudentNameComparator implements Comparator<Student> {
    @Override
    public int compare(Student s1, Student s2) {
        return s1.name.compareTo(s2.name);
    }
}

class StudentGPAComparator implements Comparator<Student> {
    @Override
    public int compare(Student s1, Student s2) {
        return Double.compare(s1.gpa, s2.gpa);
    }
}

// GUI Class
class StudentTableModel extends AbstractTableModel {
    private final String[] columnNames = {"Student ID", "Name", "Age", "GPA"};
    private final List<Student> students;

    StudentTableModel(List<Student> students) {
        this.students = students;
    }

    @Override
    public int getRowCount() {
        return students.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Student student = students.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return student.studentId;
            case 1:
                return student.name;
            case 2:
                return student.age;
            case 3:
                return student.gpa;
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
}

public class StudentManagement {
    public static void main(String[] args) {
        // Create students
        Student s1 = new Undergraduate("Alice", 20, "UG2021001", 3.5, "Computer Science");
        Student s2 = new Graduate("Bob", 25, "GR2021001", 3.8, "Artificial Intelligence");

        // Add to student collection
        StudentCollection studentCollection = new StudentCollection();
        studentCollection.addStudent(s1);
        studentCollection.addStudent(s2);

        // Display students
        for (Student student : studentCollection) {
            student.display();
        }

        // Sort and display sorted students by name
        studentCollection.sortStudents(new StudentNameComparator());
        System.out.println("Students sorted by name:");
        for (Student student : studentCollection) {
            student.display();
        }

        // GUI part
        List<Student> studentList = new ArrayList<>();
        for (Student student : studentCollection) {
            studentList.add(student);
        }

        StudentTableModel model = new StudentTableModel(studentList);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        JFrame frame = new JFrame("Student Management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setVisible(true);
    }
}
