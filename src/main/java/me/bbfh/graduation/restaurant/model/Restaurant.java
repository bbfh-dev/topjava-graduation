package me.bbfh.graduation.restaurant.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import me.bbfh.graduation.common.HasId;
import me.bbfh.graduation.common.model.NamedEntity;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
@Table(name = "restaurant")
public class Restaurant extends NamedEntity {

    public Restaurant(Integer id, String name) {
        super(id, name);
    }
}
