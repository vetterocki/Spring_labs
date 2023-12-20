package com.example.lab5.web.mapper;

import com.example.lab5.model.UserAccount;
import com.example.lab5.web.dto.user.UserAccountModifyDto;
import com.example.lab5.web.dto.user.UserAccountViewDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = "spring"
)
public interface UserAccountMapper {

  UserAccount toEntity(UserAccountModifyDto userModifyDto);

  UserAccountViewDto toDto(UserAccount userAccount);

}
