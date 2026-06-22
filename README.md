<div align="center">
  <img src="https://img.icons8.com/color/120/000000/world-cup.png" alt="Logo">
  <h1>🏆 Polla Mundialista App</h1>
  <p>
    <strong>Una aplicación moderna y dinámica desarrollada en Java Swing para gestionar pronósticos y competencias tipo "Polla" (Quinielas) basadas en campeonatos de fútbol.</strong>
  </p>
  <p>
    <img alt="Java" src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white">
    <img alt="MySQL" src="https://img.shields.io/badge/MySQL-005C84?style=for-the-badge&logo=mysql&logoColor=white">
    <img alt="Swing" src="https://img.shields.io/badge/Swing-GUI-blue?style=for-the-badge">
  </p>
</div>

---

## 📖 Descripción del Proyecto

**Polla Mundialista** es un sistema de escritorio diseñado para organizar predicciones ("pronósticos") de partidos de fútbol. Combina la potencia del lenguaje de programación **Java** junto con el motor de base de datos relacional **MySQL**. Posee una interfaz gráfica totalmente renovada construida en Java Swing, que utiliza principios de diseño moderno (Glassmorphism, esquemas de color armónicos y esquinas redondeadas) alejándose del clásico diseño rígido.

Permite que usuarios convencionales registren su cuenta, vean partidos organizados por fases (Grupos, Octavos, etc.), predigan los marcadores y compitan por llegar al primer lugar de la tabla de clasificación. Un usuario con rol de **Administrador** puede actualizar los marcadores reales y administrar usuarios.

## ✨ Características Principales

* **🔐 Sistema de Autenticación (Login / Registro):** Los usuarios pueden registrarse creando un nombre de usuario y contraseña encriptada (con Hash).
* **🎯 Panel de Pronósticos:** Interfaz de usuario rica y fluida con banderas de países extraídas de APIs asíncronas para cada encuentro. Los usuarios pueden predecir marcadores libremente hasta **10 minutos antes** de que inicie cada encuentro.
* **📊 Tabla de Clasificación (Leaderboard):** El sistema calcula automáticamente los puntos sumando **3 puntos** si el usuario acierta el marcador exacto, o **1 punto** si sólo acierta la tendencia del partido (Ganador o Empate).
* **👑 Panel de Administración:** Un administrador central controla la plataforma pudiendo cerrar partidos e ingresar los resultados finales para que la tabla actualice sus puntajes.
* **⚡ Diseño Responsivo y Moderno:** Interfaz personalizada desde cero, uso de fuentes limpias y elementos en pantalla asíncronos que evitan el congelamiento visual de la interfaz.

## 🛠️ Tecnologías Utilizadas

* **Lenguaje:** Java SE (Versión 17+)
* **GUI Framework:** Java Swing nativo (Componentes sobreescritos para diseño redondeado `Graphics2D`).
* **Base de Datos:** MySQL Server 8.0+.
* **Controlador JDBC:** MySQL Connector/J (`mysql-connector-j.jar`).
* **Arquitectura:** Patrón Modelo-Vista-Controlador (MVC) y Patrón DAO (Data Access Object).

## 🚀 Instalación y Ejecución

1. **Clonar el repositorio:**
   ```bash
   git clone https://github.com/kevin101022/polla_mundialista.git
   ```

2. **Configurar la Base de Datos:**
   - Asegúrate de tener MySQL ejecutándose localmente o en un servidor.
   - Crea una base de datos llamada `polla_mundialista`.
   - Modifica las credenciales (usuario y contraseña) dentro de la clase `config/DatabaseConfig.java`.
   - Al ejecutar el programa, éste se encargará automáticamente de crear las tablas necesarias e insertar los países (Semilla de datos).

3. **Compilar el proyecto:**
   Posiciónate dentro de la carpeta `/java` y asegúrate de tener `javac` en tu sistema:
   ```bash
   javac -cp "lib/mysql-connector-j.jar" -d out $(find src -name "*.java")
   ```

4. **Ejecutar la App:**
   ```bash
   java -cp "out;lib/mysql-connector-j.jar" Main
   ```

## 🎮 Uso de la Aplicación

1. **Como Usuario Estándar:** 
   Regístrate en la ventana inicial, ve al menú *Pronósticos* en la barra lateral y comienza a ingresar tus marcadores para cada partido. Recuerda guardarlos antes de que inicie el juego. Luego observa cómo subes en la tabla de clasificación (*Ranking*).
2. **Como Administrador:** 
   Inicia sesión con las credenciales de administración. En el panel de control, podrás ver un listado exhaustivo de partidos. Usa esta vista para actualizar el marcador final cuando termine un juego de la vida real. Una vez actualizado, todos los puntajes de los jugadores se calcularán inmediatamente.

## 🤝 Contribuir

¡Las contribuciones son bienvenidas!
Si encuentras un error o tienes ideas para nuevas características:
1. Haz un Fork del proyecto
2. Crea tu rama de característica (`git checkout -b feature/NuevaCaracteristica`)
3. Haz Commit a tus cambios (`git commit -m 'Agregar nueva característica'`)
4. Haz Push a la rama (`git push origin feature/NuevaCaracteristica`)
5. Abre un Pull Request

---
*Hecho con ❤️ y Java Swing.*
