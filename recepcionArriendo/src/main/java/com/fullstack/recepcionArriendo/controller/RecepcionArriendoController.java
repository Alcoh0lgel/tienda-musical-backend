package com.fullstack.recepcionArriendo.controller;

import com.fullstack.recepcionArriendo.dto.RecepcionArriendoRequest;
import com.fullstack.recepcionArriendo.model.RecepcionArriendo;
import com.fullstack.recepcionArriendo.service.RecepcionArriendoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recepcionArriendo")
public class RecepcionArriendoController {

    @Autowired
    private RecepcionArriendoService recepcionArriendoService;

    @GetMapping
    public ResponseEntity<List<RecepcionArriendo>> getRecepcion(){
        List<RecepcionArriendo> recepcionArriendos = recepcionArriendoService.getRecepcion();
        if(recepcionArriendos.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(recepcionArriendos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRecepcionPorId(@PathVariable Integer id){
        RecepcionArriendo recepcionArriendo = recepcionArriendoService.buscarPorId(id);
        if(recepcionArriendo == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(recepcionArriendo);
    }

    @PostMapping
    public ResponseEntity<?> crearRecepcion(@Valid @RequestBody RecepcionArriendoRequest request, @RequestHeader("Authorization") String token){
        RecepcionArriendo recepcionArriendo = recepcionArriendoService.crearDesdeRequest(request, token);
        return ResponseEntity.status(HttpStatus.CREATED).body(recepcionArriendo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarRecepcion(@PathVariable Integer id){
        boolean eliminado = recepcionArriendoService.eliminar(id);
        if(!eliminado){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/publico")
    public ResponseEntity<String> publico(){
        return ResponseEntity.ok("Endpoint público - Tienda musical");
    }

}
