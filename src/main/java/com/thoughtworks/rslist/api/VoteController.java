package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.po.VotePO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collector;

@RestController
public class VoteController {
    @Autowired
    VoteRespository voteRespository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRepository rsEventRepository;

    @PostMapping("/rs/vote/{rsEventId}")
    public ResponseEntity voteForRsEvent(@RequestBody @Valid Vote vote,@PathVariable Integer rsEvertId){
        int voteCount = vote.getVoteNum();
        UserPO userPO = userRepository.findById(vote.getVoteNum()).get();
        Integer voteNum = userPO.getVoteNum();
        if(voteNum >= voteCount){
            VotePO success =  VotePO.builder().voteTime(LocalDateTime.now())
                    .voteNum(voteNum).userP0(userPO).build();
            voteRespository.save(success);
            RsEventPO rsEventPO = rsEventRepository.findById(rsEvertId).get();
            rsEventPO.setVoteNum(voteCount + rsEventPO.getVoteNum());
            rsEventRepository.save(rsEventPO);
            userPO.setVoteNum(userPO.getVoteNum()-voteCount);
            userRepository.save(userPO);
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.badRequest().build();
        }
    }
}
