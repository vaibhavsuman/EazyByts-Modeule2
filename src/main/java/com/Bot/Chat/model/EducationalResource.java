package com.Bot.Chat.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "educational_resources")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EducationalResource {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(columnDefinition = "TEXT")
    private String content;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "resource_type", nullable = false)
    private ResourceType resourceType;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty_level", nullable = false)
    private DifficultyLevel difficultyLevel;
    
    @Column(name = "estimated_duration")
    private Integer estimatedDuration; // in minutes
    
    @Column(name = "video_url")
    private String videoUrl;
    
    @Column(name = "image_url")
    private String imageUrl;
    
    @Column(name = "external_link")
    private String externalLink;
    
    @Column(name = "tags")
    private String tags; // Comma-separated tags
    
    @Column(name = "is_published")
    private boolean isPublished = true;
    
    @Column(name = "view_count")
    private Long viewCount = 0L;
    
    @Column(name = "rating")
    private Double rating = 0.0;
    
    @Column(name = "rating_count")
    private Long ratingCount = 0L;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "resource", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Quiz> quizzes;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    public void incrementViewCount() {
        this.viewCount++;
    }
    
    public void updateRating(Double newRating) {
        if (ratingCount == 0) {
            this.rating = newRating;
        } else {
            double totalRating = rating * ratingCount + newRating;
            this.rating = totalRating / (ratingCount + 1);
        }
        this.ratingCount++;
    }
    
    public enum ResourceType {
        ARTICLE,
        VIDEO,
        TUTORIAL,
        QUIZ,
        WEBINAR,
        EBOOK,
        CASE_STUDY,
        GLOSSARY
    }
    
    public enum DifficultyLevel {
        BEGINNER,
        INTERMEDIATE,
        ADVANCED,
        EXPERT
    }
}
