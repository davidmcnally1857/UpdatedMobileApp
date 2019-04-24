package android.example.caproject;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface TopicDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addTopic(Topics topic);

    @Query("SELECT * FROM Topics")
    public List<Topics> getAllTopics();

    @Query("SELECT * FROM Topics WHERE ParentTopic = 0")
    public List<Topics> getAllTopicsWherParentTopicsIsZero();

    @Query("SELECT * FROM Topics WHERE ParentTopic !=0")
    public List<Topics> getAllTopicsWhereParentTopisIsNotZero();



}
