package com.example.alex.safesharing;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class HelpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        mAuth=FirebaseAuth.getInstance();
        TextView tvTitle=(TextView)findViewById(R.id.tvTitle);
        TextView tvDescription=(TextView)findViewById(R.id.tvDescription);
        BottomNavigationView bottomNavigationView=(BottomNavigationView)findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_help:{
                        Intent intent=new Intent(HelpActivity.this,HelpActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.action_add:{
                        Intent intent=new Intent(HelpActivity.this, AddFileActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.action_signout:{
                        AlertDialog.Builder builder=new AlertDialog.Builder(HelpActivity.this);
                        builder.setMessage("Are you sure do you want to sign out?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        mAuth.signOut();
                                        finish();
                                        Intent intent=new Intent(HelpActivity.this,LogInActivity.class);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.files: {
                Intent intent = new Intent(HelpActivity.this, FilesActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.mainFolder: {
                Intent intent = new Intent(HelpActivity.this, SetFolderActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.history: {
                Intent intent = new Intent(HelpActivity.this, HistoryActivity.class);
                startActivity(intent);
                break;
            }
//            case R.id.help: {
//                Intent intent = new Intent(HelpActivity.this, HelpActivity.class);
//                startActivity(intent);
//                break;
//            }
            case R.id.contact: {
                Intent intent=new Intent(HelpActivity.this,ContactActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.signout: {
                AlertDialog.Builder builder=new AlertDialog.Builder(HelpActivity.this);
                builder.setMessage("Are you sure do you want to sign out?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mAuth.signOut();
                                finish();
                                Intent intent=new Intent(HelpActivity.this,LogInActivity.class);
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
            }
            case R.id.exit: {
                finish();
                System.exit(0);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
