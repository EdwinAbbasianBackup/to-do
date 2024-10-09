package nl.hu.boardservice.application.external;

import lombok.RequiredArgsConstructor;
import nl.hu.boardservice.application.exception.NoDataFoundException;
import nl.hu.boardservice.application.exception.ServerErrorException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

/**
 * UserServiceHTTP provides methods to interact with the User Service using HTTP calls.
 */
@Service

public class UserServiceHTTP {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${user-service-http.url}")
    private String userServiceUrl;



    /**
     * Fetches a user by its ID from the User Service.
     *
     * @param userId The ID of the user to be fetched.
     * @return A {@link UserResponse} containing user details.
     * @throws NoDataFoundException If the User Service does not return a successful response.
     * @throws ServerErrorException If the User Service does not return a successful response.
     */
    public UserResponse getUserById(String userId) {
        try {
            ResponseEntity<UserResponse> response = restTemplate.getForEntity(userServiceUrl + userId, UserResponse.class);

            System.out.println("RESPONSE: " + response);

            if (response.getStatusCode().is2xxSuccessful()) {
                UserResponse userResponse = response.getBody();
                if (userResponse != null && userResponse.id != null) {
                    return userResponse;
                } else {
                    String responseBody = Objects.requireNonNull(response.getBody()).toString();
                    if (responseBody.contains("User not found")) {
                        throw new NoDataFoundException("User with ID " + userId + " not found.");
                    } else {
                        throw new ServerErrorException("User with ID " + userId + " not found.");
                    }
                }
            }
        } catch (HttpClientErrorException e) {
            throw new ServerErrorException("Error during fetching user");
        } catch (RestClientException e) {
            throw new ServerErrorException("Error communicating with User Service");
        }
        throw new ServerErrorException("Error communicating with User Service");
    }

    /**
     * Represents the response returned from the User Service. This class is used to map the response to a Java object.
     */
    public static class UserResponse {
        public String id;
        public String username;
    }
}
