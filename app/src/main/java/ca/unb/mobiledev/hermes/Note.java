package ca.unb.mobiledev.hermes;

public class Note {
    private long id;
    private String title;
    private String content;
    private String date;
    private String time;
    private long folderID;

    Note(String title, String content, String date, String time, long folderID){
        this.title = title;
        this.content = content;
        this.date = date;
        this.time = time;
        this.folderID = folderID;
    }

    Note(long id,String title,String content,String date, String time, long folderID){
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
        this.time = time;
        this.folderID = folderID;
    }

    Note() {
        //empty constructor
    }

    public long getId(){
        return id;
    }

    public void setId(long id){
        this.id = id;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getContent(){
        return content;
    }

    public void setContent(String id){
        this.content = content;
    }

    public String getDate(){
        return date;
    }

    public void setDate(String date){
        this.date = date;
    }

    public String getTime(){
        return time;
    }

    public void setTime(String time){
        this.time = time;
    }

    public long getFolderID(){
        return folderID;
    }

    public void setFolderID(long folderID){
        this.folderID = folderID;
    }
}
