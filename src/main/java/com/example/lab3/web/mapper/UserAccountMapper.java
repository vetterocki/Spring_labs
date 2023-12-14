package com.example.lab3.web.mapper;

import com.example.lab3.model.AdminAccount;
import com.example.lab3.model.UserAccount;
import com.example.lab3.web.dto.user.AdminAccountModifyDto;
import com.example.lab3.web.dto.user.AdminAccountViewDto;
import com.example.lab3.web.dto.user.UserModifyDto;
import com.example.lab3.web.dto.user.UserAccountViewDto;
import com.example.lab3.web.dto.user.UserViewDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.SubclassExhaustiveStrategy;
import org.mapstruct.SubclassMapping;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    subclassExhaustiveStrategy = SubclassExhaustiveStrategy.RUNTIME_EXCEPTION,
    componentModel = "spring"
)
public interface UserAccountMapper {

  @SubclassMapping(source = UserModifyDto.class, target = UserAccount.class)
  @SubclassMapping(source = AdminAccountModifyDto.class, target = AdminAccount.class)
  UserAccount toEntity(UserModifyDto userModifyDto);

  @SubclassMapping(source = UserAccount.class, target = UserAccountViewDto.class)
  @SubclassMapping(source = AdminAccount.class, target = AdminAccountViewDto.class)
  UserViewDto toDto(UserAccount userAccount);
}
