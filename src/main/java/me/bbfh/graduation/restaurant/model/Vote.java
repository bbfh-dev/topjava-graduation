package me.bbfh.graduation.restaurant.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import me.bbfh.graduation.common.model.BaseEntity;
import me.bbfh.graduation.user.model.User;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "vote", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "vote_date"}, name = "vote_unique_user_voted_at_idx")
}, indexes = {@Index(columnList = "restaurant_id, vote_date", name = "vote_restaurant_date_idx")})
public class Vote extends BaseEntity {

    @Column(name = "vote_date", nullable = false, columnDefinition = "timestamp with timezone default now()")
    @NotNull
    private Date voteDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Restaurant restaurant;
}
