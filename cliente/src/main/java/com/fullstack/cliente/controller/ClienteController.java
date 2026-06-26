package com.fullstack.cliente.controller;

import com.fullstack.cliente.assemblers.ClienteModelAssembler; // Asegúrate de que el nombre coincida
import com.fullstack.cliente.dto.ClienteRequest;
import com.fullstack.cliente.model.Cliente;
import com.fullstack.cliente.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ClienteModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<Cliente>> listar() {
        List<Cliente> clientes = clienteService.listarTodos();

        List<EntityModel<Cliente>> clienteModels = clientes.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(clienteModels,
                linkTo(methodOn(ClienteController.class).listar()).withSelfRel());
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Cliente>> buscarPorId(@PathVariable Integer id) {
        Cliente cliente = clienteService.buscarPorId(id);
        if (cliente == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toModel(cliente));
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Cliente>> guardar(@Valid @RequestBody ClienteRequest request) {
        Cliente clienteGuardado = clienteService.crearDesdeRequest(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .location(linkTo(methodOn(ClienteController.class).buscarPorId(clienteGuardado.getId())).toUri())
                .body(assembler.toModel(clienteGuardado));
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Cliente>> actualizar(@PathVariable Integer id, @Valid @RequestBody ClienteRequest request) {
        Cliente clienteActualizado = clienteService.actualizar(id, request);
        if (clienteActualizado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toModel(clienteActualizado));
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
    public ResponseEntity<String> publico() {
        return ResponseEntity.ok("Endpoint público - Tienda musical");
    }
}