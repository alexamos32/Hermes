package ca.unb.mobiledev.hermes;

public class Folder {
    private int id;
    private int parentID;
    private String name;

    public Folder(int id, int parentID, String name)
    {
        this.id = id;
        this.parentID = parentID;
        this.name = name;
    }
    public Folder(int parentID, String name)
    {
        this.parentID = parentID;
        this.name = name;
    }

    public int getID(){ return id; }
    public int getParentID(){ return parentID; }
    public String getFolderName(){ return name; }
    public void setID(int id){ id = this.id; }
    public void setParentID(int parentID){ parentID = this.parentID; }
    public void setFolderName(String name){ name = this.name; }
}
