package com.example.ezpoker;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ViewPastActivity extends Activity {
    private DatabaseHelper dbHelper;
    private ListView gamesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_past);

        dbHelper = new DatabaseHelper(this);
        gamesListView = findViewById(R.id.games_list_view);

        displayGames();

        gamesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ViewPastActivity.this, ViewDetailsActivity.class);
                intent.putExtra("GAME_ID", id);
                startActivity(intent);
            }
        });
    }

    private void displayGames() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Query to get all saved games
        String query = "SELECT * FROM " + DatabaseHelper.TABLE_GAMES;
        Cursor cursor = db.rawQuery(query, null);

        // Create an adapter to bind data to the ListView
        GameButtonAdapter adapter = new GameButtonAdapter(this, cursor, 0);
        gamesListView.setAdapter(adapter);
    }
}