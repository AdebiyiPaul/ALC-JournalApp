package journal.alc.com.parlegrandpa.journalapp.interfaces;

import java.util.List;

import journal.alc.com.parlegrandpa.journalapp.model.Note;

public interface MainViewInterface {

    void onNotesLoaded(List<Note> notes);

    void onNoteAdded();

    void onDataNotAvailable();
}
