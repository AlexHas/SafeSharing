package com.example.alex.safesharing;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class SetFolderActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private static final int REQUEST_PATH = 1;
    private EditText etFolder;
    private Button btnBrowse;
    private Button btnSave;
    private TextView tvMainFolder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_folder);
        mAuth=FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference();
        etFolder=(EditText)findViewById(R.id.etFolder);
        etFolder.setKeyListener(null);
        btnBrowse=(Button)findViewById(R.id.btnBrowse);
        btnSave=(Button)findViewById(R.id.btnSave);
        tvMainFolder=(TextView)findViewById(R.id.tvMainFolder);
        final String id=mAuth.getCurrentUser().getUid();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String mainFolder =(String)  dataSnapshot.child("Users").child(id).child("mainFolder").getValue();
                if(mainFolder.equals("defaultFolder")){
                    tvMainFolder.setText("You don't set a folder yet.");
                }
                else{
                    tvMainFolder.setText("Your current folder is: " + mainFolder + ".");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etFolder.getText().length()!=0) {
                    String id = mAuth.getCurrentUser().getUid();
                    mDatabase.child("Users").child(id).child("mainFolder").setValue(etFolder.getText().toString());
                    //mDatabase.child("Users").child(task.getResult().getUser().getUid()).child("email").setValue(etEmail.getText().toString());
                    String toast=etFolder.getText().toString()+" was set as main foder!";
                    Toast.makeText(getApplicationContext(),toast, Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(SetFolderActivity.this, FilesActivity.class);
                    startActivity(intent);
                }
            }
        });
        BottomNavigationView bottomNavigationView=(BottomNavigationView)findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_help:{
                        Intent intent=new Intent(SetFolderActivity.this,HelpActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.action_add:{

                        Intent intent=new Intent(SetFolderActivity.this,AddFileActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.action_signout:{
                        AlertDialog.Builder builder=new AlertDialog.Builder(SetFolderActivity.this);
                        builder.setMessage("Are you sure do you want to sign out?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        mAuth.signOut();
                                        finish();
                                        Intent intent=new Intent(SetFolderActivity.this,LogInActivity.class);
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
    }
    public void getfile(View view){
        Intent intent1 = new Intent(this, FolderChooser.class);
        startActivityForResult(intent1,REQUEST_PATH);
    }
    // Listen for results.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        // See which child activity is calling us back.

            if (resultCode == -1) {

                etFolder.setText(data.getStringExtra("FolderPath"));
                //tvMainFolder.setText("");
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
                Intent intent = new Intent(SetFolderActivity.this, FilesActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.mainFolder: {
                Intent intent = new Intent(SetFolderActivity.this, SetFolderActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.history: {
                Intent intent=new Intent(SetFolderActivity.this,HistoryActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.help: {
                Intent intent = new Intent(SetFolderActivity.this, HelpActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.contact: {
                Intent intent=new Intent(SetFolderActivity.this,ContactActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.signout:
                AlertDialog.Builder builder=new AlertDialog.Builder(SetFolderActivity.this);
                builder.setMessage("Are you sure do you want to sign out?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mAuth.signOut();
                                finish();
                                Intent intent=new Intent(SetFolderActivity.this,LogInActivity.class);
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
