package me.bbfh.graduation.restaurant.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.bbfh.graduation.common.model.BaseEntity;
import me.bbfh.graduation.common.validation.NoHtml;

@Entity
@Table(name = "restaurant")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Restaurant extends BaseEntity {
    @Column(name = "name", nullable = false, unique = true)
    @NotBlank
    @Size(max = 64)
    @NoHtml   // https://stackoverflow.com/questions/17480809
    private String name;
}
