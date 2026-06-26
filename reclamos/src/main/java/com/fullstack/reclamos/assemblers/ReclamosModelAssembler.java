package com.fullstack.reclamos.assemblers;

import com.fullstack.reclamos.controller.ReclamosController;
import com.fullstack.reclamos.model.Reclamos;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ReclamosModelAssembler implements RepresentationModelAssembler<Reclamos, EntityModel<Reclamos>> {

    @Override
    public EntityModel<Reclamos> toModel(Reclamos reclamos) {
        return EntityModel.of(reclamos,
                linkTo(methodOn(ReclamosController.class).getReclamosPorId(reclamos.getId())).withSelfRel(),
                linkTo(methodOn(ReclamosController.class).listarReclamos()).withRel("reclamos")
        );
    }
}