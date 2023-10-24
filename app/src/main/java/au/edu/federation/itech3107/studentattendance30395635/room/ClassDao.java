package au.edu.federation.itech3107.studentattendance30395635.room;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Maybe;


@Dao
public interface ClassDao {
    @Query("SELECT * FROM classbean")
    Maybe<List<ClassBean>> getAllUsers();

    @Insert()
    void insert(ClassBean... users);

    @Update
    void update(ClassBean... users);

    @Delete
    void delete(ClassBean... users);

}
