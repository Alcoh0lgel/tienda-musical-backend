package com.fullstack.servicioTecnico.service;

import com.fullstack.servicioTecnico.dto.ServicioTecnicoRequest;
import com.fullstack.servicioTecnico.webClient.ArriendoClient;
import com.fullstack.servicioTecnico.webClient.PedidosClient;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fullstack.servicioTecnico.model.ServicioTecnico;
import com.fullstack.servicioTecnico.repository.ServicioTecnicoRepository;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ServicioTecnicoService {

    private static final Logger log = LoggerFactory.getLogger(ServicioTecnico.class);

    @Autowired
    private ServicioTecnicoRepository servicioTecnicoRepository;

    @Autowired
    private PedidosClient pedidosClient;

    @Autowired
    private ArriendoClient arriendoClient;

    public List<ServicioTecnico> listarServicioTecnico(){
        log.info("Listando servicios");
        return servicioTecnicoRepository.findAll();
    }

    public ServicioTecnico buscarPorId(Integer id){
        log.info("Buscando servicios por id: {}", id);
        return servicioTecnicoRepository.findById(id).orElse(null);
    }

    public ServicioTecnico guardar(ServicioTecnico servicioTecnico){
        log.info("Guardando servicio técnico");
        return servicioTecnicoRepository.save(servicioTecnico);
    }

    public ServicioTecnico crearDesdeRequest(ServicioTecnicoRequest request, String token) {
        log.info("Iniciando registro en servicio técnico desde request: {}", request);

        ServicioTecnico servicioTecnico = new ServicioTecnico();

        servicioTecnico.setFalla(request.getFalla());
        servicioTecnico.setDescripcion(request.getDescripcion());
        servicioTecnico.setEstado(request.getEstado());
        servicioTecnico.setFechaIngreso(request.getFechaIngreso());

        if (request.getPedidoId() != null) {
            Map<String, Object> pedido = pedidosClient.obtenerPedidoId(request.getPedidoId(), token);

            if (pedido == null || pedido.isEmpty()) {
                log.warn("No se pudo crear, pedido no encontrado: {}", request.getPedidoId());
                throw new RuntimeException("El pedido no existe en el catálogo principal.");
            }

            servicioTecnico.setTipo("PEDIDO");
            servicioTecnico.setPedidoId(request.getPedidoId());
            servicioTecnico.setNombreCliente(pedido.get("nombreCliente").toString());
            servicioTecnico.setNumeroSerie(pedido.get("numeroSerie") != null ? pedido.get("numeroSerie").toString() : "S/N");
            servicioTecnico.setNombreProducto(pedido.get("nombreProducto").toString());

        } else if (request.getArriendoId() != null) {

            Map<String, Object> arriendo = arriendoClient.obtenerArriendoId(request.getArriendoId(), token);

            if (arriendo == null || arriendo.isEmpty()) {
                log.warn("No se pudo crear, arriendo no encontrado: {}", request.getArriendoId());
                throw new RuntimeException("El arriendo no existe en el catálogo.");
            }

            servicioTecnico.setTipo("ARRIENDO");
            servicioTecnico.setArriendoId(request.getArriendoId());
            servicioTecnico.setNombreCliente(arriendo.get("nombreCliente").toString());
            servicioTecnico.setNumeroSerie(arriendo.get("numeroSerie") != null ? arriendo.get("numeroSerie").toString() : "S/N");
            servicioTecnico.setNombreProducto(arriendo.get("nombreProducto").toString());


        } else {
            throw new RuntimeException("Debe especificar un ID de pedido o un ID de arriendo para ingresar a servicio técnico.");
        }

        log.info("Servicio guardado correctamente: {}", servicioTecnico);
        return guardar(servicioTecnico);
    }

    public boolean eliminar(Integer id){
        log.info("Eliminando servicio por id: {}", id);
        ServicioTecnico servicioTecnico = buscarPorId(id);
        if(servicioTecnico == null){
            log.warn("Servicio no encontrado con id: {}", id);
            return false;
        }
        servicioTecnicoRepository.delete(servicioTecnico);
        log.info("Servicio eliminado correctamente.");
        return true;
    }

}
