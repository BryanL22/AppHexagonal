# Gestión de Drones — Arquitectura Hexagonal

Aplicación de escritorio en JavaFX para gestionar drones sobre una base de
datos MySQL. Proyecto académico de **Ingeniería de Software II** (Politécnico
Grancolombiano), construido con **arquitectura hexagonal** (puertos y
adaptadores).

Además del CRUD de drones, la aplicación implementa siete patrones de diseño:
**Singleton, Observer, Chain of Responsibility, Command, Strategy, State y
Mediator** (ver [Patrones de diseño](#patrones-de-diseño)).

Hay dos tipos de dron:

| Tipo          | Dato propio                         |
|---------------|-------------------------------------|
| Agricultura   | Capacidad del tanque (litros)       |
| Vigilancia    | Detección térmica (sí / no)         |

## Arquitectura

El núcleo de la aplicación no depende de la tecnología. La interfaz JavaFX, la
base de datos MySQL y la API del clima se conectan al núcleo a través de
**puertos** (interfaces) y se implementan como **adaptadores**.

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

| Paquete                              | Contenido |
|--------------------------------------|-----------|
| `dominio.modelo`                     | Entidades: `Drone`, `Agricultura`, `Vigilancia`, `Piloto`, `Sensor`, `Mision`. Solo usan `java.*`. |
| `dominio.weather`                    | Modelo meteorológico `Weather` y sujeto `WeatherObserver` (Observer). |
| `dominio.weather.chain`              | `WeatherHandler` y sus eslabones `TemperatureHandler`, `HumidityHandler`, `WindHandler`, `PressureHandler`, `VisibilityHandler`, más `ViabilityResult` (Chain of Responsibility). |
| `dominio.command`                    | `DroneCommand` y los comandos `TomarFotografiaCommand`, `EscanearZonaCommand`, `MedirTemperaturaCommand` (Command). |
| `dominio.strategy`                   | `ConsumoStrategy` y las estrategias `AhorroStrategy`, `NormalStrategy`, `AltoRendimientoStrategy`, más `PuntoGeografico` (Strategy). |
| `dominio.state`                      | `DroneContext` (Contexto), `DroneState` y los estados `OffState`, `ReadyState`, `FlyingState`, `LandingState` (State). |
| `aplicacion.puerto.entrada`          | Casos de uso: CRUD de drones, `GetWeatherByCityUseCase`, `GestionarSuscripcionesClimaUseCase`, `EvaluarViabilidadVueloUseCase`, `ManageDroneStateUseCase` y `ExecuteMissionUseCase`. |
| `aplicacion.puerto.salida`           | Puertos de persistencia de drones, `ConsultarClimaPorCiudadPort`, `GeocodificarCiudadPort`, `PersistenciaException` y `WeatherQueryException`. |
| `aplicacion.servicio`                | Servicios: CRUD de drones, `GetWeatherByCityService`, `WeatherNotificationService`, `EvaluarViabilidadVueloServicio`, `DroneOperacionService` y `ProgramadorTareas` (Command), `DroneStateService` (State) y `MissionService` (Mediator). |
| `aplicacion.usecase`                 | `CalcularConsumoUseCase`: Contexto del patrón Strategy. |
| `aplicacion.mediator`                | `DroneMediator` (Mediator) y `MissionCoordinator` (torre de control). |
| `aplicacion.mediator.components`     | `NavigationSystem`, `SensorSystem`, `BatterySystem`, `CommunicationSystem`. |
| `infraestructura.persistencia`       | `MySqlDronRepository` (puertos de persistencia con SQL) y `ConexionBD` (Singleton JDBC). |
| `infraestructura.weather`            | Clientes y adaptadores de Open-Meteo: `GeoApiClient`, `GeoApiAdapter`, `OpenWeatherClient`, `OpenWeatherAdapter` (sin API key). |
| `infraestructura.loader`             | `JsonRutaLoader`: lee una ruta (origen y destino) desde un archivo JSON. |
| `infraestructura.util`               | `HaversineUtil`: distancia en kilómetros entre dos coordenadas. |
| `infraestructura.ui`                 | `MainController`: adaptador de entrada JavaFX. |
| `vista`                              | `App`: punto de entrada; crea y conecta todas las piezas. |

La vista (`GestorDrones.fxml`) y la hoja de estilos están en
`src/main/resources/co/edu/poli/sw2/`. El proyecto es modular: su
`module-info.java` declara las dependencias de JavaFX, `java.sql`,
`java.net.http`, `java.logging` y Jackson.

### Decisiones de diseño

- **Un puerto por operación.** El CRUD usa cinco puertos de entrada y cinco de
  salida; no se agrupan en una sola interfaz.
- **Un servicio por caso de uso.** Cada servicio recibe por constructor solo
  los colaboradores que necesita.
- **`App` es la raíz de composición.** Es la única clase que hace `new` de las
  piezas del hexágono. Cambiar de infraestructura solo toca `App`.
- **La tecnología no cruza hacia el núcleo.** El repositorio y los adaptadores
  traducen las excepciones técnicas (`SQLException`, `IOException`) a
  excepciones de puerto.
- **Las reglas de negocio viven en el núcleo, no en la interfaz.** El
  controlador solo lee y escribe controles y dispara casos de uso.
- **La entidad `Drone` no se modificó para los patrones.** Conserva solo sus
  atributos originales; el estado (State) y la coordinación de misiones
  (Mediator) viven fuera de ella.

## Patrones de diseño

| Patrón | Dónde | Qué resuelve |
|--------|-------|--------------|
| **Singleton** | `ConexionBD` | Una sola conexión compartida a MySQL. |
| **Observer** | `WeatherObserver` (sujeto) y `Drone` (observador) | Al consultar el clima, los drones suscritos reciben la actualización. |
| **Chain of Responsibility** | `WeatherHandler` y sus 5 eslabones | Evalúa temperatura, humedad, viento, presión y visibilidad en cadena; el primero que falla declara el vuelo no apto. |
| **Command** | `DroneCommand`, 3 comandos, `ProgramadorTareas` (Invoker), `DroneOperacionService` (Receiver) | Encola operaciones del dron (fotografía, escaneo, temperatura) y las ejecuta juntas, con historial por dron. |
| **Strategy** | `ConsumoStrategy`, 3 estrategias, `CalcularConsumoUseCase` (Contexto) | Calcula el consumo de una ruta con un factor intercambiable: ahorro ×0.5, normal ×1.0, alto rendimiento ×1.5. |
| **State** | `DroneContext` (Contexto), `DroneState` y 4 estados | Controla el ciclo de vida del dron y rechaza las transiciones no permitidas. |
| **Mediator** | `DroneMediator`, `MissionCoordinator` y 4 sistemas | Coordina una operación de vigilancia sin que los sistemas se comuniquen entre sí. |

### State: ciclo de vida del dron

```
APAGADO ──encender──▶ EN ESPERA ──despegar──▶ VOLANDO
   ▲                     │  ▲                    │
   └──────apagar─────────┘  │                aterrizar
                            │                    ▼
                            └─finalizarAterrizaje─ ATERRIZANDO
```

`DroneContext` envuelve al `Drone`, guarda el estado actual y delega en él cada
operación. Cualquier transición que no aparece en el diagrama lanza un error
con el motivo (por ejemplo, "No se puede despegar en estado APAGADO.").

El estado vive **solo en memoria**: `DroneStateService` recuerda el último
estado de cada dron por su ID. Un dron que nunca se operó empieza en APAGADO,
y al cerrar la aplicación todos vuelven a APAGADO (no hay columna de estado
en MySQL).

### Mediator: torre de control de la operación de vigilancia

`MissionCoordinator` es la torre de control. Los cuatro sistemas solo conocen
a la interfaz `DroneMediator`: cuando terminan su paso le avisan con
`notificar(this, evento)` y la torre decide qué sigue.

```
iniciarMision ─▶ Navegación: prepara la ruta ── "RUTA_LISTA" ──▶ torre
torre ─▶ Sensores: activa los sensores ── "SENSORES_ACTIVOS" ──▶ torre
torre ─▶ Batería: verifica la energía ── "BATERIA_OK" / "BATERIA_BAJA" ──▶ torre
torre ─▶ Comunicación: establece el enlace ── "ENLACE_LISTO" ──▶ torre
torre ─▶ misión iniciada
```

Si la batería está por debajo del **30 %** (`BatterySystem.NIVEL_MINIMO`), la
torre aborta la misión y no establece el enlace. Cada paso queda anotado en
una bitácora que se muestra en pantalla. El Mediator está en la capa de
aplicación y no usa las operaciones CRUD del dron.

## Requisitos

- JDK 21
- Maven 3.9 o superior
- MySQL 8 en ejecución
- Conexión a internet para la consulta del clima (Open-Meteo)

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
mvn test                # ejecuta las pruebas unitarias
mvn javadoc:javadoc     # genera la documentación en target/site/apidocs/index.html
```

La ventana tiene el formulario y las secciones de cada patrón a la izquierda,
y la tabla de drones a la derecha. Al hacer clic en una fila de la tabla, sus
datos pasan al formulario y el dron queda seleccionado en todas las secciones.

1. **Gestor de Drones (CRUD).** Completa ID, serial, modelo, fabricante y
   peso, y elige el tipo; aparece el campo propio del tipo. **Crear**,
   **Actualizar**, **Consultar por ID**, **Consultar todos** y **Eliminar**.
2. **Monitoreo Meteorológico y Viabilidad (Observer + Chain).** Escribe una
   ciudad y pulsa **Consultar clima**: se muestran temperatura, humedad,
   viento, presión y visibilidad, y la cadena indica si el vuelo es apto.
   **Suscribir** / **Desuscribir** agregan o quitan drones de las
   notificaciones del clima.
3. **Operaciones del Dron (Command).** Elige un dron, marca las operaciones
   (Tomar Fotografía, Escanear Zona, Medir Temperatura) y pulsa **Confirmar y
   Ejecutar Comandos**. Debajo aparece el historial del dron.
4. **Consumo Energético de Ruta (Strategy).** Elige un dron, carga una ruta
   JSON (hay un ejemplo en `ruta_ejemplo.json`), elige la estrategia y pulsa
   **Calcular**. Se muestran la distancia (fórmula de Haversine) y el consumo.
5. **Estado del Dron (State).** Elige un dron y usa **Encender**,
   **Despegar**, **Aterrizar**, **Finalizar aterrizaje** y **Apagar**. Si la
   transición no está permitida, aparece una alerta y el estado no cambia.
6. **Operación de Vigilancia (Mediator).** Elige un dron, escribe el
   porcentaje de batería (0 a 100) y pulsa **Iniciar misión**. La bitácora
   muestra cada paso de la torre de control y **Resultado** indica si la
   misión se inició o se abortó (con menos de 30 %).

Formato de la ruta JSON:

```json
{
  "origen":  { "nombre": "Punto Origen",  "latitud": 4.60971, "longitud": -74.08175 },
  "destino": { "nombre": "Punto Destino", "latitud": 4.70159, "longitud": -74.14690 }
}
```

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

Solo los drones tienen operaciones CRUD en la aplicación. Las tablas de
pilotos, sensores y misiones se crean, pero todavía no tienen CRUD. Ni el
estado del dron (State) ni las operaciones de vigilancia (Mediator) se guardan
en la base de datos.

## Pruebas

Las pruebas unitarias (JUnit 5) están en
`src/test/java/co/edu/poli/sw2/tests/unitaria`: 85 pruebas en 23 clases. No
necesitan base de datos ni internet (el clima se prueba con dobles).

| Área | Clases de prueba |
|------|------------------|
| Dominio | `DroneTest`, `AgriculturaTest`, `VigilanciaTest`, `PilotoTest`, `SensorTest`, `MisionTest` |
| Observer y clima | `WeatherObserverTest`, `GetWeatherByCityServiceTest`, `OpenWeatherAdapterTest` |
| Chain of Responsibility | `TemperatureHandlerTest`, `HumidityHandlerTest`, `WindHandlerTest`, `PressureHandlerTest`, `VisibilityHandlerTest`, `WeatherChainTest`, `EvaluarViabilidadVueloServicioTest` |
| Strategy | `ConsumoStrategyTest`, `CalcularConsumoUseCaseTest`, `JsonRutaLoaderTest`, `HaversineUtilTest` |
| State | `DroneStateTest`, `DroneStateServiceTest` |
| Mediator | `MissionMediatorTest` |
