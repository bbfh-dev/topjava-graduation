package me.bbfh.graduation.restaurant.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.bbfh.graduation.common.model.BaseEntity;
import me.bbfh.graduation.user.model.User;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "votes", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "vote_date"}, name = "vote_unique_user_voted_at_idx")
}, indexes = {@Index(columnList = "restaurant_id, vote_date", name = "vote_restaurant_date_idx")})
public class Vote extends BaseEntity {

    @Column(name = "vote_date", nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull
    private LocalDate voteDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Menu menu;

    public Vote(Integer id, LocalDate voteDate, User user, Menu menu) {
        super(id);
        this.voteDate = voteDate;
        this.user = user;
        this.menu = menu;
    }

    public Vote(User user, Menu menu) {
        this(null, LocalDate.now(), user, menu);
    }
}
