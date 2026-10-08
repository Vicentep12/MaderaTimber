# Registro de decisiones

Las decisiones D-001 a D-004 son **observadas en el código** al 2026-10-08; la fecha de revisión no es la fecha histórica de adopción. No se recuperaron justificaciones del equipo desde conversaciones. Las alternativas indicadas son comparaciones posibles, no evidencia de una evaluación previa.

## D-001 — Android nativo con Kotlin y Compose

- **Estado:** observado, existente.
- **Evidencia:** `MainActivity.kt`, `ui/theme/`, dependencias y `buildFeatures.compose` en `app/build.gradle.kts`.
- **Decisión:** actividad Android y UI declarativa Compose con Material 3.
- **Justificación histórica:** no documentada.
- **Alternativas posibles:** Views/XML para UI; un framework multiplataforma. No consta que se hayan considerado.
- **Consecuencias:** requiere herramientas Android; componentes Compose y tema se desarrollan en Kotlin. No existe aplicación iOS/web.

## D-002 — Un módulo de aplicación

- **Estado:** observado, existente.
- **Evidencia:** `include(":app")` en `settings.gradle.kts` y contenido de `app/src`.
- **Decisión:** concentrar la aplicación en `:app` y los archivos de UI/tema actuales.
- **Justificación histórica:** no documentada. Que sea suficiente para una base pequeña es una inferencia, no una intención atribuida al equipo.
- **Alternativas posibles:** módulos por características/capas; MVVM dentro del mismo módulo. Ninguna está implementada ni aprobada.
- **Consecuencias:** estructura pequeña; separar responsabilidades futuras requerirá una decisión según requisitos, no una refactorización automática.

## D-003 — Wrapper, catálogo y toolchain del daemon

- **Estado:** observado, existente; compatibilidad completa pendiente de validación.
- **Evidencia:** `gradle/wrapper/*`, `gradle/libs.versions.toml`, `gradle/gradle-daemon-jvm.properties`, scripts `.kts`.
- **Decisión:** versiones centralizadas, distribución Gradle con checksum y daemon JDK 25. Versiones concretas en README.
- **Justificación histórica:** no documentada.
- **Alternativas posibles:** Gradle global y versiones inline; no adoptadas en el árbol actual.
- **Consecuencias:** configuración transportable, pero primer uso necesita resolver descargas y SDK por equipo. Java source/target 11 no habilita ejecutar el build con JDK 11. No deducir la versión Kotlin integrada de la del plugin Compose.

## D-004 — Identidad y configuración Android actuales

- **Estado:** observado; no hay decisión de unificación de nombres.
- **Evidencia:** manifiesto, `strings.xml`, `settings.gradle.kts`, `app/build.gradle.kts`.
- **Decisión observada:** identificador `com.duoc.madertimber`, nombre visible/proyecto `MaderTimber`, SDK mínimo 24 y compilación/objetivo 37; release sin optimización. No hay configuración propia de firma release.
- **Justificación histórica y alternativas evaluadas:** desconocidas.
- **Consecuencias:** renombrar identificadores cambia identidad de instalación y prueba instrumentada; debe acordarse. Reglas de backup/firma/optimización necesitan revisión al implementar datos o preparar una entrega.

## D-005 — Contexto persistente versionado

- **Fecha:** 2026-10-08.
- **Estado:** acordado por solicitud explícita del propietario; implementado en esta entrega local.
- **Contexto:** continuar desde distintos equipos y sesiones sin depender del historial del chat.
- **Decisión:** `AGENTS.md` como instrucciones y `docs/contexto.md` como entrada rápida; documentos separados para arquitectura, decisiones, estado y tareas; README para instalación y Cloud.
- **Justificación:** los archivos dentro de la raíz Git viajan con el mismo código y pueden revisarse conjuntamente.
- **Alternativas:** usar solo memoria/historial de chat (no cumple el requisito); documentación externa como única fuente (no acompaña necesariamente al clon).
- **Consecuencias:** cada cambio significativo debe actualizar los documentos afectados. Git y su sincronización autorizada transportan el contexto; el estado guardado de una tarea Cloud no reemplaza Git. No se publican cambios remotos en esta entrega.

## Cómo registrar una nueva decisión

Agregar un ID consecutivo con fecha, estado (`propuesta`, `aceptada`, `observada` o `sustituida`), contexto/evidencia, decisión, justificación, alternativas y consecuencias. No convertir propuestas en hechos. Si una decisión se reemplaza, conservar su registro y enlazar el nuevo ID; no crear un diario de cada cambio menor.
