package models;

public class Book implements IEntity {
    private final String bookID;
    private String ISBN;
    private String title;
    private double price;
    private Author author;

    public Book(String bookID, String ISBN, String title, double price, Author author) {
        this.bookID = bookID;
        this.ISBN = ISBN;
        this.title = title;
        this.price = price;
        this.author = author;
    }

    public String getBookID() {
        return bookID;
    }

    public String getISBN() {
        return ISBN;
    }

    public String getTitle() {
        return title;
    }

    public double getPrice() {
        return price;
    }

    public Author getAuthor() {
        return author;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    
    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }


    @Override
    public String getID() {
        return bookID;
    }

    @Override
    public String toString() {
        return bookID + " | " + ISBN + " | " + title + " | " + price + " | " + author.getAuthorID() + "-" + author.getName();
    }
}