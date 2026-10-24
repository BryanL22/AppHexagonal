# Gestión de Drones — Arquitectura Hexagonal

Aplicación de escritorio en JavaFX para registrar, consultar, actualizar y
eliminar drones sobre una base de datos MySQL. Proyecto académico de
**Ingeniería de Software II** (Politécnico Grancolombiano), construido con
**arquitectura hexagonal** (puertos y adaptadores).

Hay dos tipos de dron:

| Tipo          | Dato propio                         |
|---------------|-------------------------------------|
| Agricultura   | Capacidad del tanque (litros)       |
| Vigilancia    | Detección térmica (sí / no)         |

## Arquitectura

El núcleo de la aplicación no depende de la tecnología. La interfaz JavaFX y
la base de datos MySQL se conectan al núcleo a través de **puertos**
(interfaces) y se implementan como **adaptadores**.

Recorrido de una operación, por ejemplo **Crear**:

```
Usuario
   │  clic en un botón
   ▼
MainController           ← Adaptador de ENTRADA   (infraestructura/ui)
   │  llama a la interfaz
   ▼
CrearDronUseCase         ← Puerto de ENTRADA       (aplicacion/puerto/entrada)
   │  implementado por
   ▼
CrearDronServicio        ← Servicio de aplicación  (aplicacion/servicio)
   │  llama a la interfaz
   ▼
GuardarDronPort          ← Puerto de SALIDA        (aplicacion/puerto/salida)
   │  implementado por
   ▼
MySqlDronRepository      ← Adaptador de SALIDA     (infraestructura/persistencia)
   │  SQL por JDBC (conexión de ConexionBD)
   ▼
Base de datos MySQL
```

Los servicios y los puertos trabajan con las entidades de `dominio/modelo`.

Las dependencias siempre apuntan hacia adentro: la infraestructura conoce a la
aplicación y la aplicación conoce al dominio, nunca al revés.

### Paquetes (`co.edu.poli.sw2`)

| Paquete                        | Contenido                                                                                           |
|--------------------------------|-----------------------------------------------------------------------------------------------------|
| `dominio.modelo`               | Entidades: `Drone`, `Agricultura`, `Vigilancia`, `Piloto`, `Sensor`, `Mision`. Solo usan `java.*`.   |
| `dominio.weather`              | Modelo meteorologico: `Weather` y sujeto `WeatherObserver` (patron Observer).                       |
| `dominio.weather.chain`        | Evaluacion meteorologica: `WeatherHandler`, `TemperatureHandler`, `HumidityHandler`, `WindHandler`, `PressureHandler`, `VisibilityHandler` y `ViabilityResult` (patron Chain of Responsibility). |
| `aplicacion.puerto.entrada`    | Casos de uso: CRUD de drones, `GetWeatherByCityUseCase`, `GestionarSuscripcionesClimaUseCase` y `EvaluarViabilidadVueloUseCase`. |
| `aplicacion.puerto.salida`     | Puertos: persistencia de drones, `ConsultarClimaPorCiudadPort`, `GeocodificarCiudadPort`, `WeatherQueryException` y `PersistenciaException`. |
| `aplicacion.servicio`          | Servicios de aplicacion: CRUD de drones, `GetWeatherByCityService`, `WeatherNotificationService` y `EvaluarViabilidadVueloServicio`. |
| `infraestructura.persistencia` | `MySqlDronRepository` (implementa los puertos de persistencia con SQL) y `ConexionBD` (Singleton JDBC). |
| `infraestructura.weather`      | Adaptadores y clientes Open-Meteo (`GeoApiClient`, `GeoApiAdapter`, `OpenWeatherClient`, `OpenWeatherAdapter`), sin API key. |
| `infraestructura.ui`           | `MainController`: adaptador de entrada JavaFX.                                                       |
| `vista`                        | `App`: punto de entrada; crea y conecta todas las piezas.                                           |

La vista (`GestorDrones.fxml`) y la hoja de estilos están en
`src/main/resources/co/edu/poli/sw2/`. El proyecto es modular: su
`module-info.java` declara las dependencias de JavaFX, `java.sql` y `java.logging`.

### Decisiones de diseño

- **Un puerto por operación.** Interfaces dedicadas para CRUD, consulta de clima, suscripciones y evaluacion de viabilidad.
- **Un servicio por caso de uso.** Cada servicio recibe por constructor solo los colaboradores que necesita.
- **Patrones de diseño utilizados:**
  - **Singleton:** `ConexionBD` para una conexion compartida a MySQL.
  - **Observer:** `WeatherObserver` notifica a los drones suscritos sobre actualizaciones meteorologicas.
  - **Chain of Responsibility:** `WeatherHandler` evalua secuencialmente temperatura, humedad, viento, presion y visibilidad para determinar la viabilidad del vuelo.
- **Servicio meteorologico externo:** Open-Meteo (geocodificacion y pronostico) sin requerimiento de API key.
- **Cambiar de infraestructura solo toca `App`.** Adaptadores reemplazables sin afectar el dominio ni los casos de uso.
- **`App` es la raíz de composición.** Es la única clase que hace `new` de las piezas del hexágono.
- **La tecnología no cruza hacia el núcleo.** Los repositorios y adaptadores capturan excepciones tecnicas y las traducen a excepciones de puerto.
- **Las reglas de negocio viven en el núcleo, no en la interfaz.** Validaciones de entidades en el dominio y reglas de vuelo en la cadena de responsabilidad.

## Requisitos

- JDK 21
- Maven 3.9 o superior
- MySQL 8 en ejecución

## Configuración

Las credenciales de la base de datos no están en el código. Se leen de
variables de entorno o, si no existen, de un archivo `.env` en la raíz del
proyecto:

```bash
cp .env.example .env      # en PowerShell: Copy-Item .env.example .env
```

Luego completa los valores:

```
DB_URL=jdbc:mysql://localhost:3306/gestion_drones?allowPublicKeyRetrieval=true&useSSL=false
DB_USER=root
DB_PASSWORD=tu_contraseña
```

El archivo `.env` está en `.gitignore` y nunca se sube al repositorio.

La base de datos indicada en `DB_URL` debe existir. Las tablas se crean solas
la primera vez que la aplicación se conecta.

## Uso

```bash
mvn javafx:run          # ejecuta la aplicación
mvn test                # ejecuta las pruebas del dominio
mvn javadoc:javadoc     # genera la documentación en target/site/apidocs/index.html
```

En la ventana:

1. Completa ID, serial, modelo, fabricante y peso, y elige el tipo. Aparece el
   campo propio del tipo elegido.
2. **Crear** registra el dron. **Actualizar** guarda los cambios del dron con
   ese ID.
3. **Consultar por ID** y **Eliminar** solo necesitan el ID.
4. **Consultar todos** recarga la tabla. Al hacer clic en una fila, sus datos
   pasan al formulario.

## Base de datos

| Tabla          | Descripción                                                                  |
|----------------|------------------------------------------------------------------------------|
| `drone`        | Datos comunes de todos los drones. Llave primaria: `id`.                     |
| `agricultura`  | `capacidad_tanque`, enlazada a `drone` por `id_drone`.                       |
| `vigilancia`   | `deteccion_termica`, enlazada a `drone` por `id_drone`.                      |
| `piloto`       | Pilotos; `drone.id_piloto` apunta aquí (opcional).                           |
| `sensor`       | Sensores; cada uno pertenece a un dron.                                      |
| `mision`       | Misiones de vuelo.                                                           |
| `mision_drone` | Relación muchos a muchos entre misiones y drones.                            |

Cada especialización de dron tiene su propia tabla (herencia por tabla). Al
eliminar un dron se borran en cascada sus filas relacionadas.

Por ahora solo los drones tienen operaciones en la aplicación. Las tablas de
pilotos, sensores y misiones se crean, pero todavía no tienen CRUD.

## Pruebas

Las pruebas unitarias (JUnit 5) están en `src/test/java/co/edu/poli/sw2/tests/unitaria`
y cubren las entidades del dominio. No necesitan base de datos.
