package pl.mb.githubauth.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import pl.mb.githubauth.exceptions.RequestException;

import java.util.List;

public class Requester {

    private static final Log logger = LogFactory.getLog(Requester.class);

    public static <T> T sendRestRequest(RestTemplate restTemplate, UriComponents comp, HttpMethod httpMethod, HttpEntity<?> entity, Class<T> clazz) {
        ResponseEntity<T> resp = restTemplate.exchange(comp.toUri(), httpMethod, entity, clazz);
        if (HttpStatus.OK.equals(resp.getStatusCode())) {
            logger.info("Request to " + comp.toUri() + " success!");
            return resp.getBody();
        }

        logger.error("Sending request to " + comp.toUri() + " failure!");
        throw new RequestException("Sending request to " + comp.toUri() + " failure!");
    }

    public static <T> List<T> sendRestRequest(RestTemplate restTemplate, UriComponents comp, HttpMethod httpMethod, HttpEntity<?> entity, ParameterizedTypeReference<List<T>> typeReference) {
        ResponseEntity<List<T>> resp = restTemplate.exchange(comp.toUri(), httpMethod, entity, typeReference);
        if (HttpStatus.OK.equals(resp.getStatusCode())) {
            logger.info("Request to " + comp.toUri() + " success!");
            return resp.getBody();
        }

        logger.error("Sending request to " + comp.toUri() + " failure!");
        throw new RequestException("Sending request to " + comp.toUri() + " failure!");
    }
}
