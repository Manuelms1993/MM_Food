# Inputs

La aplicación consume exactamente dos ficheros JSON:

```text
inputs/
├── comidas.json
└── cenas.json
```

Ambos siguen la misma estructura general:

- `schemaVersion`
- `planType`
- `fechaInicio`
- `semanas[]`
- `semanas[].dias[]`
- `semanas[].dias[].opciones[]`
- `semanas[].dias[].opciones[].nombre`
- `semanas[].dias[].opciones[].ingredientes[]`

Cada plan contiene 4 semanas de 7 días. La app calcula el día actual en bucle desde `fechaInicio`.

En ejecución, la app empaqueta estos ficheros como assets y además puede sincronizarlos desde:

```text
https://github.com/Manuelms1993/MM_Food/tree/master/inputs
```
