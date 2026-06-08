package com.fullstack.cliente.service;

import com.fullstack.cliente.dto.ClienteRequest;
import com.fullstack.cliente.model.Cliente;
import com.fullstack.cliente.repository.ClienteRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ClienteService {

    private static final Logger log = LoggerFactory.getLogger(ClienteService.class);

    @Autowired
    private ClienteRepository clienteRepository;

    public List<Cliente> listarTodos(){
        log.info("Listando todos los clientes");
        return clienteRepository.findAll();}

    public Cliente buscarPorId(Integer id){
        log.info("Buscando cliente con id: {}", id);
        return clienteRepository.findById(id).orElse(null);}

    public Cliente guardar(Cliente cliente){
        log.info("Guardando cliente con RUN: {}", cliente.getRun());
        return clienteRepository.save(cliente);}

    public Cliente crearDesdeRequest(ClienteRequest request){

        Cliente cliente = new Cliente();

        cliente.setRun(request.getRun());
        cliente.setNombre(request.getNombre());
        cliente.setApellido(request.getApellido());
        cliente.setFechaNacimiento(request.getFechaNacimiento());
        cliente.setCorreo(request.getCorreo());
        Cliente creado = guardar(cliente);
        log.info("Cliente creado exitosamente con id: {}", creado.getId());

        return creado;
    }

    public Cliente actualizar(Integer id, ClienteRequest request){
        log.info("Actualizando cliente con id {}", id);

        Cliente cliente = buscarPorId(id);

        if (cliente == null){
            log.warn("No se pudo actualizad, cliente no encontrado con id: {}", id);
            throw new RuntimeException("id Cliente no encontrado");
        }

        cliente.setRun(request.getRun());
        cliente.setNombre(request.getNombre());
        cliente.setApellido(request.getApellido());
        cliente.setFechaNacimiento(request.getFechaNacimiento());
        cliente.setCorreo(request.getCorreo());
        log.info("Cliente actualizado existosamente con id: {}", id);
        return guardar(cliente);
    }

    public boolean eliminar(Integer id){
        log.info("Eliminando cliente con id: {}", id);
        Cliente cliente = buscarPorId(id);
        if(cliente == null){
            log.warn("No se pudo eliminar, cliente no encontrado con id: {}", id);
            return false;
        }
        clienteRepository.deleteById(id);
        log.info("Cliente eliminado exitosamente con id: {}", id);
        return true;
    }

}
