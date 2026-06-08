CREATE TABLE pedidos(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    productoId BIGINT NOT NULL,
    clienteId BIGINT NOT NULL,
    nombreCliente VARCHAR(255) NOT NULL,
    numeroSerie VARCHAR(255),
    nombreProducto VARCHAR(255) NOT NULL,
    tipoInstrumento VARCHAR(255) NOT NULL,
    descripcion VARCHAR(255) NOT NULL,
    precioInstrumento INTEGER,
    direccion VARCHAR(255) NOT NULL,
    precioEnvio INTEGER,
    fechaPedido date
);