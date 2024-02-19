package br.com.santos.vinicius.bowtruckleapi.controller.v1;

import br.com.santos.vinicius.bowtruckleapi.controller.handler.CustomExceptionHandler;
import br.com.santos.vinicius.bowtruckleapi.exception.NotFoundException;
import br.com.santos.vinicius.bowtruckleapi.model.dto.StreamTimeDTO;
import br.com.santos.vinicius.bowtruckleapi.model.entity.StreamEntity;
import br.com.santos.vinicius.bowtruckleapi.model.response.RangeResponse;
import br.com.santos.vinicius.bowtruckleapi.model.response.Response;
import br.com.santos.vinicius.bowtruckleapi.model.response.SuccessResponse;
import br.com.santos.vinicius.bowtruckleapi.service.StreamService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/v1/streams")
public class StreamsController extends CustomExceptionHandler {

    final StreamService streamService;

    public StreamsController(StreamService streamService) {
        Assert.notNull(streamService, "StreamService must no be null.");
        this.streamService = streamService;
    }

    @GetMapping
    public ResponseEntity<Response> getStreamsDetails(@RequestParam(name = "page", defaultValue = "1") int page, @RequestParam(name = "limit", defaultValue = "5") int limit) {
        Page<StreamEntity> streamPages = streamService.findStreamsDetails(page, limit);

        if (!streamPages.hasContent()) {
            throw new NotFoundException("There wasn't any stream available to get!");
        }
        List<StreamEntity> stream = streamPages.get().collect(Collectors.toList());

        SuccessResponse successResponse = new SuccessResponse(formatRecords(stream), "Stream details found!", streamPages, page, limit);
        return ResponseEntity.status(HttpStatus.OK).body(new Response(successResponse));
    }

    @GetMapping("/from/{from}/to/{to}")
    public ResponseEntity<Response> getStreamDetailsFromDate(@PathVariable("from") LocalDate from, @PathVariable("to") LocalDate to) {
        List<StreamEntity> streamList = streamService.findStreamsByDateRange(from, to);
        if (streamList.isEmpty()) {
            throw new NotFoundException("There wasn't any stream available to get for this range!");
        }
        SuccessResponse successResponse = new SuccessResponse("Stream details found for this range!", new RangeResponse(from, to), formatRecords(streamList));

        return ResponseEntity.status(HttpStatus.OK).body(new Response(successResponse));
    }

    @GetMapping("/streamtime")
    public ResponseEntity<Response> getStreamTime(@RequestParam(name = "of", required = false) LocalDate of) {
        if (of == null)
            of = LocalDate.now();

        StreamTimeDTO streamTimeDTO = this.streamService.streamTime(of);
        SuccessResponse successResponse = new SuccessResponse(Collections.singletonList(streamTimeDTO), "Stream time found!");

        return ResponseEntity.status(HttpStatus.OK).body(new Response(successResponse));
    }

    private List<Object> formatRecords(List<StreamEntity> streamDetails) {
        //Make the list of list to just one list
        return Stream.of(streamDetails).flatMap(Collection::stream).collect(Collectors.toList());
    }
}
