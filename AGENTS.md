# Instrucciones para Codex: Cenamad / MaderaTimber

## Leer al comenzar

1. Trabajar desde esta raíz Git (`MaderaTimber`), no desde su carpeta contenedora.
2. Leer `docs/contexto.md`; consultar `docs/estado-actual.md` y `docs/pendientes.md` para recuperar el progreso.
3. Revisar `git status --short`, la rama y los cambios locales antes de editar. No sobrescribir trabajo ajeno.
4. Antes de cambios importantes, leer `docs/arquitectura.md`, las decisiones relevantes de `docs/decisiones.md` y el código afectado.

## Proyecto y alcance confirmado

Cenamad es el nombre indicado por el propietario para este proyecto Android. El repositorio se llama MaderaTimber; el código usa `MaderTimber` y el paquete `com.duoc.madertimber`. La relación funcional entre estos nombres y los objetivos de negocio todavía no están definidos en el repositorio. No inventar requisitos a partir del nombre.

El objetivo acordado de este sistema documental es conservar conocimiento técnico y progreso entre equipos y sesiones. La aplicación actual es una base inicial que muestra `Hello Android!`. Los requisitos del producto deben incorporarse cuando el propietario los confirme.

## Arquitectura y convenciones

- Aplicación Android nativa en Kotlin, un único módulo Gradle `:app`, una `ComponentActivity` y UI declarativa con Jetpack Compose / Material 3.
- Entrada: `app/src/main/java/com/duoc/madertimber/MainActivity.kt`. Tema: `ui/theme/`. Recursos y manifiesto: `app/src/main/res/` y `app/src/main/AndroidManifest.xml`.
- No existen actualmente capas de dominio/datos, ViewModel, navegación, API, base de datos ni inyección de dependencias. No describir MVVM o Clean Architecture como implementadas.
- Mantener estilo oficial Kotlin, indentación de cuatro espacios, nombres existentes, funciones composables y parámetro `Modifier` donde corresponda. Los archivos `.kt` están en `src/.../java/`; no trasladarlos solo por su extensión.
- Configuración con Gradle Kotlin DSL y catálogo `gradle/libs.versions.toml`; declarar allí nuevas versiones. Usar el Wrapper versionado, no un Gradle global.
- AGP 9 usa soporte Kotlin integrado; no agregar `org.jetbrains.kotlin.android` por asumir que falta. La versión del plugin Compose y la del compilador integrado deben verificarse por separado.
- Mantener la identidad `com.duoc.madertimber` y las variantes de nombre existentes hasta acordar un cambio explícito.

## Entorno y verificación

Instalación y versiones declaradas: `README.md`. El daemon solicita JDK 25; `compileOptions` Java 11 describe el código compilado, no el JDK con que ejecutar Gradle. SDK de compilación/objetivo: API 37; mínimo del dispositivo: API 24.

Desde la raíz, en PowerShell:

```powershell
.\gradlew.bat --version
.\gradlew.bat :app:assembleDebug :app:testDebugUnitTest :app:lintDebug --console=plain
# Solo con dispositivo/emulador conectado:
.\gradlew.bat :app:connectedDebugAndroidTest
```

En Linux/macOS, usar `bash ./gradlew` con los mismos argumentos; evita depender del permiso ejecutable del archivo, actualmente versionado como `100644`.

Para cambios de código/build, ejecutar comprobaciones pertinentes. Para documentación/configuración de Git, revisar enlaces, exactitud, exclusiones y `git diff --check`. Informar qué se ejecutó, resultado y cualquier bloqueo; nunca presentar análisis estático como prueba ejecutada. No inventar cobertura: las pruebas existentes son ejemplos de plantilla.

## Reglas al modificar

- Mantener el alcance autorizado. No refactorizar, eliminar archivos, renombrar paquetes o cambiar arquitectura solo para documentar.
- No publicar, hacer push, merge ni cambiar remotos sin autorización explícita. Un commit local tampoco equivale a sincronización con otros equipos.
- Nunca versionar credenciales, tokens, `.env`, rutas SDK, almacenes de firma o cachés. `.gitignore` no protege archivos ya rastreados; revisar también `git ls-files`. No imprimir valores sensibles en informes.
- No cambiar versiones/SDK para ocultar fallos del entorno. Registrar la causa observada y proponer el cambio con su impacto.
- Incorporar pruebas de comportamiento cuando se implemente funcionalidad; evitar pruebas que solo repitan el código.

## Mantener el contexto en la misma entrega

- Tras una funcionalidad o corrección significativa, actualizar `docs/estado-actual.md` con comportamiento, evidencia de validación y limitaciones; mover/completar las tareas correspondientes en `docs/pendientes.md`.
- Registrar decisiones arquitectónicas nuevas en `docs/decisiones.md`: ID, fecha, estado, contexto, decisión, justificación, alternativas y consecuencias. Si la motivación histórica no está documentada, decirlo.
- Actualizar `docs/arquitectura.md` si cambian componentes o flujos; actualizar `README.md` si cambian tecnologías, dependencias, configuración o comandos.
- Mantener `docs/contexto.md` como resumen breve de arranque: situación actual, decisiones esenciales, bloqueos y siguiente paso. No copiar documentos completos ni historiales de chat.
- Distinguir **confirmado** (código, comando o instrucción explícita), **inferencia** (indicar evidencia) y **propuesta** (no aprobada/implementada). Fechar verificaciones para que un resultado antiguo no parezca vigente.
- Registrar trabajo parcial útil antes de cerrar una sesión: qué falta, archivos afectados y cómo retomar. No afirmar terminado algo sin verificarlo.
- Evitar duplicación: versiones/comandos en README, arquitectura en su documento, decisiones en su registro, estado/pruebas en estado actual y tareas en pendientes. Usar enlaces entre ellos.
- Mantener documentos concisos; reemplazar estados obsoletos, conservar decisiones relevantes y apoyarse en Git para el historial detallado. No depender de memoria de conversaciones.

## Documentación complementaria

- `README.md`: instalación, uso desde otro equipo, GitHub y Codex Cloud.
- `docs/arquitectura.md`: estructura y responsabilidades reales.
- `docs/decisiones.md`: decisiones observadas y política documental acordada.
- `docs/estado-actual.md`: funcionalidades, problemas y validación.
- `docs/pendientes.md`: tareas priorizadas con criterios de cierre.
- `docs/contexto.md`: entrada rápida para cualquier sesión.
