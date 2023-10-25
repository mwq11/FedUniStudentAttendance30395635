package au.edu.federation.itech3107.studentattendance30395635.room;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Maybe;


@Dao
public interface CourseDao {
    @Query("SELECT * FROM coursev2 where groupId = :id")
    Maybe<List<CourseV2>> getAllUsers(int id);

    @Query("SELECT * FROM coursev2 where couId = :id")
    Maybe<CourseV2> getCourseById(int id);

    @Insert()
    void insert(CourseV2... users);

    @Update
    void update(CourseV2... users);

    @Delete
    void delete(CourseV2... users);

}
