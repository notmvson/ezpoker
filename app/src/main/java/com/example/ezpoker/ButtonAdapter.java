package com.example.ezpoker;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

public class ButtonAdapter extends ArrayAdapter<String> {
    private Context context;
    private String[] options;

    public ButtonAdapter(Context context, String[] options) {
        super(context, R.layout.list_item_button, options);
        this.context = context;
        this.options = options;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_button, parent, false);
        }

        Button button = convertView.findViewById(R.id.list_item_button);
        button.setText(options[position]);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button click here
                if (position == 0) {
                    // Navigate to New Game Activity
                    context.startActivity(new Intent(context, NewGameActivity.class));
                } else if (position == 1) {
                    // Navigate to View Past Games Activity
                    context.startActivity(new Intent(context, ViewPastActivity.class));
                }
            }
        });

        return convertView;
    }
}
