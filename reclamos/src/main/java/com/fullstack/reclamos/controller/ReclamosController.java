package com.fullstack.reclamos.controller;

import com.fullstack.reclamos.assemblers.ReclamosModelAssembler;
import com.fullstack.reclamos.dto.ReclamosRequest;
import com.fullstack.reclamos.model.Reclamos;
import com.fullstack.reclamos.service.ReclamosService;
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
@RequestMapping("/api/reclamos")
public class ReclamosController {

    @Autowired
    private ReclamosService reclamosService;

    @Autowired
    private ReclamosModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<Reclamos>> listarReclamos() {
        List<Reclamos> reclamos = reclamosService.listar();
        if (reclamos.isEmpty()) {
            return CollectionModel.empty(linkTo(methodOn(ReclamosController.class).listarReclamos()).withSelfRel());
        }

        List<EntityModel<Reclamos>> reclamosModels = reclamos.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(reclamosModels,
                linkTo(methodOn(ReclamosController.class).listarReclamos()).withSelfRel());
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Reclamos>> getReclamosPorId(@PathVariable Integer id) {
        Reclamos reclamos = reclamosService.buscarPorId(id);
        if (reclamos == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toModel(reclamos));
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Reclamos>> crearReclamos(@Valid @RequestBody ReclamosRequest request, @RequestHeader("Authorization") String token) {
        Reclamos reclamosGuardado = reclamosService.crearDesdeRequest(request, token);
        return ResponseEntity
                .created(linkTo(methodOn(ReclamosController.class).getReclamosPorId(reclamosGuardado.getId())).toUri())
                .body(assembler.toModel(reclamosGuardado));
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Void> eliminarReclamos(@PathVariable Integer id) {
        boolean eliminado = reclamosService.eliminar(id);
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