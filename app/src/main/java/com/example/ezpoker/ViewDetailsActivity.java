package com.example.ezpoker;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class ViewDetailsActivity extends Activity {
    private DatabaseHelper dbHelper;
    private ListView playersListView;
    private TextView gameDetailsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_details);

        dbHelper = new DatabaseHelper(this);
        playersListView = findViewById(R.id.players_list_view);
        gameDetailsTextView = findViewById(R.id.game_details);

        long gameId = getIntent().getLongExtra("GAME_ID", -1);

        displayGameDetails(gameId);
        displayPlayers(gameId);
    }

    private void displayGameDetails(long gameId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT * FROM " + DatabaseHelper.TABLE_GAMES + " WHERE " + DatabaseHelper.COLUMN_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(gameId)});

        if (cursor.moveToFirst()) {
            double buyInAmount = cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.COLUMN_BUY_IN_AMOUNT));
            String gameDate = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_GAME_DATE));

            StringBuilder gameData = new StringBuilder();
            gameData.append("Game Date: ").append(gameDate).append("\n");
            gameData.append("Buy-In Amount: ").append(buyInAmount).append("\n");

            gameDetailsTextView.setText(gameData.toString());
        }

        cursor.close();
    }

    private void displayPlayers(long gameId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT * FROM " + DatabaseHelper.TABLE_PLAYERS + " WHERE " + DatabaseHelper.COLUMN_GAME_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(gameId)});

        String[] fromColumns = {
                DatabaseHelper.COLUMN_PLAYER_NAME,
                DatabaseHelper.COLUMN_BUY_OUT_AMOUNT,
                DatabaseHelper.COLUMN_AMOUNT_DIFFERENCE
        };

        int[] toViews = {
                R.id.player_name,
                R.id.buy_out_amount,
                R.id.amount_difference
        };

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this,
                R.layout.player_list_item,
                cursor,
                fromColumns,
                toViews,
                0
        );

        playersListView.setAdapter(adapter);
    }
}