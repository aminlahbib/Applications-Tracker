package com.example.berwerbungstrackingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import androidx.appcompat.app.AppCompatActivity;

public class EditNoteActivity extends AppCompatActivity {

    private Note selectedNote;
    private EditText titleEditText, dateEditText, statusEditText, placeEditText, establishmentEditText, remarksEditText;
    private RadioGroup typeRadioGroup;
    private Button updateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        // Retrieve the selected note from the intent
        Intent intent = getIntent();
        selectedNote = (Note) intent.getSerializableExtra("selectedNote");

        titleEditText = findViewById(R.id.titleEditText);
        dateEditText = findViewById(R.id.dateEditText);
        statusEditText = findViewById(R.id.statusEditText);
        placeEditText = findViewById(R.id.placeEditText);
        establishmentEditText = findViewById(R.id.establishmentEditText);
        typeRadioGroup = findViewById(R.id.typeRadioGroup);
        remarksEditText = findViewById(R.id.remarksEditText);
        updateButton = findViewById(R.id.updateButton);

        // Populate EditText fields with the existing values
        titleEditText.setText(selectedNote.getTitle());
        dateEditText.setText(selectedNote.getDate());
        statusEditText.setText(selectedNote.getStatus());
        placeEditText.setText(selectedNote.getPlace());
        establishmentEditText.setText(selectedNote.getEstablishment());
        remarksEditText.setText(selectedNote.getRemarks());

        // Set the selected radio button based on the existing type
        switch (selectedNote.getType()) {
            case "Vollzeit":
                typeRadioGroup.check(R.id.radioVollzeit);
                break;
            case "Teilzeit":
                typeRadioGroup.check(R.id.radioTeilzeit);
                break;
            case "Sonstiges":
                typeRadioGroup.check(R.id.radioSonstiges);
                break;
        }

        // Handle Update button click
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Update the selected note with the edited values
                selectedNote.setTitle(titleEditText.getText().toString().trim());
                selectedNote.setDate(dateEditText.getText().toString().trim());
                selectedNote.setStatus(statusEditText.getText().toString().trim());
                selectedNote.setPlace(placeEditText.getText().toString().trim());
                selectedNote.setEstablishment(establishmentEditText.getText().toString().trim());

                // Get the selected type from the radio group
                int selectedRadioButtonId = typeRadioGroup.getCheckedRadioButtonId();
                if (selectedRadioButtonId != -1) {
                    RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
                    selectedNote.setType(selectedRadioButton.getText().toString());
                }

                selectedNote.setRemarks(remarksEditText.getText().toString().trim());

                // Update the note in the database
                NoteDatabaseHelper databaseHelper = new NoteDatabaseHelper(EditNoteActivity.this);
                int updateResult = databaseHelper.updateNote(selectedNote);
                databaseHelper.close();

                // Check if the update was successful
                if (updateResult > 0) {
                    // Return the updated note to DetailNoteActivity
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("updatedNote", selectedNote);
                    setResult(RESULT_OK, resultIntent);

                    // Finish the activity
                    finish();
                } else {
                    // Handle update failure, show a message or log an error
                    // You may want to display a Toast or Snackbar here
                }
            }
        });
    }
}

