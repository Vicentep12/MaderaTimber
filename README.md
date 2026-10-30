# Cenamad / MaderaTimber

Aplicación Android del panel de indicadores CENAMAD, con MVVM obligatoria para la asignatura. Contratos y alcance en [especificacion.md](docs/especificacion.md).

Su público principal son estudiantes que quieren explorar líneas de investigación, comprender su evolución por años y consultar proyectos, investigadores y publicaciones relacionados. El enfoque de comprensión y las vistas de referencia están en las secciones 2.1–2.2 de la especificación.

La pantalla inicial presenta un login de demostración, con acceso al registro y al panel como invitado. Ambos formularios separan vistas, estado/eventos en ViewModels y reglas en AccesoValidador; validan los campos, permiten mostrar u ocultar cada contraseña y responden al teclado. Un envío válido abre un aviso modal; no autentican ni crean cuentas. El tema utiliza una paleta verde fija en modo claro y oscuro. El dashboard muestra cuatro indicadores calculados, una imagen temática y una comparación animada de las tres líneas para el año elegido. Ofrece filtros rápidos y un modal de línea/año con Aplicar y Cancelar. La navegación abre detalles, contenidos relacionados y fichas con línea/año conservados. El detalle de línea muestra cinco indicadores y un histórico seleccionable; el detalle del indicador muestra valor, variación, interpretación e histórico anual. Las listas relacionadas ofrecen categorías, totales y acceso a fichas de proyecto, investigador y publicación con datos de la selección.

La fuente local del panel está en `app/src/main/assets/cenamad_datos.json`. `LocalPanelRepository` la lee, valida y obtiene los cinco indicadores por línea/año; `DashboardViewModel` entrega esos resultados a la vista y conserva la selección durante recreaciones.

El nombre de la aplicación es **MaderaTimber** (etiqueta Android, tema, `rootProject.name`, repositorio y carpeta); el panel pertenece a **Cenamad**. Paquete e identificador: `com.duoc.madertimber`.

## Integrantes

- Diego Roble (usuario de GitHub: DiegoRobleM)
- Vicente Pérez (usuario de GitHub: Vicentep12)
- Agatha Ruiz (usuario de GitHub: VictoriaRuiz007)

## Funcionalidades implementadas

- Inicio de sesión y registro de demostración con formularios validados: íconos por campo, mensajes de error y aviso modal. Guardan nombre y correo en Room; la contraseña nunca se guarda.
- Dashboard con imagen de portada, filtros (modal y rápidos), cuatro indicadores y comparación entre líneas.
- Detalle de línea y de indicador, con gráfico histórico, variación respecto al año anterior y explicación.
- Listas y fichas de proyectos, investigadores y publicaciones relacionados.
- Persistencia local con Room de la última selección del panel (línea, año y métrica).
- Dos recursos nativos: cámara (foto de apoyo, con permiso y FileProvider) y selector de archivos del sistema (demostrativo, no sube nada).
- Animaciones: transiciones entre pantallas, cambio de valores en las tarjetas y barras de comparación.
- Arquitectura MVVM: vistas Compose, ViewModels con estado y eventos, repositorios y modelo con las reglas.

Los pasos para ejecutar el proyecto están en las secciones «Instalación en otro computador» y «Compilar e instalar».

## Documentación

| Documento | Contenido |
| --- | --- |
| [Especificación](docs/especificacion.md) | Contratos, modelos, cálculos, plan técnico completo, aceptación y entregables. |
| [Arquitectura](docs/arquitectura.md) | Componentes actuales y responsabilidades por capa (MVVM). |

Para continuar en otro equipo, sincronizar el código por Git y comprobar el entorno Android. La instalación se describe abajo.

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
| Compose | BOM 2026.02.01; UI, graphics, animation, preview y Material 3 | Catálogo y módulo `app` |
| AndroidX | Core KTX 1.10.1; Lifecycle Runtime KTX 2.6.1; Activity Compose 1.8.0 | Catálogo |
| Pruebas | No incluidas: se retiraron el 29-10-2026 junto con sus librerías porque la asignatura no las exige | — |
| Lectura JSON | Gson 2.14.0 | Catálogo y módulo `app` |
| Carga asíncrona | Lifecycle ViewModel KTX 2.6.1; Kotlin coroutines Android 1.10.2 | Catálogo y módulo `app` |
| Navegación | Navigation Compose 2.10.2 | Catálogo y módulo `app` |
| Persistencia | Room 2.8.5 y KSP 2.3.12 | Catálogo y módulo `app`; guarda la última selección del panel (T18) y el nombre y correo del usuario del login |
| Recursos nativos | Cámara (contrato TakePicture, permiso CAMERA y FileProvider) y selector de archivos (OpenDocument, sin permisos; demostrativo) | `ui/screens/camara` y `ui/screens/archivo` |
| Resolución de toolchains | Foojay resolver convention 1.0.0 | `settings.gradle.kts` |

AGP incorpora soporte Kotlin desde la versión 9. La variable `kotlin` del catálogo configura el plugin Compose; no prueba la versión efectiva del compilador integrado. Ver [documentación oficial Android](https://developer.android.com/build/migrate-to-built-in-kotlin).

Repositorios de dependencias: Google Maven, Maven Central y Gradle Plugin Portal. Configuration Cache activada; heap Gradle configurado en 2 GB. Un único módulo `:app`. No hay backend, servicios externos ni variables `.env` requeridas por el código actual.

**SDK del proyecto: API 37.** API 38 no está disponible y no es un requisito de instalación. El commit `b2ea407` restableció `compileSdk` y `targetSdk` a 37; esta configuración se comprobó por lectura en `main` remoto el 22-10-2026. Gradle Wrapper permanece en 9.6.0 y `minSdk` en 24.

## Instalación en otro computador

1. Instalar Git, un JDK 25 y Android Studio compatible con las versiones declaradas de AGP/SDK. La versión exacta de Android Studio usada originalmente no consta.
2. En Windows elegir una ruta sin tildes u otros caracteres no ASCII, por ejemplo `C:/dev/MaderaTimber`: la comprobación de AGP se ha desactivado en este proyecto mediante `android.overridePathCheck=true`, pero una ruta ASCII simplifica el diagnóstico de herramientas. Clonar el remoto configurado (requiere permiso si el repositorio es privado):

   ```powershell
   git clone https://github.com/Vicentep12/MaderaTimber.git C:/dev/MaderaTimber
   cd C:/dev/MaderaTimber
   git status --short
   ```

3. Abrir **esa carpeta** en Android Studio. En Windows el usuario trabaja bajo `E:/AnalistaProgramador/Desarrollo app moviles`; la carpeta contenedora no es la raíz Git.
4. En SDK Manager instalar Android SDK Platform 37.0 (API 37), Platform Tools y las Build Tools que requiera AGP (36.0.0 solicitadas durante esta revisión). Aceptar licencias. No usar API 24 como plataforma de compilación: es solo el mínimo para dispositivos.
5. Configurar el JDK para Gradle y la ubicación local del SDK. Android Studio puede crear `local.properties`; debe permanecer ignorado. Alternativa de terminal (reemplazar rutas por las de tu equipo):

   ```powershell
   $env:JAVA_HOME = 'C:/ruta/al/jdk-25'
   $env:ANDROID_HOME = 'C:/ruta/al/Android/Sdk'
   .\gradlew.bat --version
   .\gradlew.bat :app:assembleDebug :app:lintDebug --console=plain
   ```

   Estas variables duran solo esa terminal; configurar cada equipo por separado. Si se usa `ANDROID_SDK_ROOT` además de `ANDROID_HOME`, ambas deben señalar el mismo SDK.
6. Permitir descargas de Gradle, Google Maven, Maven Central, plugins y, si es necesario, toolchains. No copiar cachés ni `local.properties` desde otro equipo.
7. Ejecutar con **Run** en Android Studio, seleccionando un emulador o dispositivo Android API 24 o superior.

En Linux/macOS, con JDK/SDK ya instalados:

```bash
export JAVA_HOME=/ruta/al/jdk-25
export ANDROID_HOME=/ruta/al/Android/Sdk
bash ./gradlew --version
bash ./gradlew :app:assembleDebug :app:lintDebug --console=plain
```

`gradlew` está versionado sin permiso ejecutable; `bash ./gradlew` funciona sin cambiar el índice Git. `.gitattributes` fija LF para este script y Markdown, y CRLF para el script Windows.

## Compilar e instalar

| Acción | PowerShell desde la raíz |
| --- | --- |
| APK debug | `.\gradlew.bat :app:assembleDebug` |
| Análisis Android Lint | `.\gradlew.bat :app:lintDebug` |
| Instalar en dispositivo conectado | `.\gradlew.bat :app:installDebug` |

APK esperado: `app/build/outputs/apk/debug/app-debug.apk`; reportes Lint: `app/build/reports/`. Estos son resultados generados, no archivos a versionar. Para tareas Linux/macOS sustituir el ejecutor por `bash ./gradlew`.

El repositorio no incluye pruebas automatizadas: se retiraron el 29-10-2026 porque la asignatura no las exige. La validación se hace compilando, con Lint y revisando la app en emulador o dispositivo.

**Antecedentes Windows:** hubo rechazos por rutas no ASCII y fallo de inicio AAPT2; después se registró sync/assemble exitosos con `android.overridePathCheck=true`. No se repitieron pruebas Windows aquí.

## GitHub y archivos locales

Git está inicializado. Comprobar la rama y los cambios locales antes de editar o sincronizar.

`.gitignore` excluye cachés, builds, configuración local del SDK, archivos privados del IDE, `.env`, credenciales habituales y firmas. No se detectaron nombres sensibles ni patrones de secretos en los archivos de texto rastreados revisados; es una comprobación básica del árbol actual, no una auditoría de todo el historial. El Wrapper JAR sí debe conservarse.

La configuración XML local de `.idea/` también queda ignorada; se conserva su `.gitignore` rastreado. No transportar rutas JDK/SDK particulares del IDE como configuración del equipo completo.

Antes de compartir, revisar `git status --short`, `git diff`, `git diff --check`, `git ls-files` y el contenido que se vaya a incluir. Si un secreto ya está rastreado, ignorarlo no lo retira de Git: informar, rotarlo si corresponde y acordar su retirada sin borrarlo de improviso.

Entre equipos: guardar código **y documentación** en el mismo commit; hacer push solo cuando esté autorizado; en el siguiente equipo usar `git pull --ff-only` con el árbol limpio. Si hay ramas divergentes o cambios sin guardar, revisar antes de integrar; no usar reset forzado. No editar simultáneamente la misma rama desde dos equipos sin coordinar o utilizar ramas separadas.
