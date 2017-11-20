package com.rjs.mymovies.server.repos;


import com.rjs.mymovies.server.model.MediaFormat;

/**
 * <p/>
 * Created with IntelliJ IDEA.<br>
 * User: Randy Strobel<br>
 * Date: 2017-07-06<br>
 * Time: 12:35<br>
 */
public interface MediaFormatRepository extends BaseRepository<MediaFormat> {
	MediaFormat findByName(String name);
}
