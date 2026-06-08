CREATE TABLE servicioTecnico (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    pedidoId BIGINT,
    arriendoId BIGINT,
    nombreCliente VARCHAR(255) NOT NULL,
    nombreProducto VARCHAR(255) NOT NULL,
    numeroSerie VARCHAR(255) NOT NULL,
    fechaIngreso DATE,
    falla VARCHAR(100) NOT NULL,
    descripcion VARCHAR(1000) NOT NULL,
    tipo VARCHAR(255) NOT NULL,
    estado VARCHAR(50) NOT NULL
);