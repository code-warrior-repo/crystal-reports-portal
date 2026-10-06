package com.company.reports.launch;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PortalLaunchController {

    private final LaunchTokenValidator launchTokenValidator;
    private final UserSessionService userSessionService;

    public PortalLaunchController(LaunchTokenValidator launchTokenValidator,
                                  UserSessionService userSessionService) {
        this.launchTokenValidator = launchTokenValidator;
        this.userSessionService = userSessionService;
    }

    @GetMapping("/launch")
    public String launch(@RequestParam("token") String token, HttpServletRequest request) {
        LaunchToken launchToken = launchTokenValidator.validate(token);
        userSessionService.establishReportsSession(launchToken.userId(), request);
        return "redirect:/reports";
    }
}
