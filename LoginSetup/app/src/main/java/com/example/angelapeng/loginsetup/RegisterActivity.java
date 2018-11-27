package com.example.angelapeng.loginsetup;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    Button signupBtn;
    EditText fname, sname, user_email, pass, confPass;
    TextInputLayout fnamewrapper, snamewrapper, emailwrapper, passwordwrapper, confPasswrapper;

    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();

        fname = findViewById(R.id.userFirstname);
        sname =  findViewById(R.id.userSurname);
        user_email = findViewById(R.id.userEmail);
        pass =  findViewById(R.id.userPassword);
        confPass = findViewById(R.id.confirmPass);

        signupBtn =  findViewById(R.id.btnUserSignup);

        fnamewrapper = findViewById(R.id.fname_wrap);
        snamewrapper =  findViewById(R.id.sname_wrap);
        emailwrapper =  findViewById(R.id.email_wrapper);
        passwordwrapper =  findViewById(R.id.password_wrapper);
        confPasswrapper =  findViewById(R.id.confirm_pass_w);

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(auth.getCurrentUser() != null){
                    //user logged in will direct to another activity


                } else {
                    final String firstname = fname.getText().toString().trim();
                    final String surname = sname.getText().toString().trim();
                    final String email = user_email.getText().toString().trim();
                    String password = pass.getText().toString().trim();
                    String cpass = confPass.getText().toString().trim();

                    if (firstname.isEmpty()) {
                        fnamewrapper.setError("Enter first name ");
                        fnamewrapper.requestFocus();
                        return;
                    }
                    if (surname.isEmpty()) {
                        snamewrapper.setError("Enter last name");
                        snamewrapper.requestFocus();
                        return;
                    }
                    if (email.isEmpty()) {
                        emailwrapper.setError("Enter email");
                        emailwrapper.requestFocus();
                        return;
                    }
                    if (password.isEmpty()) {
                        passwordwrapper.setError("Enter password");
                        passwordwrapper.requestFocus();
                        return;
                    }
                    if (cpass.isEmpty()) {
                        confPasswrapper.setError("Enter confirm password");
                        confPasswrapper.requestFocus();
                        return;
                    }
                    if (password != cpass) {
                        confPasswrapper.setError("password does not match");
                        confPasswrapper.requestFocus();
                        return;
                    }
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //adds data to database
                                User user = new User(firstname,surname,email);
                                FirebaseDatabase.getInstance().getReference("Users")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(RegisterActivity.this, "User created", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                        } else{
                                            Toast.makeText(RegisterActivity.this,task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                        }

                                    }
                                });


                            }else{
                                Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }

                        }
                    });
                }



            }
        });


    }
}
