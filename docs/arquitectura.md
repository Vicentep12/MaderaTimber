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
| app/src/main/java/com/duoc/madertimber/MainActivity.kt | Actividad, tema, inicialización de los ViewModels y toda la navegación (objeto `Rutas`, composable `AppNavigation` con Scaffold y NavHost). |
| app/src/main/java/com/duoc/madertimber/ui/screens/dashboard/DashboardScreen.kt y FiltrosPanelDialog.kt | Vista del panel: renderiza DashboardUiState, muestra imagen, tarjetas animadas, filtros rápidos, modal de selección, barras accesibles y estados; emite DashboardEvent sin consultar datos directamente. El modal mantiene un borrador hasta Aplicar. |
| app/src/main/java/com/duoc/madertimber/ui/screens/dashboard/DashboardViewModel.kt | Estado observable, eventos, carga asíncrona, selección guardada y coordinación de indicadores y contenidos relacionados. |
| app/src/main/java/com/duoc/madertimber/ui/screens/lineas/DetalleLinea.kt | Vista del detalle de línea: representa cinco indicadores, histórico seleccionado, conteos de relacionados y estado sin cobertura; envía eventos y acciones de navegación. |
| app/src/main/java/com/duoc/madertimber/ui/screens/lineas/DetalleIndicador.kt | Vista del indicador: representa valor, unidad, variación calculada, histórico, interpretación y acceso a relacionados. |
| app/src/main/java/com/duoc/madertimber/ui/components/GraficoHistorico.kt | Componente reutilizable de líneas y puntos para históricos preparados: etiquetas, unidad, selección, huecos y resumen textual. |
| app/src/main/java/com/duoc/madertimber/data/model/ | Los diez contratos de la sección 7 están versionados. `IntegridadDatos.kt` valida entidades al cargar; `CalculosIndicadores.kt` deriva métricas, históricos y variaciones desde esas entidades; `HistoricoIndicador.kt` prepara el eje anual con huecos sin depender de Compose. |
| app/src/main/assets/cenamad_datos.json | Entidades sintéticas de tres líneas, cobertura declarada 2022–2024 y ninguna cifra de indicador mantenida a mano. |
| app/src/main/java/com/duoc/madertimber/data/repository/ | `LectorDatosPanel` analiza JSON estricto y aplica la integridad de T02 y la cobertura; `LocalPanelRepository` lee el asset, consulta entidades e indicadores por línea/año y filtra contenidos relacionados según el tipo, con estados de carga, vacío, falta de cobertura y error. |
| app/src/main/java/com/duoc/madertimber/ui/screens/relacionados/ | Lista con categorías aplicables, totales, estados vacíos y apertura de fichas por ID; fichas de proyecto, investigador y publicación con el contexto de la selección. Recibe datos filtrados del DashboardViewModel. |
| app/src/main/java/com/duoc/madertimber/ui/screens/auth/AuthScreen.kt | Vistas sin estado propio de formulario: renderizan AuthUiState y emiten AuthEvent; gestionan foco, teclado y aviso modal de presentación; incluyen previews. |
| app/src/main/java/com/duoc/madertimber/ui/screens/auth/AuthViewModel.kt | LoginViewModel y RegisterViewModel, estado observable inmutable y procesamiento de eventos. |
| app/src/main/java/com/duoc/madertimber/data/model/AuthValidator.kt | Reglas de validación y errores de los formularios. |
| app/src/main/java/com/duoc/madertimber/ui/theme/ | Paletas, tipografía y tema Compose. |
| app/src/main/res/ | Recursos de ventana, etiqueta, iconos y backup de plantilla. |
| app/src/main/java/com/duoc/madertimber/data/repository/SeleccionRoom.kt | Entidad, DAO, base Room y SeleccionRepository para guardar la última línea, año y métrica de comparación. MainActivity entrega el repositorio a DashboardViewModel. |

La documentación vive en docs/. Builds, cachés y local.properties son locales por equipo.

## Flujo existente

MainActivity activa edge-to-edge, crea los ViewModels, monta el tema y contiene toda la navegación: `Rutas` y `AppNavigation` (Scaffold y NavHost de Navigation Compose) viven en el mismo archivo. Inicia en LoginScreen, permite registro y entrada como invitado, y conecta dashboard → línea → indicador → relacionados → ficha. Los destinos llevan ID, año y tipo opcional; las funciones puras de `data/model/ValidacionRutasPanel.kt` comprueban contexto, indicador y ficha antes de mostrar contenido, con regreso ante error. El NavController conserva la pila y DashboardViewModel, ligado a la actividad, conserva línea, año y métrica en SavedStateHandle. Al abandonar acceso o cerrar sesión se limpian ambos formularios; contraseñas no se persisten. El acceso sigue siendo demostrativo.

El tema utiliza modo oscuro/claro y una paleta verde fija alineada con la referencia. DashboardScreen recibe DashboardUiState y emite eventos; T07 añadió una imagen local, transiciones de tarjeta y barras, controles rápidos y descripciones accesibles. T08 añadió un modal con selección provisional de línea y año que solo envía el evento conjunto al aplicar. T09 añadió un gráfico histórico reutilizable y lo integró en DetalleIndicadorScreen con puntos preparados por el modelo y el ViewModel. T10 conectó el detalle de línea con los cinco indicadores, histórico seleccionable y conteos relacionados; DashboardViewModel conserva esa elección aparte de la comparación del panel. T11 completó en código el detalle del indicador con variación e interpretación. T12 completó las listas relacionadas con categorías, totales, vacíos y acceso por ID; los datos siguen filtrándose en el Repository. T13 completó las fichas de proyecto, investigador y publicación en el flujo actual. T14 ajustó y comprobó los formularios de acceso en emulador API 37. T15 revisó las doce vistas en tres perfiles Android y ajustó la presentación de contraseñas y tarjetas para pantallas estrechas o texto ampliado; evidencia y límites en estado-actual.md. La navegación, el dashboard y el histórico de T09–T10 se comprobaron con pruebas Android anteriores. T18 añadió Room: DashboardViewModel lee la selección guardada al cargar (SavedStateHandle tiene prioridad si ya contiene valores), la valida contra los datos disponibles y guarda cada cambio de línea, año o métrica de comparación fuera del hilo principal. El 29/10 se retiraron las pruebas automatizadas y sus librerías por instrucción del usuario.

## Responsabilidades objetivo

| Componente | Responsabilidad |
| --- | --- |
| View Compose | Renderizar el estado, accesibilidad y eventos de usuario. |
| DashboardViewModel y ViewModels de detalle según necesidad | Mantener estado observable y coordinar filtros/carga; comunicar resultados y errores. |
| Model/Repository | Leer la fuente local, validar entidades y aplicar los cálculos definidos. |
| JSON en assets | Entidades sintéticas y cobertura temporal declarada. |
| Navegación | Transmitir IDs/año/tipo válidos y conservar el contexto del flujo. |

La sección 6 de la especificación contiene el árbol objetivo del paquete existente com.duoc.madertimber. Mantener :app; módulos adicionales no están aprobados. `SeleccionRepository` utiliza Room para persistir línea, año y métrica; DashboardViewModel coordina la recuperación y la escritura. La validación de T18 sigue en curso. Casos de uso e Hilt siguen siendo opcionales.

El acceso demostrativo separa View, ViewModel y reglas del modelo. El dashboard y los contenidos relacionados siguen View → ViewModel → Repository: las vistas representan estado y envían eventos, el ViewModel coordina la carga y el Repository consulta datos y cálculos. La revisión visual de T15 está completada; faltan los casos integrados de T16 y los requisitos restantes de entrega.
