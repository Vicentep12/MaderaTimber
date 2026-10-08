# Estado actual

Revisión: **2026-10-08**, checkout Cloud en rama `work`, HEAD `32bc0f2`. Aplicación base de `3dcf635`; documentación previa ya presente en Git. El usuario autorizó guardar esta integración del plan/directrices mediante un commit local en esta rama. Push y sincronización a Windows pendientes.

## Integración del plan y evidencia Cloud

MaderaTimber implementa el panel CENAMAD y debe usar MVVM, según instrucciones del usuario recuperadas de chats locales. Se prioriza la próxima lección guiada: pantalla parcial con datos demostrativos. [Plan](../PLAN_IMPLEMENTACION.md), [aprendizaje](../APRENDIZAJE.md), [progreso](../PROGRESO.md) y [directrices Cloud](directrices-nube.md) incorporados; MVP/MVVM todavía no implementados.

Originales en `E:\AnalistaProgramador\Desarrollo app moviles`, ruta confirmada por el usuario, no montada aquí. La carpeta MVP integrada contiene una síntesis de acuerdos, pendiente de contraste con su README original. No se editaron archivos Windows ni la configuración publicada del servicio. Se amplió el START local externo al repositorio para leer las directrices; eso no cambia el start_skill publicado.

Estado Cloud observado: conectado/en ejecución, observaciones actuales y red restringida aplicada. Sin secretos/variables/identidades configurados en su ficha. Helpers externos preparan JDK/SDK/variables. El registro local `/workspace/.maderatimber-env/VALIDATION.md`, APK y reportes XML existentes del 2026-10-08 acreditan build inicial, una prueba de plantilla (0 fallos) y Lint (0 errores, 12 advertencias). AAPT2 Linux funcionó según ese registro. No se volvió a compilar en esta entrega de documentación. No hay emulador/KVM ni pruebas instrumentadas o revisión visual Cloud acreditadas.

Los antecedentes Windows del registro inferior pertenecen a la revisión previa: no son comprobaciones ejecutadas en esta sesión. El chat de aprendizaje registró una Greeting modificada y ejecución inicial Windows; este checkout aún muestra `Hello Android!`.

## Funcionalidad real

| Área | Estado | Evidencia / límite |
| --- | --- | --- |
| Base Android | Implementada en código | Módulo `:app`, launcher y `MainActivity`. Ejecución en dispositivo no verificada en esta revisión. |
| Pantalla inicial | Implementada en código | Saludo `Hello Android!`, Scaffold y edge-to-edge; no es una pantalla de negocio Cenamad. |
| Tema | Implementado en código | Material 3, modo oscuro/claro, colores dinámicos en API 31+, tipografía y preview. Recursos de plantilla, sin diseño de producto confirmado. |
| Pruebas | Parcial | Dos ejemplos: aritmética e identidad del paquete. No hay pruebas de negocio ni de comportamiento UI. |
| Build portable | Build inicial Cloud acreditado por registro/artefactos; sync/assemble Windows registrados previamente. | Otro clon/entorno requiere validación. |
| Backup y release | Configuración inicial | Backup habilitado con XML de ejemplo; optimización release desactivada, sin firma release propia. No es una entrega productiva validada. |
| Contexto entre sesiones | Contexto previo versionado; integración nueva local | Plan común, aprendizaje/progreso, especificación MVP y directrices Cloud; sincronización pendiente. |
| Codex Cloud | Entorno preparado y conectado | Build/prueba JVM/Lint iniciales acreditados por registro y artefactos; restauración/visual/instrumentación pendientes. |

**No implementado en este árbol:** pantalla/funcionalidad del panel, MVVM, formularios o interacción de usuario, navegación, autenticación, API/backend, persistencia, modelos de dominio, ViewModel, DI y CI. Esta lista describe ausencias: panel/MVVM son requisitos confirmados; backend, autenticación y DI no se derivan automáticamente de ellos.

## Problemas, límites e información faltante

- **Producto:** alcance general, MVVM y próxima lección confirmados. Contrastar el README original para fórmulas, detalle de indicadores y aceptación completa.
- **Nombres:** CENAMAD es el producto y MaderaTimber su repositorio Android; conservar `MaderTimber`/paquete existentes. No hay cambio de identidad autorizado.
- **Reproducibilidad:** un clon local independiente recuperó el commit y los archivos correctamente; no se comprobó clonación desde GitHub ni en otro SO. Tampoco consta la versión original de Android Studio. El Wrapper está rastreado como `100644`; usar `bash ./gradlew` en POSIX. Finales de línea fijados mediante `.gitattributes` para los futuros clones que incorporen esta entrega.
- **Bloqueo Windows por caracteres no ASCII (Resuelto):** AGP rechazaba la ruta del proyecto por contener caracteres no ASCII (`Móviles`). Se agregó `android.overridePathCheck=true` a `gradle.properties`, permitiendo la sincronización de Gradle y la compilación exitosa de `:app:assembleDebug`.
- **Bloqueo AAPT2 confirmado en el clon:** `:app:processDebugResources` falló al iniciar el daemon `aapt2-9.4.1-15978811-windows`, durante la transformación de recursos de la dependencia resuelta `androidx.core:core:1.16.0`. Gradle sugiere revisar Windows Universal C Runtime; esto no prueba que falte. Ejecutar directamente el binario también falló con error de inicio `0xfffffffe`. Falta distinguir instalación/runtime, restricciones de ejecución y ubicación de la caché (que en esta sesión sigue bajo la ruta con tildes). No se instaló un runtime Windows ni se cambió la aplicación para ocultar el fallo.
- **Entorno de la revisión Windows previa:** Java 25.0.1 disponible; `JAVA_HOME`, `ANDROID_HOME` y `ANDROID_SDK_ROOT` inicialmente ausentes y sin `local.properties`. Existe una carpeta SDK local; sus paquetes no pudieron enumerarse dentro del sandbox inicial, por lo que su contenido no está certificado por esa comprobación.
- **Seguridad:** comprobación básica de nombres sensibles y patrones de secretos en textos rastreados: sin coincidencias. No se auditó el historial completo, los archivos de configuración personales fuera del proyecto ni el contenido remoto. No se requieren secretos de aplicación actualmente.
- **Remoto en la revisión Windows previa:** `origin` configurado para `Vicentep12/MaderaTimber`; rama local `main` sigue `origin/main`. Esta es la referencia guardada localmente, no una confirmación de que GitHub siga igual. No se ejecutó fetch/push ni se verificó acceso de la cuenta.

## Registro de validación

| Fecha | Comprobación | Resultado |
| --- | --- | --- |
| 2026-10-08 | Lectura de todo el código Kotlin, builds, catálogo, manifiesto, recursos XML, reglas y pruebas; inventario de archivos rastreados/ocultos | Base de un módulo y UI de ejemplo confirmadas. |
| 2026-10-08 | `git status`, rama/remoto locales, `git fsck --no-reflogs` | Árbol inicialmente limpio; integridad local sin errores reportados. |
| 2026-10-08 | `git ls-files` + búsqueda de nombres sensibles/patrones en textos | Cero coincidencias en la comprobación básica; no equivale a auditoría exhaustiva. |
| 2026-10-08 | `gradlew.bat --version --no-daemon` dentro del sandbox | Falló al descargar Gradle por `SocketException: Permission denied: getsockopt`; no llegó a evaluar el proyecto. |
| 2026-10-08 | `:app:assembleDebug :app:testDebugUnitTest :app:lintDebug --no-daemon --console=plain` con acceso de descarga y SDK local | Gradle 9.6.0 descargado; fallo al aplicar AGP por la ruta con caracteres no ASCII. No se ejecutaron las tareas solicitadas. Caché aislada en `.gradle-user-home/`. |
| 2026-10-08 | Mismas tareas con `'-Pandroid.overridePathCheck=true'` temporal | Superó el chequeo de ruta y preparó Build Tools 36.0.0 usando licencias locales ya aceptadas. Se interrumpió para validar desde un clon sin tildes; ninguna de las tres tareas quedó acreditada. No se cambió configuración del proyecto. |
| 2026-10-08 | `git clone --no-hardlinks` a directorio temporal independiente | Commit `3dcf635` recuperado, árbol limpio, código y Wrapper JAR presentes. Solo incluye archivos ya confirmados, no esta documentación todavía sin commit. Se comprobó también checkout con finales LF para el script POSIX. |
| 2026-10-08 | Enlaces Markdown locales y exclusiones con `git check-ignore --no-index` | Enlaces válidos. `.env`/variantes, SDK local, firmas, XML del IDE, build y caché ignorados; ejemplos `.env.example` sin secretos, documentación y Wrapper JAR no ignorados. |
| 2026-10-08 | Build, prueba local y Lint desde clon Windows sin tildes | `BUILD FAILED` en `:app:processDebugResources` por inicio del daemon AAPT2; 32 tareas ejecutadas, sin APK ni resultado de prueba local/Lint acreditados. El clon utilizó la caché aislada de esta sesión, ubicada bajo la ruta original. |
| 2026-10-08 | Inicio directo de `aapt2.exe version` | Falló al iniciar el proceso con error `0xfffffffe`; no devolvió una versión. Causa de sistema/entorno sin confirmar. |
| 2026-10-08 | `:app:processDebugResources --offline --no-daemon` desde el clon | Reprodujo el mismo fallo de inicio de AAPT2 sin nuevas descargas (1 tarea ejecutada, 13 al día). |
| 2026-10-08 | Revisión final de documentos, enlaces, LF y `git diff --check` | Correcta; cambios entregables limitados a documentación, `.gitignore` y `.gitattributes`. Código/builds de aplicación sin cambios. |
| 2026-10-08 | Adición de `android.overridePathCheck=true` a `gradle.properties` y ejecución de Gradle sync y `:app:assembleDebug` | Sincronización exitosa y compilación `:app:assembleDebug` completada con éxito. |

Al modificar código o resolver un bloqueo, reemplazar los estados afectados y añadir el resultado pertinente con fecha. Diferenciar compilación, Lint, prueba local, prueba instrumentada y ejecución visual; el éxito de una no acredita las demás.

## Validación de esta integración documental

2026-10-08: revisión de acuerdos mediante chats locales, inventario del checkout, estado Cloud y registro/artefactos de configuración. Sin cambios de código/dependencias. Verificación de cierre: 13 documentos Markdown, cero enlaces locales rotos y `git diff --check` sin errores. Lectura de XML existentes: JVM 1 prueba/0 fallos/0 errores/0 omitidas; Lint 0 errores/12 advertencias; APK presente. No se ejecutaron nuevamente estas tareas Gradle. No se equipara esta integración local con sincronización a Windows, push o publicación de configuración Cloud.

2026-10-08: el usuario solicitó crear el commit de la integración documental. Se revisaron cambios y `git diff --check` antes de guardarlos; no se autorizó ni ejecutó push en este paso. Consultar el historial Git para el identificador del commit.
