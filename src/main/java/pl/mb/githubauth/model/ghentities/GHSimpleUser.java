package pl.mb.githubauth.model.ghentities;

public class GHSimpleUser {
    private String login;
    private long id;
    private String avatar_url;
    private String html_url;

    public GHSimpleUser() {
    }

    public GHSimpleUser(String login, long id, String avatar_url, String html_url) {
        this.login = login;
        this.id = id;
        this.avatar_url = avatar_url;
        this.html_url = html_url;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getHtml_url() {
        return html_url;
    }

    public void setHtml_url(String html_url) {
        this.html_url = html_url;
    }
}