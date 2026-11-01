package co.edu.poli.sw2.dominio.command;

import co.edu.poli.sw2.aplicacion.servicio.DroneOperacionService;

public class TomarFotografiaCommand implements DroneCommand {

    private DroneOperacionService servicio;

    public TomarFotografiaCommand(DroneOperacionService servicio) {
        this.servicio = servicio;
    }

    @Override
    public String ejecutar() {
        return servicio.tomarFotografia();
    }
}
