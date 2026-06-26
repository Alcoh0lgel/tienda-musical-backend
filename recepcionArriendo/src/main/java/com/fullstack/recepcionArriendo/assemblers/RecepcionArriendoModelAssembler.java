package com.fullstack.recepcionArriendo.assemblers;

import com.fullstack.recepcionArriendo.controller.RecepcionArriendoController;
import com.fullstack.recepcionArriendo.model.RecepcionArriendo;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class RecepcionArriendoModelAssembler implements RepresentationModelAssembler<RecepcionArriendo, EntityModel<RecepcionArriendo>> {

    @Override
    public EntityModel<RecepcionArriendo> toModel(RecepcionArriendo recepcionArriendo) {
        return EntityModel.of(recepcionArriendo,
                linkTo(methodOn(RecepcionArriendoController.class).getRecepcionPorId(recepcionArriendo.getId())).withSelfRel(),
                linkTo(methodOn(RecepcionArriendoController.class).getRecepcion()).withRel("recepcionArriendos")
        );
    }
}