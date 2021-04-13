package ca.unb.mobiledev.hermes;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;



import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Detail extends AppCompatActivity {
    long id;
    String content;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent startingIntent = getIntent();
        id = startingIntent.getLongExtra("ID", 0);
        NoteDatabase db = new NoteDatabase(this);
        Note note = db.getNote(id);

        getSupportActionBar().setTitle(note.getTitle());
        TextView details = findViewById(R.id.noteDesc);

        //Setting the content for Details
        content = note.getContent();
        MarkdownRender mdRenderer = new MarkdownRender();
        content = mdRenderer.render(content);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            details.setText(Html.fromHtml(content,Html.FROM_HTML_MODE_LEGACY));
        }
        else {
            details.setText(Html.fromHtml(content));
        }

        //details.setText(note.getContent());
        details.setMovementMethod(new ScrollingMovementMethod());

        FloatingActionButton faButton = findViewById(R.id.fab);
        faButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NoteDatabase db = new NoteDatabase(getApplicationContext());
                db.deleteNote(id);
                Toast.makeText(getApplicationContext(), "Note Deleted", Toast.LENGTH_SHORT).show();
                //return to main
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == R.id.edit) {
            Intent intent = new Intent(this, Edit.class);
            intent.putExtra("ID", id);
            intent.putExtra("parentID", getIntent().getLongExtra("parentID", -1));
            startActivity(intent);
        }
        else if (item.getItemId() == R.id.email){
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setType("text/html");
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, getSupportActionBar().getTitle());
            emailIntent.putExtra(Intent.EXTRA_TEXT, content);
            startActivity(Intent.createChooser(emailIntent, "Email:"));
        }
        return super.onOptionsItemSelected(item);
    }
}
