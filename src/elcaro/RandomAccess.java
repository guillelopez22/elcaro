/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elcaro;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Vector;

/**
 *
 * @author Don Papi
 */
public class RandomAccess {

    private static RandomAccessFile raf;
    private static int record_number;
    private static int record_size = 80;
    
    public static void createFile(File file) throws IOException {
        if (file.exists() && !file.isFile()) {
            throw new IOException(file.getName() + " no es un archivo");
        }
        raf = new RandomAccessFile(file, "rw");
        record_number = (int) Math.ceil((double) raf.length() / (double) record_size);
    }
    
    public static void closeFile() throws IOException {
        raf.close();
    }
    
    public static void setRecord(int i, Record data) throws IOException {
        
        raf.seek(i * record_size);
        
        
    }
    
    public static boolean deleteRecord(String aEliminar) throws IOException {
        int pos = searchRecord(aEliminar);
        if (pos == -1) {
            return false;
        }
        Record deletedRecord = getRecord(pos);
        deletedRecord.setInactive();
        setRecord(pos, deletedRecord);
        return true;
    }
    
    public static void compactarArchivo(File archivo) throws IOException {
        createFile(archivo); // Abrimos el flujo.
        Record[] listado = new Record[record_number];
        for (int i = 0; i < record_number; ++i) {
            listado[i] = getRecord(i);
        }
        closeFile(); // Cerramos el flujo.
        archivo.delete(); // Borramos el archivo.

        File tempo = new File("temporal.dat");
        createFile(tempo); // Como no existe se crea.
        for (Record p : listado) {
            if (p.isActive()) {
                addRecord(p);
            }
        }
        closeFile();
        
        tempo.renameTo(archivo); // Renombramos.
    }
    
    public static void addRecord(Record record) throws IOException {
        int inactivo = searchInactiveRecord();
        if (setRecord(inactivo == -1 ? record_number : inactivo, record)) {
            record_number++;
        }        
    }
    
    public static int getNumeroRegistros() {
        return record_number;
    }
    
    public static Record getRecord(int i) throws IOException {
        if (i >= 0 && i <= getNumeroRegistros()) {
            raf.seek(i * record_size);
            return new Record(raf.readUTF(), raf.readInt(), raf.readBoolean());
        } else {
            System.out.println("\nNúmero de registro fuera de límites.");
            return null;
        }
    }
    
    public static int searchRecord(String buscado) throws IOException {
        Record p;
        if (buscado == null) {
            return -1;
        }
        for (int i = 0; i < getNumeroRegistros(); i++) {
            raf.seek(i * record_size);
            p = getRecord(i);
            if (p.equals(buscado)) {
                return i;
            }
        }
        return -1;
    }

    private static int searchInactiveRecord() throws IOException {
        String nombre;
        for (int i = 0; i < getNumeroRegistros(); i++) {
            raf.seek(i * record_size);
            if (!getRecord(i).isActive()) {
                return i;
            }
        }
        return -1;        
    }
}
