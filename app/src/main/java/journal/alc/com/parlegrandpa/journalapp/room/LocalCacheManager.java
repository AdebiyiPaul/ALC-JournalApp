package journal.alc.com.parlegrandpa.journalapp.room;

import android.annotation.SuppressLint;
import android.content.Context;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import journal.alc.com.parlegrandpa.journalapp.model.Note;
import journal.alc.com.parlegrandpa.journalapp.interfaces.AddNoteViewInterface;
import journal.alc.com.parlegrandpa.journalapp.interfaces.MainViewInterface;

public class LocalCacheManager {
    private Context context;
    private static LocalCacheManager _instance;
    private AppDatabase db;

    public static LocalCacheManager getInstance(Context context) {
        if (_instance == null) {
            _instance = new LocalCacheManager(context);
        }
        return _instance;
    }

    public LocalCacheManager(Context context) {
        this.context = context;
        db = AppDatabase.getAppDatabase(context);
    }

    @SuppressLint("CheckResult")
    public void getNotes(final MainViewInterface mainViewInterface) {
        db.noteDao().getAll().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<Note>>() {
            @Override
            public void accept(List<Note> notes) throws Exception {
                mainViewInterface.onNotesLoaded(notes);
            }
        });
    }

    public void addNotes(final AddNoteViewInterface addNoteViewInterface, final String note_title, final String note_text, final String created_date) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                Note note = new Note(note_title, note_text, created_date);
                db.noteDao().insertAll(note);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onComplete() {
                addNoteViewInterface.onNoteAdded();
            }

            @Override
            public void onError(Throwable e) {
                addNoteViewInterface.onDataNotAvailable();
            }
        });
    }

    public void updateNotes(final AddNoteViewInterface addNoteViewInterface, final String note_title, final String note_text, final int note_id) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                db.noteDao().updateNote(note_id, note_title, note_text);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onComplete() {
                addNoteViewInterface.onNoteAdded();
            }

            @Override
            public void onError(Throwable e) {
                addNoteViewInterface.onDataNotAvailable();
            }
        });
    }

    public void deleteNote(Note note)  {
        db.noteDao().deleteNote(note);
    }
}

