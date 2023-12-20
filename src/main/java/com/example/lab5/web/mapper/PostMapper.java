package com.example.lab5.web.mapper;

import com.example.lab5.model.Post;
import com.example.lab5.web.dto.PostModifyDto;
import com.example.lab5.web.dto.PostViewDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface PostMapper {
  @Mapping(target = "creator.id", source = "creatorId")
  @Mapping(target = "topic.id", source = "topicId")
  Post toEntity(PostModifyDto postModifyDto);

  @InheritInverseConfiguration(name = "toEntity")
  PostViewDto toDto(Post post);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  Post partial(PostModifyDto postModifyDto, @MappingTarget Post post);
}
