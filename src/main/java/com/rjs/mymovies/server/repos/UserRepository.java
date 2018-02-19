package com.rjs.mymovies.server.repos;


import com.rjs.mymovies.server.model.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * <p/>
 * Created with IntelliJ IDEA.<br>
 * User: Randy Strobel<br>
 * Date: 2017-07-06<br>
 * Time: 12:58<br>
 */
public interface UserRepository extends BaseRepository<User>, JpaSpecificationExecutor<User> {
	User findByUsername(String username);
	User findByUsernameAndPassword(String username, String password);
}
