# Evaluación 4 - Seguimiento

## Objetivo de la Evaluación
**Programar las respectivas operaciones para automatizar el despliegue de la API para calendarios laborales de una compañía desarrollada en Spring Boot.**

## IP Pública / DNS Público del Cluster

### 🌐 Endpoint de la API Desplegada
```
http://calendario-festivos-alb-1112364300.us-east-2.elb.amazonaws.com
```

**Estado:** ✅ **API REST FUNCIONANDO**

**DNS del Application Load Balancer:**
- `calendario-festivos-alb-1112364300.us-east-2.elb.amazonaws.com`

**Puertos:**
- Puerto público: **80 (HTTP)**
- Puerto contenedor: **8080**

---

## Información del Proyecto

- **Estudiante:** Laura Carolina Carodona
- **Repositorio:** https://github.com/Lauracar812/calendario-festivos-backend
- **Aplicación:** API REST - Calendario de Festivos (Spring Boot 3.5.5, Java 17)
- **Región AWS:** us-east-2 (Ohio)
- **Fecha:** 25 de noviembre de 2025

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

## Operaciones Automatizadas Implementadas

### 1. Compilación Automática (CodeBuild)
- **Trigger:** Push a GitHub branch `main`
- **Proceso:** Maven compile → Docker build → Push a ECR
- **Tiempo:** ~50 segundos
- **Estado:** ✅ Build #8 exitoso

### 2. Generación de Imagen Docker
- **Base:** amazoncorretto:17-alpine
- **Artefacto:** JAR de Spring Boot
- **Registro:** Amazon ECR
- **Versionamiento:** Por commit hash + tag `latest`

### 3. Despliegue Automático a ECS
- **Estrategia:** Rolling update
- **Descarga automática:** Imagen desde ECR
- **Registro automático:** En Target Group del ALB
- **Health checks:** Automáticos cada 30 segundos

### 4. Alta Disponibilidad
- **Multi-AZ:** Zonas us-east-2a y us-east-2b
- **Auto-healing:** ECS reinicia tareas fallidas automáticamente
- **Load Balancing:** Distribución de tráfico por ALB

---

## Estado Actual - ✅ COMPLETADO

- ✅ **ECR:** Repositorio creado con imagen Docker (tag: latest, 53793a9)
- ✅ **CodeBuild:** Build exitoso (#8, 50 segundos)
- ✅ **ECS Cluster:** Activo con 1 tarea RUNNING
- ✅ **Task Definition:** Revisión 5 con RDS configurado
- ✅ **RDS PostgreSQL:** Base de datos `calendario_festivos` creada
- ✅ **IAM Roles:** ecsTaskExecutionRole, ecsTaskRole, CodeBuild role
- ✅ **Security Groups:** Reglas configuradas (ALB→Internet, ECS→ALB, ECS→RDS)
- ✅ **CloudWatch:** Logs centralizados en `/ecs/calendario-festivos`
- ✅ **Servicio ECS:** 1 tarea ejecutándose correctamente
- ✅ **API Pública:** Accesible en `http://calendario-festivos-alb-1112364300.us-east-2.elb.amazonaws.com`

---

## Evidencias Técnicas

1. **Código sin modificaciones:** La aplicación original no fue alterada
2. **Configuración cloud-native:** Todos los archivos (buildspec.yml, taskdef.json, Dockerfile)
3. **Build automatizado:** CodeBuild compila y genera imágenes
4. **Contenedores en ECR:** Registro privado con imágenes versionadas
5. **Infraestructura serverless:** ECS Fargate sin servidores que administrar

---

## Flujo de Despliegue Automatizado Completo

```
1. Developer Push
   ↓
   git push origin main
   ↓
2. CodeBuild Trigger (Automático)
   ↓
   • Descarga código de GitHub
   • Compila: mvn clean package
   • Build Docker: amazoncorretto:17-alpine + JAR
   • Tag: commit-hash
   • Push a ECR
   ↓
3. Imagen en ECR
   ↓
4. ECS Fargate (Automático)
   ↓
   • Detecta nueva imagen
   • Pull desde ECR
   • Inicia nueva tarea
   • Health check (30s intervalo)
   • Registra en Target Group
   ↓
5. Application Load Balancer
   ↓
   • Valida health check
   • Redirige tráfico a nueva tarea
   • Termina tarea antigua
   ↓
6. API Disponible Públicamente
   ↓
   http://calendario-festivos-alb-1112364300.us-east-2.elb.amazonaws.com
```

---

## Prueba de Funcionamiento

### Acceso Público a la API
**URL:** http://calendario-festivos-alb-1112364300.us-east-2.elb.amazonaws.com

**Prueba desde terminal:**
```bash
curl http://calendario-festivos-alb-1112364300.us-east-2.elb.amazonaws.com
```

**Respuesta esperada:** Página de Spring Boot o respuesta JSON de la API

---

## Conclusión

✅ **Se implementó exitosamente un pipeline de despliegue automatizado** para la API de Calendarios Laborales desarrollada en Spring Boot, cumpliendo con:

### Requisitos Cumplidos:
1. ✅ **Operaciones automatizadas:** CodeBuild + ECS con despliegue automático
2. ✅ **API desplegada:** Funcionando en cluster ECS Fargate
3. ✅ **IP/DNS público proporcionado:** `calendario-festivos-alb-1112364300.us-east-2.elb.amazonaws.com`
4. ✅ **Servicios no administrados:** ECS Fargate (sin servidores que gestionar)
5. ✅ **Alta disponibilidad:** Multi-AZ con auto-scaling
6. ✅ **Seguridad:** IAM roles, Secrets Manager, Security Groups
7. ✅ **Persistencia:** RDS PostgreSQL integrado
8. ✅ **Monitoreo:** CloudWatch Logs centralizado

### Arquitectura Cloud Implementada:
- **Computación:** ECS Fargate (serverless containers)
- **CI/CD:** CodeBuild (compilación automática)
- **Registro:** ECR (imágenes Docker versionadas)
- **Balanceo:** Application Load Balancer
- **Base de datos:** RDS PostgreSQL
- **Seguridad:** IAM + Secrets Manager
- **Monitoreo:** CloudWatch
- **Networking:** VPC + Security Groups multi-AZ

**Documentación técnica completa:** [DESPLIEGUE.md](./DESPLIEGUE.md)

---

**Fecha de entrega:** 25 de noviembre de 2025  
**Estado final:** ✅ API desplegada y funcionando correctamente
