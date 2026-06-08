package com.fullstack.recepcionArriendo.webClient;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
public class ArriendoClient {
    private final WebClient webClient;

    public ArriendoClient(@Value("${arriendo-service.url}") String pedidoServidor){
        this.webClient = WebClient.builder().baseUrl(pedidoServidor).build();
    }

    public Map<String, Object> obtenerArriendoId(Integer id, String token){
        return this.webClient.get()
                .uri("/{id}", id)
                .header("Authorization", token)
                .retrieve()
                .onStatus(status -> status.is4xxClientError(),
                        response -> response.bodyToMono(String.class)
                                .map(body -> new RuntimeException("Arriendo no encontrado")))
                .bodyToMono(Map.class)
                .block();
    }
}
