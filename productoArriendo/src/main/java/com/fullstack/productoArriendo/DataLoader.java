package com.fullstack.productoArriendo;

import com.fullstack.productoArriendo.model.ProductoArriendo;
import com.fullstack.productoArriendo.repository.ProductoArriendoRepository;
import net.datafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Locale;

@Slf4j
@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ProductoArriendoRepository productoArriendoRepository;

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker(Locale.forLanguageTag("es-CL"));

        if (productoArriendoRepository.count() == 0) {
            log.info("Iniciando la carga automatica de 15 Productos de Arriendo con DataFaker...");

            for (int i = 0; i < 15; i++) {
                ProductoArriendo productoArriendo = new ProductoArriendo();

                productoArriendo.setNumeroSerie(faker.bothify("ARR-####-????").toUpperCase());
                productoArriendo.setNombreProducto(faker.music().instrument());
                productoArriendo.setTipoInstrumento(faker.options().option("Cuerda", "Viento", "Percusión", "Teclado", "Audiovisual"));
                productoArriendo.setDescripcion(faker.lorem().sentence());
                productoArriendo.setPrecioArriendo(faker.number().numberBetween(5000, 50000));
                productoArriendo.setPrecioGarantia(faker.number().numberBetween(50000, 200000));
                productoArriendo.setEstado(faker.options().option("DISPONIBLE", "ARRENDADO","TALLER"));

                productoArriendo.setFechaRegistro(new Date());

                productoArriendoRepository.save(productoArriendo);
            }

            log.info("¡Carga de Productos de Arriendo completada con exito!");
        } else {
            log.warn("La tabla 'producto_arriendo' ya contiene registros. Se omitió el DataLoader.");
        }
    }
}