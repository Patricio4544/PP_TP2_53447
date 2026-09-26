import excepciones.CupoExcedidoException;
import modelo.Estudiante;
import modelo.EventoUniversitario;
import modelo.Sala;
import modelo.Inscripcion;
import modelo.actividades.Actividad;
import modelo.actividades.Charla;
import modelo.actividades.Curso;
import modelo.actividades.Taller;
import modelo.certificacion.Certificable;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        List<Estudiante> estudiantes = new ArrayList<>();
        Scanner input = new Scanner(System.in);
        System.out.printf("REGISTRO DE ESTUDIANTES%n=======================%n%n");
        boolean continuar = true;

        while (continuar) {
            System.out.print("Ingrese legajo del estudiante: ");
            String legajo = input.nextLine();
            System.out.print("Ingrese nombre del estudiante: ");
            String nombre = input.nextLine();
            estudiantes.add(new Estudiante(legajo, nombre));
            System.out.printf("%n Desea añadir mas estudiantes? (S/N): ");
            String respuesta = input.nextLine().toLowerCase();
            continuar = respuesta.equals("s");
        }

        System.out.printf("%n%nREGISTRO DE EVENTOS%n=======================%n%n");
        continuar = true;
        int id = 1;

        while (continuar) {
            System.out.print("Ingrese titulo del evento: ");
            String titulo = input.nextLine();
            System.out.print(" El evento es gratuito para los participantes? (S/N): " );
            boolean gratuito = input.nextLine().toLowerCase().equals("s");
            System.out.print("Ingrese costo base del evento (0 si puso gratuito antes): ");
            double costoBase = input.nextDouble();
            input.nextLine();

            EventoUniversitario evento = new EventoUniversitario("EVT-" + id, titulo, gratuito, costoBase);

            System.out.printf("%n%nREGISTRO DE LA SALA DEL EVENTO%n=======================%n%n");
            System.out.print("Ingrese el nombre sala donde se va a realizar dicho evento: ");
            String nombreSala = input.nextLine();
            Sala sala = new Sala(id, nombreSala);
            evento.asignarSala(sala);

            System.out.printf("%n%nREGISTRO DE ACTIVIDADES DEL EVENTO %s%n=======================%n%n", evento.getTitulo());
            int idAct = 1;
            boolean continuarAct = true;

            while (continuarAct) {
                System.out.print("Ingrese titulo de la actividad: ");
                String tituloAct = input.nextLine();
                System.out.print("Ingrese cupo maximo de la actividad: ");
                int cupoMax = input.nextInt();
                input.nextLine();
                System.out.print(" El evento es charla, taller o curso? (charla/taller/curso): ");
                String tipo = input.nextLine().trim().toLowerCase();
                evento.crearActividad(idAct, tituloAct, cupoMax, tipo);
                System.out.print(" Desea crear otra actividad? (S/N): ");
                continuarAct = input.nextLine().toLowerCase().equals("s");
                idAct++;
            }

            try {
                System.out.printf("%n%nINSCRIPCION DE ESTUDIANTES A ACTIVIDADES%n===============%n%n");
                boolean continuarInsc = true;
                while (continuarInsc) {
                    System.out.print("Ingrese el legajo del alumno: ");
                    String legajo = input.nextLine();
                    System.out.print("Ingrese el Id de la actividad: ");
                    int idActividadBuscada = input.nextInt();
                    input.nextLine();

                    for (Estudiante estudiante : estudiantes) {
                        if (estudiante.getLegajo().equals(legajo)) {
                            evento.getActividades().get(idActividadBuscada - 1).inscribir(estudiante);
                            System.out.println(" Estudiante inscrito con éxito!");
                        }
                    }
                    System.out.print(" Desea inscribir otro estudiante? (S/N): ");
                    continuarInsc = input.nextLine().toLowerCase().equals("s");
                }
            } catch (CupoExcedidoException e) {
                System.out.println("Error al inscribir: " + e.getMessage());
            }

            try {
                evento.persistirEvento();
                EventoUniversitario copiaDesdeArchivo = evento.recuperarEvento(evento.getId());
                System.out.printf("%n%nDATOS DEL EVENTO REGISTRADO:%n%n");
                evento.mostrarDatos();
                System.out.println("\n Datos del evento recuperado desde archivo:");
                copiaDesdeArchivo.mostrarDatos();
            } catch (FileNotFoundException e) {
                System.out.println("Error 01: No se encontro archivo del evento: " + e.getMessage());
            } catch (ClassNotFoundException e) {
                System.out.println("No fue posible reconstruir el objeto almacenado: " + e.getMessage());
            } catch (IOException e) {
                System.out.println("Se produjo un error de entrada/salida: " + e.getMessage());
            }

            for (Actividad actividad: evento.getActividades()) {
                if (actividad instanceof Certificable certificable) {
                    System.out.println("\n--- Certificados para " + actividad.getTitulo() + " ---");
                    for (Inscripcion inscripcion: actividad.getInscripciones()) {
                        String certificado = certificable.generarCertificado(inscripcion.getEstudiante());
                        System.out.println(certificado);
                    }
                }
            }

            List charlas = evento.filtrarActividadesPorTipo(Charla.class);
            List talleres = evento.filtrarActividadesPorTipo(Taller.class);
            List cursos = evento.filtrarActividadesPorTipo(Curso.class);

            System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            System.out.println("Actividades filtradas por tipo para el evento " + evento.getTitulo() + ":");
            System.out.println("    | Charlas encontradas: " + charlas.size());
            System.out.println("    | Talleres encontrados: " + talleres.size());
            System.out.println("    | Cursos encontrados: " + cursos.size());
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

            System.out.println("Costo de materiales de cada actividad:");
            System.out.println("    | Costo total de materiales en talleres: $" + evento.calcularCostoMateriales(talleres));
            System.out.println("    | Costo total de materiales en cursos: $" + evento.calcularCostoMateriales(cursos));
            System.out.println("    | Costo total de materiales en charlas: $" + evento.calcularCostoMateriales(charlas));
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");

            System.out.print("%n Desea registrar otro evento? (S/N): ");
            continuar = input.nextLine().toLowerCase().equals("s");
            id++;
        }

        System.out.println("Total de eventos creados históricamente: " + EventoUniversitario.getCantidadEventos());
    }
}