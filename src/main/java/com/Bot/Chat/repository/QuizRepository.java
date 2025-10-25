package com.Bot.Chat.repository;

import com.Bot.Chat.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    
    List<Quiz> findByResourceId(Long resourceId);
    
    @Query("SELECT q FROM Quiz q WHERE q.difficulty = :difficulty")
    List<Quiz> findByDifficulty(@Param("difficulty") Quiz.DifficultyLevel difficulty);
    
    @Query("SELECT q FROM Quiz q WHERE q.questionType = :questionType")
    List<Quiz> findByQuestionType(@Param("questionType") Quiz.QuestionType questionType);
}
