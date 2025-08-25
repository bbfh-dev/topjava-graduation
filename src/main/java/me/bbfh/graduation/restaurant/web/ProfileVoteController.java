package me.bbfh.graduation.restaurant.web;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import me.bbfh.graduation.app.AuthUser;
import me.bbfh.graduation.common.error.NotFoundException;
import me.bbfh.graduation.common.util.DateTimeUtil;
import me.bbfh.graduation.restaurant.VoteUtil;
import me.bbfh.graduation.restaurant.model.Vote;
import me.bbfh.graduation.restaurant.repository.MenuRepository;
import me.bbfh.graduation.restaurant.repository.VoteRepository;
import me.bbfh.graduation.restaurant.to.VoteTo;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = ProfileVoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@Transactional(readOnly = true)
public class ProfileVoteController {
    static final String REST_URL = "/api/profile/votes";

    protected final VoteRepository voteRepository;
    private final MenuRepository menuRepository;

    public ProfileVoteController(VoteRepository voteRepository, MenuRepository menuRepository) {
        this.voteRepository = voteRepository;
        this.menuRepository = menuRepository;
    }

    @GetMapping("history")
    public List<VoteTo> getAll(@AuthenticationPrincipal AuthUser authUser) {
        return VoteUtil.getTos(voteRepository.getAll(authUser.getUser().getId()));
    }

    @GetMapping("today")
    public VoteTo getToday(@AuthenticationPrincipal AuthUser authUser) {
        return VoteUtil.getTo(voteRepository.getToday(authUser.getUser().getId(), LocalDate.now()));
    }

    @PostMapping
    public VoteTo vote(@Valid @RequestBody VoteTo.RestTo userVoteTo, @AuthenticationPrincipal AuthUser authUser) {
        assert userVoteTo.getMenuId() != null;
        if (!menuRepository.existsById(userVoteTo.getMenuId())) {
            throw new NotFoundException("Provided menu doesn't exist with id=" + userVoteTo.getMenuId());
        }

        Vote vote = voteRepository.save(new Vote(authUser.getUser(), menuRepository.getReferenceById(userVoteTo.getMenuId())));
        return VoteUtil.getTo(vote);
    }

    @PutMapping("{voteId}")
    public VoteTo changeToday(@PathVariable int voteId, @Valid @RequestBody VoteTo.RestTo userVoteTo,
                              @AuthenticationPrincipal AuthUser authUser) {
        if (!DateTimeUtil.isVoteChangeAllowed()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Not allowed to change vote after 11:00 local time");
        }

        assert userVoteTo.getMenuId() != null;
        if (!menuRepository.existsById(userVoteTo.getMenuId())) {
            throw new NotFoundException("Provided menu doesn't exist with id=" + userVoteTo.getMenuId());
        }

        if (!voteRepository.existsById(voteId)) {
            throw new NotFoundException("Provided vote doesn't exist with id=" + voteId);
        }

        LocalDate now = LocalDate.now();
        Vote vote = voteRepository.save(new Vote(voteId, now, authUser.getUser(),
                menuRepository.getReferenceById(userVoteTo.getMenuId())));
        return VoteUtil.getTo(vote);
    }
}
