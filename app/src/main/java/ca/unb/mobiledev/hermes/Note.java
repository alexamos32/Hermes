package ca.unb.mobiledev.hermes;

public class Note {
    private long id;
    private String title;
    private String content;
    private String date;
    private String time;
    private String remTime;
    private String remDate;

    Note(String title, String content, String date, String time){
        this.title = title;
        this.content = content;
        this.date = date;
        this.time = time;
        this.remTime = "ignore";
        this.remDate = "ignore";
    }

    Note(long id,String title,String content,String date, String time){
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
        this.time = time;
        this.remTime = "ignore";
        this.remDate = "ignore";
    }
    Note(long id,String title,String content,String date, String time, String remTime, String remDate){
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
        this.time = time;
        this.remTime = remTime;
        this.remDate = remDate;
    }
    Note(String title,String content,String date, String time, String remTime, String remDate){
        this.title = title;
        this.content = content;
        this.date = date;
        this.time = time;
        this.remTime = remTime;
        this.remDate = remDate;
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

    public String getRemTime(){
        return remTime;
    }

    public void setRemTime(String remTime){
        this.remTime = remTime;
    }

    public String getRemDate(){ return remDate; }

    public void setRemDate(String remDate){
        this.remDate = remDate;
    }



}
