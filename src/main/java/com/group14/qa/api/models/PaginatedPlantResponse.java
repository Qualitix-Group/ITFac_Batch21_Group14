package com.group14.qa.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PaginatedPlantResponse {

    @JsonProperty("content")
    private List<PlantResponse> content;

    @JsonProperty("totalPages")
    private int totalPages;

    @JsonProperty("totalElements")
    private long totalElements;

    @JsonProperty("pageable")
    private PageableInfo pageable;

    @JsonProperty("size")
    private int size;

    @JsonProperty("number")
    private int number;

    public List<PlantResponse> getContent() {
        return content;
    }

    public void setContent(List<PlantResponse> content) {
        this.content = content;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public PageableInfo getPageable() {
        return pageable;
    }

    public void setPageable(PageableInfo pageable) {
        this.pageable = pageable;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PageableInfo {
        @JsonProperty("pageNumber")
        private int pageNumber;

        @JsonProperty("pageSize")
        private int pageSize;

        public int getPageNumber() {
            return pageNumber;
        }

        public void setPageNumber(int pageNumber) {
            this.pageNumber = pageNumber;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }
    }
}