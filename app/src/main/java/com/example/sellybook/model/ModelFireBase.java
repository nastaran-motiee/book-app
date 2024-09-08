package com.example.sellybook.model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.sellybook.MyApplication;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.core.ViewSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ModelFireBase {

    private List<Book> data = new LinkedList<Book>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public final static ModelFireBase instance = new ModelFireBase();
    FirebaseUser user;
    String  currentUserId;



    private ModelFireBase(){
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
    }

    public static void addBook(Book book, Model.AddBookListener listener) {

   //   DocumentReference ref = ModelFireBase.instance.db.collection("books").document();
   //   book.setId(ref.toString());
   //     Log.d("tah", "addBook: "+ref.toString());
   //   ref.set(book.toJason())
   //           .addOnSuccessListener((successListener)->{
   //               ModelFireBase.instance.db.collection("books").document(book.getId()).set(book.toJason());
   //               listener.onComplete();
   //           })
   //           .addOnFailureListener((e)->{
   //               Log.w("TAG", "Error adding document", e);
   //           });
      ModelFireBase.instance.db.collection("books")
             .document(book.getId()).set(book.toJason())
             .addOnSuccessListener((successListener)->{
                 listener.onComplete();
             })
             .addOnFailureListener((e)->{
                 Log.w("TAG", "Error adding document", e);
             });


        //TODO:)))))))))))))))


    }

    public FirebaseAuth getFireBaseAuthInstance() {
         return FirebaseAuth.getInstance();
    }

    public void getAllBooks(Long since, Model.getAllBooksListener listener) {

        db.collection("books")
                .whereGreaterThanOrEqualTo(Book.LAST_UPDATED, new Timestamp(since, 0))
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                LinkedList<Book> booksList = new LinkedList<Book>();
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot doc: task.getResult()){
                         Book b = Book.fromJson(doc.getData());
                         if(b != null){
                             booksList.add(b);
                         }

                    }
                }else {

                }
                listener.onComplete(booksList);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onComplete(null);

            }
        });




    }

    public interface GetUserUploadedBooksListener{
        void onComplete(LinkedList<Book> books);
    }

    MutableLiveData<List<Book>> userUploadedBooksListLiveData = new MutableLiveData<List<Book>>();
    public LiveData<List<Book>> getAllUserUploaded() {
        return userUploadedBooksListLiveData;
    }

    public void getUserUploadedBooks( GetUserUploadedBooksListener listener) {
        user=FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        currentUserId = user.getUid();
        db.collection("users").document(currentUserId).collection("this_user_uploads"+currentUserId)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                LinkedList<Book> booksList = new LinkedList<Book>();
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot doc: task.getResult()){
                        Book b = Book.fromJson(doc.getData());
                        if(b != null){
                            booksList.add(b);
                        }

                    }
                }else {
                    // Handle the error appropriately
                    // e.g., Log the error or show a message to the user
                    Log.e("Firestore", "Error getting documents: ", task.getException());
                }
                userUploadedBooksListLiveData.postValue(booksList);
                listener.onComplete(booksList);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onComplete(null);

            }
        });




    }

    public void getBookById(String bookId, Model.GetBookByIdListener listener) {
        DocumentReference docRef = db.collection("books").document(bookId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Book b = Book.fromJson(document.getData());
                        listener.onComplete(b);

                    } else {
                        listener.onComplete(null);
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                    listener.onComplete(null);
                }
            }
        });
    }

    public void saveImage(Bitmap bitmap, Book book, Model.SaveImageListener listener) {

        DocumentReference ref = ModelFireBase.instance.db.collection("books").document();
        book.setId(ref.toString());
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child("bookImage/" + book.getId()+ ".jpg");

        // Get the data from an ImageView as bytes
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if(bitmap!=null){
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        }
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnFailureListener(exception -> listener.onComplete(null)).addOnSuccessListener(taskSnapshot -> {
            imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                Uri downloadUrl = uri;
                listener.onComplete(downloadUrl.toString());
            });
            listener.onComplete(null);
        });
    }


    public interface AddBookToUserUploadsListener {
        void onComplete();
    }

    public void addBookToUserUploads(Book book, AddBookToUserUploadsListener listener) {
        user=FirebaseAuth.getInstance().getCurrentUser();
        currentUserId = user.getUid();
        CollectionReference users = ModelFireBase.instance.db.collection("users");
        ModelFireBase.instance.db.collection("users")
                .document(currentUserId).collection("this_user_uploads"+currentUserId).document(book.getId()).set(book.toJason())
                .addOnSuccessListener((successListener)->{
                    listener.onComplete();
                })
                .addOnFailureListener((e)->{
                    Log.w("TAG", "Error adding document", e);
                });
    }

    //TODO:-------
    public interface DeleteBook{
        void onComplete();
    }

    public void deleteBook(Book book, DeleteBookFromUsersFavorites listener){
        user=FirebaseAuth.getInstance().getCurrentUser();
        currentUserId = user.getUid();
        ModelFireBase.instance.db.collection("users")
                .document(currentUserId).collection("this_user_uploads"+currentUserId).document(book.getId()).delete()
                .addOnSuccessListener((successListener)->{
                    listener.onComplete();
                })
                .addOnFailureListener((e)->{
                    Log.w("TAG", "Error adding document", e);
                });
        ModelFireBase.instance.db.collection("books")
                .document(book.getId()).delete()
                .addOnSuccessListener((successListener)->{
                    listener.onComplete();
                })
                .addOnFailureListener((e)->{
                    Log.w("TAG", "Error adding document", e);
                });

    //   MyApplication.executorService.execute(() -> {

    //       Long loLastUpdate = new Long(0);
    //           AppLocalDB.db.bookDao().insertAll(book);
    //           if (book.getLastUpdated() > loLastUpdate) {
    //               loLastUpdate = book.getLastUpdated();
    //           }
    //               Book.setLocalLastUpdated(loLastUpdate);
    //       });

     //  //5. return all the records to the caller
     //  List<Book> bkList = AppLocalDB.db.bookDao().getAll();
     //  Model.instance.allBooksListLiveData.postValue(bkList);

        }




    //TODO:____

    public interface AddBookToUsersFavoritesListener{
        void onComplete();
    }

    public void addBookToUsersFavorites(Book book,AddBookToUsersFavoritesListener listener) {
       user=FirebaseAuth.getInstance().getCurrentUser();
       currentUserId = user.getUid();
       CollectionReference users = ModelFireBase.instance.db.collection("users");

                ModelFireBase.instance.db.collection("users")
                        .document(currentUserId).collection("this_user_favorites").document(book.getId()).set(book.toJason())
                        .addOnSuccessListener((successListener)->{
                            listener.onComplete();
                        })
                        .addOnFailureListener((e)->{
                            Log.w("TAG", "Error adding document", e);
                        });



    }

    public interface DeleteBookFromUsersFavorites{
        void onComplete();
    }

    public void deleteBookFromUsersFavorites(Book book, DeleteBookFromUsersFavorites listener){
        user=FirebaseAuth.getInstance().getCurrentUser();
        currentUserId = user.getUid();
        ModelFireBase.instance.db.collection("users")
                .document(currentUserId).collection("this_user_favorites").document(book.getId()).delete()
                .addOnSuccessListener((successListener)->{
                    listener.onComplete();
                })
                .addOnFailureListener((e)->{
                    Log.w("TAG", "Error adding document", e);
                });


    }



}
