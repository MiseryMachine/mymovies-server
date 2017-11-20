package com.rjs.mymovies.server.model.mdb;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * <p/>
 * Created with IntelliJ IDEA.<br>
 * User: Randy Strobel<br>
 * Date: 2/7/2017<br>
 * Time: 12:53 PM<br>
 */
@XmlRootElement
public class MdbGenre implements Serializable {
	public int id;
	public String name;
}
