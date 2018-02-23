package com.rjs.mymovies.server.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

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
	private Date releaseDate;
	private int runtime = 0;
	private String showType;
	private Set<String> genres = new LinkedHashSet<>();
	private String imageUrl;
	private String mediaFormat;
	private String myNotes;
	private int myRating = 0;

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

	@Column(name = "show_type", length = 40, nullable = false)
	public String getShowType() {
		return showType;
	}

	public void setShowType(String showType) {
		this.showType = showType;
	}

	@ElementCollection
	@CollectionTable(name = "show_genre", joinColumns = @JoinColumn(name = "show_id"))
	@Column(name = "genre", length = 40, nullable = false)
	@OrderBy(value = "genre")
	public Set<String> getGenres() {
		return genres;
	}

	public void setGenres(Set<String> genres) {
		this.genres = genres;
	}

	@Transient
	public String getGenreText() {
		if (genres == null || genres.isEmpty()) {
			return "No Genres";
		}

		return genres.stream().collect(Collectors.joining(", "));
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
	public int getMyRating() {
		return myRating;
	}

	public void setMyRating(int myRating) {
		this.myRating = myRating;
	}
}
