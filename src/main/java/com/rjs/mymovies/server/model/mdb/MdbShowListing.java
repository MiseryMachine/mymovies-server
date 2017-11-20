package com.rjs.mymovies.server.model.mdb;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * <p/>
 * Created with IntelliJ IDEA.<br>
 * User: Randy Strobel<br>
 * Date: 2/7/2017<br>
 * Time: 3:36 PM<br>
 */
@XmlRootElement
public class MdbShowListing {
	public int page;
	public List<MdbShow> results;
}
