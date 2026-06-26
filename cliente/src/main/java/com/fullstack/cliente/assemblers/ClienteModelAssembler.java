package com.fullstack.cliente.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.fullstack.cliente.controller.ClienteController;
import com.fullstack.cliente.model.Cliente;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class ClienteModelAssembler implements RepresentationModelAssembler<Cliente, EntityModel<Cliente>> {

    @Override
    public EntityModel<Cliente> toModel(Cliente cliente) {
        return EntityModel.of(cliente,
                // 1. Quitamos el "get" y dejamos buscarPorId
                linkTo(methodOn(ClienteController.class).buscarPorId(cliente.getId())).withSelfRel(),

                // 2. Apunta a listar() que es el nombre correcto en tu controller
                linkTo(methodOn(ClienteController.class).listar()).withRel("clientes")
        );
    }
}