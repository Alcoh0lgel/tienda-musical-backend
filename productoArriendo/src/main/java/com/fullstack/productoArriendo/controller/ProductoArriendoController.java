package com.fullstack.productoArriendo.controller;

import com.fullstack.productoArriendo.dto.ProductoArriendoRequest;
import com.fullstack.productoArriendo.model.ProductoArriendo;
import com.fullstack.productoArriendo.service.ProductoArriendoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productoArriendo")
public class ProductoArriendoController {

    @Autowired
    private ProductoArriendoService productoArriendoService;

    @GetMapping
    public ResponseEntity<List<ProductoArriendo>> listar(){
        List<ProductoArriendo> productos = productoArriendoService.listarTodos();
        if(productos.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoArriendo> buscarPorId(@PathVariable Integer id){
        ProductoArriendo productos = productoArriendoService.buscarPorId(id);
        if(productos == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productos);
    }

    @PostMapping
    public ResponseEntity<ProductoArriendo> guardar(@Valid @RequestBody ProductoArriendoRequest request){
        ProductoArriendo productoGuardado = productoArriendoService.crearDesdeRequest(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(productoGuardado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoArriendo> actualizar(@PathVariable Integer id, @Valid @RequestBody ProductoArriendoRequest request){
        ProductoArriendo productoActualizado = productoArriendoService.actualizar(id, request);
        if(productoActualizado == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productoActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id){
        boolean eliminado = productoArriendoService.eliminar(id);
        if (!eliminado){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/publico")
    public ResponseEntity<String> publico(){
        return ResponseEntity.ok("Endpoint público - Tienda musical");
    }

}
