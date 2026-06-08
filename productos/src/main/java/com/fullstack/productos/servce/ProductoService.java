package com.fullstack.productos.servce;

import com.fullstack.productos.dto.ProductosRequest;
import com.fullstack.productos.model.Producto;
import com.fullstack.productos.repository.ProductoRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Transactional
public class ProductoService {

    public static final Logger log = LoggerFactory.getLogger(ProductoService.class);

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> listarTodos(){
        log.info("Iniciando lista de productos");
        return productoRepository.findAll();}

    public Producto buscarPorId(Integer id){
        log.info("Busqueda de producto con id: {}", id);
        return productoRepository.findById(id).orElse(null);}

    public Producto guardar(Producto producto){
        log.info("Guardando producto: {}", producto);
        return productoRepository.save(producto);}

    public Producto crearDesdeRequest(ProductosRequest request){

        Producto producto = new Producto();

        producto.setNumeroSerie(request.getNumeroSerie());
        producto.setNombreProducto(request.getNombreProducto());
        producto.setTipoInstrumento(request.getTipoInstrumento());
        producto.setDescripcion(request.getDescripcion());
        producto.setPrecioInstrumento(request.getPrecioInstrumento());
        producto.setEstado(request.getEstado());
        producto.setFechaRegistro(request.getFechaRegistro());
        Producto creado = guardar(producto);
        log.info("Producto creado exitosamente con id: {}", creado.getId());

        return creado;
    }

    public Producto actualizar(Integer id, ProductosRequest request){
        log.info("Actualizando producto: {}", id);

        Producto producto = buscarPorId(id);

        if (producto == null){
            log.warn("No existe el producto con id: {}", id);
            throw new RuntimeException("No se actualizar el Producto, id " + id + " no encontrado");
        }

        producto.setNumeroSerie(request.getNumeroSerie());
        producto.setNombreProducto(request.getNombreProducto());
        producto.setTipoInstrumento(request.getTipoInstrumento());
        producto.setDescripcion(request.getDescripcion());
        producto.setPrecioInstrumento(request.getPrecioInstrumento());
        producto.setEstado(request.getEstado());
        producto.setFechaRegistro(request.getFechaRegistro());
        log.info("Producto actualizado exitosamente con id: {}", id);

        return guardar(producto);
    }

    public boolean eliminar(Integer id){
        log.info("Eliminando producto: {}", id);
        Producto producto = buscarPorId(id);
        if (producto == null){
            log.warn("No existe el producto con id: {}", id);
            return false;
        }
        productoRepository.deleteById(id);
        log.info("Producto eliminado exitosamente con id: {}", id);
        return true;
    }
}
