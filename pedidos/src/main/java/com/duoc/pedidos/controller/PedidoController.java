package com.duoc.pedidos.controller;

import com.duoc.pedidos.assemblers.PedidoModelAssembler;
import com.duoc.pedidos.dto.PedidoRequest;
import com.duoc.pedidos.model.Pedido;
import com.duoc.pedidos.service.PedidoService;
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
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private PedidoModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<Pedido>> getPedido() {
        List<Pedido> pedidos = pedidoService.getPedidos();
        List<EntityModel<Pedido>> pedidoModels = pedidos.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(pedidoModels,
                linkTo(methodOn(PedidoController.class).getPedido()).withSelfRel());
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Pedido>> getPedidosPorId(@PathVariable Integer id) {
        Pedido pedido = pedidoService.buscarPorId(id);
        if (pedido == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toModel(pedido));
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Pedido>> crearPedidos(@Valid @RequestBody PedidoRequest request, @RequestHeader("Authorization") String token) {
        Pedido pedidoGuardado = pedidoService.crearDesdeRequest(request, token);
        return ResponseEntity
                .created(linkTo(methodOn(PedidoController.class).getPedidosPorId(pedidoGuardado.getId())).toUri())
                .body(assembler.toModel(pedidoGuardado));
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Void> eliminarPedido(@PathVariable Integer id) {
        boolean eliminado = pedidoService.eliminar(id);
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