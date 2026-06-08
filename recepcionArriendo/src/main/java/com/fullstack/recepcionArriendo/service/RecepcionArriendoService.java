package com.fullstack.recepcionArriendo.service;

import com.fullstack.recepcionArriendo.dto.RecepcionArriendoRequest;
import com.fullstack.recepcionArriendo.model.RecepcionArriendo;
import com.fullstack.recepcionArriendo.repository.RecepcionArriendoRepository;
import com.fullstack.recepcionArriendo.webClient.ArriendoClient;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class RecepcionArriendoService {

    private static final Logger log = LoggerFactory.getLogger(RecepcionArriendoService.class);

    @Autowired
    private RecepcionArriendoRepository recepcionArriendoRepository;

    @Autowired
    private ArriendoClient arriendoClient;

    public List<RecepcionArriendo> getRecepcion() {
        log.info("Listando recepciones");
        return recepcionArriendoRepository.findAll();}

    public RecepcionArriendo buscarPorId(Integer id){
        log.info("Buscando recepciones con id: {}", id);
        return recepcionArriendoRepository.findById(id).orElse(null);}

    public RecepcionArriendo guardarRecepcion(RecepcionArriendo recepcionArriendo) {
        log.info("Guardando recepción");
        return recepcionArriendoRepository.save(recepcionArriendo);}

    public RecepcionArriendo crearDesdeRequest(RecepcionArriendoRequest request, String token) {
        log.info("Creando recepción de arriendo");

        Map<String, Object> arriendo = arriendoClient.obtenerArriendoId(request.getArriendoId(), token);

        if(arriendo == null || arriendo.isEmpty()){
            log.warn("Arriendo no encontrado con id: {}", request.getArriendoId());
            throw new RuntimeException("Arriendo no encontrado");
        }

        RecepcionArriendo recepcion = new RecepcionArriendo();

        recepcion.setArriendoId(request.getArriendoId());
        recepcion.setFechaRecepcion(request.getFechaRecepcion());
        recepcion.setObservacion(request.getObservacion());
        recepcion.setEstado(request.getEstado());
        recepcion.setMulta(request.getMulta());

        recepcion.setNombreCliente(arriendo.get("nombreCliente").toString());
        recepcion.setNombreProducto(arriendo.get("nombreProducto").toString());
        recepcion.setNumeroSerie(arriendo.get("numeroSerie").toString());
        recepcion.setPrecioGarantia((Integer) arriendo.get("precioGarantia"));
        log.info("Recepcion creada exitosamente.");

        return guardarRecepcion(recepcion);
    }

    public boolean eliminar(Integer id) {
        log.info("Eliminando recepcion con id: {}", id);
        RecepcionArriendo recepcionArriendo = buscarPorId(id);
        if(recepcionArriendo == null){
            log.warn("Recepción no encontrado con id: {}", id);
            return false;
        }
        recepcionArriendoRepository.deleteById(id);
        log.info("Recepción eliminada con id: {}", id);
        return true;
    }

}
