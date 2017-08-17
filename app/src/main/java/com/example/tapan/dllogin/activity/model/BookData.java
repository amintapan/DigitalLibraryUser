package com.example.tapan.dllogin.activity.model;

/**
 * Created by Tapan on 3/10/2017.
 */

public class BookData {
    public static String getAuthorName() {
        return authorName;
    }

    public static void setAuthorName(String authorName) {
        BookData.authorName = authorName;
    }

    public static String getAvailableBooks() {
        return availableBooks;
    }

    public static void setAvailableBooks(String availableBooks) {
        BookData.availableBooks = availableBooks;
    }

    public static String getBookId() {
        return bookId;
    }

    public static void setBookId(String bookId) {
        BookData.bookId = bookId;
    }

    public static String getBookName() {
        return bookName;
    }

    public static void setBookName(String bookName) {
        BookData.bookName = bookName;
    }

    public static String getIssues() {
        return issues;
    }

    public static void setIssues(String issues) {
        BookData.issues = issues;
    }

    public static String getPages() {
        return pages;
    }

    public static void setPages(String pages) {
        BookData.pages = pages;
    }

    public static String getPrice() {
        return price;
    }

    public static void setPrice(String price) {
        BookData.price = price;
    }

    public static String getPublication() {
        return publication;
    }

    public static void setPublication(String publication) {
        BookData.publication = publication;
    }

    public static String getQuantity() {
        return quantity;
    }

    public static void setQuantity(String quantity) {
        BookData.quantity = quantity;
    }

    private static String authorName, availableBooks, bookId, bookName;
    private static String  issues, pages, price, publication, quantity;

    public int getNumberOfBooks() {
        return numberOfBooks;
    }

    public void setNumberOfBooks(int numberOfBooks) {
        this.numberOfBooks = numberOfBooks;
    }

    int numberOfBooks;

    public float getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(float avgRating) {
        this.avgRating = avgRating;
    }

    float avgRating;
}
