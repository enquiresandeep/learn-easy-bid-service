package com.learneasy.user.infrastructure.mapper;

import com.learneasy.user.domain.Address;
import com.learneasy.user.infrastructure.dto.AddressDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface AddressMapper {

    Address addressDTOToAddress(AddressDTO addressDTO);
    AddressDTO addressToAddressDTO(Address address);

    List<Address> addressDTOToAddresses(List<AddressDTO> addressDTOs);
    List<AddressDTO> addressToAddressDTOs(List<Address> address);

}
