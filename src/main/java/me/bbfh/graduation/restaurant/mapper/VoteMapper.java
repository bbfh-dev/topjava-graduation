package me.bbfh.graduation.restaurant.mapper;

import me.bbfh.graduation.restaurant.model.Vote;
import me.bbfh.graduation.restaurant.to.VoteTo;

import java.util.List;

public class VoteMapper {
    public static VoteTo toTo(Vote vote) {
        return new VoteTo(vote.getId(), vote.getMenu().getId(), vote.getVoteDate());
    }

    public static List<VoteTo> toTos(List<Vote> votes) {
        return votes.stream().map(VoteMapper::toTo).toList();
    }
}
