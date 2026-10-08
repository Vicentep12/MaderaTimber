# Arquitectura: implementación actual y objetivo

Revisión por lectura: 2026-10-08. Configuración y versiones en [README](../README.md); requisitos MVVM y estructura objetivo en la sección 6 de [especificacion.md](especificacion.md); validación en [estado-actual.md](estado-actual.md).

## Estructura actual

Un módulo Android :app. Archivos principales, relativos a la raíz Git:

| Ubicación | Responsabilidad |
| --- | --- |
| settings.gradle.kts | Incluye :app y configura repositorios y resolución de toolchains. |
| build.gradle.kts y gradle/libs.versions.toml | Plugins y versiones compartidas. |
| app/build.gradle.kts | Identidad, SDK, opciones de compilación y dependencias de la aplicación. |
| app/src/main/AndroidManifest.xml | Launcher MainActivity, configuración de ventana y backup. |
| app/src/main/java/com/duoc/madertimber/MainActivity.kt | Actividad, Greeting y GreetingPreview. |
| app/src/main/java/com/duoc/madertimber/ui/theme/ | Paletas, tipografía y tema Compose. |
| app/src/main/res/ | Recursos de ventana, etiqueta, iconos y backup de plantilla. |
| app/src/test/ y app/src/androidTest/ | Ejemplos de pruebas JVM e instrumentadas. |

La documentación vive en docs/. Builds, cachés y local.properties son locales por equipo.

## Flujo existente

MainActivity activa edge-to-edge y monta el tema, un Scaffold a tamaño completo y Greeting con el padding del Scaffold. Greeting recibe un nombre y Modifier, construye un saludo y lo muestra con Text. GreetingPreview permite inspección estática en el IDE.

El tema utiliza modo oscuro/claro y colores dinámicos en Android 12 o superior cuando están habilitados. UI de plantilla: no hay todavía estado de negocio, acceso a datos ni navegación entre pantallas.

## Responsabilidades objetivo

| Componente | Responsabilidad |
| --- | --- |
| View Compose | Renderizar el estado, accesibilidad y eventos de usuario. |
| DashboardViewModel y ViewModels de detalle según necesidad | Mantener estado observable y coordinar filtros/carga; comunicar resultados y errores. |
| Model/Repository | Leer la fuente local, validar entidades y aplicar los cálculos definidos. |
| JSON en assets | Entidades sintéticas y cobertura temporal declarada. |
| Navegación | Transmitir IDs/año/tipo válidos y conservar el contexto del flujo. |

La sección 6 de la especificación contiene el árbol objetivo del paquete existente com.duoc.madertimber. Mantener :app; módulos adicionales no están aprobados. Casos de uso, Hilt y Room se incorporan solo si su necesidad está justificada, de acuerdo con el alcance.

MVVM se incorporará al añadir datos y filtros. La práctica inicial estática de Compose prepara la View; no acredita arquitectura completa. El código y las pruebas deben demostrar el flujo View → ViewModel → Repository y actualización del estado.
