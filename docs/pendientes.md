# Pendientes priorizados

Actualización: 2026-10-08. Priorizar el [plan acordado](../PLAN_IMPLEMENTACION.md); requisitos confirmados: panel CENAMAD, primera pantalla parcial y MVVM. La aceptación detallada debe contrastarse con la especificación original.

## P0 — Continuidad

- [ ] **Contrastar los originales Windows.** Integrar AGENTS/APRENDIZAJE/PROGRESO y README del MVP de la carpeta superior indicada por el usuario; conservar cambios de su código. Cierre: requisitos y progreso comparados, sin acuerdos perdidos ni duplicaciones divergentes.
- [ ] **Compartir la integración por Git con autorización.** Revisar diff/exclusiones y subir código/documentación juntos. Cierre: otro clon recibe plan, tutoría y especificación en la misma revisión. Commit local autorizado por el usuario; push pendiente.
- [ ] **Actualizar el inicio publicado de Cloud.** El START local remite a las directrices versionadas; falta integrar esa referencia en la configuración del servicio mediante su interfaz. Cierre: una tarea nueva lee el plan y recupera el siguiente paso; no basta guardar un archivo local.

## P1 — Plan de implementación y aprendizaje

- [ ] **Próxima lección: pantalla parcial Compose.** El usuario escribe título, «Construcción sustentable», 2024, «Proyectos activos: 8» y etiqueta demostrativa. Cierre: código revisado, ejecución visual Android y conceptos registrados en PROGRESO.
- [ ] **Modelos y Repository local.** Contrastar contratos y reglas del README original. Cierre: datos demostrativos explícitos y cálculos definidos.
- [ ] **Implementar MVVM, estado y filtros.** DashboardViewModel, estado observable y eventos; lógica de datos en Repository. Cierre: selección de línea/año actualiza indicadores y pruebas de comportamiento verifican el flujo.
- [ ] **Completar indicadores, comparación y detalle.** Según especificación recuperada y aceptación detallada; no inventar fórmulas de impacto.
- [ ] **Validar funcionalidad en Android.** Pruebas relevantes, build/JVM/Lint y ejecución visual/instrumentada. Cierre: dispositivo/API y resultados registrados; pruebas de plantilla no acreditan el MVP.

## P2 — Entorno y evolución

- [ ] **Validar clon limpio en otro equipo y restauración Cloud.** Build inicial Cloud acreditado por registro/artefactos; falta comprobar traslado/restauración. Comandos en README.
- [ ] **Diagnosticar AAPT2 Windows si persiste.** Hay un fallo histórico y un assemble posterior registrado; no asumir que sigue bloqueando todos los equipos. Contrastar ejecución/caché actuales antes de cambiar dependencias.
- [ ] **CI, accesibilidad y entrega.** Abordar cuando lo exija el avance; firma, backup e identidad visual requieren decisiones concretas. Sin cambio de identidad Android autorizado.

## Preparado

- [x] Contexto técnico versionado y protección de archivos locales/sensibles.
- [x] `android.overridePathCheck=true` para rutas Windows no ASCII.
- [x] Recuperar acuerdos de producto, MVVM, aprendizaje aplicado y próxima lección; integrarlos dentro de la raíz Git y carpeta del MVP.
- [x] Directrices de nube enlazadas al plan común y al progreso.

Las dos últimas tareas están preparadas localmente; originales Windows, sincronización remota y configuración publicada siguen pendientes.
