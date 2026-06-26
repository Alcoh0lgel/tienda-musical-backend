package com.fullstack.productoArriendo.assemblers;

import com.fullstack.productoArriendo.controller.ProductoArriendoController;
import com.fullstack.productoArriendo.model.ProductoArriendo;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ProductoArriendoModelAssembler implements RepresentationModelAssembler<ProductoArriendo, EntityModel<ProductoArriendo>> {

    @Override
    public EntityModel<ProductoArriendo> toModel(ProductoArriendo productoArriendo) {
        return EntityModel.of(productoArriendo,

                linkTo(methodOn(ProductoArriendoController.class).buscarPorId(productoArriendo.getId())).withSelfRel(),

                linkTo(methodOn(ProductoArriendoController.class).listar()).withRel("productosArriendo")
        );
    }
}