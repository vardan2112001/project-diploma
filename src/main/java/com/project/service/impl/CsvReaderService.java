package com.project.service.impl;

import com.project.dto.CsvPlayerDto;
import com.project.mapper.CsvMapper;
import com.project.service.PlayerReaderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CsvReaderService implements PlayerReaderService {

    private static final int MIN_COLUMNS_REQUIRED = 48;

    @Value("${app.import.file.path}")
    private String filePath;

    @Override
    public List<CsvPlayerDto> readPlayers() {
        List<CsvPlayerDto> parsedPlayers = new ArrayList<>();
        String line;

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            bufferedReader.readLine();
            while ((line = bufferedReader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] data = line.split(",", -1);
                if (data.length < MIN_COLUMNS_REQUIRED) continue;

                parsedPlayers.add(CsvMapper.toDto(data));
            }
            log.info("File read successfully, {} players parsed", parsedPlayers.size());

        } catch (Exception e) {
            log.error("Error while reading CSV: ", e);
        }

        return parsedPlayers;
    }
}
