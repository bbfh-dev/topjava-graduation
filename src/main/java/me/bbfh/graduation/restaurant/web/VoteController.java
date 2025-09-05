package me.bbfh.graduation.restaurant.web;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import me.bbfh.graduation.app.AuthUser;
import me.bbfh.graduation.common.error.ForbiddenRequestException;
import me.bbfh.graduation.common.error.IllegalRequestDataException;
import me.bbfh.graduation.common.error.NotFoundException;
import me.bbfh.graduation.common.util.DateTimeUtil;
import me.bbfh.graduation.restaurant.mapper.VoteMapper;
import me.bbfh.graduation.restaurant.model.Menu;
import me.bbfh.graduation.restaurant.model.Vote;
import me.bbfh.graduation.restaurant.repository.MenuRepository;
import me.bbfh.graduation.restaurant.repository.VoteRepository;
import me.bbfh.graduation.restaurant.to.VoteTo;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@Transactional(readOnly = true)
public class VoteController {

    static final String REST_URL = "/api/votes";

    protected final VoteRepository voteRepository;
    private final MenuRepository menuRepository;

    public VoteController(VoteRepository voteRepository, MenuRepository menuRepository) {
        this.voteRepository = voteRepository;
        this.menuRepository = menuRepository;
    }

    @GetMapping("history")
    public List<VoteTo> getHistory() {
        return VoteMapper.toTos(voteRepository.findAll());
    }

    @GetMapping("today")
    public List<VoteTo> getToday() {
        return VoteMapper.toTos(voteRepository.getByUserIdAndDate(DateTimeUtil.getCurrentDate()));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public VoteTo vote(@Valid @RequestBody VoteTo.RestTo userVoteTo, @AuthenticationPrincipal AuthUser authUser) {
        Menu menu = menuRepository.getExisted(userVoteTo.getMenuId());
        if (menu == null) {
            throw new NotFoundException("Provided menu doesn't exist with id=" + userVoteTo.getMenuId());
        }

        if (!menu.getRelevancyDate().isEqual(DateTimeUtil.getCurrentDate())) {
            throw new IllegalRequestDataException("Can't vote for an irrelevant Menu. Vote for a menu that is relevant today.");
        }

        Vote existingVote = voteRepository.getByUserIdAndDate(authUser.getUser().getId(), DateTimeUtil.getCurrentDate());
        if (existingVote != null) {
            throw new IllegalRequestDataException("Already casted a vote with id=" + existingVote.getId());
        }

        Vote vote = voteRepository.save(new Vote(authUser.getUser(), menu));
        return VoteMapper.toTo(vote);
    }

    @PutMapping("{voteId}")
    @Transactional
    public VoteTo change(@PathVariable int voteId, @Valid @RequestBody VoteTo.RestTo userVoteTo,
                         @AuthenticationPrincipal AuthUser authUser) {
        if (!DateTimeUtil.isVoteChangeAllowed()) {
            throw new IllegalRequestDataException("Not allowed to change vote after "
                    + DateTimeUtil.VOTE_TIME_LIMIT + " local time");
        }

        assert userVoteTo.getMenuId() != null;
        Menu menu = menuRepository.findById(userVoteTo.getMenuId())
                .orElseThrow(() -> new NotFoundException("Provided menu doesn't exist with id=" + userVoteTo.getMenuId()));

        Vote vote = voteRepository.findById(voteId)
                .orElseThrow(() -> new NotFoundException("vote not found with id=" + voteId));
        if (!vote.getUser().equals(authUser.getUser())) {
            throw new ForbiddenRequestException("Cannot change votes of other users");
        }

        return VoteMapper.toTo(voteRepository.save(
                new Vote(voteId, DateTimeUtil.getCurrentDate(), authUser.getUser(), menu)));
    }

    @DeleteMapping("{voteId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void retract(@PathVariable int voteId, @AuthenticationPrincipal AuthUser authUser) {
        voteRepository.deleteByUserId(authUser.getUser().getId(), voteId);
    }

    @DeleteMapping("today")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void retractToday(@AuthenticationPrincipal AuthUser authUser) {
        voteRepository.deleteByUserIdAndDate(authUser.getUser().getId(), DateTimeUtil.getCurrentDate());
    }
}
