package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.po.VotePO;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VoteRespository extends CrudRepository {
    @Override
    List<VotePO> findAll();

    List<VotePO> findAllByUserIdAndRsEventId(int userId, int rsEventId);
}
