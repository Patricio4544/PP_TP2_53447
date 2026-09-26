package modelo.actividades;
 import modelo.Estudiante;
 import modelo.certificacion.Certificable;

public class Curso extends Actividad implements Certificable{
    private int nivel;

    public Curso(int id, String titulo, int nivel, int cupo) {
        super(id, titulo, cupo);
        this.nivel = nivel;
    }

    public Curso(int id, String titulo, int cupo) { super(id, titulo, cupo);}

    @Override
    public double calcularCostoMateriales() {
        switch (nivel) {
            case 1:
                return 1000.0;
            case 2:
                return 2000.0;
            case 3:
                return 3000.0;
            default:
                return 0.0;
        }
    }

    @Override
    public String getTipo() {
        return this.getClass().getName();
    }


    @Override
    public String generarCertificado(Estudiante estudiante) {
        return "Certificado emitido por" + Entidad_Emisora + estudiante.getNombre() + "participo de: "
                + getTitulo();
    }


}
