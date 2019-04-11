package com.example.myapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.control.Control;
import com.example.myapplication.helper.LocalHelper;
import com.example.myapplication.holder.ProductHolder;
import com.example.myapplication.model.Product_Type;
import com.example.myapplication.model.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {


    TextView email, name, phone;
    Button delProfile, updPass;

    AlertDialog.Builder alertDialog;
    EditText edtPass, newPass, repPass;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalHelper.onAttach(newBase, "en"));
    }
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_app_bar_layout);
        View view =getSupportActionBar().getCustomView();

        ImageButton imageButton= (ImageButton)view.findViewById(R.id.action_bar_back);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             onBackPressed();
            }
        });


        email = findViewById(R.id.editEmail);
        name = findViewById(R.id.editName);
        phone = findViewById(R.id.editPhone);
        name.setText(Control.currentUser.getName());
        email.setText(Control.currentUser.getEmail());
        phone.setText(Control.currentUser.getPhone());

        delProfile = findViewById(R.id.delProfile);
        delProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                final FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                currentUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ProfileActivity.this, getResources().getString(R.string.success), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(ProfileActivity.this, getResources().getString(R.string.unsuccess), Toast.LENGTH_SHORT).show();

                        }
                    }
                });

            }
        });


        updPass = findViewById(R.id.updtPass);
        updPass.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
              showDialogPass();
            }

        });



    }

    private void showDialogPass() {
        alertDialog = new AlertDialog.Builder(ProfileActivity.this);
        // alertDialog.setTitle("Change Password");
        alertDialog.setMessage("Please fill all information");
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.change_password_layout,null);

        newPass = view.findViewById(R.id.newPass);
        edtPass = view.findViewById(R.id.edtPass);
        repPass = view.findViewById(R.id.repPass);

        alertDialog.setView(view);

        alertDialog.setPositiveButton("Change", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(edtPass.getText().toString().isEmpty()){
                    Toast.makeText(ProfileActivity.this, "password field is empty", Toast.LENGTH_SHORT).show();
                }
                if(edtPass.getText().toString().equals(Control.currentUser.getPassword())){
                    if(newPass.getText().toString().equals(repPass.getText().toString())){
                        Map<String, Object> passwordUpdt = new HashMap<>();
                        passwordUpdt.put("password", edtPass.getText().toString());

                        DatabaseReference user = FirebaseDatabase.getInstance().getReference("User");
                        user.child(Control.currentUser.getPhone()).updateChildren(passwordUpdt)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(ProfileActivity.this, "Password Updated", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ProfileActivity.this, getResources().getString(R.string.unsuccess), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else {
                        Toast.makeText(ProfileActivity.this, "New password does not match", Toast.LENGTH_SHORT).show();

                    }
                }
                else{
                    Toast.makeText(ProfileActivity.this, "Wrong password", Toast.LENGTH_SHORT).show();
                }


                }

            });


            alertDialog.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();

                }
            });

            alertDialog.show();
    }


}
