package me.bbfh.graduation.restaurant.repository;

import me.bbfh.graduation.common.BaseRepository;
import me.bbfh.graduation.restaurant.model.Vote;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface VoteRepository extends BaseRepository<Vote> {

    @Query("SELECT e FROM #{#entityName} e WHERE e.user.id=:userId")
    List<Vote> getAll(int userId);

    @Query("SELECT e FROM #{#entityName} e WHERE e.user.id=:userId AND e.voteDate=:voteDate")
    Vote getByDate(int userId, LocalDate voteDate);

    @Transactional
    @Modifying
    @Query("DELETE FROM #{#entityName} e WHERE e.user.id=:userId AND e.id=:voteId")
    void deleteFromUser(int userId, int voteId);

    @Transactional
    @Modifying
    @Query("DELETE FROM #{#entityName} e WHERE e.user.id=:userId AND e.voteDate=:voteDate")
    void deleteFromUserByDate(int userId, LocalDate voteDate);
}
