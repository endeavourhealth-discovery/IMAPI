package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.dataaccess.EntityRepository;
import org.endeavourhealth.imapi.dataaccess.UserRepository;
import org.endeavourhealth.imapi.model.dto.RecentActivityItemDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserService {
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
    private UserRepository userRepository = new UserRepository();

    public String getUserTheme(String user) {
        return userRepository.getUserTheme(user);
    }

    public void updateUserTheme(String user, String theme) throws JsonProcessingException {
        userRepository.updateUserTheme(user, theme);
    }

    public List<RecentActivityItemDto> getUserMRU(String user) throws JsonProcessingException {
        return userRepository.getUserMRU(user);
    }

    public void updateUserMRU(String user, List<RecentActivityItemDto> mru) throws JsonProcessingException {
        userRepository.updateUserMRU(user, mru);
    }

    public List<String> getUserFavourites(String user) throws JsonProcessingException {
        return userRepository.getUserFavourites(user);
    }

    public void updateUserFavourites(String user, List<String> favourites) throws JsonProcessingException {
        userRepository.updateUserFavourites(user, favourites);
    }
}
