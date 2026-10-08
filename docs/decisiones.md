# Decisiones vigentes

Actualización: 2026-10-08. Las configuraciones observadas no implican una justificación histórica conocida. El historial detallado de reorganizaciones se conserva en Git; este registro mantiene las decisiones que afectan el trabajo actual.

## Arquitectura e identidad

| Referencia | Decisión y estado | Consecuencia |
| --- | --- | --- |
| D-001 | Android nativo con Kotlin y Compose/Material 3, observado en el código. | Usar las herramientas y UI existentes; no hay versión web/iOS. |
| D-002 | Un módulo :app, observado. No se aprobó modularización adicional. | Implementar las responsabilidades MVVM dentro de la estructura existente antes de considerar más módulos. |
| D-003 | Wrapper, catálogo de versiones y toolchain del daemon, observados. | Versiones/comandos en README y Gradle; no confundir JDK del daemon con source/target Java. |
| D-004 | Mantener identidad com.duoc.madertimber y nombres actuales MaderaTimber/MaderTimber. | No renombrar paquete, proyecto o app sin instrucción explícita. |
| D-006 | MaderaTimber es el panel CENAMAD y la asignatura exige MVVM, confirmado por el usuario. | View Compose, estado/eventos en ViewModel y datos/cálculos en Model/Repository. Implementación pendiente. |

## Documentación y tutoría

Las decisiones D-005, D-007 y D-008 se consolidan en la política siguiente, confirmada por el usuario:

- Mantener los planes completos, requisitos y progreso dentro del repositorio para continuar desde distintos dispositivos.
- Conservar una fuente por tema: especificación para contratos y plan técnico detallado; plan operativo para tareas/estado; aprendizaje para perfil/recorrido; progreso para evidencias del estudiante; estado técnico para validaciones; arquitectura para responsabilidades.
- README contiene instalación y comandos, incluida la continuidad Cloud. AGENTS.md establece cómo trabajar y qué leer.
- La raíz mantiene README y AGENTS; la documentación temática vive en docs/. El AGENTS de la carpeta superior solo dirige al repositorio.
- Respetar la tutoría: una lección a la vez, código escrito por el usuario, revisión guiada y conceptos aplicados a Kotlin/Android.
- Guardar un archivo local no lo sincroniza con otro dispositivo: Git transporta código y documentos una vez guardados y subidos con autorización.

Esta política sustituye las versiones anteriores que dependían de documentos externos o síntesis incompletas. Los años 2022–2024 y la selección inicial 2024 se conservan; usar 2026 fue una sugerencia no adoptada.

## Consolidación de contenido — D-009

- **Fecha:** 2026-10-08.
- **Estado:** aplicada por solicitud del usuario.
- **Decisión:** integrar contexto y pendientes en plan-implementacion.md; integrar las notas técnicas Cloud útiles en README; retirar sus archivos redundantes. Reducir los relatos de reorganización y conservar evidencias de aprendizaje y validación.
- **Motivo:** evitar varias versiones del siguiente paso, del estado y de las mismas reglas.
- **Consecuencia:** preservar los contratos y el recorrido completos, actualizar enlaces e instrucciones y mantener una ubicación por tema. No cambia alcance ni código.

## Nuevas decisiones

Registrar solo decisiones que alteren alcance, arquitectura o forma de trabajar: fecha, estado, evidencia/motivo, decisión y consecuencia. Si reemplazan otra decisión, indicar cuál. Los movimientos menores y resultados de comandos pertenecen al diff o al estado técnico, no a un nuevo registro por cada edición.
