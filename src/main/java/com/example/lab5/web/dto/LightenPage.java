package com.example.lab5.web.dto;

import java.util.List;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
public class LightenPage<C> {

  private List<C> content;

  private int size;

  private int pageNumber;

  private long totalElements;

  private int totalPages;

  public static <C> LightenPage<C> of(Page<C> page) {
    LightenPage<C> lightenPage = new LightenPage<>();
    lightenPage.setPageNumber(page.getNumber());
    lightenPage.setTotalPages(page.getTotalPages());
    lightenPage.setSize(page.getSize());
    lightenPage.setTotalElements(page.getTotalElements());
    lightenPage.setContent(page.getContent());
    return lightenPage;
  }

}