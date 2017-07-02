package com.example.alex.safesharing;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ContactActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private Button btnCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final EditText etMessage = (EditText) findViewById(R.id.etMessage);
        Button btnSendMessage = (Button) findViewById(R.id.btnSendMessage);
        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //te trimite in pagina "My files" si da Toast cu "Mesaj trimis, Raspunsul il veti primi pe mail"

                String id = mAuth.getCurrentUser().getUid().toString();
                try {
                    mDatabase.child("Messages").child(id).child("message").setValue(etMessage.getText().toString());
                    Intent intent = new Intent(ContactActivity.this, FilesActivity.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "Message sent. You will be contacted by e-mail.", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Error! Try again!", Toast.LENGTH_SHORT).show();
                }


            }
        });
        btnCall = (Button) findViewById(R.id.btnCall);
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:0735659566"));
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                startActivity(intent);
            }
        });
        BottomNavigationView bottomNavigationView=(BottomNavigationView)findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_help:{
                        Intent intent=new Intent(ContactActivity.this,HelpActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.action_add:{

                        Intent intent=new Intent(ContactActivity.this,AddFileActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.action_signout:{
                        AlertDialog.Builder builder=new AlertDialog.Builder(ContactActivity.this);
                        builder.setMessage("Are you sure do you want to sign out?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        mAuth.signOut();
                                        finish();
                                        Intent intent=new Intent(ContactActivity.this,LogInActivity.class);
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.files: {
                Intent intent = new Intent(ContactActivity.this, FilesActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.mainFolder: {
                Intent intent = new Intent(ContactActivity.this, SetFolderActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.history: {
                Intent intent=new Intent(ContactActivity.this,HistoryActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.help: {
                Intent intent = new Intent(ContactActivity.this, HelpActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.contact: {
                Intent intent=new Intent(ContactActivity.this,ContactActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.signout:
                AlertDialog.Builder builder=new AlertDialog.Builder(ContactActivity.this);
                builder.setMessage("Are you sure do you want to sign out?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mAuth.signOut();
                                finish();
                                Intent intent=new Intent(ContactActivity.this,LogInActivity.class);
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
