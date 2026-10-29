# Plan de implementación y continuidad

Actualización: 2026-10-29. Esta es la única lista de etapas, tareas abiertas y próxima actividad. Los requisitos y los doce pasos técnicos completos están en [especificacion.md](especificacion.md); el recorrido pedagógico completo, en [aprendizaje.md](aprendizaje.md).

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

La pauta de la asignatura compartida el 2026-10-28 fija la entrega para el **jueves 29 de octubre de 2026**. Exige formulario validado, imagen, animación, modal, Room, navegación, un recurso nativo, uso continuado de Git y Trello, MVVM y proyecto compilable. La ruta del repositorio entregada por cada integrante debe coincidir con la del Excel Oficial. Mantener el objetivo previo de las doce vistas/estados del mockup, pero revisar de inmediato la distribución del trabajo: las tareas abiertas y las estimaciones no caben de forma realista en la jornada restante de una sola persona. No presentar tareas incompletas como terminadas.

T01–T12 están versionadas en `main`; T12 quedó en `a1e5380` con listas relacionadas, totales, vacíos y acceso por ID. T13 está implementada en el árbol local: fichas de proyecto, investigador y publicación con datos de la selección; `assembleDebug` pasó. Próxima actividad: T14, ajustar y validar el acceso; coordinar de inmediato T18–T20, que incluyen Room, selector nativo y evidencia de Git/Trello. Las tarjetas T01–T20 desglosan las etapas existentes y los requisitos nuevos de la pauta, sin reemplazar los doce pasos técnicos ni las cinco iteraciones de la especificación. Mantener acceso demostrativo y datos locales; autenticación real no está incluida.

## Continuidad de tutoría


El público principal confirmado el 2026-10-15 son estudiantes. Al preparar las pantallas y sus textos, aplicar las pautas y la referencia de doce vistas de las secciones 2.1–2.3 de [especificacion.md](especificacion.md). Se mantienen el plan técnico, la muestra 2022–2024 y la actividad de tutoría siguiente.

Por petición explícita de implementación, Codex creó la primera vista estática: título, “Construcción sustentable”, año 2024, cuatro tarjetas, etiqueta “Datos demostrativos” y comparación de proyectos activos entre las tres líneas. Valores fijos de la muestra del plan; sin filtros interactivos ni navegación.

Tutoría retomada el 2026-10-22 por petición del usuario con T02: él escribe el código y Codex guía y revisa un ejercicio a la vez. Se revisaron en el chat `LineaInvestigacion`, `ValorIndicador`, las opciones de `TipoIndicador` e `Indicador`; se comprobó la comprensión del ID estable, de las opciones limitadas del enum y de la asociación entre año y resultado en la lista de valores. En el checkout del 2026-10-28 se comprobaron por lectura esos cuatro modelos y también `EstadoProyecto`, `SeguimientoProyecto` y `Proyecto`; `TipoIndicador.kt` contiene las cinco opciones sin los caracteres de formato del mensaje. La presencia de los otros tres archivos no acredita por sí sola comprensión del usuario. El usuario escribió `VinculacionInvestigador` y, tras una corrección guiada, identificó como duplicado repetir el mismo par `(lineaId, anio)`. También escribió `Investigador` y reconoció que tres pares distintos de línea/año requieren tres elementos en `vinculaciones`. Estos modelos y `Publicacion` están ahora en `main`; el usuario explicó que una publicación de 2023 no aparece con el filtro 2024. Escribió y corrigió `tieneIdsDuplicados` para líneas; explicó correctamente los casos de IDs distintos, repetidos y lista vacía. Creó y ejecutó con éxito tres pruebas JUnit para esos casos. `tieneProyectosConLineaInexistente` y su primer test de referencia ausente se añadieron a partir del código entregado por Codex por solicitud expresa; la prueba pasó. A solicitud expresa de implementación, Codex completó las demás reglas y pruebas de T02. El cierre técnico no acredita comprensión del código nuevo; próxima lección: distinguir un año cubierto sin entidades de un año sin cobertura en T03. La revisión pedagógica de DashboardScreen, Column, Card, Modifier y funciones reutilizables sigue pendiente. T05 conectó ViewModel y Repository; T06 conectó la navegación en código; T07–T08 completaron el dashboard y el filtro modal. La revisión visual integral de las doce vistas continúa pendiente.

## Etapas y tareas

La numeración conserva los doce pasos de la sección 11 de la especificación. Su detalle, fórmulas y aceptación se mantienen allí; esta tabla registra la ejecución.

| Paso | Tarea | Estado y criterio de cierre |
| --- | --- | --- |
| 1. Alcance | Confirmar flujo, líneas, indicadores, años y aceptación. | Definido en la especificación. |
| 2. Base Android | Crear/configurar proyecto y ejecutar la pantalla inicial. | Proyecto existente; ejecución inicial registrada en tutoría. No recrearlo. |
| 3. Dependencias | Completar navegación, ViewModel, coroutines y lectura JSON según cada incremento. | Compose/Material 3, Lifecycle ViewModel, coroutines, Gson y Navigation Compose configurados; Room pendiente para T18. Cada cambio de Gradle debe compilar. |
| 4. Modelos | Implementar contratos de la sección 7 e integridad de la sección 8. | Completado y registrado en el commit local `0dffc3d`; compilación y pruebas JVM correctas. |
| 5. Fuente local | JSON, lector y Repository; calcular indicadores desde entidades. | Fuente JSON, cobertura, lectura y consultas en T03; cinco indicadores, históricos y variaciones en T04. La vista del panel consume los resultados por medio del ViewModel desde T05. |
| 6. Navegación | Definir rutas y argumentos, montar pantallas y validar regreso. | T06 versionada; su prueba instrumentada de regreso conservando año pasó en emulador API 37. El recorrido completo se revisará al integrar T08–T14. |
| 7. ViewModel | Estado observable, eventos y coordinación de datos/filtros. | Acceso y dashboard implementados; DashboardViewModel guarda selección en SavedStateHandle y coordina datos, comparación y errores. ViewModels de detalles pendientes al integrar esas vistas. |
| 8. Dashboard | Completar selectores, cuatro tarjetas, comparativo y acceso al detalle. | T07–T08 implementadas: cuatro tarjetas, cinco métricas, barras accesibles, imagen, animación, filtros rápidos y modal; comprobadas en emulador. |
| 9. Detalle de línea | Cinco indicadores, histórico y entidades relacionadas. | T10 implementada: selector y navegación comprobados en Android; falta la revisión visual integral de T15. |
| 10. Detalle de indicador | Valor seleccionado, variación, histórico e interpretación. | T11 implementada en código; compilación correcta. Falta revisión visual completa en T15. |
| 11. Diseño y accesibilidad | Contraste, legibilidad, descripciones y pantallas pequeñas/grandes. | Pendiente para el producto. Validar durante cada pantalla y en la revisión final. |
| 12. Persistencia Room | Última línea, año y métrica entre sesiones; favoritos o historial opcionales. | Obligatoria para la entrega; T18. No sustituye SavedStateHandle para restaurar el estado durante el flujo. |

Los contenidos relacionados incluyen las pestañas según el indicador de origen y fichas breves, aunque no tengan un paso numerado independiente. T12–T13 los conectaron con datos del modelo en la pantalla común; las maquetas antiguas de investigadores siguen estáticas y fuera del flujo. La revisión visual completa permanece en T15.

## Validación y entrega

- [ ] Ejecutar las pruebas de comportamiento de la sección 14: cálculo, filtros, integridad, ausencias, restauración y navegación.
- [ ] Comprobar build, pruebas JVM y Lint; con Android disponible, pruebas UI y revisión visual.
- [ ] Verificar todos los criterios de la sección 12 y realizar el recorrido de demostración.
- [ ] Preparar los entregables completos de la sección 15: código/datos, instrucciones, arquitectura/modelo, capturas, casos y video o demostración.
- [ ] Comprobar en dispositivo imagen, animación, modal y selector nativo de archivos; verificar recuperación de la selección guardada en Room tras reiniciar.
- [ ] Revisar historial Git y tarjetas Trello con responsables y estados; cotejar la ruta de repositorio con el Excel Oficial antes de que cada integrante la entregue.

Organizar los incrementos según las cinco iteraciones de la sección 13. No cambiar requisitos ni dar una etapa por completada solo porque compiló la plantilla.

## Continuidad y entorno pendientes

- [x] Sincronizar con el remoto el commit del mockup, el mapa y su documentación. El propietario autorizó pasarlo todo a `main` el 2026-10-22; `f16875b` se integró por avance directo y se subió a `origin/main`, sin conflictos.
- [ ] Comprobar que otro clon o sesión Cloud recibe la misma revisión y lee AGENTS.md.
- [ ] Verificar herramientas y configuración publicada Cloud cuando se use ese entorno; editar documentación no modifica el servicio.
- [ ] Validar el proyecto actualizado en el equipo donde se vaya a ejecutar. El fallo AAPT2 histórico se diagnostica solo si reaparece.

Backend, autenticación real, Google Play y sincronización externa permanecen fuera del MVP. No añadir CI, firma o nuevos frameworks como condiciones de cierre no acordadas.

## Tarjetas para Trello

Copiar cada título como tarjeta y su contenido como descripción/checklist. Listas sugeridas: **Por hacer → En curso → Por validar → Hecho**. T01–T12 están versionadas; T13 está implementada en el árbol local y pendiente de commit. T14–T20 conservan trabajo pendiente. Asignar un responsable al tomar cada tarjeta y actualizarla durante el trabajo, con enlace al commit o evidencia verificable. P0 indica un requisito o dependencia indispensable; P1 también forma parte del alcance acordado. No se han creado tarjetas en Trello desde este plan.

Las estimaciones originales T01–T17 suman **56–85 horas** de trabajo concentrado; T18–T20 añaden trabajo no incluido en ese total. Son estimaciones, no evidencia de avance. Ante el plazo del 29/10, acordar responsables y capacidad del equipo hoy; registrar cualquier requisito que no llegue a verificarse y no declarar una entrega completa sin compilación y recorrido comprobados.

### T01 — Ampliar los mockups a doce vistas

- **Prioridad:** P0 · **Objetivo:** 21/10 · **Estimación:** 2–3 h · **Depende de:** nada.
- **Estado al 22/10:** completada. Plano ampliado y mapa de navegación revisados y enlazados en la sección 2.2 de la especificación; el propietario autorizó su incorporación mediante commit. Este cierre corresponde al diseño, no a la implementación o validación Android.
- [x] Incorporar las vistas 11 (inicio de sesión) y 12 (registro), con campos, enlaces, invitado y aviso demostrativo de la sección 2.3.
- [x] Mantener las diez vistas originales y definir estados de error, vacío, carga y controles de regreso/cierre.
- [x] Alinear colores, tipografía, espaciados y botones entre acceso y panel; guardar la referencia visual en `docs/mockups/` y enlazarla desde la especificación.
- **Aceptación:** referencia visual de las doce vistas y recorrido completo identificable, revisados conjuntamente y aprobados por el propietario.

### T02 — Implementar los modelos del panel

- **Prioridad:** P0 · **Objetivo:** 21/10 · **Estimación:** 3–4 h · **Depende de:** nada.
- **Estado al 28/10:** completada y registrada en el commit local `0dffc3d`. Los diez modelos están versionados en `main`; `validarIntegridadDatos` comprueba IDs únicos por entidad, referencias a líneas, seguimientos y vinculaciones únicos, avances finitos de 0 a 100, indicadores únicos por línea/tipo y valores únicos por año. Se conservan los dos controles previos. La clase JUnit tiene 13 pruebas sin fallos; `assembleDebug` y `testDebugUnitTest` pasaron.
- [x] Escribir los contratos de la sección 7 por estructura.
- [x] Validar la integridad de la sección 8 en los modelos; la cobertura temporal y carga JSON corresponden a T03.
- [x] Probar IDs, referencias, duplicados temporales y avances fuera de rango.
- **Aceptación:** modelos independientes de Compose; no se introducen relaciones proyecto–autor–publicación ajenas al contrato.

### T03 — Crear datos JSON y Repository local

- **Prioridad:** P0 · **Objetivo:** 22/10 · **Estimación:** 4–6 h · **Depende de:** T02.
- **Estado al 28/10:** completada en el commit `f6344f3`. El JSON contiene entidades y cobertura 2022–2024 para las tres líneas; el Repository consulta por línea/año y diferencia carga, datos, conjunto vacío, falta de cobertura y error. El lector aplica la validación de T02. Ocho pruebas JVM del Repository pasan, incluida lectura del archivo real; la UI aún no está conectada.
- [x] Generar entidades y cobertura declarada para tres líneas y 2022–2024; reproducir los conteos de la muestra 2024.
- [x] Añadir lector y Repository; incorporar solo las dependencias necesarias en el catálogo y comprobar compilación.
- [x] Diferenciar carga, éxito, vacío, falta de cobertura y error; probar archivo inválido y lectura offline.
- **Aceptación:** entidades consultables por línea/año; ningún conteo manual independiente sustituye los datos.

### T04 — Implementar cálculos y sus pruebas

- **Prioridad:** P0 · **Objetivo:** 23/10 · **Estimación:** 4–6 h · **Depende de:** T03.
- **Estado al 28/10:** completada en el commit `4d7009e`. El modelo calcula valores anuales e históricos sin redondear; el Repository expone los cinco indicadores por línea/año y los contenidos relacionados según el tipo. Once pruebas nuevas pasan, incluidos la muestra 2024, el año anterior exacto, ceros, ausencias, avance y metas superadas. La conexión UI se añadió en T05.
- [x] Calcular las cinco métricas, históricos y variaciones conforme a la sección 8.
- [x] Cubrir la batería de reglas de negocio de la sección 14: ausencias, base cero, año anterior exacto, puntos porcentuales, deduplicación, cobertura e impacto.
- **Aceptación:** 2024 reproduce 78,0 / 54,0 / 96,0 puntos de impacto y los conteos de la muestra; listas y valores coinciden, sin ceros inventados ni infinitos.

### T05 — Implementar estado MVVM del panel

- **Prioridad:** P0 · **Objetivo:** 23/10 · **Estimación:** 4–6 h · **Depende de:** T03, T04.
- **Estado al 29/10:** versionada en `4328a5a` e integrada con la navegación del equipo en `0fad2d0`. DashboardViewModel carga el Repository en segundo plano, expone estado observable y eventos, prepara datos para el panel y conserva línea/año/métrica en SavedStateHandle. La vista renderiza tarjetas, comparación y estados de carga, vacío, falta de cobertura y error; seis pruebas JVM del ViewModel pasaron. El contenido completo de detalles sigue en T10–T14.
- [x] Crear DashboardViewModel y estados/eventos necesarios para detalles; conectar Repository.
- [x] Guardar línea, año y métrica con SavedStateHandle; iniciar con 2024 y proyectos activos.
- [x] Probar cambios de selección, carga, vacío, error y restauración; mantener cálculos fuera de composables.
- **Aceptación:** las vistas reciben estado observable y emiten eventos; recrear el estado recupera los filtros.

### T06 — Completar navegación y regreso

- **Prioridad:** P0 · **Objetivo:** 26/10 · **Estimación:** 3–5 h · **Depende de:** T05.
- **Estado al 29/10:** versionada en `c372e32`. `MainActivity` conserva inicialización y tema; `ui/navigation` contiene rutas y flujo. Cuatro pruebas JVM nuevas, compilación y Lint pasaron; la prueba instrumentada de regreso conservando 2023 pasó después en un emulador API 37 durante T07. El contenido completo de las vistas sigue en T10–T14.
- [x] Crear `Rutas.kt` para definir los destinos y argumentos del flujo.
- [x] Crear `AppNavigation.kt` para conectar destinos y pantallas; trasladar allí la selección y navegación actualmente en `MainActivity`, manteniendo la inicialización de la actividad y el tema en su lugar.
- [x] Integrar el acceso existente con rutas de dashboard, línea, indicador y relacionados de la sección 11; habilitar apertura/cierre de fichas.
- [x] Transmitir y validar IDs, año y tipo; ofrecer regreso ante argumentos inválidos.
- [x] Conservar selección al volver y limpiar formularios al salir del acceso.
- **Aceptación:** rutas y pila de regreso preparadas; verificar el recorrido completo cuando T07–T14 estén integradas. No cerrar solo por tener destinos vacíos.

### T07 — Completar dashboard y comparación (vista 1)

- **Prioridad:** P1 · **Objetivo:** 26/10 · **Estimación:** 4–5 h · **Depende de:** T01, T05, T06.
- **Estado al 29/10:** versionada en `29f9459`. Imagen de investigación en madera incluida en recursos y descrita para accesibilidad; tarjetas con transición de valor y barras animadas; controles rápidos visibles/ocultables. Build, pruebas JVM, Lint y tres pruebas Android pasaron en emulador API 37. Capturas revisadas tras cambiar proyectos por publicaciones; las tres barras y la unidad se actualizaron.
- [x] Sustituir constantes por datos del ViewModel en las cuatro tarjetas.
- [x] Añadir selector de las cinco métricas y barras de las tres líneas con año/unidad, resaltado y descripción accesible.
- [x] Incorporar una imagen del contenido visible dentro de la app y una animación útil al actualizar una tarjeta o cambiar de selección; describir la imagen y verificar la animación en Android.
- [x] Añadir Ver línea y acceso al selector de filtros; representar valores ausentes. El selector actual es rápido/en línea; el modal con aplicar/cancelar corresponde a T08.
- **Aceptación:** cambiar la selección actualiza tarjetas y comparativo de forma coherente, conservando las otras líneas visibles; imagen y animación se comprueban en la app.

### T08 — Implementar selección de filtros (vista 2)

- **Prioridad:** P1 · **Objetivo:** 26/10 · **Estimación:** 2–3 h · **Depende de:** T05, T07.
- **Estado al 29/10:** versionada en `a0e6c76`. Compilación, 50 pruebas JVM, Lint y cinco pruebas Android correctas en emulador API 37. La prueba de interfaz verifica Aplicar, Cancelar, Atrás, regreso desde detalle y recreación de actividad; la prueba del ViewModel verifica restauración de línea, año y métrica.
- [x] Implementar selector/modal con tres líneas, años 2022–2024 y selección actual.
- [x] Aplicar cambios mediante eventos; cancelar/cerrar sin cambiar la selección aplicada.
- **Aceptación:** filtros efectivos en dashboard y detalles; regresar o recrear conserva línea, año y métrica.

### T09 — Crear gráfico histórico reutilizable

- **Prioridad:** P0 · **Objetivo:** 26/10 · **Estimación:** 3–5 h · **Depende de:** T01, T04.
- **Estado al 29/10:** versionada en `e02e57b`. `prepararHistorico` genera puntos anuales desde los indicadores calculados; `GraficoHistorico` dibuja segmentos solo entre valores contiguos. El detalle de indicador ya lo presenta. Compilación, Lint, 53 pruebas JVM y ocho pruebas Android correctas; captura revisada en emulador API 37 con valores 4–6–8.
- [x] Mostrar años hasta el seleccionado, valores/unidades, resaltado y resumen accesible.
- [x] Representar huecos sin interpolar, un valor como punto y ausencia total con mensaje.
- **Aceptación:** gráfico renderiza valores calculados y cubre los casos de la sección 10 sin cálculo de negocio en la vista.

### T10 — Implementar detalle de línea (vista 3)

- **Prioridad:** P1 · **Objetivo:** 27/10 · **Estimación:** 3–4 h · **Depende de:** T05, T06, T09.
- **Estado al 29/10:** versionada en `34e2554`. Compilación y Lint pasaron; tras retirar un caso redundante, 54 pruebas JVM pasaron. La primera ejecución Android tuvo nueve pruebas correctas y una falla porque la prueba pulsó un chip fuera de la pantalla. El registro compartido por el usuario mostró `Selected = false` y posición vertical fuera del dispositivo; la prueba corregida desplaza primero el gráfico y pasó aislada. Se conservan pruebas de pantalla sin cobertura y navegación.
- [x] Mostrar nombre, descripción, año y cinco indicadores, incluido avance promedio.
- [x] Integrar histórico con proyectos activos por defecto y selección de indicador.
- [x] Abrir detalle de indicador y contenidos relacionados conservando línea/año.
- **Aceptación:** funcionan las tres líneas y los tres años, con sus estados sin datos.

### T11 — Implementar detalle de indicador (vista 4)

- **Prioridad:** P1 · **Objetivo:** 27/10 · **Estimación:** 3–5 h · **Depende de:** T05, T06, T09.
- **Estado al 29/10:** versionada en `5679d20`. La vista usa la variación ya calculada por el modelo y presenta los mensajes especiales de ausencia y base cero. `:app:assembleDebug` pasó; por instrucción del usuario no se añadieron ni ejecutaron pruebas unitarias para T11. La comprobación visual integral sigue en T15.
- [x] Mostrar valor del año seleccionado, unidad, variación, histórico e interpretación sencilla.
- [x] Incluir etiqueta demostrativa y acceso a los contenidos correspondientes al tipo.
- **Aceptación:** funcionan las cinco métricas y mensajes especiales de la sección 8; abrir un detalle nunca cambia al año más reciente.

### T12 — Implementar las tres listas relacionadas (vistas 5, 7 y 9)

- **Prioridad:** P1 · **Objetivo:** 28/10 · **Estimación:** 4–6 h · **Depende de:** T03, T05, T06, T10, T11.
- **Estado al 29/10:** versionada en `a1e5380`. El flujo usa `RelacionadosScreen` con datos filtrados por el Repository; la maqueta fija previa de investigadores permanece fuera del flujo. La vista muestra contexto de línea/año, categorías aplicables, total, mensaje de vacío o falta de cobertura y tarjetas que abren fichas por ID. `:app:assembleDebug` pasó; por instrucción del usuario no se añadieron ni ejecutaron pruebas unitarias. La revisión visual integral sigue en T15.
- [x] Mostrar proyectos activos, investigadores vinculados y publicaciones de línea/año.
- [x] Respetar las pestañas aplicables al indicador de origen según la sección 9.4; desde línea, ofrecer las tres.
- [x] Mostrar totales, estados vacíos y abrir fichas por ID.
- **Aceptación:** todos los registros son accesibles mediante desplazamiento; los tres ejemplos visibles del mockup no limitan la lista real. Conteos coinciden con indicadores.

### T13 — Implementar las tres fichas (vistas 6, 8 y 10)

- **Prioridad:** P1 · **Objetivo:** 28/10 · **Estimación:** 3–5 h · **Depende de:** T12.
- **Estado al 29/10:** implementada en el árbol local, pendiente de commit. El flujo usa `FichaRelacionadaScreen` alimentada por `DatosRelacionados`; la maqueta fija previa de investigador permanece fuera del flujo. Proyecto, investigador y publicación muestran los campos de la selección y un regreso a la lista; la publicación se identifica como ficticia sin inventar fuentes. Los IDs inválidos usan el error recuperable de navegación. `:app:assembleDebug` pasó; por instrucción del usuario no se añadieron ni ejecutaron pruebas unitarias. La revisión visual integral sigue en T15.
- [x] Proyecto: título, línea/año, estado y avance del seguimiento seleccionado.
- [x] Investigador: nombre, especialidad y vinculación de la selección.
- [x] Publicación: título, tipo, año y línea; identificar registro ficticio sin inventar fuentes.
- **Aceptación:** abrir y cerrar cada ficha conserva pestaña, línea y año; IDs inválidos muestran una salida recuperable.

### T14 — Ajustar y validar login y registro (vistas 11 y 12)

- **Prioridad:** P1 · **Objetivo:** 28/10 · **Estimación:** 2–4 h · **Depende de:** T01, T06.
- [ ] Reutilizar AuthScreen, LoginViewModel, RegisterViewModel y AuthValidator existentes; alinear su apariencia con T01.
- [ ] Verificar campos vacíos, correo inválido, contraseña de registro mínima y confirmación, visibilidad, aviso demo y acceso invitado.
- [ ] Comprobar que el aviso demostrativo se muestra como modal y se puede cerrar con teclado, Atrás o su botón según corresponda.
- [ ] Revisar teclado, desplazamiento, Atrás, cambio de formulario y limpieza de credenciales al salir.
- **Aceptación:** ambas vistas funcionan en Android y conservan MVVM; sin crear cuentas ni afirmar autenticación real. Las seis pruebas JVM existentes siguen pasando.

### T15 — Revisar diseño y accesibilidad de las doce vistas

- **Prioridad:** P1 · **Objetivo:** 29/10 · **Estimación:** 3–5 h · **Depende de:** T07–T14.
- [ ] Comparar cada vista con la referencia ampliada; corregir textos, jerarquía, paleta y botones.
- [ ] Revisar nombres descriptivos, ortografía, comentarios útiles y código sin uso; confirmar que README sigue describiendo el proyecto real.
- [ ] Revisar pantallas pequeñas/grandes, texto ampliado, contraste, desplazamiento, teclado y descripciones de controles/gráficos.
- **Aceptación:** doce vistas verificadas en Android con capturas; sin contenido esencial cortado ni información comunicada solo por color.

### T16 — Ejecutar pruebas integradas y corregir fallos

- **Prioridad:** P1 · **Objetivo:** 29/10 · **Estimación:** 6–8 h · **Depende de:** T06–T15.
- [ ] Ejecutar assembleDebug, testDebugUnitTest y lintDebug mediante Wrapper; registrar resultados nuevos y causas de fallos.
- [ ] Ejecutar casos UI de la sección 14 en dispositivo/emulador, además del acceso y las tres fichas.
- [ ] Verificar offline, filtros, navegación, recreación, argumentos inválidos, vacíos, errores, huecos históricos y coincidencia de listas/indicadores.
- **Aceptación:** fallos que impiden los criterios de la sección 12 corregidos y comprobados de nuevo; evidencia con fecha, dispositivo, casos y resultado. Un build exitoso solo no cierra esta tarjeta.

### T17 — Preparar y ensayar la entrega

- **Prioridad:** P0 · **Objetivo:** 29/10 antes del envío · **Estimación original:** 3–5 h · **Depende de:** T16, T18–T20.
- [ ] Preparar todos los entregables de la sección 15: proyecto compilable, datos, instrucciones, arquitectura/modelo, capturas, casos y video o demostración.
- [ ] Ensayar acceso → dashboard → filtros → línea → indicador → listados → fichas → regreso, incluyendo cambio de línea/año.
- [ ] Actualizar evidencia técnica y estado de tarjetas; comprobar ejecución del proyecto en el equipo de presentación.
- [ ] Confirmar con el Excel Oficial la ruta exacta del repositorio y que cada integrante entregue la misma; ensayar también la explicación técnica de MVVM, Room y el recurso nativo.
- **Aceptación:** entrega reproducible, recorrido completo y evidencia de las doce vistas; cualquier limitación restante queda explícita. Sin publicación en Google Play como requisito.

### T18 — Guardar la selección con Room

- **Prioridad:** P0 · **Objetivo:** 29/10 · **Estimación adicional:** 4–7 h · **Depende de:** T05.
- [ ] Añadir Room en el catálogo y el módulo, sin cambiar versiones/SDK para ocultar fallos; compilar tras configurar las dependencias.
- [ ] Crear entidad, DAO, base de datos y Repository para línea, año y métrica seleccionados; recuperar la selección al iniciar y actualizarla al cambiar filtros.
- [ ] Mantener las reglas y validación fuera de composables; no guardar correo ni contraseñas del acceso demostrativo.
- [ ] Probar escritura, lectura, selección inválida y recuperación tras cerrar y abrir la app; comprobar que SavedStateHandle sigue cubriendo recreación y navegación.
- **Aceptación:** Room persiste una selección válida entre sesiones y la UI la refleja sin confundir persistencia con autenticación.

### T19 — Importar datos con el selector nativo de archivos

- **Prioridad:** P0 · **Objetivo:** 29/10 · **Estimación adicional:** 3–5 h · **Depende de:** T03, T05.
- [ ] Añadir una acción visible para abrir el selector nativo de documentos y elegir un JSON de datos sintéticos.
- [ ] Entregar el contenido al ViewModel/Repository para lectura y validación; comunicar éxito, cancelación, archivo inválido y error de lectura.
- [ ] Probar que cancelar o seleccionar un archivo inválido no reemplaza los datos válidos; confirmar el recorrido en Android.
- **Aceptación:** existe un uso comprobado de un recurso nativo y los datos importados respetan los contratos y reglas de la sección 8.

### T20 — Coordinar y evidenciar Git y Trello

- **Prioridad:** P0 · **Objetivo:** seguimiento desde 28/10 y cierre 29/10 · **Estimación adicional:** 1–2 h, distribuida durante el trabajo · **Depende de:** asignación del equipo.
- [ ] Crear/actualizar las tarjetas T01–T20 en Trello, asignar responsables y moverlas según evidencia real; mantener visibles los bloqueos.
- [ ] Integrar cambios por incrementos comprobables con mensajes de commit descriptivos y revisar el historial antes de entregar.
- [ ] Revisar diferencias, archivos rastreados y secretos antes de cada sincronización autorizada; no hacer push sin autorización.
- **Aceptación:** tablero e historial muestran trabajo continuado y coinciden con el estado real de la app.

### Hitos propuestos

| Fecha | Resultado esperado | Tarjetas |
| --- | --- | --- |
| 20–23 octubre | Diseño ampliado y base de modelos, datos, cálculos y estado | T01–T05 |
| 26 octubre | Navegación, dashboard, filtros e histórico reutilizable | T06–T09 |
| 27 octubre | Detalles de línea e indicador | T10–T11 |
| 28 octubre | Listas, fichas y acceso integrados; doce vistas disponibles | T12–T14 |
| 28 octubre | Asignar responsables y abrir seguimiento Git/Trello; iniciar Room y selector nativo en cuanto estén sus dependencias | T18–T20 |
| 29 octubre, antes del envío | Integrar imagen, animación, modal y Room; comprobar build, pruebas, Android, documentación y ruta del repositorio; ensayar entrega | T07, T14–T20 |

El hito del 29/10 es el plazo informado por la pauta, no una afirmación de que el trabajo alcance a completarse. Revisar capacidad y bloqueos con el equipo el 28/10; si algo no se implementa o no compila, registrar el límite con evidencia y comunicarlo de forma honesta. Favoritos e historial siguen fuera de las tarjetas obligatorias; Room para la selección no.
