package me.bbfh.graduation.restaurant.repository;

import me.bbfh.graduation.common.BaseRepository;
import me.bbfh.graduation.restaurant.model.Vote;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface VoteRepository extends BaseRepository<Vote> {

    @Query("SELECT e FROM Vote e WHERE e.user.id=:userId")
    List<Vote> getByUserId(int userId);

    @Query("SELECT e FROM Vote e WHERE e.user.id=:userId AND e.voteDate=:voteDate")
    Vote getByDate(int userId, LocalDate voteDate);

    @Query("SELECT e FROM Vote e WHERE e.voteDate=:voteDate")
    List<Vote> getByDate(LocalDate voteDate);
}
