package com.example.ezpoker;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class GameButtonAdapter extends CursorAdapter {

    public GameButtonAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.game_list_item_button, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView gameDateTextView = view.findViewById(R.id.game_date);
        TextView buyInAmountTextView = view.findViewById(R.id.buy_in_amount);
        Button viewDetailsButton = view.findViewById(R.id.view_details_button);

        String gameDate = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_GAME_DATE));
        double buyInAmount = cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.COLUMN_BUY_IN_AMOUNT));
        long gameId = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID));

        gameDateTextView.setText(gameDate);
        buyInAmountTextView.setText(String.valueOf(buyInAmount));

        viewDetailsButton.setTag(gameId);
        viewDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long id = (long) v.getTag();
                Intent intent = new Intent(context, ViewDetailsActivity.class);
                intent.putExtra("GAME_ID", id);
                context.startActivity(intent);
            }
        });
    }
}
