package com.rjs.mymovies.server.repos;


import com.rjs.mymovies.server.model.Show;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * <p/>
 * Created with IntelliJ IDEA.<br>
 * User: Randy Strobel<br>
 * Date: 2017-07-06<br>
 * Time: 12:36<br>
 */
public interface ShowRepository extends BaseRepository<Show>, JpaSpecificationExecutor<Show> {
	Show findByMdbId(String mdbId);
	List<Show> findByTitle(String title);
}
