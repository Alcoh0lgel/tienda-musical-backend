package com.fullstack.devolucion.service;
import com.fullstack.devolucion.dto.DevolucionRequest;
import com.fullstack.devolucion.model.Devolucion;
import com.fullstack.devolucion.repository.DevolucionRepository;
import com.fullstack.devolucion.webclient.PedidosClient;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class DevolucionService {

    private static final Logger log = LoggerFactory.getLogger(DevolucionService.class);

    @Autowired
    private DevolucionRepository devolucionRepository;

    @Autowired
    private PedidosClient pedidosClient;

    public List<Devolucion> getDevolucion() {
        log.info("Listando devoluciones");
        return devolucionRepository.findAll();}

    public Devolucion buscarPorId(Integer id){
        log.info("Buscando devolucion con id: {}", id);
        return devolucionRepository.findById(id).orElse(null);}

    public Devolucion guardarDevolucion(Devolucion devolucion) {
        log.info("Guardando devolucion");
        return devolucionRepository.save(devolucion);}

    public Devolucion crearDesdeRequest(DevolucionRequest request, String token){
        log.info("Creando devolucion");

        Map<String, Object> pedidos = pedidosClient.obtenerPedidoId(request.getPedidoId(), token);

        if(pedidos == null || pedidos.isEmpty()){
            log.warn("Pedido no encontrado con id: {}", request.getPedidoId());
            throw new RuntimeException("Pedido no encontrado");
        }

        Devolucion devolucion1 = new Devolucion();

        devolucion1.setPedidoId(request.getPedidoId());
        devolucion1.setRequerimiento(request.getRequerimiento());
        devolucion1.setMotivo(request.getMotivo());
        devolucion1.setNumeroSerie(pedidos.get("numeroSerie").toString());
        devolucion1.setNombreProducto(pedidos.get("nombreProducto").toString());
        devolucion1.setPrecioInstrumento((Integer) pedidos.get("precioInstrumento"));
        devolucion1.setNombreCliente(pedidos.get("nombreCliente").toString());
        log.info("Devolucion creada | cliente: {} | producto: {}", devolucion1.getNombreCliente(), devolucion1.getNombreProducto());
        return guardarDevolucion(devolucion1);
    }

    public boolean eliminar(Integer id) {
        log.info("Eliminando devolucion con id: {}", id);
        Devolucion devolucion = buscarPorId(id);
        if(devolucion == null){
            log.warn("Devolucion no encontrado con id: {}", id);
            return false;
        }
        devolucionRepository.deleteById(id);
        log.info("Devolucion eliminada con id: {}", id);
        return true;
    }
}