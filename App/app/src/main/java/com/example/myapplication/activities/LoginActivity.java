package com.example.myapplication.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import io.paperdb.Paper;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.control.Control;
import com.example.myapplication.helper.LocalHelper;
import com.example.myapplication.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    EditText phone_input, password_input;
    Button loginbtn;
    TextView registerbtn;

    FirebaseAuth mAuth;
    FirebaseDatabase mDatabaseRef;

    ProgressDialog progressDialog;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalHelper.onAttach(newBase, "en"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginbtn = findViewById(R.id.buttonLogin);
        registerbtn = findViewById(R.id.signUp);
        progressDialog = new ProgressDialog(LoginActivity.this);

        mDatabaseRef = FirebaseDatabase.getInstance();
        final DatabaseReference userTable = mDatabaseRef.getReference("Users");
        mAuth = FirebaseAuth.getInstance();
        phone_input =  findViewById(R.id.editPhone);
        password_input =  findViewById(R.id.editPassword);
        Paper.init(this);
        String lang = Paper.book().read("language");
        if(lang == null){
            Paper.book().write("language", "en");
        }
        updateLanguage((String)Paper.book().read("language"));


        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Control.checkConnectivity(getBaseContext())) {
                    progressDialog.setMessage(getResources().getString(R.string.loggingin));
                    progressDialog.show();


                    if (phone_input.getText().toString().isEmpty()) {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.phone), Toast.LENGTH_SHORT).show();
                        return;
                    } else if (password_input.getText().toString().isEmpty()) {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.password), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    userTable.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                           if (dataSnapshot.child(phone_input.getText().toString()).exists()) {

                                User user = dataSnapshot.child(phone_input.getText().toString()).getValue(User.class);
                                user.setPhone(phone_input.getText().toString());

                                if (!Boolean.parseBoolean(user.getIsStaff())) {
                                    if (user.getPassword().equals(password_input.getText().toString())) {
                                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.success), Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(LoginActivity.this, ScanActivity.class);
                                        Control.currentUser = user;
                                        startActivity(intent);
                                        finish();
                                        userTable.removeEventListener(this);


                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.unsuccess), Toast.LENGTH_SHORT).show();

                                    }
                                    progressDialog.dismiss();
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.errorLog), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, getResources().getString(R.string.notExist), Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }else {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.interCon), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    private void updateLanguage(String language) {
        Context context = LocalHelper.setLocale(this, language);
        Resources resources = context.getResources();

        loginbtn.setText(resources.getString(R.string.btnLogin));
        registerbtn.setText(resources.getString(R.string.signup));
       // phone_input.setText(resources.getString(R.string.phone));
       // password_input.setText(resources.getString(R.string.password));
    }

}
