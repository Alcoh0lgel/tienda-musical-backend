package com.fullstack.devolucion.assemblers;

import com.fullstack.devolucion.controller.DevolucionController;
import com.fullstack.devolucion.model.Devolucion;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class DevolucionModelAssembler implements RepresentationModelAssembler<Devolucion, EntityModel<Devolucion>> {


    @Override
    public EntityModel<Devolucion> toModel(Devolucion devolucion) {

        return EntityModel.of(devolucion,

                linkTo(methodOn(DevolucionController.class).getDevolucionPorId(devolucion.getId())).withSelfRel(),

                linkTo(methodOn(DevolucionController.class).getDevolucion()).withRel("devoluciones")
        );
    }
}