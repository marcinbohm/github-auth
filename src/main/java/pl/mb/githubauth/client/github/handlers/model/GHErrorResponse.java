package pl.mb.githubauth.client.github.handlers.model;

public class GHErrorResponse {

    private String message;
    private String documentation_url;

    public GHErrorResponse(String message, String documentation_url) {
        this.message = message;
        this.documentation_url = documentation_url;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDocumentation_url() {
        return documentation_url;
    }

    public void setDocumentation_url(String documentation_url) {
        this.documentation_url = documentation_url;
    }
}
