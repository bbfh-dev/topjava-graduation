package me.bbfh.graduation.restaurant.web;

import me.bbfh.graduation.common.util.DateTimeUtil;
import me.bbfh.graduation.common.util.JsonUtil;
import me.bbfh.graduation.restaurant.mapper.VoteMapper;
import me.bbfh.graduation.restaurant.model.Vote;
import me.bbfh.graduation.restaurant.to.CountedVotesTo;
import me.bbfh.graduation.restaurant.to.VoteTo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.function.Predicate;

import static me.bbfh.graduation.restaurant.VoteTestData.*;
import static me.bbfh.graduation.user.UserTestData.USER_MAIL;
import static me.bbfh.graduation.user.UserTestData.user;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class VoteControllerTest extends AbstractRestaurantControllerTest {

    private void getAllAndAssert(String endpoint, Predicate<Vote> filter) throws Exception {
        ResultActions action = perform(MockMvcRequestBuilders.get(endpoint))
                .andDo(print())
                .andExpect(status().isOk());
        List<VoteTo> createdList = VOTE_TO_MATCHER.readListFromJson(action);

        assertThat(createdList)
                .usingRecursiveFieldByFieldElementComparator()
                .hasSameElementsAs(VoteMapper.toTos(VOTES.stream()
                        .filter(filter)
                        .toList()));
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
        ResultActions action = perform(MockMvcRequestBuilders.get(VoteController.REST_URL + "/today"))
                .andDo(print())
                .andExpect(status().isOk());
        List<CountedVotesTo> createdList = VOTE_TODAY_MATCHER.readListFromJson(action);

        assertThat(createdList)
                .usingRecursiveFieldByFieldElementComparator()
                .hasSameElementsAs(TODAY_VOTES);
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
