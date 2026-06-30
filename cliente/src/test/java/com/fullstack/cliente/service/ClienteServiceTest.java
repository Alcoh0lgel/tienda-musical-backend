package com.fullstack.cliente.service;

import com.fullstack.cliente.dto.ClienteRequest;
import com.fullstack.cliente.exception.RunDuplicadoException;
import com.fullstack.cliente.model.Cliente;
import com.fullstack.cliente.repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    @Test
    void testListarTodos() {
        Cliente c1 = new Cliente();
        c1.setId(1);
        c1.setNombre("Alan");

        Cliente c2 = new Cliente();
        c2.setId(2);
        c2.setNombre("Maria");

        when(clienteRepository.findAll()).thenReturn(Arrays.asList(c1, c2));

        List<Cliente> resultado = clienteService.listarTodos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Alan", resultado.get(0).getNombre());
        verify(clienteRepository, times(1)).findAll();
    }

    @Test
    void testCrearDesdeRequestExito() {
        ClienteRequest request = new ClienteRequest();
        request.setRun("12345678-9");
        request.setNombre("Juan");
        request.setApellido("Perez");
        request.setFechaNacimiento(new java.util.Date());
        request.setCorreo("juan@correo.com");

        Cliente clienteGuardado = new Cliente();
        clienteGuardado.setId(100);
        clienteGuardado.setRun("12345678-9");
        clienteGuardado.setNombre("Juan");

        when(clienteRepository.existsByRun("12345678-9")).thenReturn(false);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteGuardado);

        Cliente resultado = clienteService.crearDesdeRequest(request);

        assertNotNull(resultado);
        assertEquals(100, resultado.getId());
        assertEquals("Juan", resultado.getNombre());
    }

    @Test
    void testCrearDesdeRequestErrorRunDuplicado() {
        ClienteRequest request = new ClienteRequest();
        request.setRun("12345678-9");

        when(clienteRepository.existsByRun("12345678-9")).thenReturn(true);

        assertThrows(RunDuplicadoException.class, () -> {
            clienteService.crearDesdeRequest(request);
        });

        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void testBuscarPorIdNoEncontrado() {
        when(clienteRepository.findById(99)).thenReturn(Optional.empty());

        Cliente resultado = clienteService.buscarPorId(99);

        assertNull(resultado);
    }

    @Test
    void testActualizarExito() {
        Integer id = 1;
        ClienteRequest request = new ClienteRequest();
        request.setRun("87654321-0");
        request.setNombre("Pedro");
        request.setApellido("Gomez");
        request.setFechaNacimiento(new java.util.Date());
        request.setCorreo("pedro@correo.com");

        Cliente clienteExistente = new Cliente();
        clienteExistente.setId(id);
        clienteExistente.setNombre("Juan");

        when(clienteRepository.findById(id)).thenReturn(Optional.of(clienteExistente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteExistente);

        Cliente resultado = clienteService.actualizar(id, request);

        assertNotNull(resultado);
        assertEquals("Pedro", resultado.getNombre());
        verify(clienteRepository, times(1)).save(clienteExistente);
    }

    @Test
    void testActualizarErrorNoEncontrado() {
        Integer id = 99;
        ClienteRequest request = new ClienteRequest();

        when(clienteRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            clienteService.actualizar(id, request);
        });

        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void testEliminarExito() {
        Integer id = 1;
        Cliente clienteExistente = new Cliente();
        clienteExistente.setId(id);

        when(clienteRepository.findById(id)).thenReturn(Optional.of(clienteExistente));

        boolean resultado = clienteService.eliminar(id);

        assertTrue(resultado);
        verify(clienteRepository, times(1)).deleteById(id);
    }

    @Test
    void testEliminarNoEncontrado() {
        Integer id = 99;

        when(clienteRepository.findById(id)).thenReturn(Optional.empty());

        boolean resultado = clienteService.eliminar(id);

        assertFalse(resultado);
        verify(clienteRepository, never()).deleteById(anyInt());
    }

}