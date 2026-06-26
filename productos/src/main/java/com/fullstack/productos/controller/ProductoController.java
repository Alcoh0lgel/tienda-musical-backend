package com.fullstack.productos.controller;

import com.fullstack.productos.assemblers.ProductoModelAssembler;
import com.fullstack.productos.dto.ProductosRequest;
import com.fullstack.productos.model.Producto;
import com.fullstack.productos.servce.ProductoService;
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
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ProductoModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<Producto>> listar() {
        List<Producto> productos = productoService.listarTodos();

        List<EntityModel<Producto>> productoModels = productos.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(productoModels,
                linkTo(methodOn(ProductoController.class).listar()).withSelfRel());
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Producto>> buscarPorId(@PathVariable Integer id) {
        Producto producto = productoService.buscarPorId(id);
        if (producto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toModel(producto));
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Producto>> guardar(@Valid @RequestBody ProductosRequest request) {
        Producto productoGuardado = productoService.crearDesdeRequest(request);

        return ResponseEntity
                .created(linkTo(methodOn(ProductoController.class).buscarPorId(productoGuardado.getId())).toUri())
                .body(assembler.toModel(productoGuardado));
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Producto>> actualizar(@PathVariable Integer id, @Valid @RequestBody ProductosRequest request) { //
        Producto productoActualizado = productoService.actualizar(id, request);
        if (productoActualizado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toModel(productoActualizado));
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        boolean eliminado = productoService.eliminar(id);
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