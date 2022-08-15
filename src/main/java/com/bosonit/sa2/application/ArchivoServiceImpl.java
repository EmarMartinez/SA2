package com.bosonit.sa2.application;


import com.bosonit.sa2.domain.Archivo;
import com.bosonit.sa2.infraestructure.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

@Service
public class ArchivoServiceImpl implements ArchivoService{

    @Autowired
    FileRepository fileRepository;


    private final Path rootLocation;

    @Autowired
    public ArchivoServiceImpl(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    public Archivo guardarDatos(MultipartFile file, String categoria) {
        Archivo archivo = new Archivo();
        archivo.setCategoria(categoria);
        archivo.setNombrearchivo(file.getOriginalFilename());
        archivo.setFechaSubida(new Date());

//        archivo.setUrl(this.rootLocation.resolve(file.getOriginalFilename()).toString());
        Path test = this.rootLocation.resolve(file.getOriginalFilename());
        try {
            Resource resource = new UrlResource(test.toUri());
//            System.out.println(resource.toString());
            archivo.setUrl(resource.toString());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        archivo.setId(fileRepository.save(archivo).getId());

        return archivo;

    }

    @Override
    public ResponseEntity<?> getPorNombre(String nombre) {

        try {
            return new ResponseEntity<>(fileRepository.findByNombre(nombre), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

    }

    @Override
    public ResponseEntity<?> findById(int id) {
        try {
            return new ResponseEntity<>(fileRepository.findById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<?> subirArchivoTipo(MultipartFile file, String tipo) throws Exception{
        String nombre = file.getOriginalFilename();
        String[] trozos = nombre.split("\\.");
//        System.out.println(file.getOriginalFilename().toString());
//        System.out.println(trozos);
        try {
            if(trozos[1].equals(tipo)) {
                return new ResponseEntity<>(this.guardarDatos(file, ""), HttpStatus.OK);
            }
            else {
                throw new Exception("La extensión no coincide");
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}
