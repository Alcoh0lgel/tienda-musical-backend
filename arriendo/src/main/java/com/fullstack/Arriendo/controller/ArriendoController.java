package com.fullstack.Arriendo.controller;

import com.fullstack.Arriendo.dto.ArriendoRequest;
import com.fullstack.Arriendo.model.Arriendo;
import com.fullstack.Arriendo.service.ArriendoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/arriendos")
public class ArriendoController {

    @Autowired
    private ArriendoService arriendoService;

    @GetMapping
    public ResponseEntity<List<Arriendo>> getArriendo(){
        List<Arriendo> arriendos = arriendoService.listar();
        if(arriendos.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(arriendos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getArriendoPorId(@PathVariable Integer id){
        Arriendo arriendo = arriendoService.buscarPorId(id);
        if(arriendo == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(arriendo);
    }

    @PostMapping
    public ResponseEntity<?> crearArriendo(@Valid @RequestBody ArriendoRequest request, @RequestHeader("Authorization") String token){
        Arriendo arriendoGuardado = arriendoService.crearDesdeRequest(request, token);
        return ResponseEntity.status(HttpStatus.CREATED).body(arriendoGuardado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarArriendo(@PathVariable Integer id){
        boolean eliminado = arriendoService.eliminar(id);
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
