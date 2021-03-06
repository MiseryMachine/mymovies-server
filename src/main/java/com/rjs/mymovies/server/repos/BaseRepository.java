package com.rjs.mymovies.server.repos;

import com.rjs.mymovies.server.model.AbstractElement;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * <p/>
 * Created with IntelliJ IDEA.<br>
 * User: Randy Strobel<br>
 * Date: 2017-07-06<br>
 * Time: 11:36<br>
 */
public interface BaseRepository<E extends AbstractElement> extends JpaRepository<E, Long> {
}
