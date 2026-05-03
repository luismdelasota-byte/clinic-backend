#  Backend - clinicapp

##  Descripción

API REST desarrollada con Spring Boot usando Java 21, orientada a la gestión de un sistema clínico.

El sistema permite administrar:

* Usuarios y roles
* Pacientes
* Doctores
* Citas médicas (appointments)

Incluye autenticación y autorización mediante JWT.

---

## Tecnologías utilizadas

* Java 21
* Spring Boot
* Spring Security (JWT)
* Spring Data JPA / Hibernate
* MySQL
* Maven

---
## Servidor disponible en:

```
http://localhost:8081
```
## JWT(token de seguridad)

¿Cómo funciona en este proyecto?
* El usuario envía sus credenciales:

POST /auth/login
{
  "usernameOrEmail": "admin",
  "password": "123456",
  "role" : "ADMIN"
}

* Si las credenciales son correctas, el backend genera un token:
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "admin",
  "rol":"ADMIN
}
El cliente (frontend o Postman) debe enviar ese token en cada petición protegida:
Authorization: Bearer TOKEN_GENERADO
---

##  Endpoints principales

###  Usuarios

```
GET    /api/users
POST   /api/users
PUT    /api/users/{id}
DELETE /api/users/{id}
```

### Pacientes

```
GET    /api/patients
POST   /api/patients
PUT    /api/patients/{id}
DELETE /api/patients/{id}
```

### Doctores

```
GET    /api/doctors
POST   /api/doctors
PUT    /api/doctors/{id}
DELETE /api/doctors/{id}
```

### Citas (Appointments)

```
GET    /api/appointments
POST   /api/appointments
PUT    /api/appointments/{id}
DELETE /api/appointments/{id}
```

---

## Estructura del proyecto

```
src/main/java/com/clinicapp
 ├── entity
 │    ├── User
 │    ├── Role
 │    ├── Patient
 │    ├── Doctor
 │    └── Appointment
 │
 ├── repository
 │    └── (interfaces JPA)
 │
 ├── service
 │    └── (lógica de negocio)
 │
 ├── controller
 │    ├── AuthController
 |    ├── RoleController
 │    ├── UserController
 │    ├── PatientController
 │    ├── DoctorController
 │    └── AppointmentController
 │
 └── security
      ├── config
      │    └── SecurityConfig
      ├── dto
      │    ├── LoginRequestDTO
      │    ├── LoginResponseDTO
      │    └── RegisterRequestDTO
      └── service
           └── CustomUserDetailsService
```

---

## Pruebas

Puedes probar la API usando:

* Postman

---

## Características principales

*  Autenticación con JWT
*  Gestión de usuarios y roles
*  CRUD de pacientes
*  CRUD de doctores
*  Gestión de citas médicas
*  Arquitectura en capas (Controller - Service - Repository - Entity)

---

## 🚧 Mejoras futuras: En este proyecto no implemente "dto" general pero si para el login
* Validaciones con DTOs
* Manejo global de errores (@ControllerAdvice)
* Documentación con Swagger
* Tests unitarios
* Roles y permisos más avanzados

---

## Autor

Luis De la Sota

##  Contacto

- LinkedIn: https://www.linkedin.com/in/luis-de-la-sota-llerena-529a4a36b/
- GitHub: https://github.com/luismdelasota-byte