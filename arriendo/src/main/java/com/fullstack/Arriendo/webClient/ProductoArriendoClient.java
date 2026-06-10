package com.fullstack.Arriendo.webClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
public class ProductoArriendoClient {

    @Autowired
    private WebClient.Builder webClientBuilder;

    public Map<String, Object> obtenerProductoId(Integer id, String token){
        return this.webClientBuilder.build()
                .get()
                .uri("http://PRODUCTOARRIENDO/api/productoArriendo/{id}", id)
                .header("Authorization", token)
                .retrieve()
                .onStatus(status -> status.is4xxClientError(),
                        response -> response.bodyToMono(String.class)
                                .map(body -> new RuntimeException("Producto no encontrado")))
                .bodyToMono(Map.class)
                .block();
    }
}
