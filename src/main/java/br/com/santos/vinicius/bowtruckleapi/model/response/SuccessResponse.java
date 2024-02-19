package br.com.santos.vinicius.bowtruckleapi.model.response;

import br.com.santos.vinicius.bowtruckleapi.filter.ExcludeZerosFilter;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.io.Serial;
import java.util.List;

@Getter
public class SuccessResponse extends GenericResponse {

    @Serial
    private static final long serialVersionUID = -7375336352878709500L;

    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = ExcludeZerosFilter.class)
    private int page;

    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = ExcludeZerosFilter.class)
    private int limit;

    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = ExcludeZerosFilter.class)
    private int totalPages;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private Long totalElements;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private RangeResponse range;

    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = ExcludeZerosFilter.class)
    private final int recordCount;

    private final List<Object> records;

    public SuccessResponse(List<Object> records, String message) {
        super(message);
        this.records = records;
        this.recordCount = records.size();
    }

    public SuccessResponse(String message, RangeResponse range, List<Object> records) {
        super(message);
        this.range = range;
        this.records = records;
        this.recordCount = records.size();
    }

    public SuccessResponse(List<Object> records, String message, int page, int totalPages, Long totalElements, int limit) {
        super(message);
        this.records = records;
        this.recordCount = records.size();
        this.page = page;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.limit = limit;
    }

    public SuccessResponse(List<Object> records, String message, Page entityPage, int page, int limit) {
        super(message);
        this.records = records;
        this.recordCount = records.size();
        this.totalPages = entityPage.getTotalPages();
        this.totalElements = entityPage.getTotalElements();
        this.page = page;
        this.limit = limit;
    }

}
