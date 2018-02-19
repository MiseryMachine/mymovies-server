package com.rjs.mymovies.server.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rjs.mymovies.server.model.Show;
import com.rjs.mymovies.server.model.User;
import com.rjs.mymovies.server.repos.ShowRepository;
import com.rjs.mymovies.server.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.logging.Logger;

@Configuration
public class InitialDataConfig {
    private static final Logger LOGGER = Logger.getLogger(InitialDataConfig.class.getName());

    @Autowired
    private ObjectMapper jsonObjectMapper;
    @Autowired
    private ShowRepository showRepository;
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

                if (!StringUtils.isEmpty(pw)) {
                    user.setPassword(encoder.encode(pw));
                }

                user = userRepository.save(user);
                LOGGER.info("User created [" + user.getUsername() + "].");
            }
        }
    }

    private void initShows(Show[] shows) {
        for (Show show : shows) {
            List<Show> curShows = showRepository.findByTitle(show.getTitle());

            if (curShows.isEmpty()) {
                show = showRepository.save(show);
                LOGGER.info("Show created [" + show.getTitle() + "].");
            }
            else {
                LOGGER.info("Show found [" + show.getTitle() + "].");
            }
        }
    }
}
