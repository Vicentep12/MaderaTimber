# Arquitectura: implementación actual y objetivo

Revisión por lectura: 2026-10-29. Configuración y versiones en [README](../README.md); requisitos MVVM y estructura objetivo en la sección 6 de [especificacion.md](especificacion.md); validación en [estado-actual.md](estado-actual.md).

## Estructura actual

Un módulo Android :app. Archivos principales, relativos a la raíz Git:

| Ubicación | Responsabilidad |
| --- | --- |
| settings.gradle.kts | Incluye :app y configura repositorios y resolución de toolchains. |
| build.gradle.kts y gradle/libs.versions.toml | Plugins y versiones compartidas. |
| app/build.gradle.kts | Identidad, SDK, opciones de compilación y dependencias de la aplicación. |
| app/src/main/AndroidManifest.xml | Launcher MainActivity, configuración de ventana y backup. |
| app/src/main/java/com/duoc/madertimber/MainActivity.kt | Actividad, tema e inicialización de los ViewModels; entrega el flujo a AppNavigation. |
| app/src/main/java/com/duoc/madertimber/ui/navigation/ | Rutas, argumentos y NavHost; conecta acceso, dashboard, detalles, contenidos relacionados y fichas, con regreso y error para argumentos inválidos. |
| app/src/main/java/com/duoc/madertimber/ui/screens/dashboard/DashboardScreen.kt y FiltrosPanelDialog.kt | Vista del panel: renderiza DashboardUiState, muestra imagen, tarjetas animadas, filtros rápidos, modal de selección, barras accesibles y estados; emite DashboardEvent sin consultar datos directamente. El modal mantiene un borrador hasta Aplicar. |
| app/src/main/java/com/duoc/madertimber/ui/screens/dashboard/DashboardViewModel.kt | Estado observable, eventos, carga asíncrona, selección guardada y coordinación de indicadores y contenidos relacionados. |
| app/src/main/java/com/duoc/madertimber/ui/screens/lineas/DetalleLinea.kt | Vista del detalle de línea: representa cinco indicadores, histórico seleccionado, conteos de relacionados y estado sin cobertura; envía eventos y acciones de navegación. |
| app/src/main/java/com/duoc/madertimber/ui/components/GraficoHistorico.kt | Componente reutilizable de líneas y puntos para históricos preparados: etiquetas, unidad, selección, huecos y resumen textual. |
| app/src/main/java/com/duoc/madertimber/data/model/ | Los diez contratos de la sección 7 están versionados. `IntegridadDatos.kt` valida entidades al cargar; `CalculosIndicadores.kt` deriva métricas, históricos y variaciones desde esas entidades; `HistoricoIndicador.kt` prepara el eje anual con huecos sin depender de Compose. |
| app/src/main/assets/cenamad_datos.json | Entidades sintéticas de tres líneas, cobertura declarada 2022–2024 y ninguna cifra de indicador mantenida a mano. |
| app/src/main/java/com/duoc/madertimber/data/repository/ | `LectorDatosPanel` analiza JSON estricto y aplica la integridad de T02 y la cobertura; `LocalPanelRepository` lee el asset, consulta entidades e indicadores por línea/año y filtra contenidos relacionados según el tipo, con estados de carga, vacío, falta de cobertura y error. |
| app/src/main/java/com/duoc/madertimber/ui/screens/relacionados/ | Lista con pestañas aplicables y fichas básicas alimentadas por datos del DashboardViewModel. |
| app/src/main/java/com/duoc/madertimber/ui/screens/investigadores/ | Maquetas previas de lista y ficha, conservadas para el trabajo completo de T12–T14; el flujo T06 usa la pantalla relacionada común. |
| app/src/main/java/com/duoc/madertimber/ui/screens/auth/AuthScreen.kt | Vistas sin estado propio de formulario: renderizan AuthUiState y emiten AuthEvent; incluyen previews. |
| app/src/main/java/com/duoc/madertimber/ui/screens/auth/AuthViewModel.kt | LoginViewModel y RegisterViewModel, estado observable inmutable y procesamiento de eventos. |
| app/src/main/java/com/duoc/madertimber/model/auth/AuthValidator.kt | Reglas de validación y errores de los formularios. |
| app/src/main/java/com/duoc/madertimber/ui/theme/ | Paletas, tipografía y tema Compose. |
| app/src/main/res/ | Recursos de ventana, etiqueta, iconos y backup de plantilla. |
| app/src/test/ y app/src/androidTest/ | Pruebas JVM de acceso, integridad, datos, cálculos, rutas y DashboardViewModel; pruebas Android del dashboard, filtros y navegación. Los resultados actuales constan en estado-actual.md. |

La documentación vive en docs/. Builds, cachés y local.properties son locales por equipo.

## Flujo existente

MainActivity activa edge-to-edge, crea los ViewModels y monta el tema. AppNavigation contiene el Scaffold y el NavHost de Navigation Compose. Inicia en LoginScreen, permite registro y entrada como invitado, y conecta dashboard → línea → indicador → relacionados → ficha. Los destinos llevan ID, año y tipo opcional; las funciones puras de `data/model/ValidacionRutasPanel.kt` comprueban contexto, indicador y ficha antes de mostrar contenido, con regreso ante error. El NavController conserva la pila y DashboardViewModel, ligado a la actividad, conserva línea, año y métrica en SavedStateHandle. Al abandonar acceso o cerrar sesión se limpian ambos formularios; contraseñas no se persisten. El acceso sigue siendo demostrativo.

El tema utiliza modo oscuro/claro y colores dinámicos en Android 12 o superior cuando están habilitados. DashboardScreen recibe DashboardUiState y emite eventos; T07 añadió una imagen local, transiciones de tarjeta y barras, controles rápidos y descripciones accesibles. T08 añadió un modal con selección provisional de línea y año que solo envía el evento conjunto al aplicar. T09 añadió un gráfico histórico reutilizable y lo integró en DetalleIndicadorScreen con puntos preparados por el modelo y el ViewModel. T10 conectó el detalle de línea con los cinco indicadores, histórico seleccionable y conteos relacionados; DashboardViewModel conserva esa elección aparte de la comparación del panel. La pantalla relacionada recibe listas filtradas desde el ViewModel; el contenido completo de T11–T14 permanece pendiente. La navegación, el dashboard y el histórico de T09–T10 se comprobaron con pruebas Android en emulador API 37; la revisión completa de la entrega sigue pendiente.

## Responsabilidades objetivo

| Componente | Responsabilidad |
| --- | --- |
| View Compose | Renderizar el estado, accesibilidad y eventos de usuario. |
| DashboardViewModel y ViewModels de detalle según necesidad | Mantener estado observable y coordinar filtros/carga; comunicar resultados y errores. |
| Model/Repository | Leer la fuente local, validar entidades y aplicar los cálculos definidos. |
| JSON en assets | Entidades sintéticas y cobertura temporal declarada. |
| Navegación | Transmitir IDs/año/tipo válidos y conservar el contexto del flujo. |

La sección 6 de la especificación contiene el árbol objetivo del paquete existente com.duoc.madertimber. Mantener :app; módulos adicionales no están aprobados. Room es requisito de la entrega y se incorporará para persistir la selección mediante Repository; casos de uso e Hilt siguen siendo opcionales.

El acceso demostrativo separa View, ViewModel y reglas del modelo. El dashboard y los contenidos relacionados siguen View → ViewModel → Repository: las vistas representan estado y envían eventos, el ViewModel coordina la carga y el Repository consulta datos y cálculos. Falta completar las vistas de detalle de T10–T14 y revisar visualmente el recorrido completo.
