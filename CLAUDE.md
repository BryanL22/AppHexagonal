# AppHexagonal

Aplicación JavaFX de gestión de drones (proyecto académico de Ingeniería de
Software II, Politécnico Grancolombiano), reescrita con **arquitectura
hexagonal**. Viene de migrar el proyecto `AppDrones`, que usaba MVC y nueve
patrones de diseño.

La estructura sigue el ejemplo del profesor (`examen2_Soto`, en
`Documentos\HexagonalJavaFxPostgres-main`), tomando solo su lado MySQL:
nosotros usamos MySQL, no PostgreSQL.

## Arquitectura

Cuatro paquetes bajo `co.edu.poli.sw2`:

```
dominio/
  modelo/            Drone, Agricultura, Vigilancia, Mision, Piloto, Sensor
aplicacion/
  puerto/entrada/    5 interfaces: CrearDron, ConsultarDron, ConsultarDrones,
                     ActualizarDron, EliminarDron  (sufijo UseCase)
  puerto/salida/     5 interfaces: GuardarDron, BuscarDron, BuscarDrones,
                     ActualizarDron, EliminarDron  (sufijo Port)
                     + PersistenciaException
  servicio/          Un servicio por caso de uso (CrearDronServicio, ...);
                     cada uno recibe solo los puertos de salida que usa
infraestructura/
  persistencia/      ConexionBD (Singleton) y MySqlDronRepository
                     (implementa los 5 puertos de salida; contiene el SQL)
  ui/                MainController — adaptador de entrada
vista/
  App.java           punto de entrada; conecta todo el hexágono
module-info.java     módulo co.edu.poli.sw2
```

Tests en `src/test/java/co/edu/poli/sw2/tests/unitaria`.

### Reglas que no se rompen

1. **El dominio no importa nada externo.** Solo `java.*` y su propio paquete.
   Nada de JavaFX, JDBC ni infraestructura. Se puede comprobar con:
   ```
   grep -rh "^import" src/main/java/co/edu/poli/sw2/dominio/ | sort -u
   ```
2. **Los puertos son interfaces**, una por operación. No agrupar el CRUD en una
   sola interfaz: el enunciado pide 5 de entrada y 5 de salida.
3. **El controlador no tiene lógica de negocio.** Lee y escribe controles de la
   vista y dispara casos de uso. No conoce el repositorio, ni `SQLException`, ni SQL.
4. **`App` es el único lugar que instancia dependencias** (composition root).
   Ninguna otra clase hace `new` de sus colaboradores.
5. **El único patrón de diseño es el Singleton** (`ConexionBD`). El profesor solo
   pide ese; los demás se eliminaron a propósito. No reintroducir factorías,
   Builder, Prototype, Decorator, Bridge, Adapter, Composite, Facade ni Proxy.

### Detalles que sorprenden

- `MainController` **no tiene constructor vacío**: recibe los 5 casos de uso.
  Por eso `App` carga el FXML con `loader.setControllerFactory(...)`. Si se
  quita esa línea, el FXML falla al cargar.
- Los recursos se cargan con **ruta absoluta** (`/co/edu/poli/sw2/view/...`)
  porque `App` no está en el mismo paquete que los recursos.
- `module-info.java` abre `dominio.modelo` a `javafx.base` porque la tabla usa
  `PropertyValueFactory` sobre `Drone`. El driver de MySQL no se declara con
  `requires`: `DriverManager` lo encuentra como servicio.
- `MySqlDronRepository` traduce `SQLException`/`IOException` a
  `PersistenciaException` (unchecked, definida en el puerto de salida) para que
  la tecnología no se filtre hacia el dominio.
- `MySqlDronRepository` crea y migra las tablas al conectarse. Existen tablas de `piloto`,
  `sensor`, `mision` y `mision_drone`, pero **solo hay CRUD en Java para
  drones**.

## Ejecutar y probar

```bash
mvn javafx:run     # arranca la aplicación (requiere MySQL en marcha)
mvn test           # tests del dominio
mvn compile
mvn javadoc:javadoc  # HTML en target/site/apidocs; doclint "all" falla si falta Javadoc
```

Antes de la primera ejecución: copiar `.env.example` como `.env` y completar
las credenciales de MySQL. El `.env` está en `.gitignore`.

Si MySQL no está disponible, la app muestra un alert de error durante
`initialize()` y se queda esperando a que se cierre ese diálogo.

## Convenciones de este repo

- Javadoc en español, sin tildes en el código fuente (sí en los textos de la
  interfaz).
- Los commits **no** llevan línea de co-autoría de Claude.
