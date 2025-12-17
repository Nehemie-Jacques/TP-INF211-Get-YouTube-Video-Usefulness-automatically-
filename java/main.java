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