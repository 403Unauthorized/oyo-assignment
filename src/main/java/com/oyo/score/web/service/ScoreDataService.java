package com.oyo.score.web.service;

import com.oyo.score.web.entity.db.TblScore;
import com.oyo.score.web.entity.dto.ScoreDTO;
import com.oyo.score.web.entity.dto.request.ScoreCreateRequest;
import com.oyo.score.web.entity.dto.request.ScoreQueryRequest;
import com.oyo.score.web.entity.dto.response.OperationResponse;
import com.oyo.score.web.entity.dto.response.PageResponse;
import com.oyo.score.web.repository.TblScoreRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import javax.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service component for fetching, deleting and creating data for score.
 */
@Service
public class ScoreDataService {

    private static final Logger log = LoggerFactory.getLogger(ScoreDataService.class);
    private final TblScoreRepository scoreRepository;

    public ScoreDataService(TblScoreRepository scoreRepository) {
        this.scoreRepository = scoreRepository;
    }

    public Mono<OperationResponse> createScore(ScoreCreateRequest request) {
        OperationResponse response = new OperationResponse();
        return Mono.just(response).flatMap(resp -> {
            TblScore score = new TblScore(request.getPlayer(), request.getScore(), request.getTime());
            score = scoreRepository.save(score);
            log.info("Score data has beed stored with id: {}", score.getId());
            resp.setCreated(1);
            resp.setStatus("SUCCESS");
            return Mono.just(resp);
        })
                .onErrorResume(throwable -> {
                    log.error("Error occurred while saving score data.", throwable);
                    response.setStatus("FAILURE");
                    return Mono.just(response);
                });
    }

    public Mono<PageResponse<ScoreDTO>> getScores(ScoreQueryRequest request) {
        Specification<TblScore> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!ObjectUtils.isEmpty(request.getPlayer())) {
                predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("player")), request.getPlayer().toLowerCase()));
            }
            if (!ObjectUtils.isEmpty(request.getStart())) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("time"),
                        request.getStart().atStartOfDay()));
            }
            if (!ObjectUtils.isEmpty(request.getEnd())) {
                predicates.add(criteriaBuilder.lessThan(root.get("time"), request.getEnd().plusDays(1).atStartOfDay()));
            }
            if (!ObjectUtils.isEmpty(predicates)) {
                return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
            }
            return null;
        };
        return Mono.just(spec).flatMap(specification -> {
            Pageable pageable = PageRequest.of(request.getPage() - 1, request.getPageSize(), Sort.by(Sort.Order.desc("score")));
            Page<TblScore> pageData = scoreRepository.findAll(spec, pageable);
            List<ScoreDTO> scores = pageData.get().map(tblScore -> new ScoreDTO(tblScore.getId(), tblScore.getPlayer(),
                    tblScore.getScore(), tblScore.getTime())).collect(Collectors.toList());
            return Mono.just(new PageResponse<>(scores, request.getPage(), pageData.getTotalPages(), pageData.getTotalElements()));
        });
    }

    public Mono<ScoreDTO> getScoreById(Long id) {
        return Mono.just(id).flatMap(key -> {
            ScoreDTO scoreDTO = scoreRepository.findById(id)
                    .map(tblScore -> new ScoreDTO(tblScore.getId(), tblScore.getPlayer(),
                            tblScore.getScore(), tblScore.getTime()))
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No data found"));
            return Mono.just(scoreDTO);
        });
    }

    public Mono<OperationResponse> deleteScore(Long id) {
        OperationResponse response = new OperationResponse();
        return Mono.just(response).flatMap(resp -> {
            scoreRepository.deleteById(id);
            resp.setDeleted(1);
            resp.setStatus("SUCCESS");
            return Mono.just(resp);
        })
                .onErrorResume(throwable -> {
                    if (throwable instanceof EmptyResultDataAccessException) {
                        response.setStatus("SUCCESS");
                    } else {
                        response.setStatus("FAILURE");
                    }
                    return Mono.just(response);
                });
    }

    public Mono<Map<String, Object>> getScoreHistory(String player) {
        Specification<TblScore> spec = (root, query, criteriaBuilder) ->
                criteriaBuilder.and(criteriaBuilder
                        .equal(criteriaBuilder.lower(root.get("player")), player.toLowerCase()));
        List<TblScore> scores = scoreRepository.findAll(spec);
        List<ScoreDTO> scoreDTOList = scores.stream().map(tblScore -> new ScoreDTO(tblScore.getId(), tblScore.getPlayer(),
                tblScore.getScore(), tblScore.getTime())).collect(Collectors.toList());
        var summary = scores.stream().collect(Collectors.summarizingInt(TblScore::getScore));
        BigDecimal avg = BigDecimal.valueOf(summary.getAverage()).setScale(2, RoundingMode.HALF_UP);
        return Mono.just(Map.of(
                "player", player,
                "top", summary.getMax(),
                "low", summary.getMin(),
                "avg", avg.doubleValue(),
                "data", scoreDTOList
        ));
    }
}
