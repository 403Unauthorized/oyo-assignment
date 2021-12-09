package com.oyo.score.web.controller;

import com.oyo.score.web.context.OyoContext;
import com.oyo.score.web.entity.dto.request.ScoreCreateRequest;
import com.oyo.score.web.entity.dto.request.ScoreQueryRequest;
import com.oyo.score.web.entity.dto.response.OperationResponse;
import com.oyo.score.web.entity.dto.response.PageResponse;
import com.oyo.score.web.entity.dto.ScoreDTO;
import com.oyo.score.web.logger.LoggerRequest;
import com.oyo.score.web.service.ScoreDataService;
import com.oyo.score.web.utils.DateUtil;
import com.oyo.score.web.utils.MapUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.Map;

/**
 * API controller for scores
 */
@RestController
@RequestMapping("/score")
public class ScoreController {

    private static final Logger log = LoggerFactory.getLogger(ScoreController.class);

    private final ScoreDataService scoreDataService;

    public ScoreController(ScoreDataService scoreDataService) {
        this.scoreDataService = scoreDataService;
    }

    /**
     * Create score data.
     * @param request {@link ScoreCreateRequest} Request parameters.
     * @return {@link OperationResponse} To see if the operation is successfully completed.
     */
    @PostMapping
    public Mono<ResponseEntity<OperationResponse>> createScore(@Valid @RequestBody ScoreCreateRequest request) {
        LoggerRequest loggerRequest = new LoggerRequest();
        loggerRequest.setRequestBody(MapUtil.parseObjectToJsonString(request));
        return  Mono.just(request).doOnEach(signal -> {
                    if (signal.isOnNext()){
                        updateLoggerRequest(loggerRequest, signal.getContextView().get("oyoContext"));
                    }
                })
                .flatMap(scoreDataService::createScore)
                .map(ResponseEntity::ok)
                .doOnRequest(l -> log.debug("Start creating score data..."))
                .doOnSuccess(o -> {
                    loggerRequest.setResponseTime(DateUtil.currentTimeMillis() - loggerRequest.getStartTime());
                    if (loggerRequest.isDetailedLog()) {
                        log.debug("API score.create successfully completed: {}", o);
                    }
                    log.info(MapUtil.parseObjectToJsonString(loggerRequest));
                });
    }

    /**
     * Query score data.
     * Available parameters:
     * player {@link String} player name
     * start {@link java.time.LocalDate} start date
     * end {@link java.time.LocalDate} end date
     * page: current page number
     * pageSize: capacity of one page
     * @param request {@link PageResponse<ScoreQueryRequest>} Query parameters.
     * @return Paginated data of scores sorted by score desc.
     */
    @GetMapping
    public Mono<ResponseEntity<PageResponse<ScoreDTO>>> getScores(@Valid ScoreQueryRequest request) {
        LoggerRequest loggerRequest = new LoggerRequest();
        loggerRequest.setQueryParams(MapUtil.parseObjectToJsonString(request));
        return Mono.just(request).doOnEach(signal -> {
            if (signal.isOnNext()) {
                updateLoggerRequest(loggerRequest, signal.getContextView().get("oyoContext"));
            }
        })
                .flatMap(scoreDataService::getScores)
                .map(ResponseEntity::ok)
                .doOnRequest(l -> log.debug("Start fetching score data..."))
                .doOnSuccess(o -> {
                    loggerRequest.setResponseTime(DateUtil.currentTimeMillis() - loggerRequest.getStartTime());
                    if (loggerRequest.isDetailedLog()) {
                        log.debug("API score.get successfully completed: {}", o);
                    }
                    log.info(MapUtil.parseObjectToJsonString(loggerRequest));
                });
    }

    /**
     * Query score data by id.
     * @param id score data id
     * @return Score data with respect to the id
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ScoreDTO>> getScoreById(@PathVariable Long id) {
        LoggerRequest loggerRequest = new LoggerRequest();
        loggerRequest.setQueryParams(MapUtil.parseObjectToJsonString(Map.of("id", id)));
        return Mono.just(id).doOnEach(signal -> {
            if (signal.isOnNext()){
                updateLoggerRequest(loggerRequest, signal.getContextView().get("oyoContext"));
            }
        })
                .flatMap(scoreDataService::getScoreById)
                .map(ResponseEntity::ok)
                .doOnRequest(l -> log.debug("Start fetching score data by id {}...", id))
                .doOnSuccess(o -> {
                    loggerRequest.setResponseTime(DateUtil.currentTimeMillis() - loggerRequest.getStartTime());
                    if (loggerRequest.isDetailedLog()) {
                        log.debug("API score.getById successfully completed: {}", o);
                    }
                    log.info(MapUtil.parseObjectToJsonString(loggerRequest));
                });
    }

    /**
     * Delete score by id.
     * @param id score data id
     * @return {@link OperationResponse}
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<OperationResponse>> deleteScore(@PathVariable Long id) {
        LoggerRequest loggerRequest = new LoggerRequest();
        loggerRequest.setQueryParams(MapUtil.parseObjectToJsonString(Map.of("id", id)));
        return Mono.just(id).doOnEach(signal -> {
                    if (signal.isOnNext()){
                        updateLoggerRequest(loggerRequest, signal.getContextView().get("oyoContext"));
                    }
                })
                .flatMap(scoreDataService::deleteScore)
                .map(ResponseEntity::ok)
                .doOnRequest(l -> log.debug("Start deleting score data..."))
                .doOnSuccess(o -> {
                    loggerRequest.setResponseTime(DateUtil.currentTimeMillis() - loggerRequest.getStartTime());
                    if (loggerRequest.isDetailedLog()) {
                        log.debug("API score.delete successfully completed: {}", o);
                    }
                    log.info(MapUtil.parseObjectToJsonString(loggerRequest));
                });
    }

    @GetMapping("/history/{player}")
    public Mono<ResponseEntity<Map<String, Object>>> playerScoreHistory(@PathVariable String player) {
        LoggerRequest loggerRequest = new LoggerRequest();
        loggerRequest.setQueryParams(MapUtil.parseObjectToJsonString(Map.of("player", player)));
        return Mono.just(player).doOnEach(signal -> {
                    if (signal.isOnNext()){
                        updateLoggerRequest(loggerRequest, signal.getContextView().get("oyoContext"));
                    }
                })
                .flatMap(scoreDataService::getScoreHistory)
                .map(ResponseEntity::ok)
                .doOnRequest(l -> log.debug("Start retrieving score data..."))
                .doOnSuccess(o -> {
                    loggerRequest.setResponseTime(DateUtil.currentTimeMillis() - loggerRequest.getStartTime());
                    if (loggerRequest.isDetailedLog()) {
                        loggerRequest.setResponseBody(MapUtil.parseObjectToJsonString(o));
                        log.debug("API score.history successfully completed: {}", o);
                    }
                    log.info(MapUtil.parseObjectToJsonString(loggerRequest));
                });
    }

    private void updateLoggerRequest(LoggerRequest loggerRequest, OyoContext oyoContext) {
        loggerRequest.setStartTime(oyoContext.getStartTime());
        loggerRequest.setMethod(oyoContext.getMethod());
        loggerRequest.setHost(oyoContext.getHost());
        loggerRequest.setUserAgent(oyoContext.getUserAgent());
        loggerRequest.setDetailedLog(oyoContext.isDetailedLog());
        loggerRequest.setUri(oyoContext.getUri());
    }
}
