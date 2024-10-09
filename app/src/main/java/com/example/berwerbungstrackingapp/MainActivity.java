package com.example.berwerbungstrackingapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private NoteDatabaseHelper databaseHelper;
    private RecyclerView recyclerView;
    private NoteAdapter noteAdapter;
    private EditText searchEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new NoteDatabaseHelper(this);

        Button addNoteButton = findViewById(R.id.addNoteButton);
        Button seeMoreButton = findViewById(R.id.seeMoreButton);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        noteAdapter = new NoteAdapter();
        recyclerView.setAdapter(noteAdapter);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // Get the clicked note
                        Note clickedNote = noteAdapter.getItem(position);

                        // Launch DetailNoteActivity
                        Intent intent = new Intent(MainActivity.this, DetailNoteActivity.class);
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
        searchEditText = findViewById(R.id.searchEditText);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Update RecyclerView based on the search query
                searchNotes(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        addNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewNoteActivity.class);
                startActivity(intent);
            }
        });

        seeMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Note> allNotes = databaseHelper.getAllNotes();
                Intent intent = new Intent(MainActivity.this, DisplayNotesActivity.class);
                intent.putExtra("notesList", (Serializable) allNotes);
                startActivity(intent);
            }
        });

        // Fetch recent notes and update the RecyclerView
        updateRecentNotes();
    }

    private void updateRecentNotes() {
        List<Note> recentNotes = databaseHelper.getRecentNotes(5); // Fetch the most recent 5 notes
        noteAdapter.setNotes(recentNotes);
        noteAdapter.notifyDataSetChanged();
    }
    private void searchNotes(String query) {
        List<Note> searchResults = databaseHelper.searchNotesByTitle(query);
        noteAdapter.setNotes(searchResults);
        noteAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (noteAdapter != null){
            updateRecentNotes();
        }
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
                    Toast.makeText(MainActivity.this, "Note deleted", Toast.LENGTH_SHORT).show();
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
