# Arquitectura: implementación actual y objetivo

Revisión por lectura: 2026-10-15. Configuración y versiones en [README](../README.md); requisitos MVVM y estructura objetivo en la sección 6 de [especificacion.md](especificacion.md); validación en [estado-actual.md](estado-actual.md).

## Estructura actual

Un módulo Android :app. Archivos principales, relativos a la raíz Git:

| Ubicación | Responsabilidad |
| --- | --- |
| settings.gradle.kts | Incluye :app y configura repositorios y resolución de toolchains. |
| build.gradle.kts y gradle/libs.versions.toml | Plugins y versiones compartidas. |
| app/build.gradle.kts | Identidad, SDK, opciones de compilación y dependencias de la aplicación. |
| app/src/main/AndroidManifest.xml | Launcher MainActivity, configuración de ventana y backup. |
| app/src/main/java/com/duoc/madertimber/MainActivity.kt | Actividad, tema, Scaffold y selección de login, registro o dashboard. |
| app/src/main/java/com/duoc/madertimber/ui/screens/dashboard/DashboardScreen.kt | Vista estática inicial, tarjetas, barras comparativas y preview. |
| app/src/main/java/com/duoc/madertimber/ui/screens/auth/AuthScreen.kt | Login y registro de demostración, campos, validación local y previews. |
| app/src/main/java/com/duoc/madertimber/ui/theme/ | Paletas, tipografía y tema Compose. |
| app/src/main/res/ | Recursos de ventana, etiqueta, iconos y backup de plantilla. |
| app/src/test/ y app/src/androidTest/ | Ejemplos de pruebas JVM e instrumentadas. |

La documentación vive en docs/. Builds, cachés y local.properties son locales por equipo.

## Flujo existente

MainActivity activa edge-to-edge y monta el tema y un Scaffold a tamaño completo. Inicia en LoginScreen; permite cambiar a RegisterScreen o abrir DashboardScreen como invitado. La selección se conserva con rememberSaveable; Atrás desde registro o dashboard vuelve al login. Cada formulario tiene su propio estado de UI; las contraseñas solo viven en memoria y se descartan al salir o recrear la actividad. Los formularios no tienen Repository ni autenticación: al validar muestran un aviso explícito de demostración. El dashboard utiliza una Column desplazable, cuatro tarjetas reutilizables y tres barras con las cifras de ejemplo del plan para 2024. DashboardPreview permite inspección estática en el IDE. Los valores son constantes de esta práctica visual; todavía no se calculan desde entidades.

El tema utiliza modo oscuro/claro y colores dinámicos en Android 12 o superior cuando están habilitados. No hay todavía estado de negocio, acceso a datos, filtros interactivos ni navegación de detalles; solo existe el flujo visual de acceso.

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
