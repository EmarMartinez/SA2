package com.bosonit.sa2.controller;

import java.io.IOException;
import java.util.stream.Collectors;

import com.bosonit.sa2.application.ArchivoServiceImpl;
import com.bosonit.sa2.application.StorageService;
import com.bosonit.sa2.exceptions.StorageFileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;



@Controller

public class controller {

    private final StorageService storageService;

    @Autowired
    ArchivoServiceImpl archivoServiceImpl;

    @Autowired
    public controller(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/")
    public String listUploadedFiles(Model model) throws IOException {

        model.addAttribute("files", storageService.loadAll().map(
                        path -> MvcUriComponentsBuilder.fromMethodName(controller.class,
                                "serveFile", path.getFileName().toString()).build().toUri().toString())
                .collect(Collectors.toList()));

        return "uploadForm";
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes,@RequestParam String categoria) {

        storageService.store(file);

        archivoServiceImpl.guardarDatos(file, categoria);

        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");


        return "redirect:/";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

    @GetMapping("archivo/nombre/{nombre}")
    public ResponseEntity<?> getFicheroPorNombre(@PathVariable String nombre) {

        try {
            return archivoServiceImpl.getPorNombre(nombre);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping("archivo/id/{id}")
    public ResponseEntity<?> getFicheroPorId(@PathVariable int id) {

        try {
            return archivoServiceImpl.findById(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("upload/{tipo}")
    public ResponseEntity<?> subirFicheroTipo(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes,@PathVariable String tipo) {
        try {
            return archivoServiceImpl.subirArchivoTipo(file, tipo);
        } catch (Exception e) {
            throw new RuntimeException("Extension no valida");
        }
    }

}