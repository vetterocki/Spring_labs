package com.example.lab5.web.mapper;

import com.example.lab5.model.Topic;
import com.example.lab5.web.dto.TopicModifyDto;
import com.example.lab5.web.dto.TopicViewDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")

public interface TopicMapper {
  @Mapping(target = "creator.id", source = "creatorId")

  Topic toEntity(TopicModifyDto topicModifyDto);

  @InheritInverseConfiguration(name = "toEntity")

  TopicViewDto toDto(Topic topic);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  Topic partial(TopicModifyDto topicModifyDto, @MappingTarget Topic topic);

}
