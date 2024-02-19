package br.com.santos.vinicius.bowtruckleapi.task;

import br.com.santos.vinicius.bowtruckleapi.service.StreamService;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;

@Slf4j
public class StreamIdleTask extends TimerTask {

    Timer timer;

    StreamService streamService;

    LocalDateTime endedAt;

    public StreamIdleTask(Timer timer, StreamService streamService) {
        this.timer = timer;
        this.streamService = streamService;
        this.endedAt = LocalDateTime.now();
        log.info("Stream in idle mode at {}.", endedAt);
    }

    @Override
    public void run() {
        log.info("Stream has not come back.");
        this.streamService.wentOffline(endedAt);
        timer.cancel();
    }
}
