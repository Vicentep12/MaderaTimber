# Plan de implementación y continuidad

Actualización: 2026-10-08. Esta es la única lista de etapas, tareas abiertas y próxima actividad. Los requisitos y los doce pasos técnicos completos están en [especificacion.md](especificacion.md); el recorrido pedagógico completo, en [aprendizaje.md](aprendizaje.md).

## Retomar una sesión

1. Trabajar desde la raíz Git y leer [AGENTS.md](../AGENTS.md).
2. Revisar [progreso.md](progreso.md), [estado-actual.md](estado-actual.md) y las etapas siguientes. Comprobar rama y cambios locales antes de editar.
3. Consultar las secciones relevantes de la especificación y adaptar la lección al aprendizaje comprobado.
4. Al cerrar, actualizar el progreso del usuario, la evidencia técnica y el estado de la etapa afectada.
5. Para continuar en otro dispositivo, guardar y sincronizar los cambios mediante Git con autorización. Instalación y comandos: [README](../README.md).

## Próxima actividad acordada

Crear la base visual del dashboard con título del panel, “Construcción sustentable”, año 2024, tarjeta “Proyectos activos: 8” y etiqueta “Datos demostrativos”. El usuario escribe el código; el tutor introduce composables, Column, Text, Card y Modifier según se necesiten.

Cierre de la lección: código revisado, resultado visual comprobado en Android y comprensión registrada. Es un ejercicio estático inicial; los filtros interactivos se incorporan después con ViewModel y Repository. No acredita el dashboard completo ni MVVM.

## Etapas y tareas

La numeración conserva los doce pasos de la sección 11 de la especificación. Su detalle, fórmulas y aceptación se mantienen allí; esta tabla registra la ejecución.

| Paso | Tarea | Estado y criterio de cierre |
| --- | --- | --- |
| 1. Alcance | Confirmar flujo, líneas, indicadores, años y aceptación. | Definido en la especificación. |
| 2. Base Android | Crear/configurar proyecto y ejecutar la pantalla inicial. | Proyecto existente; ejecución inicial registrada en tutoría. No recrearlo. |
| 3. Dependencias | Completar navegación, ViewModel, coroutines y lectura JSON según cada incremento. | Parcial: Compose/Material 3 y pruebas de plantilla. Cada cambio de Gradle debe compilar. |
| 4. Modelos | Implementar contratos de la sección 7 e integridad de la sección 8. | Pendiente. IDs/referencias únicos, relaciones temporales y rangos válidos. |
| 5. Fuente local | JSON, lector y Repository; calcular indicadores desde entidades. | Pendiente. Datos coherentes, funcionamiento offline y carga/éxito/vacío/error diferenciados. |
| 6. Navegación | Definir rutas y argumentos, montar pantallas y validar regreso. | Pendiente. Definir rutas al preparar el flujo; completar conservación de filtros después del paso 7. |
| 7. ViewModel | Estado observable, eventos y coordinación de datos/filtros. | Pendiente. Incorporarlo antes de filtros interactivos; restaurar selección con SavedStateHandle. |
| 8. Dashboard | Completar selectores, cuatro tarjetas, comparativo y acceso al detalle. | Pendiente. La práctica visual inicial es el primer incremento; el cierre cumple la sección 9.1. |
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

- [ ] Guardar y sincronizar la reorganización documental local. No se realizó commit ni push de estas ediciones.
- [ ] Comprobar que otro clon o sesión Cloud recibe la misma revisión y lee AGENTS.md.
- [ ] Verificar herramientas y configuración publicada Cloud cuando se use ese entorno; editar documentación no modifica el servicio.
- [ ] Validar el proyecto actualizado en el equipo donde se vaya a ejecutar. El fallo AAPT2 histórico se diagnostica solo si reaparece.

Backend, autenticación, Google Play y sincronización externa permanecen fuera del MVP. No añadir CI, firma o nuevos frameworks como condiciones de cierre no acordadas.
