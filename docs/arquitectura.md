# Arquitectura: implementación actual y objetivo

Revisión por lectura: 2026-10-28. Configuración y versiones en [README](../README.md); requisitos MVVM y estructura objetivo en la sección 6 de [especificacion.md](especificacion.md); validación en [estado-actual.md](estado-actual.md).

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
| app/src/main/java/com/duoc/madertimber/data/model/ | Los diez contratos de la sección 7 están presentes, incluidos `VinculacionInvestigador`, `Investigador` y `Publicacion` como cambios locales. `IntegridadDatos.kt` contiene el primer control de IDs duplicados de líneas; faltan las demás reglas de integridad y sus pruebas. |
| app/src/main/java/com/duoc/madertimber/ui/screens/investigadores/ | Lista y ficha estáticas con contenido fijo y previews; todavía no reciben estado del ViewModel ni se conectan a la navegación. |
| app/src/main/java/com/duoc/madertimber/ui/navegacion/navegacion.kt | Solo declaración de paquete; las rutas y destinos del panel siguen pendientes. |
| app/src/main/java/com/duoc/madertimber/ui/screens/auth/AuthScreen.kt | Vistas sin estado propio de formulario: renderizan AuthUiState y emiten AuthEvent; incluyen previews. |
| app/src/main/java/com/duoc/madertimber/ui/screens/auth/AuthViewModel.kt | LoginViewModel y RegisterViewModel, estado observable inmutable y procesamiento de eventos. |
| app/src/main/java/com/duoc/madertimber/model/auth/AuthValidator.kt | Reglas de validación y errores de los formularios. |
| app/src/main/java/com/duoc/madertimber/ui/theme/ | Paletas, tipografía y tema Compose. |
| app/src/main/res/ | Recursos de ventana, etiqueta, iconos y backup de plantilla. |
| app/src/test/ y app/src/androidTest/ | Seis pruebas JVM de acceso y ejemplos de plantilla JVM/instrumentado; sin pruebas UI del flujo. |

La documentación vive en docs/. Builds, cachés y local.properties son locales por equipo.

## Flujo existente

MainActivity activa edge-to-edge y monta el tema y un Scaffold a tamaño completo. Inicia en LoginScreen; permite cambiar a RegisterScreen o abrir DashboardScreen como invitado. La selección se conserva con rememberSaveable; Atrás desde registro o dashboard vuelve al login. Cada formulario recibe estado observable de su ViewModel, obtenido con ViewModelProvider y ligado al ciclo de vida de la actividad. Los eventos modifican el estado y delegan las reglas en AuthValidator. La actividad limpia ambos formularios al navegar, incluido Atrás. El estado sobrevive a cambios de configuración; las contraseñas solo viven en memoria, no se guardan en SavedStateHandle y se pierden al morir el proceso o abandonar el formulario. No existe Repository de autenticación porque no hay acceso a datos ni servicio de cuentas: un envío válido muestra un aviso explícito de demostración. El dashboard utiliza una Column desplazable, cuatro tarjetas reutilizables y tres barras con las cifras de ejemplo del plan para 2024. DashboardPreview permite inspección estática en el IDE. Los valores son constantes de esta práctica visual; todavía no se calculan desde entidades.

El tema utiliza modo oscuro/claro y colores dinámicos en Android 12 o superior cuando están habilitados. No hay todavía estado de negocio del panel, acceso a datos, filtros interactivos ni navegación de detalles; solo existe el flujo visual de acceso. Las maquetas de lista y ficha de investigadores se pueden previsualizar, pero `MainActivity` no las presenta.

## Responsabilidades objetivo

| Componente | Responsabilidad |
| --- | --- |
| View Compose | Renderizar el estado, accesibilidad y eventos de usuario. |
| DashboardViewModel y ViewModels de detalle según necesidad | Mantener estado observable y coordinar filtros/carga; comunicar resultados y errores. |
| Model/Repository | Leer la fuente local, validar entidades y aplicar los cálculos definidos. |
| JSON en assets | Entidades sintéticas y cobertura temporal declarada. |
| Navegación | Transmitir IDs/año/tipo válidos y conservar el contexto del flujo. |

La sección 6 de la especificación contiene el árbol objetivo del paquete existente com.duoc.madertimber. Mantener :app; módulos adicionales no están aprobados. Room es requisito de la entrega y se incorporará para persistir la selección mediante Repository; casos de uso e Hilt siguen siendo opcionales.

El acceso demostrativo ya separa View, ViewModel y reglas del modelo. MVVM para el dashboard se incorporará al añadir datos y filtros. La práctica inicial estática de Compose prepara la View; no acredita arquitectura completa. El código y las pruebas deben demostrar el flujo View → ViewModel → Repository y actualización del estado.
