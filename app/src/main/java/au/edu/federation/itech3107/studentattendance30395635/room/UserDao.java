package au.edu.federation.itech3107.studentattendance30395635.room;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;


@Dao
public interface UserDao {
    @Query("SELECT * FROM userbean")
    Flowable<List<UserBean>> getAllUsers();

    @Insert()
    void insert(UserBean... users);

    @Update
    void update(UserBean... users);

    @Delete
    void delete(UserBean... users);

//    @Query("SELECT * FROM userbean WHERE defaultLogin = 1")
//    Flowable<UserBean> getDefaultLoginUser();

    @Query("SELECT * FROM userbean WHERE name = :loginName")
    Maybe<List<UserBean>> getUserByName(String loginName);
}
