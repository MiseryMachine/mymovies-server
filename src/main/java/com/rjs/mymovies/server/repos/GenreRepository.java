package com.rjs.mymovies.server.repos;


import com.rjs.mymovies.server.model.Genre;
import com.rjs.mymovies.server.model.Medium;

import java.util.List;

/**
 * <p/>
 * Created with IntelliJ IDEA.<br>
 * User: Randy Strobel<br>
 * Date: 2017-07-06<br>
 * Time: 11:37<br>
 */
public interface GenreRepository extends BaseRepository<Genre> {
	List<Genre> findByMedium(Medium medium);
	List<Genre> findByName(String name);
	List<Genre> findById(List<Long> ids);
	List<Genre> findByMdbIdInAndMedium(List<String> mbIds, Medium medium);
	Genre findByNameAndMedium(String name, Medium medium);
}
