# Instrucciones para trabajar en MaderaTimber

## Inicio y fuentes

- Trabajar desde esta raíz Git. Revisar rama, git status y cambios locales antes de editar; preservar el trabajo del usuario.
- Leer docs/plan-implementacion.md para tareas y próximo paso, docs/progreso.md para aprendizaje comprobado y docs/estado-actual.md para evidencia técnica.
- Consultar docs/especificacion.md antes de implementar requisitos: conserva los modelos, cálculos y plan técnico completo. Consultar docs/aprendizaje.md en tutoría y docs/arquitectura.md y docs/decisiones.md cuando se afecten responsabilidades o acuerdos.
- Instalación, versiones, comandos y notas Cloud están en README.md. La configuración Gradle es la fuente de versiones efectivas.

## Alcance y forma de trabajar

- MaderaTimber implementa el panel CENAMAD. La asignatura exige MVVM; seguir las responsabilidades definidas en la especificación.
- Toda implementación nueva o modificación de funcionalidad debe respetar MVVM desde el inicio, incluidas las pantallas de demostración: las vistas Compose renderizan estado y emiten eventos; los ViewModels mantienen el estado observable y procesan las acciones; el modelo contiene las reglas de negocio y validación, y los Repository gestionan el acceso a datos cuando corresponda. No colocar validaciones de negocio ni acceso a datos en composables o actividades. Usar la separación de login y registro como referencia y consultar docs/arquitectura.md; el dashboard estático pendiente de migración no es un patrón para nuevas funcionalidades. Verificar esta separación y probar el comportamiento de la lógica implementada antes de dar el trabajo por terminado.
- Mantener el paquete com.duoc.madertimber y las variantes de nombre actuales. No renombrar, refactorizar o añadir módulos/frameworks por iniciativa documental.
- Responder en español y aplicar la tutoría de docs/aprendizaje.md: una lección a la vez, ejemplos breves, ejercicios verificables, código escrito por el usuario y revisión guiada.
- Leer el perfil y progreso antes de preguntar por experiencia o entorno; preguntar únicamente lo que falte o cambie.
- No resolver ejercicios completos ni modificar código durante la tutoría salvo petición explícita. Una petición de implementación autoriza realizar ese trabajo.
- Usar los planes completos acordados. No reemplazarlos por síntesis que omitan requisitos ni agregar condiciones de cierre fuera del alcance.

## Convenciones y validación

- Mantener estilo Kotlin oficial, cuatro espacios, composables con Modifier cuando corresponda y rutas existentes bajo src/.../java/.
- Declarar versiones en gradle/libs.versions.toml y utilizar el Wrapper. El proyecto usa Kotlin integrado en AGP; no añadir otro plugin Kotlin Android por asumir que falta.
- Ejecutar comprobaciones pertinentes al código/build modificado (compilación, Lint y revisión en Android). Desde el 2026-10-29 el repositorio no incluye pruebas automatizadas, por instrucción del usuario; no añadirlas salvo petición explícita. Comandos en README.
- Para documentación, comprobar enlaces, rutas, coherencia y git diff --check. Distinguir lectura estática, ejecución reportada y comandos ejecutados; no inventar cobertura ni presentar la plantilla como MVP validado.
- No cambiar versiones/SDK para ocultar fallos de entorno. Registrar causa y evidencia antes de proponer un cambio.
- No publicar, hacer push, merge ni cambiar remotos sin autorización explícita. Un commit local no sincroniza otro dispositivo.
- No versionar secretos, rutas SDK, firma ni cachés; revisar archivos rastreados porque .gitignore no protege los ya incorporados.

## Mantenimiento sin duplicaciones

- Mantener una fuente por tema dentro de docs/. Actualizar el plan cuando cambien tareas; progreso cuando se compruebe aprendizaje; estado técnico cuando haya código o validación; arquitectura cuando cambien componentes y decisiones solo ante acuerdos relevantes.
- Los contratos, fórmulas, aceptación y plan técnico detallado viven en especificacion.md. Referenciarlos desde el plan operativo, sin mantener otra versión.
- Registrar evidencia, límites y próximo paso útil, sin diarios de movimientos de archivos ni copias del historial de chat.
- Conservar planes completos y resultados relevantes. El historial detallado de versiones vive en Git.
- Mantener README si cambia instalación/configuración. No crear documentación equivalente en la carpeta superior; su AGENTS solo dirige a este archivo.
