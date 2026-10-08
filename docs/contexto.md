# Contexto rápido de CENAMAD

Actualización: **2026-10-08**. Leer [AGENTS](../AGENTS.md) y priorizar [plan común](../PLAN_IMPLEMENTACION.md), [aprendizaje](../APRENDIZAJE.md) y [progreso](../PROGRESO.md).

**Confirmado por el usuario en chats locales recuperados:** MaderaTimber implementa el panel de indicadores de impacto CENAMAD; MVVM es obligatorio para la asignatura. La especificación estaba fuera del repositorio en la carpeta Windows `MVP_Panel_Indicadores_Impacto_CENAMAD`. Se incorporó una [síntesis del MVP](../MVP_Panel_Indicadores_Impacto_CENAMAD/README.md); falta contrastar el archivo original, que no está montado en Cloud.

**Código disponible:** Android nativo, módulo `:app`, paquete `com.duoc.madertimber`, Compose/Material 3; `MainActivity` muestra `Hello Android!`. Sin ViewModel, Repository, filtros, persistencia o backend. El chat de tutoría registra una Greeting modificada y ejecución inicial en Windows; esos cambios no aparecen en este checkout y deben conservarse al integrar allí.

**Siguiente paso acordado:** una lección guiada para crear título del panel, «Construcción sustentable», año 2024, tarjeta «Proyectos activos: 8» y «Datos demostrativos». El usuario escribe el código. Después modelos/datos locales y MVVM para estado y filtros. Conoce fundamentos; enfocar la explicación en Kotlin/Android aplicado, una lección de una hora a la vez.

**Entorno:** JDK 25, SDK API 37; comandos/versiones en [README](../README.md). Cloud está conectado y preparado con helpers externos al checkout. Registro local y artefactos acreditan build base, una prueba de plantilla sin fallos y Lint sin errores con 12 advertencias; ejecución visual/instrumentada no realizadas en Cloud. Detalles en [estado](estado-actual.md) y [directrices Cloud](directrices-nube.md).

**Compartir:** documentos preparados localmente en rama `work`, base Git `32bc0f2`. El usuario autorizó guardar esta integración en un commit local; la sincronización remota sigue pendiente. Para recibirlos desde otro dispositivo deben compartirse por Git con autorización; publicar el entorno no sincroniza código. No se modificaron los originales de `E:` ni la configuración publicada de Cloud. La continuidad del plan no implica una app web/iOS o datos sincronizados.

Antes de editar: revisar rama/cambios locales; seguir [pendientes](pendientes.md). Al cerrar: actualizar progreso, estado y decisiones; preservar la distinción entre requisito, implementación y prueba. Arquitectura real: [arquitectura](arquitectura.md); decisiones: [registro](decisiones.md).
