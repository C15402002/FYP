package com.example.appserver.activites;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.appserver.holder.MenuHolder;
import com.example.appserver.R;
import com.example.appserver.control.Control;
import com.example.appserver.model.Menu;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class MenuListActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    FirebaseDatabase db;
    DatabaseReference menuRef;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    FloatingActionButton fab ;
    FirebaseRecyclerOptions<Menu> options;
    FirebaseRecyclerAdapter<Menu, MenuHolder> adapter;

    String menuId = "";
    EditText title, descript, price;
    Button buttonupload, buttonselect;
    Menu newMenu;
    Uri saveURL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_list);

        db = FirebaseDatabase.getInstance();
        menuRef = db.getReference("Restaurant").child(Control.currentUser.getRestId()).child("details").child("Menu");
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        recyclerView = findViewById(R.id.recycleViewFood);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_app_bar_layout);
        View view =getSupportActionBar().getCustomView();

        ImageButton imageButton= (ImageButton) findViewById(R.id.action_bar_back);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MenuListActivity.this, "Add new items", Toast.LENGTH_SHORT).show();
                showDialog();
            }
        });

        if(getIntent() != null){
            menuId = getIntent().getStringExtra("Product_TypeId");

        } if(!menuId.isEmpty()){
            loadMenuItems(menuId);
        }

    }

    private void showDialog() {
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.add_new_menu_layout, null);
        title = view.findViewById(R.id.name);
        descript = view.findViewById(R.id.descript);
        price = view.findViewById(R.id.price);
        buttonupload = view.findViewById(R.id.buttonUpload);

        buttonupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();

            }
        });

        buttonselect = view.findViewById(R.id.buttonSelect);
        buttonselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MenuListActivity.this);
        alertDialog.setTitle("Add new " +
                "" +
                "menu items");
        alertDialog.setMessage("Enter information");
        alertDialog.setView(view);
        alertDialog.setIcon(R.drawable.ic_add_black_24dp);
        alertDialog.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if(newMenu != null){
                    menuRef.push().setValue(newMenu);
                    Toast.makeText(MenuListActivity.this, "New item" + newMenu.getName() + "created", Toast.LENGTH_SHORT).show();
                }
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alertDialog.show();
    }
    private void loadMenuItems(String menuId) {

        options = new FirebaseRecyclerOptions.Builder<Menu>().setQuery(menuRef.orderByChild("foodId").equalTo(menuId), Menu.class).build();
        adapter = new FirebaseRecyclerAdapter<Menu, MenuHolder>(options){

            @Override
            protected void onBindViewHolder(@NonNull MenuHolder holder, int position, @NonNull Menu model) {
                holder.fdName.setText(model.getName());
                holder.fdDescript.setText(model.getDescription());
                holder.fdPrice.setText(model.getPrice());
                Picasso.get().load(model.getImage()).into(holder.fdImage, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(getApplicationContext(),"Could not get message", Toast.LENGTH_LONG).show();

                    }
                });
            }
            @NonNull
            @Override
            public MenuHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View v = (View) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.menu_item,viewGroup,false);
                return new MenuHolder(v);
            }

        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select photo"), Control.PICK_IMAGE_REQUEST);

    }
    private void uploadImage() {
        if(saveURL != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("loading...");
            progressDialog.show();
            String imageName = UUID.randomUUID().toString();
            final StorageReference folder = storageReference.child("images/" +imageName);
            folder.putFile(saveURL).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(MenuListActivity.this, "Upload completely", Toast.LENGTH_SHORT).show();
                    folder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            newMenu = new Menu();
                            newMenu.setName(title.getText().toString());
                            newMenu.setDescription(descript.getText().toString());
                            newMenu.setPrice(price.getText().toString());
                            newMenu.setFoodId(menuId);
                            newMenu.setImage(uri.toString());
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(MenuListActivity.this, "Upload failed", Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded" + progress + "%");

                }
            });



        }else {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Control.PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data!= null
                && data.getData()!= null){
            saveURL = data.getData();
            buttonselect.setText("Selected");

        }
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle().equals(Control.update)){
            showUpdateDialog(adapter.getRef(item.getOrder()).getKey(), adapter.getItem(item.getOrder()));
        } else if(item.getTitle().equals(Control.delete)){
            showDeleteDialog(adapter.getRef(item.getOrder()).getKey(), adapter.getItem(item.getOrder()));
        }
        return super.onContextItemSelected(item);
    }
    private void showUpdateDialog(final String key, final Menu item) {
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.add_new_menu_layout, null);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MenuListActivity.this);
        alertDialog.setTitle("Edit product");
        alertDialog.setMessage("Enter information");
        alertDialog.setView(view);
        title = view.findViewById(R.id.name);
        descript = view.findViewById(R.id.descript);
        price = view.findViewById(R.id.price);
        buttonupload = view.findViewById(R.id.buttonUpload);
        buttonselect = view.findViewById(R.id.buttonSelect);

        title.setText(item.getName());
        descript.setText(item.getDescription());
        price.setText(item.getPrice());

        buttonupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadNewImage(item);

            }
        });

        buttonselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        alertDialog.setIcon(R.drawable.ic_add_black_24dp);
        alertDialog.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if(newMenu != null){
                    item.setName(title.getText().toString());
                    item.setName(descript.getText().toString());
                    item.setName(price.getText().toString());

                    menuRef.child(key).setValue(item);

                }

            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alertDialog.show();
    }


    private void showDeleteDialog(String key, Menu item) {

        menuRef.child(key).removeValue();

        Toast.makeText(this, "Item removed!", Toast.LENGTH_SHORT).show();
    }

    private void uploadNewImage(final Menu item) {
        if(saveURL != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("loading...");
            progressDialog.show();
            String imageName = UUID.randomUUID().toString();
            final StorageReference folder = storageReference.child("images/" +imageName);
            folder.putFile(saveURL).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(MenuListActivity.this, "Upload completely", Toast.LENGTH_SHORT).show();
                    folder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            item.setImage(uri.toString());

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(MenuListActivity.this, "Upload failed", Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded" + progress + "%");

                }
            });



        }else {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(adapter!=null){
            adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(adapter!=null){
            adapter.stopListening();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(adapter!=null){
            adapter.startListening();
        }
    }



}
