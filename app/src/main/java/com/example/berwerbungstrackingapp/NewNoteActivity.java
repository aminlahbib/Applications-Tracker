package com.example.berwerbungstrackingapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

public class NewNoteActivity extends AppCompatActivity {

    private EditText titleEditText, dateEditText, statusEditText, placeEditText, establishmentEditText,  remarksEditText;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        titleEditText = findViewById(R.id.titleEditText);
        dateEditText = findViewById(R.id.dateEditText);
        statusEditText = findViewById(R.id.statusEditText);
        placeEditText = findViewById(R.id.placeEditText);
        establishmentEditText = findViewById(R.id.establishmentEditText);
        remarksEditText = findViewById(R.id.remarksEditText);
        saveButton = findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNote();
            }
        });
    }

    private void saveNote() {
        String title = titleEditText.getText().toString().trim();
        String date = dateEditText.getText().toString().trim();
        String status = statusEditText.getText().toString().trim();
        String place = placeEditText.getText().toString().trim();
        String establishment = establishmentEditText.getText().toString().trim();
        String remarks = remarksEditText.getText().toString().trim();

        if (!title.isEmpty()) {
            RadioGroup typeRadioGroup = findViewById(R.id.typeRadioGroup);
            int selectedRadioButtonId = typeRadioGroup.getCheckedRadioButtonId();
            String selectedType = "";

            if (selectedRadioButtonId != -1) {
                RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
                selectedType = selectedRadioButton.getText().toString();
            }

            // Create a Note instance with the selected type
            Note newNote = new Note(title, date, status, place, establishment, selectedType, remarks);

            // Save the new note to the database
            NoteDatabaseHelper databaseHelper = new NoteDatabaseHelper(this);
            databaseHelper.addNote(newNote);

            // Optionally, you can show a success message or navigate to the main activity
            finish();
        } else {
            // Title is required, you may show an error message
            // or perform other actions as needed
        }
    }
}
