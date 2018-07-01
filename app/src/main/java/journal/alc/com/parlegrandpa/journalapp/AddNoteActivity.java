package journal.alc.com.parlegrandpa.journalapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import journal.alc.com.parlegrandpa.journalapp.room.LocalCacheManager;
import journal.alc.com.parlegrandpa.journalapp.interfaces.AddNoteViewInterface;

public class AddNoteActivity extends AppCompatActivity implements AddNoteViewInterface {

    @BindView(R.id.etTitle)
    EditText etTitle;

    @BindView(R.id.etNote)
    EditText etNote;

    private String user_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        ButterKnife.bind(this);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        SharedPreferences pref = getApplicationContext().getSharedPreferences("UserInfoPreference", 0);
        user_email = pref.getString("user_email", null);
    }


    private void saveNote(){

        String title = etTitle.getText().toString();
        String note_text = etNote.getText().toString();
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

        if(title.equals("") || note_text.equals("")){
            showToast("Please fill all the fields before saving");
        }else{
            //Call Method to add note

            LocalCacheManager.getInstance(this).addNotes(this, title, note_text, currentDateTimeString, user_email);
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
            saveNote();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNoteAdded() {
        Toast.makeText(this,"Note Added",Toast.LENGTH_SHORT).show();

        Intent i = new Intent(AddNoteActivity.this, JournalListActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    @Override
    public void onDataNotAvailable() {
        Toast.makeText(this,"Could not add note",Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
