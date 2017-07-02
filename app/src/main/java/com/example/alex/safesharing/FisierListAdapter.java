package com.example.alex.safesharing;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexHash on 16.06.2017.
 */

public class FisierListAdapter extends BaseAdapter {
    private Activity context;
    private List<Fisier> files;
    private String id;
    private double totalSpace;

    public double getTotalSpace(){
        return this.totalSpace;
    }

    public FisierListAdapter(Activity context, List<Fisier> lista, String id){
        this.context=context;
        this.files=lista;
        this.id=id;
        double space=0;
        for (Fisier f : this.files) {
            double s = Double.parseDouble(f.getSize().substring(0, f.getSize().length() - 3));
            space += s;
            //Toast.makeText(getApplicationContext(),f.getSize(),Toast.LENGTH_LONG).show();
        }
        this.totalSpace=space;

    }
    public double calculateTotalSpace(List<Fisier> fisiere){
        double space=0.00;
        for (Fisier f : fisiere) {
            double s = Double.parseDouble(f.getSize().substring(0, f.getSize().length() - 3));
            space += s;
            //Toast.makeText(getApplicationContext(),f.getSize(),Toast.LENGTH_LONG).show();
        }
        return space;

    }
    @Override
    public int getCount() {
        return files.size();
    }

    @Override
    public Object getItem(int i) {
        return files.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v=View.inflate(context, R.layout.item_fisier, null);
        double space=0.00;

        final TextView tvName=(TextView) v.findViewById(R.id.tvName);
        final TextView tvLocation=(TextView) v.findViewById(R.id.tvLocation);
        Button btnGetBack=(Button) v.findViewById(R.id.btnGetBack);
        Button btnDelete=(Button) v.findViewById(R.id.btnDelete);
        tvName.setText(files.get(i).getName());
        tvLocation.setText(files.get(i).getLocation());
        final Fisier deSters=files.get(i);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setMessage("Are you sure do you want to permanently delete this file?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, final int j) {
                                FilesActivity.progressDialog.setMessage("Deleting...");
                                FilesActivity.progressDialog.show();
                                FilesActivity.files.remove(deSters);
                                FilesActivity.adapterLista.notifyDataSetChanged();
                                //steregere din bd!!!

                                Query query=FilesActivity.mDatabase.child("Users").child(id).child("Files");
                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for( DataSnapshot data: dataSnapshot.getChildren()){
                                            String loc=data.child("location").getValue(String.class);
                                            String nam=data.child("name").getValue(String.class);
                                            String siz=data.child("size").getValue(String.class);
                                            if(loc==tvLocation.getText()){
                                                data.getRef().removeValue();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                String refChild=id+"/" + tvName.getText().toString();
                                StorageReference delRef=FilesActivity.storageReference.child(refChild);
                                delRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(context, "File was succesfully deleted!", Toast.LENGTH_LONG).show();
                                        FilesActivity.progressDialog.hide();
                                    }
                                });
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
        });
        btnGetBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setMessage("Are you sure do you want to download this file?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                               //aduc inapoi in telefon

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
        });
        v.setTag(files.get(i));
        return v;

    }

}
