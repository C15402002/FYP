     public void onClick(View v) {
                userTable.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(email.getText().toString()).exists()) {
                            User user = dataSnapshot.child(email.getText().toString()).getValue(User.class);
                            if (user.getPassword_input().equals(password.getText().toString())) {
                                Toast.makeText(LoginActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                Control.currentUser = user;
                                startActivity(intent);
                                finish();

                            } else {
                                Toast.makeText(LoginActivity.this, "unsuccessful", Toast.LENGTH_SHORT).show();

                            }
                        }
                        else{
                            Toast.makeText(LoginActivity.this, "user doe not exist", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });