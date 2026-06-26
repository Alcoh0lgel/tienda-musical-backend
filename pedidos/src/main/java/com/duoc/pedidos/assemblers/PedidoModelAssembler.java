package com.duoc.pedidos.assemblers;

import com.duoc.pedidos.controller.PedidoController;
import com.duoc.pedidos.model.Pedido;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class PedidoModelAssembler implements RepresentationModelAssembler<Pedido, EntityModel<Pedido>> {

    @Override
    public EntityModel<Pedido> toModel(Pedido pedido) {
        return EntityModel.of(pedido,
                linkTo(methodOn(PedidoController.class).getPedidosPorId(pedido.getId())).withSelfRel(),
                linkTo(methodOn(PedidoController.class).getPedido()).withRel("pedidos")
        );
    }
}