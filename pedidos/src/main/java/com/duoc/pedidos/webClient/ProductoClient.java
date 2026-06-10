package com.duoc.pedidos.webClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
public class ProductoClient {

    @Autowired
    private WebClient.Builder webClientBuilder;

    public Map<String, Object> obtenerProductoId(Integer id, String token){
        return this.webClientBuilder.build()
                .get()
                .uri("http://PRODUCTOS/api/productos/{id}", id)
                .header("Authorization", token)
                .retrieve()
                .onStatus(status -> status.is4xxClientError(),
                        response -> response.bodyToMono(String.class)
                                .map(body -> new RuntimeException("Producto no encontrado")))
                .bodyToMono(Map.class)
                .block();
    }
}
