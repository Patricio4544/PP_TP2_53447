package modelo.actividades;

import excepciones.CupoExcedidoException;
import modelo.Estudiante;
import modelo.Inscripcion;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class Actividad implements Serializable {
    protected int id;
    protected String titulo;
    protected int cupoMaximo;
    public final static int CUPO_MINIMO = 5;  // se mantiene constante para todas las actividades
    protected List<Inscripcion> inscripciones; //lo pide al final del punto 2



    public Actividad(int id, String titulo, int cupoMaximo) {
        this.id = id;
        this.titulo = titulo;
        this.inscripciones = new ArrayList<>();
        if (cupoMaximo < CUPO_MINIMO) {
            this.cupoMaximo = CUPO_MINIMO;
        } else {
            this.cupoMaximo = cupoMaximo;
        }
    }

    public Inscripcion inscribir(Estudiante estudiante) throws CupoExcedidoException {
        if (inscripciones.size() >= cupoMaximo) {
            throw new CupoExcedidoException("No puede inscribirse al estudiante " + estudiante.getNombre() + ". Cupo maximo excedido");
        }
        Inscripcion inscripcion = new Inscripcion(LocalDate.now(), "REGISTRADA", this, estudiante);
        inscripciones.add(inscripcion);
        return inscripcion;
    }

    public List<Inscripcion> getInscripciones() {
        return inscripciones;
    }

    public void mostrarInscripciones() {
        System.out.printf("    Inscriptos (%d/%d):%n", this.inscripciones.size(), this.cupoMaximo);
        if (this.inscripciones.isEmpty()) {
            System.out.println("      - No hay inscriptos aún.");
        } else {
            for (Inscripcion inscripcion : this.inscripciones) {
                System.out.printf("      - %s (Legajo: %s)%n",
                        inscripcion.getEstudiante().getNombre(),
                        inscripcion.getEstudiante().getLegajo());
            }
        }
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getCupoMaximo() {
        return cupoMaximo;
    }

    public void setCupoMaximo(int cupoMaximo) {
        this.cupoMaximo = cupoMaximo;
    }

    public final void mostrarIdentificacion() {
        System.out.println("- " + getTipo() + ": " + titulo + " (id=" + id + ")" + "- Cupo máximo: " + cupoMaximo );
    }

    public abstract double calcularCostoMateriales();

    public abstract String getTipo();


}
