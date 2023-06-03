package pl.mb.githubauth.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AuthorizationController {

    private final OAuth2AuthorizedClientService authorizedClientService;

    public AuthorizationController(OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
    }

    @GetMapping(value = "/", produces = "text/plain")
    public String home() {
        return "Exciting home page";
    }

    @GetMapping(value = "/loginSuccess", produces = "text/plain")
    public String loginSuccess() {
        OAuth2User user = (OAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Map<String, Object> attributesMap = user.getAttributes();
        return "Successfully logged in! Welcome " + attributesMap.get("name") + "\n" +
                "You joined Github in : " + attributesMap.get("created_at") + "\n" +
                "Link to your GitHub account: " + attributesMap.get("url");
    }

    @GetMapping(value = "/loginFailure", produces = "text/plain")
    public String loginFailure() {
        return "Log in failure!";
    }
}