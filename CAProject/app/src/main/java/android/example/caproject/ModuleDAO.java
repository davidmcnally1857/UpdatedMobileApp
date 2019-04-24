package android.example.caproject;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface ModuleDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addModule(Module module);

    @Query("SELECT * FROM Module")
    public List<Module> getAllModules();

    @Query("SELECT * FROM Module LIMIT 1")
    public Module getModule();


    @Query("SELECT * FROM Module WHERE Module_ID = :Module_ID")
    public Module getModule(long Module_ID);


    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateModule(Module module);

    @Query("DELETE FROM Module")
    void removeAllModules();

}
