package pl.mb.githubauth.model.ghentities;

import java.time.ZonedDateTime;

public class GHRepository {
    private long id;
    private String name;
    private String full_name;
    private String description;
    private String html_url;
    private String clone_url;
    private int stargazers_count;
    private int forks_count;
    private int open_issues_count;
    private ZonedDateTime created_at;
    private ZonedDateTime updated_at;

    public GHRepository() {
    }

    public GHRepository(long id, String name, String full_name, String description, String html_url, String clone_url, int stargazers_count, int forks_count, int open_issues_count, ZonedDateTime created_at, ZonedDateTime updated_at) {
        this.id = id;
        this.name = name;
        this.full_name = full_name;
        this.description = description;
        this.html_url = html_url;
        this.clone_url = clone_url;
        this.stargazers_count = stargazers_count;
        this.forks_count = forks_count;
        this.open_issues_count = open_issues_count;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHtml_url() {
        return html_url;
    }

    public void setHtml_url(String html_url) {
        this.html_url = html_url;
    }

    public String getClone_url() {
        return clone_url;
    }

    public void setClone_url(String clone_url) {
        this.clone_url = clone_url;
    }

    public int getStargazers_count() {
        return stargazers_count;
    }

    public void setStargazers_count(int stargazers_count) {
        this.stargazers_count = stargazers_count;
    }

    public int getForks_count() {
        return forks_count;
    }

    public void setForks_count(int forks_count) {
        this.forks_count = forks_count;
    }

    public int getOpen_issues_count() {
        return open_issues_count;
    }

    public void setOpen_issues_count(int open_issues_count) {
        this.open_issues_count = open_issues_count;
    }

    public ZonedDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(ZonedDateTime created_at) {
        this.created_at = created_at;
    }

    public ZonedDateTime getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(ZonedDateTime updated_at) {
        this.updated_at = updated_at;
    }
}

