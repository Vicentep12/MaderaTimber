# Estado técnico y validación

Revisión de implementación Cloud: 2026-10-18. Los antecedentes Windows del 2026-10-08 se conservan abajo. Comandos y herramientas requeridas en [README](../README.md); responsabilidades en [arquitectura.md](arquitectura.md); tareas abiertas en [plan-implementacion.md](plan-implementacion.md).

## Implementación observada

| Área | Estado |
| --- | --- |
| Aplicación Android | Un módulo :app, launcher MainActivity, paquete com.duoc.madertimber y UI Compose/Material 3. |
| Acceso | Login y registro de demostración con validación local, visibilidad de contraseña, navegación y acceso como invitado. Sin autenticación ni persistencia de credenciales. |
| Pantalla | DashboardScreen muestra título CENAMAD, línea Construcción sustentable, año 2024, etiqueta demostrativa, cuatro tarjetas y barras comparativas. Es una vista estática desplazable. |
| Tema | Paleta clara/oscura, colores dinámicos y tipografía de plantilla; identidad visual del producto pendiente. |
| Pruebas | Dos ejemplos de plantilla: suma e identidad del paquete. No hay pruebas de indicadores ni de comportamiento UI. |
| MVP/MVVM | Base visual del dashboard implementada con constantes de ejemplo. Faltan modelos, JSON, Repository, ViewModel, filtros interactivos, navegación, histórico y contenidos relacionados. |
| Configuración release | Backup con reglas de plantilla y optimización desactivada. No hay firma release propia; publicación no pertenece al MVP. |

Los resultados de ejecución de la tutoría están en [progreso.md](progreso.md). Código leído, build exitoso y comprensión del estudiante son evidencias diferentes.

## Validación Cloud — 2026-10-18

Se añadieron LoginScreen y RegisterScreen en AuthScreen.kt. MainActivity abre el login; el registro y el panel de invitado regresan al login con Atrás. Los formularios comprueban correo, campos requeridos y, para registro, mínimo de ocho caracteres y confirmación de contraseña. Un envío válido explica que no se crea una cuenta ni se inicia sesión. Las contraseñas no se guardan con rememberSaveable.

Se ejecutó `bash ./gradlew :app:assembleDebug :app:testDebugUnitTest :app:lintDebug --max-workers=2 --console=plain`: BUILD SUCCESSFUL, 18 tareas ejecutadas y 33 reutilizadas. El primer intento dentro del sandbox no pudo iniciar Gradle por restricciones de red local; la ejecución autorizada fuera del sandbox completó los controles. APK generado, prueba JVM de plantilla correcta y Lint sin errores. La prueba de plantilla no valida los formularios. Pendiente revisión visual y pruebas de interacción en dispositivo Android; se incluyen previews de ambas pantallas.

## Validación Cloud — 2026-10-15

Se ejecutó `bash ./gradlew :app:assembleDebug :app:testDebugUnitTest :app:lintDebug --console=plain --max-workers=2` con JDK 25 y SDK 37. Resultado: BUILD SUCCESSFUL, 18 tareas ejecutadas y 33 reutilizadas. Se generó `app/build/outputs/apk/debug/app-debug.apk`; la prueba JVM de plantilla se ejecutó con 1 prueba, 0 fallos y 0 omitidas. Lint se ejecutó con 0 errores y 12 advertencias existentes sobre etiqueta redundante, versiones y recursos sin uso.

El dashboard es una práctica visual estática; esta prueba de aritmética no valida la UI ni cálculos de negocio. No se ejecutaron pruebas instrumentadas ni revisión visual Android en Cloud. Hay una preview para revisar en Android Studio. Se comprobaron enlaces Markdown locales y `git diff --check` sin errores.

## Git y cambios locales — antecedente del 2026-10-08

Al iniciar esta revisión: rama main, HEAD 30f29f9; la integración documental 49284ea está en el historial. MainActivity.kt tiene cambios preparados por el usuario: argumento Diego y val saludo. Se preservaron el archivo y el índice.

La consolidación documental posterior permanece local. No se hizo commit, push, fetch ni se verificó el estado remoto o la configuración Cloud durante esta revisión. Estos identificadores describen esta comprobación y deben actualizarse cuando cambie el checkout.

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
