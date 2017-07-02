package com.example.alex.safesharing;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class AddFileActivity extends AppCompatActivity {

    private EditText etFile;
    private static final int REQUEST_PATH = 1;
    String curFileName;
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    private EditText etName;
    private EditText etLocation;
    private EditText etExtension;
    private EditText etSize;
    private ProgressDialog progressDialog;
    private DatabaseReference mDatabase;
    private Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_file);
        mDatabase= FirebaseDatabase.getInstance().getReference();
        progressDialog=new ProgressDialog(this);
        etFile = (EditText) findViewById(R.id.etFile);
        mAuth=FirebaseAuth.getInstance();
        final String id= mAuth.getCurrentUser().getUid();
        mStorageRef= FirebaseStorage.getInstance().getReference();
        etFile.setKeyListener(null);
        etName=(EditText) findViewById(R.id.etNAme);
        etName.setKeyListener(null);
        etLocation=(EditText) findViewById(R.id.etLocation);
        etLocation.setKeyListener(null);
        etExtension=(EditText) findViewById(R.id.etExtension);
        etExtension.setKeyListener(null);
        etSize=(EditText) findViewById(R.id.etSize);
        etSize.setKeyListener(null);
        btnAdd=(Button) findViewById(R.id.btnAddFile);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etName.getText().length()==0){
                    Toast.makeText(getApplicationContext(), "First choose a file!",Toast.LENGTH_LONG).show();
                }else {
                    progressDialog.setMessage("Uploading file...");
                    progressDialog.show();
                    String path = etLocation.getText().toString();
                    String fileName = etName.getText().toString();
                    Uri file = Uri.fromFile(new File(path));
                    StorageReference riversRef = mStorageRef.child(id).child(fileName);
                    riversRef.putFile(file)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    progressDialog.hide();
                                    Toast.makeText(getApplicationContext(), "Uploaded file succesfull!", Toast.LENGTH_LONG).show();
                                    String name = etName.getText().toString();
                                    String location = etLocation.getText().toString();
                                    String size = etSize.getText().toString();
                                    Random r=new Random();
                                    String customLocation=location.replace(".","|").replace("/","|");
                                    String fileChild="file"+customLocation;
                                    mDatabase.child("Users").child(id).child("Files").child(fileChild).child("name").setValue(name);
                                    mDatabase.child("Users").child(id).child("Files").child(fileChild).child("location").setValue(location);
                                    mDatabase.child("Users").child(id).child("Files").child(fileChild).child("size").setValue(size);

                                    File fileToDelete = new File(location);
                                    fileToDelete.delete();
                                    Log.d("Activity","||||||||||||||||||||||||||||||||||||||||||||||||||||||| " + fileToDelete.getAbsolutePath()+ "}||||||||||||||||| "+ fileToDelete.getName());
                                    if(fileToDelete.exists()){
                                        try {
                                            fileToDelete.getCanonicalFile().delete();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        if(fileToDelete.exists()){
                                            getApplicationContext().deleteFile(fileToDelete.getName());
                                        }
                                    }
                                    Intent intent = new Intent(AddFileActivity.this, FilesActivity.class);
                                    startActivity(intent);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.hide();
                                    Toast.makeText(getApplicationContext(), "Uploaded file failed! Try again", Toast.LENGTH_LONG).show();
                                }
                            });
                }
            }
        });


    }
    public void getfile(View view){
        Intent intent1 = new Intent(this, FileChooser.class);
        startActivityForResult(intent1,REQUEST_PATH);
    }
    // Listen for results.
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        // See which child activity is calling us back.
        if (requestCode == REQUEST_PATH){
            if (resultCode == RESULT_OK) {

                etFile.setText(data.getStringExtra("GetFileName"));
                etName.setText(data.getStringExtra("GetFileName"));
                etLocation.setText(data.getStringExtra("GetPath"));
                etExtension.setText(data.getStringExtra("GetFileName").substring(data.getStringExtra("GetFileName").lastIndexOf(".")));
                String size=data.getStringExtra("GetSize").substring(0,data.getStringExtra("GetSize").lastIndexOf("Byte")-1);
                Float sizeFloat=Float.parseFloat(size)/1024/1024;
                etSize.setText(sizeFloat.toString()+" MB");

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.files: {
                Intent intent = new Intent(AddFileActivity.this, FilesActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.mainFolder: {
                Intent intent = new Intent(AddFileActivity.this, SetFolderActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.history: {
                Intent intent=new Intent(AddFileActivity.this,HistoryActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.help: {
                Intent intent = new Intent(AddFileActivity.this, HelpActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.contact: {
                Intent intent=new Intent(AddFileActivity.this,ContactActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.signout:
                AlertDialog.Builder builder=new AlertDialog.Builder(AddFileActivity.this);
                builder.setMessage("Are you sure do you want to sign out?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mAuth.signOut();
                                finish();
                                Intent intent=new Intent(AddFileActivity.this,LogInActivity.class);
                                startActivity(intent);
                            }

                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                AlertDialog alert=builder.create();
                alert.setTitle("Confirmation");
                alert.show();

                break;
            case R.id.exit: {
                finish();
                System.exit(0);
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }


}
