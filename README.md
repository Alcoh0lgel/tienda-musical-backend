# Tienda-Musical-Backend
**Grupo 9**
* Alan Alfaro Bettancourt
## Descripción
Creé una plataforma digital enfocada en un e-commerce de instrumentos musicales. El sistema cuenta con un módulo de autenticación (Login) que genera tokens de seguridad (JWT) necesarios para acceder a los distintos endpoints de la aplicación. Para facilitar las pruebas, todos los microservicios exponen un endpoint público (`/publico`) que permite verificar su estado sin necesidad de autenticación.
El sistema administra una base de datos de Clientes, almacenando la información esencial de los usuarios, y un Catálogo de Productos con diversos instrumentos musicales tanto para venta, como para arriendo.
En cuanto a la lógica de negocio, los módulos de Pedidos, Arriendo, RecepcionArriendo, Devolucion y Reclamos se estructuraron utilizando relaciones de dependencia hacia las clases principales de Productos y ProductoArriendo.
La ventaja de este diseño es la eficiencia en la comunicación: el módulo de Devolución está conectado directamente a Pedidos, y el de RecepcionArriendo está conectado a Arriendo. Al hacerlo de esta manera, ambos módulos heredan inmediatamente toda la información de la transacción sin necesidad de realizar viajes adicionales o consultas externas al microservicio Cliente, ya que los datos vienen precargados desde el servicio intermedio. Como contraparte, el microservicio de Reclamos se comunica de forma única y exclusiva con Cliente para gestionar los datos de contacto y el historial de casos del usuario.
## Propuesta de Valor
Más allá de la venta tradicional, el proyecto innova incorporando la funcionalidad de **Arriendo de Instrumentos**. Esta solución está pensada para músicos profesionales que enfrentan emergencias (fallas técnicas) o que prefieren evitar el riesgo de dañar sus propios instrumentos en viajes largos (aviones/buses). La plataforma ofrece una alternativa rápida y segura para resolver urgencias logísticas en la industria musical.
## Tecnologías Utilizadas
***Lenguaje:*** Java 21

***Framework:*** Spring Boot 3 / Spring Cloud

***Base de Datos:*** MySQL (Laragon)

***Testing:*** JUnit 5, Mockito

***Infraestructura:*** Docker, API Gateway, Eureka Server

***Documentación:*** Swagger / OpenAPI
## Instalación de Base de Datos en LARAGON
Antes de iniciar todos los microservicios es necesario tener abierto un navegador, postman y laragon junto con la base de datos.  
También es importante saber que las credenciales para que la base de datos funcione son:
```
username: root  
password: system  
```
En los microservicios también debe estar el mismo username y la misma password en el archivo **application.yml** (En el perfil dev para poder inicializarlo en Duoc).
```
datasource:
  url: jdbc:mysql://localhost:3306/db_tiendamusical
  username: root
  password: system
  driver-class-name: com.mysql.cj.jdbc.Driver
```
Accederemos al apartado de **Laragon.MySQL**, daremos click en **consulta**, ingresaremos y ejecutaremos el siguiente script para crear nuestra base de datos:
```
CREATE DATABASE db_tiendamusical;
```
## Instalación y Despliegue con Docker
La forma más rápida y estandarizada de levantar toda la arquitectura (Bases de datos + Microservicios) es utilizando Docker.
1. Asegúrese de tener instalado y en ejecución **Docker** y **Docker Compose**.
2. Abra una terminal en la carpeta raíz del proyecto (donde se encuentra el archivo `docker-compose.yml`).
3. Ejecute el siguiente comando para construir y levantar todos los contenedores en segundo plano:
```
docker compose up -d --build
```
***Se encuentran ordenados según sus puertos***
1. **api-gateway** -8080
2. **auth-service** -8081  
3. **cliente** -8082  
4. **productos** -8083
5. **productoArriendo** -8084
6. **pedidos** -8085
7. **arriendo** -8086
8. **devolucion** -8087
9. **reclamos** -8089
10. **recepcionArriendo** -8090
11. **eureka-server** -8791

**Nota**
El puerto 8088 fue eliminado durante el transcurso del proyecto, olvidé completamente corregir esto.
## Eureka-Server
El proyecto utiliza Netflix Eureka Server como el orquestador centralizado para el descubrimiento y registro de los 11 microservicios del ecosistema.

***Nota Especial de Configuración:*** A diferencia del puerto estándar de Spring Cloud (8761), este servidor ha sido configurado explícitamente para ejecutarse en el puerto 8791.

Para visualizar el panel de control en tiempo real, ver los estados de cada microservicio y confirmar que la red interna está enlazada correctamente, acceda desde su navegador a:
```
Eureka Dashboard: http://localhost:8791/
```
## Swagger/OpenApi
Cada microservicio cuenta con su propia documentación auto-generada mediante OpenAPI. Para auditar o probar los endpoints de forma directa e individual (saltándose el Gateway), puede acceder a la interfaz de Swagger a través del navegador cambiando el puerto según la lista anterior:
```
http://localhost:[PUERTO_DEL_MICROSERVICIO]/swagger-ui/index.html
```
***URL DIRECTAS:***
```
Auth-Service: http://localhost:8081/swagger-ui/index.html
Cliente: http://localhost:8082/swagger-ui/index.html
Productos: http://localhost:8083/swagger-ui/index.html
ProductoArriendo: http://localhost:8084/swagger-ui/index.html
Pedidos: http://localhost:8085/swagger-ui/index.html
Arriendo: http://localhost:8086/swagger-ui/index.html
Devolucion: http://localhost:8087/swagger-ui/index.html
Reclamos: http://localhost:8089/swagger-ui/index.html
RecepcionArriendo: http://localhost:8090/swagger-ui/index.html
```
## POSTMAN
***IMPORTANTE (Concepto de API Gateway):*** Para probar los endpoints desde Postman, **NO** se deben utilizar los puertos individuales de cada microservicio. Gracias a la implementación del api-gateway, todas las peticiones externas se centralizan exclusivamente a través del puerto 8080. El Gateway se encarga de enrutar la petición internamente al servicio correspondiente.

Todas las peticiones de creación (POST) o actualización (PUT) deben enviarse en formato JSON a través de la pestaña Body > raw.
## Auth-Service:  
Este servicio provee los tokens de acceso requeridos para consumir las rutas protegidas del ecosistema.

***Endpoint Público (Sin credenciales):***
```
GET http://localhost:8080/auth/publico
```
***Generación de token***
```
POST http://localhost:8080/auth/login
```
***PayLoad de Login***
```
    {
      "username": "admin",
      "password": "1234"
    }
```
***Uso de Token:***
Al recibir la respuesta exitosa, copie el valor del campo token. En Postman, diríjase a la pestaña Authorization en sus demás peticiones, seleccione Bearer Token y pegue el valor. Este paso es obligatorio para el resto del flujo. En caso de usar Swagger, se debe colocar en el candado de la esquina derecha superior de los recuadros de los endpoints, de esta manera autoriza el uso con el token.
## Cliente.  
***Listar/Crear Clientes:***
```
GET|POST http://localhost:8080/api/clientes
```
***Consultar/Actualizar/Eliminar por ID:***
```
GET|PUT|DELETE http://localhost:8080/api/clientes/{ID}
```
***Payload para Crear/Actualizar (POST/PUT):***
```
{
  "run": "99.999.999-9",
  "nombre": "Roberto",
  "apellido": "Carlos Suarez",
  "correo": "CarlosSuarezRoberto@Gmail.com",
  "fechaNacimiento": "1999-10-10"
}
```
(El ID se genera automáticamente. El run debe ser único).

## Producto.
***Listar/Crear Productos:***
```
GET|POST http://localhost:8080/api/productos
```
***Consultar/Actualizar/Eliminar por ID:***
```
GET|PUT|DELETE http://localhost:8080/api/productos/{ID}
```
***Payload para Crear/Actualizar (POST/PUT):***
```
{
  "numeroSerie": "SN-STRAT-001",
  "nombreProducto": "Fender Stratocaster",
  "tipoInstrumento": "Cuerdas",
  "descripcion": "Guitarra eléctrica clásica color Sunburst",
  "precioInstrumento": 1200000,
  "estado": "Disponible",
  "fechaRegistro": "2026-01-10"
}
```
(El numeroSerie debe ser único).
## ProductoArriendo.
***Listar/Crear Productos:***
```
GET|POST http://localhost:8080/api/productoArriendo
```
***Consultar/Actualizar/Eliminar por ID:***
```
GET|PUT|DELETE http://localhost:8080/api/productoArriendo/{ID}
```
***Payload para Crear/Actualizar (POST/PUT):***
```
{
  "numeroSerie": "SN-STRAT-003",
  "nombreProducto": "Fender Stratocaster",
  "tipoInstrumento": "Cuerdas",
  "descripcion": "Guitarra eléctrica clásica color Sunburst",
  "precioArriendo": 20000,
  "precioGarantia": 10000,
  "estado": "Disponible",
  "fechaRegistro": "2026-01-10"
}
```
(El numeroSerie debe ser único).
## Pedidos.
***Listar/Crear Pedidos:***
```
GET|POST http://localhost:8080/api/pedidos
```
***Consultar/Eliminar por ID:***
```
GET|DELETE http://localhost:8080/api/pedidos/{ID}
```
***Payload para Crear(POST):***
```
{
  "productoId": 3,
  "clienteId": 3,
  "direccion": "Las Rosas #2111",
  "precioEnvio": 15000,
  "fechaPedido": "2026-06-30"
}
```
## Arriendo.
***Listar/Crear Arriendos:***
```
GET|POST http://localhost:8080/api/arriendos
```
***Consultar/Eliminar por ID:***
```
GET|DELETE http://localhost:8080/api/arriendos/{ID}
```
***Payload para Crear(POST):***
```
{
  "productoId": 4,
  "clienteId": 4,
  "direccion": "Las Rosas #1112",
  "precioEnvio": 15000,
  "direccionSucursal": "Sucursal 1",
  "fechaPrestacion": "2026-06-01",
  "fechaRegreso": "2026-07-21"
}
```
## Devolucion.
***Listar/Crear Devolución:***
```
GET|POST http://localhost:8080/api/devoluciones
```
***Consultar/Eliminar por ID:***
```
GET|DELETE http://localhost:8080/api/devoluciones/{ID}
```
***Payload para Crear(POST):***
```
{
  "pedidoId": 1,
  "requerimiento": "reembolso",
  "motivo": "Creo que tiene malo algo."
}
```
## Reclamos.
***Listar/Crear Reclamos:***
```
GET|POST http://localhost:8080/api/reclamos
```
***Consultar/Eliminar por ID:***
```
GET|DELETE http://localhost:8080/api/reclamos/{ID}
```
***Payload para Crear(POST):***
```
{
  "fechaRegistro": "2026-06-30",
  "asunto": "Reclamo 1",
  "descripcion": "Reclamo de prueba.",
  "clienteId": 1
}
```
## RecepcionArriendo.
***Listar/Crear Recepciones de Arriendos:***
```
GET|POST http://localhost:8080/api/recepcionArriendo
```
***Consultar/Eliminar por ID:***
```
GET|DELETE http://localhost:8080/api/recepcionArriendo/{ID}
```
***Payload para Crear(POST):***
```
{
  "arriendoId": 1,
  "fechaRecepcion": "2026-06-30",
  "observacion": "Aun sigue en revisión",
  "estado": "Observacion",
  "multa": 0
}
```
## TESTING CON MOCKITO
Para garantizar la calidad y robustez de la lógica de negocio, se implementaron pruebas unitarias utilizando **JUnit 5** y **Mockito**. En esta iteración del proyecto, el testing se concentró exclusivamente en el microservicio **Cliente**, validando tanto los flujos exitosos como el manejo de excepciones.

El archivo de pruebas se encuentra en la ruta estándar del proyecto:
`src/test/java/com/fullstack/cliente/service/ClienteServiceTest.java`

Se implementaron un total de **8 pruebas unitarias** que cubren el CRUD completo:
* `testListarTodos()`: Verifica la correcta obtención del listado de clientes.
* `testCrearDesdeRequestExito()`: Valida la persistencia exitosa de un cliente nuevo.
* `testCrearDesdeRequestErrorRunDuplicado()`: Comprueba que el sistema rechace la creación si el RUN ya existe en la base de datos.
* `testBuscarPorIdNoEncontrado()`: Asegura el correcto manejo del error al buscar un ID inexistente.
* `testActualizarExito()`: Confirma que la modificación de datos opere de forma correcta.
* `testActualizarErrorNoEncontrado()`: Valida el error al intentar actualizar un registro que no existe.
* `testEliminarExito()`: Verifica la eliminación correcta de un cliente en la base de datos.
* `testEliminarNoEncontrado()`: Comprueba que el sistema responda adecuadamente al intentar eliminar un ID que no está registrado.