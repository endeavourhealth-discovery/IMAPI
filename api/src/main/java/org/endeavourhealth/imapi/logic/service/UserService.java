package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.dataaccess.UserRepository;
import org.endeavourhealth.imapi.model.dto.RecentActivityItemDto;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Component
public class UserService {
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
    private UserRepository userRepository = new UserRepository();
    private EntityService entityService = new EntityService();


    public String getUserTheme(String userId) {
        return userRepository.getUserTheme(userId);
    }

    public void updateUserTheme(String userId, String theme) throws JsonProcessingException {
        userRepository.updateUserTheme(userId, theme);
    }

    public String getUserScale(String userId) {
        return userRepository.getUserScale(userId);
    }

    public void updateUserScale(String userId, String scale) throws JsonProcessingException {
        userRepository.updateUserScale(userId, scale);
    }

    public List<RecentActivityItemDto> getUserMRU(String userId) throws JsonProcessingException {
        return userRepository.getUserMRU(userId);
    }

    public void updateUserMRU(String userId, List<RecentActivityItemDto> mru) throws JsonProcessingException {
        userRepository.updateUserMRU(userId, mru);
    }

    public List<String> getUserFavourites(String userId) throws JsonProcessingException {
        return userRepository.getUserFavourites(userId);
    }

    public void updateUserFavourites(String userId, List<String> favourites) throws JsonProcessingException {
        userRepository.updateUserFavourites(userId, favourites);
    }

    public List<String> getUserOrganisations(String userId) throws JsonProcessingException {
        return userRepository.getUserOrganisations(userId);
    }

    public void updateUserOrganisations(String userId, List<String> organisations) throws JsonProcessingException {
        userRepository.updateUserOrganisations(userId, organisations);
    }

    public boolean userIdExists(String userId) {
        return userRepository.getUserIdExists(userId);
    }

    public boolean getEditAccess(String userId, String entityIri) throws JsonProcessingException {
        List<String> organisations = this.getUserOrganisations(userId);
        Set<String> predicates = Collections.singleton(IM.HAS_SCHEME);
        TTEntity entity = entityService.getBundle(entityIri, predicates).getEntity();
        if(null == entity.getScheme()) return false;
        return organisations.contains(entity.getScheme().getIri());
    }
}
