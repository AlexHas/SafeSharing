package com.example.alex.safesharing;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FilesActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    static DatabaseReference mDatabase;
    static List<Fisier> files;
    static FisierListAdapter adapterLista;
    static StorageReference storageReference;
    private ListView lvFiles;
    static ProgressDialog progressDialog;
    private TextView tvTotalSpace;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_files);
        storageReference= FirebaseStorage.getInstance().getReference();
        progressDialog=new ProgressDialog(this);
        mDatabase= FirebaseDatabase.getInstance().getReference();
        mAuth=FirebaseAuth.getInstance();
        tvTotalSpace=(TextView)findViewById(R.id.tvTotalSpace);

        lvFiles=(ListView) findViewById(R.id.lvFiles);
        files=new ArrayList<Fisier>();
        final String id=mAuth.getCurrentUser().getUid();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String mainFolder =(String)  dataSnapshot.child("Users").child(id).child("mainFolder").getValue();
                Intent intent=new Intent(FilesActivity.this, ServiceMonitor.class);
                intent.putExtra("mainFolder", mainFolder);
                startService(intent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabase.child("Users").child(id).child("Files").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot fisier: dataSnapshot.getChildren()){
                    String loc=fisier.child("location").getValue(String.class);
                    String nam=fisier.child("name").getValue(String.class);
                    String siz=fisier.child("size").getValue(String.class);
                    Fisier f=new Fisier(nam, loc, siz);

                    files.add(f);
                }

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        BottomNavigationView bottomNavigationView=(BottomNavigationView)findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_help:{
                        Intent intent=new Intent(FilesActivity.this,HelpActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.action_add:{

                        Intent intent=new Intent(FilesActivity.this, AddFileActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.action_signout:{
                        AlertDialog.Builder builder=new AlertDialog.Builder(FilesActivity.this);
                        builder.setMessage("Are you sure do you want to sign out?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        mAuth.signOut();
                                        finish();
                                        Intent intent=new Intent(FilesActivity.this,LogInActivity.class);
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
                    }
                }
                return true;
            }
        });
        adapterLista=new FisierListAdapter(this,files, id);
        lvFiles.setAdapter(adapterLista);
        double space=adapterLista.getTotalSpace();
        tvTotalSpace.setText("Total space used: " + ((Double) space).toString() + " MB.");
        //calculateTotalSpace();
    }

//    public void calculateTotalSpace(){
//        double space=0.00;
//        for (Fisier f : files) {
//                double s = Double.parseDouble(f.getSize().substring(0, f.getSize().length() - 3));
//                space += s;
//                //Toast.makeText(getApplicationContext(),f.getSize(),Toast.LENGTH_LONG).show();
//            }
//
//        tvTotalSpace.setText("Total space used: " + ((Double) space).toString() + " MB.");
//
//
//    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
//            case R.id.files: {
//                Intent intent = new Intent(FilesActivity.this, FilesActivity.class);
//                startActivity(intent);
//                break;
//            }
            case R.id.mainFolder: {
                Intent intent = new Intent(FilesActivity.this, SetFolderActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.history: {
                Intent intent=new Intent(FilesActivity.this,HistoryActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.help: {
                Intent intent = new Intent(FilesActivity.this, HelpActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.contact: {
                Intent intent=new Intent(FilesActivity.this,ContactActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.signout:
                AlertDialog.Builder builder=new AlertDialog.Builder(FilesActivity.this);
                builder.setMessage("Are you sure do you want to sign out?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mAuth.signOut();
                                finish();
                                Intent intent=new Intent(FilesActivity.this,LogInActivity.class);
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
