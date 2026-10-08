# Plan integrado CENAMAD / MaderaTimber

Actualización: 2026-10-08. **Prioridad: continuar el plan previamente acordado.** MaderaTimber es la aplicación Android del panel de indicadores de impacto CENAMAD. MVVM es un requisito explícito de la asignatura, todavía pendiente de implementar.

## Procedencia y límite de la integración

Acuerdos recuperados de los chats «Revisa el archivo Markdown» y «Planifica una sesión inicial de 1 h» del 2026-10-08. El usuario ubicó los originales en `E:\AnalistaProgramador\Desarrollo app moviles`: `AGENTS.md`, `APRENDIZAJE.md`, `PROGRESO.md` y `MVP_Panel_Indicadores_Impacto_CENAMAD/README.md`. Esa unidad Windows no está montada en este entorno Cloud. Los documentos de aprendizaje y MVP incorporados aquí son una síntesis de los acuerdos recuperados, no una copia íntegra de esos archivos. Queda pendiente contrastar los originales, especialmente indicadores, fórmulas y criterios de evaluación.

Esta raíz Git contiene el plan común; el [plan del MVP](MVP_Panel_Indicadores_Impacto_CENAMAD/PLAN_IMPLEMENTACION.md) enlaza aquí. [APRENDIZAJE.md](APRENDIZAJE.md) rige la tutoría, [PROGRESO.md](PROGRESO.md) registra lo aprendido, [estado actual](docs/estado-actual.md) registra verificaciones técnicas y [pendientes](docs/pendientes.md) mantiene tareas. Evitar copias divergentes del mismo plan.

## Orden de implementación

| Etapa | Trabajo y criterio de cierre | Estado |
| --- | --- | --- |
| 0. Continuidad | Reunir acuerdos, tutoría y especificación dentro de Git; contrastar originales Windows y compartir la misma revisión entre equipos. | Integración local preparada; originales y sincronización pendientes. |
| 1. Próxima lección | Pantalla Compose con título del panel, «Construcción sustentable», año 2024, tarjeta «Proyectos activos: 8» y etiqueta visible «Datos demostrativos». El usuario escribe el código; revisión y comprobación visual en Android. | Acordada; código pendiente. |
| 2. Model y datos locales | Modelos Kotlin y Repository con datos demostrativos. Precisar contratos y cálculos contra el README original antes de implementarlos. | Planificada. |
| 3. MVVM y filtros | `DashboardViewModel` publica estado observable; Compose muestra datos y envía eventos de selección de línea/año. El Repository entrega los datos; la lógica no se acumula en MainActivity. | Requisito confirmado; implementación pendiente. |
| 4. Indicadores y detalle | Proyectos, publicaciones, investigadores e impacto estimado; comparación entre líneas y detalle, según especificación original recuperada. | Alcance recuperado; aceptación detallada pendiente. |
| 5. Validación | Pruebas de comportamiento del filtrado y cálculos definidos; build, pruebas JVM y Lint; ejecución visual e instrumentada en Android. Documentar dispositivo/API y resultados. | Build base Cloud acreditado; funcionalidad MVP pendiente. |

El año 2024 y el valor 8 son ejemplos pedagógicos, no estadísticas reales ni límites técnicos. Se conserva la actividad confirmada; la sugerencia anterior de usar 2026 no fue una instrucción para sustituirla. No añadir backend, autenticación o nuevos frameworks sin necesidad acordada.

## Retomar desde cualquier equipo

1. Abrir la raíz Git actualizada, revisar rama y cambios locales.
2. Leer [AGENTS.md](AGENTS.md), este plan, [APRENDIZAJE.md](APRENDIZAJE.md), [PROGRESO.md](PROGRESO.md) y [contexto](docs/contexto.md).
3. Retomar la etapa pendiente, una lección a la vez; registrar lo que el usuario comprendió y lo que se comprobó.
4. Compartir código y documentos juntos mediante Git con autorización. Un archivo local, un chat compartido o publicar el entorno Cloud no sincronizan los cambios al remoto.

Desde teléfono/tablet se puede continuar la tutoría y revisar documentos en la interfaz disponible de Codex Cloud. Compilar requiere el entorno Android preparado; ejecutar la app requiere Android compatible. Esta continuidad no convierte la app nativa en web o iOS. Comandos por plataforma: [README](README.md); arranque Cloud: [directrices](docs/directrices-nube.md).
