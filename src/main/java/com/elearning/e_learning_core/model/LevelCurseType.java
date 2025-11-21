package com.elearning.e_learning_core.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum LevelCurseType {
	BEGINNER("Iniciante"),
	INTERMEDIATE("Intermediário"),
	ADVANCED("Avançado"),
	EXPERT("Especialista");

	private final String label;

	LevelCurseType(String label) {
		this.label = label;
	}

	@JsonValue
	public String getLabel() {
		return label;
	}

	@JsonCreator
	public static LevelCurseType fromLabel(String value) {
		for (LevelCurseType level : values()) {
			if (level.label.equalsIgnoreCase(value) || level.name().equalsIgnoreCase(value)) {
				return level;
			}
		}
		throw new IllegalArgumentException("Nível inválido: " + value);
	}
}
