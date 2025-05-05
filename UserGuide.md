
# PhysiPlay – User Guide

## Overview

**PhysiPlay** is a JavaFX-based educational simulation tool designed to visualize and explore physics phenomena like momentum, pendulum motion, and spring oscillations. It includes interactive controls, dynamic charts, multi-language support (English and French), and a modular component system for extensibility.

---

## Features

### 🎓 Physics Simulations

Each simulation comes with customizable parameters, real-time animations, and dynamic data visualization.

#### Momentum Simulation
- Visualize 1D elastic collisions between two balls.
- Customize each ball's mass and initial velocity.
- Output results of momentum transfer post-collision.

#### Pendulum Simulation
- Simulate a simple pendulum with mass and damping controls.
- Graphs included:
  - Angle vs Time
  - Angular Velocity vs Time
  - Angular Acceleration vs Time

#### Spring Simulation
- Mass-spring system with adjustable:
  - Amplitude
  - Spring constant
  - Mass
- Graphs included:
  - Displacement vs Time
  - Velocity vs Time
  - Acceleration vs Time

---

## Interface

### Menu Navigation
- **Main Menu**: Choose a simulation.
- **Menu Bar**: Access camera reset, scaling, language, and file actions.
- **Back Button**: Returns to the main menu from any simulation.

### Simulation View
Each module includes:
- A **Canvas Area** to animate physics.
- A **Control Panel** with sliders and buttons.
- Real-time **Graphs** for simulation variables.

### Preset System
- Create and reuse object configurations using `CreatePresetController`.
- Set object `position`, `rotation`, and `scale`.
- Add components like `Rigidbody`, `Renderer`, etc.

### Polygon Visualizer
- A freeform polygon tool for creating and editing shapes.
- Use `PolygonVisualizerModel` to add, move, and remove points.
- Export vertex positions as normalized coordinate sets.

---

## Localization

- Supports **English** and **French**.

---

## Component System

PhysiPlay uses a flexible object-component system:

| Component              | Purpose                                   |
|------------------------|-------------------------------------------|
| `Rigidbody`            | Adds physics body (mass, friction, etc.)  |
| `CircleRenderer`       | Renders object as a circle                |
| `RectangularRenderer`  | Renders object as a rectangle             |
| `PolygonRenderer`      | Renders object from point list            |
| `RegularPolygonRenderer`| Renders uniform-sided polygons           |
| `Vector2Field`         | Allows editing of 2D position/scale       |
| `NumberOnlyTextField`  | Restricts input to numeric values         |

---

## Architecture

| Class/File                | Role |
|---------------------------|------|
| `SimulationManager`       | Controls global physics state, rendering |
| `ScreenController`        | Switches between scenes like simulations, settings |
| `VinithDebController`     | Main simulation router (pendulum, spring, momentum) |
| `Component.java`          | Base class for attachable simulation components |
| `StartStopControllable`  | Interface for play/pause logic in simulations |
| `MainApp.java`            | JavaFX `Application` launcher |
| `LoginController`, `RegisterPageController` | Handles account creation & login logic |
| `SettingsController`      | Manages language and view preferences |

---

## Tips

- Use **middle-click + drag** to pan the canvas.
- Use **scroll wheel** to zoom in/out.
- Check the **View menu** to toggle canvas sidebars.
- Language, scaling, and camera reset options are available in the top menu.
