package com.example.QuizApp.service;

import com.example.QuizApp.dao.QuestionDao;
import com.example.QuizApp.dao.QuizDao;
import com.example.QuizApp.model.Question;
import com.example.QuizApp.model.QuestionWrapper;
import com.example.QuizApp.model.Quiz;
import com.example.QuizApp.model.Response;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.http.ResponseEntity;

import java.util.*;

class QuizServiceTest {

    @Mock
    private QuizDao quizDao;

    @Mock
    private QuestionDao questionDao;

    @InjectMocks
    private QuizService quizService;

    @BeforeEach
    void setUp() {
        org.mockito.MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateQuiz() {
        List<Question> questions = Arrays.asList(new Question(), new Question());
        when(questionDao.findRandomQuestionsByCategory("Java", 2)).thenReturn(questions);

        ResponseEntity<String> response = quizService.createQuiz("Java", 2, "Java Basics");

        assertEquals(201, response.getStatusCodeValue());
        assertEquals("Success", response.getBody());

        verify(questionDao, times(1)).findRandomQuestionsByCategory("Java", 2);
        verify(quizDao, times(1)).save(any(Quiz.class));
    }

    @Test
    void testGetQuizQuestions() {
        Question q = new Question();
        q.setId(1);
        q.setQuestionTitle("Sample Question?");
        q.setOption1("A");
        q.setOption2("B");
        q.setOption3("C");
        q.setOption4("D");

        Quiz quiz = new Quiz();
        quiz.setQuestions(List.of(q));

        when(quizDao.findById(1)).thenReturn(Optional.of(quiz));

        ResponseEntity<List<QuestionWrapper>> response = quizService.getQuizQuestions(1);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
        assertEquals(1, response.getBody().get(0).getId());

        verify(quizDao, times(1)).findById(1);
    }

    @Test
    void testCalculateResult() {
        Question q = new Question();
        q.setRightAnswer("A");
        List<Question> questions = List.of(q);

        Quiz quiz = new Quiz();
        quiz.setQuestions(questions);

        Response r = new Response();
        r.setResponse("A");

        when(quizDao.findById(1)).thenReturn(Optional.of(quiz));

        ResponseEntity<Integer> response = quizService.calculateResult(1, List.of(r));

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody());

        verify(quizDao, times(1)).findById(1);
    }
}

