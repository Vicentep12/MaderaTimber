# Estado técnico y validación

Última comprobación técnica: 2026-10-29. Los cortes anteriores y los antecedentes Windows del 2026-10-08 se conservan abajo como históricos. Comandos y herramientas requeridas en [README](../README.md); responsabilidades en [arquitectura.md](arquitectura.md); tareas abiertas en [plan-implementacion.md](plan-implementacion.md).

## Simplificación de consultas - 2026-10-29

El repositorio reutiliza la regla de categorías relacionadas para filtrar los contenidos por indicador. El ViewModel concentra la lectura de resultados disponibles o vacíos. Se conserva MVVM y no se modifican las fórmulas ni Room. Android Studio ejecutó assembleDebug: BUILD SUCCESSFUL. La comprobación visual queda pendiente.

Por decisión del usuario, no se implementará el selector nativo de archivos; Room se revisará al final.

## Persistencia de selección — T18 — 2026-10-29

En `main`, HEAD `e73d7ef` y árbol limpio al iniciar, se incorporaron Room 2.8.5 y KSP 2.3.12, entidad, DAO, base y Repository para línea, año y métrica de comparación (`data/repository/SeleccionRoom.kt`). `MainActivity` crea la base en disco (`seleccion-panel.db`) y la entrega a DashboardViewModel. El ViewModel lee la selección guardada antes de la primera carga, da prioridad a SavedStateHandle cuando contiene valores y valida línea, año y métrica contra los datos disponibles antes de usarla. Cada cambio de línea, año, filtros o métrica de comparación se guarda en orden fuera del hilo principal. No se almacenan credenciales. Los errores de Room se registran en Logcat; la persistencia no presenta un aviso al usuario.

El Wrapper ejecutó `:app:assembleDebug :app:assembleDebugAndroidTest :app:lintDebug --console=plain --max-workers=2`: BUILD SUCCESSFUL, 0 errores y 25 advertencias de Lint. La prueba instrumentada `SeleccionRoomTest` pasó en Pixel_10_Pro API 37: escritura, lectura por un nuevo ViewModel y recuperación ante datos inválidos usando una base en memoria. Esa prueba se retiró después junto con las demás (ver sección siguiente).

Revisión por lectura del 29/10, posterior al commit `a368c53`: el flujo de lectura, validación y escritura es coherente con la aceptación de T18 y no requirió cambios de código. Esta revisión no ejecutó la app: el entorno de revisión no tenía Android SDK.

**Comprobación manual pendiente para cerrar T18** (emulador o dispositivo):

1. Ejecutar la app, entrar como invitado y en el panel elegir una línea distinta de la inicial, el año 2023 y otra métrica de comparación.
2. Cerrar la app por completo (quitarla de recientes o detenerla desde Android Studio) y volver a abrirla.
3. Entrar como invitado: el panel debe mostrar la misma línea, año y métrica.
4. Abrir el detalle de línea y volver: la selección debe mantenerse.

Registrar aquí fecha, dispositivo y resultado al completarla.

## Retiro de pruebas automatizadas — 2026-10-29

Por instrucción del usuario, porque el profesor no las exige, se eliminaron las 18 clases de `app/src/test` y `app/src/androidTest` y se retiraron del catálogo y del módulo `app` JUnit, AndroidX JUnit, Espresso, Compose UI Test (junit4 y manifest), kotlinx-coroutines-test y `testInstrumentationRunner`. El código de `app/src/main` no usaba esas librerías. Los resultados de pruebas citados en secciones anteriores son históricos y ya no se pueden reproducir con el repositorio actual. Tras este cambio no se ejecutó Gradle en el entorno de revisión (sin Android SDK); debe comprobarse `:app:assembleDebug :app:lintDebug` en Android Studio antes de la entrega.

## Flujo integrado — T16 — 2026-10-29

Continuación en `main`, HEAD `ca7c5b0`, árbol limpio al iniciar: se permitió proporcionar el controlador de navegación a `AppNavigation` para comprobar rutas en Android; la aplicación sigue creando el suyo por defecto. `EscenariosIntegradosTest` cargó una copia del JSON con 2025 cubierto solo para L1: el comparativo mostró cero para L1 y “Sin datos” para L2. Una ruta con línea inexistente mostró el error y el botón Volver regresó al panel conservando 2025. En Pixel_10_Pro API 37, **1 prueba instrumentada, 0 fallos**. `:app:assembleDebug :app:assembleDebugAndroidTest :app:lintDebug --console=plain --max-workers=2` terminó **BUILD SUCCESSFUL** (17 tareas ejecutadas y 59 reutilizadas). No se ejecutaron pruebas unitarias. La ruta inválida probada es la de línea; las demás validaciones se revisaron por lectura. T16 queda completada para el flujo actual; Room y selector nativo siguen pendientes en T18–T19.

Rama `main`, árbol limpio al iniciar, con T15 en HEAD `709d6b3`. En Windows, con JDK 25.0.2 y el Wrapper, se ejecutó `:app:assembleDebug :app:assembleDebugAndroidTest :app:lintDebug --console=plain --max-workers=2`: **BUILD SUCCESSFUL**; 3 tareas ejecutadas y 73 reutilizadas. Lint informó **0 errores y 25 advertencias**. La ejecución inicial desde el sandbox no pudo abrir la caché del Wrapper y ADB no pudo iniciar su servidor; ambos comandos funcionaron con los permisos del entorno. No se cambiaron versiones ni SDK.

En el emulador Pixel_10_Pro API 37, `:app:connectedDebugAndroidTest` ejecutó las **11 pruebas UI existentes, 0 fallos**. Cubren el recorrido de doce vistas con acceso y tres fichas, filtros/modal, regreso con año, restauración de línea/año, detalle e histórico, falta de cobertura en una vista preparada, hueco y ausencia total del gráfico histórico. Se añadieron y ejecutaron por separado tres casos UI instrumentados: (1) comparación de publicaciones para las tres líneas en 2023 y restauración de línea L2/año/métrica tras recreación; (2) coincidencia de los conteos de L2/2023 —4 proyectos, 7 investigadores, 7 publicaciones— con sus listas y restricción a publicaciones desde ese indicador; (3) error de carga, evento Reintentar y vista vacía sin indicadores. **Los tres pasaron**. Un primer intento del caso de comparación falló porque la prueba pulsó un chip fuera de la zona visible; se desplazó el panel hasta la comparación, se comprobó el chip seleccionado y se repitió con éxito. Una importación errónea impidió compilar una versión intermedia del segundo caso; corregida, compiló y pasó. No se detectó un fallo funcional de la app en estos casos.

Se desactivaron temporalmente Wi-Fi y datos móviles del emulador; los **6 casos de AppNavigationTest pasaron sin conexión, 0 fallos**. Un bloque `finally` restauró ambas conexiones y ADB confirmó `wifi_on=1` y `mobile_data=1`. Los datos de la app se leyeron desde el JSON local. No se añadieron ni ejecutaron pruebas unitarias, conforme a la instrucción del usuario.

En el primer incremento de T16, las rutas inválidas se revisaron por lectura y la falta de cobertura se comprobó con estados UI preparados. La continuación registrada arriba amplía esa evidencia con un recorrido integrado y una ruta inválida. Room y el selector nativo aún no existen y se trabajan en T18–T19. El recorrido completo con esos requisitos corresponde además a T17. No se acredita la entrega completa.

## Revisión visual y accesibilidad — T15 — 2026-10-29

Rama `main`, HEAD `76b8f50` y árbol limpio al iniciar. Se contrastaron las doce vistas del flujo activo con el plano de la sección 2.2 de la especificación. Se conservaron la paleta, jerarquía de títulos, datos demostrativos y navegación. Las diferencias numéricas respecto al boceto (por ejemplo, avance real de la muestra frente al ejemplo gráfico) siguen los datos del Repository, sin sustituirlos por constantes.

Correcciones: el detalle del indicador recibe y muestra el nombre de la línea; los controles de visibilidad anuncian el campo de contraseña correspondiente y pasan debajo del campo con ancho menor de 360 dp o escala de fuente mayor de 1,3; las cuatro tarjetas del dashboard pasan a una columna bajo esas mismas condiciones para evitar palabras partidas. Las filas de selección del modal tienen mínimo 48 dp y su contenedor declara el grupo de selección. Son ajustes de presentación y semántica: los eventos y reglas siguen en los ViewModels/modelo existentes; no se añadieron validaciones ni acceso a datos a las vistas.

Validación ejecutada con JDK 25.0.2 y Wrapper: `:app:assembleDebug :app:assembleDebugAndroidTest :app:lintDebug --quiet --max-workers=2`, salida **0**. Lint: **0 errores y 23 advertencias** (incluye avisos de versiones y recursos existentes). No se añadieron ni ejecutaron pruebas unitarias. `RevisionVisualTest` se ejecutó mediante `adb shell am instrument -w -e class com.duoc.madertimber.ui.navigation.RevisionVisualTest -e perfil <perfil> com.duoc.madertimber.test/androidx.test.runner.AndroidJUnitRunner`: **1 prueba, 0 fallos en cada uno de los tres perfiles finales**. Verifica desplazamiento hasta las acciones, selección del año en el modal, visibilidad independiente de confirmación, apertura de las doce vistas y regreso desde las fichas al acceso. Un intento intermedio falló por un selector de prueba ambiguo entre la tarjeta y el chip de impacto; se corrigió el selector y se repitió. Las capturas iniciales tomadas durante transiciones se reemplazaron esperando también al compositor Android.

Perfiles verticales del mismo emulador Pixel_10_Pro API 37:

| Perfil | Resolución / densidad | Texto y tema | Resultado visual |
| --- | --- | --- | --- |
| Normal | 1280 × 2856 px / 480 dpi (≈427 × 952 dp) | 100 %, claro | Doce vistas, cuatro tarjetas, gráficos, modal, listas, fichas y formularios legibles. |
| Pequeño | 960 × 1920 px / 480 dpi (320 × 640 dp) | 150 %, claro | Formularios y tarjetas corregidos; contenido y acciones alcanzables por desplazamiento, etiquetas sin palabras partidas por los controles de contraseña. |
| Grande | 1800 × 2700 px / 320 dpi (900 × 1350 dp) | 100 %, oscuro | Doce vistas sin recortes esenciales; formulario centrado, listas y gráficos disponibles. |

Se revisaron las capturas, incluidos estados desplazados, errores y teclado del login. Los selectores horizontales permiten alcanzar las opciones fuera del área visible; las pantallas largas conservan desplazamiento vertical. La configuración original del emulador se restauró después. Son perfiles de un emulador, no tres dispositivos físicos.

Accesibilidad: imagen con descripción, barras con línea/año/valor/selección, histórico con años, asterisco y resumen textual, errores con mensaje además del color, controles con texto y roles de Compose. Se comprobaron los nombres de los controles de contraseña mediante semántica instrumentada. Contraste calculado por luminancia sRGB de las parejas principales del tema: blanco/verde **6,56:1**, texto/tarjeta clara **13,44:1**, texto/contenedor verde claro **10,13:1**, texto/botón oscuro **7,56:1**, texto/tarjeta oscura **10,29:1** y texto/contenedor verde oscuro **8,18:1**. No es una medición exhaustiva de cada píxel, una certificación de accesibilidad ni una prueba manual con TalkBack.

Revisión de mantenimiento: nombres y textos del flujo activo corresponden a los contenidos; README sigue describiendo la aplicación real. Las maquetas anteriores bajo `ui/screens/proyectos`, `investigadores` y `publicaciones` y el marcador `SeleccionFiltros.kt` no forman parte de `AppNavigation`; se conservan como trabajo previo, sin usarlas como evidencia de las doce vistas. No hubo reorganización de responsabilidades ni aprendizaje nuevo demostrado por el usuario.

Capturas: **24 por perfil, 72 PNG** en [normal](capturas/t15/normal), [pequeño con texto ampliado](capturas/t15/pequeno-texto150) y [grande oscuro](capturas/t15/grande-oscuro). Cada perfil conserva los mismos nombres; los sufijos indican posiciones desplazadas o estados auxiliares. Índice de las doce vistas en el perfil normal:

| Vista | Captura |
| --- | --- |
| 1. Dashboard | [Tarjetas](capturas/t15/normal/01-dashboard-indicadores.png) y [comparación](capturas/t15/normal/01-dashboard-comparacion.png) |
| 2. Filtros | [Modal](capturas/t15/normal/02-filtros.png) |
| 3. Detalle de línea | [Inicio](capturas/t15/normal/03-linea.png) y [histórico](capturas/t15/normal/03-linea-historico.png) |
| 4. Detalle de indicador | [Indicador](capturas/t15/normal/04-indicador.png) |
| 5. Proyectos | [Lista](capturas/t15/normal/5-lista-proyectos.png) |
| 6. Proyecto | [Ficha](capturas/t15/normal/6-ficha-proyectos.png) |
| 7. Investigadores | [Lista](capturas/t15/normal/7-lista-investigadores.png) |
| 8. Investigador | [Ficha](capturas/t15/normal/8-ficha-investigadores.png) |
| 9. Publicaciones | [Lista](capturas/t15/normal/9-lista-publicaciones.png) |
| 10. Publicación | [Ficha](capturas/t15/normal/10-ficha-publicaciones.png) |
| 11. Login | [Formulario](capturas/t15/normal/11-login.png) y [teclado](capturas/t15/normal/11-login-teclado.png) |
| 12. Registro | [Formulario](capturas/t15/normal/12-registro.png) y [texto ampliado](capturas/t15/pequeno-texto150/12-registro-inferior.png) |

T15 completada en el árbol local, pendiente de commit. T16 conserva la validación integral de ausencias, errores, restauración y demás casos funcionales; Room, importación nativa y evidencia de Git/Trello siguen en T18–T20. Este resultado no acredita entrega completa.

## Acceso y registro — T14 — 2026-10-29

Rama `main`, HEAD `d89b602` y árbol limpio antes de T14. `AuthScreen` conserva estado y eventos en `LoginViewModel`/`RegisterViewModel`, y validación en `AuthValidator`. Los formularios añaden acciones de teclado Siguiente/Listo, desplazamiento con teclado, foco liberado al mostrar/cerrar el aviso, botones de regreso y acceso invitado, visibilidad independiente de contraseña y confirmación, indicación de mínimo ocho caracteres y aviso demostrativo destacado. El tema aplica una paleta verde estable en claro/oscuro para coincidir con la referencia. `AppNavigation` evita que Atrás salga de registro mientras el aviso está abierto. El cambio de formulario y la entrada al panel limpian ambos estados de acceso.

Ejecutado con JDK 25.0.2 y el Wrapper: `:app:assembleDebug --quiet --max-workers=2`, **salida 0**. En emulador Pixel_10_Pro API 37, instalación y apertura correctas; se observaron errores al enviar campos vacíos, correo inválido, contraseña de siete caracteres y confirmación distinta. Login y registro válidos mostraron el modal sin crear cuenta. Atrás cerró el modal; tras ajustar el foco, el teclado quedó oculto y el formulario permaneció en registro. Se comprobó el desplazamiento con teclado, el acceso invitado desde registro y el regreso a login con campos vacíos. Capturas: [login](capturas/t14-login.png), [registro](capturas/t14-registro.png) y [aviso](capturas/t14-aviso.png). No se añadieron ni ejecutaron pruebas unitarias por instrucción del usuario. La revisión de las doce vistas y tamaños adicionales sigue en T15; T14 está pendiente de commit.

## Fichas relacionadas — T13 — 2026-10-29

Rama `main`, HEAD `a1e5380` y árbol limpio antes de T13. `FichaRelacionadaScreen` presenta el proyecto con título, línea, año, estado y avance del seguimiento seleccionado; el investigador con nombre, especialidad y vinculación a línea/año; y la publicación con título, tipo, año, línea y una nota explícita de registro ficticio sin fuente bibliográfica real. Las tres fichas conservan el botón de regreso a la lista. `AppNavigation` ya valida categoría, ID, línea, año y tipo antes de mostrar la ficha; un ID inválido abre un mensaje recuperable con Volver. La maqueta fija previa de investigador permanece fuera del flujo.

Ejecutado con JDK 25.0.2 y el Wrapper: `:app:assembleDebug --quiet --max-workers=2`, **salida 0**. Por instrucción del usuario se priorizó completar la vista y no se añadieron ni ejecutaron pruebas unitarias. No se hizo una nueva comprobación visual Android en ese incremento; sigue en T15. T13 quedó versionada en `d89b602`; el estado de T14 se registra arriba.

## Listas relacionadas — T12 — 2026-10-29

Rama `main`, HEAD `5679d20` y árbol limpio antes de T12. `RelacionadosScreen` usa las listas ya filtradas por `LocalPanelRepository`: proyectos activos, investigadores vinculados y publicaciones de la línea/año. Desde la línea ofrece las tres categorías; desde un indicador presenta solo las categorías aplicables según `categoriasRelacionadas`. Muestra el contexto aun sin cobertura, evita un total cero ficticio cuando no hay consulta, presenta totales y estados vacíos, y abre cada ficha por ID conservando la ruta de línea/año/tipo. Las tarjetas añaden un dato breve y una acción visible de apertura. La maqueta antigua de investigadores sigue fuera del flujo.

Ejecutado con JDK 25.0.2 y el Wrapper: `:app:assembleDebug --quiet --max-workers=2`, **salida 0**. Por instrucción del usuario se priorizó la vista funcional y no se añadieron ni ejecutaron pruebas unitarias. No se hizo una nueva comprobación visual Android para T12; la revisión integral sigue en T15. T12 quedó versionada en `a1e5380`; el estado posterior de T13 se registra arriba.

## Detalle de indicador — T11 — 2026-10-29

Rama `main`, HEAD `34e2554` y árbol limpio antes de T11. `DetalleIndicadorScreen` muestra el valor del año seleccionado con su unidad, la variación frente al año calendario anterior, el gráfico histórico de T09, una explicación breve según el tipo y la nota de datos demostrativos. Usa los resultados de `IndicadorCalculado` sin recalcular la variación en Compose. Distingue “Sin datos”, “Sin comparación” y “No calculable: base anterior cero”; presenta porcentajes o puntos porcentuales con un decimal y signo positivo cuando corresponde. El acceso a contenidos relacionados conserva línea, año y tipo mediante la ruta existente.

Ejecutado con JDK 25.0.2 y el Wrapper: `:app:assembleDebug --quiet --max-workers=2`, **salida 0**. Por instrucción del usuario se priorizó completar la vista y no se añadieron ni ejecutaron pruebas unitarias para T11. No se hizo una nueva comprobación visual Android en ese incremento; está prevista en la revisión de T15. T11 quedó versionada en `5679d20`; el estado posterior de T12 se registra arriba.

## Detalle de línea — T10 — 2026-10-29

Rama `main`, HEAD `e02e57b` y árbol limpio antes de T10. `DetalleLineaScreen` representa nombre, descripción, año, cinco indicadores calculados, selector del histórico, gráfico, conteos de proyectos activos/investigadores/publicaciones y accesos al indicador y a los contenidos relacionados. El ViewModel conserva la métrica del histórico separada de la comparación del dashboard, con proyectos activos como opción inicial; al cambiarla no altera línea ni año. La pantalla muestra “Sin datos” cuando la línea/año carece de cobertura, sin presentar ceros ni accesos vacíos. Las rutas mantienen el contexto y la selección elegida al abrir el indicador.

Ejecutado con JDK 25.0.2 y el Wrapper: `:app:assembleDebug :app:testDebugUnitTest :app:lintDebug :app:assembleDebugAndroidTest --quiet --max-workers=2`, **salida 0**. Tras retirar una prueba redundante a petición del usuario, `:app:testDebugUnitTest --quiet --max-workers=2` registró **54 pruebas, 0 fallos y 0 errores**. El ViewModel prueba selección, restauración y ausencia de cobertura. La primera ejecución Android registró **10 pruebas, 1 fallo**; las otras nueve, incluida la pantalla sin cobertura, pasaron. El registro que ejecutó el usuario mostró que el chip de publicaciones seguía sin seleccionar (`Selected = false`) y estaba fuera de la pantalla. La prueba ahora desplaza primero el gráfico, pulsa el chip y verifica la selección y el título: ejecutada aisladamente en emulador Pixel_10_Pro API 37, **1 prueba, 0 fallos**. No se repitió toda la suite tras ese ajuste. T10 quedó versionada en `34e2554`; el estado posterior de T11 se registra arriba.

## Gráfico histórico — T09 — 2026-10-29

Rama `main`, HEAD `a0e6c76` y árbol limpio antes de T09. `prepararHistorico` convierte el histórico calculado de una línea y métrica en puntos para cada año hasta el seleccionado; deja `null` en años sin valor y excluye años futuros. `DashboardViewModel` prepara los históricos junto con los indicadores. El componente reutilizable `GraficoHistorico` dibuja puntos y une solo años contiguos con valor, muestra unidad, años, cifras, marca de selección y resumen textual; con ausencia total presenta un mensaje. Está integrado en el detalle del indicador; el detalle de línea sigue en T10.

Ejecutado con JDK 25.0.2 y el Wrapper: `:app:assembleDebug :app:testDebugUnitTest :app:lintDebug :app:assembleDebugAndroidTest --quiet --max-workers=2`, **salida 0**. Los resultados JVM registran **53 pruebas, 0 fallos y 0 errores**. En emulador Pixel_10_Pro API 37, `:app:connectedDebugAndroidTest --quiet --max-workers=2` pasó **8 pruebas, 0 fallos y 0 errores**. Las nuevas pruebas comprueban huecos, ausencia total, un solo valor, exclusión de años futuros y renderizado en el detalle. Se abrió manualmente el detalle de proyectos activos de Construcción sustentable y se revisó una captura: 2022→4, 2023→6 y 2024→8 proyectos se leen completos, con el último año destacado. La prueba de huecos comprueba los puntos y etiquetas; no mide píxeles del trazo. T09 quedó versionada en `e02e57b`; el estado posterior de T10 se registra arriba.

## Filtros modales — T08 — 2026-10-29

Rama `main`, HEAD `29f9459` y árbol limpio antes de T08. El dashboard abre `FiltrosPanelDialog` con las líneas y años disponibles y la selección actual. Los cambios dentro del modal permanecen en un borrador local: Cancelar o cerrar con Atrás lo descarta. Aplicar emite un solo evento `FiltrosAplicados` con línea y año; `DashboardViewModel` valida ambos valores, los guarda en `SavedStateHandle` y actualiza la consulta. La métrica comparada permanece intacta. Los filtros rápidos de T07 siguen disponibles.

Ejecutado con JDK 25.0.2 y el Wrapper: `:app:assembleDebug :app:testDebugUnitTest :app:lintDebug :app:assembleDebugAndroidTest --quiet --max-workers=2`, **salida 0**. Los resultados JVM registran **50 pruebas, 0 fallos y 0 errores**. En emulador Pixel_10_Pro API 37, `:app:connectedDebugAndroidTest --quiet --max-workers=2` pasó **5 pruebas, 0 fallos y 0 errores**. Las pruebas nuevas comprueban Cancelar, Atrás y Aplicar, el filtro efectivo en detalle, el regreso al panel y la recreación de actividad; una prueba del ViewModel comprueba la restauración conjunta de línea, año y métrica. Esta comprobación no incluye todavía la persistencia entre sesiones, prevista para T18. T08 quedó versionada en `a0e6c76`; el estado posterior de T09 se registra arriba.

## Dashboard y comparación — T07 — 2026-10-29

Rama `main`, HEAD `c372e32` y árbol limpio antes de T07. `DashboardScreen` incorpora una imagen local de investigación en madera con descripción accesible, controles rápidos de línea/año que pueden ocultarse, transición animada de los valores de las tarjetas y animación de las barras al cambiar métrica o año. Las barras anuncian línea, métrica, año, valor y selección; una línea sin valor muestra “Sin datos” sin dibujar una barra de cero. Conserva las cinco métricas, cuatro tarjetas calculadas y acceso al detalle. El modal con aplicar/cancelar sigue en T08.

Ejecutado con JDK 25.0.2 y el Wrapper: `:app:assembleDebug :app:testDebugUnitTest :app:lintDebug :app:assembleDebugAndroidTest --console=plain --max-workers=2`, **BUILD SUCCESSFUL**. En emulador Pixel_10_Pro API 37, `:app:connectedDebugAndroidTest` pasó **3 pruebas, 0 fallos y 0 errores**, incluidas la navegación T06 y la accesibilidad/evento del dashboard. Se abrió la app como invitado y se revisaron capturas: la imagen se ve en el panel, y al cambiar la comparación de proyectos a publicaciones se actualizan las tres barras y la unidad sin cambiar las tarjetas de la línea seleccionada. La transición está implementada y se ejercitó en el emulador; las capturas son estados puntuales, no una medición de fotogramas o rendimiento.

El primer intento de pruebas Android falló antes de abrir las pantallas porque Espresso 3.5.1 buscó por reflexión `InputManager.getInstance`, ausente en el emulador. Se actualizó solo la dependencia de pruebas a Espresso 3.7.0; las [notas oficiales de AndroidX](https://developer.android.com/jetpack/androidx/releases/test) registran la corrección de ese acceso. La repetición terminó correctamente. T07 quedó versionada en `29f9459`; el estado posterior de T08 se registra arriba.

## Navegación del panel — T06 — 2026-10-29

Rama `main`, árbol limpio en `0fad2d0` antes de T06; ese commit integra T05 con las rutas añadidas por el equipo. Se incorporó Navigation Compose 2.10.2 desde el catálogo. `MainActivity` conserva inicialización y tema; `ui/navigation/Rutas.kt` construye destinos con IDs y año, y `AppNavigation.kt` monta el NavHost para acceso, dashboard, línea, indicador, relacionados y fichas. El regreso usa la pila del NavController; el DashboardViewModel ligado a la actividad conserva filtros en SavedStateHandle. Los formularios se limpian al cambiar entre acceso y panel o al volver/cerrar sesión.

Las rutas de detalle comprueban línea, año e indicador; la ruta de relacionados comprueba el tipo opcional, y cada ficha comprueba categoría e ID dentro de los datos filtrados. Argumentos inválidos muestran un mensaje y botón Volver. Las pantallas de línea e indicador muestran nombre/año y valor del contexto real. La pantalla relacionada ofrece las categorías aplicables y fichas básicas con datos del ViewModel; el contenido completo de T10–T14 sigue pendiente. Se conservaron las maquetas estáticas previas, fuera de este flujo.

Ejecutado con JDK 25.0.2 y el Wrapper: `:app:assembleDebug :app:testDebugUnitTest :app:lintDebug --console=plain --max-workers=2`, **BUILD SUCCESSFUL**. `ValidacionRutasPanelTest` registra **4 pruebas, 0 fallos, 0 errores y 0 omitidas** para contexto, IDs de fichas, categorías e indicador. La prueba instrumentada `AppNavigationTest` compila con `:app:assembleDebugAndroidTest`; no había dispositivo conectado según `adb devices`, por lo que no se ejecutó el recorrido visual ni esa prueba. T06 está implementada en código y pendiente de commit; el siguiente incremento es T07–T08, además de T18–T20 para la entrega.

## Estado MVVM del panel — T05 — 2026-10-28

Rama `main`, HEAD `4d7009e` y árbol limpio antes de T05; la rama coincidía con `origin/main`. `DashboardViewModel` recibe `LocalPanelRepository` y `SavedStateHandle`, carga el JSON en segundo plano y expone un `DashboardUiState` observable. Coordina los cinco indicadores, la comparación de tres líneas y los contenidos relacionados para el indicador seleccionado. Guarda línea, año, métrica de comparación e indicador en `SavedStateHandle`; inicia con 2024 y proyectos activos y valida las selecciones restauradas. Distingue carga, datos, conjunto vacío, falta de cobertura y error con reintento.

`DashboardScreen` ahora renderiza ese estado y envía eventos para cambiar línea, año y métrica. Las cuatro tarjetas y barras muestran valores derivados del Repository; el redondeo a un decimal ocurre solo al mostrar. `MainActivity` obtiene el ViewModel con una fábrica y vuelve a iniciar en login, como pide la especificación, en lugar de abrir directamente la lista de publicaciones. Las otras pantallas y su conexión de navegación siguen pendientes para T06–T14.

Se ejecutó con JDK 25.0.2 y el Wrapper `:app:assembleDebug :app:testDebugUnitTest :app:lintDebug --console=plain --max-workers=2`: **BUILD SUCCESSFUL**. El XML de `DashboardViewModelTest` registra **6 pruebas, 0 fallos, 0 errores y 0 omitidas**. Cubren carga, selección y comparación, falta de cobertura sin cero inventado, restauración y saneamiento de valores guardados, reintento tras error y conjunto vacío. La compilación y Lint pasaron; no se ejecutó la app ni se inspeccionó visualmente en dispositivo. La recreación de actividad no se probó de extremo a extremo: las pruebas reconstruyen el ViewModel con un SavedStateHandle que contiene la selección.

T05 está implementada en el árbol local y pendiente de commit. Próximo paso: T06, rutas y regreso entre pantallas, seguido de completar el dashboard y sus recursos visuales en T07–T08.

## Cálculos e históricos de T04 — 2026-10-28

Rama `main`, HEAD `f6344f3`, árbol limpio e igual a `origin/main` antes de T04. `CalculosIndicadores.kt` deriva desde entidades y cobertura los cinco valores anuales por línea, incluido avance promedio solo para proyectos activos e impacto demostrativo limitado por las metas 10/20/15. Conserva precisión completa y omite el valor de avance cuando no hay proyectos activos. `LocalPanelRepository` expone los indicadores del año solicitado con histórico, variación respecto de `t - 1` y la cobertura; también filtra contenidos relacionados por tipo de indicador usando las entidades que participaron en el cálculo. El gráfico puede usar el histórico hasta el año seleccionado sin mostrar valores posteriores.

La variación de conteos e impacto es porcentual cuando la base anterior es mayor que cero; con base cero devuelve el estado específico, incluso si ambos valores son cero. El avance se expresa como diferencia de puntos porcentuales y admite base cero. Si falta el valor actual o el del año calendario anterior, devuelve ausencia de comparación. Un año cubierto sin entidades produce conteos e impacto cero; uno sin cobertura no genera indicadores. Un error de carga no se transforma en cero.

Ejecutado con JDK 25.0.2 y el Wrapper: `:app:assembleDebug :app:testDebugUnitTest :app:lintDebug --console=plain --max-workers=2`, **BUILD SUCCESSFUL**. Los reportes XML registran **6 pruebas de cálculos y 5 del Repository, todas sin fallos, errores ni omisiones**. Se verificaron los impactos 78,0 / 54,0 / 96,0 de 2024 con el asset real, igualdad de conteos con listas relacionadas, selección de 2023 aunque exista 2024, proyecto finalizado excluido, huecos del histórico, año anterior exacto, avance ausente, base cero, signo de variación y saturación del impacto. No se ejecutó la app en dispositivo; el dashboard aún usa cifras fijas y su conexión MVVM corresponde a T05.

Al cerrar esta comprobación de T04, sus cambios seguían pendientes de commit. El usuario creó después `4d7009e`; el estado de T05 se describe en la sección superior.

## Fuente local y Repository de T03 — 2026-10-28

Rama `main`, HEAD `0dffc3d` y árbol limpio antes de T03; la rama local está un commit por delante de `origin/main`. T02 ya quedó registrada en ese commit. Se añadió `app/src/main/assets/cenamad_datos.json` con tres líneas, cobertura explícita de 2022–2024 para cada una, 23 proyectos, 36 investigadores y 91 publicaciones sintéticas. En 2024 las consultas contienen 8/5/10 proyectos activos, 15/11/18 publicaciones y 12/9/15 investigadores únicos para L1/L2/L3. El JSON almacena entidades y cobertura, sin conteos de indicadores independientes.

`LectorDatosPanel` usa Gson 2.14.0 en modo estricto y rechaza sintaxis o estructura inválida, campos obligatorios ausentes, relaciones inválidas según T02, cobertura repetida y entidades situadas fuera de la cobertura declarada. `LocalPanelRepository` abre el asset de Android o un `Reader` inyectado, y consulta proyectos, investigadores y publicaciones por línea/año. Distingue carga, datos disponibles, conjunto cubierto vacío, falta de cobertura y error de lectura o validación. La lectura desde el archivo local no requiere red; `AndroidManifest.xml` no solicita permiso de Internet. El dashboard todavía no consume el Repository y los cinco indicadores no se calculan aún: son trabajo de T04–T05.

Ejecución local con JDK 25.0.2 y el Wrapper: `:app:assembleDebug :app:testDebugUnitTest :app:lintDebug --console=plain --max-workers=2`, **BUILD SUCCESSFUL**. El reporte de `LocalPanelRepositoryTest` registra **8 pruebas, 0 fallos, 0 errores y 0 omitidas**. Incluyen lectura del asset real, cobertura y conteos 2024, estados de consulta, archivo ausente, JSON inválido, referencias, años fuera de cobertura, avances inválidos y formato JSON estricto. El APK se compiló y Lint pasó; no se ejecutó la app en dispositivo ni se comprobó visualmente el panel.

Al cerrar esta comprobación de T03, sus cambios seguían pendientes de commit. El usuario creó después `f6344f3`; el estado de T04 se describe en la sección superior.

## Cierre técnico de T02 — 2026-10-28

Rama `main`, HEAD `970735c` y árbol limpio antes de estos cambios. Los diez modelos de la sección 7 ya estaban versionados. Se completó `IntegridadDatos.kt` con `validarIntegridadDatos`, que devuelve errores con tipo y detalle para IDs repetidos por entidad, referencias de proyectos, investigadores, publicaciones e indicadores a líneas inexistentes, seguimientos repetidos por año, vinculaciones repetidas por `(lineaId, anio)`, avances fuera de 0–100 o no finitos, indicadores repetidos por `(lineaId, tipo)` y valores anuales repetidos. Se conservaron `tieneIdsDuplicados` y `tieneProyectosConLineaInexistente`.

Se añadieron pruebas de referencias válidas y listas vacías, un conjunto válido con límites 0 y 100, y casos inválidos de cada regla. Ejecutado con JDK 25.0.2 y el Wrapper: `:app:assembleDebug :app:testDebugUnitTest :app:lintDebug --console=plain --max-workers=2`, **BUILD SUCCESSFUL** en la comprobación final. El reporte XML de `IntegridadDatosTest` registra **13 pruebas, 0 fallos, 0 errores y 0 omitidas**. Estas comprobaciones validan compilación, pruebas JVM y Lint; no acreditan ejecución en dispositivo ni los flujos de las pantallas.

Al cerrar esta comprobación de T02, el commit todavía no se había ejecutado y T03 seguía pendiente. El usuario creó después el commit local `0dffc3d`; el estado de T03 se describe en la sección superior.

## Prueba de integridad — 2026-10-28

Rama `main`, HEAD `7a5fa87` y árbol limpio antes de que el usuario añadiera `app/src/test/java/com/duoc/madertimber/data/model/IntegridadDatosTest.kt`. La prueba local crea dos líneas con el mismo ID y llama a `assertTrue(tieneIdsDuplicados(lineas))`; el archivo de prueba está añadido con cambios locales.

El usuario ejecutó primero desde Android Studio `:app:testDebugUnitTest --tests com.duoc.madertimber.data.model.IntegridadDatosTest.detectaIdsRepetidos` y compartió `BUILD SUCCESSFUL`. El reporte local registró una prueba, cero fallos y cero errores. Después añadió `idsDistintosNoSonDuplicados` y `listaVaciaNoTieneIdsDuplicados`, ejecutando la clase completa tras cada incremento. Se inspeccionó `app/build/test-results/testDebugUnitTest/TEST-com.duoc.madertimber.data.model.IntegridadDatosTest.xml`: tres pruebas, cero fallos, cero errores y cero omitidas. Este resultado verifica los casos de IDs repetidos, distintos y lista vacía y la compilación necesaria para esa tarea; no acredita `assembleDebug`, Lint ni el flujo Android. Próximo control: referencias de proyectos a líneas existentes, seguido de las demás reglas de integridad.

Después, el usuario guardó en `IntegridadDatos.kt` la función `tieneProyectosConLineaInexistente`, a partir del código completo solicitado a Codex. La lectura muestra que compara cada `Proyecto.lineaId` con los IDs de líneas y devuelve `true` si encuentra una referencia ausente. Añadió el caso `detectaProyectoConLineaInexistente` y ejecutó la clase completa en Android Studio. El reporte XML local registra cuatro pruebas, cero fallos, cero errores y cero omitidas; el cuarto caso usa `lineaId = "L2"` con una sola línea `id = "L1"`. Los archivos tienen cambios locales. Falta comprobar referencia válida y lista vacía; esta ejecución tampoco acredita `assembleDebug`, Lint ni el flujo Android.

## Corte anterior del checkout — 2026-10-28

Rama `main`, HEAD `6c99e16`, árbol limpio al iniciar la revisión. Se leyeron archivos fuente y configuración sin consultar el remoto. Esta evidencia describe el checkout local; no certifica ejecución Android.

| Área | Evidencia por lectura | Pendiente |
| --- | --- | --- |
| Modelos del panel | Existen los diez contratos de la sección 7 bajo `data/model`. El 28/10 el usuario guardó `VinculacionInvestigador`, `Investigador` y `Publicacion` como archivos añadidos con cambios locales; se revisaron sus propiedades y tipos por lectura. También escribió `tieneIdsDuplicados` para IDs de líneas en `IntegridadDatos.kt`; tras corregir la comparación, su estructura coincide con los tres casos explicados por el usuario. | Probar la función y completar la validación de otros IDs, referencias, años y avances. T02 sigue abierta. |
| Vistas relacionadas | `ListaInvestigadoresScreen` presenta tres nombres fijos y `FichaInvestigadorScreen` muestra una ficha fija; ambas tienen previews y callbacks. El resto de archivos de listas, fichas, detalles y filtros son marcadores de paquete. | Conectar modelos, Repository, ViewModels y navegación; listas completas, selección por ID y regreso al contexto. T12–T13 siguen abiertas. |
| Navegación y panel | `MainActivity` conserva la selección local login/registro/dashboard; `ui/navegacion/navegacion.kt` solo declara el paquete. El dashboard sigue con cifras fijas. | Rutas del panel, filtros, datos calculados y estados MVVM. |
| Datos y pruebas | No hay JSON en assets ni Repository del panel; las pruebas fuente siguen siendo las de acceso y los ejemplos de plantilla. | Implementar datos, cálculos y sus pruebas; revisar interfaz y ejecución Android. |

Los modelos presentes coinciden por estructura con los contratos correspondientes de la sección 7 de la especificación, pero su presencia no demuestra integridad de conjuntos de datos; el aprendizaje comprobado se registra por separado en [progreso.md](progreso.md). Las pantallas de investigadores son maquetas estáticas aún no integradas; su existencia tampoco acredita las vistas funcionales del MVP.

Validación intentada en Windows el 2026-10-28: se inició `.\gradlew.bat :app:assembleDebug :app:testDebugUnitTest --console=plain` desde la raíz. Tras aproximadamente dos minutos sin salida, se interrumpió; `java -version` tampoco devolvió salida y se interrumpió. No se obtuvo resultado de compilación ni de pruebas, y la causa no está determinada. Antes de acreditar T02 o avanzar a datos, diagnosticar el arranque de Java/Gradle en este equipo y repetir la comprobación. `git diff --check` y los enlaces Markdown locales se revisaron sin errores para esta actualización documental.

## Mockup ampliado para revisión conjunta — 2026-10-22

El [plano de referencia](mockups/cenamad-plan-estudiantes.png) incorpora las doce vistas numeradas: conserva dashboard, filtros, detalles, listas y fichas, y añade inicio de sesión y registro. Incluye ejemplos auxiliares de validación, aviso demostrativo, carga, vacío, error y falta de cobertura, controles de regreso/cierre y el recorrido entre vistas. Los formularios conservan el alcance de demostración y la entrada como invitado; los errores de carga se refieren al archivo local.

Estado: revisado y autorizado para commit por el propietario el 2026-10-22. Tras su autorización de pasarlo todo a `main`, el commit `f16875b` se integró por avance directo y se subió a `origin/main`, sin conflictos. La ampliación es una referencia de diseño; no modifica código Kotlin ni configuración Gradle y no acredita implementación o validación Android de las doce vistas. Los requisitos de acceso y las aclaraciones del plano se mantienen en las secciones 2.2–2.3 de la especificación; T01 registra el cierre del diseño.

Se añade `docs/mockups/cenamad-mapa-navegacion.png` como imagen independiente, con las doce vistas ordenadas por recorrido y flechas de apertura/regreso. Se revisaron la correspondencia de números, la entrada como invitado, los filtros, las pestañas según indicador y el regreso de cada ficha a su lista. El enlace se mantiene en la sección 2.2 de la especificación; el mapa se incluye en el commit autorizado.

Validación de esta revisión: inspección visual de las doce vistas, los ejemplos auxiliares y el mapa de navegación; plano PNG de 1190 × 1322 píxeles y mapa de 1024 × 1536 píxeles guardados en sus rutas de referencia. Comprobados 40 enlaces locales de nueve Markdown, sin enlaces rotos; `git diff --check` sin errores. No se ejecutaron Gradle ni pruebas Android por tratarse de cambios de diseño y documentación.

## Cambio solicitado a SDK 38 — 2026-10-20

`app/build.gradle.kts` declara ahora `compileSdk = release(38)` y `targetSdk = 38`. Se mantienen `minSdk = 24`, AGP y Gradle Wrapper 9.6.0. Cambio solicitado y confirmado por el propietario, no motivado por un fallo de entorno.

Validación: `sdkmanager --list` consultó el catálogo remoto; aparecen plataformas 37.x y Canary, pero ninguna API 38. El SDK local contiene `android-37.0`. El intento de `:app:assembleDebug :app:testDebugUnitTest :app:lintDebug --max-workers=2 --console=plain` primero falló por restricciones de conexiones locales del sandbox. Se reintentó fuera del sandbox y se interrumpió durante configuración tras comprobar la ausencia de la plataforma solicitada. No hay build, pruebas ni Lint exitosos para este cambio. Pendiente instalar API 38 cuando esté disponible y repetir la validación, incluida la ejecución Android por el cambio de targetSdk. README actualizado; `git diff --check` sin errores.

## Corte de entrega histórico — 2026-10-20

Checkout revisado: rama `work`, HEAD `6c1c257`; árbol limpio antes de esta actualización. Se leyeron MainActivity, dashboard, acceso, validadores, pruebas y configuración Gradle, y se inspeccionó visualmente `docs/mockups/cenamad-plan-estudiantes.png`. No se consultó el remoto; este estado corresponde al checkout disponible.

| Referencia visual | Evidencia actual | Pendiente para entrega |
| --- | --- | --- |
| 1. Dashboard | Cuatro tarjetas y barras con constantes 2024. | Datos calculados, filtros, métrica seleccionable, resaltado y botón Ver línea. |
| 2. Filtros | No hay controles interactivos. | Selector/modal de línea y año, aplicar/cancelar y restauración. |
| 3–4. Detalles de línea e indicador | No hay pantallas ni ViewModels de detalle. | Indicadores, históricos, variación, interpretación y navegación. |
| 5, 7 y 9. Listas relacionadas | No implementadas. | Pestañas según origen, listas completas y conteos coherentes. |
| 6, 8 y 10. Fichas | No implementadas. | Proyecto, investigador y publicación con regreso al contexto. |
| 11–12. Login y registro (ampliación solicitada) | Formularios Compose, MVVM, validación, visibilidad de contraseña, aviso demo e invitado implementados. | Incorporarlos al diseño de referencia y comprobarlos visualmente en Android. |

En este corte del 20-10, el PNG contenía diez vistas; la ampliación a doce se preparó después para la revisión conjunta del 22-10 indicada arriba. No existen modelos del panel, JSON en assets, Repository ni DashboardViewModel. Hay dependencia explícita Lifecycle ViewModel; no hay dependencias explícitas de Navigation Compose ni lector JSON. MainActivity usa una selección local de tres pantallas, no el flujo de detalles del MVP.

Esta revisión actualiza documentación y planificación, no funcionalidad. No se ejecutaron Gradle, pruebas JVM ni pruebas Android. Los resultados históricos siguientes no acreditan una nueva ejecución ni la entrega completa. Las tareas y fechas propuestas están en [plan-implementacion.md](plan-implementacion.md#tarjetas-para-trello).

## Implementación observada en el corte histórico del 2026-10-20

| Área | Estado |
| --- | --- |
| Aplicación Android | Un módulo :app, launcher MainActivity, paquete com.duoc.madertimber y UI Compose/Material 3. |
| Acceso | Login y registro separan vistas, LoginViewModel/RegisterViewModel y AuthValidator. Estado observable y eventos; sin autenticación ni persistencia de credenciales. |
| Pantalla | DashboardScreen muestra título CENAMAD, línea Construcción sustentable, año 2024, etiqueta demostrativa, cuatro tarjetas y barras comparativas. Es una vista estática desplazable. |
| Tema | Paleta clara/oscura, colores dinámicos y tipografía de plantilla; identidad visual del producto pendiente. |
| Pruebas | Seis pruebas JVM de los ViewModels de acceso, más dos ejemplos de plantilla: suma e identidad del paquete. No hay pruebas de indicadores ni de comportamiento UI. |
| MVP/MVVM | Base visual del dashboard implementada con constantes de ejemplo. Faltan modelos, JSON, Repository, ViewModel, filtros interactivos, navegación, histórico y contenidos relacionados. |
| Configuración release | Backup con reglas de plantilla y optimización desactivada. No hay firma release propia; publicación no pertenece al MVP. |

Los resultados de ejecución de la tutoría están en [progreso.md](progreso.md). Código leído, build exitoso y comprensión del estudiante son evidencias diferentes.

## Separación MVVM del acceso — 2026-10-18

AuthScreen solo renderiza AuthUiState y envía AuthEvent. LoginViewModel y RegisterViewModel mantienen estados independientes y consultan AuthValidator para validar. MainActivity obtiene los ViewModels con ViewModelProvider y limpia los formularios al navegar. Sobreviven a cambios de configuración; las contraseñas no se serializan y se descartan al salir del formulario o terminar el proceso. El acceso sigue siendo demostrativo, sin Repository de cuentas ni servicio de autenticación. El dashboard aún no implementa MVVM.

Validación ejecutada: `:app:assembleDebug :app:testDebugUnitTest :app:lintDebug --max-workers=2 --console=plain`, resultado BUILD SUCCESSFUL. Siete pruebas JVM sin fallos (seis nuevas de acceso y una de plantilla). Lint: cero errores y 13 advertencias, incluida la sugerencia de actualizar la dependencia ViewModel declarada con la misma versión Lifecycle existente. `git diff --check` correcto. No se ejecutaron pruebas de UI ni revisión visual en dispositivo.

## Validación Cloud — 2026-10-18

Se añadieron LoginScreen y RegisterScreen en AuthScreen.kt. MainActivity abre el login; el registro y el panel de invitado regresan al login con Atrás. Los formularios comprueban correo, campos requeridos y, para registro, mínimo de ocho caracteres y confirmación de contraseña. Un envío válido explica que no se crea una cuenta ni se inicia sesión. Las contraseñas no se guardan con rememberSaveable.

Se ejecutó `bash ./gradlew :app:assembleDebug :app:testDebugUnitTest :app:lintDebug --max-workers=2 --console=plain`: BUILD SUCCESSFUL, 18 tareas ejecutadas y 33 reutilizadas. El primer intento dentro del sandbox no pudo iniciar Gradle por restricciones de red local; la ejecución autorizada fuera del sandbox completó los controles. APK generado, prueba JVM de plantilla correcta y Lint sin errores. La prueba de plantilla no valida los formularios. Pendiente revisión visual y pruebas de interacción en dispositivo Android; se incluyen previews de ambas pantallas.

## Validación Cloud — 2026-10-15

Se ejecutó `bash ./gradlew :app:assembleDebug :app:testDebugUnitTest :app:lintDebug --console=plain --max-workers=2` con JDK 25 y SDK 37. Resultado: BUILD SUCCESSFUL, 18 tareas ejecutadas y 33 reutilizadas. Se generó `app/build/outputs/apk/debug/app-debug.apk`; la prueba JVM de plantilla se ejecutó con 1 prueba, 0 fallos y 0 omitidas. Lint se ejecutó con 0 errores y 12 advertencias existentes sobre etiqueta redundante, versiones y recursos sin uso.

El dashboard es una práctica visual estática; esta prueba de aritmética no valida la UI ni cálculos de negocio. No se ejecutaron pruebas instrumentadas ni revisión visual Android en Cloud. Hay una preview para revisar en Android Studio. Se comprobaron enlaces Markdown locales y `git diff --check` sin errores.

## Git y cambios locales — antecedente del 2026-10-08

Al iniciar esta revisión: rama main, HEAD 30f29f9; la integración documental 49284ea está en el historial. MainActivity.kt tiene cambios preparados por el usuario: argumento Diego y val saludo. Se preservaron el archivo y el índice.

La consolidación documental posterior permanece local. No se hizo commit, push, fetch ni se verificó el estado remoto o la configuración Cloud durante esta revisión. Estos identificadores corresponden exclusivamente al antecedente del 2026-10-08; el checkout vigente se registra en el corte de entrega superior.

## Antecedentes de validación — 2026-10-08

Estos resultados proceden de registros anteriores conservados en la documentación. No se repitieron durante la revisión actual.

| Entorno / comprobación | Evidencia registrada y límite |
| --- | --- |
| Windows, primera descarga Gradle | El sandbox bloqueó una conexión; no llegó a evaluar el proyecto. |
| Windows, ruta no ASCII | AGP rechazó la ruta. El proyecto incorporó android.overridePathCheck=true; se registraron sync y assembleDebug exitosos posteriormente. |
| Windows, clon de prueba / AAPT2 | processDebugResources y el inicio directo del binario fallaron con 0xfffffffe. No se determinó la causa de sistema/runtime/caché. No acredita un bloqueo vigente tras el assemble posterior. |
| Clon local independiente | Recuperó el commit y el Wrapper; no demuestra acceso actual a GitHub ni compatibilidad de otros equipos. |
| Cloud, revisión previa | Se registraron APK y reportes existentes: una prueba JVM de plantilla, cero fallos; Lint, cero errores y 12 advertencias. Las tareas no se volvieron a ejecutar al documentarlo. |
| Android visual/instrumentado | Ejecución inicial registrada en tutoría. No consta validación visual del dashboard ni pruebas instrumentadas del MVP. |
| Exclusiones y secretos | Revisión básica previa de textos rastreados sin coincidencias sensibles; no es una auditoría del historial o del remoto. |

El fallo AAPT2 se diagnostica si se reproduce en el equipo actual. No inferir que falta un runtime por una sugerencia del mensaje de error ni cambiar versiones para ocultar un problema sin determinar su causa.

## Revisión documental del 2026-10-08

Se leyeron los documentos y el código afectado; se consolidaron fuentes, enlaces y estados. Comprobación de cierre: siete documentos en docs/; diez Markdown revisados incluyendo README y los dos AGENTS; cero enlaces locales rotos. Se conservaron 18 secciones de especificación, doce pasos técnicos, cinco iteraciones y doce temas de aprendizaje. git diff --check no informó errores en cambios preparados ni no preparados; MainActivity.kt conserva el hash previo y los cambios preparados del usuario. No se ejecutaron Gradle ni pruebas Android porque los cambios son documentales.

Para futuras validaciones, registrar fecha, entorno, comando, resultado y límite de lo comprobado. Una tarea UP-TO-DATE reutiliza resultados y no equivale a ejecutarlos de nuevo.
