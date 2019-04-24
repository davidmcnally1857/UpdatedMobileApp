package android.example.caproject;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Module {

    @PrimaryKey
    public final int Module_ID;
    public String Module_Code;
    public String Module_Name;
    public String Course;
    public String Course_Intake;
    public String Lecturer;
    public String Start_Date;
    public String End_Date;
    public String Start;
    public String End;



    public Module(int Module_ID, String Module_Code, String Module_Name, String Course,
                  String Course_Intake, String Lecturer, String Start_Date, String End_Date, String Start, String End){

        this.Module_ID = Module_ID;
        this.Module_Code = Module_Code;
        this.Module_Name = Module_Name;
        this.Course = Course;
        this.Course_Intake = Course_Intake;
        this.Lecturer = Lecturer;
        this.Start_Date = Start_Date;
        this.End_Date = End_Date;
        this.Start = Start;
        this.End = End;

     }



}
