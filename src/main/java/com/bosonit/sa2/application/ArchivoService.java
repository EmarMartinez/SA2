package com.bosonit.sa2.application;

import com.bosonit.sa2.domain.Archivo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface ArchivoService {
    Archivo guardarDatos(MultipartFile file, String categoria);

    ResponseEntity<?> getPorNombre(String nombre);

    ResponseEntity<?> findById(int id);

    ResponseEntity<?> subirArchivoTipo(MultipartFile file, String tipo) throws Exception;
}
