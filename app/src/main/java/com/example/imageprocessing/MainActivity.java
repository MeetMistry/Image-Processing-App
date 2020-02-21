package com.example.imageprocessing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ListView list = findViewById(R.id.menuListView);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Filters");
        arrayList.add("Ege Detection");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.
                layout.simple_list_item_1, arrayList);
        list.setAdapter(arrayAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if(i == 0){
                    Intent intent = new Intent(getApplicationContext(), Filter.class);
                    startActivity(intent);
                }else if (i == 1){

                }
            }
        });

    }
}
