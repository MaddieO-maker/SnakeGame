# Project Guidelines

## Code Style
Use standard Java naming conventions: CamelCase for classes and methods, camelCase for variables. Follow basic indentation and spacing. Reference SimpleWindow.java for JPanel extension patterns.

## Architecture
This is a lightweight Java Swing application with custom drawing. Components include JPanel subclasses for game rendering. No external frameworks; relies on AWT/Swing for GUI.

## Build and Test
Compile with `javac *.java`. Run classes with `java ClassName` (e.g., `java SimpleWindow` for GUI test). No automated tests; verify manually by running and observing behavior.

## Conventions
Hardcode constants as final variables (e.g., gridSize, cellSize). Use Graphics object in paintComponent for custom drawing. Include main methods for standalone execution.