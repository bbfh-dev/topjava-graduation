package me.bbfh.graduation.restaurant.web;

import me.bbfh.graduation.restaurant.model.Vote;
import me.bbfh.graduation.restaurant.repository.MenuRepository;
import me.bbfh.graduation.restaurant.repository.VoteRepository;
import me.bbfh.graduation.restaurant.to.VoteTo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static me.bbfh.graduation.restaurant.VoteTestData.*;
import static me.bbfh.graduation.user.UserTestData.USER_MAIL;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class VoteControllerTest extends AbstractRestaurantControllerTest {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Test
    @WithUserDetails(value = USER_MAIL)
    void history() throws Exception {
        ResultActions action = perform(MockMvcRequestBuilders.get(ProfileVoteController.REST_URL + "/history"))
                .andDo(print())
                .andExpect(status().isOk());

        List<VoteTo> createdList = VOTE_TO_MATCHER.readListFromJson(action);

        Map<Integer, Vote> expectedMap = VOTES.stream()
                .collect(Collectors.toMap(Vote::getId, r -> r));

        for (VoteTo created : createdList) {
            Vote expected = expectedMap.get(created.getId());
            if (expected == null) {
                throw new IllegalStateException("There must be no extra restaurants");
            }
            VoteTo expectedTo = new VoteTo(expected);
            VOTE_TO_MATCHER.assertMatch(created, expectedTo);
            VOTE_MATCHER.assertMatch(voteRepository.getExisted(created.id()), expected);
        }
    }
}
