package pl.mb.githubauth.client.github;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "github.client")
public class GitHubClientProperties {

    private String trustStore;

    private String trustStorePassword;

    private Integer githubTimeoutInMin;

    private String apiVersion;

    public String getTrustStore() {
        return trustStore;
    }

    public void setTrustStore(String trustStore) {
        this.trustStore = trustStore;
    }

    public String getTrustStorePassword() {
        return trustStorePassword;
    }

    public void setTrustStorePassword(String trustStorePassword) {
        this.trustStorePassword = trustStorePassword;
    }

    public Integer getGithubTimeoutInMin() {
        return githubTimeoutInMin;
    }

    public void setGithubTimeoutInMin(Integer githubTimeoutInMin) {
        this.githubTimeoutInMin = githubTimeoutInMin;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }
}
