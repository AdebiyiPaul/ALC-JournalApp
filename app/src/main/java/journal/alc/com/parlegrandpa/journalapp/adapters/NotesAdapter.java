package journal.alc.com.parlegrandpa.journalapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import journal.alc.com.parlegrandpa.journalapp.R;
import journal.alc.com.parlegrandpa.journalapp.model.Note;
import journal.alc.com.parlegrandpa.journalapp.interfaces.OnItemClickListener;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {

    private Context context;
    private List<Note> noteList = new ArrayList<>();
    private OnItemClickListener listener;

    public NotesAdapter(Context context, List<Note> noteList, OnItemClickListener listener) {
        this.context = context;
        this.noteList = noteList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.row_note,parent,false);
        final NotesViewHolder myViewHolder = new NotesViewHolder(itemView);
        itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, myViewHolder.getLayoutPosition());
            }
        });
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        holder.bind(noteList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public class NotesViewHolder extends RecyclerView.ViewHolder{

        TextView title,timeStamp;
        public NotesViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            timeStamp = itemView.findViewById(R.id.timestamp);
        }

        public void bind(final Note note, final OnItemClickListener listener) {
            title.setText(note.getTitle());
            timeStamp.setText(note.getCreated_date());
        }
    }
}
