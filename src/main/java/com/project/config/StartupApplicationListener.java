package com.project.config;

import com.project.service.CsvReaderService;
import com.project.service.DatabaseSeederService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StartupApplicationListener {

    private final CsvReaderService csvReaderService;
    private final DatabaseSeederService databaseSeederService;


    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationEvent() {
        var parsedData = csvReaderService.readPlayersFromFile();

        if (!parsedData.isEmpty()) {
            databaseSeederService.seedDatabase(parsedData);
        }
    }
}