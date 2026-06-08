package com.fullstack.cliente;

import com.fullstack.cliente.model.Cliente;
import com.fullstack.cliente.repository.ClienteRepository;
import net.datafaker.Faker;
import lombok.extern.slf4j.Slf4j; // 👈 1. Importamos la librería de logs de Lombok
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Locale;

@Slf4j // 👈 2. Agregamos esta anotación mágica de Lombok
@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker(Locale.forLanguageTag("es-CL"));

        if (clienteRepository.count() == 0) {

            log.info("Iniciando la carga automatica de 25 clientes con DataFaker...");

            for (int i = 0; i < 25; i++) {
                Cliente cliente = new Cliente();
                cliente.setRun(faker.idNumber().valid());
                cliente.setNombre(faker.name().firstName());
                cliente.setApellido(faker.name().lastName());
                cliente.setCorreo(faker.internet().emailAddress());
                cliente.setFechaNacimiento(new Date());

                clienteRepository.save(cliente);
            }
            log.info("Carga masiva completada con exito");

        } else {
            log.warn("La base de datos ya contiene registros. Se omitió el DataLoader.");
        }
    }
}