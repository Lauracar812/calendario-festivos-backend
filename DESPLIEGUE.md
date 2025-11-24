# Despliegue AWS - Pipeline CI/CD

## Información del Proyecto
- **Repositorio:** https://github.com/Lauracar812/calendario-festivos-backend
- **Aplicación:** API REST - Calendario de Festivos (Spring Boot 3.5.5)
- **Región AWS:** us-east-2 (Ohio)
- **Cuenta AWS:** 110412263170

---

## Arquitectura Implementada

```
GitHub → CodeBuild → ECR → ECS Fargate → Application Load Balancer
```

**Servicios AWS utilizados:**
- **ECR** - Registro de imágenes Docker
- **CodeBuild** - Compilación y construcción de imágenes
- **ECS Fargate** - Ejecución serverless de contenedores
- **Application Load Balancer** - Distribución de tráfico
- **CloudWatch** - Logs y monitoreo
- **Secrets Manager** - Gestión de credenciales
- **IAM** - Roles y permisos

---

## URLs de Recursos AWS

### 1. Repositorio de Imágenes (ECR)
```
https://us-east-2.console.aws.amazon.com/ecr/repositories/private/110412263170/calendario-festivos-backend?region=us-east-2
```
- **URI:** `110412263170.dkr.ecr.us-east-2.amazonaws.com/calendario-festivos-backend`
- **Estado:** Activo con imagen `latest` y tags por commit

### 2. Proyecto CodeBuild
```
https://us-east-2.console.aws.amazon.com/codesuite/codebuild/projects/calendario-festivos-build?region=us-east-2
```
- **Build exitoso:** #8
- **Fases:** Maven compile → Docker build → ECR push
- **Tiempo de compilación:** ~50 segundos

### 3. Cluster ECS
```
https://us-east-2.console.aws.amazon.com/ecs/v2/clusters/calendario-festivos-cluster?region=us-east-2
```
- **Nombre:** calendario-festivos-cluster
- **Tipo:** AWS Fargate (serverless)
- **Estado:** ACTIVE

### 4. Servicio ECS
```
https://us-east-2.console.aws.amazon.com/ecs/v2/clusters/calendario-festivos-cluster/services/calendario-festivos-service?region=us-east-2
```
- **Nombre:** calendario-festivos-service
- **Tareas deseadas:** 1
- **Task Definition:** calendario-festivos-task:4
- **Estrategia:** Rolling update

### 5. Application Load Balancer
```
https://us-east-2.console.aws.amazon.com/ec2/home?region=us-east-2#LoadBalancers:
```
- **DNS:** `calendario-festivos-alb-1112364300.us-east-2.elb.amazonaws.com`
- **Puerto:** 80 → 8080 (contenedor)
- **Health checks:** Configurados en `/`

### 6. CloudWatch Logs
```
https://us-east-2.console.aws.amazon.com/cloudwatch/home?region=us-east-2#logsV2:log-groups/log-group/$252Fecs$252Fcalendario-festivos
```
- **Log Group:** `/ecs/calendario-festivos`
- **Retención:** 7 días

### 7. Secrets Manager
```
https://us-east-2.console.aws.amazon.com/secretsmanager/home?region=us-east-2#!/secret?name=calendario-festivos%2Fdb-password
```
- **Secret:** `calendario-festivos/db-password`
- **ARN:** `arn:aws:secretsmanager:us-east-2:110412263170:secret:calendario-festivos/db-password-Me1hX1`

---

## Archivos de Configuración

### buildspec.yml
Configuración de CodeBuild para:
- Compilación Maven de la aplicación Spring Boot
- Construcción de imagen Docker
- Push a ECR
- Generación de `imagedefinitions.json` para ECS

### taskdef.json
Definición de tarea ECS que especifica:
- Recursos: 512 CPU, 1024 MB RAM
- Puerto: 8080
- Imagen: `110412263170.dkr.ecr.us-east-2.amazonaws.com/calendario-festivos-backend:latest`
- Secretos desde Secrets Manager
- Logs en CloudWatch

### Dockerfile
Imagen Docker basada en `amazoncorretto:17-alpine` con la aplicación Spring Boot compilada.

### appspec.yml
Especificación de despliegue para CodeDeploy (integración futura con CodePipeline).

---

## Flujo de Despliegue

1. **Push a GitHub** → Código actualizado en branch `main`
2. **CodeBuild** → Se ejecuta automáticamente (o manualmente)
   - Descarga código desde GitHub
   - Compila con Maven (`mvn clean package`)
   - Construye imagen Docker
   - Sube imagen a ECR con tag del commit
3. **ECR** → Almacena la imagen Docker
4. **ECS** → Detecta nueva imagen y despliega
   - Descarga imagen desde ECR
   - Inicia nueva tarea con la imagen actualizada
   - Registra en Target Group del ALB
5. **ALB** → Distribuye tráfico a la nueva tarea
6. **CloudWatch** → Captura logs de la aplicación

---

## Endpoint de la API

**URL pública:**
```
http://calendario-festivos-alb-1112364300.us-east-2.elb.amazonaws.com
```

**Ejemplo de prueba:**
```bash
curl http://calendario-festivos-alb-1112364300.us-east-2.elb.amazonaws.com/
```

---

## Seguridad

### Security Groups
- **ALB SG:** Permite HTTP (80) desde internet (0.0.0.0/0)
- **ECS SG:** Permite tráfico en puerto 8080 solo desde ALB

### IAM Roles
- **ecsTaskExecutionRole:** Permite a ECS descargar imágenes de ECR, leer secretos y escribir logs
- **ecsTaskRole:** Permisos de la aplicación en ejecución
- **codebuild-service-role:** Permite a CodeBuild acceder a ECR, ECS y CloudWatch

### Secrets Manager
- Contraseña de base de datos almacenada de forma segura
- Inyectada como variable de entorno en el contenedor

---

## Networking

### VPC
- **VPC ID:** vpc-050cecf16a9a36c8e
- **Subnets públicas:**
  - subnet-0b13dbd91c560efd3 (us-east-2a)
  - subnet-08bab083089c5760b (us-east-2b)

### Target Group
- **ARN:** `arn:aws:elasticloadbalancing:us-east-2:110412263170:targetgroup/calendario-festivos-tg/d289cc8bc50b3a6f`
- **Health check:** HTTP en puerto 8080, ruta `/`
- **Thresholds:** 3 intentos, 30s intervalo

---

## Monitoreo y Logs

### CloudWatch Logs
Los logs de la aplicación se envían automáticamente a:
- **Log Group:** `/ecs/calendario-festivos`
- **Stream prefix:** `ecs/calendario-festivos-container/[task-id]`

### Métricas disponibles
- CPU y memoria del contenedor
- Número de tareas en ejecución
- Health checks del ALB
- Requests por segundo

---

## Build History

| Build # | Estado | Commit | Duración | Fecha |
|---------|--------|--------|----------|-------|
| #8 | ✅ SUCCESS | 53793a9 | 50s | 24 Nov 2025 00:48 |
| #7 | ❌ FAILED | 1dfdc50 | 15s | Docker image error |
| #6 | ❌ FAILED | 643d3dc | 13s | YAML error |

---

## Notas Técnicas

- **No se modificó el código fuente** de la aplicación Java
- **Despliegue serverless** con Fargate (sin gestión de servidores)
- **Rolling updates** sin downtime
- **Logs centralizados** en CloudWatch
- **Credenciales seguras** en Secrets Manager
- **Pipeline listo** para integración con CodePipeline

---

## Próximas Mejoras

- [ ] Crear CodePipeline completo (GitHub → CodeBuild → ECS automático)
- [ ] Implementar RDS PostgreSQL para datos persistentes
- [ ] Configurar Auto Scaling basado en CPU/memoria
- [ ] Agregar certificado SSL/HTTPS en el ALB
- [ ] Implementar blue/green deployment

---

**Fecha de despliegue:** 24 de noviembre de 2025
