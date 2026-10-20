# ChulesRPG

A 2D RPG built with Java Swing, featuring exploration, turn-based combat, and player-created assets loaded from a companion web platform. The game requests asset data from a REST API and uses selected player, enemy, and skill assets in the game.

This repository contains the **game client**. The companion web project is available at [ChulesRPG-Web](https://github.com/IvanLopezVelilla/ChulesRPG-Web).

## Highlights

- **2D RPG gameplay:** explore connected areas, interact with characters and objects, manage inventory and equipment, and fight enemies in turn-based combat.
- **Community-driven content:** retrieves asset metadata as JSON from the web API and loads asset images over HTTP.
- **Randomized assets:** player, enemy, and skill assets are selected from the available content.
- **Save and settings support:** stores game progress and local audio/display settings on the player's machine.
- **Java desktop application:** renders the game with Swing/AWT and uses a dedicated game loop.

## Technology

- **Java 8** (Java SE 8 project configuration)
- **Java Swing and AWT** for the desktop interface, rendering, and input
- **REST API / HTTP and JSON** for retrieving web-submitted assets
- **Gson 2.14.0** for JSON deserialization
- **Eclipse IDE** project metadata is included; no Maven or Gradle build file is currently provided

## Requirements

- JDK 8 or newer
- An internet connection to access the asset API and download remote images
- The API and its asset files must be available for dynamic content to load

The Gson dependency is included in `lib/gson-2.14.0.jar`.

## Run from the command line

Run these commands from the repository root. On Linux/macOS:

```bash
mkdir -p bin
javac -source 8 -target 8 -cp lib/gson-2.14.0.jar -d bin $(find src -name "*.java")
java -cp "bin:resources:lib/gson-2.14.0.jar" main.Main
```

On Windows PowerShell:

```powershell
New-Item -ItemType Directory -Force bin | Out-Null
$sources = Get-ChildItem src -Recurse -Filter *.java | ForEach-Object { $_.FullName }
javac -source 8 -target 8 -cp "lib\gson-2.14.0.jar" -d bin $sources
java -cp "bin;resources;lib/gson-2.14.0.jar" main.Main
```

Compile the sources first using the JDK, with the Gson JAR on the classpath. Keep `resources` on the runtime classpath so the game can load its bundled maps, sprites, and sounds.

## Run from Eclipse

1. Clone or download this repository and import its root directory as an existing Java project.
2. Set the project JRE to JDK 8 or newer.
3. Add `src` and `resources` as source folders.
4. Add `lib/gson-2.14.0.jar` to the project's Java Build Path.
5. Run `main.Main` as a Java application.

The local Eclipse `.classpath` file is intentionally not tracked because it contains a machine-specific absolute path. Configure the build path as described above after importing.

## Controls

| Key | Action |
| --- | --- |
| `W`, `A`, `S`, `D` | Move / navigate menus |
| `K` or `Space` | Interact / confirm |
| `L` | Open the player menu |
| `P` | Pause |
| `M` | Open the map |
| `I` | Open inventory |
| `Esc` | Back / close a menu |

Some actions depend on the current game state.

## Runtime data

The game reads and writes `config.txt` for display and audio settings and `save.dat` for saved progress in its working directory. These are local runtime files and are excluded from version control.

## Asset licensing

Before publishing or redistributing this project, verify that you have permission to distribute every bundled or remotely supplied sprite, font, and sound file, and include any required attribution. User-submitted assets should also be covered by the web platform's content and licensing terms.


## Project structure

```text
src/        Java source code
resources/  Bundled maps, sprites, fonts, and sounds
lib/        Gson dependency
```

## License

Add a license before redistributing or accepting contributions. If you choose an open-source license, include its text in a `LICENSE` file.

## Author

**Iván López**  

[GitHub](https://github.com/IvanLopezVelilla) · [LinkedIn](https://www.linkedin.com/in/iv%C3%A1n-l%C3%B3pez-fern%C3%A1ndez-30b994415/?isSelfProfile=true)

## With help of

[RySnow youtube tutorials](https://www.youtube.com/watch?v=om59cwR7psI&list=PL_QPQmz5C6WUF-pOQDsbsKbaBZqXj4qSq)

