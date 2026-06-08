CREATE TABLE recepcionArriendo (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            arriendoId BIGINT NOT NULL,
                            nombreCliente VARCHAR(255) NOT NULL,
                            nombreProducto VARCHAR(255) NOT NULL,
                            numeroSerie VARCHAR(255),
                            precioGarantia INTEGER Not NULL,
                            fechaRecepcion date NOT NULL,
                            observacion VARCHAR(255) NOT NULL,
                            estado VARCHAR(255) NOT NULL,
                            multa INTEGER
);