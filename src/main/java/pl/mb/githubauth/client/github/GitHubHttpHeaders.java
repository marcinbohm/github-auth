package pl.mb.githubauth.client.github;

import org.springframework.http.HttpHeaders;

public class GitHubHttpHeaders extends HttpHeaders {

    // NAMES
    public static final String API_VERSION = "X-GitHub-Api-Version";

    // VALUES

    public static final String ACCEPT_VALUE = "application/vnd.github+json";
}
