package com.example.sellybook.ui;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.graphics.BlendMode;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.hardware.camera2.params.BlackLevelPattern;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.sellybook.R;

public class MainActivity extends AppCompatActivity {
    NavController navController;
    NavHostFragment navHostFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //change action bar text color
        Toolbar toolbar = (Toolbar) findViewById(R.id.action_bar);
        toolbar.setTitleTextColor(Color.rgb(38,34,64));





        /**
         * change the AppBar label to the label of the fragment
         */
        /*When creating the NavHostFragment using FragmentContainerView
         or if manually adding the NavHostFragment to your activity
        via a FragmentTransaction, attempting to retrieve the NavController
        in onCreate() of an Activity via Navigation.findNavController(Activity, @IdRes int)
        will fail. You should retrieve the NavController directly from the NavHostFragment instead.*/
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.main_navhost);
        navController = navHostFragment.getNavController();
        NavigationUI.setupActionBarWithNavController(this,navController);

    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            //go back

            navController.navigateUp();
        }else{
            return NavigationUI.onNavDestinationSelected(item,navController);
        }
        return true;
    }



}