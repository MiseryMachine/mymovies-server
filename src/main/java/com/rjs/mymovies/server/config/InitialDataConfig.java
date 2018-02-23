package com.rjs.mymovies.server.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rjs.mymovies.server.model.DataConstants;
import com.rjs.mymovies.server.model.Show;
import com.rjs.mymovies.server.model.ShowType;
import com.rjs.mymovies.server.model.User;
import com.rjs.mymovies.server.repos.UserRepository;
import com.rjs.mymovies.server.service.ShowService;
import com.rjs.mymovies.server.service.ShowTypeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.logging.Logger;

@Configuration
public class InitialDataConfig {
    private static final Logger LOGGER = Logger.getLogger(InitialDataConfig.class.getName());

    @Autowired
    private ObjectMapper jsonObjectMapper;
    @Autowired
    private ShowTypeService showTypeService;
    @Autowired
    private ShowService showService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder encoder;

    public InitialDataConfig() {
    }

    @Bean
    public String initData() {
//        initUsers();
        ResourceLoader resourceLoader = new FileSystemResourceLoader();
        Resource dataResource = resourceLoader.getResource("classpath:show-data.json");

        try {
            JsonNode jsonNode = jsonObjectMapper.readTree(dataResource.getInputStream());

            if (jsonNode.has("users")) {
                String jsonString = jsonNode.get("users").toString();
                User[] users = jsonObjectMapper.readValue(jsonString, User[].class);
                initUsers(users);
            }

            if (jsonNode.has("showTypes")) {
                String jsonString = jsonNode.get("showTypes").toString();
                ShowType[] showTypes = jsonObjectMapper.readValue(jsonString, ShowType[].class);
                initShowTypes(showTypes);
            }

            if (jsonNode.has("shows")) {
                String jsonString = jsonNode.get("shows").toString();
                Show[] shows = jsonObjectMapper.readValue(jsonString, Show[].class);
                initShows(shows);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return "data initialized";
    }

    private void initUsers(User[] users) {
        for (User user : users) {
            User curUser = userRepository.findByUsername(user.getUsername());

            if (curUser != null) {
                LOGGER.info("User found [" + curUser.getUsername() + "].");
            }
            else {
                String pw = user.getPassword();

                if (StringUtils.isNotBlank(pw)) {
                    user.setPassword(encoder.encode(pw));
                }

                user = userRepository.save(user);
                LOGGER.info("User created [" + user.getUsername() + "].");
            }
        }
    }

    private void initShowTypes(ShowType[] showTypes) {
        for (String showTypeName : DataConstants.SHOW_TYPES) {
            ShowType showType = new ShowType();
            showType.setName(showTypeName);
            showTypeService.save(showType);
            LOGGER.info("Show type created [" + showType.getName() + "].");
        }

        for (ShowType showType : showTypes) {
            ShowType curShowType = showTypeService.get(showType.getName());

            if (curShowType != null) {
                LOGGER.info("Show type found [" + curShowType.getName() + "].");
            }
            else {
                curShowType = showTypeService.save(showType);
                LOGGER.info("Show type created [" + showType.getName() + "].");
            }

            boolean genreAdded = false;

            for (String genre : showType.getGenres()) {
                if (!curShowType.getGenres().contains(genre)) {
                    curShowType.getGenres().add(genre);
                    genreAdded = true;
                }
            }

            if (genreAdded) {
                showTypeService.save(curShowType);
            }
        }
    }

    private void initShows(Show[] shows) {
        for (Show show : shows) {
            List<Show> curShows = showService.findByTitle(show.getTitle());

            if (curShows.isEmpty()) {
                show = showService.save(show);
                LOGGER.info("Show created [" + show.getTitle() + "].");
            }
            else {
                LOGGER.info("Show found [" + show.getTitle() + "].");
            }
        }
    }
}
