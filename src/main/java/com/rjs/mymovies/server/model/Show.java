package com.rjs.mymovies.server.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * <p/>
 * Created with IntelliJ IDEA.<br>
 * User: Randy Strobel<br>
 * Date: 2017-07-06<br>
 * Time: 12:20<br>
 */
@Entity
public class Show extends AbstractElement {
	private String mdbId;
	private String imdbId;
	@NotNull(message = "Show must have a title.")
	private String title;
	private String tagLine;
	private String description;
	@NotNull(message = "Show must have a medium.")
	private Medium medium;
	private Date releaseDate;
	private int runtime = 0;
	private Set<Genre> genres = new LinkedHashSet<>();
	private String imageUrl;
	private String mediaFormat;
	private String myNotes;
	private double myRating = 0.0;

	public Show() {
	}

	@Column(name = "mdb_id")
	public String getMdbId() {
		return mdbId;
	}

	public void setMdbId(String mdbId) {
		this.mdbId = mdbId;
	}

	@Column(name = "imdb_id")
	public String getImdbId() {
		return imdbId;
	}

	public void setImdbId(String imdbId) {
		this.imdbId = imdbId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "tag_line", length = 511)
	public String getTagLine() {
		return tagLine;
	}

	public void setTagLine(String tagLine) {
		this.tagLine = tagLine;
	}

	@Column(length = 2000)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Enumerated(EnumType.STRING)
	public Medium getMedium() {
		return medium;
	}

	public void setMedium(Medium medium) {
		this.medium = medium;
	}

	@Column(name = "release_date")
	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	public int getRuntime() {
		return runtime;
	}

	public void setRuntime(int runtime) {
		this.runtime = runtime;
	}

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "show_genre", joinColumns = @JoinColumn(name = "show_id"))
	@Column(name = "genre")
	public Set<Genre> getGenres() {
		return genres;
	}

	public void setGenres(Set<Genre> genres) {
		this.genres = genres;
	}

	@Column(name = "image_url")
	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	@Column(name = "media_format")
	public String getMediaFormat() {
		return mediaFormat;
	}

	public void setMediaFormat(String mediaFormat) {
		this.mediaFormat = mediaFormat;
	}

	@Column(name = "my_notes", length = 2000)
	public String getMyNotes() {
		return myNotes;
	}

	public void setMyNotes(String myNotes) {
		this.myNotes = myNotes;
	}

	@Column(name = "my_rating")
	public double getMyRating() {
		return myRating;
	}

	public void setMyRating(double myRating) {
		this.myRating = myRating;
	}
}
