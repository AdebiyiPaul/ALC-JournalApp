package journal.alc.com.parlegrandpa.journalapp.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "notes")
public class Note {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "note")
    private String note;

    @ColumnInfo(name = "created_date")
    private String created_date;

    @ColumnInfo(name = "user_email")
    private String user_email;

    @Ignore
    public Note(){
    }

    @Ignore
    public Note(int id, String title, String note, String created_date, String user_email){
        this.id = id;
        this.title = title;
        this.note = note;
        this.created_date = created_date;
        this.user_email = user_email;
    }

    public Note(String title, String note, String created_date, String user_email){
        this.title = title;
        this.note = note;
        this.created_date = created_date;
        this.created_date = created_date;
        this.user_email = user_email;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getNote() {
        return note;
    }

    public String getCreated_date()
    {
        return created_date;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setCreated_date(String created_date)
    {
        this.created_date = created_date;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }
}
