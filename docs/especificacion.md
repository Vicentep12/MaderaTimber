# MVP: Panel móvil de indicadores e impacto para CENAMAD

## 1. Propósito

Este documento define paso a paso cómo construir un Producto Mínimo Viable (MVP) para Android que permita consultar indicadores sintéticos relacionados con las líneas de investigación de CENAMAD.

El MVP debe ser académico, funcional y demostrable. No debe conectarse a bases de datos productivas ni utilizar información confidencial. El conjunto de demostración será sintético y se identificará como tal.

## 2. Idea del producto

La aplicación permitirá seleccionar una línea de investigación y consultar sus principales indicadores de actividad e impacto.

El usuario podrá:

- Visualizar un resumen general de indicadores.
- Filtrar por línea de investigación y año.
- Comparar líneas de investigación.
- Consultar proyectos, investigadores y publicaciones asociados.
- Revisar la evolución de un indicador mediante gráficos simples.
- Ver una explicación breve de cada indicador.

## 3. Alcance del MVP

### Incluido

- Aplicación Android desarrollada con Kotlin.
- Interfaz con Jetpack Compose y Material 3.
- Datos sintéticos almacenados localmente.
- Dashboard con resumen de indicadores.
- Filtros por línea y año.
- Gráficos simples de barras y líneas.
- Detalle de una línea de investigación.
- Detalle de un indicador.
- Navegación hacia proyectos, investigadores y publicaciones.
- Estados de carga, vacío y error.
- Pruebas básicas y datos de demostración.

### Fuera del alcance

- Conexión a sistemas internos de CENAMAD.
- Inicio de sesión real.
- Dashboard institucional productivo.
- Datos personales no públicos.
- Sincronización con servicios externos reales.
- Publicación en Google Play.
- Predicciones o análisis estadístico avanzado.

## 4. Flujo principal

    Inicio
      -> Seleccionar línea de investigación
      -> Seleccionar año
      -> Ver resumen de indicadores
      -> Abrir gráfico de evolución
      -> Seleccionar un indicador
      -> Consultar proyectos, investigadores y publicaciones relacionados

## 5. Tecnologías recomendadas

- Kotlin.
- Android Studio.
- Jetpack Compose.
- Material 3.
- ViewModel.
- Kotlin Coroutines y Flow.
- Navigation Compose.
- Hilt para inyección de dependencias, opcional.
- Room para persistencia local, opcional.
- JUnit y Compose UI Test.

Para mantener el alcance controlado, se recomienda comenzar con datos JSON locales y migrar a Room solo si se necesita guardar filtros, favoritos o historial.

## 6. Arquitectura requerida

MVVM es requisito explícito de la asignatura. Usar separación por responsabilidades dentro del módulo existente `:app`:

    UI - Jetpack Compose
           |
    ViewModel - estado de pantalla y eventos
           |
    Repository - fuente de datos
           |
    JSON local o Room - datos sintéticos

Estructura sugerida:

    com.duoc.madertimber
    ├── data
    │   ├── local
    │   ├── model
    │   └── repository
    ├── domain
    │   └── usecase
    ├── ui
    │   ├── components
    │   ├── navigation
    │   ├── screens
    │   │   ├── dashboard
    │   │   ├── lineadetail
    │   │   └── indicadordetail
    │   └── theme
    └── MainActivity.kt

Esta es la estructura objetivo, todavía pendiente de implementar. La carpeta `domain/usecase` es opcional y puede añadirse si resulta útil separar lógica reutilizable; no se exige Clean Architecture ni módulos adicionales. La estructura actual se describe en [arquitectura.md](arquitectura.md).

## 7. Modelo de datos

### Línea de investigación

    data class LineaInvestigacion(
        val id: String,
        val nombre: String,
        val descripcion: String,
        val color: String
    )

### Indicador

    enum class TipoIndicador {
        PROYECTOS_ACTIVOS,
        PUBLICACIONES_GENERADAS,
        INVESTIGADORES_ASOCIADOS,
        AVANCE_PROMEDIO,
        IMPACTO_ESTIMADO
    }

    data class Indicador(
        val id: String,
        val lineaId: String,
        val nombre: String,
        val descripcion: String,
        val unidad: String,
        val tipo: TipoIndicador,
        val valores: List<ValorIndicador>
    )

### Valor anual

    data class ValorIndicador(
        val anio: Int,
        val valor: Double
    )

### Entidades relacionadas

    data class Proyecto(
        val id: String,
        val lineaId: String,
        val titulo: String,
        val seguimientos: List<SeguimientoProyecto>
    )

    enum class EstadoProyecto { ACTIVO, FINALIZADO, SUSPENDIDO }

    data class SeguimientoProyecto(
        val anio: Int,
        val estado: EstadoProyecto,
        val avancePorcentaje: Double
    )

    data class Investigador(
        val id: String,
        val nombre: String,
        val especialidad: String,
        val vinculaciones: List<VinculacionInvestigador>
    )

    data class VinculacionInvestigador(
        val lineaId: String,
        val anio: Int
    )

    data class Publicacion(
        val id: String,
        val lineaId: String,
        val titulo: String,
        val tipo: String,
        val anio: Int
    )

Usar IDs para relacionar entidades y evitar duplicar la información de una línea dentro de cada proyecto o publicación.

Cada proyecto tiene una sola línea y un seguimiento por año registrado. Su estado corresponde al cierre de ese año. Cada investigador puede pertenecer a varias líneas y años mediante vinculaciones únicas por `(lineaId, anio)`. Cada publicación pertenece a una línea y su `anio` corresponde al año de publicación.

Debe existir como máximo un indicador por `(lineaId, tipo)` y un valor por año dentro de cada indicador. `tipo` identifica la misma métrica entre líneas; `id` identifica el indicador de una línea concreta.

Los valores se calculan en el Repository a partir de las entidades locales; no se mantienen conteos manuales independientes en el JSON. `ValorIndicador` representa el resultado de esos cálculos. Si una métrica no puede calcularse, se omite el valor de ese año y la UI muestra “Sin datos”. Un cero significa un resultado calculado igual a cero.

## 8. Indicadores sintéticos sugeridos

Crear tres líneas ficticias con cinco indicadores definidos para el MVP:

| Tipo | Unidad | Regla por línea y año |
|---|---|---|
| `PROYECTOS_ACTIVOS` | proyectos | Contar IDs únicos de proyectos con seguimiento en estado `ACTIVO` en el año seleccionado. |
| `PUBLICACIONES_GENERADAS` | publicaciones | Contar IDs únicos de publicaciones del año seleccionado. |
| `INVESTIGADORES_ASOCIADOS` | investigadores | Contar IDs únicos de investigadores con vinculación a la línea en el año seleccionado. |
| `AVANCE_PROMEDIO` | % | Media aritmética del avance de los proyectos activos del año, con igual peso por proyecto. Sin proyectos activos, mostrar “Sin datos”. |
| `IMPACTO_ESTIMADO` | puntos (0–100) | Índice exclusivamente demostrativo calculado con la fórmula siguiente. |

El impacto estimado se calcula como `100 × (0,4 × min(P / 10, 1) + 0,4 × min(U / 20, 1) + 0,2 × min(I / 15, 1))`, donde `P` son proyectos activos, `U` publicaciones e `I` investigadores asociados. Las metas de 10, 20 y 15 y los pesos son convenciones ficticias fijas para todos los años y líneas. No representan una metodología oficial de CENAMAD ni una medición validada de impacto. Un valor mayor indica mayor actividad respecto de esas metas, sin demostrar calidad ni impacto real.

Calcular con precisión completa y redondear solo al mostrar: conteos enteros, avance e impacto con un decimal. Organizaciones colaboradoras y equipos o infraestructuras quedan para una versión futura, cuando se definan sus entidades y reglas.

Ejemplo:

| Línea | Año | Proyectos | Publicaciones | Investigadores | Impacto |
|---|---:|---:|---:|---:|---:|
| Construcción sustentable | 2024 | 8 | 15 | 12 | 78,0 |
| Biomateriales de madera | 2024 | 5 | 11 | 9 | 54,0 |
| Procesos industriales | 2024 | 10 | 18 | 15 | 96,0 |

Los valores deben identificarse como datos demostrativos.

### Integridad de los datos de demostración

- Preparar datos completos de 2022, 2023 y 2024 para las tres líneas. La ausencia de entidades en un conjunto completo equivale a un conteo cero; un archivo faltante o inválido es un error de carga, nunca un cero.
- Declarar en el conjunto JSON qué años están cubiertos por cada línea. Usar esa cobertura para distinguir un año sin datos de un año completo con cero entidades; no deducir cobertura de la existencia de proyectos o publicaciones.
- Generar primero las entidades y derivar después los indicadores. Para reproducir la tabla, las listas filtradas deben contener exactamente los conteos indicados.
- Validar IDs únicos por entidad, referencias a líneas existentes, seguimientos y vinculaciones sin duplicados, y avances entre 0 y 100.
- Un investigador cuenta una vez por línea y año, aunque participe en varios proyectos. No sumar conteos de líneas para obtener personas únicas entre todas ellas.
- Mantener escenarios de prueba separados para años sin datos, listas vacías y JSON inválido.

### Año seleccionado y variación

El año seleccionado rige las tarjetas, los detalles y los contenidos relacionados. Al iniciar se selecciona 2024; abrir un detalle nunca sustituye el año elegido por el más reciente. Si falta el valor solicitado, mostrar “Sin datos” y conservar el filtro.

La variación compara exclusivamente el año seleccionado `t` con `t - 1`, nunca con otro año anterior disponible. Para conteos e impacto, usar `(valor_t - valor_anterior) / valor_anterior × 100` cuando el valor anterior sea mayor que cero. Si falta cualquiera de los dos valores, mostrar “Sin comparación”; si el anterior es cero, mostrar “No calculable: base anterior cero”, incluso si ambos son cero. No mostrar infinito ni sustituirlo por 0 %.

Para avance promedio, mostrar la diferencia en puntos porcentuales: `avance_t - avance_anterior`. Por ejemplo, pasar de 40 % a 50 % equivale a +10 puntos porcentuales. Esta diferencia sí puede calcularse con un valor anterior de cero. Mostrar variaciones con un decimal y signo cuando corresponda.

## 9. Pantallas del MVP

### 9.1 Dashboard

Debe mostrar:

- Nombre de la aplicación.
- Selector de línea.
- Selector de año.
- Cuatro tarjetas principales: proyectos activos, publicaciones generadas, investigadores asociados e impacto estimado.
- Selector de métrica y gráfico de barras comparativo entre líneas para el año seleccionado.
- Botón para ver el detalle de la línea.

Componentes Compose sugeridos:

- Scaffold.
- TopAppBar.
- ExposedDropdownMenuBox.
- LazyVerticalGrid o LazyColumn.
- Card.
- FilterChip.

### 9.2 Detalle de línea

Debe mostrar:

- Nombre y descripción.
- Los cinco indicadores del año seleccionado, incluido el avance promedio.
- Evolución anual del indicador elegido, por defecto proyectos activos.
- Proyectos asociados.
- Investigadores asociados.
- Publicaciones relacionadas.

### 9.3 Detalle de indicador

Debe mostrar:

- Nombre del indicador.
- Valor del año seleccionado, con el año visible.
- Unidad de medida.
- Variación respecto del año calendario anterior según las reglas de la sección 8.
- Gráfico histórico.
- Explicación de interpretación.
- Nota de que el valor es sintético o demostrativo.

### 9.4 Contenidos relacionados

Puede ser una pantalla con pestañas:

- Proyectos.
- Investigadores.
- Publicaciones.

Cada elemento debe abrir una ficha breve y permitir volver al detalle de la línea.

Desde el detalle de línea se muestran proyectos activos, investigadores vinculados y publicaciones de esa línea y año. Desde un indicador se aplica además esta correspondencia:

| Indicador de origen | Contenidos relacionados |
|---|---|
| Proyectos activos o avance promedio | Proyectos activos usados en el cálculo. |
| Publicaciones generadas | Publicaciones contadas en el año. |
| Investigadores asociados | Investigadores contados en el año. |
| Impacto estimado | Las tres pestañas con las entidades usadas en sus componentes. |

Mostrar solo las pestañas aplicables al indicador de origen. La relación se deriva de `lineaId`, año y `tipo`; no implica que exista una relación directa entre un proyecto y una publicación o un investigador. Esa relación adicional queda fuera del modelo inicial. Las fichas pueden abrirse como diálogos, conservando el contexto al cerrarlas.

## 10. Gráficos

Implementar inicialmente solo:

1. Gráfico de barras para comparar líneas.
2. Gráfico de líneas para mostrar evolución anual.

El gráfico de barras compara la misma métrica en las tres líneas durante el año seleccionado. Su selector ofrece los cinco tipos de indicador y comienza con proyectos activos. Seleccionar una línea resalta su barra y actualiza las tarjetas, pero mantiene visibles las otras líneas. Cambiar el año actualiza tanto las barras como las tarjetas. No mezclar métricas ni unidades en un mismo gráfico; una línea sin valor se etiqueta “Sin datos”, sin representarla como cero.

El gráfico histórico muestra los años disponibles hasta el año seleccionado para una sola línea y métrica, y resalta el año seleccionado si tiene valor. Los años faltantes se muestran como interrupciones, sin interpolarlos ni reemplazarlos por cero. Si solo existe un valor, mostrar un punto con su etiqueta.

Cada gráfico debe incluir:

- Título.
- Unidad de medida.
- Etiquetas legibles.
- Resumen textual o descripción accesible.

Si no se utiliza una librería externa, se puede crear una visualización simplificada con Canvas de Jetpack Compose. La prioridad es la claridad.

## 11. Implementación paso a paso

### Paso 1: Definir el alcance

1. Confirmar el flujo dashboard -> filtros -> detalle -> indicador.
2. Usar las tres líneas ficticias de la sección 8: Construcción sustentable, Biomateriales de madera y Procesos industriales.
3. Usar los cinco indicadores y las reglas de cálculo de la sección 8.
4. Usar los tres años demostrativos acordados: 2022, 2023 y 2024.
5. Escribir los criterios de aceptación antes de programar.

### Paso 2: Crear el proyecto Android

Para un proyecto nuevo, seguir estos pasos. MaderaTimber ya existe; conservar su paquete `com.duoc.madertimber`, su configuración y su repositorio. Verificar el entorno en lugar de recrearlo.

1. Abrir Android Studio.
2. Crear un proyecto con plantilla Empty Activity.
3. Seleccionar Kotlin.
4. Activar Jetpack Compose.
5. Usar un minSdk compatible con los dispositivos de prueba.
6. Configurar nombre del paquete y repositorio Git.
7. Ejecutar la aplicación inicial en un emulador o dispositivo.

### Paso 3: Configurar dependencias

Agregar dependencias para:

- Compose y Material 3.
- Navigation Compose.
- ViewModel Compose.
- Coroutines.
- JSON local o Room.
- Pruebas unitarias y de interfaz.

Después de cada cambio en Gradle, ejecutar una compilación.

### Paso 4: Crear los modelos

1. Crear las clases de datos.
2. Definir los tipos de indicadores.
3. Definir relaciones mediante IDs.
4. Evitar duplicar datos.
5. Crear suficientes datos para probar filtros y gráficos.

### Paso 5: Crear la fuente de datos

1. Crear archivos JSON en app/src/main/assets.
2. Crear un lector de JSON.
3. Crear un Repository con funciones suspendidas o Flow.
4. Agregar estados de carga, éxito y error.
5. Verificar que la aplicación funcione sin internet.

### Paso 6: Crear la navegación

Definir estas rutas:

- dashboard
- linea/{lineaId}/{anio}
- indicador/{indicadorId}/{anio}
- relacionados/{lineaId}/{anio}?tipo={tipo}

El parámetro `tipo` es opcional: se omite al navegar desde una línea y se incluye desde un indicador para determinar las pestañas aplicables. Validar IDs, años y tipos recibidos; ante argumentos inválidos, mostrar un error con opción de volver.

La navegación debe permitir volver al dashboard sin perder línea, año ni métrica de comparación. Mantener este estado en un ViewModel del flujo y guardarlo con SavedStateHandle para restaurarlo ante recreaciones de la pantalla.

### Paso 7: Implementar el ViewModel

El ViewModel debe manejar:

- Línea seleccionada.
- Año seleccionado.
- Indicadores filtrados.
- Estado de carga.
- Mensajes de error.
- Indicador seleccionado.
- Tipo de indicador elegido para comparar líneas.

La UI no debe ejecutar consultas ni transformaciones complejas directamente.

Definir las rutas del paso 6 al preparar la navegación; completar su restauración después de introducir este ViewModel. Implementar el estado de filtros en el ViewModel antes de agregar selectores interactivos al dashboard. Una práctica inicial con una tarjeta estática puede preceder estos pasos para aprender Compose.

### Paso 8: Construir el dashboard

1. Crear el Scaffold.
2. Agregar la barra superior.
3. Agregar selectores de línea y año.
4. Crear tarjetas reutilizables.
5. Agregar el gráfico comparativo.
6. Agregar navegación al detalle.
7. Probar en pantallas pequeñas y grandes.

### Paso 9: Construir el detalle de línea

1. Mostrar información general.
2. Mostrar indicadores.
3. Agregar gráfico histórico.
4. Agregar listas de proyectos, investigadores y publicaciones.
5. Agregar estados vacíos cuando no existan datos.

### Paso 10: Construir el detalle de indicador

1. Mostrar el valor del año seleccionado o “Sin datos”.
2. Calcular la variación porcentual o la diferencia en puntos porcentuales según la sección 8, contemplando valores ausentes y base cero.
3. Mostrar la evolución histórica.
4. Explicar el indicador en lenguaje simple.
5. Mostrar una etiqueta de datos demostrativos.

### Paso 11: Aplicar diseño y accesibilidad

- Usar una paleta consistente con una temática científica y forestal.
- Mantener buen contraste.
- No depender solo del color.
- Usar textos legibles.
- Añadir descripciones a iconos y gráficos.
- Probar orientación vertical y desplazamiento.
- Evitar saturar la pantalla.

### Paso 12: Agregar persistencia opcional

Si el tiempo lo permite, guardar:

- Última línea seleccionada.
- Último año seleccionado.
- Indicadores favoritos.
- Historial de líneas consultadas.

Room es suficiente para esta funcionalidad. No es necesario crear un backend.

Esta persistencia opcional conserva preferencias entre sesiones. La conservación de filtros al navegar y recrear pantallas mediante SavedStateHandle sigue siendo un requisito obligatorio del flujo, aunque no se implemente Room.

## 12. Criterios de aceptación

El MVP se considera funcional cuando:

- La aplicación inicia sin errores.
- El usuario puede seleccionar línea y año.
- Los indicadores se actualizan según los filtros.
- Los gráficos coinciden con los datos de las tarjetas.
- El detalle conserva el año seleccionado, incluso si existe un valor más reciente.
- El comparativo muestra la misma métrica y año para las tres líneas y resalta la seleccionada.
- Los conteos coinciden con las entidades únicas de las listas relacionadas tras aplicar los mismos filtros.
- El impacto reproduce la fórmula demostrativa y el avance usa solo proyectos activos del año.
- Los valores ausentes y las variaciones con base cero tienen los mensajes definidos; no se muestran ceros inventados ni valores infinitos.
- Se puede abrir el detalle de una línea.
- Se puede consultar la evolución de un indicador.
- Existen proyectos, investigadores y publicaciones relacionados.
- Se puede volver entre pantallas y recrear la pantalla conservando línea, año y métrica de comparación.
- La app funciona con datos locales y sin internet.
- Los datos están identificados como ficticios o sintéticos.
- El flujo se entiende en una demostración de pocos minutos.

## 13. Plan de trabajo sugerido

### Iteración 1: Base técnica

- Crear proyecto.
- Configurar Compose.
- Crear modelos.
- Agregar datos sintéticos.
- Crear navegación inicial.

### Iteración 2: Funcionalidad principal

- Implementar Repository.
- Implementar ViewModel.
- Crear dashboard.
- Agregar filtros.
- Mostrar tarjetas.

### Iteración 3: Visualización

- Agregar gráfico comparativo.
- Crear detalle de línea.
- Agregar gráfico histórico.
- Crear detalle de indicador.

### Iteración 4: Contenido relacionado

- Agregar proyectos.
- Agregar investigadores.
- Agregar publicaciones.
- Agregar navegación entre contenidos.

### Iteración 5: Calidad y presentación

- Agregar estados vacíos y errores.
- Revisar accesibilidad.
- Ejecutar pruebas.
- Preparar datos de demostración.
- Crear documentación y capturas.

## 14. Pruebas mínimas

### Pruebas unitarias

- Filtrar indicadores por línea.
- Filtrar indicadores por año.
- Calcular variaciones positivas y negativas, valores faltantes y base anterior cero, incluido el caso de ambos valores cero.
- Verificar que una comparación use `t - 1` y no otro año disponible.
- Calcular diferencias de avance en puntos porcentuales, incluido un avance anterior cero.
- Obtener el valor del año seleccionado aunque exista otro más reciente.
- Obtener contenidos relacionados por línea, año y tipo de indicador.
- Comprobar que los conteos coincidan con las listas y que un investigador no se cuente dos veces en una misma línea y año.
- Validar IDs, referencias, duplicados y límites de avance; rechazar JSON inválido.
- Verificar la fórmula de impacto con los tres ejemplos, valores cero y valores que superen las metas.
- Verificar avance sin proyectos activos y distinguir un año sin datos de un conteo cero en un conjunto completo.
- Validar la cobertura por línea/año declarada en el JSON, incluso cuando una línea no tenga entidades para ese año.

### Pruebas de interfaz

- Seleccionar línea.
- Seleccionar año.
- Abrir detalle de línea.
- Abrir detalle de indicador.
- Volver al dashboard.
- Mostrar mensaje sin resultados.
- Cambiar la métrica comparada y comprobar que todas las barras usan el mismo año y unidad.
- Conservar filtros al volver y al recrear la pantalla.
- Mostrar huecos en el histórico y “Sin datos” en el comparativo cuando falten valores.
- Abrir las pestañas correspondientes a cada indicador y comprobar el año visible.

### Recorrido de demostración

1. Abrir la aplicación.
2. Seleccionar Construcción sustentable.
3. Seleccionar 2024.
4. Mostrar las tarjetas.
5. Abrir el gráfico de evolución.
6. Seleccionar un indicador.
7. Revisar proyectos y publicaciones.
8. Cambiar de línea y comprobar que los valores se actualizan.

## 15. Entregables

- Proyecto Android Studio compilable.
- Código fuente organizado.
- Archivo con datos sintéticos.
- README con instrucciones de ejecución.
- Diagrama simple de arquitectura.
- Modelo de datos.
- Capturas de pantallas.
- Lista de casos de prueba.
- Video breve o demostración presencial.

## 16. Riesgos y decisiones de alcance

### Demasiados indicadores

Comenzar con cuatro tarjetas y dos gráficos.

### Gráficos difíciles

Usar barras y líneas simples, priorizando legibilidad.

### Datos poco creíbles

Documentar las reglas usadas para generar los datos y marcarlos como sintéticos.

### Aplicación demasiado grande

Mantener un único flujo completo y dejar funciones secundarias para una versión futura.

## 17. Mejoras futuras

- Backend con API REST.
- Actualización remota de indicadores.
- Exportación de reportes.
- Comparación avanzada entre períodos.
- Notificaciones sobre nuevos proyectos.
- Inicio de sesión por perfiles.
- Modo offline con sincronización.
- Visualización geográfica de organizaciones.

Estas funciones no son necesarias para el MVP inicial.

## 18. Definición final del MVP

La primera versión debe concentrarse en esta experiencia:

> Seleccionar una línea de investigación, revisar sus indicadores sintéticos, observar su evolución y navegar hacia los contenidos relacionados.

Es preferible entregar un MVP pequeño, estable y demostrable antes que una aplicación extensa con funcionalidades incompletas.
