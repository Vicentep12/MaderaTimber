# MVP: Panel de indicadores de impacto CENAMAD

Integración: 2026-10-08. **MaderaTimber implementa este panel Android.** Documento recuperado parcialmente de acuerdos de chat; no es la copia íntegra del README original de `E:\AnalistaProgramador\Desarrollo app moviles\MVP_Panel_Indicadores_Impacto_CENAMAD`. Contrastar ese archivo antes de cerrar requisitos detallados.

## Alcance recuperado

Dashboard para seleccionar línea de investigación y año; consultar proyectos, publicaciones, investigadores e impacto estimado; comparar líneas y acceder a su detalle. Datos locales inicialmente y arquitectura MVVM exigida por la asignatura.

Primer incremento confirmado: título de la pantalla, «Construcción sustentable», año 2024, tarjeta «Proyectos activos: 8» y etiqueta «Datos demostrativos». Años y cantidades son ejemplos; no representan estadísticas reales. Las fórmulas, catálogo completo de líneas, diseño detallado y criterios de evaluación no se recuperaron íntegramente y deben contrastarse con el original.

## Arquitectura prevista

| Parte | Responsabilidad |
| --- | --- |
| Model | Modelos de datos, Repository y reglas de cálculo acordadas; fuente local demostrativa inicial. |
| View | Pantallas Jetpack Compose; muestran estado y comunican eventos del usuario. |
| ViewModel | `DashboardViewModel`: selección de línea/año, indicadores y estados de carga/error según el contrato; solicita datos al Repository y publica estado observable. |

MVVM está planificado; el código actual solo contiene la base Compose. Ruta de implementación y criterios: [plan del MVP](PLAN_IMPLEMENTACION.md), que remite al [plan común](../PLAN_IMPLEMENTACION.md). Dinámica de trabajo: [aprendizaje](../APRENDIZAJE.md); siguiente lección: [progreso](../PROGRESO.md).
