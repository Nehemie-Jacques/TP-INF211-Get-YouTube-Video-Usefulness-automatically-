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

import java.util.*;

public class YouTubeAnalyzerSystem {
    // Collections pour stocker les instances
    private static Map<String, User> users = new HashMap<>();
    private static Map<String, VideoCreator> videoCreators = new HashMap<>();
    private static Map<String, Viewer> viewers = new HashMap<>();
    private static Map<String, Video> videos = new HashMap<>();
    private static Map<String, Comment> comments = new HashMap<>();
    private static Map<String, AnalysisResult> results = new HashMap<>();
    
    private static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        System.out.println("Welcome to YouTube Video Analyzer System!");
        
        while (true) {
            displayMenu();
            System.out.print("\nEnter your choice: ");
            String choice = scanner.nextLine();
            
            switch (choice) {
                case "1": createUser(); break;
                case "2": createVideo(); break;
                case "3": createComment(); break;
                case "4": analyzeVideo(); break;
                case "5": listUsers(); break;
                case "6": listVideos(); break;
                case "7": listComments(); break;
                case "8": searchUser(); break;
                case "9": searchVideo(); break;
                case "10": deleteUser(); break;
                case "11": deleteVideo(); break;
                case "12": displayVideoDetails(); break;
                case "0":
                    System.out.println("\nThank you for using YouTube Video Analyzer System!");
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("\nInvalid choice! Please try again.");
            }
            
            System.out.print("\nPress Enter to continue...");
            scanner.nextLine();
        }
    }
    
    private static void displayMenu() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("YOUTUBE VIDEO ANALYZER SYSTEM");
        System.out.println("=".repeat(60));
        System.out.println("1. Create User/Creator/Viewer");
        System.out.println("2. Create Video");
        System.out.println("3. Create Comment");
        System.out.println("4. Analyze Video");
        System.out.println("5. List All Users");
        System.out.println("6. List All Videos");
        System.out.println("7. List All Comments");
        System.out.println("8. Search User by ID");
        System.out.println("9. Search Video by ID");
        System.out.println("10. Delete User");
        System.out.println("11. Delete Video");
        System.out.println("12. Display Video Details");
        System.out.println("0. Exit");
        System.out.println("=".repeat(60));
    }
    
    private static void createUser() {
        System.out.println("\nSelect user type:");
        System.out.println("1. Regular User");
        System.out.println("2. Video Creator");
        System.out.println("3. Viewer");
        System.out.print("Choice: ");
        String choice = scanner.nextLine();
        
        System.out.print("Enter User ID: ");
        String userId = scanner.nextLine();
        System.out.print("Enter Username: ");
        String username = scanner.nextLine();
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        
        switch (choice) {
            case "1":
                User user = new User(userId, username, email);
                users.put(userId, user);
                System.out.println("\nUser " + username + " created successfully!");
                break;
            case "2":
                VideoCreator creator = new VideoCreator(userId, username, email);
                videoCreators.put(userId, creator);
                users.put(userId, creator);
                System.out.println("\nVideo Creator " + username + " created successfully!");
                break;
            case "3":
                Viewer viewer = new Viewer(userId, username, email);
                viewers.put(userId, viewer);
                users.put(userId, viewer);
                System.out.println("\nViewer " + username + " created successfully!");
                break;
            default:
                System.out.println("Invalid choice!");
        }
    }
    
    private static void createVideo() {
        if (videoCreators.isEmpty()) {
            System.out.println("\nNo video creators available. Create a creator first!");
            return;
        }
        
        System.out.println("\nAvailable creators:");
        for (Map.Entry<String, VideoCreator> entry : videoCreators.entrySet()) {
            System.out.println("  " + entry.getKey() + ": " + entry.getValue().getUsername());
        }
        
        System.out.print("\nEnter Creator ID: ");
        String creatorId = scanner.nextLine();
        if (!videoCreators.containsKey(creatorId)) {
            System.out.println("Creator not found!");
            return;
        }
        
        System.out.print("Enter Video ID: ");
        String videoId = scanner.nextLine();
        System.out.print("Enter Title: ");
        String title = scanner.nextLine();
        System.out.print("Enter URL: ");
        String url = scanner.nextLine();
        System.out.print("Enter Duration (seconds): ");
        int duration = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter Category: ");
        String category = scanner.nextLine();
        
        Video video = new Video(videoId, title, url, duration, category);
        videos.put(videoId, video);
        videoCreators.get(creatorId).uploadVideo(video);
        System.out.println("\nVideo '" + title + "' created successfully!");
    }
    
    private static void createComment() {
        if (videos.isEmpty()) {
            System.out.println("\nNo videos available. Create a video first!");
            return;
        }
        if (users.isEmpty()) {
            System.out.println("\nNo users available. Create a user first!");
            return;
        }
        
        System.out.println("\nAvailable videos:");
        for (Map.Entry<String, Video> entry : videos.entrySet()) {
            System.out.println("  " + entry.getKey() + ": " + entry.getValue().getTitle());
        }
        
        System.out.print("\nEnter Video ID: ");
        String videoId = scanner.nextLine();
        if (!videos.containsKey(videoId)) {
            System.out.println("Video not found!");
            return;
        }
        
        System.out.println("\nAvailable users:");
        for (Map.Entry<String, User> entry : users.entrySet()) {
            System.out.println("  " + entry.getKey() + ": " + entry.getValue().getUsername());
        }
        
        System.out.print("\nEnter User ID (author): ");
        String userId = scanner.nextLine();
        if (!users.containsKey(userId)) {
            System.out.println("User not found!");
            return;
        }
        
        System.out.print("Enter Comment ID: ");
        String commentId = scanner.nextLine();
        System.out.print("Enter Comment Content: ");
        String content = scanner.nextLine();
        
        Comment comment = new Comment(commentId, content, users.get(userId));
        comments.put(commentId, comment);
        videos.get(videoId).addComment(comment);
        System.out.println("\nComment added successfully!");
    }
    
    private static void analyzeVideo() {
        if (videos.isEmpty()) {
            System.out.println("\nNo videos available!");
            return;
        }
        
        System.out.println("\nAvailable videos:");
        for (Map.Entry<String, Video> entry : videos.entrySet()) {
            Video v = entry.getValue();
            System.out.println("  " + entry.getKey() + ": " + v.getTitle() + 
                             " (" + v.getComments().size() + " comments)");
        }
        
        System.out.print("\nEnter Video ID to analyze: ");
        String videoId = scanner.nextLine();
        if (!videos.containsKey(videoId)) {
            System.out.println("Video not found!");
            return;
        }
        
        Video video = videos.get(videoId);
        if (video.getComments().isEmpty()) {
            System.out.println("\nThis video has no comments to analyze!");
            return;
        }
        
        Analyzer analyzer = new Analyzer("analyzer_1", "sentiment_analysis");
        System.out.println("\nAnalyzing " + video.getComments().size() + " comments...");
        AnalysisResult result = analyzer.analyzeComments(video.getComments());
        results.put(videoId, result);
        result.displayResult();
    }
    
    private static void listUsers() {
        if (users.isEmpty()) {
            System.out.println("\nNo users in the system.");
            return;
        }
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("ALL USERS");
        System.out.println("=".repeat(60));
        for (User user : users.values()) {
            user.displayInfo();
            System.out.println("-".repeat(60));
        }
    }
    
    private static void listVideos() {
        if (videos.isEmpty()) {
            System.out.println("\nNo videos in the system.");
            return;
        }
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("ALL VIDEOS");
        System.out.println("=".repeat(60));
        for (Video video : videos.values()) {
            video.displayInfo();
            System.out.println("-".repeat(60));
        }
    }
    
    private static void listComments() {
        if (comments.isEmpty()) {
            System.out.println("\nNo comments in the system.");
            return;
        }
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("ALL COMMENTS");
        System.out.println("=".repeat(60));
        for (Comment comment : comments.values()) {
            comment.displayComment();
            System.out.println("-".repeat(60));
        }
    }
    
    private static void searchUser() {
        System.out.print("\nEnter User ID to search: ");
        String userId = scanner.nextLine();
        if (users.containsKey(userId)) {
            System.out.println("\nUser found:");
            users.get(userId).displayInfo();
        } else {
            System.out.println("User not found!");
        }
    }
    
    private static void searchVideo() {
        System.out.print("\nEnter Video ID to search: ");
        String videoId = scanner.nextLine();
        if (videos.containsKey(videoId)) {
            System.out.println("\nVideo found:");
            videos.get(videoId).displayInfo();
        } else {
            System.out.println("Video not found!");
        }
    }
    
    private static void deleteUser() {
        System.out.print("\nEnter User ID to delete: ");
        String userId = scanner.nextLine();
        if (users.containsKey(userId)) {
            String username = users.get(userId).getUsername();
            users.remove(userId);
            videoCreators.remove(userId);
            viewers.remove(userId);
            System.out.println("User " + username + " deleted successfully!");
        } else {
            System.out.println("User not found!");
        }
    }
    
    private static void deleteVideo() {
        System.out.print("\nEnter Video ID to delete: ");
        String videoId = scanner.nextLine();
        if (videos.containsKey(videoId)) {
            String title = videos.get(videoId).getTitle();
            videos.remove(videoId);
            System.out.println("Video '" + title + "' deleted successfully!");
        } else {
            System.out.println("Video not found!");
        }
    }
    
    private static void displayVideoDetails() {
        System.out.print("\nEnter Video ID: ");
        String videoId = scanner.nextLine();
        if (!videos.containsKey(videoId)) {
            System.out.println("Video not found!");
            return;
        }
        
        Video video = videos.get(videoId);
        video.displayInfo();
        
        System.out.println("\nComments on this video:");
        for (Comment comment : video.getComments()) {
            comment.displayComment();
        }
        
        if (results.containsKey(videoId)) {
            System.out.println("\nAnalysis Result:");
            results.get(videoId).displayResult();
        }
    }
}