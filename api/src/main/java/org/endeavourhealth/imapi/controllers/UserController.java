package org.endeavourhealth.imapi.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.endeavourhealth.imapi.logic.service.RequestObjectService;
import org.endeavourhealth.imapi.logic.service.UserService;
import org.endeavourhealth.imapi.model.dto.RecentActivityItemDto;
import org.endeavourhealth.imapi.model.dto.ThemeDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.util.List;

@RestController
@RequestMapping("api/user")
@CrossOrigin(origins = "*")
@Tag(name = "UserController")
@RequestScope
public class UserController {
    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
    private final UserService userService = new UserService();
    private final RequestObjectService requestObjectService = new RequestObjectService();

    @GetMapping(value = "/theme", produces = "application/json")
    public String getUserTheme(HttpServletRequest request) throws JsonProcessingException {
        LOG.debug("getUserTheme");
        String userId = requestObjectService.getRequestAgentId(request);
        return userService.getUserTheme(userId);
    }

    @PostMapping(value = "/theme", produces = "application/json")
    public ResponseEntity<String> updateUserTheme(HttpServletRequest request, @RequestBody ThemeDto themeDto) throws JsonProcessingException {
        LOG.debug("updateUserTheme");
        String userId = requestObjectService.getRequestAgentId(request);
        userService.updateUserTheme(userId, themeDto.getThemeValue());
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/MRU", produces = "application/json")
    public List<RecentActivityItemDto> getUserMRU(HttpServletRequest request) throws JsonProcessingException {
        LOG.debug("getUserMRU");
        String userId = requestObjectService.getRequestAgentId(request);
        return userService.getUserMRU(userId);
    }

    @PostMapping(value = "/MRU", produces = "application/json")
    public ResponseEntity<String> updateUserMRU(HttpServletRequest request, @RequestBody List<RecentActivityItemDto> mru) throws JsonProcessingException {
        LOG.debug("updateUserMRU");
        String userId = requestObjectService.getRequestAgentId(request);
        userService.updateUserMRU(userId, mru);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/favourites", produces = "application/json")
    public List<String> getUserFavourites(HttpServletRequest request) throws JsonProcessingException {
        LOG.debug("getUserFavourites");
        String userId = requestObjectService.getRequestAgentId(request);
        return userService.getUserFavourites(userId);
    }

    @PostMapping(value = "/favourites", produces = "application/json")
    public ResponseEntity<String> updateUserFavourites(HttpServletRequest request, @RequestBody List<String> favourites) throws JsonProcessingException {
        LOG.debug("updateUserFavourites");
        String userId = requestObjectService.getRequestAgentId(request);
        userService.updateUserFavourites(userId, favourites);
        return ResponseEntity.ok().build();
    }
}
