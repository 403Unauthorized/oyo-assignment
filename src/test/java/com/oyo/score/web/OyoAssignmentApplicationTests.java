package com.oyo.score.web;

import com.oyo.score.web.entity.dto.ScoreDTO;
import com.oyo.score.web.entity.dto.request.ScoreQueryRequest;
import com.oyo.score.web.entity.dto.response.OperationResponse;
import com.oyo.score.web.entity.dto.response.PageResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Sql({"/data.sql"})
class OyoAssignmentApplicationTests {

    @Test
    void contextLoads() {
    }

}
