package com.example.sellybook.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.sellybook.MyApplication;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;

@Entity
public class Book {
    final static String ID = "id";
    public final static String LAST_UPDATED = "LAST_UPDATED";

    @PrimaryKey
    @NonNull
    @DocumentId
    public String id = "";
    public String name = "";
    public String price = "";
    public String author = "";
    public String phone = "";
    public String address = "";
    public boolean cb = false;
    String bookImageURL = "";
    Long lastUpdated = new Long(0);

    public Book() {
    }

    public Book(String name,String price, String phone, String address, boolean cb) {
        this.name = name;
        this.price = price;
        this.cb = cb;
        this.phone = phone;
        this.address = address;

    }
    public Book(String name, String id, String price, String author, String phone, boolean cb) {
        this.name = name;
        this.id = id;
        this.price = price;
        this.author = author;
        this.cb = cb;
        this.phone = phone;
    }


    public Long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getPhone() {
        return this.phone;
    }

    public String getAuthor() {
        return this.author;
    }

    public String getPrice() {
        return this.price;
    }

    public boolean isFlag() {
        return this.cb;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFlag(boolean flag) {
        this.cb = flag;
    }

    public String getBookImageURL() {
        return bookImageURL;
    }

    public void setBookImageURL(String bookImageURL) {
        this.bookImageURL = bookImageURL;
    }

    public Map<String, Object> toJason() {
        Map<String, Object> json = new HashMap<>();
        json.put(ID, getId());
        json.put("name", getName());
        json.put("flag", isFlag());
        json.put("price", getPrice());
        json.put("author", getAuthor());
        json.put("phone", getPhone());
        json.put(LAST_UPDATED, FieldValue.serverTimestamp());
        json.put("bookImageURL", bookImageURL);
        return json;
    }


    static Book fromJson(Map<String, Object> json) {
        String id = (String) json.get(ID);
        if (id == null) {
            return null;
        }
        String name = (String) json.get("name");
        String price = (String) json.get("price");
        String author = (String) json.get("author");
        String phone = (String) json.get("phone");
        boolean flag = (boolean) json.get("flag");
        Book book = new Book(name, id, price, author, phone, flag);
        String bookImageURL = (String)json.get("bookImageURL");
        Timestamp ts = (Timestamp) json.get(LAST_UPDATED);
        book.setLastUpdated(new Long(ts.getSeconds()));
        book.setBookImageURL(bookImageURL);
        return book;

    }

    static Long getLocalLastUpdated(){
        Long localLastUpdate = MyApplication.getContext().getSharedPreferences("Tag", Context.MODE_PRIVATE)
                .getLong("BOOKS_LAST_UPDATE", 0);
        return localLastUpdate;
    }

    static void setLocalLastUpdated(Long date){
        SharedPreferences.Editor editor = MyApplication.getContext().getSharedPreferences("Tag", Context.MODE_PRIVATE).edit();
        editor.putLong("BOOKS_LAST_UPDATE", date);
        editor.commit();
    }
}
