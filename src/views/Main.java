package views;

import controllers.BookStoreManager;

public class Main {
    public static void main(String[] args) {
        BookStoreManager manager = new BookStoreManager();
        manager.loadAuthors();  // Load dữ liệu tác giả từ author.dat
        manager.loadBooks();    // Load dữ liệu sách từ book.dat
        manager.menu();         // Bắt đầu menu chính
    }
}