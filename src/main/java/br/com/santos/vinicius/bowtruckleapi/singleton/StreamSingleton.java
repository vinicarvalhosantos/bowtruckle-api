package br.com.santos.vinicius.bowtruckleapi.singleton;

import org.springframework.stereotype.Component;

@Component
public class StreamSingleton {

    public static StreamSingleton singleInstance = null;

    public boolean isStreaming = false;

    public static StreamSingleton getInstance() {
        if (singleInstance == null) {
            singleInstance = new StreamSingleton();
        }

        return singleInstance;
    }


}
