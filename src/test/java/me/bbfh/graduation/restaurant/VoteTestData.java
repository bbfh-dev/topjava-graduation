package me.bbfh.graduation.restaurant;

import me.bbfh.graduation.MatcherFactory;
import me.bbfh.graduation.restaurant.model.Vote;
import me.bbfh.graduation.restaurant.to.VoteTo;

import java.time.LocalDate;
import java.util.List;

import static me.bbfh.graduation.restaurant.MenuTestData.MENU_1;
import static me.bbfh.graduation.restaurant.MenuTestData.MENU_2;
import static me.bbfh.graduation.user.UserTestData.admin;
import static me.bbfh.graduation.user.UserTestData.user;

public class VoteTestData {
    public static final MatcherFactory.Matcher<VoteTo> VOTE_TO_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(VoteTo.class);
    public static final MatcherFactory.Matcher<Vote> VOTE_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Vote.class, "user");

    private static final int VOTE1_ID = 1;

    public static final LocalDate VOTE_DATE = LocalDate.of(2025, 8, 20);
    public static final LocalDate NEW_VOTE_DATE = VOTE_DATE.plusDays(1);

    public static final Vote VOTE_1 = new Vote(VOTE1_ID, VOTE_DATE, user, MENU_1);
    public static final Vote VOTE_2 = new Vote(VOTE1_ID + 1, VOTE_DATE, admin, MENU_1);
    public static final Vote NEW_VOTE = new Vote(null, NEW_VOTE_DATE, user, MENU_2);
    public static final Vote UPDATED_VOTE = new Vote(null, NEW_VOTE_DATE, user, MENU_2);

    public static final List<Vote> VOTES = List.of(VOTE_1, VOTE_2);

    public static VoteTo.RestTo getNewVote() {
        return new VoteTo.RestTo(MENU_2.getId());
    }

    public static VoteTo.RestTo getUpdatedVote() {
        return new VoteTo.RestTo(MENU_2.getId());
    }
}
