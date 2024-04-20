package com.example.videoopsservice.services;

import com.example.videoopsservice.entities.UserDetails;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.graphql.client.GraphQlClient;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.graphql.client.WebGraphQlClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class GraphQLClientService {

    private final HttpGraphQlClient graphQLClient;
//    getUserDetails(accessToken: String!): Claims

    public GraphQLClientService() {
        WebClient client = WebClient.builder().baseUrl("http://localhost:8001/graphql").build();
        graphQLClient = HttpGraphQlClient.builder(client).build();
    }
    public UserDetails getEmail(String token) {
        String query = "query { getUserDetails(accessToken: \"" + token + "\") {email} }";

        return graphQLClient.document(query)
                .retrieve("getUserDetails")
                .toEntity(UserDetails.class).block();
    }


}
