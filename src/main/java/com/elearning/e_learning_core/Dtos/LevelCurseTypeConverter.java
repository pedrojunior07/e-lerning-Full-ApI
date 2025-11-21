package com.elearning.e_learning_core.Dtos;

import com.elearning.e_learning_core.model.LevelCurseType;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class LevelCurseTypeConverter implements AttributeConverter<LevelCurseType, String> {

    @Override
    public String convertToDatabaseColumn(LevelCurseType attribute) {
        return attribute != null ? attribute.getLabel() : null;
    }

    @Override
    public LevelCurseType convertToEntityAttribute(String dbData) {
        return dbData != null ? LevelCurseType.fromLabel(dbData) : null;
    }
}