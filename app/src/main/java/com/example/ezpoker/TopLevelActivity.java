package com.example.ezpoker;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.ListView;
import android.view.View;


//This activity extends the Activity class

public class TopLevelActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_level);


//Create an OnItemClickListener

        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {


//Implement the onItemClick method

            public void onItemClick(AdapterView<?> listView,
                                    View itemView,
                                    int position,
                                    long id) {


//Ignore Android Studio's message that it does not exist

                if (position == 0) {
                    Intent intent = new Intent(TopLevelActivity.this, NewGameActivity.class);
                    startActivity(intent);
                }
                if (position == 1) {
                    Intent intent = new Intent(TopLevelActivity.this, ViewPastActivity.class);
                    startActivity(intent);
                }
            }
        };



//Add the listener to the list view

        ListView listView = (ListView) findViewById(R.id.list_options);
        listView.setOnItemClickListener(itemClickListener);

        // Get the options from the resources
        String[] options = getResources().getStringArray(R.array.options);

        // Create and set the custom adapter
        ButtonAdapter adapter = new ButtonAdapter(this, options);
        listView.setAdapter(adapter);
    }
}