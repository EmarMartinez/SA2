package com.bosonit.sa2.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.nio.file.Path;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Archivo {
    @Id
    @GeneratedValue
    int id;
    String nombrearchivo;
    Date fechaSubida;
    String categoria;
    String url;
}
