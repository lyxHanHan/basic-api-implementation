package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.po.RsEventPO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RsEventRepository extends CrudRepository <RsEventPO,Integer>{
 @Override
    List<RsEventPO> findAll();
}
