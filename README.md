# spaceship

# Iniciar la aplicación
- Antes de ejecutar los comandos para levantar la aplicación debes abrir una terminal y ubicarla en la raíz del  proyecto.
- Ejecuta los comandos de abajo en secuencia:
  - mvn package
  - docker-compose build
  - docker-compose up
- Este proceso levanta el sevicio de RabbitMQ primero y despues la aplicacion.

# Autenticación:
- Usuario: admin
- Contraseña: admin
- Oservación: Está sin codificación para el modo de desarrollo.

# Swagger:
- http://localhost:8080/swagger-ui.html

# Colecciones de Postman:
- https://api.postman.com/collections/36024999-2dc5c299-079e-4656-8437-a581bb7dac7a?access_key=PMAT-01HZXZZPC6M4MS2EVYRYSWJD6Gl.

# Caché:
- Caffeine.

# Cobertura:
- JaCoCo.
- mvn clean verify
- Está configurado para aceptar un mínimo de 80% de cobertura.

# Consumer/Producer: RabbitMQ.
- Pasos para levantar RabbitMQ en el Docker:
    - docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3.13-management
- Para acceder a la interfaz de administración:
    - http://localhost:15672
    - Usuario: guest / Contraseña: guest
- Hay un endpoint específico para la mensajería.

# Mantenimiento de la base de datos: Flyway
