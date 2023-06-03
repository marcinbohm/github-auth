package pl.mb.githubauth.client.github;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import pl.mb.githubauth.client.github.handlers.GitHubResponseErrorHandler;
import pl.mb.githubauth.exceptions.FailureException;
import pl.mb.githubauth.utils.TimeoutUtil;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Configuration
public class GitHubClientConfiguration {

    private final Log logger = LogFactory.getLog(this.getClass());

    private final GitHubClientProperties gitHubClientProperties;

    public GitHubClientConfiguration(GitHubClientProperties gitHubClientProperties) {
        this.gitHubClientProperties = gitHubClientProperties;
    }

    private static final int DEFAULT_TIMEOUT_IN_SECONDS = 100;

    @Bean(name = "githubRestTemplate")
    public RestTemplate createRestTemplate() {
        int timeout = (int) TimeoutUtil.determineTimeoutInMs(
                gitHubClientProperties.getGithubTimeoutInMin() != null ?
                        gitHubClientProperties.getGithubTimeoutInMin() : DEFAULT_TIMEOUT_IN_SECONDS);

        Optional<HttpClient> httpClient = createHttpClient();

        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory =
                httpClient.map(HttpComponentsClientHttpRequestFactory::new)
                        .orElseGet(HttpComponentsClientHttpRequestFactory::new);

        clientHttpRequestFactory.setConnectTimeout(timeout);
        clientHttpRequestFactory.setConnectionRequestTimeout(timeout);
        clientHttpRequestFactory.setReadTimeout(timeout);

        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                .errorHandler(new GitHubResponseErrorHandler());

        httpClient.ifPresent(client -> restTemplateBuilder.requestFactory(() -> new
                HttpComponentsClientHttpRequestFactory(client)));

        return restTemplateBuilder.build();
    }

    private Optional<HttpClient> createHttpClient() {
        Optional<HttpClient> client = createHttpClientWithTrustStore();
        if (client.isPresent()) {
            return client;
        }
        return bypassHostnameCheck();
    }

    private Optional<HttpClient> createHttpClientWithTrustStore() {
        try {
            if (gitHubClientProperties.getTrustStore() == null) {
                throw new FailureException("Trust Store not provided!");
            }

            File trustStoreFile = Paths.get(gitHubClientProperties.getTrustStore()).toFile();

            SSLContext sslContext = new SSLContextBuilder()
                    .loadTrustMaterial(
                            trustStoreFile,
                            gitHubClientProperties.getTrustStorePassword().toCharArray()
                    ).build();
            SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext);

            return Optional.of(HttpClients.custom()
                    .setSSLSocketFactory(socketFactory)
                    .build());

        } catch (InvalidPathException | FileNotFoundException e) {
            logger.error("Wrong path to Trust Store cert!");
            return Optional.empty();
        } catch (Exception e) {
            logger.error("Error while creating HttpClient.", e);
            return Optional.empty();
        }
    }

    private Optional<HttpClient> bypassHostnameCheck() {
        try {
            SSLContext sslContext = SSLContexts.custom()
                    .loadTrustMaterial(null, new TrustSelfSignedStrategy())
                    .build();
            SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(
                    sslContext, NoopHostnameVerifier.INSTANCE);

            return Optional.of(HttpClients.custom()
                    .setSSLSocketFactory(socketFactory)
                    .build());
        } catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
            logger.error("Error while bypassing hostname check, creating HttpClient.", e);
        }
        return Optional.empty();
    }
}
