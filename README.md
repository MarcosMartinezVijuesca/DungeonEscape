# DungeonEscape

Videojuego 2D de plataformas con temática medieval desarrollado con [libGDX](https://libgdx.com/) como proyecto final para el módulo de Programación Multimedia y Dispositivos Móviles (DAM - Centro San Valero, Zaragoza).

## Descripción

DungeonEscape es un plataformero en el que el jugador controla a un mago que debe escapar de las mazmorras de un castillo oscuro. A lo largo de 3 niveles, el jugador se enfrentará a diferentes enemigos y deberá llegar a la puerta de salida para avanzar.

## Niveles

- **Nivel 1 - Mazmorra:** Introducción al juego con esqueletos y magos oscuros
- **Nivel 2 - Torres del castillo:** Mayor dificultad con más enemigos
- **Nivel 3 - Sala del trono:** Enfrentamiento final contra el Ogro

## Enemigos

- **Esqueleto:** Patrulla de lado a lado, ataca cuerpo a cuerpo
- **Mago Oscuro:** Detecta al jugador y lanza proyectiles
- **Ogro:** Jefe final, persigue al jugador y ataca cuerpo a cuerpo

## Controles

| Acción | Tecla |
|--------|-------|
| Mover izquierda | ← / A |
| Mover derecha | → / D |
| Saltar | Espacio / ↑ / W |
| Doble salto | Espacio dos veces |
| Disparar | F |
| Pausa | ESC |

## Características

- 3 niveles diferenciados con enemigos y dificultad progresiva
- Sistema de puntuación con Top 10 persistente
- Menú de pausa con opciones de sonido
- Dificultad configurable (Fácil, Normal, Difícil)
- Animaciones y sonidos completos para todos los personajes
- Sistema de vidas con invencibilidad temporal tras recibir daño

## Ejecutar el juego

Para ejecutar el juego necesitas tener Java instalado. Genera el JAR ejecutable con:

```bash
./gradlew lwjgl3:jar
```

El fichero `.jar` se genera en `lwjgl3/build/libs/`. Ejecútalo con:

```bash
java -jar DungeonEscape-1.0.0.jar
```

O simplemente haz doble clic en el fichero `.jar`.

## Desarrollo

Este proyecto usa [Gradle](https://gradle.org/) para gestionar dependencias.

Comandos útiles:
- `./gradlew lwjgl3:run` — ejecuta el juego desde el código fuente
- `./gradlew lwjgl3:jar` — genera el JAR ejecutable
- `./gradlew clean` — limpia los ficheros compilados

## Tecnologías

- Java 21
- libGDX 1.14.0
- LWJGL3

## Autor

Marcos Martínez Vijuesca — DAM, Centro San Valero, Zaragoza (2025-2026)
