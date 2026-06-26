package com.fullstack.Arriendo.assemblers;

import com.fullstack.Arriendo.controller.ArriendoController;
import com.fullstack.Arriendo.model.Arriendo;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ArriendoModelAssembler implements RepresentationModelAssembler<Arriendo, EntityModel<Arriendo>> {

    @Override
    public EntityModel<Arriendo> toModel(Arriendo arriendo) {
        return EntityModel.of(arriendo,
                linkTo(methodOn(ArriendoController.class).getArriendoPorId(arriendo.getId())).withSelfRel(),
                linkTo(methodOn(ArriendoController.class).getArriendo()).withRel("arriendos")
        );
    }
}