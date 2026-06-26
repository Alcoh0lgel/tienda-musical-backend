package com.fullstack.recepcionArriendo.controller;

import com.fullstack.recepcionArriendo.assemblers.RecepcionArriendoModelAssembler;
import com.fullstack.recepcionArriendo.dto.RecepcionArriendoRequest;
import com.fullstack.recepcionArriendo.model.RecepcionArriendo;
import com.fullstack.recepcionArriendo.service.RecepcionArriendoService;
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
@RequestMapping("/api/recepcionArriendo")
public class RecepcionArriendoController {

    @Autowired
    private RecepcionArriendoService recepcionArriendoService;

    @Autowired
    private RecepcionArriendoModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<RecepcionArriendo>> getRecepcion() {
        List<RecepcionArriendo> recepcionArriendos = recepcionArriendoService.getRecepcion();
        List<EntityModel<RecepcionArriendo>> recepcionModels = recepcionArriendos.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(recepcionModels,
                linkTo(methodOn(RecepcionArriendoController.class).getRecepcion()).withSelfRel());
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<RecepcionArriendo>> getRecepcionPorId(@PathVariable Integer id) {
        RecepcionArriendo recepcionArriendo = recepcionArriendoService.buscarPorId(id);
        if (recepcionArriendo == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toModel(recepcionArriendo));
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<RecepcionArriendo>> crearRecepcion(@Valid @RequestBody RecepcionArriendoRequest request, @RequestHeader("Authorization") String token) {
        RecepcionArriendo recepcionArriendo = recepcionArriendoService.crearDesdeRequest(request, token);
        return ResponseEntity
                .created(linkTo(methodOn(RecepcionArriendoController.class).getRecepcionPorId(recepcionArriendo.getId())).toUri())
                .body(assembler.toModel(recepcionArriendo));
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Void> eliminarRecepcion(@PathVariable Integer id) {
        boolean eliminado = recepcionArriendoService.eliminar(id);
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