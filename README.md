# Food Express

## Integrantes
- Aydan Gonzalez
- Marco Corrales

## Funcionalidades
- Registro y login de usuarios con validaciones.
- Navegación entre pantallas con animaciones (Compose Navigation).
- Carrito de compras con gestión de estado en tiempo real.
- Consumo de API externa (Spring Boot backend).
- Visualización de mapa con ubicación en tiempo real (Google Maps API).
- Secciones de dietas, promociones, categorías y reseñas.
- Persistencia de sesión y perfil de usuario.

## Endpoints usados

### Propios (Spring Boot backend)
- `POST /usuarios/login` → login de usuario.
- `POST /usuarios/registro` → registro de usuario.
- `GET /usuarios/{id}` → obtener datos de usuario.

### Externos
- **Google Maps API** → visualización de mapa y ubicación.
- **Firebase Analytics** → registro de eventos de uso.

## Instrucciones para ejecutar el proyecto
1. Clonar el repositorio:
   ```bash
   git clone https://github.com/AydanGM/FoodExpress_Movil.git
   ```
2. Abrir el proyecto en Android Studio.
3. Configurar el backend Spring Boot:
    - Java 21
    - Maven
    - H2 Database
    - Ejecutar en `http://10.0.2.2:8080/`
4. Ejecutar la app en emulador o dispositivo físico.
5. Instalar el APK firmado (ubicado en carpeta `/apk`).

## APK firmado y ubicación del archivo .jks
- APK firmado: `/apk/app-release.apk`
- Keystore: `/apk/upload-keystore.jks`

## Código fuente
- **Microservicios (Spring Boot)**: carpeta `/backend`
- **App móvil (Android Studio)**: carpeta `/app`

## Evidencia de trabajo colaborativo
- Link al tablero de Trello: [Trello Board](https://trello.com/b/k3MHOemZ/foodexpressmovil)
```
