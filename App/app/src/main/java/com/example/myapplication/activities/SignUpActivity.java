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

public class SignUpActivity extends AppCompatActivity {

    EditText name, phone, email, password;
    Button Registerbtn;
    TextView Loginbtn;

    //firebase authentication
    FirebaseAuth mAuth;
    FirebaseDatabase mDatabaseRef;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalHelper.onAttach(newBase, "en"));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name = findViewById(R.id.editName);
        phone = findViewById(R.id.editPhone);
        email = findViewById(R.id.editEmail);
        password = findViewById(R.id.editPassword);
        Registerbtn = findViewById(R.id.buttonRegister);
        Loginbtn = findViewById(R.id.login);


        mDatabaseRef = FirebaseDatabase.getInstance();
        final DatabaseReference userTable = mDatabaseRef.getReference("Users");
        mAuth = FirebaseAuth.getInstance();

        //login button onclick
        Loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(i);

            }
        });

        Paper.init(this);
        String lang = Paper.book().read("language");
        if(lang == null){
            Paper.book().write("language", "en");
        }
        updateLanguage((String)Paper.book().read("language"));


        //register button onclick
        Registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (name.getText().toString().isEmpty()) {
                    Toast.makeText(SignUpActivity.this, getText(R.string.fname_input), Toast.LENGTH_SHORT).show();
                    return;
                } else if (email.getText().toString().isEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                    Toast.makeText(SignUpActivity.this, getResources().getString(R.string.validEmail), Toast.LENGTH_SHORT).show();
                    return;
                }else if (phone.getText().toString().isEmpty()) {
                    Toast.makeText(SignUpActivity.this, getResources().getString(R.string.phone), Toast.LENGTH_SHORT).show();
                    return;
                } else if (phone.length() < 10) {
                    Toast.makeText(SignUpActivity.this, getResources().getString(R.string.phone), Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (password.getText().toString().isEmpty()) {
                    Toast.makeText(SignUpActivity.this, getResources().getString(R.string.password), Toast.LENGTH_SHORT).show();
                    return;
                } else if(password.length() < 6){
                    Toast.makeText(SignUpActivity.this, getResources().getString(R.string.passAuth), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (Control.checkConnectivity(getBaseContext())) {
                    userTable.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot data: dataSnapshot.getChildren()) {
                                if (dataSnapshot.child(phone.getText().toString()).exists()) {
                                    Toast.makeText(SignUpActivity.this, getResources().getString(R.string.Exist), Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();

                                } else {

                                    User user = new User(name.getText().toString(), email.getText().toString(), password.getText().toString());
                                    userTable.child(phone.getText().toString()).setValue(user);
                                    Toast.makeText(SignUpActivity.this, getResources().getString(R.string.success), Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(SignUpActivity.this, ScanActivity.class);
                                    startActivity(intent);
                                    finish();

                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                } else {
                    Toast.makeText(SignUpActivity.this, getResources().getString(R.string.interCon), Toast.LENGTH_SHORT).show();
                    return;
                }


            }

        });
    }

    private void updateLanguage(String language) {
        Context context = LocalHelper.setLocale(this, language);
        Resources resources = context.getResources();

        Registerbtn.setText(resources.getString(R.string.btnSignup));
        name.setHint(resources.getString(R.string.fname_input));
        phone.setHint(resources.getString(R.string.phone));
        email.setHint(resources.getString(R.string.email));
        password.setHint(resources.getString(R.string.password));
        Loginbtn.setText(resources.getString(R.string.login));
    }
}