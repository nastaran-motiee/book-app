package com.example.sellybook.model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
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


    private ModelFireBase(){}

    public static void addBook(Book book, Model.AddBookListener listener) {

       DocumentReference ref = ModelFireBase.instance.db.collection("books").document();

       ref.set(book.toJason())
               .addOnSuccessListener((successListener)->{
                   book.setId(ref.toString());
                   ModelFireBase.instance.db.collection("books").document(book.getId()).update("Id", book.getId());
                   listener.onComplete();
               })

               .addOnFailureListener((e)->{
                   Log.w("TAG", "Error adding document", e);

               });
       //ModelFireBase.instance.db.collection("books")
       //       .document(book.getId()).set(book.toJason())
       //       .addOnSuccessListener((successListener)->{
       //           listener.onComplete();
       //       })

       //       .addOnFailureListener((e)->{
       //           Log.w("TAG", "Error adding document", e);

       //       });


        //TODO:)))))))))))))))


    }

    public FirebaseAuth getFireBaseAuthInstance() {
         return FirebaseAuth.getInstance();
    }

    //TODO: implement get all books since
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

    public void saveImage(Bitmap bitmap, String id, Model.SaveImageListener listener) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child("bookImage/" + id + ".jpg");

        // Get the data from an ImageView as bytes
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
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

    private void listenToBooks(){





    }

}
