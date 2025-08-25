package me.bbfh.graduation.restaurant;

import lombok.experimental.UtilityClass;
import me.bbfh.graduation.restaurant.model.Vote;
import me.bbfh.graduation.restaurant.to.VoteTo;

import java.util.List;

@UtilityClass
public class VoteUtil {
    public static List<VoteTo> getTos(List<Vote> votes) {
        return votes.stream().map(VoteTo::new).toList();
    }

    public static VoteTo getTo(Vote vote) {
        return (vote == null) ? null : new VoteTo(vote);
    }
}
