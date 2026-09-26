package modelo.certificacion;
import modelo.Estudiante;
public interface Certificable {
    String Entidad_Emisora = "UTN - FRM";
    String generarCertificado (Estudiante estudiante);
}

