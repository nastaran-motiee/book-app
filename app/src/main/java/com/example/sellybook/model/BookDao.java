package com.example.sellybook.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface BookDao {
    @Query("select * from Book")
    List<Book> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Book... books);

    @Delete
    void delete(Book book);

    @Query("SELECT * FROM Book WHERE id=:id ")
    Book getBookById(String id);

    @Update(entity = Book.class)
    void updateBook(Book book);




}
