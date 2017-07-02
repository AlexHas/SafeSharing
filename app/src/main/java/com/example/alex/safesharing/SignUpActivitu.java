package com.example.alex.safesharing;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.List;

public class SignUpActivitu extends AppCompatActivity implements Validator.ValidationListener{
    private Validator validator;

    @NotEmpty
    @Email
    private EditText etEmail;
    @NotEmpty
    private EditText etUsername;
    @NotEmpty
    @Password
    private EditText etPassword;
    @NotEmpty
    @ConfirmPassword
    EditText etConfirmPassword;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;
    private TextView tvErrorRegistering;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_activitu);
        tvErrorRegistering=(TextView)findViewById(R.id.tvErrorRegistration);
        firebaseAuth=FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference();
        etEmail=(EditText)findViewById(R.id.etEmail);
        etUsername=(EditText)findViewById(R.id.etUsername);
        etPassword=(EditText)findViewById(R.id.etPassword);
        etConfirmPassword=(EditText)findViewById(R.id.etConfirmPassword);
        Button btnSignup=(Button)findViewById(R.id.btnSignUp);
        validator = new Validator(this);
        validator.setValidationListener(this);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
            }
        });
        progressDialog=new ProgressDialog(this);

    }

    private void registerUser(){
        String email=etEmail.getText().toString();
        final String password=encryptPassword(etPassword.getText().toString());
        progressDialog.setMessage("Registering...");
        progressDialog.show();

            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        mDatabase.child("Users").child(task.getResult().getUser().getUid()).child("email").setValue(etEmail.getText().toString());
                        mDatabase.child("Users").child(task.getResult().getUser().getUid()).child("password").setValue(password);
                        mDatabase.child("Users").child(task.getResult().getUser().getUid()).child("username").setValue(etUsername.getText().toString());
                        mDatabase.child("Users").child(task.getResult().getUser().getUid()).child("mainFolder").setValue("defaultFolder");
                        Intent intent = new Intent(SignUpActivitu.this, FilesActivity.class);
                        startActivity(intent);
                        String toast = "Registered! Welcome, " + etUsername.getText().toString() + "!";
                        Toast.makeText(SignUpActivitu.this, toast, Toast.LENGTH_SHORT).show();
                    } else {
                        tvErrorRegistering.setText(R.string.errorRegistering);
                        progressDialog.hide();
                    }
                }
            });

    }
    @Override
    public void onValidationSucceeded() {
        //codul pe care vreau sa-l faca
        //Toast.makeText(SignUpActivitu.this,"S-a validat!",Toast.LENGTH_LONG).show();
        registerUser();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }
    private String encryptPassword(String str){
        String Newstr="";
        for (int i=0;i<str.length();i++)
        {
            char ch=Character.toLowerCase(str.charAt(i));
            switch (ch)
            {
                case 'a':
                    Newstr=Newstr+"{";
                    break;
                case 'b':
                    Newstr=Newstr+"}";
                    break;
                case 'c':
                    Newstr=Newstr+"#";
                    break;
                case 'd':
                    Newstr=Newstr+"~";
                    break;
                case 'e':
                    Newstr=Newstr+"+";
                    break;
                case 'f':
                    Newstr=Newstr+"-";
                    break;
                case 'g':
                    Newstr=Newstr+"*";
                    break;
                case 'h':
                    Newstr=Newstr+"@";
                    break;
                case 'i':
                    Newstr=Newstr+"/";
                    break;
                case 'j':
                    Newstr=Newstr+"\\";
                    break;
                case 'k':
                    Newstr=Newstr+"?";
                    break;
                case 'l':
                    Newstr=Newstr+"$";
                    break;
                case 'm':
                    Newstr=Newstr+"!";
                    break;
                case 'n':
                    Newstr=Newstr+"^";
                    break;
                case 'o':
                    Newstr=Newstr+"(";
                    break;
                case 'p':
                    Newstr=Newstr+")";
                    break;
                case 'q':
                    Newstr=Newstr+"<";
                    break;
                case 'r':
                    Newstr=Newstr+">";
                    break;
                case 's' :
                    Newstr=Newstr+"=";
                    break;
                case 't':
                    Newstr=Newstr+";";
                    break;
                case 'u':
                    Newstr=Newstr+",";
                    break;
                case 'v' :
                    Newstr=Newstr+"_";
                    break;
                case 'w':
                    Newstr=Newstr+"[";
                    break;
                case 'x' :
                    Newstr=Newstr+"]";
                    break;
                case 'y':
                    Newstr=Newstr+":";
                    break;
                case 'z' :
                    Newstr=Newstr+"\"";
                    break;
                case ' ' :
                    Newstr=Newstr+" ";
                    break;
                case '.':
                    Newstr=Newstr+'3';
                    break;
                case ',':
                    Newstr=Newstr+"1";
                    break;
                case '(':
                    Newstr=Newstr+'4';
                    break;
                case '\"':
                    Newstr=Newstr+'5';
                    break;
                case ')' :
                    Newstr=Newstr+"7";
                    break;
                case '?' :
                    Newstr= Newstr+"2";
                    break;
                case '!':
                    Newstr= Newstr+"8";
                    break;
                case '-' :
                    Newstr= Newstr+"6";
                    break;
                case '%' :
                    Newstr = Newstr+"9";
                    break;
                case '1':
                    Newstr=Newstr+"r";
                    break;
                case '2':
                    Newstr=Newstr+"k";
                    break;
                case '3':
                    Newstr=Newstr+"b";
                    break;
                case '4':
                    Newstr = Newstr+"e";
                    break;
                case '5':
                    Newstr = Newstr+"q";
                    break;
                case '6':
                    Newstr = Newstr+"h";
                    break;
                case '7':
                    Newstr = Newstr+"u";
                    break;
                case '8' :
                    Newstr= Newstr+"y";
                    break;
                case '9':
                    Newstr = Newstr+"w";
                    break;
                case '0':
                    Newstr = Newstr+"z";
                    break;
                default:
                    Newstr=Newstr+"0";
                    break;
            }
        }
        return Newstr;

    }
}
