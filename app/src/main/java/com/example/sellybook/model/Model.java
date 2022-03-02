package com.example.sellybook.model;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.sellybook.MyApplication;
import com.google.firebase.auth.FirebaseAuth;

import java.util.LinkedList;
import java.util.List;

public class Model {
    public final static Model instance = new Model();
    private ModelFireBase modelFireBase = ModelFireBase.instance;
    private List<Book> data = new LinkedList<Book>();


    private Model() {
        reloadAllBooksList();
    }


    public FirebaseAuth getFireBaseAuthInstance() {
        return modelFireBase.getFireBaseAuthInstance();
    }


    public interface getAllBooksListener {
        void onComplete(List<Book> data);
    }

    public void getAllBooks(getAllBooksListener listener) {
     //  MyApplication.executorService.execute(()->{
     //      List<Book> data = AppLocalDB.db.bookDao().getAll();
     //      MyApplication.mainHandler.post(()->{
     //          listener.onComplete(data);
     //      });
     //  });
    }


    MutableLiveData<List<Book>> allBooksListLiveData = new MutableLiveData<List<Book>>();


    public void reloadAllBooksList() {
        //1. get local last update
       Long localLastUpdate = Book.getLocalLastUpdated();
        //2. get all books record since local last update from firebase
        modelFireBase.getAllBooks(localLastUpdate, (list) -> {
            MyApplication.executorService.execute(() -> {
                //3. update local last update date
                //4. add new records to local db
                Long loLastUpdate = new Long(0);
                for (Book b : list) {
                    AppLocalDB.db.bookDao().insertAll(b);
                    if (b.getLastUpdated() > loLastUpdate) {
                        loLastUpdate = b.getLastUpdated();
                    }

                }

                Book.setLocalLastUpdated(loLastUpdate);

                //5. return all the records to the caller
                List<Book> bkList = AppLocalDB.db.bookDao().getAll();
                allBooksListLiveData.postValue(bkList);

            });


        });

    }

//  MutableLiveData<List<Book>> userUploadedBooksListLiveData = new MutableLiveData<List<Book>>();
//  public void reloadUserUploadedBooksList() {
//      //1. get local last update
//      Long localLastUpdate = Book.getLocalLastUpdated();
//      //2. get all books record since local last update from firebase
//      modelFireBase.getUserUploadedBooks(localLastUpdate, (list) -> {
//          MyApplication.executorService.execute(() -> {
//              //3. update local last update date
//              //4. add new records to local db
//              Long loLastUpdate = new Long(0);
//              Book.setLocalLastUpdated(loLastUpdate);
//              //5. return all the records to the caller
//              List<Book> bkList = AppLocalDB.db.bookDao().getAll();
//              userUploadedBooksListLiveData.postValue(bkList);

//          });


//      });

//  }



    public LiveData<List<Book>> getAll() {
        return allBooksListLiveData;
    }

    public interface AddBookListener {
        void onComplete();
    }

    public void addBook(Book book, AddBookListener listener) {
        ModelFireBase.addBook(book, () -> {
            reloadAllBooksList();
            listener.onComplete();
        });
     //   MyApplication.executorService.execute(()->{
     //      AppLocalDB.db.bookDao().insertAll(book);
     //      MyApplication.mainHandler.post(()->{
     //          listener.onComplete();
     //      });
     //  });
    }



    public interface GetBookByIdListener {
        void onComplete(Book book);
    }

    public void getBookById(String bookId, GetBookByIdListener listener) {
        modelFireBase.getBookById(bookId, listener);
     //   MyApplication.executorService.execute(()->{
     //      Book book = AppLocalDB.db.bookDao().getBookById(bookId);
     //      MyApplication.mainHandler.post(()->{
     //          listener.onComplete(book);
     //      });
     //  });
    }

    public Book getBktById(String bookId) {
        for (Book b_ : data) {
            if (b_.id.equals(bookId)) {
                return b_;
            }
        }
        return null;
    }




    public interface  SaveImageListener{
        void onComplete(String url);
    }
    public void saveImage(Bitmap bitmap, Book book,SaveImageListener listener) {
        modelFireBase.saveImage(bitmap,book, listener);
    }




    public void addBookToUsersFavorites(Book book, ModelFireBase.AddBookToUsersFavoritesListener listener) {
        MyApplication.executorService.execute(()->{
                 AppLocalDB.db.bookDao().updateBook(book);
                 MyApplication.mainHandler.post(()->{
                     listener.onComplete();
                 });
             });

        modelFireBase.addBookToUsersFavorites(book,listener);
    }

    public void deleteBookFromUsersFavorites(Book book, ModelFireBase.DeleteBookFromUsersFavorites listener){
        MyApplication.executorService.execute(()->{
            AppLocalDB.db.bookDao().updateBook(book);
            MyApplication.mainHandler.post(()->{
                listener.onComplete();
            });
        });
      modelFireBase.deleteBookFromUsersFavorites(book,listener);

    }

}
