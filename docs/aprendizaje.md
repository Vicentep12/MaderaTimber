# Aprendizaje aplicado de Kotlin y Android

Objetivo: aprender a comprender, desarrollar y depurar aplicaciones por cuenta propia construyendo MaderaTimber. [AGENTS.md](../AGENTS.md) define las instrucciones del tutor; [progreso.md](progreso.md) registra el aprendizaje comprobado y [plan-implementacion.md](plan-implementacion.md) la próxima actividad.

## Perfil y adaptación

Perfil declarado el 2026-10-08: segundo año de Analista Programador en Duoc UC y fundamentos generales de programación conocidos. Android Studio y dispositivo disponibles según el usuario. El dominio de Kotlin se comprueba mediante ejercicios, no se deduce de la malla académica.

Introducir sintaxis y particularidades de Kotlin en tareas del panel. Las etapas básicas de la tabla son referencia o refuerzo: no reiniciar aritmética y variables si el usuario ya comprende esos fundamentos. Al retomar, consultar el progreso y preguntar únicamente lo que falte o haya cambiado.

Priorizar la práctica visual acordada; después modelos/datos y ViewModel antes de agregar filtros interactivos. La arquitectura requerida y el contrato completo están en [especificacion.md](especificacion.md).

## Dinámica de una lección

1. Presentar un objetivo pequeño, su utilidad y los archivos involucrados.
2. Explicar el concepto con un ejemplo breve.
3. Dar un ejercicio para que el usuario escriba el código.
4. Indicar resultado esperado y cómo comprobarlo.
5. Esperar el resultado y ayudar a diagnosticar dificultades con pistas.
6. Pedir una explicación o variación del ejercicio para comprobar comprensión.
7. Registrar resultados y dificultades en progreso.md; actualizar el plan cuando cierre una etapa.

Sesión orientativa de una hora: 10 minutos de explicación, 35 de práctica, 10 de revisión y 5 de registro. Adaptar al tiempo y las dificultades. Una etapa puede requerir varias lecciones.

No entregar la solución completa de entrada. Una petición explícita de implementación puede autorizar trabajo directo; en tutoría se conserva el código escrito por el usuario.

## Recorrido completo de referencia

| Tema | Conceptos | Ejercicio orientativo | Evidencia para avanzar |
|---|---|---|---|
| 0. Entorno y proyecto | Estructura Android, archivos fuente, recursos y Gradle | Identificar las partes de MaderaTimber sin editarlas; ejecutar un proyecto de práctica cuando el entorno esté preparado | Explicar dónde está el código y distinguir compilación de ejecución |
| 1. Kotlin básico | val, var, tipos, operadores y cadenas | Calcular un subtotal y mostrar un mensaje | Cambiar entradas y explicar el resultado |
| 2. Lógica y funciones | if, when, bucles, parámetros y retornos | Validar cantidades y calcular un total mediante funciones | Resolver casos normales y límites sin copiar la solución |
| 3. Modelos y null safety | Clases, data classes, enums y tipos anulables | Modelar un elemento de ejemplo con un dato opcional | Explicar el modelo y manejar la ausencia de datos sin fallos |
| 4. Colecciones | Listas, map, filter, búsquedas y agregaciones | Filtrar elementos y calcular un resumen | Verificar resultados con listas vacías y varios elementos |
| 5. Interfaz Android | Componentes, distribución, recursos y accesibilidad | Crear una pantalla pequeña en un proyecto de práctica | Explicar cómo se construye y comprobar que el contenido es legible |
| 6. Estado y eventos | Entrada de usuario, estado observable en ViewModel y actualización de la interfaz | Cambiar un filtro y actualizar una lista | Explicar qué cambia al pulsar o escribir y probar un resultado vacío |
| 7. Organización | Responsabilidades, ViewModel y lógica independiente de la UI | Separar un cálculo de la pantalla | Explicar dónde vive cada responsabilidad y comprobar el cálculo |
| 8. Datos y asincronía | Repository, JSON local, coroutines y Flow, introducidos por separado | Cargar datos de práctica y representar carga, éxito y error | Distinguir un error de carga de un conjunto vacío |
| 9. Navegación | Pantallas, argumentos, regreso y conservación de estado | Abrir un detalle y volver conservando un filtro | Probar el recorrido y explicar cómo se identifica el elemento |
| 10. Calidad | Depuración, pruebas unitarias, pruebas de interfaz y accesibilidad | Reproducir y corregir un fallo pequeño con una comprobación útil | Explicar la causa y demostrar que el caso funciona |
| 11. Autonomía | Descomposición, consulta de documentación y validación | Proponer y desarrollar una función pequeña autorizada | Dividirla en pasos, justificar decisiones y comprobarla con ayuda mínima |

La numeración identifica temas; el orden de actividades está en el plan operativo. La tabla no impone empezar desde cero ni reemplaza los doce pasos y las cinco iteraciones del plan técnico original. Incorporar dependencias cuando la actividad las necesite y verificar su compilación.

La práctica inicial de UI puede usar datos fijos. Cuando se introduzcan selección y carga de datos, ubicar el estado de filtros y su coordinación en DashboardViewModel y los datos/cálculos en Repository. El estado puramente visual de un componente puede permanecer local.

## Autonomía

Al terminar cada etapa, pedir un pequeño cambio que el usuario resuelva sin copiar. Antes de una funcionalidad propia, debe poder explicar el problema, dividirlo en pasos, localizar el código, consultar documentación y verificar su resultado. No marcar una etapa como comprendida por haber mostrado una explicación o compilado el ejemplo.
