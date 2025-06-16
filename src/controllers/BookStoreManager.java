package controllers;

import java.util.*;
import java.io.*;
import models.Book;
import models.Author;
import java.nio.charset.StandardCharsets;

public class BookStoreManager {
    private List<Book> books = new ArrayList<>();
    private List<Author> authors = new ArrayList<>();
    private Scanner sc = new Scanner(System.in);
    private static final String DATA_FOLDER = ""; 


    public void loadAuthors() {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                    new FileInputStream(DATA_FOLDER + "author.dat"), StandardCharsets.UTF_8))) {

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 2) {
                    String authorID = parts[0].trim();
                    String name = parts[1].trim();
                    authors.add(new Author(authorID, name));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading author.dat: " + e.getMessage());
        }
    }

    public void loadBooks() {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                    new FileInputStream(DATA_FOLDER + "book.dat"), StandardCharsets.UTF_8))) {

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 5) {
                    String bookID = parts[0].trim();
                    String ISBN = parts[1].trim();
                    String title = parts[2].trim();
                    double price = Double.parseDouble(parts[3].trim());
                    String authorID = parts[4].trim();

                    Author author = findAuthorByID(authorID);
                    if (author != null) {
                        books.add(new Book(bookID, ISBN, title, price, author));
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading book.dat: " + e.getMessage());
        }
    }

    private void pressEnterToContinue() {
        System.out.println("\nPress Enter to go back...");
        sc.nextLine();
    }
    
    public void menu() {
        int choice;
        do {
            System.out.println("\n===== HKT Book Store Management =====");
            System.out.println("1. Show Book List");
            System.out.println("2. Add New Book");
            System.out.println("3. Update Book");
            System.out.println("4. Delete Book");
            System.out.println("5. Search Book");
            System.out.println("6. Store Data to File");
            System.out.println("0. Quit");
            System.out.print("Your choice: ");
            choice = getIntInput();
            switch (choice) {
                case 1:
                    showBooks();
                    pressEnterToContinue();
                    break;
                case 2:
                    addBook();
                    pressEnterToContinue();
                    break;
                case 3:
                    updateBook();
                    pressEnterToContinue();
                    break;
                case 4:
                    deleteBook();
                    pressEnterToContinue();
                    break;
                case 5:
                    searchBook();
                    pressEnterToContinue();
                    break;
                case 6:
                    saveToFile();
                    pressEnterToContinue();
                    break;
                case 0:
                    System.out.println("End Program!");
                    break;
                default:
                    System.out.println("Invalid choice.");
                    pressEnterToContinue();

                sc.nextLine();
            }
        } while (choice != 0);
    }

    public void showBooks() {
        if (books.isEmpty()) {
            System.out.println("No books found.");
            return;
        }
        for (Book b : books)
            System.out.println(b);
    }

    public void addBook() {
        while (true) {
            System.out.print("Enter Book ID: ");
            String id = sc.nextLine().trim();
            if (findBookByID(id) != null) {
                System.out.println("Book ID already exists.");
                continue;
            }

            System.out.print("Enter ISBN: ");
            String isbn = sc.nextLine().trim();
            System.out.print("Enter Title: ");
            String title = sc.nextLine().trim();
            double price = getDoubleInput("Enter Price: ");

            System.out.println("Available Authors:");
            for (Author a : authors)
                System.out.println(a);
            System.out.print("Enter Author ID: ");
            String aid = sc.nextLine().trim();
            Author author = findAuthorByID(aid);
            if (author == null) {
                System.out.println("Author not found.");
                continue;
            }

            books.add(new Book(id, isbn, title, price, author));
            System.out.println("Book added successfully.");

            System.out.print("Do you want to add another book? (Y/N): ");
            String cont = sc.nextLine().trim();
            if (!cont.equalsIgnoreCase("Y"))
                break;
        }
    }

    public void updateBook() {
        System.out.print("Do you want to see current book list? (Y/N): ");
        String ans = sc.nextLine().trim();
        if (ans.equalsIgnoreCase("Y")) {
            showBooks();
        }

        System.out.print("Enter bookID to update: ");
        String id = sc.nextLine().trim();
        Book bookToUpdate = findBookByID(id);
        if (bookToUpdate == null) {
            System.out.println("Book does not exist.");
            return;
        }

        System.out.print("Enter new ISBN (" + bookToUpdate.getISBN() + "): ");
        String newISBN = sc.nextLine().trim();
        if (!newISBN.isEmpty()) {
            bookToUpdate.setISBN(newISBN);
        }

        System.out.print("Enter new Title (" + bookToUpdate.getTitle() + "): ");
        String newTitle = sc.nextLine().trim();
        if (!newTitle.isEmpty()) {
            bookToUpdate.setTitle(newTitle);
        }

        System.out.print("Enter new Price (" + bookToUpdate.getPrice() + "): ");
        String newPriceStr = sc.nextLine().trim();
        if (!newPriceStr.isEmpty()) {
            try {
                double newPrice = Double.parseDouble(newPriceStr);
                bookToUpdate.setPrice(newPrice);
            } catch (NumberFormatException e) {
                System.out.println("Invalid price input, price unchanged.");
            }
        }

        // Update author (bắt buộc phải nhập hợp lệ)
        while (true) {
            System.out.print("Enter new Author ID (" + bookToUpdate.getAuthor().getAuthorID() + "): ");
            String newAuthorID = sc.nextLine().trim();
            if (newAuthorID.isEmpty()) {
                // Nếu để trống thì giữ nguyên tác giả cũ
                break;
            }
            Author newAuthor = findAuthorByID(newAuthorID);
            if (newAuthor == null) {
                System.out.println("Author not found. Please enter a valid authorID.");
            } else {
                bookToUpdate.setAuthor(newAuthor);
                break;
            }
        }

        System.out.println("Book updated successfully.");
        System.out.println(bookToUpdate);
    }

    public void deleteBook() {
        System.out.print("Do you want to see current book list? (Y/N): ");
        String ans = sc.nextLine().trim();
        if (ans.equalsIgnoreCase("Y")) {
            showBooks();
        }

        System.out.print("Enter bookID to delete: ");
        String id = sc.nextLine().trim();
        Book bookToDelete = findBookByID(id);
        if (bookToDelete == null) {
            System.out.println("Book not found.");
            return;
        }

        System.out.print("Are you sure to delete this book? (Y/N): ");
        String confirm = sc.nextLine().trim();
        if (confirm.equalsIgnoreCase("Y")) {
            books.remove(bookToDelete);
            System.out.println("Delete successful.");
        } else {
            System.out.println("Delete cancelled.");
        }
    }

    public void searchBook() {
        System.out.print("Enter keyword to search: ");
        String keyword = sc.nextLine().toLowerCase();
        boolean found = false;
        for (Book b : books) {
            if (b.getTitle().toLowerCase().contains(keyword)) {
                System.out.println(b);
                found = true;
            }
        }
        if (!found) System.out.println("No book matched.");
    }

    public void saveToFile() {
        try (PrintWriter pw = new PrintWriter(
                new OutputStreamWriter(
                    new FileOutputStream(DATA_FOLDER + "book.dat"), StandardCharsets.UTF_8))) {

            for (Book b : books) {
                pw.println(b.getBookID() + "|" + b.getISBN() + "|" + b.getTitle() + "|" + b.getPrice() + "|" + b.getAuthor().getAuthorID());
            }
            System.out.println("Data saved to book.dat successfully.");
        } catch (IOException e) {
            System.out.println("Error writing to book.dat: " + e.getMessage());
        }
    }


    private Book findBookByID(String id) {
        for (Book b : books)
            if (b.getBookID().equals(id)) return b;
        return null;
    }

    private Author findAuthorByID(String id) {
        for (Author a : authors)
            if (a.getAuthorID().equals(id)) return a;
        return null;
    }

    private int getIntInput() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Invalid integer. Please enter again: ");
            }
        }
    }

    private double getDoubleInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid double number. Please enter again.");
            }
        }
    }
}