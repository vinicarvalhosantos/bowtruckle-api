package br.com.santos.vinicius.bowtruckleapi.controller.v1;

import br.com.santos.vinicius.bowtruckleapi.controller.handler.CustomExceptionHandler;
import br.com.santos.vinicius.bowtruckleapi.model.dto.GameTimeDTO;
import br.com.santos.vinicius.bowtruckleapi.model.entity.StreamEntity;
import br.com.santos.vinicius.bowtruckleapi.model.response.Response;
import br.com.santos.vinicius.bowtruckleapi.model.response.SuccessResponse;
import br.com.santos.vinicius.bowtruckleapi.service.StreamGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/v1/stream-games")
public class StreamGamesController extends CustomExceptionHandler {

    final
    StreamGameService streamGameService;

    public StreamGamesController(StreamGameService streamGameService) {
        this.streamGameService = streamGameService;
    }

    @GetMapping("/gametime/{gameName}")
    public ResponseEntity<Response> getGameTime(@PathVariable("gameName") String gameName, @RequestParam(name = "from", required = false) LocalDate from) {
        if (from == null) {
            from = LocalDate.now();
        }
        List<GameTimeDTO> gameTime = new ArrayList<>(Collections.singletonList(this.streamGameService.gameTime(gameName, from)));
        SuccessResponse successResponse = new SuccessResponse(formatRecords(gameTime), "Game played was found!");

        return ResponseEntity.status(HttpStatus.OK).body(new Response(successResponse));
    }

    @GetMapping("/gametime")
    public ResponseEntity<Response> getCurrentGameTime() {

        List<GameTimeDTO> gameTime = new ArrayList<>(Collections.singletonList(this.streamGameService.currentGameTime()));
        SuccessResponse successResponse = new SuccessResponse(formatRecords(gameTime), "Game played was found!");

        return ResponseEntity.status(HttpStatus.OK).body(new Response(successResponse));
    }


    private List<Object> formatRecords(List<GameTimeDTO> gameTime) {
        //Make the list of list to just one list
        return Stream.of(gameTime).flatMap(Collection::stream).collect(Collectors.toList());
    }

}
