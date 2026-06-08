package com.fullstack.Arriendo.service;

import com.fullstack.Arriendo.dto.ArriendoRequest;
import com.fullstack.Arriendo.model.Arriendo;
import com.fullstack.Arriendo.repository.ArriendoRepository;
import com.fullstack.Arriendo.webClient.ClienteClient;
import com.fullstack.Arriendo.webClient.ProductoArriendoClient;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ArriendoService {

    private static final Logger log = LoggerFactory.getLogger(ArriendoService.class);

    @Autowired
    private ArriendoRepository arriendoRepository;

    @Autowired
    private ClienteClient clienteClient;

    @Autowired
    private ProductoArriendoClient productoArriendoClient;

    public List<Arriendo> listar() {
        log.info("Iniciando listar arriendos");
        return arriendoRepository.findAll();
    }

    public Arriendo buscarPorId(Integer id) {
        log.info("Iniciando buscar arriendo por id: {}", id);
        return arriendoRepository.findById(id).orElse(null);
    }

    public Arriendo guardarArriendo(Arriendo arriendo) {
        log.info("Iniciando guardar arriendo: {}", arriendo);
        return arriendoRepository.save(arriendo);
    }

    public Arriendo crearDesdeRequest(ArriendoRequest request, String token) {
        log.info("Iniciando crear desde request: {}", request);

        Map<String, Object> cliente = clienteClient.obtenerClienteId(request.getClienteId(), token);

        if (cliente == null || cliente.isEmpty()) {
            log.warn("No se pudo crear, cliente no encontrado");
            throw new RuntimeException("Cliente no encontrado");
        }

        Map<String, Object> productoArriendo = productoArriendoClient.obtenerProductoId(request.getProductoId(), token);

        if(productoArriendo == null || productoArriendo.isEmpty()) {
            log.warn("No se pudo crear, producto en arriendo no encontrado");
            throw new RuntimeException("Producto no encontrado");
        }

        Arriendo arriendo = new Arriendo();

        arriendo.setClienteId(request.getClienteId());
        arriendo.setProductoId(request.getProductoId());
        arriendo.setDireccion(request.getDireccion());
        arriendo.setPrecioEnvio(request.getPrecioEnvio());
        arriendo.setDireccionSucursal(request.getDireccionSucursal());
        arriendo.setFechaPrestacion(request.getFechaPrestacion());
        arriendo.setFechaRegreso(request.getFechaRegreso());

        String nombre = cliente.get("nombre").toString();
        String apellido = cliente.get("apellido").toString();
        arriendo.setNombreCliente(nombre + " " + apellido);

        arriendo.setNombreProducto(productoArriendo.get("nombreProducto").toString());
        arriendo.setNumeroSerie(productoArriendo.get("numeroSerie") != null ? productoArriendo.get("numeroSerie").toString() : null);
        arriendo.setDescripcion(productoArriendo.get("descripcion").toString());
        arriendo.setTipoInstrumento(productoArriendo.get("tipoInstrumento").toString());
        arriendo.setPrecioArriendo(Integer.parseInt(productoArriendo.get("precioArriendo").toString()));
        log.info("Arriendo guardado exitosamente: {}", arriendo);

        return guardarArriendo(arriendo);
    }

    public boolean eliminar(Integer id) {
        log.info("Iniciando eliminar el arriendo con id: {}", id);
        Arriendo arriendo = buscarPorId(id);
        if(arriendo == null){
            log.warn("Arriendo no encontrado: {}", id);
            return false;
        }
        arriendoRepository.deleteById(id);
        log.info("Arriendo eliminado exitosamente: {}", arriendo);
        return true;
    }
}
