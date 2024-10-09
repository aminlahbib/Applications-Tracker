package com.example.berwerbungstrackingapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.Serializable;
import java.util.List;

public class DisplayNotesActivity extends AppCompatActivity {
    private NoteDatabaseHelper databaseHelper;
    private RecyclerView recyclerView;
    private NoteAdapter noteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_notes);
        databaseHelper = new NoteDatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        noteAdapter = new NoteAdapter();
        recyclerView.setAdapter(noteAdapter);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Note clickedNote = noteAdapter.getItem(position);
                        Intent intent = new Intent(DisplayNotesActivity.this, DetailNoteActivity.class);
                        intent.putExtra("selectedNote", clickedNote);
                        startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // Handle long item click (Show delete confirmation dialog)
                        showDeleteConfirmationDialog(position);
                    }
                })
        );
        // Retrieve the list of notes from the intent
        Intent intent = getIntent();
        List<Note> notesList = (List<Note>) intent.getSerializableExtra("notesList");

        // Update RecyclerView with the provided notes
        noteAdapter.setNotes(notesList);
        noteAdapter.notifyDataSetChanged();
    }

    private void showDeleteConfirmationDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Note");
        builder.setMessage("Are you sure you want to delete this note?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Delete the note from the database
                Note noteToDelete = noteAdapter.getItem(position);
                if (noteToDelete != null) {
                    databaseHelper.deleteNote(noteToDelete.getId());
                    // Remove the note from the RecyclerView
                    noteAdapter.removeNoteAt(position);
                    Toast.makeText(DisplayNotesActivity.this, "Note deleted", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // User clicked "No", do nothing or add your logic here
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
