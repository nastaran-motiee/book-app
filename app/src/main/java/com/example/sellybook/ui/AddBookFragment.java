package com.example.sellybook.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.sellybook.R;
import com.example.sellybook.model.Book;
import com.example.sellybook.model.Model;
import com.example.sellybook.ui.alerts.MissigNameAlertDialog;
import com.example.sellybook.ui.alerts.MissingImageAlertDialog;
import com.example.sellybook.ui.alerts.MissingPriceAlertDialog;

public class AddBookFragment extends Fragment {

    static final int REQUEST_IMAGE_CAPTURE =1;
    View view;
    ImageView selectImage;
    Button cancelButton;
    Button saveButton;
    String name;
    String price;
    String phone;
    String address;
    boolean cb;
    ProgressBar progressBar;
    ImageButton cameraBtn;
    ActivityResultLauncher<Intent> activityResultLauncher;

    MissigNameAlertDialog missigNameAlertDialog;
    MissingPriceAlertDialog missingIdAlertDialog;
    MissingImageAlertDialog missingImageAlertDialog;
    Bitmap bitmap;
    ImageView bookImage;
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
        missingImageAlertDialog = new MissingImageAlertDialog();
        progressBar = view.findViewById(R.id.add_book_progress_bar);
        progressBar.setVisibility(View.GONE);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                name= ((EditText)view.findViewById(R.id.add_name_et)).getText().toString();
                price = ((EditText)view.findViewById(R.id.add_price_et)).getText().toString();
                phone = ((EditText)view.findViewById(R.id.add_phone_et)).getText().toString();
                address = ((EditText)view.findViewById(R.id.add_address_et)).getText().toString();
                cb = ((CheckBox)view.findViewById(R.id.add_book_cb)).isChecked();
                if(name.equals("")){
                    missigNameAlertDialog.show(getParentFragmentManager(),"MissingNameAlertDialog");
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }else if(price.equals("")){
                    missingIdAlertDialog.show(getParentFragmentManager(),"MissingPriceAlertDialog");
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }

                Book bk = new Book(name,price,phone,address,cb);
//TODO:_________________________________
                Model.instance.saveImage(bitmap,bk.getId(), url -> {
                    bk.setBookImageURL(url);
                    Model.instance.addBook(bk,()->{
                        //Navigation.findNavController(view).navigateUp();
                        Navigation.findNavController(view).popBackStack(R.id.allBooksFragment,false);
                        //Navigation.findNavController(view).navigate(R.id.action_global_allBooksFragment);
                    });
                });
                saveButton.setEnabled(false);
                cancelButton.setEnabled(false);
            }

        });

        bookImage = view.findViewById(R.id.add_book_image_view);
        cameraBtn = view.findViewById(R.id.add__book_camera_btn);
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == getActivity().RESULT_OK && result.getData() != null){
                    Bundle bundle = result.getData().getExtras();
                    bitmap = (Bitmap) bundle.get("data");
                    bookImage.setImageBitmap(bitmap);
                }
            }
        });
                cameraBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            //  getActivity().startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                            activityResultLauncher.launch(intent);

                    }
                });


        return view;

    }


}