package com.example.sellybook;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.sellybook.model.Book;
import com.example.sellybook.model.Model;
import com.example.sellybook.model.ModelFireBase;

import java.util.List;

public class UserUploadedBooksFragmentViewModel extends ViewModel {
    LiveData<List<Book>> data = Model.instance.getAllUserUploaded();


    public LiveData<List<Book>> getData() {
        return data;
    }


}

