package com.example.sellybook.ui;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.sellybook.R;
import com.example.sellybook.model.Book;
import com.example.sellybook.model.Model;
import com.example.sellybook.ui.alerts.MissigNameAlertDialog;
import com.example.sellybook.ui.alerts.MissingPriceAlertDialog;

public class AddBookFragment extends Fragment {

    View view;
    ImageView selectImage;
    Button cancelButton;
    Button saveButton;
    String name;
    String id;
    String phone;
    String address;
    boolean cb;
    ProgressBar progressBar;

    MissigNameAlertDialog missigNameAlertDialog;
    MissingPriceAlertDialog missingIdAlertDialog;
    public AddBookFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_book, container, false);
        //TODO:************************************
        //selectImage = view.findViewById(R.id.add_book_image_view);



       //selectImage.setOnClickListener(new View.OnClickListener() {
       //    @Override
       //    public void onClick(View v) {
       //        mGetContent.launch("image/*");
       //    }
       //});

        //TODO:************************************


        cancelButton = view.findViewById(R.id.add_book_cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).popBackStack(R.id.allBooksFragment,false);

            }
        });

        saveButton = view.findViewById(R.id.add_book_save_button);
        missigNameAlertDialog = new MissigNameAlertDialog();
        missingIdAlertDialog = new MissingPriceAlertDialog();
        progressBar = view.findViewById(R.id.add_book_progress_bar);
        progressBar.setVisibility(View.GONE);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                saveButton.setEnabled(false);
                cancelButton.setEnabled(false);
                name= ((EditText)view.findViewById(R.id.add_name_et)).getText().toString();
                id = ((EditText)view.findViewById(R.id.add_price_et)).getText().toString();
                phone = ((EditText)view.findViewById(R.id.add_phone_et)).getText().toString();
                address = ((EditText)view.findViewById(R.id.add_address_et)).getText().toString();
                cb = ((CheckBox)view.findViewById(R.id.add_book_cb)).isChecked();




                if(name.equals("")){
                    missigNameAlertDialog.show(getParentFragmentManager(),"MissingNameAlertDialog");
                    return;
                }else if(id.equals("")){
                    missingIdAlertDialog.show(getParentFragmentManager(),"MissingIdAlertDialog");
                    return;
                }
                Book bk =  new Book(name,id,phone,address,cb,selectImage);
                Model.instance.addBook(bk,()->{
                    Navigation.findNavController(view).popBackStack(R.id.allBooksFragment,false);

                });
            }

        });



        return view;

    }

    // GetContent creates an ActivityResultLauncher<String> to allow you to pass
// in the mime type you'd like to allow the user to select
    //TODO:*********************************************
  //ctivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
  //       new ActivityResultCallback<Uri>() {
  //           @Override
  //           public void onActivityResult(Uri uri) {
  //               // Handle the returned Uri
  //               selectImage.setImageURI(uri);


  //           }
  //       });
    //TODO:*********************************************

}