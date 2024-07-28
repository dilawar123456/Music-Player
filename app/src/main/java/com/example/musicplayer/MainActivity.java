package com.example.musicplayer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.DexterBuilder;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView list;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        String [] Songs= Namesgetter();
        ArrayList<File> songsarray=fetchSongs(Environment.getExternalStorageDirectory());

        list=findViewById(R.id.listView);
       permission(list);
       list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               Intent intent=new Intent(MainActivity.this, MainActivity2.class);
               intent.putExtra("position",position);
               intent.putExtra("name",Songs[position]);
               intent.putExtra("songs_array",songsarray);
               startActivity(intent);

           }
       });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public ArrayList<File> fetchSongs(File file)
    {
        ArrayList arraylist= new ArrayList();
        File[] songs= file.listFiles();
        if (songs!=null)
        {
            for(File myfile: songs)
            {
                if(!myfile.isHidden() && myfile.isDirectory())
                {
                    arraylist.addAll(fetchSongs(myfile));
                }
                else if(myfile.getName().endsWith(".mp3") && !myfile.getName().startsWith("."))
                {
                    arraylist.add(myfile);
                }
            }

        }
        return  arraylist;
    }

    public void permission(ListView listview)
    {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.TIRAMISU)
        {
            Dexter.withContext(this).withPermission(Manifest.permission.READ_MEDIA_AUDIO).withListener(new PermissionListener() {
                @Override
                public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                    Namesetter(listview);
                }

                @Override
                public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                    Toast.makeText(MainActivity.this, "Permission Denied!!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                    permissionToken.continuePermissionRequest();
                }
            }).check();

        }
        else
        {
            Dexter.withContext(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
                @Override
                public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                    Namesetter(listview);
                }

                @Override
                public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                    Toast.makeText(MainActivity.this, "Permission Denied!!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                    permissionToken.continuePermissionRequest();
                }
            }).check();
        }

    }

    public void Namesetter(ListView listview)
    {
        ArrayList<File> mysongs=fetchSongs(Environment.getExternalStorageDirectory());
        String [] items= new String[mysongs.size()];
        for(int  i=0;i<mysongs.size();i++)
        {
            items[i]=mysongs.get(i).getName().replace(".mp3"," ");
        }
        ArrayAdapter<String> ad= new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,items);
        listview.setAdapter(ad);
    }
    public String[] Namesgetter()
    {
        ArrayList<File> mysongs=fetchSongs(Environment.getExternalStorageDirectory());
        String [] items= new String[mysongs.size()];
        for(int i=0;i<mysongs.size();i++)
        {
            items[i]=mysongs.get(i).getName();
        }
        return items;

    }


}