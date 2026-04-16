package com.project.config;


import com.project.service.DataSeeder;
import com.project.service.PlayerReaderService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StartupApplicationListener {

    private final PlayerReaderService playerReaderService;
    private final DataSeeder databaseSeederService;


    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationEvent() {
        var parsedData = playerReaderService.readPlayers();

        if (!parsedData.isEmpty()) {
            databaseSeederService.seedDatabase(parsedData);
        }
    }
}