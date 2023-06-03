package pl.mb.githubauth.utils;

public class ClientUtil {

    public static String createBearerToken(String token) {
        return "Bearer " + token;
    }
}
