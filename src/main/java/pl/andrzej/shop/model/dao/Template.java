package pl.andrzej.shop.model.dao;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;

@Data
@AllArgsConstructor
@Entity
@Table(indexes = @Index(name = "idx_name", columnList = "name", unique = true))
public class Template {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Lob
    private String body;

    private String title;

}
