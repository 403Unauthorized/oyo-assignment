package com.oyo.score.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oyo.score.web.context.OyoContext;
import com.oyo.score.web.controller.ScoreController;
import com.oyo.score.web.entity.dto.ScoreDTO;
import com.oyo.score.web.entity.dto.request.ScoreCreateRequest;
import com.oyo.score.web.entity.dto.request.ScoreQueryRequest;
import com.oyo.score.web.entity.dto.response.OperationResponse;
import com.oyo.score.web.entity.dto.response.PageResponse;
import com.oyo.score.web.repository.TblScoreRepository;
import com.oyo.score.web.service.ScoreDataService;
import com.oyo.score.web.utils.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;
import reactor.util.context.ContextView;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
@Sql({"/data.sql"})
class OyoAssignmentApplicationTests {

    private static final String FILE_BASE_PATH = "integration/";

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void init() {

    }

    @Test
    void contextLoads() {
    }

    @Test
    @DisplayName("Create Score")
    public void testCreateScore() throws IOException {
        File testFile = TestUtil.getFile("integration/create_score_request.json");
        ScoreCreateRequest request = objectMapper.readValue(testFile, ScoreCreateRequest.class);

        var response = webTestClient.post().uri("/score")
                .body(BodyInserters.fromValue(request))
                .header("host", "localhost:8081")
                .header("detailedLog", "false")
                        .exchange().expectStatus().is2xxSuccessful()
                        .expectBody(OperationResponse.class)
                                .returnResult();

        var res = response.getResponseBody();

        assertNotNull(res);
        assertNotNull(res);
        assertEquals("SUCCESS", res.getStatus());
        assertEquals(1, res.getCreated());
    }

    @ParameterizedTest
    @DisplayName("Get Score")
    @CsvSource(value = {
                    "get_score_request.json,request1,3,1,2,4",
                    "get_score_request.json,request2,3,1,1,3"
            }, delimiter = ','
    )
    public void testGetScore(String fileName, String request, Integer count, Integer currentPage, Integer totalPage, Long totalElements) {
        File file = TestUtil.getFile(FILE_BASE_PATH + fileName);
        Map<String, Object> requestMap = TestUtil.readValue(file, Map.class);
        URI uri = TestUtil.buildUri("/score", (Map<String, Object>) requestMap.get(request));

        var response = webTestClient.get().uri(uri)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(PageResponse.class)
                .returnResult();
        PageResponse<ScoreDTO> page = response.getResponseBody();
        assertNotNull(page);
        List<ScoreDTO> data = page.getData();
        assertNotNull(data);
        assertEquals(count, data.size());
        assertEquals(currentPage, page.getCurrentPage());
        assertEquals(totalPage, page.getTotalPage());
        assertEquals(totalElements, page.getTotalElements());
    }

    @ParameterizedTest
    @CsvSource(value = {"1,Player1,230,2021-12-01T14:23:09", "2,Keanu,80,2021-12-12T03:23:09"}, delimiter = ',')
    @DisplayName("Get Score By Id")
    public void testGetScoreById(Long id, String player, Integer score, LocalDateTime time) {
        String uri = "/score/" + id;
        var responseEntity = webTestClient.get().uri(uri)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(ScoreDTO.class)
                .returnResult();

        ScoreDTO response = responseEntity.getResponseBody();
        assertNotNull(response);
        assertEquals(id, response.getId());
        assertEquals(player, response.getPlayer());
        assertEquals(score, response.getScore());
        assertEquals(time, response.getTime());
    }

    @ParameterizedTest
    @DisplayName("Delete Score")
    @ValueSource(longs = {1L, 2L})
    public void testDeleteScore(Long id) {
        String uri = "/score/" + id;
        var responseEntity = webTestClient.delete().uri(uri)
                .exchange().expectStatus().is2xxSuccessful()
                .expectBody(OperationResponse.class)
                .returnResult();

        OperationResponse response = responseEntity.getResponseBody();
        assertNotNull(response);
        assertEquals("SUCCESS", response.getStatus());
        assertEquals(1, response.getDeleted());
    }

    @ParameterizedTest
    @DisplayName("Player Score History")
    @CsvSource(value = {
            "Keanu,138,16,89.5,4",
            "Monica,180,60,129.67,3"
    })
    public void testScoreHistory(String player, Integer max, Integer min, Double avg, Integer dataSize) {
        String uri = "/score/history/" + player;
        var responseEntity = webTestClient.get().uri(uri)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Map.class)
                .returnResult();
        Map<String, Object> res = responseEntity.getResponseBody();
        assertNotNull(res);
        assertEquals(max, res.get("top"));
        assertEquals(min, res.get("low"));
        assertEquals(avg, Double.parseDouble(String.valueOf(res.get("avg"))));
        assertEquals(dataSize, ((List)res.get("data")).size());
    }

}
