import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Classe de base User
class User {
    private String userId;
    private String username;
    private String email;
    private LocalDateTime registrationDate;
    
    public User(String userId, String username, String email) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.registrationDate = LocalDateTime.now();
    }
    
    public String getId() { return userId; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    
    public void displayInfo() {
        System.out.println("User ID: " + userId);
        System.out.println("Username: " + username);
        System.out.println("Email: " + email);
        System.out.println("Registration Date: " + registrationDate.format(DateTimeFormatter.ISO_DATE));
    }
}

// Classe VideoCreator hérite de User
class VideoCreator extends User {
    private int subscriberCount;
    private int totalVideos;
    private boolean isVerified;
    private List<Video> videos;
    
    public VideoCreator(String userId, String username, String email) {
        super(userId, username, email);
        this.subscriberCount = 0;
        this.totalVideos = 0;
        this.isVerified = false;
        this.videos = new ArrayList<>();
    }
    
    public int getSubscriberCount() { return subscriberCount; }
    public int getTotalVideos() { return totalVideos; }
    public boolean isVerified() { return isVerified; }
    
    public void uploadVideo(Video video) {
        videos.add(video);
        totalVideos++;
        System.out.println("Video '" + video.getTitle() + "' uploaded successfully");
    }
    
    public void deleteVideo(String videoId) {
        videos.removeIf(v -> v.getVideoId().equals(videoId));
        totalVideos--;
        System.out.println("Video " + videoId + " deleted");
    }
    
    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Subscribers: " + subscriberCount);
        System.out.println("Total Videos: " + totalVideos);
        System.out.println("Verified: " + isVerified);
    }
}

// Classe Viewer hérite de User
class Viewer extends User {
    private List<String> watchHistory;
    private List<String> favoriteCategories;
    
    public Viewer(String userId, String username, String email) {
        super(userId, username, email);
        this.watchHistory = new ArrayList<>();
        this.favoriteCategories = new ArrayList<>();
    }
    
    public void addToWatchHistory(String videoId) {
        watchHistory.add(videoId);
    }
    
    public List<String> getFavoriteCategories() {
        return favoriteCategories;
    }
    
    public void watchVideo(Video video) {
        video.addView();
        addToWatchHistory(video.getVideoId());
        System.out.println(getUsername() + " is watching: " + video.getTitle());
    }
}

// Classe Video
class Video {
    private String videoId;
    private String title;
    private String url;
    private int duration;
    private int viewCount;
    private LocalDateTime uploadDate;
    private String category;
    private List<Comment> comments;
    
    public Video(String videoId, String title, String url, int duration, String category) {
        this.videoId = videoId;
        this.title = title;
        this.url = url;
        this.duration = duration;
        this.viewCount = 0;
        this.uploadDate = LocalDateTime.now();
        this.category = category;
        this.comments = new ArrayList<>();
    }
    
    public String getVideoId() { return videoId; }
    public String getTitle() { return title; }
    public String getUrl() { return url; }
    public int getDuration() { return duration; }
    public int getViewCount() { return viewCount; }
    
    public void addView() { viewCount++; }
    
    public void addComment(Comment comment) {
        comments.add(comment);
    }
    
    public List<Comment> getComments() {
        return comments;
    }
    
    public void displayInfo() {
        System.out.println("\n--- Video Information ---");
        System.out.println("ID: " + videoId);
        System.out.println("Title: " + title);
        System.out.println("URL: " + url);
        System.out.println("Duration: " + duration + " seconds");
        System.out.println("Views: " + viewCount);
        System.out.println("Category: " + category);
        System.out.println("Comments: " + comments.size());
    }
}

// Classe Comment
class Comment {
    private String commentId;
    private String content;
    private LocalDateTime postDate;
    private int likesCount;
    private int dislikesCount;
    private User author;
    
    public Comment(String commentId, String content, User author) {
        this.commentId = commentId;
        this.content = content;
        this.author = author;
        this.postDate = LocalDateTime.now();
        this.likesCount = 0;
        this.dislikesCount = 0;
    }
    
    public String getCommentId() { return commentId; }
    public String getContent() { return content; }
    public LocalDateTime getPostDate() { return postDate; }
    public int getLikesCount() { return likesCount; }
    
    public void addLike() { likesCount++; }
    public void addDislike() { dislikesCount++; }
    
    public void displayComment() {
        System.out.println("\nComment by " + author.getUsername() + ": " + content);
        System.out.println("Likes: " + likesCount + ", Dislikes: " + dislikesCount);
    }
}

// Classe Analyzer
class Analyzer {
    private String analyzerId;
    private String analysisMethod;
    private Map<String, Double> sentimentScores;
    
    public Analyzer(String analyzerId, String analysisMethod) {
        this.analyzerId = analyzerId;
        this.analysisMethod = analysisMethod;
        this.sentimentScores = new HashMap<>();
    }
    
    public AnalysisResult analyzeComments(List<Comment> comments) {
        // Analyse chaque commentaire
        for (Comment comment : comments) {
            double sentiment = getSentimentScore(comment);
            sentimentScores.put(comment.getCommentId(), sentiment);
        }
        
        // Calcule le score global
        double qualityScore = calculateScore(comments);
        
        // Crée et retourne le résultat
        return new AnalysisResult(qualityScore, comments.size());
    }
    
    public double getSentimentScore(Comment comment) {
        String content = comment.getContent().toLowerCase();
        String[] positiveWords = {"good", "great", "excellent", "amazing", "love", "best", "awesome"};
        String[] negativeWords = {"bad", "terrible", "awful", "hate", "worst", "poor", "disappointing"};
        
        int positiveCount = 0;
        int negativeCount = 0;
        
        for (String word : positiveWords) {
            if (content.contains(word)) positiveCount++;
        }
        
        for (String word : negativeWords) {
            if (content.contains(word)) negativeCount++;
        }
        
        if (positiveCount + negativeCount == 0) return 0.0;
        return (double)(positiveCount - negativeCount) / (positiveCount + negativeCount);
    }
    
    public double calculateScore(List<Comment> comments) {
        if (comments.isEmpty()) return 5.0;
        
        double totalSentiment = 0.0;
        for (Double score : sentimentScores.values()) {
            totalSentiment += score;
        }
        
        double avgSentiment = totalSentiment / comments.size();
        return Math.round((avgSentiment + 1) * 5 * 100.0) / 100.0;
    }
    
    public String generateReport(Video video) {
        AnalysisResult result = analyzeComments(video.getComments());
        return "Analysis Report for '" + video.getTitle() + "': Score " + 
               result.getQualityScore() + "/10";
    }
}

// Classe AnalysisResult
class AnalysisResult {
    private String resultId;
    private double qualityScore;
    private int totalCommentsAnalyzed;
    private LocalDateTime analysisDate;
    private String recommendation;
    
    public AnalysisResult(double qualityScore, int totalComments) {
        this.resultId = "result_" + System.currentTimeMillis();
        this.qualityScore = qualityScore;
        this.totalCommentsAnalyzed = totalComments;
        this.analysisDate = LocalDateTime.now();
        this.recommendation = generateRecommendation();
    }
    
    public double getQualityScore() { return qualityScore; }
    public String getRecommendation() { return recommendation; }
    
    private String generateRecommendation() {
        if (qualityScore >= 8.0) {
            return "Highly recommended! This video has excellent reviews.";
        } else if (qualityScore >= 6.0) {
            return "Recommended. This video has good reviews overall.";
        } else if (qualityScore >= 4.0) {
            return "Mixed reviews. Watch at your own discretion.";
        } else {
            return "Not recommended. This video has poor reviews.";
        }
    }
    
    public void displayResult() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("ANALYSIS RESULT");
        System.out.println("=".repeat(50));
        System.out.println("Quality Score: " + qualityScore + "/10");
        System.out.println("Total Comments Analyzed: " + totalCommentsAnalyzed);
        System.out.println("Recommendation: " + recommendation);
        System.out.println("Analysis Date: " + analysisDate.format(DateTimeFormatter.ISO_DATE_TIME));
        System.out.println("=".repeat(50) + "\n");
    }
    
    public void saveResult() {
        System.out.println("Result saved to " + resultId + ".txt");
    }
}