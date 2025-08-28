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
import java.util.stream.Collectors;

import static me.bbfh.graduation.restaurant.VoteTestData.*;
import static me.bbfh.graduation.user.UserTestData.ADMIN_MAIL;
import static me.bbfh.graduation.user.UserTestData.USER_MAIL;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class VoteControllerTest extends AbstractRestaurantControllerTest {

    @Autowired
    private VoteRepository voteRepository;

    @Test
    @WithUserDetails(value = USER_MAIL)
    void history() throws Exception {
        ResultActions action = perform(MockMvcRequestBuilders.get(ProfileVoteController.REST_URL))
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
            VoteTo expectedTo = VoteMapper.toTo(expected);
            VOTE_TO_MATCHER.assertMatch(created, expectedTo);
            VOTE_MATCHER.assertMatch(voteRepository.getExisted(created.id()), expected);
        }
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getToday() throws Exception {
        DateTimeUtil.overrideCurrentDate(VOTE_DATE);
        ResultActions action = perform(MockMvcRequestBuilders.get(ProfileVoteController.REST_URL + "/today"))
                .andDo(print())
                .andExpect(status().isOk());

        List<VoteTo> createdList = VOTE_TO_MATCHER.readListFromJson(action);

        for (int i = 0; i < createdList.size(); i++) {
            VoteTo created = createdList.get(i);
            Vote expected = VOTES.get(i);
            VoteTo expectedTo = VoteMapper.toTo(expected);
            VOTE_TO_MATCHER.assertMatch(created, expectedTo);
            VOTE_MATCHER.assertMatch(voteRepository.getExisted(created.id()), expected);
        }
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void retract() throws Exception {
        perform(MockMvcRequestBuilders.delete(ProfileVoteController.REST_URL + "/" + VOTES.getFirst().getId()))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();

        Assertions.assertFalse(voteRepository.existsById(VOTES.getFirst().getId()));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void retractToday() throws Exception {
        DateTimeUtil.overrideCurrentDate(VOTE_DATE);
        perform(MockMvcRequestBuilders.delete(ProfileVoteController.REST_URL + "/today"))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();

        Assertions.assertFalse(voteRepository.existsById(VOTES.getFirst().getId()));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void vote() throws Exception {
        DateTimeUtil.overrideCurrentDate(NEW_VOTE_DATE);
        ResultActions action = perform(MockMvcRequestBuilders.post(ProfileVoteController.REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(getNewVote())))
                .andDo(print())
                .andExpect(status().isCreated());

        VoteTo created = VOTE_TO_MATCHER.readFromJson(action);
        NEW_VOTE.setId(created.getId());
        VOTE_TO_MATCHER.assertMatch(created, VoteMapper.toTo(NEW_VOTE));
        VOTE_MATCHER.assertMatch(voteRepository.getExisted(created.getId()), NEW_VOTE);
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void voteNotAllowed() throws Exception {
        DateTimeUtil.overrideCurrentDate(VOTE_DATE);
        perform(MockMvcRequestBuilders.post(ProfileVoteController.REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(getNewVote())))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andReturn();
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void change() throws Exception {
        DateTimeUtil.overrideCurrentDate(NEW_VOTE_DATE);
        DateTimeUtil.overrideCurrentTime(DateTimeUtil.VOTE_TIME_LIMIT.minusMinutes(1));
        ResultActions action = perform(MockMvcRequestBuilders.put(ProfileVoteController.REST_URL + "/" + VOTES.getFirst().getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(getUpdatedVote())))
                .andDo(print())
                .andExpect(status().isOk());

        VoteTo created = VOTE_TO_MATCHER.readFromJson(action);
        UPDATED_VOTE.setId(created.getId());
        Assertions.assertEquals(created.getMenuId(), getUpdatedVote().getMenuId());
        VOTE_TO_MATCHER.assertMatch(created, VoteMapper.toTo(UPDATED_VOTE));
        VOTE_MATCHER.assertMatch(voteRepository.getExisted(created.getId()), UPDATED_VOTE);
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void changePastTimeLimit() throws Exception {
        DateTimeUtil.overrideCurrentDate(NEW_VOTE_DATE);
        DateTimeUtil.overrideCurrentTime(DateTimeUtil.VOTE_TIME_LIMIT);
        perform(MockMvcRequestBuilders.put(ProfileVoteController.REST_URL + "/" + VOTES.getFirst().getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(getUpdatedVote())))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andReturn();
    }
}
