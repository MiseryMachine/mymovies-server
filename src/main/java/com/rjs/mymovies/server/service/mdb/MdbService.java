package com.rjs.mymovies.server.service.mdb;


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
public interface MdbService {
	Iterable<MdbShow> searchShows(String showTypeName, String title);

	Show addShow(String showTypeName, String mdbId);

	Set<String> getGenres(String showTypeName);
}
