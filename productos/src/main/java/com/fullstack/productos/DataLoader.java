package com.fullstack.productos; // 👈 Revisa que este sea tu paquete real

import com.fullstack.productos.model.Producto; // 👈 Ajustar import
import com.fullstack.productos.repository.ProductoRepository; // 👈 Ajustar import
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
    private ProductoRepository productoRepository;

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker(Locale.forLanguageTag("es-CL"));

        if (productoRepository.count() == 0) {
            log.info("Iniciando la carga automatica de 20 Productos con DataFaker...");

            for (int i = 0; i < 20; i++) {
                Producto producto = new Producto();

                producto.setNumeroSerie(faker.bothify("SN-####-????").toUpperCase());
                producto.setNombreProducto(faker.music().instrument());
                producto.setTipoInstrumento(faker.options().option("Cuerda", "Viento", "Percusión", "Teclado", "Electrónico"));
                producto.setDescripcion(faker.lorem().sentence());
                producto.setPrecioInstrumento(faker.number().numberBetween(50000, 1500000));
                producto.setEstado(faker.options().option("DISPONIBLE", "VENDIDO"));
                producto.setFechaRegistro(new Date());

                productoRepository.save(producto);
            }
            log.info("¡Carga de Productos completada con exito!");
        } else {
            log.warn("La tabla 'producto' ya contiene registros. Se omitió el DataLoader.");
        }
    }
}