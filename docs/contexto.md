# Contexto rápido de Cenamad

Actualización: **2026-10-08**. Leer primero `../AGENTS.md`. Este resumen permite iniciar otra sesión sin historial de chats; verificarlo contra código y Git si han cambiado.

## Qué es y qué funciona

**Confirmado:** proyecto Android denominado Cenamad por el propietario, repositorio/carpeta `MaderaTimber`, nombre interno/visible `MaderTimber`, paquete `com.duoc.madertimber`. Su propósito de negocio y la relación entre los nombres no están especificados. No inventar catálogo, inventario, usuarios, pedidos u otras funciones a partir del nombre.

La app es una base inicial: `MainActivity` → `MaderTimberTheme` → `Scaffold` → `Greeting("Android")` → `Hello Android!`. Tema claro/oscuro y colores dinámicos en Android 12+. Dos pruebas de ejemplo; sin funciones de negocio, navegación, datos persistentes o backend.

## Mapa técnico y decisiones esenciales

- Un módulo Gradle `:app`. Código Kotlin en `app/src/main/java/com/duoc/madertimber/`; tema en `ui/theme/`. UI Jetpack Compose / Material 3. No hay MVVM/Clean Architecture implementadas.
- Builds Kotlin DSL, catálogo `gradle/libs.versions.toml`, Wrapper versionado y daemon JDK 25. Java source/target 11 no es el JDK para ejecutar Gradle. SDK compilación/objetivo API 37 y mínimo API 24. Tabla de versiones/comandos: README.
- Nombres/paquete y configuración de aplicación se conservan. Motivos históricos de elección de tecnologías/estructura desconocidos; decisiones observadas D-001 a D-004 en `decisiones.md`.
- D-005: contexto persistente versionado, solicitado por el propietario. Documentación y progreso se actualizan con el código; no depender de memoria de Codex.

## Progreso y límites

Base de aplicación revisada en `3dcf635`; Git local en `main`, seguimiento `origin/main`, remoto configurado `Vicentep12/MaderaTimber`. No se verificó estado remoto ni se hizo commit/push. La nueva documentación/configuración está local: no estará en otros equipos hasta sincronizarla con autorización.

Gradle se descargó; AGP rechazó la ruta Windows por tildes (`Móviles`). Un clon local independiente recuperó el código, pero su build falló al iniciar AAPT2; la causa del entorno sigue sin confirmar. Usar rutas sin tildes para proyecto/caché al diagnosticar. APK, prueba local, Lint, pruebas instrumentadas, ejecución visual y build Cloud no están acreditados. Detalle: `estado-actual.md`. La revisión básica de textos rastreados no detectó secretos; no fue una auditoría histórica.

**Inferencia:** parece una plantilla inicial Android/Compose por sus ejemplos. No consta versión original de Android Studio, requerimientos de negocio ni decisiones previas fuera de Git.

## Antes de continuar

1. Abrir la raíz Git que contiene `settings.gradle.kts`, revisar rama y cambios locales; traer cambios compartidos solo con el árbol limpio y según autorización.
2. Leer `estado-actual.md` y `pendientes.md`; validar JDK/SDK según README antes de prometer una compilación.
3. Confirmar el primer caso de uso Cenamad y criterios de aceptación. La siguiente funcionalidad todavía no ha sido seleccionada.
4. Para cambios importantes, consultar `arquitectura.md` y `decisiones.md`; preservar identidad y alcance autorizado.
5. Al terminar, actualizar estado, tareas y decisiones afectadas; refrescar este resumen solo con cambios esenciales. Informar pruebas realizadas y lo que falta.

Guías: [README](../README.md), [arquitectura](arquitectura.md), [decisiones](decisiones.md), [estado](estado-actual.md), [pendientes](pendientes.md). La compatibilidad Cloud es condicionada al entorno JDK/SDK/red; su conexión requiere pasos manuales del propietario.
