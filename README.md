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
| `aplicacion.puerto.entrada`    | 5 casos de uso: `CrearDron`, `ConsultarDron`, `ConsultarDrones`, `ActualizarDron`, `EliminarDron` (sufijo `UseCase`). |
| `aplicacion.puerto.salida`     | 5 puertos: `GuardarDron`, `BuscarDron`, `BuscarDrones`, `ActualizarDron`, `EliminarDron` (sufijo `Port`) y `PersistenciaException`. |
| `aplicacion.servicio`          | Un servicio por caso de uso: `CrearDronServicio`, `ConsultarDronServicio`, `ConsultarDronesServicio`, `ActualizarDronServicio`, `EliminarDronServicio`. |
| `infraestructura.persistencia` | `MySqlDronRepository` (implementa los 5 puertos de salida con SQL) y `ConexionBD` (Singleton JDBC).  |
| `infraestructura.ui`           | `MainController`: adaptador de entrada JavaFX.                                                       |
| `vista`                        | `App`: punto de entrada; crea y conecta todas las piezas.                                           |

La vista (`GestorDrones.fxml`) y la hoja de estilos están en
`src/main/resources/co/edu/poli/sw2/`. El proyecto es modular: su
`module-info.java` declara las dependencias de JavaFX y `java.sql`.

### Decisiones de diseño

- **Un puerto por operación.** Hay 5 interfaces de entrada y 5 de salida, en
  vez de una sola interfaz con todo el CRUD.
- **Un servicio por caso de uso.** Cada servicio recibe por constructor solo
  los puertos de salida que usa.
- **Cambiar de base de datos solo toca `App`.** Para otro motor bastaría con
  escribir otro adaptador que implemente los mismos 5 puertos y cambiar la
  línea donde se crea `MySqlDronRepository`.
- **`App` es la raíz de composición.** Es la única clase que hace `new` de las
  piezas del hexágono. Como `MainController` recibe los casos de uso por
  constructor, `App` lo crea con `FXMLLoader.setControllerFactory(...)`.
- **La tecnología no cruza hacia el núcleo.** `MySqlDronRepository` convierte
  `SQLException` e `IOException` en `PersistenciaException`, que es unchecked
  y está definida junto a los puertos de salida.
- **Patrón de diseño: Singleton.** `ConexionBD` garantiza una única conexión
  JDBC compartida por toda la aplicación.
- **Las reglas de negocio viven en el núcleo, no en la interfaz.**
  `Drone.validar()` exige ID, serial, modelo y fabricante, y un peso mayor
  que 0; `Agricultura` añade que la capacidad del tanque sea mayor que 0.
  `CrearDronServicio` y `ActualizarDronServicio` llaman a esa validación
  antes de guardar, y `CrearDronServicio` además impide repetir un ID. Si una
  regla no se cumple, el servicio lanza `IllegalArgumentException` y el
  controlador solo muestra el mensaje.

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
