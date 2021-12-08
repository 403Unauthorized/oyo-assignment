package com.oyo.score.web.entity.dto;

import java.time.LocalDateTime;

public class ScoreDTO {
    private Long id;
    private String player;
    private Integer score;
    private LocalDateTime time;

    public ScoreDTO() {
    }

    public ScoreDTO(Long id, String player, Integer score, LocalDateTime time) {
        this.id = id;
        this.player = player;
        this.score = score;
        this.time = time;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
