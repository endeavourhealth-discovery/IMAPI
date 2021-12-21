package org.endeavourhealth.imapi;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecurityTest {

    @GetMapping("/entity/create")
    @PreAuthorize("hasAuthority('user')")
    public String securityTest() {
        return "I am authenticated";
    }
}
