package com.example.appserver;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appserver.Holder.MenuHolder;
import com.example.appserver.control.Control;
import com.example.appserver.model.Product_Type;
import com.example.appserver.view.ItemClickedListener;
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

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    TextView name;

    RecyclerView recyclerView;
    FirebaseRecyclerOptions<Product_Type> options;
    FirebaseRecyclerAdapter<Product_Type, MenuHolder> adapter;
    FirebaseDatabase db;
    DatabaseReference product;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Menu");
        setSupportActionBar(toolbar);

        //firebase category
        db = FirebaseDatabase.getInstance();
        product = db.getReference("Product_Type");
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HomeActivity.this, "Add new items", Toast.LENGTH_SHORT).show();
                showDialog();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        name = header.findViewById(R.id.viewName);
        name.setText(Control.currentUser.getName());

        //get menu
        recyclerView = findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true);


        options = new FirebaseRecyclerOptions.Builder<Product_Type>().setQuery(product, Product_Type.class).build();
        adapter = new FirebaseRecyclerAdapter<Product_Type, MenuHolder>(options){

            @Override
            protected void onBindViewHolder(@NonNull MenuHolder holder, int position, @NonNull Product_Type model) {

                Picasso.get().load(model.getImage()).into(holder.fdImage, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(getApplicationContext(),"Could not get message", Toast.LENGTH_LONG).show();

                    }
                });
                final Product_Type clicked = model;
                holder.setItemClickListener(new ItemClickedListener() {
                    @Override
                    public void onClick(View v, int pos, boolean isLongClicked) {

//                        Toast.makeText(HomeActivity.this, "" + clicked.getName(), Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(HomeActivity.this, MenuListActivity.class);
//                        intent.putExtra("Product_TypeId", adapter.getRef(pos).getKey());
//                        startActivity(intent);
                    }
                });
                holder.fdName.setText(model.getName());
            }
            @NonNull
            @Override
            public MenuHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View v = (View) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.menu_item,viewGroup,false);
                return new MenuHolder(v);
            }


        };

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter.startListening();
        recyclerView.setAdapter(adapter);


    }


    EditText title;
    Button buttonupload, buttonselect;
    Product_Type newProduct;
    Uri saveURL;
    private final int PICK_IMAGE_REQUEST = 71;


    private void showDialog() {
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.add_item_layout, null);

        title = view.findViewById(R.id.name);
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

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeActivity.this);
        alertDialog.setTitle("Add new product");
        alertDialog.setMessage("Enter information");
        alertDialog.setView(view);
        alertDialog.setIcon(R.drawable.ic_add_black_24dp);
        alertDialog.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if(newProduct != null){
                    product.push().setValue(newProduct);
                    Toast.makeText(HomeActivity.this, "New product" + newProduct.getName() + "created", Toast.LENGTH_SHORT).show();
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

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select photo"), PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data!= null
        && data.getData()!= null){
            saveURL = data.getData();
            buttonselect.setText("Selected");

        }
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
                    Toast.makeText(HomeActivity.this, "Upload completely", Toast.LENGTH_SHORT).show();
                    folder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            newProduct = new Product_Type(title.getText().toString(), uri.toString());
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(HomeActivity.this, "Upload failed", Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded" + progress + "%");

                }
            });



        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_history) {
//            Intent intent = new Intent(HomeActivity.this, OrderPlacedActivity.class);
//            startActivity(intent);

        } else if (id == R.id.nav_menu) {
            Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_about) {
//            Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
//            startActivity(intent);
        }
        else if (id == R.id.nav_signout) {
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
