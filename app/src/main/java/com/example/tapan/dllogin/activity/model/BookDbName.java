package com.example.tapan.dllogin.activity.model;

/**
 * Created by Tapan on 3/21/2017.
 */

public class BookDbName {

    public static String getBookUid() {
        return bookUid;
    }

    public static void setBookUid(String bookUid) {
        BookDbName.bookUid = bookUid;
    }

    private static String bookUid;

    public static String getBookDbName() {
        return bookDbName;
    }

    public static void setBookDbName(String bookDbName) {
        BookDbName.bookDbName = bookDbName;
    }

    static String bookDbName;

}
