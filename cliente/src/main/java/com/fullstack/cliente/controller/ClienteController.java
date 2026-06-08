package com.fullstack.cliente.controller;

import com.fullstack.cliente.dto.ClienteRequest;
import com.fullstack.cliente.model.Cliente;
import com.fullstack.cliente.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public ResponseEntity<List<Cliente>> listar(){
        List<Cliente> clientes = clienteService.listarTodos();
        if(clientes.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarPorId(@PathVariable Integer id){
        Cliente cliente = clienteService.buscarPorId(id);
        if(cliente == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cliente);
    }

    @PostMapping
    public ResponseEntity<Cliente> guardar(@Valid @RequestBody ClienteRequest request){
        Cliente clienteGuardado = clienteService.crearDesdeRequest(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteGuardado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> actualizar(@PathVariable Integer id, @Valid @RequestBody ClienteRequest request){
        Cliente clienteActualizado = clienteService.actualizar(id, request);
        if(clienteActualizado == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(clienteActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        boolean eliminado = clienteService.eliminar(id);

        if (!eliminado) {
            return ResponseEntity.status(404)
                    .body(Map.of("error", "No se encontró el cliente para eliminar"));
        }
        return ResponseEntity.ok()
                .body(Map.of("mensaje", "Cliente eliminado con éxito en la base de datos"));
    }

    @GetMapping("/publico")
    public ResponseEntity<String> publico(){
        return ResponseEntity.ok("Endpoint público - Tienda musical");
    }
}
