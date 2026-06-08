package com.fullstack.reclamos.service;

import com.fullstack.reclamos.dto.ReclamosRequest;
import com.fullstack.reclamos.model.Reclamos;
import com.fullstack.reclamos.repository.ReclamosRepository;
import com.fullstack.reclamos.webClient.ClienteClient;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ReclamosService {

    private static final Logger log = LoggerFactory.getLogger(ReclamosService.class);

    @Autowired
    private ReclamosRepository reclamosRepository;

    @Autowired
    private ClienteClient clienteClient;

    public List<Reclamos> listar() {
        log.info("Iniciando listar reclamos");
        return reclamosRepository.findAll();}

    public Reclamos buscarPorId(Integer id) {
        log.info("Iniciando buscar reclamos por id: {}", id);
        return reclamosRepository.findById(id).orElse(null);}

    public Reclamos guardar(Reclamos reclamos) {
        log.info("Guardando reclamos: {}", reclamos);
        return reclamosRepository.save(reclamos);}

    public Reclamos crearDesdeRequest(ReclamosRequest request, String token){

        Map<String, Object> cliente = clienteClient.obtenerClienteId(request.getClienteId(), token);

        if(cliente == null || cliente.isEmpty()){
            log.warn("No se pudo crear, cliente no encontrado");
            throw new RuntimeException("Cliente no encontrado, no se puede agregar reclamo");
        }

        Reclamos reclamos = new Reclamos();

        reclamos.setAsunto(request.getAsunto());
        reclamos.setDescripcion(request.getDescripcion());
        reclamos.setFechaRegistro(request.getFechaRegistro());
        reclamos.setClienteId(request.getClienteId());

        String nombre = (String) cliente.get("nombre");
        String apellido = (String) cliente.get("apellido");
        reclamos.setNombreCliente(nombre + " " + apellido);
        log.info("Creando reclamos: {}", reclamos);

        return guardar(reclamos);
    }

    public boolean eliminar(Integer id) {
        log.info("Iniciando eliminar reclamos por id: {}", id);
        Reclamos reclamos = buscarPorId(id);
        if(reclamos == null){
            log.warn("Reclamos no encontrado: {}", id);
            return false;
        }
        reclamosRepository.deleteById(id);
        log.info("Eliminando reclamos por id: {}", id);
        return true;
    }

}