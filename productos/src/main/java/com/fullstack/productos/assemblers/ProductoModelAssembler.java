package com.fullstack.productos.assemblers;

import com.fullstack.productos.controller.ProductoController;
import com.fullstack.productos.model.Producto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProductoModelAssembler implements RepresentationModelAssembler<Producto, EntityModel<Producto>> {

    @Override
    public EntityModel<Producto> toModel(Producto producto) {
        return EntityModel.of(producto,
                // 1. Quitamos el "get" y dejamos buscarPorId
                linkTo(methodOn(ProductoController.class).buscarPorId(producto.getId())).withSelfRel(),

                // 2. Apunta a listar() que es el nombre correcto en tu controller
                linkTo(methodOn(ProductoController.class).listar()).withRel("productos")
        );
    }
}