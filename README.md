# Sistema de Gestión de Pruebas de Manejo y Monitoreo de Vehículos (TPI Backend)

Proyecto desarrollado para la cátedra **Backend de Aplicaciones** (Ingeniería en Sistemas de Información - UTN FRC). 

Consiste en una plataforma modular orientada a **microservicios** con **Spring Boot**, diseñada para gestionar pruebas de manejo de vehículos para una agencia automotriz, incluyendo control de interesados, asignación de empleados, simulación/seguimiento de posición geográfica, alertas por desvíos e incidentes, y generación de reportes operativos.

---

## 🏗️ Arquitectura del Sistema

La solución implementa una arquitectura distribuida compuesta por un punto de entrada central (**API Gateway**) con control de seguridad/roles y tres microservicios de dominio desacoplados que se comunican entre sí mediante REST (`RestTemplate`):

```text
[ Cliente / Frontend / Postman ]
               │
               ▼
┌──────────────────────────────┐
│         API Gateway          │ (Puerto 8080 - Enrutamiento & Seguridad)
└──────────────┬───────────────┘
               │
      ┌────────┼────────────────┐
      ▼        ▼                ▼
┌──────────┐ ┌──────────────┐ ┌──────────────┐
│Empleados │ │   Pruebas    │ │Notificaciones│
│ Servicio │ │(Core Domain) │ │   Servicio   │
│ (Puerto) │ │   (Puerto)   │ │   (Puerto)   │
└──────────┘ └──────────────┘ └──────────────┘
      │              │               │
      └──────────────┼───────────────┘
                     ▼
          [ Base de Datos SQLite ]
```

---

## 📦 Componentes y Microservicios

### 1. **API Gateway (`/Gateway`)**
* **Responsabilidad:** Punto único de acceso inverso (Reverse Proxy) y enrutamiento hacia los microservicios.
* **Seguridad:** Configuración de autenticación y autorización basada en roles (`SecurityConfig` y `GWConfig`).

### 2. **Microservicio de Pruebas (`/Microservicios/Pruebas`)**
* **Responsabilidad:** Núcleo de la lógica de negocio.
* **Módulos:**
  * **Pruebas de Manejo:** Creación, inicio, finalización y detalle de pruebas (`PruebaController`).
  * **Interesados:** Registro de potenciales compradores, validación de licencias y carga masiva desde CSV (`InteresadoController`).
  * **Posición y Geoposicionamiento:** Tracking de ubicación del vehículo, cálculo de distancias y verificación de zonas permitidas (`PosicionController`, `ConfiguracionGeoLocalizacion`).
  * **Reportes:** Estadísticas de kilometraje, incidentes por empleado y rendimiento por vehículo (`ReporteController`).

### 3. **Microservicio de Empleados (`/Microservicios/Empleados`)**
* **Responsabilidad:** Gestión de los empleados de la agencia que acompañan las pruebas (`EmpleadoController`).
* **Operaciones:** CRUD y consulta de disponibilidad de empleados.

### 4. **Microservicio de Notificaciones (`/Microservicios/Notificaciones`)**
* **Responsabilidad:** Registro y envío de alertas ante incidentes (salida de zona permitida, excesos de velocidad, emergencias) disparadas durante una prueba (`NotificacionController`).

---

## 🛠️ Tecnologías y Herramientas

* **Lenguaje:** Java 17+
* **Framework principal:** Spring Boot (Spring Web, Spring Data JPA, Spring Security / Gateway)
* **Persistencia:** JPA / Hibernate con SQLite (archivo `agencia.db`)
* **Gestor de dependencias:** Apache Maven
* **Comunicación síncrona:** REST (`RestTemplate`)
* **Pruebas de API:** Postman

---

## 🚀 Instalación y Puesta en Marcha

### Prerrequisitos
* **Java Development Kit (JDK):** Versión 17 o superior.
* **Maven** (o utilizar los scripts `mvnw` / `mvnw.cmd` incluidos en cada servicio).
* **Git** instalado.

### Pasos para iniciar los servicios

Para un correcto funcionamiento, se deben ejecutar los servicios en terminales independientes (o mediante tu IDE preferido):

1. **Clonar el repositorio:**
   ```bash
   git clone https://github.com/Nitlus/Backend-Grupo64-TPI.git
   cd Backend-Grupo64-TPI
   ```

2. **Ejecutar Microservicio de Empleados:**
   ```bash
   cd Microservicios/Empleados
   ./mvnw spring-boot:run
   ```

3. **Ejecutar Microservicio de Notificaciones:**
   ```bash
   cd Microservicios/Notificaciones
   ./mvnw spring-boot:run
   ```

4. **Ejecutar Microservicio de Pruebas:**
   ```bash
   cd Microservicios/Pruebas
   ./mvnw spring-boot:run
   ```

5. **Iniciar el API Gateway:**
   ```bash
   cd Gateway
   ./mvnw spring-boot:run
   ```

> **Nota:** Verifica la configuración de puertos en cada archivo `application.properties` o en el archivo `puertos.txt` incluido en la raíz.

---

## 📬 Endpoints y Colección de Pruebas

El proyecto cuenta con un listado detallado de endpoints en el archivo `PostmanEndpoints.txt`. Algunos de los flujos principales accesibles a través del Gateway incluyen:

| Método | Endpoint Base | Descripción |
|---|---|---|
| `GET / POST` | `/api/pruebas` | Listado y registro de pruebas de manejo |
| `POST` | `/api/posiciones` | Envío de telemetría/coordenadas para control de zona |
| `GET` | `/api/reportes/incidentes` | Consulta de incidentes generados durante las pruebas |
| `GET / POST` | `/api/empleados` | Consulta y gestión de personal habilitado |
| `GET / POST` | `/api/interesados` | Gestión de clientes e interesados en pruebas |
| `POST` | `/api/notificaciones` | Disparo y consulta de notificaciones de alerta |

---

## 👥 Equipo de Desarrollo (Grupo 64)

* **Lucas Valentín Sifón Monteros** - [LinkedIn](https://www.linkedin.com/in/lucassifon/) · [GitHub](https://github.com/Nitlus)
* Integrantes Cátedra Backend de Aplicaciones - UTN Regional Córdoba.