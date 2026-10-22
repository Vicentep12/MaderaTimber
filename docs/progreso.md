# Progreso de aprendizaje

Actualización: 2026-10-22. Registrar aquí evidencias del aprendizaje; el perfil y método están en [aprendizaje.md](aprendizaje.md), las tareas y próxima lección en [plan-implementacion.md](plan-implementacion.md), y la validación técnica en [estado-actual.md](estado-actual.md).

## Resultados comprobados

| Actividad | Evidencia | Estado |
| --- | --- | --- |
| Entorno y ejecución inicial | El usuario declaró Android Studio y dispositivo disponibles. Una captura de la tutoría anterior mostró Hello Android. | Ejecución inicial comprobada visualmente; no se repitió en esta revisión. |
| Lectura de Compose | Identificó setContent y la llamada a Greeting. | Lectura realizada; comprensión de componentes adicionales pendiente. |
| Interpolación | Explicó que $name inserta el valor de name y predijo el resultado al quitar $. | Comprensión comprobada por sus respuestas. |
| val y var | Explicó que val no permite reasignar y que var sí. Escribió val saludo y lo pasó a Text. | Código leído y respuesta correctos; ejecución de esa versión reportada por el usuario. |
| Cambio del saludo | El código local pasa Diego y usa val saludo; el usuario reportó ver el nombre cambiado al ejecutar. | Código comprobado por lectura; ejecución de esa versión no verificada aquí. |
| Ejercicio de enteros y multiplicación | Se retiró al aclarar que conoce fundamentos generales. | No realizado; no es requisito para continuar. |
| Dashboard y MVVM | El 2026-10-15 Codex implementó la vista estática por solicitud explícita. Falta evaluar la comprensión del usuario de Column, Card, estado, eventos y ViewModel. | Implementación asistida; aprendizaje y MVVM pendientes. |
| Primer modelo de T02 | El 2026-10-22 el usuario compartió en el chat una `data class LineaInvestigacion` bajo `com.duoc.madertimber.data.model`, con `id`, `nombre`, `descripcion` y `color` como `val String`. Se revisaron los espacios antes de `:` y el cuerpo vacío como detalles de estilo. | Estructura correcta por lectura del código compartido. El archivo aún no está presente en este checkout; no se verificaron compilación ni ejecución. |
| Identidad estable de las entidades | Ante un cambio de nombre de la línea, explicó que los proyectos seguirían conectados al usar un `id` estable. | Comprensión del propósito del identificador comprobada por su respuesta. |
| Valor anual de un indicador | Compartió `ValorIndicador` con `anio: Int` y `valor: Double`, ambas propiedades `val`. Se indicaron ajustes de formato en los paréntesis. | Estructura correcta por lectura del código compartido; sin verificación de archivo, compilación o ejecución en este checkout. |
| Opciones limitadas con enum | Compartió las cinco opciones de `TipoIndicador`. El mensaje incluía escapes, entidades HTML y asteriscos; se indicó eliminarlos si también estaban en el archivo. Explicó que una opción no declarada daría error porque el tipo solo permite las declaradas en el enum. | Comprensión de las opciones permitidas comprobada por su respuesta. No se verificó una versión del archivo sin caracteres de formato ni su compilación. |
| Indicador e histórico anual | Compartió `Indicador` con las siete propiedades del contrato, incluidas `tipo: TipoIndicador` y `valores: List<ValorIndicador>`. Preguntó por qué necesitaba una lista; tras revisar el ejemplo 2022→4, 2023→6 y 2024→8, identificó que seleccionar 2023 debe mostrar 6. | Estructura correcta por lectura del código compartido y asociación año–resultado comprobada por su respuesta. No se verificaron archivo, compilación ni ejecución. Próximo ejercicio: enum `EstadoProyecto`, antes de `SeguimientoProyecto` y `Proyecto`. |

## Cómo continuar el registro

Al cerrar cada lección, añadir su objetivo, código revisado, respuesta o ejercicio que demuestra comprensión, resultado de ejecución y dificultad pendiente. Distinguir ejecución reportada de comprobación visual y de comandos ejecutados. No registrar aquí el historial de movimientos de archivos ni repetir la especificación.

## Última revisión documental

Se consolidaron tareas/contexto en el plan operativo, se conservaron los requisitos y el recorrido pedagógico completos y se retiraron referencias desactualizadas. Esta revisión no constituye una nueva lección ni acredita ejecución Android.
