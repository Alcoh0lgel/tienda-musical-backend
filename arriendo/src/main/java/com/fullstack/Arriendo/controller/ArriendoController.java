package com.fullstack.Arriendo.controller;

import com.fullstack.Arriendo.assemblers.ArriendoModelAssembler;
import com.fullstack.Arriendo.dto.ArriendoRequest;
import com.fullstack.Arriendo.model.Arriendo;
import com.fullstack.Arriendo.service.ArriendoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/arriendos")
public class ArriendoController {

    @Autowired
    private ArriendoService arriendoService;

    @Autowired
    private ArriendoModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<Arriendo>> getArriendo() {
        List<Arriendo> arriendos = arriendoService.listar();
        List<EntityModel<Arriendo>> arriendoModels = arriendos.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(arriendoModels,
                linkTo(methodOn(ArriendoController.class).getArriendo()).withSelfRel());
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Arriendo>> getArriendoPorId(@PathVariable Integer id) {
        Arriendo arriendo = arriendoService.buscarPorId(id);
        if (arriendo == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toModel(arriendo));
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Arriendo>> crearArriendo(@Valid @RequestBody ArriendoRequest request, @RequestHeader("Authorization") String token) {
        Arriendo arriendoGuardado = arriendoService.crearDesdeRequest(request, token);
        return ResponseEntity
                .created(linkTo(methodOn(ArriendoController.class).getArriendoPorId(arriendoGuardado.getId())).toUri())
                .body(assembler.toModel(arriendoGuardado));
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Void> eliminarArriendo(@PathVariable Integer id) {
        boolean eliminado = arriendoService.eliminar(id);
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