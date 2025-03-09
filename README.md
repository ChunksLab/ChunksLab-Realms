# ChunksLab-Realms 🌍

![Code Quality](https://img.shields.io/codefactor/grade/github/ChunksLab/ChunksLab-Realms)
<a href="https://wiki.chunkslab.com" alt="GitBook">
<img src="https://img.shields.io/badge/docs-gitbook-brightgreen" alt="Gitbook"/>
</a>
![Code Size](https://img.shields.io/github/languages/code-size/ChunksLab/ChunksLab-Realms)
![bStats Servers](https://img.shields.io/bstats/servers/25022)
![bStats Players](https://img.shields.io/bstats/players/25022)
![GitHub License](https://img.shields.io/github/license/ChunksLab/ChunksLab-Realms)

## 📌 Overview

ChunksLab-Realms is a high-performance Paper plugin designed to enhance **realm and world management** in Minecraft servers. It prioritizes **efficiency**, **scalability**, and **flexibility**. 🏗️

### 🔥 Key Features

- **☕ Zstd Compression**: Efficient data storage and retrieval for world management.
- **⚡ Multi-Threaded World Handling**: Distributes tasks across multiple threads to reduce lag.
- **🛠️ Developer API**: Allows integration with custom realm mechanics and interactions.

---
## 🔧 Building the Project

### 💻 Command Line Usage

1. Install **JDK 17 & 21**.
2. Open a terminal and navigate to the project directory.
3. Run:
   ```sh
   ./gradlew build
   ```
4. The compiled artifact can be found in the **/target** folder.

### 🛠️ Using an IDE

1. Import the project into your preferred **IDE**.
2. Run the **Gradle build** process.
3. Locate the compiled artifact in the **/target** folder.

---
## 🤝 Contributing

### 🌍 Translations

1. Clone the repository.
2. Create a new language file in:
   ```
   /src/main/java/com/chunkslab/realms/config/messages
   ```
3. Submit a **pull request** with your changes. Your contributions are appreciated! 💖

### 🚀 Areas for Improvement

- Optimize realm **loading/unloading** system for lower memory usage.
- Improve **asynchronous chunk management** for smoother world transitions.
- Implement **thread-safe operations** for realm management.
- Optimize **packet transmission** for reduced network load.
- Enhance **entity movement packets** for smoother in-realm interactions.

---
## 💖 Support the Developer

If you enjoy using **ChunksLab-Realms**, consider supporting the developer! 🥰

- [Polymart](https://polymart.org/resource/chunkslab-realms/)
- [BuiltByBit](https://builtbybit.com/resources/chunkslab-realms/)
- [ChunksLab](https://chunkslab.com/chunkslab-realms/)
- [PayPal](https://paypal.me/tamergumus)

---
## 📚 ChunksLab-Realms API

### 📌 Repository
```kotlin
repositories {
    maven("https://repo.voxelarc.net/releases/")
}
```

### 📌 Dependency
```kotlin
dependencies {
    compileOnly("com.chunkslab.realms:api:1.0.0")
}
```
