# Directrices para continuar CENAMAD en la nube

Actualización: 2026-10-08. Estas instrucciones versionadas completan el arranque técnico del entorno y conservan los acuerdos que antes estaban fuera de Git.

## Prioridad al iniciar

Trabajar desde la raíz que contiene `settings.gradle.kts`. Leer `../AGENTS.md`, [plan común](../PLAN_IMPLEMENTACION.md), [aprendizaje](../APRENDIZAJE.md), [progreso](../PROGRESO.md), [contexto](contexto.md), [estado](estado-actual.md) y [pendientes](pendientes.md); revisar rama y cambios locales. Cada tarea Cloud ya está aislada; no crear worktrees salvo solicitud explícita.

MaderaTimber es el panel CENAMAD, MVVM es obligatorio y todavía no implementado. Continuar el plan pedagógico de una hora: Kotlin aplicado, una lección a la vez, código escrito por el usuario y revisión guiada. Siguiente actividad: título, «Construcción sustentable», 2024, «Proyectos activos: 8» y etiqueta «Datos demostrativos». No sustituirla por otra funcionalidad ni desarrollar el MVP completo automáticamente.

No depender de rutas `E:` ni de documentos de la carpeta superior. Los originales Windows no están montados aquí; contrastarlos e integrarlos a Git cuando estén disponibles. Conservar cualquier cambio del usuario que aparezca en el clon local antes de integrar estos documentos.

## Entorno técnico disponible

El estado observado de Cloud está conectado/en ejecución, con política de red restringida aplicada; sin secretos, variables o identidades externas configuradas en su ficha. Los helpers locales preparan las herramientas y variables de compilación; el código no requiere secretos de aplicación.

Instalación local: `/workspace/.maderatimber-env`, fuera del checkout; JDK 25.0.4.1, Gradle Wrapper 9.6.0, SDK API 37.0, Build Tools 36.0.0 y Platform Tools 37.0.1, según el registro de configuración del 2026-10-08. No transportar SDK, cachés, truststore ni rutas privadas mediante Git.

```bash
cd /workspace/MaderaTimber
bash /workspace/.maderatimber-env/configure-trust.sh
source /workspace/.maderatimber-env/activate.sh
bash ./gradlew --version --console=plain
bash ./gradlew :app:assembleDebug :app:testDebugUnitTest :app:lintDebug --max-workers=4 --console=plain
```

Estos comandos pertenecen a este entorno preparado. En otro entorno verificar herramientas/helpers antes de ejecutarlos; usar [README](../README.md) para Windows/Linux/macOS. Mantener proxy, TLS y checksums; no alterar `HOME`, versiones del proyecto ni permisos de red para ocultar fallos. Si faltan helpers, preparar el entorno mediante su configuración; este documento no instala herramientas por sí solo.

El registro local `VALIDATION.md` y los artefactos existentes acreditan un build inicial, una prueba de plantilla sin fallos y Lint sin errores con 12 advertencias. Una tarea `UP-TO-DATE` reutiliza resultados. No hay emulador preparado ni `/dev/kvm`; ejecución visual e instrumentación quedan pendientes. Actualizar [estado](estado-actual.md) con la evidencia efectivamente obtenida.

## Continuidad y publicación

Actualizar aprendizaje, estado, tareas y decisiones con cada entrega. Código y documentos viajan juntos por Git; guardar/publicar una configuración Cloud no sube archivos al repositorio. Las ediciones de este documento y del `START.md` local no modifican automáticamente la configuración publicada del servicio. Revisar allí su texto de inicio para que lea estas directrices antes de publicar una nueva versión.

Acceso desde distintos dispositivos significa continuar el mismo plan y la misma revisión Git mediante las interfaces disponibles. La app sigue siendo Android, sin versión iOS/web ni sincronización de datos entre instalaciones implementada.
