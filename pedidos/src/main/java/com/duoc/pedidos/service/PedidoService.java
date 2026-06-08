package com.duoc.pedidos.service;

import com.duoc.pedidos.dto.PedidoRequest;
import com.duoc.pedidos.model.Pedido;
import com.duoc.pedidos.repository.PedidoRepository;
import com.duoc.pedidos.webClient.ClienteClient;
import com.duoc.pedidos.webClient.ProductoClient;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class PedidoService {

    private static final Logger log = LoggerFactory.getLogger(PedidoService.class);

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ClienteClient clienteClient;

    @Autowired
    private ProductoClient productoClient;

    public List<Pedido> getPedidos() {
        log.info("Iniciando lista de pedidos");
        return pedidoRepository.findAll();}

    public Pedido buscarPorId(Integer id){
        log.info("Iniciando buscar pedido con id: {}", id);
        return pedidoRepository.findById(id).orElse(null);}

    public Pedido guardarPedido(Pedido pedidos) {
        log.info("Iniciando guardar pedido: {}", pedidos);
        return pedidoRepository.save(pedidos);}

    public Pedido crearDesdeRequest(PedidoRequest request, String token){
        log.info("Iniciando crear desde request: {}", request);

        Map<String, Object> cliente = clienteClient.obtenerClienteId(request.getClienteId(), token);

        if(cliente == null || cliente.isEmpty()) {
            log.warn("No se pudo crear, cliente no encontrado");
            throw new RuntimeException("Cliente no encontrado");
        }

        Map<String, Object> producto = productoClient.obtenerProductoId(request.getProductoId(), token);

        if(producto == null || producto.isEmpty()) {
            log.warn("No se pudo crear, producto no encontrado");
            throw new RuntimeException("Producto no encontrado");
        }

        Pedido pedido2 = new Pedido();

        pedido2.setClienteId(request.getClienteId());
        pedido2.setProductoId(request.getProductoId());
        pedido2.setDireccion(request.getDireccion());
        pedido2.setFechaPedido(request.getFechaPedido());
        pedido2.setPrecioEnvio(request.getPrecioEnvio());


        pedido2.setNombreProducto(producto.get("nombreProducto").toString());
        pedido2.setNumeroSerie(producto.get("numeroSerie") != null ? producto.get("numeroSerie").toString() : null);
        pedido2.setDescripcion(producto.get("descripcion").toString());
        pedido2.setTipoInstrumento(producto.get("tipoInstrumento").toString());
        pedido2.setPrecioInstrumento(Integer.parseInt(producto.get("precioInstrumento").toString()));

        String nombre = cliente.get("nombre").toString();
        String apellido = cliente.get("apellido").toString();
        pedido2.setNombreCliente(nombre + " " + apellido);
        log.info("Pedido guardado exitosamente: {}", pedido2);

        return guardarPedido(pedido2);
    }

    public boolean eliminar(Integer id) {
        log.info("Iniciando eliminar el pedido con id: {}", id);
        Pedido pedido = buscarPorId(id);
        if(pedido == null){
            log.warn("Pedido no encontrado: {}", id);
            return false;
        }
        pedidoRepository.deleteById(id);
        log.info("Pedido eliminado exitosamente: {}", pedido);
        return true;
    }


}
