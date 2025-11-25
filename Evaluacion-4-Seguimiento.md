# Evaluación 4 - Seguimiento

## Indicador de Logro
**Aplicar los conceptos de computación en la nube y de los respectivos servicios no administrados en el despliegue de una API**

---

## Información del Proyecto

- **Estudiante:** Laura Carolina Carodona
- **Repositorio:** https://github.com/Lauracar812/calendario-festivos-backend
- **Aplicación:** API REST - Calendario de Festivos (Spring Boot 3.5.5, Java 17)
- **Región AWS:** us-east-2 (Ohio)
- **Fecha:** 24 de noviembre de 2025

---

## Servicios AWS Implementados

### 1. Servicios No Administrados (Serverless)

#### Amazon ECS Fargate
- **Tipo:** Servicio de contenedores serverless
- **Cluster:** `calendario-festivos-cluster`
- **Servicio:** `calendario-festivos-service`
- **Características:**
  - Sin gestión de servidores EC2
  - Escalamiento automático de recursos
  - Pago solo por recursos consumidos
  - Configuración: 512 CPU, 1024 MB RAM

#### AWS CodeBuild
- **Tipo:** Servicio de compilación administrado
- **Proyecto:** `calendario-festivos-build`
- **Características:**
  - Compilación automática de código desde GitHub
  - Construcción de imágenes Docker
  - Push automático a ECR
  - Tiempo de compilación: ~50 segundos

### 2. Servicios Complementarios

#### Amazon ECR (Elastic Container Registry)
- **Repositorio:** `calendario-festivos-backend`
- **URI:** `110412263170.dkr.ecr.us-east-2.amazonaws.com/calendario-festivos-backend`

#### Application Load Balancer
- **DNS:** `calendario-festivos-alb-1112364300.us-east-2.elb.amazonaws.com`
- **Distribución de tráfico HTTP**

#### AWS Secrets Manager
- **Secret:** `calendario-festivos/db-password`
- **Gestión segura de credenciales**

#### Amazon CloudWatch
- **Log Group:** `/ecs/calendario-festivos`
- **Monitoreo y logs centralizados**

---

## Arquitectura Cloud Implementada

```
┌─────────────────┐
│     GitHub      │ (Repositorio código)
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│   CodeBuild     │ (Compilación serverless)
│  (No gestionado)│
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│      ECR        │ (Registro de imágenes)
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  ECS Fargate    │ (Contenedores serverless)
│  (No gestionado)│
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│      ALB        │ (Balanceador de carga)
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│    Internet     │
└─────────────────┘
```

---

## Conceptos de Computación en la Nube Aplicados

### 1. Infraestructura como Servicio (IaaS)
- **VPC personalizada** con subnets en múltiples zonas de disponibilidad
- **Security Groups** para control de tráfico
- **Application Load Balancer** para distribución de carga

### 2. Plataforma como Servicio (PaaS)
- **ECS Fargate:** Ejecución de contenedores sin gestionar infraestructura
- **CodeBuild:** Compilación y construcción de imágenes sin servidores dedicados

### 3. Servicios Administrados
- **ECR:** Registro de imágenes Docker completamente administrado
- **Secrets Manager:** Gestión de credenciales con rotación automática
- **CloudWatch:** Monitoreo y logs sin configuración de infraestructura

### 4. Escalabilidad y Alta Disponibilidad
- Despliegue en **múltiples zonas de disponibilidad** (us-east-2a, us-east-2b)
- **Auto-scaling** configurado en ECS
- **Health checks** automáticos

### 5. Seguridad en la Nube
- **IAM Roles** con permisos de mínimo privilegio
- **Secrets Manager** para credenciales sensibles
- **Security Groups** con reglas restrictivas
- **VPC aislada** con control de red

---

## URLs de Recursos AWS

### Repositorio ECR
```
https://us-east-2.console.aws.amazon.com/ecr/repositories/private/110412263170/calendario-festivos-backend?region=us-east-2
```

### Cluster ECS Fargate
```
https://us-east-2.console.aws.amazon.com/ecs/v2/clusters/calendario-festivos-cluster?region=us-east-2
```

### Proyecto CodeBuild
```
https://us-east-2.console.aws.amazon.com/codesuite/codebuild/projects/calendario-festivos-build?region=us-east-2
```

### Application Load Balancer
```
https://us-east-2.console.aws.amazon.com/ec2/home?region=us-east-2#LoadBalancers:
```

### CloudWatch Logs
```
https://us-east-2.console.aws.amazon.com/cloudwatch/home?region=us-east-2#logsV2:log-groups/log-group/$252Fecs$252Fcalendario-festivos
```

---

## Demostración de Implementación

### Build Exitoso en CodeBuild
- **Build #8:** ✅ SUCCESS
- **Commit:** 53793a9
- **Fases completadas:**
  - DOWNLOAD_SOURCE
  - INSTALL (Java 17, Maven)
  - PRE_BUILD (Login ECR)
  - BUILD (Maven compile + Docker build)
  - POST_BUILD (Push a ECR)
  - UPLOAD_ARTIFACTS

### Imagen Docker en ECR
- **Tag latest:** Disponible
- **Tag por commit:** 53793a9
- **Tamaño:** Optimizado con amazoncorretto:17-alpine

### Task Definition ECS
- **Revisión:** 4
- **Configuración serverless Fargate**
- **Integración con Secrets Manager**
- **Logs en CloudWatch**

---

## Archivos de Configuración Cloud

### buildspec.yml
Configuración de CodeBuild con:
- Variables de entorno para AWS
- Fases de compilación Maven
- Construcción de imagen Docker
- Push automático a ECR

### taskdef.json
Definición de tarea ECS Fargate:
- Especificación de recursos (CPU, memoria)
- Configuración de contenedor
- Secretos desde Secrets Manager
- Integración con CloudWatch Logs

### Dockerfile
Imagen optimizada:
- Base: `amazoncorretto:17-alpine`
- JAR de Spring Boot
- Puerto 8080 expuesto

---

## Ventajas de Servicios No Administrados Implementados

### ECS Fargate vs EC2 tradicional
✅ **Sin gestión de servidores**
✅ **Escalamiento automático**
✅ **Pago por uso (sin sobreaprovisionamiento)**
✅ **Actualizaciones automáticas de infraestructura**
✅ **Alta disponibilidad integrada**

### CodeBuild vs Jenkins tradicional
✅ **Sin mantenimiento de servidor CI/CD**
✅ **Escalamiento automático de workers**
✅ **Integración nativa con AWS**
✅ **Pago solo durante compilaciones**

---

## Estado Actual

- ✅ **ECR:** Repositorio creado con imagen Docker
- ✅ **CodeBuild:** Build exitoso (#8)
- ✅ **ECS Cluster:** Activo y configurado
- ✅ **Task Definition:** Revisión 4 registrada
- ✅ **IAM Roles:** Configurados con permisos adecuados
- ✅ **Security Groups:** Reglas de firewall aplicadas
- ✅ **CloudWatch:** Logs centralizados
- ⏳ **Servicio ECS:** Configurado (requiere base de datos para ejecución completa)

---

## Evidencias Técnicas

1. **Código sin modificaciones:** La aplicación original no fue alterada
2. **Configuración cloud-native:** Todos los archivos (buildspec.yml, taskdef.json, Dockerfile)
3. **Build automatizado:** CodeBuild compila y genera imágenes
4. **Contenedores en ECR:** Registro privado con imágenes versionadas
5. **Infraestructura serverless:** ECS Fargate sin servidores que administrar

---

## Conclusión

Se implementó exitosamente una arquitectura de despliegue cloud utilizando **servicios no administrados de AWS**, demostrando:

- ✅ Comprensión de computación en la nube
- ✅ Uso de servicios serverless (ECS Fargate, CodeBuild)
- ✅ Automatización de CI/CD
- ✅ Seguridad con IAM y Secrets Manager
- ✅ Alta disponibilidad multi-AZ
- ✅ Monitoreo con CloudWatch

**Documentación completa:** [DESPLIEGUE.md](./DESPLIEGUE.md)

---

**Fecha de entrega:** 24 de noviembre de 2025
