package com.rjs.mymovies.server.service;

import com.rjs.mymovies.server.config.AppConfig;
import com.rjs.mymovies.server.model.Show;
import com.rjs.mymovies.server.model.UserShowFilter;
import com.rjs.mymovies.server.repos.ShowRepository;
import com.rjs.mymovies.server.repos.UserShowFilterRepository;
import com.rjs.mymovies.server.repos.filter.AtomicDataFilter;
import com.rjs.mymovies.server.repos.filter.CollectionDataFilter;
import com.rjs.mymovies.server.repos.filter.NumericDateDataFilter;
import com.rjs.mymovies.server.repos.filter.StringDataFilter;
import com.rjs.mymovies.server.repos.specification.DataSpecification;
import com.rjs.mymovies.server.util.ImageUtil;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ShowService extends BaseService<Show, ShowRepository> {
    private static final Logger LOGGER = Logger.getLogger(ShowService.class.getName());

    @Autowired
    private AppConfig appConfig;
    @Autowired
    private File defaultBoxArt;
    @Autowired
    private File defaultBoxArtThumb;
    @Autowired
    private UserShowFilterRepository userShowFilterRepository;

    @Autowired
    public ShowService(ShowRepository showRepository) {
        super(showRepository);
    }

    public Show findByMdbId(String mdbId) {
        return ((ShowRepository) repository).findByMdbId(mdbId);
    }

    public List<Show> findByTitle(String title) {
        return ((ShowRepository) repository).findByTitle(title);
    }

    public List<Show> searchShows(String showTypeName, Map<String, Object> filter) {
        return searchShows(showTypeName, filter, null);
    }

    public List<Show> searchShows(String showTypeName, Map<String, Object> filter, Sort sort) {
        if (StringUtils.isNotBlank(showTypeName)) {
            Specification<Show> spec = buildShowSpecification(showTypeName, filter);

            if (spec != null) {
                return sort != null ? ((ShowRepository) repository).findAll(spec, sort) : ((ShowRepository) repository).findAll(spec);
            }
        }

        return new ArrayList<>();
    }

    public byte[] getShowPosterData(Long showId, boolean thumb) {
        if (showId == null) {
            return new byte[0];
        }

        String localPosterPath = appConfig.getLocalImagePath(String.valueOf(showId));
        String ext = ".png";
        File posterFile = new File(localPosterPath, "poster" + ext);
        String imageType = ImageUtil.PNG;

        if (!posterFile.exists()) {
            ext = ".jpg";
            posterFile = new File(localPosterPath, "poster" + ext);
            imageType = ImageUtil.JPEG;
        }

        if (!posterFile.exists()) {
            ext = ".jpeg";
            posterFile = new File(localPosterPath, "poster" + ext);
            imageType = ImageUtil.JPEG;
        }

        File boxArtFile = null;
        FileInputStream fis = null;
        byte[] imgData = new byte[0];

        try {
            if (thumb) {
                if (posterFile.exists()) {
                    boxArtFile = new File(localPosterPath, "poster_thumb" + ext);

                    // Generate a thumb image from poster, if necessary.
                    if (!boxArtFile.exists()) {
                        BufferedImage poster = ImageIO.read(posterFile);
                        BufferedImage thumbImage = ImageUtil.createThumbImage(poster);
                        ImageIO.write(thumbImage, imageType, new FileOutputStream(boxArtFile));
                    }

                    return IOUtils.toByteArray(new FileInputStream(boxArtFile));
                }
                else if (defaultBoxArtThumb != null) {
                    boxArtFile = defaultBoxArtThumb;
                }
            }
            else {
                if (posterFile.exists()) {
                    boxArtFile = posterFile;
                }
                else if (defaultBoxArt != null) {
                    boxArtFile = defaultBoxArt;
                }
            }

            if (boxArtFile != null) {
                fis = new FileInputStream(boxArtFile);
                imgData = IOUtils.toByteArray(new FileInputStream(boxArtFile));
            }
        }
        catch (IOException e) {
            LOGGER.warning("Unable to load poster image from file: " + posterFile.getAbsolutePath());
        }
        finally {
            if (fis != null) {
                try {
                    fis.close();
                }
                catch (IOException e) {
                    LOGGER.log(Level.SEVERE, "Error closing file input stream for " + boxArtFile.getName(), e);
                }
            }
        }

        return imgData;
    }

    public UserShowFilter getUserShowFilter(Long userFilterId) {
        return userShowFilterRepository.findOne(userFilterId);
    }

    private Specification<Show> buildShowSpecification(String showTypeName, Map<String, Object> params) {
        if (StringUtils.isBlank(showTypeName)) {
            return null;
        }

        Specification<Show> spec = new DataSpecification<>(new AtomicDataFilter("showType", AtomicDataFilter.EQ_OPERATOR, showTypeName));

        if (params != null && !params.isEmpty()) {
            Set<String> paramNames = params.keySet();

            for (String paramName : paramNames) {
                switch (paramName) {
                    case "title":
                        String titleValue = (String) params.get(paramName);

                        if (StringUtils.isNotEmpty(titleValue)) {
                            spec = Specifications.where(spec)
                                .and(new DataSpecification<>(new StringDataFilter("title", StringDataFilter.LIKE_OPERATOR, titleValue)));
                        }

                        break;

                    case "genres":
/*
                        String[] genreValues = (String[]) params.get(paramName);

                        if (genreValues != null && genreValues.length > 0) {
                            Set<String> genreSet = Arrays.stream(genreValues).collect(Collectors.toSet());
                            spec = Specifications.where(spec)
                                .and(new DataSpecification<>(new CollectionDataFilter("genres", CollectionDataFilter.CONTAINS_OPERATOR, genreSet)));
                        }
                        else {
                            LOGGER.info("Show genres parameter is empty.");
                        }
*/
                        Set<String> genreValues = (Set<String>) params.get(paramName);

                        if (genreValues != null && !genreValues.isEmpty()) {
                            spec = Specifications.where(spec)
                                .and(new DataSpecification<>(new CollectionDataFilter("genres", CollectionDataFilter.CONTAINS_OPERATOR, genreValues)));
                        }
                        else {
                            LOGGER.info("Show genres parameter is empty.");
                        }

                        break;

                    case "mediaFormat":
                        String formatValue = (String) params.get(paramName);

                        if (StringUtils.isNotEmpty(formatValue)) {
                            if (!formatValue.equalsIgnoreCase("any")) {
                                spec = Specifications.where(spec)
                                    .and(new DataSpecification<>(new StringDataFilter("mediaFormat", StringDataFilter.EQ_OPERATOR, formatValue)));
                            }
                        }
                        else {
                            LOGGER.info("Media format parameter is empty.");
                        }

                        break;

                    case "starRating":
                        Integer ratingValue = (Integer) params.get(paramName);

                        if (ratingValue != null) {
                            spec = Specifications.where(spec)
                                .and(new DataSpecification<>(new NumericDateDataFilter("starRating", NumericDateDataFilter.GTE_OPERATOR, ratingValue)));
                        }
                        else {
                            LOGGER.info("Rating parameter is null.");
                        }
                }
            }
        }

        return spec;
    }
}
