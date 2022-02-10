package com.example.sellybook.model;
import com.google.firebase.auth.FirebaseAuth;

import java.util.LinkedList;
import java.util.List;

public class Model {
    public final static Model instance = new Model();
    private ModelFireBase modelFireBase = ModelFireBase.instance;

    //TODO: check if there is a need for local database
    private List<Book> data = new LinkedList<Book>();
    //TODO:++++++++++++++++++++++


    private  Model(){
        //TODO:delete this rows inside the constructor
        for( int i=0; i <100;i++){
            Book book = new Book();
            book.id ="1234";
            book.name = "abc"+i;
            book.price = ""+i;
            data.add(book);
        }

    }


    public FirebaseAuth getFireBaseAuthInstance() {
        return modelFireBase.getFireBaseAuthInstance();
    }


    public List<Book> getAllBooks() {
        return data;
    }

    public Book getBooktById(String bookId){
        for(Book b_:data){
            if(b_.id.equals(bookId)){
                return b_;
            }
        }
        return null;
    }
}
