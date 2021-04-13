package ca.unb.mobiledev.hermes;

import android.content.res.Resources;
import android.view.ViewGroup;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private LayoutInflater inflater;
    private List<Note> notes;
    private List<Folder> folders;
    private Context context;

    Adapter(Context context,List<Note> notes,List<Folder> folders){
        this.inflater = LayoutInflater.from(context);
        this.notes = notes;
        this.folders = folders;
        this.context = context;
    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_list_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int i) {
        if (i < notes.size()) {
            String title = notes.get(i).getTitle();
            String date = notes.get(i).getDate();
            String time = notes.get(i).getTime();
            long id = notes.get(i).getId();

            Log.d("date on ", "Date on: " + date);

            holder.nTitle.setText(title);
            holder.nDate.setText(date);
            holder.nTime.setText(time);
            holder.nID.setText(String.valueOf(notes.get(i).getId()));
        }
        else
        {
            String folderName = folders.get(i - notes.size()).getFolderName();
            holder.nTitle.setText(folderName);
            holder.nDate.setText("");
            holder.nTime.setText("");
            holder.nImage.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.folder, null));
        }
    }

    @Override
    public int getItemCount() {
        return notes.size() + folders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView nTitle,nDate,nTime,nID;
        ImageView nImage;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            nTitle  = itemView.findViewById(R.id.nTitle);
            nDate   = itemView.findViewById(R.id.nDate);
            nTime   = itemView.findViewById(R.id.nTime);
            nID     = itemView.findViewById(R.id.listId);
            nImage  = itemView.findViewById(R.id.imageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getAdapterPosition() < notes.size()) {
                        Intent i = new Intent(v.getContext(), Detail.class);
                        i.putExtra("ID", notes.get(getAdapterPosition()).getId());
                        i.putExtra("parentID", notes.get(getAdapterPosition()).getFolderID());
                        v.getContext().startActivity(i);
                    }
                    else
                    {
                        Intent i = new Intent(v.getContext(), MainActivity.class);
                        i.putExtra("parentID", folders.get(getAdapterPosition() - notes.size()).getID());
                        v.getContext().startActivity(i);
                    }
                }
            });
        }
    }
}
