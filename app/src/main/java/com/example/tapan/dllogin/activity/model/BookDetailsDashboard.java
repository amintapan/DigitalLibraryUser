package com.example.tapan.dllogin.activity.model;

/**
 * Created by Tapan on 3/16/2017.
 */

public class BookDetailsDashboard {

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    private String userId;
    private String authorName, availableBooks, bookId, bookName;
    private String  issues, pages, price, publication, quantity;
    private String bookDbName;

    public String getBookDbName() {
        return bookDbName;
    }

    public void setBookDbName(String bookDbName) {
        this.bookDbName = bookDbName;
    }

    int numberOfBooks;

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAvailableBooks() {
        return availableBooks;
    }

    public void setAvailableBooks(String availableBooks) {
        this.availableBooks = availableBooks;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getIssues() {
        return issues;
    }

    public void setIssues(String issues) {
        this.issues = issues;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPublication() {
        return publication;
    }

    public void setPublication(String publication) {
        this.publication = publication;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public int getNumberOfBooks() {
        return numberOfBooks;
    }

    public void setNumberOfBooks(int numberOfBooks) {
        this.numberOfBooks = numberOfBooks;
    }
}
