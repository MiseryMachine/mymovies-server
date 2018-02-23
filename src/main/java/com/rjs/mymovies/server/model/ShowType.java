package com.rjs.mymovies.server.model;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "show_type")
public class ShowType extends AbstractElement {
    private String name;
    private Set<String> genres = new LinkedHashSet<>();

    public ShowType() {
    }

    @Column(unique = true, nullable = false, length = 20)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "show_type_genre", joinColumns = @JoinColumn(name = "show_type_id"))
    @Column(name = "genre", length = 40, nullable = false)
    @OrderBy(value = "genre")
    public Set<String> getGenres() {
        return genres;
    }

    public void setGenres(Set<String> genres) {
        this.genres = genres;
    }
}
