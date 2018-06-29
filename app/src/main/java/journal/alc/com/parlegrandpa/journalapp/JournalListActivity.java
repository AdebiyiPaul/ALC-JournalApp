package journal.alc.com.parlegrandpa.journalapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import journal.alc.com.parlegrandpa.journalapp.adapters.NotesAdapter;
import journal.alc.com.parlegrandpa.journalapp.model.Note;
import journal.alc.com.parlegrandpa.journalapp.room.LocalCacheManager;
import journal.alc.com.parlegrandpa.journalapp.util.ListItemDecorator;
import journal.alc.com.parlegrandpa.journalapp.interfaces.MainViewInterface;
import journal.alc.com.parlegrandpa.journalapp.interfaces.OnItemClickListener;

public class JournalListActivity extends AppCompatActivity implements MainViewInterface {

    @BindView(R.id.rvNotes)
    RecyclerView rvNotes;

    RecyclerView.Adapter adapter;
    List<Note> notesList;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_list);
        ButterKnife.bind(this);

        auth = FirebaseAuth.getInstance();

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null)
                {
                    startActivity(new Intent(JournalListActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };

        initViews();
        loadNotes();
        swipeToDelete();

    }

    private void initViews() {

        rvNotes.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadNotes(){

        LocalCacheManager.getInstance(this).getNotes(this);


    }

    @OnClick(R.id.fabAddNote)
    public void addNote(){
        Intent i = new Intent(JournalListActivity.this, AddNoteActivity.class);
        startActivity(i);
    }

    @Override
    public void onNotesLoaded(final List<Note> notes) {
        notesList = notes;

        if(notesList.size() == 0){
            onDataNotAvailable();
        }else {
            adapter = new NotesAdapter(this, notes, new OnItemClickListener() {
                @Override
                public void onItemClick(View v, int position) {
                    String title = notes.get(position).getTitle();
                    String note = notes.get(position).getNote();
                    int note_id = notes.get(position).getId();
                    Intent intent = new Intent(JournalListActivity.this, JournalDetailActivity.class);
                    intent.putExtra("title", title);
                    intent.putExtra("note", note);
                    intent.putExtra("note_id", note_id);
                    startActivity(intent);
                }
            });
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            rvNotes.setLayoutManager(mLayoutManager);
            rvNotes.setItemAnimator(new DefaultItemAnimator());
            rvNotes.addItemDecoration(new ListItemDecorator(this, LinearLayoutManager.VERTICAL, 16));
            rvNotes.setAdapter(adapter);
        }
    }

    private void deleteNote(final Note note, final int position) {
        new deleteNoteAsyncTask().execute(note);
        notesList.remove(position);
        adapter.notifyItemRemoved(position);
    }

    @SuppressLint("StaticFieldLeak")
    private class deleteNoteAsyncTask extends AsyncTask<Note, Void, Void> {

        @Override
        protected Void doInBackground(final Note... params) {
            LocalCacheManager.getInstance(getApplicationContext()).deleteNote(params[0]);
            return null;
        }
    }

    private void swipeToDelete()
    {
        ItemTouchHelper helper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView,
                                          RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                        int position = viewHolder.getAdapterPosition();

                        Toast.makeText(JournalListActivity.this,
                                getString(R.string.delete_note), Toast.LENGTH_LONG).show();

                        deleteNote(notesList.get(position), position);
                    }
                });
        helper.attachToRecyclerView(rvNotes);
    }

    @Override
    public void onNoteAdded() {
        Toast.makeText(this,"Note Added",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDataNotAvailable() {
        Toast.makeText(this,"No Notes Yet",Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_logout,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == R.id.action_logout){
            auth.signOut();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }
}
