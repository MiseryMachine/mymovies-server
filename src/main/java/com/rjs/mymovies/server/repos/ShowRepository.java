package com.rjs.mymovies.server.repos;


import com.rjs.mymovies.server.model.Genre;
import com.rjs.mymovies.server.model.Medium;
import com.rjs.mymovies.server.model.Show;

import java.util.List;

/**
 * <p/>
 * Created with IntelliJ IDEA.<br>
 * User: Randy Strobel<br>
 * Date: 2017-07-06<br>
 * Time: 12:36<br>
 */
public interface ShowRepository extends BaseRepository<Show> {
	Show findByMdbId(String mdbId);
	List<Show> findByTitle(String title);
	List<Show> findByTitleAndMedium(String title, Medium medium);
	List<Show> findByGenresIn(List<Genre> genres);
	List<Show> findByGenresInAndTitleLike(List<Genre> genres, String title);
	List<Show> findByMyRatingGreaterThanEqual(double myRating);
	List<Show> findByGenresAndMyRatingGreaterThanEqual(List<Genre> genres, double myRating);
}
