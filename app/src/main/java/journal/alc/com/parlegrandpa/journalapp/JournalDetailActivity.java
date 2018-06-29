package journal.alc.com.parlegrandpa.journalapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import journal.alc.com.parlegrandpa.journalapp.room.LocalCacheManager;
import journal.alc.com.parlegrandpa.journalapp.interfaces.AddNoteViewInterface;

public class JournalDetailActivity extends AppCompatActivity implements AddNoteViewInterface {

    @BindView(R.id.viewTitle)
    EditText viewTitle;
    @BindView(R.id.viewNote)
    EditText viewNote;
    String title, note;
    int note_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_detail);

        ButterKnife.bind(this);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        title = getIntent().getStringExtra("title");
        note = getIntent().getStringExtra("note");
        note_id = getIntent().getIntExtra("note_id", 0);

        viewTitle.setText(title);
        viewNote.setText(note);
    }

    private void updateNote(){

        String note_title = viewTitle.getText().toString();
        String note_text = viewNote.getText().toString();

        if(note_title.equals("") || note_text.equals("")){
            showToast("Please fill all the fields before updating");
        }else{
            LocalCacheManager.getInstance(this).updateNotes(this, note_title, note_text, note_id);
        }

    }


    private void showToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_note,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == R.id.action_save){
            updateNote();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNoteAdded() {
        Toast.makeText(this, getString(R.string.update_note), Toast.LENGTH_SHORT).show();

        Intent i = new Intent(JournalDetailActivity.this, JournalListActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    @Override
    public void onDataNotAvailable() {
        Toast.makeText(this, getString(R.string.not_update_note), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
