# Cenamad / MaderaTimber

Base de una aplicación Android nativa. **Estado funcional actual:** pantalla `Hello Android!`, tema Compose y pruebas de ejemplo. Los objetivos de negocio, usuarios y funcionalidades de Cenamad aún no están especificados en este repositorio; no hay una aplicación de negocio terminada.

El propietario usa el nombre **Cenamad**; GitHub/carpeta usan **MaderaTimber**, mientras `rootProject.name`, el tema y la etiqueta Android usan **MaderTimber**. Se conservan estas identidades sin renombrar código. Paquete e identificador: `com.duoc.madertimber`.

## Documentación y continuidad

Leer [AGENTS.md](AGENTS.md) y [contexto rápido](docs/contexto.md) para empezar con Codex. Complementos: [arquitectura](docs/arquitectura.md), [decisiones](docs/decisiones.md), [estado y verificaciones](docs/estado-actual.md), [pendientes](docs/pendientes.md).

Solicitud para una sesión nueva, abierta en la raíz del clon actualizado:

> Revisa AGENTS.md y docs/contexto.md, comprende el estado actual de Cenamad y continuemos desarrollando el proyecto.

El contexto viaja con los archivos versionados. Para que otro equipo o Codex Cloud reciba cambios locales, primero deben revisarse, confirmarse en Git y subirse con autorización. El historial de chats no reemplaza estos documentos.

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
2. En Windows elegir una ruta sin tildes u otros caracteres no ASCII, por ejemplo `C:/dev/MaderaTimber`: AGP rechaza la ruta original que contiene `Móviles`. Clonar el remoto configurado (requiere permiso si el repositorio es privado):

   ```powershell
   git clone https://github.com/Vicentep12/MaderaTimber.git C:/dev/MaderaTimber
   cd C:/dev/MaderaTimber
   git status --short
   ```

3. Abrir **esa carpeta** en Android Studio y Codex. En este equipo está dentro de la carpeta del proyecto semestral; esa carpeta superior no es un repositorio.
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

**Bloqueos observados en esta revisión:** AGP rechaza la ruta original Windows con tildes; además, el build desde un clon sin tildes falló al iniciar AAPT2. Gradle sugiere revisar Windows Universal C Runtime, pero la causa no se ha confirmado. Verificar el ejecutable/runtime y usar también una ubicación de caché Gradle sin tildes al diagnosticar. No se acredita APK, prueba local ni Lint exitosos hasta resolverlo; no se cambiaron dependencias ni código para ocultar el error.

## GitHub y archivos locales

Git ya está inicializado. En la revisión del 2026-10-08: rama `main`, seguimiento local `origin/main`, HEAD de aplicación `3dcf635`; `origin` apunta al repositorio mostrado en el comando de clonación. No se ha consultado el remoto para certificar su estado actual o tus permisos.

`.gitignore` excluye cachés, builds, configuración local del SDK, archivos privados del IDE, `.env`, credenciales habituales y firmas. No se detectaron nombres sensibles ni patrones de secretos en los archivos de texto rastreados revisados; es una comprobación básica del árbol actual, no una auditoría de todo el historial. El Wrapper JAR sí debe conservarse.

La configuración XML local de `.idea/` también queda ignorada; se conserva su `.gitignore` rastreado. No transportar rutas JDK/SDK particulares del IDE como configuración del equipo completo.

Antes de compartir, revisar `git status --short`, `git diff`, `git diff --check`, `git ls-files` y el contenido que se vaya a incluir. Si un secreto ya está rastreado, ignorarlo no lo retira de Git: informar, rotarlo si corresponde y acordar su retirada sin borrarlo de improviso.

Entre equipos: guardar código **y documentación** en el mismo commit; hacer push solo cuando esté autorizado; en el siguiente equipo usar `git pull --ff-only` con el árbol limpio. Si hay ramas divergentes o cambios sin guardar, revisar antes de integrar; no usar reset forzado. No editar simultáneamente la misma rama desde dos equipos sin coordinar o utilizar ramas separadas.

## Codex Cloud: pasos manuales

Guía verificada el 2026-10-08 en [Codex Cloud](https://learn.chatgpt.com/docs/cloud) y [entornos Cloud](https://learn.chatgpt.com/docs/environments/cloud-environments). La interfaz y el acceso dependen de tu cuenta; no se ha creado un entorno en esta tarea.

1. Revisar los cambios locales y subirlos a GitHub cuando lo autorices; Cloud necesita la documentación en el repositorio remoto.
2. Iniciar sesión en ChatGPT; en web/escritorio seleccionar **Work in > Cloud > Select environment > Create environment** (también desde Settings > Codex Cloud > Environments).
3. Conectar GitHub si se solicita y dar acceso al repositorio `Vicentep12/MaderaTimber`. Si no eres su propietario, disponer del permiso correspondiente; para cuentas de organización puede hacer falta autorización administrativa.
4. Seleccionar el repositorio y **Get started**. Solicitar que el entorno prepare JDK 25, SDK Android API 37, licencias, Build Tools requeridas por AGP y las variables `JAVA_HOME`/`ANDROID_HOME`; usar la raíz que contenga `settings.gradle.kts`.
5. Indicar como comprobación los comandos Linux de este README. Preparar el entorno antes de publicar su configuración; registrar las versiones efectivas y cualquier fallo en `docs/estado-actual.md`.
6. Revisar el acceso de red para Gradle, Maven, plugins y Android SDK. Si Foojay descarga un JDK, verificar `api.foojay.io` y el proveedor al que redirige; el listado de dominios debe ajustarse a errores observados. Ningún token de la app es necesario actualmente.
7. Revisar resultados y configuración, guardar y seleccionar **Publish** del entorno Cloud. Esto lo haces manualmente; no equivale a publicar la app Android. Iniciar una nueva tarea con el mensaje de continuidad anterior.
8. Revisar cambios/pruebas antes de cualquier commit o PR. Actualizar y volver a publicar la configuración del entorno si cambian herramientas/dependencias; la documentación versionada sigue siendo el contexto compartido entre equipos.

**Compatibilidad condicionada:** el proyecto tiene Wrapper y scripts para Linux, pero no hay validación de una compilación en Cloud. Lectura/edición pueden realizarse; compilación, Lint y pruebas locales requieren provisionar JDK/SDK y acceso a dependencias. No asumir que la imagen Cloud trae Android SDK ni emulador. Las pruebas instrumentadas y la verificación visual necesitan un dispositivo/emulador; si Cloud no lo proporciona, realizarlas localmente y registrar el resultado.

Para preparar paquetes desde Android SDK Command-Line Tools, si el entorno dispone de `sdkmanager`:

```bash
sdkmanager --list
sdkmanager --licenses
sdkmanager "platform-tools" "platforms;android-37" "build-tools;36.0.0"
# Revisar Build Tools si cambia AGP; 36.0.0 fue requerida en esta revisión.
```

Android ahora documenta `android sdk` como sucesor de `sdkmanager`; los comandos anteriores corresponden a la herramienta tradicional todavía documentada. Ver [gestión oficial del SDK](https://developer.android.com/tools/sdkmanager). Aceptar las licencias al configurar tu entorno; no guardar SDK/licencias en Git.
