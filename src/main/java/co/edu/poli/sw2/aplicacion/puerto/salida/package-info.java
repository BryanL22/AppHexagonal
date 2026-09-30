/**
 * Puertos de salida (driven ports): lo que la aplicacion necesita del exterior.
 *
 * <p>Una interfaz por operacion de almacenamiento. Las consumen los servicios
 * de {@link co.edu.poli.sw2.aplicacion.servicio} y las implementa el adaptador
 * {@link co.edu.poli.sw2.infraestructura.persistencia.MySqlDronRepository}.
 * {@link co.edu.poli.sw2.aplicacion.puerto.salida.PersistenciaException} es el
 * unico error que pueden propagar.</p>
 */
package co.edu.poli.sw2.aplicacion.puerto.salida;
