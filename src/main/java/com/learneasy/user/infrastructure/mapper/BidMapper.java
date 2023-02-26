package com.learneasy.user.infrastructure.mapper;

import com.learneasy.user.domain.Bid;
import com.learneasy.user.infrastructure.dto.BidDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface BidMapper {

    BidDTO bidToBidDTO(Bid bid);
    Bid bidDTOToBid(BidDTO bidDTO );

     List<BidDTO> bidToBidDTOs(List<Bid> bids);

     List<Bid> bidDTOToBids(List<BidDTO> bidDTOs );


}
