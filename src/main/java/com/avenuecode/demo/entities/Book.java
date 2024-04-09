package com.avenuecode.demo.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "book", schema = "public")
public class Book {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Column(name = "editora", nullable = false)
    private String editora;

    @Column(name = "isbn", nullable = false)
    private String isbn;

    @JsonFormat(pattern="dd/MM/yyyy")
    @Column(name = "data_publicacao", nullable = false)
    private Date dataPublicacao;
}

