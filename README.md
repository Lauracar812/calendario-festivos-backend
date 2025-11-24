# Calendario Festivos Backend

Backend Java Spring Boot para la gestión de días laborales, festivos y fines de semana por país y año.

---

## Tabla de Contenidos

- [Descripción](#descripción)
- [Características principales](#características-principales)
- [Tecnologías utilizadas](#tecnologías-utilizadas)
- [Instalación](#instalación)
- [Configuración](#configuración)
- [Despliegue en AWS](#despliegue-en-aws)
- [Uso de la API](#uso-de-la-api)
  - [Generar calendario laboral](#generar-calendario-laboral)
  - [Consultar calendario completo](#consultar-calendario-completo)
  - [Verificar si una fecha es festiva](#verificar-si-una-fecha-es-festiva)
  - [Listar festivos por año](#listar-festivos-por-año)
- [Ejemplo de respuesta](#ejemplo-de-respuesta)
- [Contribuir](#contribuir)
- [Licencia](#licencia)
- [Autores](#autores)

---

## Descripción

Este proyecto proporciona una API para la gestión y consulta de calendarios laborales y festivos, permitiendo:
- Generar calendarios laborales por país y año.
- Consultar días festivos, fines de semana y días laborales.
- Integración fácil en sistemas de RRHH, nómina, o apps de agenda.

---

## Características principales

- API RESTful para la gestión de calendarios y festivos.
- Cálculo automático de festivos nacionales, fijos y movibles.
- Soporte para la ley de puente festivo y cálculos basados en Pascua.
- Clasificación clara de días: LABORAL, FESTIVO, FIN_SEMANA.
- Respuestas en formato JSON, fácil integración con frontend o móviles.

---

## Tecnologías utilizadas

- Java 17+
- Spring Boot 3
- JPA/Hibernate
- Base de datos relacional (H2, PostgreSQL, MySQL, etc.)
- Maven

---

## Instalación

1. **Clona el repositorio**
   ```bash
   git clone https://github.com/Lauracar812/calendario-festivos-backend.git
   cd calendario-festivos-backend
   ```

2. **Configura la base de datos**  
   Edita el archivo `src/main/resources/application.properties` con los datos de tu base.

3. **Compila y ejecuta**
   ```bash
   ./mvnw spring-boot:run
   ```
   o
   ```bash
   mvn spring-boot:run
   ```

---

## Configuración

Ejemplo de configuración para H2 (por defecto):

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.h2.console.enabled=true
```

Para usar PostgreSQL, MySQL, etc., cambia los parámetros según tu base.

---

## 🚀 Despliegue en AWS

Este proyecto incluye un **pipeline de CI/CD completo** para despliegue automatizado en AWS.

### Arquitectura AWS

```
GitHub → CodePipeline → CodeBuild → ECR → ECS (Fargate) → ALB
```

### Servicios AWS Utilizados

- **AWS CodePipeline**: Orquestación del pipeline CI/CD
- **AWS CodeBuild**: Compilación y construcción de imagen Docker
- **Amazon ECR**: Registro privado de contenedores
- **Amazon ECS (Fargate)**: Ejecución de contenedores serverless
- **Application Load Balancer**: Balanceo de carga
- **Amazon RDS**: Base de datos PostgreSQL
- **CloudWatch**: Logging y monitoreo

### Inicio Rápido

1. **Configurar infraestructura base**
   ```powershell
   # Windows PowerShell
   .\aws-scripts\setup-infrastructure.ps1
   ```

2. **Seguir la guía completa**
   - Ver [AWS-DEPLOYMENT-GUIDE.md](AWS-DEPLOYMENT-GUIDE.md) para instrucciones detalladas
   - Ver [ARQUITECTURA-PIPELINE.md](ARQUITECTURA-PIPELINE.md) para entender la arquitectura

### Archivos de Configuración AWS

- `buildspec.yml`: Configuración de CodeBuild
- `taskdef.json`: Task Definition para ECS
- `appspec.yml`: Especificación de despliegue
- `aws-scripts/`: Scripts de automatización
- `.env.example`: Variables de entorno de ejemplo

### Evidencias de Despliegue

Para validar el despliegue exitoso, consulta las guías:
- Capturas de pantalla del pipeline
- Logs de CloudWatch
- Métricas de ECS
- Pruebas de la API desplegada

---

## Uso de la API

### 1. Generar calendario laboral

**Endpoint:**  
`POST /api/calendario/generar/{paisId}/{year}`

**Ejemplo:**
```http
POST http://localhost:8080/api/calendario/generar/1/2023
```
Genera y almacena todos los días del año para el país y el año indicados.

---

### 2. Consultar calendario completo

**Endpoint:**  
`GET /api/calendario/listar/{paisId}/{year}`

**Ejemplo:**
```http
GET http://localhost:8080/api/calendario/listar/1/2023
```
Retorna un array con todos los días del año, tipo y descripción.

---

### 3. Verificar si una fecha es festiva

**Endpoint:**  
`GET /api/calendario/verificar/{paisId}/{year}/{month}/{day}`

**Ejemplo:**
```http
GET http://localhost:8080/api/calendario/verificar/1/2023/12/25
```

---

### 4. Listar festivos por año

**Endpoint:**  
`GET /api/calendario/festivos/{paisId}/{year}`

**Ejemplo:**
```http
GET http://localhost:8080/api/calendario/festivos/1/2023
```

---

## Ejemplo de respuesta

Respuesta de `/api/calendario/listar/1/2023`:

```json
[
  {
    "id": 1,
    "fecha": "2023-01-01",
    "tipo": "FESTIVO",
    "descripcion": "domingo"
  },
  {
    "id": 2,
    "fecha": "2023-01-02",
    "tipo": "LABORAL",
    "descripcion": "lunes"
  },
  // ...más días...
]
```

---

## Contribuir

¿Quieres mejorar el proyecto?  
Por favor, revisa el archivo [CONTRIBUTING.md](CONTRIBUTING.md).

Pasos para contribuir:
- Haz un fork del proyecto
- Crea una rama nueva (`feature/nombre`)
- Realiza tus cambios y haz commit
- Abre un Pull Request explicando tu mejora

---

## Licencia

Este proyecto está bajo la licencia MIT.  
Revisa el archivo [LICENSE](LICENSE) para más detalles.

---

## Autores

- Laura Rojas [Lauracar812](https://github.com/Lauracar812)
- Colaboradores bienvenidos 🙌

---

¿Dudas, sugerencias o bugs?  
Abre un [issue](https://github.com/Lauracar812/calendario-festivos-backend/issues) o contacta al autor.
