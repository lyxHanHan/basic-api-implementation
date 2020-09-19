package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.po.VotePO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;


import java.util.List;

public interface VoteRespository extends PagingAndSortingRepository {
    @Override
    List<VotePO> findAll();

    @Query("select v from VotePO v where v.user.id = :userId and v.rsEvent.id = :rsEventId")
    List<VotePO> findAllByUserIdAndRsEventId(int userId, int rsEventId, Pageable pageable);
}
