package com.example.QuizApp.controller;

import com.example.QuizApp.service.QuizService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
        import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(QuizController.class)
public class QuizControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QuizService quizService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateQuiz() throws Exception {
        Mockito.when(quizService.createQuiz("java", 5, "Java Quiz"))
                .thenReturn(new org.springframework.http.ResponseEntity<>("Success", org.springframework.http.HttpStatus.CREATED));

        mockMvc.perform(post("/quiz/create")
                        .param("category", "java")
                        .param("numQ", "5")
                        .param("title", "Java Quiz"))
                .andExpect(status().isCreated())
                .andExpect(content().string("Success"));
    }

    @Test
    void testGetQuizQuestions() throws Exception {
        Mockito.when(quizService.getQuizQuestions(1))
                .thenReturn(new org.springframework.http.ResponseEntity<>(List.of(), org.springframework.http.HttpStatus.OK));

        mockMvc.perform(get("/quiz/get/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testSubmitQuiz() throws Exception {
        Mockito.when(quizService.calculateResult(eq(1), any()))
                .thenReturn(new org.springframework.http.ResponseEntity<>(3, org.springframework.http.HttpStatus.OK));

        mockMvc.perform(post("/quiz/submit/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of())))
                .andExpect(status().isOk())
                .andExpect(content().string("3"));
    }
}
