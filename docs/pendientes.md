# Pendientes priorizados

Actualización: 2026-10-08. **Tareas propuestas**, excepto la documentación solicitada ya preparada. Prioridad P0 desbloquea continuidad/desarrollo; P1 establece la primera entrega; P2 depende de requisitos. No hay funcionalidades de negocio aprobadas en este documento.

## P0 — Continuidad y entorno

- [ ] **Compartir el contexto por Git con autorización.** Revisar diff y exclusiones, guardar AGENTS/README/docs/configuración en un commit y subirlo cuando el propietario lo decida. Cierre: la rama remota elegida contiene los documentos y otro clon puede leerlos. Codex no ha realizado commit ni push en esta entrega.
- [ ] **Validar instalación reproducible.** En Windows usar una ruta sin tildes (AGP rechaza la ruta actual). Confirmar SDK 37, JDK 25, Build Tools, licencias y compatibilidad AGP/Kotlin Compose; ejecutar Wrapper, APK, prueba local y Lint desde un clon limpio. Registrar versiones/resultados en README y estado actual. No cambiar versiones sin explicar motivo e impacto.
- [ ] **Resolver el inicio de AAPT2 en este entorno Windows.** El clon sin tildes falla en `processDebugResources`; confirmar runtime Windows, permisos/restricciones y ubicación de caché antes de atribuirlo al código. Cierre: `aapt2 version` y build de recursos funcionan; después ejecutar los checks completos pendientes. Detalle del error en `estado-actual.md`.
- [ ] **Definir el primer alcance de Cenamad con el propietario.** Incorporar propósito, usuarios, primer caso de uso y criterios de aceptación. Cierre: requisitos explícitos versionados; ninguna funcionalidad deducida solo del nombre MaderaTimber.
- [ ] **Resolver/confirmar los nombres.** Aclarar relación Cenamad/MaderaTimber/MaderTimber y si se desea unificar solo documentación, etiqueta o identidad Android. Cierre: decisión registrada; cualquier cambio de paquete requiere autorización específica.
- [ ] **Configurar Codex Cloud manualmente.** Conectar GitHub, seleccionar el repositorio, preparar JDK/SDK/red y verificar comandos antes de publicar el entorno. Cierre: una tarea nueva recupera el contexto y reporta los checks disponibles; registrar límites de emulador. Guía en README.

## P1 — Primera funcionalidad verificable

- [ ] **Implementar el primer caso de uso confirmado.** Definir UI, datos, errores y aceptación antes del código; mantener alcance pequeño. Cierre: comportamiento validado y estado/contexto actualizados.
- [ ] **Decidir la estructura cuando el alcance lo requiera.** Evaluar estado de UI/ViewModel, navegación y separación de responsabilidades según el caso de uso. No implantar MVVM, Clean Architecture, backend o persistencia sin una necesidad confirmada. Cierre: decisión documentada con consecuencias.
- [ ] **Añadir pruebas de comportamiento del primer caso de uso.** Las dos pruebas actuales son ejemplos. Cierre: casos relevantes de éxito/error, más prueba de UI si existe interacción, ejecutados donde corresponda.
- [ ] **Verificar en dispositivo/emulador.** Ejecutar prueba instrumentada y revisar UI, tema/padding y compatibilidad con el mínimo Android declarado. Cierre: registrar dispositivo/API y resultado; no confundir Preview con ejecución.

## P2 — Según evolución y requisitos

- [ ] **Automatizar checks en CI.** Tras validar un build limpio, proponer configuración con JDK/SDK fijados y prueba local/Lint/build. No hay workflow actual.
- [ ] **Revisar backup y firma release.** Cuando existan datos/entrega, decidir exclusiones de backup, custodia de keystore y optimización release. Mantener secretos fuera de Git.
- [ ] **Definir identidad visual, textos y accesibilidad.** Tras confirmar producto/diseño; el saludo, iconos y paletas actuales son de ejemplo.
- [ ] **Documentar integraciones/dependencias nuevas.** Solo si se incorporan: contrato API, configuración sin valores secretos, restricciones offline y pruebas correspondientes.

## Completado en esta preparación

- [x] Configurar `android.overridePathCheck=true` en `gradle.properties` para permitir la sincronización y compilación en rutas con caracteres no ASCII en Windows.
- [x] Analizar el árbol, código, configuración, pruebas y metadatos Git locales sin reescribir funcionalidades.
- [x] Crear el sistema de contexto persistente y reglas de actualización en la raíz Git.
- [x] Ampliar exclusiones de archivos locales/sensibles y fijar finales de línea para documentación y wrappers.
- [x] Documentar instalación entre equipos, pasos manuales Cloud y límites conocidos.

Mantener tareas con criterio de cierre; al completar una, actualizar también `estado-actual.md`. No convertir esta lista en un registro extenso de conversaciones ni agregar funciones de producto sin confirmación.
