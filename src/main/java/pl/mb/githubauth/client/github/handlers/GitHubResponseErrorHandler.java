package pl.mb.githubauth.client.github.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.server.ResponseStatusException;
import pl.mb.githubauth.client.github.handlers.model.GHErrorResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.Series.CLIENT_ERROR;
import static org.springframework.http.HttpStatus.Series.SERVER_ERROR;

@Component
public class GitHubResponseErrorHandler
        implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse httpResponse)
            throws IOException {

        return (httpResponse.getStatusCode().series() == CLIENT_ERROR
                        || httpResponse.getStatusCode().series() == SERVER_ERROR);
    }

    @Override
    public void handleError(ClientHttpResponse httpResponse)
            throws IOException {

        if (httpResponse.getStatusCode()
                .series() == SERVER_ERROR) {
            // TODO 500, 503 ...
        } else if (httpResponse.getStatusCode()
                .series() == CLIENT_ERROR) {
            String responseString = new BufferedReader(
                    new InputStreamReader(httpResponse.getBody(), StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n"));
            ObjectMapper objectMapper = new ObjectMapper();
            GHErrorResponse errorResponse = objectMapper.readValue(responseString, GHErrorResponse.class);
            String message = errorResponse.getMessage() + " Check documentation: " + errorResponse.getDocumentation_url();
            switch (httpResponse.getStatusCode()) {
                case NOT_FOUND:
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, message);
                case FORBIDDEN:
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, message);
                case UNAUTHORIZED:
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, message);
                // TODO other
            }
        }
    }
}