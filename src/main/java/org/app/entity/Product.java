package org.app.entity;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    @Column(length = 20)
    @NotEmpty(message = "Поле не может быть пустым")
    @Size(min = 2, max = 20, message = "Название продукта не должно быть меньше 2 знаков и больше 20")
    private String name;

    @Column
    @NotNull(message = "Поле не может быть пустым")
    @Min(value = 0, message = "Стоимость должна быть положительной")
    private Integer price;

    @Column(length = 50)
    @NotEmpty(message = "Поле не может быть пустым")
    @Size(min = 2, max = 50, message = "Название страны не должно быть меньше 2 знаков и больше 50")
    @Pattern(regexp = "[\\D]+", message = "Название страны не должно содержать цифр")
    private String country;

    @Column
    private String note;

    @Column (name = "active", columnDefinition = "bit")
    private Boolean active = true;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer cost) {
        this.price = cost;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean archive) {
        this.active = archive;
    }
}
