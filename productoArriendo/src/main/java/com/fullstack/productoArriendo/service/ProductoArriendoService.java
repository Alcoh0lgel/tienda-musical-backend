package com.fullstack.productoArriendo.service;

import com.fullstack.productoArriendo.dto.ProductoArriendoRequest;
import com.fullstack.productoArriendo.model.ProductoArriendo;
import com.fullstack.productoArriendo.repository.ProductoArriendoRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ProductoArriendoService {

    public static final Logger log = LoggerFactory.getLogger(ProductoArriendoService.class);

    @Autowired
    private ProductoArriendoRepository productoArriendoRepository;

    public List<ProductoArriendo> listarTodos() {
        log.info("Iniciando lista de productos");
        return productoArriendoRepository.findAll();
    }

    public ProductoArriendo buscarPorId(Integer id) {
        log.info("Busqueda de producto con id: {}", id);
        return productoArriendoRepository.findById(id).orElse(null);
    }

    public ProductoArriendo guardar(ProductoArriendo producto) {
        log.info("Guardando producto: {}", producto);
        return productoArriendoRepository.save(producto);
    }

    public ProductoArriendo crearDesdeRequest(ProductoArriendoRequest request) {

        ProductoArriendo producto = new ProductoArriendo();

        producto.setNumeroSerie(request.getNumeroSerie());
        producto.setNombreProducto(request.getNombreProducto());
        producto.setTipoInstrumento(request.getTipoInstrumento());
        producto.setDescripcion(request.getDescripcion());
        producto.setPrecioArriendo(request.getPrecioArriendo());
        producto.setPrecioGarantia(request.getPrecioGarantia());
        producto.setEstado(request.getEstado());
        producto.setFechaRegistro(request.getFechaRegistro());
        ProductoArriendo creado = guardar(producto);
        log.info("Producto creado exitosamente con id: {}", creado.getId());

        return creado;
    }

    public ProductoArriendo actualizar(Integer id, ProductoArriendoRequest request){
        log.info("Actualizando producto: {}", id);

        ProductoArriendo producto = buscarPorId(id);

        if (producto == null){
            log.warn("No existe el producto con id: {}", id);
            throw new RuntimeException("No se puede actualizar, no existe el producto con id " + id);
        }

        producto.setNumeroSerie(request.getNumeroSerie());
        producto.setNombreProducto(request.getNombreProducto());
        producto.setTipoInstrumento(request.getTipoInstrumento());
        producto.setDescripcion(request.getDescripcion());
        producto.setPrecioArriendo(request.getPrecioArriendo());
        producto.setPrecioGarantia(request.getPrecioGarantia());
        producto.setEstado(request.getEstado());
        producto.setFechaRegistro(request.getFechaRegistro());
        log.info("Producto actualizado exitosamente con id: {}", id);

        return guardar(producto);
    }

    public boolean eliminar(Integer id){
        log.info("Eliminando producto: {}", id);
        ProductoArriendo producto = buscarPorId(id);
        if (producto == null){
            log.warn("No existe el producto con id: {}", id);
            return false;
        }
        productoArriendoRepository.deleteById(id);
        log.info("Producto eliminado exitosamente con id: {}", id);
        return true;
    }

}