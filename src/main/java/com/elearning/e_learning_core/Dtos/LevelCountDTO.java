package com.elearning.e_learning_core.Dtos;

import com.elearning.e_learning_core.model.LevelCurseType;

public class LevelCountDTO {
    private LevelCurseType level;
    private Long count;

    public LevelCountDTO(LevelCurseType level, Long count) {
        this.level = level;
        this.count = count;
    }

    public LevelCurseType getLevel() {
        return level;
    }

    public void setLevel(LevelCurseType level) {
        this.level = level;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
