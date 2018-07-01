package journal.alc.com.parlegrandpa.journalapp.interfaces;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Maybe;
import journal.alc.com.parlegrandpa.journalapp.model.Note;

@Dao
public interface NoteDao {

    @Query("SELECT * FROM notes WHERE user_email = :user_email ORDER BY id DESC")
    Maybe<List<Note>> getAll(String user_email);

    @Insert
    void insertAll(Note... notes);

    @Delete
    void deleteNote(Note... notes);

    @Query("UPDATE notes SET title = :title, note = :note WHERE id = :note_id")
            void updateNote(int note_id, String title, String note);
}
