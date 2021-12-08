package com.oyo.score.web.controller;

import com.oyo.score.web.config.AppConfiguration;
import com.oyo.score.web.entity.db.TblScore;
import com.oyo.score.web.entity.dto.request.ScoreCreateRequest;
import com.oyo.score.web.entity.dto.response.OperationResponse;
import com.oyo.score.web.repository.TblScoreRepository;
import com.oyo.score.web.service.ScoreDataService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = ScoreController.class)
@Import({ScoreDataService.class, AppConfiguration.class})
public class ScoreControllerTest {

    @MockBean
    TblScoreRepository repository;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void testCreateScore() {
        TblScore score = new TblScore();
        score.setId(1L);
        score.setPlayer("Olivia");
        score.setScore(230);
        LocalDateTime time = LocalDateTime.now();
        score.setTime(time);

        Mockito.when(repository.save(Mockito.any())).thenReturn(score);

        ScoreCreateRequest request = new ScoreCreateRequest();
        request.setPlayer("Olivia");
        request.setScore(230);
        request.setTime(time);

        var response = webTestClient.post()
                .uri("/score")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(OperationResponse.class)
                        .returnResult().getResponseBody();

        Mockito.verify(repository, Mockito.times(1)).save(Mockito.any());

        assertNotNull(response);
        assertEquals("SUCCESS", response.getStatus());
        assertEquals(1, response.getCreated());
        assertEquals(0, response.getDeleted());
        assertEquals(0, response.getUpdated());
    }
}
