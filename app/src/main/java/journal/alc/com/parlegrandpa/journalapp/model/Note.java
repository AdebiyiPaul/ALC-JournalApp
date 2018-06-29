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

    @Ignore
    public Note(){
    }

    @Ignore
    public Note(int id, String title, String note, String created_date){
        this.id = id;
        this.title = title;
        this.note = note;
        this.created_date = created_date;
    }

    public Note(String title, String note, String created_date){
        this.title = title;
        this.note = note;
        this.created_date = created_date;
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
}
