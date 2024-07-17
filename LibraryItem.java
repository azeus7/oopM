import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.*;

// Abstract class
abstract class LibraryItem {
    String title;
    String author;

    LibraryItem(String title, String author) {
        this.title = title;
        this.author = author;
    }

    abstract void display();

    @Override
    public String toString() {
        return "Title: " + title + ", Author: " + author;
    }
}

// Main object class
class Book extends LibraryItem {
    String isbn;
    int publicationYear;

    Book(String title, String author, String isbn, int publicationYear) {
        super(title, author);
        this.isbn = isbn;
        this.publicationYear = publicationYear;
    }

    @Override
    void display() {
        System.out.println(this);
    }

    @Override
    public String toString() {
        return super.toString() + ", ISBN: " + isbn + ", Year: " + publicationYear;
    }
}

// Subclasses
class Fiction extends Book {
    String genre;

    Fiction(String title, String author, String isbn, int publicationYear, String genre) {
        super(title, author, isbn, publicationYear);
        this.genre = genre;
    }

    @Override
    void display() {
        super.display();
        System.out.println(this);
    }

    @Override
    public String toString() {
        return super.toString() + ", Genre: " + genre;
    }
}

class NonFiction extends Book {
    String subject;

    NonFiction(String title, String author, String isbn, int publicationYear, String subject) {
        super(title, author, isbn, publicationYear);
        this.subject = subject;
    }

    @Override
    void display() {
        super.display();
        System.out.println(this);
    }

    @Override
    public String toString() {
        return super.toString() + ", Subject: " + subject;
    }
}

// Collection class with Map and Iterator
class Library implements Iterable<Book> {
    private Map<String, Book> books;

    Library() {
        books = new HashMap<>();
    }

    void addBook(Book book) {
        books.put(book.isbn, book);
    }

    void removeBook(String isbn) {
        books.remove(isbn);
    }

    Book getBook(String isbn) {
        return books.get(isbn);
    }

    void sortBooks(Comparator<Book> comparator) {
        List<Book> bookList = new ArrayList<>(books.values());
        Collections.sort(bookList, comparator);
        books.clear();
        for (Book book : bookList) {
            books.put(book.isbn, book);
        }
    }

    @Override
    public Iterator<Book> iterator() {
        return new BookIterator();
    }

    private class BookIterator implements Iterator<Book> {
        private Iterator<Book> iterator;

        BookIterator() {
            this.iterator = books.values().iterator();
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public Book next() {
            return iterator.next();
        }
    }
}

// Comparator classes
class BookTitleComparator implements Comparator<Book> {
    @Override
    public int compare(Book b1, Book b2) {
        return b1.title.compareTo(b2.title);
    }
}

class BookYearComparator implements Comparator<Book> {
    @Override
    public int compare(Book b1, Book b2) {
        return Integer.compare(b1.publicationYear, b2.publicationYear);
    }
}

// GUI Class
class LibraryTableModel extends AbstractTableModel {
    private final String[] columnNames = {"ISBN", "Title", "Author", "Year"};
    private final List<Book> books;

    LibraryTableModel(List<Book> books) {
        this.books = books;
    }

    @Override
    public int getRowCount() {
        return books.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Book book = books.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return book.isbn;
            case 1:
                return book.title;
            case 2:
                return book.author;
            case 3:
                return book.publicationYear;
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
}

public class LibraryManagement {
    public static void main(String[] args) {
        // Create books
        Book b1 = new Fiction("The Great Gatsby", "F. Scott Fitzgerald", "9780743273565", 1925, "Novel");
        Book b2 = new NonFiction("A Brief History of Time", "Stephen Hawking", "9780553380163", 1988, "Science");

        // Add to library
        Library library = new Library();
        library.addBook(b1);
        library.addBook(b2);

        // Display books
        for (Book book : library) {
            book.display();
        }

        // Sort and display sorted books by title
        library.sortBooks(new BookTitleComparator());
        System.out.println("Books sorted by title:");
        for (Book book : library) {
            book.display();
        }

        // GUI part
        List<Book> bookList = new ArrayList<>();
        for (Book book : library) {
            bookList.add(book);
        }

        LibraryTableModel model = new LibraryTableModel(bookList);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        JFrame frame = new JFrame("Library Management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setVisible(true);
    }
}
