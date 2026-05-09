# MM_Food

App Android nativa para consultar el menú diario y calcular la lista de la compra a partir de dos JSON sincronizables.

## Estructura

- `app/`: código Android en Kotlin + Jetpack Compose
- `inputs/`: datos fuente del menú en JSON
- `app/src/test/`: tests unitarios
- `docs/release-process.md`: notas del flujo de release

## Datos de entrada

La app usa dos ficheros:

- `inputs/comidas.json`
- `inputs/cenas.json`

Cada uno define:

- `fechaInicio`
- `4` semanas
- `7` días por semana
- una o varias opciones por día
- ingredientes por opción para poder calcular la compra

La app calcula en bucle el día que corresponde según la fecha actual y la fecha de inicio del plan.

## Funciones principales

- pestaña `Menú`: muestra de hoy a 7 días hacia adelante, con hoy al final de la lista y los días siguientes por encima
- actualización automática en la pestaña `Menú`: comprueba cada segundo si la ventana actual está desfasada y, si hace falta, la regenera en segundo plano
- `Actualizar`: fuerza el recálculo manual de la ventana actual
- `Sincronizar`: descarga de GitHub los JSON remotos y refresca la caché local
- pestaña `Compra`: genera la lista de ingredientes desde hoy hasta el sábado objetivo
- notificaciones diarias:
  - `10:00`: recordatorio de comida
  - `18:00`: recordatorio de cena

Regla inicial para la compra:

- lunes y martes: hasta el sábado inmediato
- miércoles a domingo: hasta el sábado de la semana siguiente

La cantidad mostrada por ingrediente en la compra es el número de apariciones de ese ingrediente dentro del periodo calculado.

## Ejecución local

Requisitos:

- Android Studio
- JDK 17 para Gradle
- dispositivo físico o emulador Android

Pasos:

1. Abre la carpeta raíz como proyecto Gradle.
2. Sincroniza el proyecto.
3. Ejecuta el módulo `app`.

Para generar una APK de debug:

```bash
./gradlew assembleDebug
```

Salida:

```text
app/build/outputs/apk/debug/app-debug.apk
```

## CI

El repositorio usa GitHub Actions con dos workflows en `.github/workflows/`:

- `android-ci.yml`: corre tests y construye una APK `release` de validación en cada PR contra `master`.
- `android-release.yml`: recalcula versión, ejecuta tests, construye la APK de release y publica GitHub Release en cada push a `master`.
