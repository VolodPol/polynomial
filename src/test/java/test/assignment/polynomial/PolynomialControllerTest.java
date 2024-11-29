package test.assignment.polynomial;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import test.assignment.polynomial.entity.Evaluation;
import test.assignment.polynomial.entity.RawExpression;
import test.assignment.polynomial.entity.SimplifiedExpression;
import test.assignment.polynomial.exceptions.handler.ErrorResponse;
import test.assignment.polynomial.repository.EvaluationRepository;
import test.assignment.polynomial.repository.RawExpressionRepository;
import test.assignment.polynomial.repository.SimplifiedExpressionRepository;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PolynomialControllerTest {
    @Autowired
    private MockMvc rest;

    @Autowired
    private RawExpressionRepository rawRepo;

    @Autowired
    private SimplifiedExpressionRepository simplifiedRepo;

    @Autowired
    private EvaluationRepository evaluationRepo;

    @Autowired
    private CacheManager manager;

    private static final String RAW = "(2*x+7)*(3*x^2-x+1)";

    private static final String SIMPLIFIED = "7-5*x+19*x^2+6*x^3";

    private static final String VALUE = "7";

    private static final double EVALUATED = 2961D;

    @BeforeEach
    void setUp() {
        manager.getCacheNames().forEach(s -> Objects.requireNonNull(manager.getCache(s)).clear());
    }

    @Test
    void testNewSimplifiedExpression() {
        assertThat(rawRepo.count()).isZero();
        assertThat(simplifiedRepo.count()).isZero();

        try {
            rest.perform(post("/api/simplify")
                            .contentType(MediaType.TEXT_PLAIN_VALUE)
                            .content(RAW.getBytes(StandardCharsets.UTF_8)))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType("text/plain;charset=UTF-8"))
                    .andExpect(content().string(SIMPLIFIED));
        } catch (Exception _) {
            Assertions.fail();
        }

        assertThat(rawRepo.count()).isOne();
        assertThat(simplifiedRepo.count()).isOne();

    }

    @Test
    void testExistingSimplifiedExpression() {
        prepareDB();

        assertThat(rawRepo.count()).isOne();
        assertThat(simplifiedRepo.count()).isOne();

        try {
            rest.perform(post("/api/simplify")
                            .contentType(MediaType.TEXT_PLAIN_VALUE)
                            .content(RAW))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType("text/plain;charset=UTF-8"))
                    .andExpect(content().string(SIMPLIFIED));
        } catch (Exception _) {
            Assertions.fail();
        }

        assertThat(rawRepo.count()).isOne();
        assertThat(simplifiedRepo.count()).isOne();
    }

    @Test
    void testEvaluateNotExistingSimplifiedThrows() {
        assertThat(simplifiedRepo.count()).isZero();

        String expectedResponse;
        try {
            expectedResponse = new ObjectMapper().writeValueAsString(
                    new ErrorResponse(HttpStatus.BAD_REQUEST, "Not existing simplified expression!")
            );
            rest.perform(post("/api/evaluate").param("value", VALUE)
                            .contentType(MediaType.TEXT_PLAIN_VALUE)
                            .content(SIMPLIFIED))
                    .andExpect(status().is4xxClientError())
                    .andExpect(content().json(expectedResponse));

            assertThat(simplifiedRepo.count()).isZero();
            assertThat(evaluationRepo.count()).isZero();

        } catch (Exception _) {
            Assertions.fail();
        }
    }

    @Test
    void testWithNewEvaluatedValue() {
        prepareDB();
        assertThat(evaluationRepo.count()).isZero();

        try {
            rest.perform(post("/api/evaluate").param("value", VALUE)
                            .contentType(MediaType.TEXT_PLAIN_VALUE)
                            .content(SIMPLIFIED))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().string(String.valueOf(EVALUATED)));
        } catch (Exception _) {
            Assertions.fail();
        }

        assertThat(evaluationRepo.count()).isOne();

    }


    @Test
    void testWithExistingEvaluatedValue() {
        SimplifiedExpression simplifiedEntity = prepareDB();
        Evaluation newEvaluation = Evaluation.builder()
                .result(EVALUATED)
                .build();
        evaluationRepo.save(newEvaluation);
        simplifiedEntity.addEvaluation(newEvaluation);

        assertThat(evaluationRepo.count()).isOne();

        try {
            rest.perform(post("/api/evaluate").param("value", VALUE)
                            .contentType(MediaType.TEXT_PLAIN_VALUE)
                            .content(SIMPLIFIED))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().string(String.valueOf(EVALUATED)));
        } catch (Exception _) {
            Assertions.fail();
        }

        assertThat(evaluationRepo.count()).isOne();
    }

    private SimplifiedExpression prepareDB() {
        RawExpression rawEntity = RawExpression.builder().expression(RAW).build();
        SimplifiedExpression simplifiedEntity = new SimplifiedExpression();
        simplifiedEntity.setExpression(SIMPLIFIED);
        simplifiedRepo.save(simplifiedEntity);
        simplifiedEntity.addRawExpression(rawEntity);
        rawRepo.save(rawEntity);

        return simplifiedEntity;
    }
}