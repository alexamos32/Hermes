package ca.unb.mobiledev.hermes;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class AddNote extends AppCompatActivity {
    Toolbar toolbar;
    EditText noteTitle, noteDetails;
    Calendar calender;
    String currentDate;
    String currentTime;
    TextView htmlPreview;
    MenuItem previewButton, editButton;
    LinearLayout imageLayout;
    ArrayList<ImageView> imagesList;


    static final int REQUEST_IMAGE_CAPTURE = 1;
    //May remove later
    ImageView note_image;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("New Note");

        htmlPreview = findViewById(R.id.htmlPreview);

        noteDetails = findViewById(R.id.noteDetails);
        noteTitle = findViewById(R.id.noteTitle);

        noteTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() != 0){
                    getSupportActionBar().setTitle(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //REMOVE LATER
        imageLayout = findViewById(R.id.image_layout);
        imagesList = new ArrayList<ImageView>();

        calender = Calendar.getInstance();
        currentDate = calender.get(Calendar.YEAR) + "/" + (calender.get(Calendar.MONTH)+1) + "/" + calender.get(Calendar.DAY_OF_MONTH);
        Log.d("DATE", "Date: " + currentDate);
        currentTime = pad(calender.get(Calendar.HOUR)) + ":" + pad(calender.get(Calendar.MINUTE));
        Log.d("TIME", "Time: "+ currentTime);

    }

    private String pad(int time){
        if(time < 10){
            return "0"+time;
        }
        return String.valueOf(time);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu,menu);

        editButton = menu.findItem(R.id.editText);
        previewButton = menu.findItem(R.id.preview);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.save){
            if(noteTitle.getText().length() != 0){
                Note note = new Note(noteTitle.getText().toString(), noteDetails.getText().toString(), currentDate, currentTime);
                NoteDatabase db = new NoteDatabase(this);
                long id = db.addNote(note);
                Note check = db.getNote(id);
                Log.d("inserted", "Note: "+ id + ", Title: " + check.getTitle() + ", Date: " + check.getDate() + ", Time: " + check.getTime());
                onBackPressed();

                Toast.makeText(this, "Note Saved.", Toast.LENGTH_SHORT).show();

            }
            else {
                noteTitle.setError("Title cannot be blank.");
            }
        }
        else if(item.getItemId() == R.id.delete){
            Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }

        else if (item.getItemId() == R.id.preview){
            String content = noteDetails.getText().toString();
            MarkdownRender mdRenderer = new MarkdownRender();
            String input = mdRenderer.render(content);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                htmlPreview.setText(Html.fromHtml(input,Html.FROM_HTML_MODE_LEGACY));
            }
            else {
                htmlPreview.setText(Html.fromHtml(input));
            }
            noteDetails.setVisibility(View.INVISIBLE);
            htmlPreview.setVisibility(View.VISIBLE);
            editButton.setVisible(true);
            item.setVisible(false);
        }
        else if (item.getItemId() == R.id.editText){
            noteDetails.setVisibility(View.VISIBLE);
            htmlPreview.setVisibility(View.INVISIBLE);
            previewButton.setVisible(true);
            item.setVisible(false);
        }
        else if (item.getItemId() == R.id.image_button){
            selectPictureOptions();
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    private void selectPictureOptions(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        CharSequence[] options = {"Take picture", "Select from gallery"};
        builder.setTitle("Image Options");
        builder.setItems(options, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0){
                    Toast.makeText(getApplicationContext(), "Take Picture", Toast.LENGTH_SHORT).show();
                    takePicture();

                }
                else if (which == 1){
                    Toast.makeText(getApplicationContext(), "Gallery", Toast.LENGTH_SHORT).show();

                }
                else{
                    Toast.makeText(getApplicationContext(), "Neither", Toast.LENGTH_SHORT).show();

                }
            }
        } );
        builder.show();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
     //   outState.putParcelableArrayList("ImageList", (ArrayList<? extends Parcelable>) imagesList);
    }



    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
       // imagesList = savedInstanceState.getParcelableArrayList("ImageList");
        Log.i("RESTORE:", Integer.toString(imagesList.size()));
        for (int i = 0; i < imagesList.size(); i++){
            imageLayout.addView(imagesList.get(i));
        }
    }

    //Change of plan save to private storage


    private void takePicture(){
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getPackageManager())!= null){
            File photo= null;
            try{
                photo = createImageFile();
            }
            catch (IOException e){
                //Put something here
                Toast.makeText(this, "Failed to make image file", Toast.LENGTH_SHORT).show();
            }

            if (photo != null){
                Uri photoUri = FileProvider.getUriForFile(this,
                        "ca.unb.mobiledev.hermes.provider",
                        photo);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(pictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    //Method to Save the Photo
    String currentPhotoPath;

    private File createImageFile() throws IOException{
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format((new Date()));
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        currentPhotoPath = image.getAbsolutePath();
        Log.i("IMAGE:", currentPhotoPath);
        return image;
    }

    private void galleryAddPic() {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(file);
        intent.setData(contentUri);
        this.sendBroadcast(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK){
           // Bundle extras = data.getExtras();
        //    Bitmap imageBitmap = (Bitmap)extras.get("data");
          //  note_image.setImageBitmap(imageBitmap);
            InputStream is;
            try {
                File imageFile = new File(currentPhotoPath);
                is = new FileInputStream(imageFile);
            }
            catch (FileNotFoundException e){
                return;
            }

            Bitmap image = BitmapFactory.decodeStream(is);

            ImageView iv = new ImageView(this);
            iv.setImageBitmap(image);
            imagesList.add(iv);
            imageLayout.addView(iv);

            Log.i("RESULT" , Integer.toString(imagesList.size()));

          
            //note_image.setImageBitmap(image);
        }
    }



  /*  @SuppressLint("LongLogTag")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            bitmapOptions);
                    note_image.setImageBitmap(bitmap);
                    String path = android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                Log.w("path of image from gallery......******************.........", picturePath+"");
                note_image.setImageBitmap(thumbnail);
            }
        }
    }*/

}
