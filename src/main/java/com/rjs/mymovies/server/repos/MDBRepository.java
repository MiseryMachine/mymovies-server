package com.rjs.mymovies.server.repos;


import com.rjs.mymovies.server.model.Genre;
import com.rjs.mymovies.server.model.Medium;
import com.rjs.mymovies.server.model.Show;
import com.rjs.mymovies.server.model.mdb.MdbShow;

import java.util.Set;

/**
 * <p/>
 * Created with IntelliJ IDEA.<br>
 * User: Randy Strobel<br>
 * Date: 2/7/2017<br>
 * Time: 11:22 AM<br>
 */
public interface MDBRepository {
	Iterable<MdbShow> searchShows(String title);
	Iterable<MdbShow> searchShows(Medium medium, String title);

	Show addShow(Medium medium, String mdbId);

	Set<Genre> getGenres(Medium medium);
}
