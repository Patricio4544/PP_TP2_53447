package modelo;

import modelo.actividades.Actividad;
import modelo.actividades.Charla;
import modelo.actividades.Taller;
import modelo.actividades.Curso;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class EventoUniversitario implements Serializable {
    private final String id;
    private String titulo;
    private double costoBase;
    private boolean gratuito;
    private static int cantidadEventos;
    private Sala sala;
    private List<Actividad> actividades;

    public EventoUniversitario(String id, String titulo, boolean gratuito, double costoBase) {
        this.id = id;
        this.titulo = titulo;
        this.gratuito = gratuito;
        if (this.gratuito) {
            this.costoBase = 0.0;
        } else {
            this.costoBase = costoBase;
        }
        EventoUniversitario.cantidadEventos = cantidadEventos + 1;
        this.actividades = new ArrayList<>();
    }

    public EventoUniversitario(EventoUniversitario otro) {
        this.id = otro.id + "- COPIA";
        this.titulo = otro.titulo;
        this.gratuito = otro.gratuito;
        this.costoBase = otro.costoBase;
        this.actividades = new ArrayList<>();
    }

    public Sala getSala() { return sala; }

    public void asignarSala(Sala sala) {
        this.sala = sala;
    }


    public void crearActividad(int id, String titulo, int cupoMaximo, String tipoActividad) {
        Scanner scanner = new Scanner(System.in);
        switch (tipoActividad) {
            case "charla":
                System.out.print("Ingrese el nombre del disertante para la charla " + titulo + " : ");
                String disertante = scanner.nextLine();
                Actividad charla = new Charla(id, titulo, disertante, cupoMaximo);
                this.actividades.add(charla);
                break;
            case "taller":
                System.out.print("El taller " + titulo + " requiere el uso de Notebook? : S/N ");
                boolean requiereNotebook = scanner.nextLine().trim().toLowerCase().equals("s");
                Actividad taller = new Taller(id, titulo, requiereNotebook, cupoMaximo);
                this.actividades.add(taller);
                break;
            case "curso":
                System.out.print("Ingrese el nivel del curso (1, 2 o 3): ");
                int nivel = scanner.nextInt();
                Actividad curso = new Curso(id, titulo, nivel, cupoMaximo);
                this.actividades.add(curso);
                break;
            default:
                System.out.println("Error: Tipo de actividad no reconocido.");
        }
    }


    public  List filtrarActividadesPorTipo(Class tipo) {
        List resultado = new ArrayList<>();
        for (Actividad actividad : actividades) {
            if (tipo.isInstance(actividad)) {
                resultado.add(tipo.cast(actividad));
            }
        }
        return resultado;
    }


    public double calcularCostoMateriales(List<Actividad> listaActividades) {
        double total = 0.0;
        for (Actividad actividad : listaActividades) {
            total += actividad.calcularCostoMateriales();
        }
        return total;
    }

    public double calcularCostoEstimado() {
        if (this.gratuito) {
            return 0.0;
        }
        double costoTotal = costoBase;
        for (Actividad actividad : actividades) {
            costoTotal += actividad.calcularCostoMateriales();
        }
        return costoTotal * 1.21;
    }

    public List<Actividad> getActividades() {
        return Collections.unmodifiableList(actividades);
    }

    public String getId() { return id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public double getCostoBase() { return costoBase; }
    public void setCostoBase(double costoBase) { this.costoBase = costoBase; }
    public boolean getGratuito() { return gratuito; }
    public void setGratuito(boolean gratuito) { this.gratuito = gratuito; }

    public void mostrarDatos() {
        System.out.printf("========================================%n");
        System.out.printf("DATOS DEL EVENTO: %s%n", this.titulo);
        System.out.printf("========================================%n");
        System.out.printf("ID del evento: %s%n", this.id);
        System.out.printf("Costo estimado del evento: $%.2f%n", this.calcularCostoEstimado());
        System.out.printf("Sala asignada: %s (ID: %d)%n", this.sala.getNombre(), this.sala.getId());

        System.out.printf("%nActividades registradas: %d%n", this.actividades.size());
        for (Actividad actividad : this.actividades) {
            actividad.mostrarIdentificacion();
            actividad.mostrarInscripciones();
        }
        System.out.printf("========================================%n%n");
    }

    public static int getCantidadEventos() {
        return EventoUniversitario.cantidadEventos;
    }

    public boolean persistirEvento() throws IOException {
        String nombreArchivo = "evento_" + this.id + ".dat";
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(nombreArchivo))) {
            oos.writeObject(this);
            return true;
        }
    }

    public EventoUniversitario recuperarEvento(String id) throws IOException, ClassNotFoundException {
        String nombreArchivo = "evento_" + id + ".dat";
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(nombreArchivo))) {
            return (EventoUniversitario) ois.readObject();
        }
    }
}