CREATE TABLE arriendo(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    productoId BIGINT NOT NULL,
    clienteId BIGINT NOT NULL,
    nombreCliente VARCHAR(255) NOT NULL,
    nombreProducto VARCHAR(255) NOT NULL,
    numeroSerie VARCHAR(255),
    tipoInstrumento VARCHAR(255) NOT NULL,
    descripcion VARCHAR(255) NOT NULL,
    precioArriendo INTEGER,
    precioGarantia INTEGER,
    precioEnvio INTEGER,
    fechaPrestacion date,
    fechaRegreso date,
    direccion VARCHAR(255) NOT NULL,
    direccionSucursal VARCHAR(255) NOT NULL
);