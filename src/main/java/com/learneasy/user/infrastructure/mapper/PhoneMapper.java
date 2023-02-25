package com.learneasy.user.infrastructure.mapper;

import com.learneasy.user.domain.Phone;
import com.learneasy.user.infrastructure.dto.PhoneDTO;
import org.mapstruct.Mapper;

@Mapper
public interface PhoneMapper {

    PhoneDTO phoneToPhoneDTO(Phone phone);
    Phone phoneDTOToPhone(PhoneDTO phoneDTO);
}
