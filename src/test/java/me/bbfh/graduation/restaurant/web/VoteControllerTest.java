package me.bbfh.graduation.restaurant.web;

import me.bbfh.graduation.common.util.DateTimeUtil;
import me.bbfh.graduation.common.util.JsonUtil;
import me.bbfh.graduation.restaurant.mapper.VoteMapper;
import me.bbfh.graduation.restaurant.model.Vote;
import me.bbfh.graduation.restaurant.repository.VoteRepository;
import me.bbfh.graduation.restaurant.to.VoteTo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static me.bbfh.graduation.restaurant.VoteTestData.*;
import static me.bbfh.graduation.user.UserTestData.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class VoteControllerTest extends AbstractRestaurantControllerTest {

    @Autowired
    private VoteRepository voteRepository;

    private void getAllAndAssert(String endpoint, Predicate<Vote> filter) throws Exception {
        ResultActions action = perform(MockMvcRequestBuilders.get(endpoint))
                .andDo(print())
                .andExpect(status().isOk());
        List<VoteTo> createdList = VOTE_TO_MATCHER.readListFromJson(action);

        Map<Integer, Vote> expectedMap = VOTES.stream()
                .filter(filter)
                .collect(Collectors.toMap(Vote::getId, r -> r));

        for (int i = 0; i < expectedMap.size(); i++) {
            VoteTo created = createdList.get(i);
            Vote expected = expectedMap.get(created.getId());
            if (expected == null) {
                throw new IllegalStateException("There must be no extra restaurants");
            }
            VoteTo expectedTo = VoteMapper.toTo(expected);
            VOTE_TO_MATCHER.assertMatch(created, expectedTo);
        }
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getAll() throws Exception {
        getAllAndAssert(ProfileVoteController.REST_URL,
                vote -> vote.getUser().equals(user));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getToday() throws Exception {
        DateTimeUtil.overrideCurrentDate(VOTE_DATE);
        getAllAndAssert(VoteController.REST_URL + "/today",
                vote -> vote.getVoteDate().equals(DateTimeUtil.getCurrentDate()));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void vote() throws Exception {
        DateTimeUtil.overrideCurrentDate(NEW_VOTE_DATE);
        ResultActions action = perform(MockMvcRequestBuilders.post(VoteController.REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(getNewVote())))
                .andDo(print())
                .andExpect(status().isCreated());

        VoteTo created = VOTE_TO_MATCHER.readFromJson(action);
        NEW_VOTE.setId(created.getId());
        VOTE_TO_MATCHER.assertMatch(created, VoteMapper.toTo(NEW_VOTE));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void voteNotAllowed() throws Exception {
        DateTimeUtil.overrideCurrentDate(VOTE_DATE);
        perform(MockMvcRequestBuilders.post(VoteController.REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(getNewVote())))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void change() throws Exception {
        DateTimeUtil.overrideCurrentDate(NEW_VOTE_DATE);
        DateTimeUtil.overrideCurrentTime(DateTimeUtil.VOTE_TIME_LIMIT.minusMinutes(1));
        ResultActions action = perform(MockMvcRequestBuilders.put(VoteController.REST_URL + "/" + VOTES.getFirst().getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(getUpdatedVote())))
                .andDo(print())
                .andExpect(status().isOk());

        VoteTo created = VOTE_TO_MATCHER.readFromJson(action);
        UPDATED_VOTE.setId(created.getId());
        Assertions.assertEquals(created.getMenuId(), UPDATED_VOTE.getMenu().getId());
        VOTE_TO_MATCHER.assertMatch(created, VoteMapper.toTo(UPDATED_VOTE));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void changePastTimeLimit() throws Exception {
        DateTimeUtil.overrideCurrentDate(NEW_VOTE_DATE);
        DateTimeUtil.overrideCurrentTime(DateTimeUtil.VOTE_TIME_LIMIT);
        perform(MockMvcRequestBuilders.put(VoteController.REST_URL + "/" + VOTES.getFirst().getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(getUpdatedVote())))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andReturn();
    }
}
