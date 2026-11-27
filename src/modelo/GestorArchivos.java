package modelo;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class GestorArchivos {

    // Mira la extensión del archivo y usa la función que corresponde
    public static String leerArchivo(File archivo) throws Exception {
        String nombre = archivo.getName().toLowerCase();

        if (nombre.endsWith(".pdf")) {
            return leerpdf(archivo);
        } else if (nombre.endsWith(".docx")) {
            return leerdocx(archivo);
        } else if (nombre.endsWith(".txt")) {
            return leertxt(archivo);
        } else {
            throw new Exception("tipo de archivo no soportado (solo .txt, .pdf y .docx)");
        }
    }

    public static String leertxt(File archivo) throws Exception {
        return Files.readString(archivo.toPath());
    }

    public static String leerpdf(File archivo) throws Exception {
        PDDocument documento = Loader.loadPDF(archivo);
        PDFTextStripper lector = new PDFTextStripper();
        String contenido = lector.getText(documento);
        documento.close();
        return contenido;
    }

    public static String leerdocx(File archivo) throws Exception {
        // Un .docx es un ZIP; el texto está dentro, en el archivo word/document.xml
        ZipFile zip = new ZipFile(archivo);
        ZipEntry entrada = zip.getEntry("word/document.xml");
        String xml = new String(zip.getInputStream(entrada).readAllBytes(), StandardCharsets.UTF_8);
        zip.close();

        // Cada </w:p> es el final de un párrafo: lo cambiamos por un salto de línea
        String contenido = xml.replace("</w:p>", "\n");
        // Quitamos todas las etiquetas XML (todo lo que está entre < y >)
        contenido = contenido.replaceAll("<[^>]+>", "");
        // El XML escribe algunos símbolos con códigos; los devolvemos a su forma normal
        contenido = contenido.replace("&lt;", "<").replace("&gt;", ">")
                .replace("&quot;", "\"").replace("&apos;", "'").replace("&amp;", "&");
        return contenido;
    }
}
