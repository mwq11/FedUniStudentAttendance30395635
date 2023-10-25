package au.edu.federation.itech3107.studentattendance30395635.room;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Maybe;


@Dao
public interface CourseGroupDao {
    @Query("SELECT * FROM coursegroupbean")
    Maybe<List<CourseGroupBean>> getAllUsers();

    @Insert()
    void insert(CourseGroupBean... users);

    @Update
    void update(CourseGroupBean... users);

    @Delete
    void delete(CourseGroupBean... users);

}
