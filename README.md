# Proyecto de Aplicación Web con Java y Spring Boot

## Descripción

Esta aplicación web permite a los usuarios crear y gestionar blogs, así como manejar la autenticación y autorización a través de JWT. También incluye la validación de cuentas mediante OTP y la gestión de imágenes de perfil.

## Tecnologías Utilizadas

- **Java**: Lenguaje de programación principal.
- **Spring Boot**: Framework para el desarrollo de aplicaciones web.
- **Spring Security**: Para la implementación de la seguridad y autenticación.
- **Spring Data JPA**: Para el acceso a datos y manejo de la persistencia.
- **Hibernate**: Implementación de JPA para la gestión de la base de datos.
- **JavaMail**: Para el envío de correos electrónicos.
- **Thymeleaf**: Motor de plantillas para la creación de interfaces de usuario (opcional).
- **MySQL**: Base de datos utilizada (puedes usar cualquier base de datos relacional).


## Funcionalidades

- **Registro de Usuario**: Permite a los usuarios registrarse y recibir un OTP para validar su cuenta.
- **Autenticación y Autorización**: Utiliza JWT para gestionar sesiones de usuario.
- **Gestión de Blogs**: Los usuarios pueden crear, actualizar y eliminar sus publicaciones de blog.
- **Gestión de Imágenes**: Permite a los usuarios subir imágenes de perfil.
- **Limpieza de Datos**: Servicio programado que elimina las vistas de IP obsoletas.
- **Limitación de Tasa**: Implementación de un filtro que limita el número de solicitudes por dirección IP.

## Endpoints de la API

### 1. Autenticación

#### 1.1. Iniciar Sesión

- **Endpoint**: `/api/auth/login`
- **Método**: `POST`
- **Descripción**: Autenticación del usuario y generación de un JWT.
- **Request Body**:
    ```json
    {
      "user": "usuario",
      "password": "contraseña"
    }
    ```
- **Response**:
    ```json
    {
      "token": "jwt-token",
      "message": "Inicio de sesión exitoso."
    }
    ```
- **Ejemplo de Solicitud (cURL)**:
    ```bash
    curl -X POST http://localhost:8080/api/auth/login \
    -H "Content-Type: application/json" \
    -d '{"user": "usuario", "password": "contraseña"}'
    ```

### 2. Usuarios

#### 2.1. Registrar Usuario

- **Endpoint**: `/api/users/register`
- **Método**: `POST`
- **Descripción**: Registrar un nuevo usuario.
- **Request Body**:
    ```json
    {
      "user": "nuevo_usuario",
      "password": "contraseña",
      "correo": "correo@example.com"
    }
    ```
- **Response**:
    ```json
    {
      "message": "Usuario registrado con éxito."
    }
    ```
- **Ejemplo de Solicitud (cURL)**:
    ```bash
    curl -X POST http://localhost:8080/api/users/register \
    -H "Content-Type: application/json" \
    -d '{"user": "nuevo_usuario", "password": "contraseña", "correo": "correo@example.com"}'
    ```

#### 2.2. Validar Usuario

- **Endpoint**: `/api/users/validate/{userId}`
- **Método**: `PUT`
- **Descripción**: Validar un usuario por ID.
- **Response**:
    ```json
    {
      "message": "Usuario validado con éxito."
    }
    ```
- **Ejemplo de Solicitud (cURL)**:
    ```bash
    curl -X PUT http://localhost:8080/api/users/validate/1 \
    -H "Authorization: Bearer jwt-token"
    ```

#### 2.3. Verificar Cuenta

- **Endpoint**: `/api/users/verify`
- **Método**: `POST`
- **Descripción**: Verificar la cuenta del usuario mediante OTP.
- **Request Body**:
    ```json
    {
      "email": "correo@example.com",
      "otp": "código_otp"
    }
    ```
- **Response**:
    ```json
    {
      "message": "OTP verificado con éxito."
    }
    ```
- **Ejemplo de Solicitud (cURL)**:
    ```bash
    curl -X POST http://localhost:8080/api/users/verify \
    -H "Content-Type: application/json" \
    -d '{"email": "correo@example.com", "otp": "código_otp"}'
    ```

#### 2.4. Regenerar OTP

- **Endpoint**: `/api/users/regenerate-otp`
- **Método**: `POST`
- **Descripción**: Regenerar el OTP para un usuario.
- **Request Body**:
    ```json
    {
      "email": "correo@example.com"
    }
    ```
- **Response**:
    ```json
    {
      "message": "Email enviado."
    }
    ```
- **Ejemplo de Solicitud (cURL)**:
    ```bash
    curl -X POST http://localhost:8080/api/users/regenerate-otp \
    -H "Content-Type: application/json" \
    -d '{"email": "correo@example.com"}'
    ```

#### 2.5. Obtener Todos los Usuarios

- **Endpoint**: `/api/users`
- **Método**: `GET`
- **Descripción**: Obtener la lista de todos los usuarios.
- **Response**:
    ```json
    [
      {
        "id": 1,
        "user": "usuario1"
      },
      {
        "id": 2,
        "user": "usuario2"
      }
    ]
    ```
- **Ejemplo de Solicitud (cURL)**:
    ```bash
    curl -X GET http://localhost:8080/api/users \
    -H "Authorization: Bearer jwt-token"
    ```

### 3. Blogs

#### 3.1. Crear Blog

- **Endpoint**: `/api/blogs`
- **Método**: `POST`
- **Descripción**: Crear una nueva publicación de blog.
- **Request Body**:
    ```json
    {
      "titulo": "Título del blog",
      "contenido": "Contenido del blog",
      "categoria": "Categoría del blog",
      "imagen": "URL de la imagen"
    }
    ```
- **Response**:
    ```json
    {
      "message": "Blog creado con éxito."
    }
    ```
- **Ejemplo de Solicitud (cURL)**:
    ```bash
    curl -X POST http://localhost:8080/api/blogs \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer jwt-token" \
    -d '{"titulo": "Título del blog", "contenido": "Contenido del blog", "categoria": "Categoría del blog", "imagen": "URL de la imagen"}'
    ```

#### 3.2. Obtener Todos los Blogs

- **Endpoint**: `/api/blogs`
- **Método**: `GET`
- **Descripción**: Obtener la lista de todas las publicaciones de blog.
- **Response**:
    ```json
    [
      {
        "id": 1,
        "titulo": "Título del blog",
        "contenido": "Contenido del blog",
        "categoria": "Categoría"
      },
      ...
    ]
    ```
- **Ejemplo de Solicitud (cURL)**:
    ```bash
    curl -X GET http://localhost:8080/api/blogs \
    -H "Authorization: Bearer jwt-token"
    ```

#### 3.3. Actualizar Blog

- **Endpoint**: `/api/blogs/{blogId}`
- **Método**: `PUT`
- **Descripción**: Actualizar una publicación de blog existente.
- **Request Body**:
    ```json
    {
      "titulo": "Nuevo título",
      "contenido": "Nuevo contenido",
      "categoria": "Nueva categoría",
      "imagen": "Nueva URL de la imagen"
    }
    ```
- **Response**:
    ```json
    {
      "message": "Blog actualizado con éxito."
    }
    ```
- **Ejemplo de Solicitud (cURL)**:
    ```bash
    curl -X PUT http://localhost:8080/api/blogs/1 \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer jwt-token" \
    -d '{"titulo": "Nuevo título", "contenido": "Nuevo contenido", "categoria": "Nueva categoría", "imagen": "Nueva URL de la imagen"}'
    ```

#### 3.4. Eliminar Blog

- **Endpoint**: `/api/blogs/{blogId}`
- **Método**: `DELETE`
- **Descripción**: Eliminar una publicación de blog.
- **Response**:
    ```json
    {
      "message": "Blog eliminado con éxito."
    }
    ```
- **Ejemplo de Solicitud (cURL)**:
    ```bash
    curl -X DELETE http://localhost:8080/api/blogs/1 \
    -H "Authorization: Bearer jwt-token"
    ```

#### 3.5. Actualizar Vistas del Blog

- **Endpoint**: `/api/blogs/{blogId}/views`
- **Método**: `PUT`
- **Descripción**: Incrementar el contador de vistas de un blog.
- **Response**:
    ```json
    {
      "message": "Vistas actualizadas."
    }
    ```
- **Ejemplo de Solicitud (cURL)**:
    ```bash
    curl -X PUT http://localhost:8080/api/blogs/1/views \
    -H "Authorization: Bearer jwt-token"
    ```

### 4. Imágenes

#### 4.1. Subir Imagen de Perfil

- **Endpoint**: `/api/users/upload-image/{userId}`
- **Método**: `POST`
- **Descripción**: Subir una imagen de perfil para un usuario.
- **Request**: Usa un formulario para enviar un archivo de imagen.
- **Response**:
    ```json
    {
      "message": "Imagen subida con éxito.",
      "imageUrl": "URL de la imagen"
    }
    ```
- **Ejemplo de Solicitud (cURL)**:
    ```bash
    curl -X POST http://localhost:8080/api/users/upload-image/1 \
    -H "Authorization: Bearer jwt-token" \
    -F "image=@/ruta/a/la/imagen.jpg"
    ```

### 5. IP Views

#### 5.1. Guardar IP View

- **Endpoint**: `/api/ipviews`
- **Método**: `POST`
- **Descripción**: Guardar la vista de un IP para un post específico.
- **Request Body**:
    ```json
    {
      "ipaddress": "192.168.1.1",
      "postid": 1
    }
    ```
- **Response**:
    ```json
    {
      "message": "IP guardada con éxito."
    }
    ```
- **Ejemplo de Solicitud (cURL)**:
    ```bash
    curl -X POST http://localhost:8080/api/ipviews \
    -H "Content-Type: application/json" \
    -d '{"ipaddress": "192.168.1.1", "postid": 1}'
    ```

## Instalación

1. **Clonar el repositorio**:
   ```bash
   git clone https://github.com/tu_usuario/nombre_del_repositorio.git

#### Configurar la base de datos:

Crea una base de datos y configura las credenciales en ```application.properties```.

# Licencia
Este proyecto está licenciado bajo la Licencia MIT.