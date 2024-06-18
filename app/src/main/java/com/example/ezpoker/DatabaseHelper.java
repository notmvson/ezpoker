package com.example.ezpoker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * DatabaseHelper is a helper class to manage database creation and version management.
 * This class extends SQLiteOpenHelper which provides the necessary functionality to work with SQLite databases.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Database name and version constants
    private static final String DATABASE_NAME = "ezpoker.db";
    private static final int DATABASE_VERSION = 2;

    // Constants for the games table
    public static final String TABLE_GAMES = "games";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_BUY_IN_AMOUNT = "buy_in_amount";
    public static final String COLUMN_GAME_DATE = "game_date";

    // Constants for the players table
    public static final String TABLE_PLAYERS = "players";
    public static final String COLUMN_GAME_ID = "game_id";
    public static final String COLUMN_PLAYER_NAME = "player_name";
    public static final String COLUMN_BUY_OUT_AMOUNT = "buy_out_amount";
    public static final String COLUMN_AMOUNT_DIFFERENCE = "amount_difference";

    // SQL statement to create the games table
    private static final String TABLE_CREATE_GAMES =
            "CREATE TABLE " + TABLE_GAMES + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_BUY_IN_AMOUNT + " REAL, " +
                    COLUMN_GAME_DATE + " TEXT);";

    // SQL statement to create the players table
    private static final String TABLE_CREATE_PLAYERS =
            "CREATE TABLE " + TABLE_PLAYERS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_GAME_ID + " INTEGER, " +
                    COLUMN_PLAYER_NAME + " TEXT, " +
                    COLUMN_BUY_OUT_AMOUNT + " REAL, " +
                    COLUMN_AMOUNT_DIFFERENCE + " REAL, " +
                    "FOREIGN KEY(" + COLUMN_GAME_ID + ") REFERENCES " + TABLE_GAMES + "(" + COLUMN_ID + "));";

    /**
     * Constructor for DatabaseHelper
     *
     * @param context The context of the activity/application
     */
    public DatabaseHelper(Context context) {
        // Call the parent constructor to create a helper object to manage the database
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database is created for the first time.
     * This is where the creation of tables and the initial population of the tables should happen.
     *
     * @param db The database
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Execute the SQL statements to create the tables
        db.execSQL(TABLE_CREATE_GAMES);
        db.execSQL(TABLE_CREATE_PLAYERS);
    }

    /**
     * Called when the database needs to be upgraded. This method will only be called if a database already exists
     * on disk with the same DATABASE_NAME, but the DATABASE_VERSION is different than the version of the database
     * that exists on disk.
     *
     * @param db The database
     * @param oldVersion The old database version
     * @param newVersion The new database version
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the old tables if they exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GAMES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYERS);
        // Create new tables
        onCreate(db);
    }
}