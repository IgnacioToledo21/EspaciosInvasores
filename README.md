# 👾 Space Invaders - Proyecto DAM

Bienvenido a **Space Invaders**, una versión moderna del clásico juego arcade, desarrollada como parte de mi formación como **Técnico Superior en Desarrollo de Aplicaciones Multiplataforma (DAM)**.

## 🚀 Descripción

Este proyecto recrea la experiencia clásica del Space Invaders, con mecánicas mejoradas y un enfoque educativo. Está desarrollado en **JavaFX**, utilizando `Canvas` para los gráficos y FXML para las interfaces.

### 🎮 Características principales:

- Control de nave fluido con disparo y movimiento simultáneo
- Sistema de oleadas con enemigos que aumentan en dificultad
- Enemigos que disparan de forma independiente
- Power-ups aleatorios que mejoran la jugabilidad
- Sistema de vidas representado con corazones
- Jefe final (Boss) con victoria y derrota personalizadas
- Animaciones en sprites y fondo en movimiento
- Pantalla de selección de nave
- Puntuaciones con tabla y opción de exportar certificado
- Vistas y controladores separados con arquitectura organizada

## 📷 Capturas

![Captura de pantalla 2025-04-26 201011](https://github.com/user-attachments/assets/995e2234-d388-4bd6-8e78-b311d8d4ee66)
![Captura de pantalla 2025-04-26 200831](https://github.com/user-attachments/assets/6a41e7c1-8cdf-47e4-9e7b-927a30f5cb0a)
![Captura de pantalla 2025-04-26 201146](https://github.com/user-attachments/assets/8e7ccc3f-57b7-47b8-a7bc-0b0ff7b339ef)

## 🛠️ Tecnologías utilizadas

- Java 22(22.0.2)
- JavaFX
- FXML
- CSS
- Maven

## 🧩 Instalación y ejecución

Sigue estos pasos para ejecutar el proyecto en tu máquina local:

### ✅ Requisitos previos

- Java 22 instalado
- Maven instalado
- Git instalado (opcional pero recomendado)
- Un IDE compatible (IntelliJ IDEA, Eclipse, VS Code con soporte Java...)

### 🚦 Pasos para ejecutar el juego

1. **Clona el repositorio**

```bash
git clone https://github.com/IgnacioToledo21/EspaciosInvasores.git
cd EspaciosInvasores
```

2. **Compila el proyecto con Maven**

```bash
mvn clean install
```

3. **Ejecuta el proyecto**

```bash
mvn javafx:run
```

### 🧪 ¿No tienes Maven?

Sigue estos pasos alternativos:

1. Abre el proyecto en tu IDE (recomendado: **IntelliJ IDEA**).
2. Asegúrate de que esté configurado el SDK de **Java 22** (22.0.2).
3. Si usas IntelliJ:
   - Ve a `File > Project Structure > Project` y selecciona JDK 22.
   - Luego, navega a la clase principal (`Main.java`).
   - Haz clic derecho sobre ella y selecciona **Run 'Main'**.
4. Asegúrate de tener las librerías de JavaFX configuradas si no estás usando Maven.



