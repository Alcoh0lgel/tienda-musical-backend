package com.fullstack.productoArriendo.controller;

import com.fullstack.productoArriendo.assemblers.ProductoArriendoModelAssembler;
import com.fullstack.productoArriendo.dto.ProductoArriendoRequest;
import com.fullstack.productoArriendo.model.ProductoArriendo;
import com.fullstack.productoArriendo.service.ProductoArriendoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/productoArriendo")
public class ProductoArriendoController {

    @Autowired
    private ProductoArriendoService productoArriendoService;

    @Autowired
    private ProductoArriendoModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<ProductoArriendo>> listar() {
        List<ProductoArriendo> productos = productoArriendoService.listarTodos();
        List<EntityModel<ProductoArriendo>> productoModels = productos.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(productoModels,
                linkTo(methodOn(ProductoArriendoController.class).listar()).withSelfRel());
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ProductoArriendo>> buscarPorId(@PathVariable Integer id) {
        ProductoArriendo producto = productoArriendoService.buscarPorId(id);
        if (producto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toModel(producto));
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ProductoArriendo>> guardar(@Valid @RequestBody ProductoArriendoRequest request) {
        ProductoArriendo productoGuardado = productoArriendoService.crearDesdeRequest(request);
        return ResponseEntity
                .created(linkTo(methodOn(ProductoArriendoController.class).buscarPorId(productoGuardado.getId())).toUri())
                .body(assembler.toModel(productoGuardado));
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ProductoArriendo>> actualizar(@PathVariable Integer id, @Valid @RequestBody ProductoArriendoRequest request) {
        ProductoArriendo productoActualizado = productoArriendoService.actualizar(id, request);
        if (productoActualizado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toModel(productoActualizado));
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        boolean eliminado = productoArriendoService.eliminar(id);
        if (!eliminado) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/publico")
    public ResponseEntity<String> publico() {
        return ResponseEntity.ok("Endpoint público - Tienda musical");
    }
}