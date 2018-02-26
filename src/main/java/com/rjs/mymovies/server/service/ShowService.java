package com.rjs.mymovies.server.service;

import com.rjs.mymovies.server.config.AppConfig;
import com.rjs.mymovies.server.model.Show;
import com.rjs.mymovies.server.repos.ShowRepository;
import com.rjs.mymovies.server.repos.filter.AtomicDataFilter;
import com.rjs.mymovies.server.repos.filter.CollectionDataFilter;
import com.rjs.mymovies.server.repos.filter.NumericDateDataFilter;
import com.rjs.mymovies.server.repos.filter.StringDataFilter;
import com.rjs.mymovies.server.repos.specification.DataSpecification;
import com.rjs.mymovies.server.util.ImageUtil;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

@Service
public class ShowService extends BaseService<Show, ShowRepository> {
    private static final Logger LOGGER = Logger.getLogger(ShowService.class.getName());

    @Autowired
    private AppConfig appConfig;

    @Autowired
    public ShowService(ShowRepository showRepository) {
        super(showRepository);
    }

    public Show findByMdbId(String mdbId) {
        return ((ShowRepository) repository).findByMdbId(mdbId);
    }

    public Page<Show> searchShowsPageable(Pageable pageable, String showTypeName, Map<String, Object> filter) {
        if (StringUtils.isNotBlank(showTypeName)) {
            Specification<Show> spec = buildShowSpecification(showTypeName, filter);

            return spec != null ? ((ShowRepository) repository).findAll(spec, pageable) : null;
        }

        return null;
    }

    public List<Show> findByTitle(String title) {
        return ((ShowRepository) repository).findByTitle(title);
    }

    public List<Show> searchShows(String showTypeName, Map<String, Object> filter) {
        if (StringUtils.isNotBlank(showTypeName)) {
            Specification<Show> spec = buildShowSpecification(showTypeName, filter);

            return spec != null ? ((ShowRepository) repository).findAll(spec) : new ArrayList<>();
        }

        return new ArrayList<>();
    }

    public byte[] getShowPosterData(Long showId, boolean thumb) {
        if (showId == null) {
            return new byte[0];
        }

        String localPosterPath = appConfig.getLocalImagePath(String.valueOf(showId));
        File posterFile = new File(localPosterPath, "poster.png");
        String imageType = ImageUtil.PNG;

        if (!posterFile.exists()) {
            posterFile = new File(localPosterPath, "poster.jpg");
            imageType = ImageUtil.JPEG;
        }

        if (!posterFile.exists()) {
            posterFile = new File(localPosterPath, "poster.jpeg");
            imageType = ImageUtil.JPEG;
        }

        if (posterFile.exists()) {
            try {
                BufferedImage poster = ImageIO.read(posterFile);

                if (thumb) {
                    BufferedImage thumbPoster = ImageUtil.getThumbImage(poster);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ImageIO.write(thumbPoster, imageType, baos);
                    baos.flush();
                    byte[] data = baos.toByteArray();
                    baos.close();

                    return data;
                }
                else {
                    return IOUtils.toByteArray(new FileInputStream(posterFile));
                }
            }
            catch (IOException e) {
                LOGGER.warning("Unable to load poster image from file: " + posterFile.getAbsolutePath());
            }
        }

        return new byte[0];
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

                    case "rating":
                        Integer ratingValue = (Integer) params.get(paramName);

                        if (ratingValue != null) {
                            spec = Specifications.where(spec)
                                .and(new DataSpecification<>(new NumericDateDataFilter("myRating", NumericDateDataFilter.GTE_OPERATOR, ratingValue)));
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
