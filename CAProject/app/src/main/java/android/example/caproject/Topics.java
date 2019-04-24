package android.example.caproject;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Topics {
    @PrimaryKey
    public int Topic_ID;
    public String Topic_Name;
    public int Subtopic;
    public int ParentTopic;
    public String Topic_Description;
    public int Module_ID;

    public Topics(int Topic_ID, String Topic_Name, int Subtopic, int ParentTopic,
                  String Topic_Description, int Module_ID){

        this.Topic_ID = Topic_ID;
        this.Topic_Name = Topic_Name;
        this.Subtopic = Subtopic;
        this.ParentTopic = ParentTopic;
        this.Topic_Description = Topic_Description;
        this.Module_ID = Module_ID;


    }

}
