package com.project.service;

import com.project.dto.CsvPlayerDto;

import java.util.List;

public interface PlayerReaderService {
  List<CsvPlayerDto> readPlayers();
}
