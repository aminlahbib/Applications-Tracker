package com.example.berwerbungstrackingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DetailNoteActivity extends AppCompatActivity {

    private Note selectedNote;
    private TextView titleTextView, dateTextView, statusTextView, placeTextView, establishmentTextView, typeTextView, remarksTextView;
    private Button editButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_note);

        // Retrieve the selected note from the intent
        Intent intent = getIntent();
        selectedNote = (Note) intent.getSerializableExtra("selectedNote");

        titleTextView = findViewById(R.id.titleTextView);
        dateTextView = findViewById(R.id.dateTextView);
        statusTextView = findViewById(R.id.statusTextView);
        placeTextView = findViewById(R.id.placeTextView);
        establishmentTextView = findViewById(R.id.establishmentTextView);
        typeTextView = findViewById(R.id.typeTextView);
        remarksTextView = findViewById(R.id.remarksTextView);
        editButton = findViewById(R.id.editButton);

        // Display the details of the selected note
        displayNoteDetails();

        // Handle Edit button click
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch EditNoteActivity
                Intent editIntent = new Intent(DetailNoteActivity.this, EditNoteActivity.class);
                editIntent.putExtra("selectedNote", selectedNote);
                startActivityForResult(editIntent, 1);
                finish();
            }
        });
    }

    private void displayNoteDetails() {
        titleTextView.setText(selectedNote.getTitle());
        dateTextView.setText(selectedNote.getDate());
        statusTextView.setText(selectedNote.getStatus());
        placeTextView.setText(selectedNote.getPlace());
        establishmentTextView.setText(selectedNote.getEstablishment());
        typeTextView.setText(selectedNote.getType());
        remarksTextView.setText(selectedNote.getRemarks());
    }

    // Handle the result from EditNoteActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Update the selectedNote with the edited values
            selectedNote = (Note) data.getSerializableExtra("updatedNote");
            // Update the displayed details
            displayNoteDetails();
        }
    }
}
