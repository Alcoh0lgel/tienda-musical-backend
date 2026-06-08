package com.fullstack.Auth_Service.service;

import com.fullstack.Auth_Service.dto.LoginResponse;
import com.fullstack.Auth_Service.exception.MalCredencialException;
import com.fullstack.Auth_Service.model.Usuario;
import com.fullstack.Auth_Service.repository.UsuarioRepository;
import org.hibernate.exception.AuthException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;

    public AuthService(UsuarioRepository usuarioRepository, JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.jwtService = jwtService;
    }

    public LoginResponse login(String username, String password) {
        log.info("Intento de login para usuario: {}", username);

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("Usuario no encontrado: {}", username);
                    return new MalCredencialException("Usuario no encontrado");
                });
        if (!usuario.getPassword().equals(password)) {
            log.warn("Password incorrecta para usuario: {}", username);
            throw new MalCredencialException("Password incorrecta");
        }
        String token = jwtService.generarToken(usuario.getUsername(), usuario.getRol());
        log.info("Login exitoso | usuario: {} | rol: {}", usuario.getUsername(), usuario.getRol());

        return new LoginResponse("Usuario autorizado", token, usuario.getUsername(), usuario.getRol());
    }
}