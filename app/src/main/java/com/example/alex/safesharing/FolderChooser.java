package com.example.alex.safesharing;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class FolderChooser extends ListActivity {//implements View.OnLongClickListener{

    private File currentDir;
    private FileArrayAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentDir = new File("/storage/");
        fill(currentDir);
        registerForContextMenu(this.getListView());
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info= (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()){
            case R.id.set: {
                Item o= (Item) this.getListView().getItemAtPosition(info.position);
                //Toast.makeText(getApplicationContext(), o.getPath(),Toast.LENGTH_LONG).show();
                Intent intent=new Intent();
                intent.putExtra("FolderPath", o.getPath());
                setResult(RESULT_OK, intent);
                finish();

            }
            case R.id.open: {
                Item o= (Item) this.getListView().getItemAtPosition(info.position);
                if(o.getImage().equalsIgnoreCase("directory_icon")||o.getImage().equalsIgnoreCase("directory_up")){
                    currentDir = new File(o.getPath());
                    fill(currentDir);
                }
                else
                {
                    onFileClick(o);
                }
            }
        }
        return super.onContextItemSelected(item);
    }

    private void fill(File f)
    {
        File[] dirs = f.listFiles();
        this.setTitle("Current Dir: "+f.getName());
        List<Item> dir = new ArrayList<Item>();
        //List<Item>fls = new ArrayList<Item>();
        try{
            for(File ff: dirs)
            {
                Date lastModDate = new Date(ff.lastModified());
                DateFormat formater = DateFormat.getDateTimeInstance();
                String date_modify = formater.format(lastModDate);
                if(ff.isDirectory()){


                    File[] fbuf = ff.listFiles();
                    int buf = 0;
                    if(fbuf != null){
                        buf = fbuf.length;
                    }
                    else buf = 0;
                    String num_item = String.valueOf(buf);
                    if(buf == 0) num_item = num_item + " item";
                    else num_item = num_item + " items";

                    //String formated = lastModDate.toString();
                    dir.add(new Item(ff.getName(),num_item,date_modify,ff.getAbsolutePath(),"directory_icon"));
                }
                else
                {

                    //fls.add(new Item(ff.getName(),ff.length() + " Byte", date_modify, ff.getAbsolutePath(),"file_icon"));
                }
            }
        }catch(Exception e)
        {

        }
        Collections.sort(dir);
        //Collections.sort(fls);
        //dir.addAll(fls);

        if(!f.getName().equalsIgnoreCase("sdcard"))

            dir.add(0,new Item("..","Parent Directory","",f.getParent(),"directory_up"));
        adapter = new FileArrayAdapter(FolderChooser.this,R.layout.file_view,dir);

        this.setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);
        Item o = adapter.getItem(position);
        if(o.getImage().equalsIgnoreCase("directory_icon")||o.getImage().equalsIgnoreCase("directory_up")){
            currentDir = new File(o.getPath());
            fill(currentDir);
        }
        else
        {
            onFileClick(o);
        }
    }
//    @Override
//    public boolean onLongClick(View view) {
//        if(view==this.getListView()){
//            Toast.makeText(getApplicationContext(),"Long click",Toast.LENGTH_LONG).show();
//        }
//        return true;
//    }
    private void onFileClick(Item o)
    {
        //Toast.makeText(this, "Folder Clicked: "+ currentDir, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.putExtra("GetFileName",o.getName());
        intent.putExtra("GetPath",o.getPath());
        intent.putExtra("GetSize",o.getData());
        System.out.println(o.getData());

        setResult(RESULT_OK, intent);
        finish();
    }
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }


}
