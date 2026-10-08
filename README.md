# Cenamad / MaderaTimber

Aplicación Android del panel de indicadores CENAMAD, con MVVM obligatoria para la asignatura. Contratos y alcance en [especificacion.md](docs/especificacion.md); implementación y validación en [estado-actual.md](docs/estado-actual.md).

El propietario usa el nombre **Cenamad**; GitHub/carpeta usan **MaderaTimber**, mientras `rootProject.name`, el tema y la etiqueta Android usan **MaderTimber**. Se conservan estas identidades sin renombrar código. Paquete e identificador: `com.duoc.madertimber`.

## Documentación y continuidad

[AGENTS.md](AGENTS.md) establece cómo trabajar. Consultar cada tema en su fuente:

| Documento | Contenido |
| --- | --- |
| [Especificación](docs/especificacion.md) | Contratos, modelos, cálculos, plan técnico completo, aceptación, pruebas y entregables. |
| [Plan operativo](docs/plan-implementacion.md) | Etapas, tareas abiertas y próxima actividad. |
| [Aprendizaje](docs/aprendizaje.md) | Perfil, método y recorrido pedagógico completo. |
| [Progreso](docs/progreso.md) | Ejercicios y comprensión comprobada. |
| [Arquitectura](docs/arquitectura.md) | Componentes actuales y responsabilidades objetivo. |
| [Estado técnico](docs/estado-actual.md) | Implementación y evidencia de validación. |
| [Decisiones](docs/decisiones.md) | Acuerdos que afectan arquitectura y mantenimiento. |

Para un chat nuevo en el clon actualizado:

> Lee AGENTS.md y retoma la próxima actividad de docs/plan-implementacion.md después de consultar docs/progreso.md. Guíame paso a paso: yo escribo el código.

Para continuar en otro equipo, sincronizar código y documentación por Git con autorización y comprobar el entorno Android. La instalación y la configuración Cloud se describen abajo.

## Tecnologías declaradas

Fuente de verdad: archivos Gradle; esta tabla no certifica que todas las versiones sean compatibles ni que se hayan resuelto.

| Componente | Configuración | Fuente |
| --- | --- | --- |
| Plataforma | Android; `compileSdk` 37, `targetSdk` 37, `minSdk` 24 | `app/build.gradle.kts` |
| Gradle Wrapper | 9.6.0, distribución con SHA-256 | `gradle/wrapper/gradle-wrapper.properties` |
| Android Gradle Plugin | 9.4.1 | `gradle/libs.versions.toml` |
| Kotlin Compose plugin | 2.2.10 | `gradle/libs.versions.toml` |
| JDK del daemon | 25; URLs de toolchain para distintos SO | `gradle/gradle-daemon-jvm.properties` |
| Compatibilidad de código Java | 11 (source/target); distinta del JDK del daemon | `app/build.gradle.kts` |
| Compose | BOM 2026.02.01; UI, graphics, preview y Material 3 | Catálogo y módulo `app` |
| AndroidX | Core KTX 1.10.1; Lifecycle Runtime KTX 2.6.1; Activity Compose 1.8.0 | Catálogo |
| Pruebas | JUnit 4.13.2; AndroidX JUnit 1.1.5; Espresso 3.5.1; Compose UI test | Catálogo y módulo `app` |
| Resolución de toolchains | Foojay resolver convention 1.0.0 | `settings.gradle.kts` |

AGP incorpora soporte Kotlin desde la versión 9. La variable `kotlin` del catálogo configura el plugin Compose; no prueba la versión efectiva del compilador integrado. Ver [documentación oficial Android](https://developer.android.com/build/migrate-to-built-in-kotlin).

Repositorios de dependencias: Google Maven, Maven Central y Gradle Plugin Portal. Configuration Cache activada; heap Gradle configurado en 2 GB. Un único módulo `:app`. No hay backend, servicios externos ni variables `.env` requeridas por el código actual.

## Instalación en otro computador

1. Instalar Git, un JDK 25 y Android Studio compatible con las versiones declaradas de AGP/SDK. La versión exacta de Android Studio usada originalmente no consta.
2. En Windows elegir una ruta sin tildes u otros caracteres no ASCII, por ejemplo `C:/dev/MaderaTimber`: la comprobación de AGP se ha desactivado en este proyecto mediante `android.overridePathCheck=true`, pero una ruta ASCII simplifica el diagnóstico de herramientas. Clonar el remoto configurado (requiere permiso si el repositorio es privado):

   ```powershell
   git clone https://github.com/Vicentep12/MaderaTimber.git C:/dev/MaderaTimber
   cd C:/dev/MaderaTimber
   git status --short
   ```

3. Abrir **esa carpeta** en Android Studio y Codex. En Windows el usuario trabaja bajo `E:/AnalistaProgramador/Desarrollo app moviles`; la carpeta contenedora no es la raíz Git. Los documentos de continuidad de esta entrega se conservan dentro del repositorio.
4. En SDK Manager instalar Android SDK Platform 37, Platform Tools y las Build Tools que requiera AGP (36.0.0 solicitadas durante esta revisión). Aceptar licencias. No usar API 24 como plataforma de compilación: es solo el mínimo para dispositivos.
5. Configurar el JDK para Gradle y la ubicación local del SDK. Android Studio puede crear `local.properties`; debe permanecer ignorado. Alternativa de terminal (reemplazar rutas por las de tu equipo):

   ```powershell
   $env:JAVA_HOME = 'C:/ruta/al/jdk-25'
   $env:ANDROID_HOME = 'C:/ruta/al/Android/Sdk'
   .\gradlew.bat --version
   .\gradlew.bat :app:assembleDebug :app:testDebugUnitTest :app:lintDebug --console=plain
   ```

   Estas variables duran solo esa terminal; configurar cada equipo por separado. Si se usa `ANDROID_SDK_ROOT` además de `ANDROID_HOME`, ambas deben señalar el mismo SDK.
6. Permitir descargas de Gradle, Google Maven, Maven Central, plugins y, si es necesario, toolchains. No copiar cachés ni `local.properties` desde otro equipo.
7. Ejecutar con **Run** en Android Studio, seleccionando un emulador o dispositivo Android API 24 o superior.

En Linux/macOS, con JDK/SDK ya instalados:

```bash
export JAVA_HOME=/ruta/al/jdk-25
export ANDROID_HOME=/ruta/al/Android/Sdk
bash ./gradlew --version
bash ./gradlew :app:assembleDebug :app:testDebugUnitTest :app:lintDebug --console=plain
```

`gradlew` está versionado sin permiso ejecutable; `bash ./gradlew` funciona sin cambiar el índice Git. `.gitattributes` fija LF para este script y Markdown, y CRLF para el script Windows.

## Compilar, probar e instalar

| Acción | PowerShell desde la raíz |
| --- | --- |
| APK debug | `.\gradlew.bat :app:assembleDebug` |
| Prueba local de ejemplo | `.\gradlew.bat :app:testDebugUnitTest` |
| Análisis Android Lint | `.\gradlew.bat :app:lintDebug` |
| Instalar en dispositivo conectado | `.\gradlew.bat :app:installDebug` |
| Prueba instrumentada | `.\gradlew.bat :app:connectedDebugAndroidTest` |

APK esperado: `app/build/outputs/apk/debug/app-debug.apk`; reportes de pruebas: `app/build/reports/tests/`; reportes Lint: `app/build/reports/`. Estos son resultados generados, no archivos a versionar. Para tareas Linux/macOS sustituir el ejecutor por `bash ./gradlew`.

Solo hay dos pruebas de plantilla: suma `2 + 2` e identidad del paquete. Las dependencias Compose Test no significan que existan pruebas de la interfaz. Consultar [estado actual](docs/estado-actual.md) para los resultados realmente obtenidos.

**Antecedentes Windows:** hubo rechazos por rutas no ASCII y fallo de inicio AAPT2; después se registró sync/assemble exitosos con `android.overridePathCheck=true`. No se repitieron pruebas Windows aquí. En Cloud el registro de configuración y artefactos existentes acreditan APK, una prueba de plantilla sin fallos y Lint sin errores con 12 advertencias. Ejecución visual e instrumentada Cloud pendientes; detalle y procedencia en [estado actual](docs/estado-actual.md).

## GitHub y archivos locales

Git está inicializado; el estado observado de rama y cambios locales se registra en [estado-actual.md](docs/estado-actual.md). Comprobarlo de nuevo antes de editar o sincronizar.

`.gitignore` excluye cachés, builds, configuración local del SDK, archivos privados del IDE, `.env`, credenciales habituales y firmas. No se detectaron nombres sensibles ni patrones de secretos en los archivos de texto rastreados revisados; es una comprobación básica del árbol actual, no una auditoría de todo el historial. El Wrapper JAR sí debe conservarse.

La configuración XML local de `.idea/` también queda ignorada; se conserva su `.gitignore` rastreado. No transportar rutas JDK/SDK particulares del IDE como configuración del equipo completo.

Antes de compartir, revisar `git status --short`, `git diff`, `git diff --check`, `git ls-files` y el contenido que se vaya a incluir. Si un secreto ya está rastreado, ignorarlo no lo retira de Git: informar, rotarlo si corresponde y acordar su retirada sin borrarlo de improviso.

Entre equipos: guardar código **y documentación** en el mismo commit; hacer push solo cuando esté autorizado; en el siguiente equipo usar `git pull --ff-only` con el árbol limpio. Si hay ramas divergentes o cambios sin guardar, revisar antes de integrar; no usar reset forzado. No editar simultáneamente la misma rama desde dos equipos sin coordinar o utilizar ramas separadas.

## Codex Cloud: configuración y continuidad

Guía consultada en la revisión Cloud anterior del 2026-10-08: [Codex Cloud](https://learn.chatgpt.com/docs/cloud) y [entornos Cloud](https://learn.chatgpt.com/docs/environments/cloud-environments). La interfaz y el acceso dependen de tu cuenta. Esa revisión registró un entorno conectado; no se verificó su disponibilidad durante la unificación local Windows. Tutoría y siguiente actividad: [plan operativo](docs/plan-implementacion.md).

1. Revisar los cambios locales y subirlos a GitHub cuando lo autorices; Cloud necesita la documentación en el repositorio remoto.
2. Iniciar sesión en ChatGPT; en web/escritorio seleccionar **Work in > Cloud > Select environment > Create environment** (también desde Settings > Codex Cloud > Environments).
3. Conectar GitHub si se solicita y dar acceso al repositorio `Vicentep12/MaderaTimber`. Si no eres su propietario, disponer del permiso correspondiente; para cuentas de organización puede hacer falta autorización administrativa.
4. Seleccionar el repositorio y **Get started**. Solicitar que el entorno prepare JDK 25, SDK Android API 37, licencias, Build Tools requeridas por AGP y las variables `JAVA_HOME`/`ANDROID_HOME`; usar la raíz que contenga `settings.gradle.kts`.
5. Indicar como comprobación los comandos Linux de este README. Preparar el entorno antes de publicar su configuración; registrar las versiones efectivas y cualquier fallo en `docs/estado-actual.md`.
6. Revisar el acceso de red para Gradle, Maven, plugins y Android SDK. Si Foojay descarga un JDK, verificar `api.foojay.io` y el proveedor al que redirige; el listado de dominios debe ajustarse a errores observados. Ningún token de la app es necesario actualmente.
7. Revisar resultados y configuración, guardar y seleccionar **Publish** del entorno Cloud. Esto lo haces manualmente; no equivale a publicar la app Android. Iniciar una nueva tarea con el mensaje de continuidad anterior.
8. Revisar cambios/pruebas antes de cualquier commit o PR. Actualizar y volver a publicar la configuración del entorno si cambian herramientas/dependencias; la documentación versionada sigue siendo el contexto compartido entre equipos.

**Compatibilidad condicionada:** el proyecto tiene Wrapper y scripts para Linux. El registro y artefactos locales de configuración acreditan una compilación inicial en Cloud; restaurar otro entorno o usar otro equipo requiere verificar su instalación. Lectura/edición pueden realizarse; compilación, Lint y pruebas locales requieren provisionar JDK/SDK y acceso a dependencias. No asumir que la imagen Cloud trae Android SDK ni emulador. Las pruebas instrumentadas y la verificación visual necesitan un dispositivo/emulador; si Cloud no lo proporciona, realizarlas localmente y registrar el resultado.

Para preparar paquetes desde Android SDK Command-Line Tools, si el entorno dispone de `sdkmanager` (la instalación Cloud publica API 37 como `android-37.0`; verificar el identificador en la lista):

```bash
sdkmanager --list
sdkmanager --licenses
sdkmanager "platform-tools" "platforms;android-37.0" "build-tools;36.0.0"
# Revisar Build Tools si cambia AGP; 36.0.0 fue requerida en esta revisión.
```

Android ahora documenta `android sdk` como sucesor de `sdkmanager`; los comandos anteriores corresponden a la herramienta tradicional todavía documentada. Ver [gestión oficial del SDK](https://developer.android.com/tools/sdkmanager). Aceptar las licencias al configurar tu entorno; no guardar SDK/licencias en Git.

### Helpers del entorno Cloud previamente preparado

La revisión Cloud anterior registró helpers externos al checkout en /workspace/.maderatimber-env. Solo si existen en el entorno donde se retoma el trabajo:

    cd /workspace/MaderaTimber
    bash /workspace/.maderatimber-env/configure-trust.sh
    source /workspace/.maderatimber-env/activate.sh
    bash ./gradlew --version --console=plain
    bash ./gradlew :app:assembleDebug :app:testDebugUnitTest :app:lintDebug --max-workers=4 --console=plain

Estas rutas no pertenecen al repositorio ni garantizan que los helpers estén disponibles en otro entorno. Si faltan, preparar JDK/SDK conforme a la instalación anterior. Mantener el proxy, TLS y checksums del entorno. Registrar resultados reales en estado-actual.md; una tarea UP-TO-DATE reutiliza resultados.

Configurar el inicio de las nuevas tareas para leer AGENTS.md desde la raíz Git. Editar estos documentos no modifica la configuración publicada del servicio. Las pruebas instrumentadas requieren un dispositivo/emulador disponible.
