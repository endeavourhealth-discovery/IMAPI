package org.endeavourhealth.imapi.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.endeavourhealth.imapi.errorhandling.GeneralCustomException;
import org.endeavourhealth.imapi.logic.service.RequestObjectService;
import org.endeavourhealth.imapi.logic.service.UserService;
import org.endeavourhealth.imapi.model.dto.RecentActivityItemDto;
import org.endeavourhealth.imapi.model.dto.ScaleDto;
import org.endeavourhealth.imapi.model.dto.ThemeDto;
import org.endeavourhealth.imapi.utility.MetricsHelper;
import org.endeavourhealth.imapi.utility.MetricsTimer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.io.IOException;
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
    public String getUserTheme(HttpServletRequest request) throws IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.User.Theme.GET")) {
            LOG.debug("getUserTheme");
            String userId = requestObjectService.getRequestAgentId(request);
            return userService.getUserTheme(userId);
        }
    }

    @PostMapping(value = "/theme", produces = "application/json")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateUserTheme(HttpServletRequest request, @RequestBody ThemeDto themeDto) throws IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.User.Theme.POST")) {
            LOG.debug("updateUserTheme");
            String userId = requestObjectService.getRequestAgentId(request);
            userService.updateUserTheme(userId, themeDto.getThemeValue());
        }
    }

    @GetMapping(value = "/scale", produces = "application/json")
    public String getUserScale(HttpServletRequest request) throws IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.User.Scale.GET")) {
            LOG.debug("getUserScale");
            String userId = requestObjectService.getRequestAgentId(request);
            return userService.getUserScale(userId);
        }
    }

    @PostMapping(value = "/scale", produces = "application/json")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateUserScale(HttpServletRequest request, @RequestBody ScaleDto scaleDto) throws IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.User.Scale.POST")) {
            LOG.debug("updateUserScale");
            String userId = requestObjectService.getRequestAgentId(request);
            userService.updateUserScale(userId, scaleDto.getScaleValue());
        }
    }

    @GetMapping(value = "/MRU", produces = "application/json")
    public List<RecentActivityItemDto> getUserMRU(HttpServletRequest request) throws IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.User.MRU.GET")) {
            LOG.debug("getUserMRU");
            String userId = requestObjectService.getRequestAgentId(request);
            return userService.getUserMRU(userId);
        }
    }

    @PostMapping(value = "/MRU", produces = "application/json")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateUserMRU(HttpServletRequest request, @RequestBody List<RecentActivityItemDto> mru) throws IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.User.MRU.POST")) {
            LOG.debug("updateUserMRU");
            String userId = requestObjectService.getRequestAgentId(request);
            userService.updateUserMRU(userId, mru);
        }
    }

    @GetMapping(value = "/favourites", produces = "application/json")
    public List<String> getUserFavourites(HttpServletRequest request) throws IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.User.Favourites.GET")) {
            LOG.debug("getUserFavourites");
            String userId = requestObjectService.getRequestAgentId(request);
            return userService.getUserFavourites(userId);
        }
    }

    @PostMapping(value = "/favourites", produces = "application/json")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateUserFavourites(HttpServletRequest request, @RequestBody List<String> favourites) throws IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.User.Favourites.POST")) {
            LOG.debug("updateUserFavourites");
            String userId = requestObjectService.getRequestAgentId(request);
            userService.updateUserFavourites(userId, favourites);
        }
    }

    @GetMapping(value = "/organisations", produces = "application/json")
    public List<String> getOrganisations(HttpServletRequest request) throws IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.User.Organisations.GET")) {
            LOG.debug(("getOrganisations"));
            String userId = requestObjectService.getRequestAgentId(request);
            return userService.getUserOrganisations(userId);
        }
    }

    @PostMapping(value = "/organisations", produces = "application/json")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PreAuthorize("hasAuthority('IMAdmin')")
    public void updateUserOrganisations(@RequestParam("UserId") String userId, @RequestBody List<String> organisations) throws JsonProcessingException, Exception {
        try (MetricsTimer t = MetricsHelper.recordTime("API.User.Organisations.POST")) {
            LOG.debug("updateUserOrganisations");
            if (!userService.userIdExists(userId))
                throw new GeneralCustomException("user not found", HttpStatus.BAD_REQUEST);
            userService.updateUserOrganisations(userId, organisations);
        }
    }

    @GetMapping(value = "/editAccess", produces = "application/json")
    public boolean getEditAccess(HttpServletRequest request, @RequestParam("iri") String iri) throws IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.User.EditAccess.GET")) {
            LOG.debug(("getEditAccess"));
            String userId = requestObjectService.getRequestAgentId(request);
            return userService.getEditAccess(userId, iri);
        }
    }
}
