package com.example.ezpoker;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.widget.AdapterView;
import java.util.Calendar;

/**
 * NewGameActivity is the activity where the user can create a new poker game, input player names,
 * and specify buy-in and buy-out amounts. This activity saves the game data to the SQLite database.
 */
public class NewGameActivity extends Activity {
    private EditText buyInAmountEditText;
    private EditText gameDateEditText;
    private Spinner playerCountSpinner;
    private LinearLayout playerNamesContainer;
    private DatabaseHelper dbHelper;
    private TextView savedGameDataTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);

        // Initialize UI elements
        buyInAmountEditText = findViewById(R.id.buy_in_amount);
        gameDateEditText = findViewById(R.id.game_date);
        playerCountSpinner = findViewById(R.id.player_count_spinner);
        playerNamesContainer = findViewById(R.id.player_names_container);
        savedGameDataTextView = findViewById(R.id.saved_game_data);
        Button saveButton = findViewById(R.id.save_button);

        // Initialize the database helper
        dbHelper = new DatabaseHelper(this);

        // Set up the date picker dialog for game date input
        gameDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        // Set up the player count spinner with an adapter
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.player_count_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        playerCountSpinner.setAdapter(adapter);

        // Handle player count selection
        playerCountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int numberOfPlayers = Integer.parseInt(parent.getItemAtPosition(position).toString());
                addPlayerNameFields(numberOfPlayers);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Handle save button click to save game data
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveGameData();
                displaySavedGameData();
            }
        });
    }

    /**
     * Shows a date picker dialog for the user to select a game date.
     */
    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                gameDateEditText.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
            }
        }, year, month, day);

        datePickerDialog.show();
    }

    /**
     * Adds player name and buy-out amount input fields dynamically based on the selected number of players.
     *
     * @param numberOfPlayers The number of players to add input fields for
     */
    private void addPlayerNameFields(int numberOfPlayers) {
        playerNamesContainer.removeAllViews();
        for (int i = 0; i < numberOfPlayers; i++) {
            // Create a horizontal layout for each player
            LinearLayout playerLayout = new LinearLayout(this);
            playerLayout.setOrientation(LinearLayout.HORIZONTAL);

            // Create an EditText for the player's name
            EditText playerNameEditText = new EditText(this);
            playerNameEditText.setHint("Player " + (i + 1) + " Name");
            playerLayout.addView(playerNameEditText);

            // Create an EditText for the player's buy-out amount
            EditText buyOutAmountEditText = new EditText(this);
            buyOutAmountEditText.setHint("Buy-Out Amount");
            buyOutAmountEditText.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
            playerLayout.addView(buyOutAmountEditText);

            // Add the horizontal layout to the player names container
            playerNamesContainer.addView(playerLayout);
        }
    }

    /**
     * Saves the game data, including buy-in amount, game date, and player details, to the SQLite database.
     */
    private void saveGameData() {
        String buyInAmount = buyInAmountEditText.getText().toString();
        String gameDate = gameDateEditText.getText().toString();

        // Validate input fields
        if (buyInAmount.isEmpty() || gameDate.isEmpty()) {
            Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get a writable database instance
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Insert game data into the games table
        ContentValues gameValues = new ContentValues();
        gameValues.put(DatabaseHelper.COLUMN_BUY_IN_AMOUNT, Double.parseDouble(buyInAmount));
        gameValues.put(DatabaseHelper.COLUMN_GAME_DATE, gameDate);
        long gameId = db.insert(DatabaseHelper.TABLE_GAMES, null, gameValues);

        // Insert player data into the players table
        for (int i = 0; i < playerNamesContainer.getChildCount(); i++) {
            LinearLayout playerLayout = (LinearLayout) playerNamesContainer.getChildAt(i);
            EditText playerEditText = (EditText) playerLayout.getChildAt(0);
            EditText buyOutEditText = (EditText) playerLayout.getChildAt(1);
            String player = playerEditText.getText().toString();
            String buyOutAmount = buyOutEditText.getText().toString();

            // Validate player input fields
            if (player.isEmpty() || buyOutAmount.isEmpty()) {
                Toast.makeText(this, "Please enter name and buy-out amount for Player " + (i + 1), Toast.LENGTH_SHORT).show();
                return;
            }

            // Calculate the amount difference
            double buyOutAmountValue = Double.parseDouble(buyOutAmount);
            double amountDifference = buyOutAmountValue - Double.parseDouble(buyInAmount);

            // Insert player data
            ContentValues playerValues = new ContentValues();
            playerValues.put(DatabaseHelper.COLUMN_GAME_ID, gameId);
            playerValues.put(DatabaseHelper.COLUMN_PLAYER_NAME, player);
            playerValues.put(DatabaseHelper.COLUMN_BUY_OUT_AMOUNT, Double.parseDouble(buyOutAmount));
            playerValues.put(DatabaseHelper.COLUMN_AMOUNT_DIFFERENCE, amountDifference);
            db.insert(DatabaseHelper.TABLE_PLAYERS, null, playerValues);
        }

        // Show a toast message to confirm data save
        Toast.makeText(this, "Game data saved", Toast.LENGTH_SHORT).show();
    }

    /**
     * Displays the saved game data in the TextView.
     */
    private void displaySavedGameData() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Query to get the latest saved game
        String query = "SELECT * FROM " + DatabaseHelper.TABLE_GAMES +
                " ORDER BY " + DatabaseHelper.COLUMN_ID + " DESC LIMIT 1";
        Cursor gameCursor = db.rawQuery(query, null);

        if (gameCursor.moveToFirst()) {
            double buyInAmount = gameCursor.getDouble(gameCursor.getColumnIndex(DatabaseHelper.COLUMN_BUY_IN_AMOUNT));
            String gameDate = gameCursor.getString(gameCursor.getColumnIndex(DatabaseHelper.COLUMN_GAME_DATE));
            long gameId = gameCursor.getLong(gameCursor.getColumnIndex(DatabaseHelper.COLUMN_ID));

            // Display the game details
            StringBuilder gameData = new StringBuilder();
            gameData.append("Game ID: ").append(gameId).append("\n");
            gameData.append("Buy-In Amount: ").append(buyInAmount).append("\n");
            gameData.append("Game Date: ").append(gameDate).append("\n\n");

            // Query to get the players for the latest saved game
            String playerQuery = "SELECT * FROM " + DatabaseHelper.TABLE_PLAYERS +
                    " WHERE " + DatabaseHelper.COLUMN_GAME_ID + " = " + gameId;
            Cursor playerCursor = db.rawQuery(playerQuery, null);

            while (playerCursor.moveToNext()) {
                String playerName = playerCursor.getString(playerCursor.getColumnIndex(DatabaseHelper.COLUMN_PLAYER_NAME));
                double buyOutAmount = playerCursor.getDouble(playerCursor.getColumnIndex(DatabaseHelper.COLUMN_BUY_OUT_AMOUNT));
                double amountDifference = playerCursor.getDouble(playerCursor.getColumnIndex(DatabaseHelper.COLUMN_AMOUNT_DIFFERENCE));

                // Display the player details
                gameData.append("Player Name: ").append(playerName).append("\n");
                gameData.append("Buy-Out Amount: ").append(buyOutAmount).append("\n\n");
                gameData.append("Amount Difference: ").append(amountDifference).append("\n\n");
            }

            playerCursor.close();
            savedGameDataTextView.setText(gameData.toString());
        }

        gameCursor.close();
    }
}
