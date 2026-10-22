# Plan de implementación y continuidad

Actualización: 2026-10-22. Esta es la única lista de etapas, tareas abiertas y próxima actividad. Los requisitos y los doce pasos técnicos completos están en [especificacion.md](especificacion.md); el recorrido pedagógico completo, en [aprendizaje.md](aprendizaje.md).

## Retomar una sesión

1. Trabajar desde la raíz Git y leer [AGENTS.md](../AGENTS.md).
2. Revisar [progreso.md](progreso.md), [estado-actual.md](estado-actual.md) y las etapas siguientes. Comprobar rama y cambios locales antes de editar.
3. Consultar las secciones relevantes de la especificación y adaptar la lección al aprendizaje comprobado.
4. Al cerrar, actualizar el progreso del usuario, la evidencia técnica y el estado de la etapa afectada.
5. Para continuar en otro dispositivo, guardar y sincronizar los cambios mediante Git con autorización. Instalación y comandos: [README](../README.md).

## Pantallas de acceso solicitadas el 2026-10-18

Implementadas las vistas de login y registro con estado y eventos en LoginViewModel/RegisterViewModel, reglas en AuthValidator, validación local, contraseñas ocultables, navegación entre formularios y entrada al dashboard como invitado. Los envíos válidos muestran un aviso de demostración: no se crean cuentas ni se inicia una sesión real. No se añade backend ni almacenamiento de credenciales.

Pendiente comprobar visualmente ambos formularios en Android, teclado, errores, regreso y acceso como invitado. La separación MVVM del acceso no acredita aprendizaje ni sustituye las etapas MVVM del dashboard. Se añadieron pruebas JVM de los eventos y validaciones de los formularios.

## Objetivo de entrega y próxima actividad

Prioridad solicitada el 2026-10-20: completar las diez vistas del mockup y añadir inicio de sesión y registro al diseño, con implementación y verificación de las doce vistas/estados. Se propone cerrar el viernes **30 de octubre de 2026**, dentro de la semana del 26 al 30; el día exacto de entrega y la disponibilidad del equipo no están confirmados. Las fechas de las tarjetas son objetivos de planificación, no compromisos ya acordados.

T01 completada: plano de doce vistas y mapa de navegación revisados; el propietario autorizó su commit el 2026-10-22. Próxima actividad de implementación: T02, modelos del panel, seguida de T03–T04 (datos y cálculos); continuar con estado/navegación antes de conectar la UI. Las tarjetas T01–T17 de abajo desglosan las etapas existentes para Trello, sin reemplazar los doce pasos técnicos ni las cinco iteraciones de la especificación. Mantener como alcance acceso demostrativo y datos locales; autenticación real no está incluida.

## Continuidad de tutoría


El público principal confirmado el 2026-10-15 son estudiantes. Al preparar las pantallas y sus textos, aplicar las pautas y la referencia de doce vistas de las secciones 2.1–2.3 de [especificacion.md](especificacion.md). Se mantienen el plan técnico, la muestra 2022–2024 y la actividad de tutoría siguiente.

Por petición explícita de implementación, Codex creó la primera vista estática: título, “Construcción sustentable”, año 2024, cuatro tarjetas, etiqueta “Datos demostrativos” y comparación de proyectos activos entre las tres líneas. Valores fijos de la muestra del plan; sin filtros interactivos ni navegación.

Tutoría retomada el 2026-10-22 por petición del usuario con T02: él escribe el código y Codex guía y revisa un ejercicio a la vez. Se revisaron en el chat `LineaInvestigacion`, `ValorIndicador`, las opciones de `TipoIndicador` e `Indicador`; se comprobó la comprensión del ID estable, de las opciones limitadas del enum y de la asociación entre año y resultado en la lista de valores. No se verificaron los archivos ni su compilación en este checkout; queda comprobar que el enum no contiene los caracteres de formato del mensaje. Próximo ejercicio: crear `EstadoProyecto`, antes de `SeguimientoProyecto` y `Proyecto`. La revisión de DashboardScreen, Column, Card, Modifier y funciones reutilizables sigue pendiente; la práctica estática no acredita el dashboard completo ni MVVM. Introducir Repository y ViewModel antes de filtros interactivos.

## Etapas y tareas

La numeración conserva los doce pasos de la sección 11 de la especificación. Su detalle, fórmulas y aceptación se mantienen allí; esta tabla registra la ejecución.

| Paso | Tarea | Estado y criterio de cierre |
| --- | --- | --- |
| 1. Alcance | Confirmar flujo, líneas, indicadores, años y aceptación. | Definido en la especificación. |
| 2. Base Android | Crear/configurar proyecto y ejecutar la pantalla inicial. | Proyecto existente; ejecución inicial registrada en tutoría. No recrearlo. |
| 3. Dependencias | Completar navegación, ViewModel, coroutines y lectura JSON según cada incremento. | Parcial: Compose/Material 3, Lifecycle ViewModel y pruebas de acceso/plantilla; sin Navigation Compose ni lector JSON explícitos. Cada cambio de Gradle debe compilar. |
| 4. Modelos | Implementar contratos de la sección 7 e integridad de la sección 8. | Pendiente. IDs/referencias únicos, relaciones temporales y rangos válidos. |
| 5. Fuente local | JSON, lector y Repository; calcular indicadores desde entidades. | Pendiente. Datos coherentes, funcionamiento offline y carga/éxito/vacío/error diferenciados. |
| 6. Navegación | Definir rutas y argumentos, montar pantallas y validar regreso. | Parcial: acceso login/registro/invitado existente; flujo del panel pendiente. Definir rutas al preparar el flujo; completar conservación de filtros después del paso 7. |
| 7. ViewModel | Estado observable, eventos y coordinación de datos/filtros. | Parcial: acceso implementado; dashboard y detalles pendientes. Incorporarlo antes de filtros interactivos; restaurar selección con SavedStateHandle. |
| 8. Dashboard | Completar selectores, cuatro tarjetas, comparativo y acceso al detalle. | Parcial: primera vista estática implementada. Faltan datos desde Repository/ViewModel, selectores interactivos, métrica seleccionable, acceso al detalle y validación visual Android. |
| 9. Detalle de línea | Cinco indicadores, histórico y entidades relacionadas. | Pendiente. Aplicar línea/año y estados vacíos según secciones 9.2 y 9.4. |
| 10. Detalle de indicador | Valor seleccionado, variación, histórico e interpretación. | Pendiente. Aplicar reglas de ausencia, base cero y puntos porcentuales de la sección 8. |
| 11. Diseño y accesibilidad | Contraste, legibilidad, descripciones y pantallas pequeñas/grandes. | Pendiente para el producto. Validar durante cada pantalla y en la revisión final. |
| 12. Persistencia opcional | Filtros entre sesiones, favoritos o historial si queda tiempo. | Opcional. No bloquea el MVP ni sustituye la restauración de estado obligatoria del flujo. |

Los contenidos relacionados incluyen las pestañas según el indicador de origen y fichas breves, aunque no tengan un paso numerado independiente. Implementarlos con los detalles de los pasos 9–10.

## Validación y entrega

- [ ] Ejecutar las pruebas de comportamiento de la sección 14: cálculo, filtros, integridad, ausencias, restauración y navegación.
- [ ] Comprobar build, pruebas JVM y Lint; con Android disponible, pruebas UI y revisión visual.
- [ ] Verificar todos los criterios de la sección 12 y realizar el recorrido de demostración.
- [ ] Preparar los entregables completos de la sección 15: código/datos, instrucciones, arquitectura/modelo, capturas, casos y video o demostración.

Organizar los incrementos según las cinco iteraciones de la sección 13. No cambiar requisitos ni dar una etapa por completada solo porque compiló la plantilla.

## Continuidad y entorno pendientes

- [x] Sincronizar con el remoto el commit del mockup, el mapa y su documentación. El propietario autorizó pasarlo todo a `main` el 2026-10-22; `f16875b` se integró por avance directo y se subió a `origin/main`, sin conflictos.
- [ ] Comprobar que otro clon o sesión Cloud recibe la misma revisión y lee AGENTS.md.
- [ ] Verificar herramientas y configuración publicada Cloud cuando se use ese entorno; editar documentación no modifica el servicio.
- [ ] Validar el proyecto actualizado en el equipo donde se vaya a ejecutar. El fallo AAPT2 histórico se diagnostica solo si reaparece.

Backend, autenticación real, Google Play y sincronización externa permanecen fuera del MVP. No añadir CI, firma o nuevos frameworks como condiciones de cierre no acordadas.

## Tarjetas para Trello

Copiar cada título como tarjeta y su contenido como descripción/checklist. Listas sugeridas: **Por hacer → En curso → Por validar → Hecho**. T01 está completada; las tarjetas T02–T17 tienen trabajo pendiente y T07 y T14 aprovechan código parcial existente. Asignar un responsable al tomar cada tarjeta. P0 indica una dependencia que desbloquea otras tareas; P1 también es obligatoria para la entrega. No se han creado tarjetas en Trello.

Las estimaciones son horas de trabajo concentrado, sin conocer la velocidad ni disponibilidad del equipo. Total orientativo: **56–85 horas**, más margen para imprevistos. El calendario requiere comenzar esta semana; no presupone que todo quepa en cinco jornadas de una sola persona. Si la capacidad no alcanza, revisar fecha o distribución de trabajo sin declarar vistas incompletas como terminadas.

### T01 — Ampliar los mockups a doce vistas

- **Prioridad:** P0 · **Objetivo:** 21/10 · **Estimación:** 2–3 h · **Depende de:** nada.
- **Estado al 22/10:** completada. Plano ampliado y mapa de navegación revisados y enlazados en la sección 2.2 de la especificación; el propietario autorizó su incorporación mediante commit. Este cierre corresponde al diseño, no a la implementación o validación Android.
- [x] Incorporar las vistas 11 (inicio de sesión) y 12 (registro), con campos, enlaces, invitado y aviso demostrativo de la sección 2.3.
- [x] Mantener las diez vistas originales y definir estados de error, vacío, carga y controles de regreso/cierre.
- [x] Alinear colores, tipografía, espaciados y botones entre acceso y panel; guardar la referencia visual en `docs/mockups/` y enlazarla desde la especificación.
- **Aceptación:** referencia visual de las doce vistas y recorrido completo identificable, revisados conjuntamente y aprobados por el propietario.

### T02 — Implementar los modelos del panel

- **Prioridad:** P0 · **Objetivo:** 21/10 · **Estimación:** 3–4 h · **Depende de:** nada.
- [ ] Implementar contratos de la sección 7 y validación de integridad de la sección 8.
- [ ] Probar IDs, referencias, duplicados temporales y avances fuera de rango.
- **Aceptación:** modelos independientes de Compose; no se introducen relaciones proyecto–autor–publicación ajenas al contrato.

### T03 — Crear datos JSON y Repository local

- **Prioridad:** P0 · **Objetivo:** 22/10 · **Estimación:** 4–6 h · **Depende de:** T02.
- [ ] Generar entidades y cobertura declarada para tres líneas y 2022–2024; reproducir los conteos de la muestra 2024.
- [ ] Añadir lector y Repository; incorporar solo las dependencias necesarias en el catálogo y comprobar compilación.
- [ ] Diferenciar carga, éxito, vacío, falta de cobertura y error; probar archivo inválido y funcionamiento offline.
- **Aceptación:** entidades consultables por línea/año; ningún conteo manual independiente sustituye los datos.

### T04 — Implementar cálculos y sus pruebas

- **Prioridad:** P0 · **Objetivo:** 23/10 · **Estimación:** 4–6 h · **Depende de:** T03.
- [ ] Calcular las cinco métricas, históricos y variaciones conforme a la sección 8.
- [ ] Cubrir la batería de reglas de negocio de la sección 14: ausencias, base cero, año anterior exacto, puntos porcentuales, deduplicación, cobertura e impacto.
- **Aceptación:** 2024 reproduce 78,0 / 54,0 / 96,0 puntos de impacto y los conteos de la muestra; listas y valores coinciden, sin ceros inventados ni infinitos.

### T05 — Implementar estado MVVM del panel

- **Prioridad:** P0 · **Objetivo:** 23/10 · **Estimación:** 4–6 h · **Depende de:** T03, T04.
- [ ] Crear DashboardViewModel y estados/eventos necesarios para detalles; conectar Repository.
- [ ] Guardar línea, año y métrica con SavedStateHandle; iniciar con 2024 y proyectos activos.
- [ ] Probar cambios de selección, carga, vacío, error y restauración; mantener cálculos fuera de composables.
- **Aceptación:** las vistas reciben estado observable y emiten eventos; recrear el estado recupera los filtros.

### T06 — Completar navegación y regreso

- **Prioridad:** P0 · **Objetivo:** 26/10 · **Estimación:** 3–5 h · **Depende de:** T05.
- **Pendiente añadido el 22/10 por solicitud del usuario:** organizar las rutas y su conexión con las pantallas en `com.duoc.madertimber.ui.navigation`. Esta subtarea forma parte de T06; no está implementada.
- [ ] Crear `Rutas.kt` para definir los destinos y argumentos del flujo.
- [ ] Crear `AppNavigation.kt` para conectar destinos y pantallas; trasladar allí la selección y navegación actualmente en `MainActivity`, manteniendo la inicialización de la actividad y el tema en su lugar.
- [ ] Integrar el acceso existente con rutas de dashboard, línea, indicador y relacionados de la sección 11; habilitar apertura/cierre de fichas.
- [ ] Transmitir y validar IDs, año y tipo; ofrecer regreso ante argumentos inválidos.
- [ ] Conservar selección al volver y limpiar formularios al salir del acceso.
- **Aceptación:** rutas y pila de regreso preparadas; verificar el recorrido completo cuando T07–T14 estén integradas. No cerrar solo por tener destinos vacíos.

### T07 — Completar dashboard y comparación (vista 1)

- **Prioridad:** P1 · **Objetivo:** 26/10 · **Estimación:** 4–5 h · **Depende de:** T01, T05, T06.
- [ ] Sustituir constantes por datos del ViewModel en las cuatro tarjetas.
- [ ] Añadir selector de las cinco métricas y barras de las tres líneas con año/unidad, resaltado y descripción accesible.
- [ ] Añadir Ver línea y acceso al selector de filtros; representar valores ausentes.
- **Aceptación:** cambiar la selección actualiza tarjetas y comparativo de forma coherente, conservando las otras líneas visibles.

### T08 — Implementar selección de filtros (vista 2)

- **Prioridad:** P1 · **Objetivo:** 26/10 · **Estimación:** 2–3 h · **Depende de:** T05, T07.
- [ ] Implementar selector/modal con tres líneas, años 2022–2024 y selección actual.
- [ ] Aplicar cambios mediante eventos; cancelar/cerrar sin cambiar la selección aplicada.
- **Aceptación:** filtros efectivos en dashboard y detalles; regresar o recrear conserva línea, año y métrica.

### T09 — Crear gráfico histórico reutilizable

- **Prioridad:** P0 · **Objetivo:** 26/10 · **Estimación:** 3–5 h · **Depende de:** T01, T04.
- [ ] Mostrar años hasta el seleccionado, valores/unidades, resaltado y resumen accesible.
- [ ] Representar huecos sin interpolar, un valor como punto y ausencia total con mensaje.
- **Aceptación:** gráfico renderiza valores calculados y cubre los casos de la sección 10 sin cálculo de negocio en la vista.

### T10 — Implementar detalle de línea (vista 3)

- **Prioridad:** P1 · **Objetivo:** 27/10 · **Estimación:** 3–4 h · **Depende de:** T05, T06, T09.
- [ ] Mostrar nombre, descripción, año y cinco indicadores, incluido avance promedio.
- [ ] Integrar histórico con proyectos activos por defecto y selección de indicador.
- [ ] Abrir detalle de indicador y contenidos relacionados conservando línea/año.
- **Aceptación:** funcionan las tres líneas y los tres años, con sus estados sin datos.

### T11 — Implementar detalle de indicador (vista 4)

- **Prioridad:** P1 · **Objetivo:** 27/10 · **Estimación:** 3–5 h · **Depende de:** T05, T06, T09.
- [ ] Mostrar valor del año seleccionado, unidad, variación, histórico e interpretación sencilla.
- [ ] Incluir etiqueta demostrativa y acceso a los contenidos correspondientes al tipo.
- **Aceptación:** funcionan las cinco métricas y mensajes especiales de la sección 8; abrir un detalle nunca cambia al año más reciente.

### T12 — Implementar las tres listas relacionadas (vistas 5, 7 y 9)

- **Prioridad:** P1 · **Objetivo:** 28/10 · **Estimación:** 4–6 h · **Depende de:** T03, T05, T06, T10, T11.
- [ ] Mostrar proyectos activos, investigadores vinculados y publicaciones de línea/año.
- [ ] Respetar las pestañas aplicables al indicador de origen según la sección 9.4; desde línea, ofrecer las tres.
- [ ] Mostrar totales, estados vacíos y abrir fichas por ID.
- **Aceptación:** todos los registros son accesibles mediante desplazamiento; los tres ejemplos visibles del mockup no limitan la lista real. Conteos coinciden con indicadores.

### T13 — Implementar las tres fichas (vistas 6, 8 y 10)

- **Prioridad:** P1 · **Objetivo:** 28/10 · **Estimación:** 3–5 h · **Depende de:** T12.
- [ ] Proyecto: título, línea/año, estado y avance del seguimiento seleccionado.
- [ ] Investigador: nombre, especialidad y vinculación de la selección.
- [ ] Publicación: título, tipo, año y línea; identificar registro ficticio sin inventar fuentes.
- **Aceptación:** abrir y cerrar cada ficha conserva pestaña, línea y año; IDs inválidos muestran una salida recuperable.

### T14 — Ajustar y validar login y registro (vistas 11 y 12)

- **Prioridad:** P1 · **Objetivo:** 28/10 · **Estimación:** 2–4 h · **Depende de:** T01, T06.
- [ ] Reutilizar AuthScreen, LoginViewModel, RegisterViewModel y AuthValidator existentes; alinear su apariencia con T01.
- [ ] Verificar campos vacíos, correo inválido, contraseña de registro mínima y confirmación, visibilidad, aviso demo y acceso invitado.
- [ ] Revisar teclado, desplazamiento, Atrás, cambio de formulario y limpieza de credenciales al salir.
- **Aceptación:** ambas vistas funcionan en Android y conservan MVVM; sin crear cuentas ni afirmar autenticación real. Las seis pruebas JVM existentes siguen pasando.

### T15 — Revisar diseño y accesibilidad de las doce vistas

- **Prioridad:** P1 · **Objetivo:** 29/10 · **Estimación:** 3–5 h · **Depende de:** T07–T14.
- [ ] Comparar cada vista con la referencia ampliada; corregir textos, jerarquía, paleta y botones.
- [ ] Revisar pantallas pequeñas/grandes, texto ampliado, contraste, desplazamiento, teclado y descripciones de controles/gráficos.
- **Aceptación:** doce vistas verificadas en Android con capturas; sin contenido esencial cortado ni información comunicada solo por color.

### T16 — Ejecutar pruebas integradas y corregir fallos

- **Prioridad:** P1 · **Objetivo:** 29/10 · **Estimación:** 6–8 h · **Depende de:** T06–T15.
- [ ] Ejecutar assembleDebug, testDebugUnitTest y lintDebug mediante Wrapper; registrar resultados nuevos y causas de fallos.
- [ ] Ejecutar casos UI de la sección 14 en dispositivo/emulador, además del acceso y las tres fichas.
- [ ] Verificar offline, filtros, navegación, recreación, argumentos inválidos, vacíos, errores, huecos históricos y coincidencia de listas/indicadores.
- **Aceptación:** fallos que impiden los criterios de la sección 12 corregidos y comprobados de nuevo; evidencia con fecha, dispositivo, casos y resultado. Un build exitoso solo no cierra esta tarjeta.

### T17 — Preparar y ensayar la entrega

- **Prioridad:** P1 · **Objetivo:** 30/10 · **Estimación:** 3–5 h · **Depende de:** T16.
- [ ] Preparar todos los entregables de la sección 15: proyecto compilable, datos, instrucciones, arquitectura/modelo, capturas, casos y video o demostración.
- [ ] Ensayar acceso → dashboard → filtros → línea → indicador → listados → fichas → regreso, incluyendo cambio de línea/año.
- [ ] Actualizar evidencia técnica y estado de tarjetas; comprobar ejecución del proyecto en el equipo de presentación.
- **Aceptación:** entrega reproducible, recorrido completo y evidencia de las doce vistas; cualquier limitación restante queda explícita. Sin publicación en Google Play como requisito.

### Hitos propuestos

| Fecha | Resultado esperado | Tarjetas |
| --- | --- | --- |
| 20–23 octubre | Diseño ampliado y base de modelos, datos, cálculos y estado | T01–T05 |
| 26 octubre | Navegación, dashboard, filtros e histórico reutilizable | T06–T09 |
| 27 octubre | Detalles de línea e indicador | T10–T11 |
| 28 octubre | Listas, fichas y acceso integrados; doce vistas disponibles | T12–T14 |
| 29 octubre | Revisión visual, pruebas y correcciones | T15–T16 |
| 30 octubre | Margen final, documentación y demostración | T17 |

El fin de semana no se cuenta como disponibilidad obligatoria. Si T01–T05 no están listas el 23, revisar el calendario antes de iniciar la semana de entrega. Favoritos, historial y persistencia opcional quedan fuera de estas tarjetas obligatorias.
