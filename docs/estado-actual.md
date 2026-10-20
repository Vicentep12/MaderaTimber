# Estado técnico y validación

Revisión por lectura del repositorio y mockup: 2026-10-20. Última ejecución Gradle registrada: 2026-10-18 (no repetida en esta revisión documental). Los antecedentes Windows del 2026-10-08 se conservan abajo. Comandos y herramientas requeridas en [README](../README.md); responsabilidades en [arquitectura.md](arquitectura.md); tareas abiertas en [plan-implementacion.md](plan-implementacion.md).

## Corte de entrega — 2026-10-20

Checkout revisado: rama `work`, HEAD `6c1c257`; árbol limpio antes de esta actualización. Se leyeron MainActivity, dashboard, acceso, validadores, pruebas y configuración Gradle, y se inspeccionó visualmente `docs/mockups/cenamad-plan-estudiantes.png`. No se consultó el remoto; este estado corresponde al checkout disponible.

| Referencia visual | Evidencia actual | Pendiente para entrega |
| --- | --- | --- |
| 1. Dashboard | Cuatro tarjetas y barras con constantes 2024. | Datos calculados, filtros, métrica seleccionable, resaltado y botón Ver línea. |
| 2. Filtros | No hay controles interactivos. | Selector/modal de línea y año, aplicar/cancelar y restauración. |
| 3–4. Detalles de línea e indicador | No hay pantallas ni ViewModels de detalle. | Indicadores, históricos, variación, interpretación y navegación. |
| 5, 7 y 9. Listas relacionadas | No implementadas. | Pestañas según origen, listas completas y conteos coherentes. |
| 6, 8 y 10. Fichas | No implementadas. | Proyecto, investigador y publicación con regreso al contexto. |
| 11–12. Login y registro (ampliación solicitada) | Formularios Compose, MVVM, validación, visibilidad de contraseña, aviso demo e invitado implementados. | Incorporarlos al diseño de referencia y comprobarlos visualmente en Android. |

El PNG todavía contiene diez vistas: la ampliación gráfica a doce sigue pendiente. No existen modelos del panel, JSON en assets, Repository ni DashboardViewModel. Hay dependencia explícita Lifecycle ViewModel; no hay dependencias explícitas de Navigation Compose ni lector JSON. MainActivity usa una selección local de tres pantallas, no el flujo de detalles del MVP.

Esta revisión actualiza documentación y planificación, no funcionalidad. No se ejecutaron Gradle, pruebas JVM ni pruebas Android. Los resultados históricos siguientes no acreditan una nueva ejecución ni la entrega completa. Las tareas y fechas propuestas están en [plan-implementacion.md](plan-implementacion.md#tarjetas-para-trello).

## Implementación observada

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
