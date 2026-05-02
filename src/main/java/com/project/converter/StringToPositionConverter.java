package com.project.converter;

import com.project.enums.Position;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class StringToPositionConverter implements Converter<String, Position> {
    @Nullable
    @Override
    public Position convert(String source) {
        return Position.fromString(source);
    }
}
