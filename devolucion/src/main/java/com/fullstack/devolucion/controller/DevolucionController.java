package com.fullstack.devolucion.controller;


import com.fullstack.devolucion.assemblers.DevolucionModelAssembler;
import com.fullstack.devolucion.dto.DevolucionRequest;
import com.fullstack.devolucion.model.Devolucion;
import com.fullstack.devolucion.service.DevolucionService;

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
@RequestMapping("/api/devoluciones")
public class DevolucionController {

    @Autowired
    private DevolucionService devolucionService;

    @Autowired
    private DevolucionModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<Devolucion>> getDevolucion(){
        List<EntityModel<Devolucion>> devoluciones = devolucionService.getDevolucion()
                .stream().map(assembler::toModel).collect(Collectors.toList());
        return CollectionModel.of(devoluciones,
                linkTo(methodOn(DevolucionController.class).getDevolucion()).withSelfRel());
    }

    @GetMapping(value="/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Devolucion>> getDevolucionPorId(@PathVariable Integer id){
        Devolucion devolucion = devolucionService.buscarPorId(id);
        if(devolucion == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(
                assembler.toModel(devolucion));
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Devolucion>> crearDevolucion(@Valid @RequestBody DevolucionRequest request, @RequestHeader("Authorization") String token){
        Devolucion devolucionGuardada = devolucionService.crearDesdeRequest(request, token);
        return ResponseEntity.created(linkTo(methodOn(DevolucionController.class)
                                .getDevolucionPorId(devolucionGuardada.getId()))
                                .toUri()).body(assembler.toModel(devolucionGuardada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarDevolucion(@PathVariable Integer id){
        boolean eliminado = devolucionService.eliminar(id);
        if(!eliminado){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/publico")
    public ResponseEntity<String> publico(){
        return ResponseEntity.ok(
                "Endpoint público - Tienda musical");
    }

}