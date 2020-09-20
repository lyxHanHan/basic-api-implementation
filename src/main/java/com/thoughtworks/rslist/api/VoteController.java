package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.po.VotePO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class VoteController {
    private final UserRepository userRepository;
    private final RsEventRepository rsEventRepository;
    private final VoteRespository voteRespository;


    public VoteController(UserRepository userRepository,RsEventRepository rsEventRepository,VoteRespository voteResposity){
        this.userRepository = userRepository;
        this.rsEventRepository = rsEventRepository;
        this.voteRespository = voteResposity;
    }

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


    @GetMapping("/voteRecord")
    public ResponseEntity<List<Vote>> getVoteRecord(@RequestParam int userId, @RequestParam int rsEventId,
                                                    @RequestParam int pageIndex){
        Pageable pageable = PageRequest.of(pageIndex,5);
        return ResponseEntity.ok(
                voteRespository.findAllByUserIdAndRsEventId(userId,rsEventId,pageable)
                        .stream().map(
                        item -> Vote.builder()
                                .userId(item.getUserP0().getId())
                                .voteTime(item.getVoteTime())
                                .rsEventId(item.getRsEventPO().getId())
                                .voteNum(item.getVoteNum()).build()

                ).collect(Collectors.toList()));
    }

}
