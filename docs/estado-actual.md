# Estado técnico y validación

Última comprobación técnica: 2026-10-28. Los cortes anteriores y los antecedentes Windows del 2026-10-08 se conservan abajo como históricos. Comandos y herramientas requeridas en [README](../README.md); responsabilidades en [arquitectura.md](arquitectura.md); tareas abiertas en [plan-implementacion.md](plan-implementacion.md).

## Fuente local y Repository de T03 — 2026-10-28

Rama `main`, HEAD `0dffc3d` y árbol limpio antes de T03; la rama local está un commit por delante de `origin/main`. T02 ya quedó registrada en ese commit. Se añadió `app/src/main/assets/cenamad_datos.json` con tres líneas, cobertura explícita de 2022–2024 para cada una, 23 proyectos, 36 investigadores y 91 publicaciones sintéticas. En 2024 las consultas contienen 8/5/10 proyectos activos, 15/11/18 publicaciones y 12/9/15 investigadores únicos para L1/L2/L3. El JSON almacena entidades y cobertura, sin conteos de indicadores independientes.

`LectorDatosPanel` usa Gson 2.14.0 en modo estricto y rechaza sintaxis o estructura inválida, campos obligatorios ausentes, relaciones inválidas según T02, cobertura repetida y entidades situadas fuera de la cobertura declarada. `LocalPanelRepository` abre el asset de Android o un `Reader` inyectado, y consulta proyectos, investigadores y publicaciones por línea/año. Distingue carga, datos disponibles, conjunto cubierto vacío, falta de cobertura y error de lectura o validación. La lectura desde el archivo local no requiere red; `AndroidManifest.xml` no solicita permiso de Internet. El dashboard todavía no consume el Repository y los cinco indicadores no se calculan aún: son trabajo de T04–T05.

Ejecución local con JDK 25.0.2 y el Wrapper: `:app:assembleDebug :app:testDebugUnitTest :app:lintDebug --console=plain --max-workers=2`, **BUILD SUCCESSFUL**. El reporte de `LocalPanelRepositoryTest` registra **8 pruebas, 0 fallos, 0 errores y 0 omitidas**. Incluyen lectura del asset real, cobertura y conteos 2024, estados de consulta, archivo ausente, JSON inválido, referencias, años fuera de cobertura, avances inválidos y formato JSON estricto. El APK se compiló y Lint pasó; no se ejecutó la app en dispositivo ni se comprobó visualmente el panel.

T03 está implementada en el árbol local y pendiente de commit. Próximo paso: T04, derivar los cinco indicadores, históricos y variaciones desde las listas validadas, sin almacenar resultados manuales en el JSON.

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
