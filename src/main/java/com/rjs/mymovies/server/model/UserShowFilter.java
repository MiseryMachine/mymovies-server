package com.rjs.mymovies.server.model;

import com.rjs.mymovies.server.model.dto.ShowFilterDto;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "user_show_filter", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "name"}))
public class UserShowFilter extends AbstractElement {
    private User user;
    private String name;
    private final ShowFilterDto showFilterDto;

    public UserShowFilter() {
        showFilterDto = new ShowFilterDto();
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Column(length = 40, nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "show_type", length = 40, nullable = false)
    public String getShowTypeName() {
        return showFilterDto.getShowTypeName();
    }

    public void setShowTypeName(String showTypeName) {
        showFilterDto.setShowTypeName(showTypeName);
    }

    public String getTitle() {
        return showFilterDto.getTitle();
    }

    public void setTitle(String title) {
        showFilterDto.setTitle(title);
    }

    @Column(name = "star_rating")
    public int getStarRating() {
        return showFilterDto.getStarRating();
    }

    public void setStarRating(int starRating) {
        showFilterDto.setStarRating(starRating);
    }

    @Column(name = "media_format")
    public String getFormat() {
        return showFilterDto.getFormat();
    }

    public void setFormat(String format) {
        showFilterDto.setFormat(format);
    }

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_show_filter_genre", joinColumns = @JoinColumn(name = "user_show_filter_id"))
    @Column(name = "genre", length = 40, nullable = false)
    @OrderBy(value = "genre")
    public Set<String> getGenres() {
        return showFilterDto.getGenres();
    }

    public void setGenres(Set<String> genres) {
        showFilterDto.setGenres(genres);
    }

    @Transient
    public ShowFilterDto getShowFilterDto() {
        return showFilterDto;
    }
}
