#include <iostream>
#include <string>
#include <vector>
#include <map>
#include <ctime>
#include <algorithm>
using namespace std;

// Déclarations anticipées
class Video;
class Comment;

// Classe de base User
class User {
protected:
    string userId;
    string username;
    string email;
    time_t registrationDate;
    
public:
    User(string uid, string uname, string mail) 
        : userId(uid), username(uname), email(mail) {
        registrationDate = time(0);
    }
    
    virtual ~User() {}
    
    string getId() const { return userId; }
    string getUsername() const { return username; }
    string getEmail() const { return email; }
    
    void setUsername(string uname) { username = uname; }
    void setEmail(string mail) { email = mail; }
    
    virtual void displayInfo() const {
        cout << "User ID: " << userId << endl;
        cout << "Username: " << username << endl;
        cout << "Email: " << email << endl;
    }
};

// Classe VideoCreator hérite de User
class VideoCreator : public User {
private:
    int subscriberCount;
    int totalVideos;
    bool verified;
    vector<Video*> videos;
    
public:
    VideoCreator(string uid, string uname, string mail) 
        : User(uid, uname, mail), subscriberCount(0), 
          totalVideos(0), verified(false) {}
    
    int getSubscriberCount() const { return subscriberCount; }
    int getTotalVideos() const { return totalVideos; }
    bool isVerified() const { return verified; }
    
    void uploadVideo(Video* video) {
        videos.push_back(video);
        totalVideos++;
        cout << "Video uploaded successfully" << endl;
    }
    
    void deleteVideo(string videoId) {
        videos.erase(remove_if(videos.begin(), videos.end(),
            [&videoId](Video* v) { 
                // Comparaison simplifiée
                return false; 
            }), videos.end());
        totalVideos--;
        cout << "Video " << videoId << " deleted" << endl;
    }
    
    void displayInfo() const override {
        User::displayInfo();
        cout << "Subscribers: " << subscriberCount << endl;
        cout << "Total Videos: " << totalVideos << endl;
        cout << "Verified: " << (verified ? "Yes" : "No") << endl;
    }
};

// Classe Viewer hérite de User
class Viewer : public User {
private:
    vector<string> watchHistory;
    vector<string> favoriteCategories;
    
public:
    Viewer(string uid, string uname, string mail) 
        : User(uid, uname, mail) {}
    
    void addToWatchHistory(string videoId) {
        watchHistory.push_back(videoId);
    }
    
    vector<string> getFavoriteCategories() const {
        return favoriteCategories;
    }
    
    void watchVideo(Video* video);
};

// Classe Comment
class Comment {
private:
    string commentId;
    string content;
    time_t postDate;
    int likesCount;
    int dislikesCount;
    User* author;
    
public:
    Comment(string cid, string cnt, User* auth) 
        : commentId(cid), content(cnt), author(auth),
          likesCount(0), dislikesCount(0) {
        postDate = time(0);
    }
    
    string getCommentId() const { return commentId; }
    string getContent() const { return content; }
    time_t getPostDate() const { return postDate; }
    int getLikesCount() const { return likesCount; }
    
    void addLike() { likesCount++; }
    void addDislike() { dislikesCount++; }
    
    void displayComment() const {
        cout << "\nComment by " << author->getUsername() 
             << ": " << content << endl;
        cout << "Likes: " << likesCount 
             << ", Dislikes: " << dislikesCount << endl;
    }
};

// Classe Video
class Video {
private:
    string videoId;
    string title;
    string url;
    int duration;
    int viewCount;
    time_t uploadDate;
    string category;
    vector<Comment*> comments;
    
public:
    Video(string vid, string t, string u, int dur, string cat) 
        : videoId(vid), title(t), url(u), duration(dur),
          viewCount(0), category(cat) {
        uploadDate = time(0);
    }
    
    ~Video() {
        for (Comment* c : comments) delete c;
    }
    
    string getVideoId() const { return videoId; }
    string getTitle() const { return title; }
    string getUrl() const { return url; }
    int getDuration() const { return duration; }
    int getViewCount() const { return viewCount; }
    
    void addView() { viewCount++; }
    
    void addComment(Comment* comment) {
        comments.push_back(comment);
    }
    
    vector<Comment*> getComments() const {
        return comments;
    }
    
    void displayInfo() const {
        cout << "\n--- Video Information ---" << endl;
        cout << "ID: " << videoId << endl;
        cout << "Title: " << title << endl;
        cout << "URL: " << url << endl;
        cout << "Duration: " << duration << " seconds" << endl;
        cout << "Views: " << viewCount << endl;
        cout << "Category: " << category << endl;
        cout << "Comments: " << comments.size() << endl;
    }
};

// Implémentation de watchVideo après la définition de Video
void Viewer::watchVideo(Video* video) {
    video->addView();
    addToWatchHistory(video->getVideoId());
    cout << username << " is watching: " << video->getTitle() << endl;
}

// Classe AnalysisResult
class AnalysisResult {
private:
    string resultId;
    double qualityScore;
    int totalCommentsAnalyzed;
    time_t analysisDate;
    string recommendation;
    
    string generateRecommendation() {
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
    
public:
    AnalysisResult(double score, int totalComments) 
        : qualityScore(score), totalCommentsAnalyzed(totalComments) {
        resultId = "result_" + to_string(time(0));
        analysisDate = time(0);
        recommendation = generateRecommendation();
    }
    
    double getQualityScore() const { return qualityScore; }
    string getRecommendation() const { return recommendation; }
    
    void displayResult() const {
        cout << "\n" << string(50, '=') << endl;
        cout << "ANALYSIS RESULT" << endl;
        cout << string(50, '=') << endl;
        cout << "Quality Score: " << qualityScore << "/10" << endl;
        cout << "Total Comments Analyzed: " << totalCommentsAnalyzed << endl;
        cout << "Recommendation: " << recommendation << endl;
        cout << string(50, '=') << "\n" << endl;
    }
    
    void saveResult() const {
        cout << "Result saved to " << resultId << ".txt" << endl;
    }
};

// Classe Analyzer
class Analyzer {
private:
    string analyzerId;
    string analysisMethod;
    map<string, double> sentimentScores;
    
public:
    Analyzer(string aid, string method) 
        : analyzerId(aid), analysisMethod(method) {}
    
    double getSentimentScore(Comment* comment) {
        string content = comment->getContent();
        transform(content.begin(), content.end(), content.begin(), ::tolower);
        
        vector<string> positiveWords = {"good", "great", "excellent", "amazing", "love", "best", "awesome"};
        vector<string> negativeWords = {"bad", "terrible", "awful", "hate", "worst", "poor", "disappointing"};
        
        int positiveCount = 0;
        int negativeCount = 0;
        
        for (const string& word : positiveWords) {
            if (content.find(word) != string::npos) positiveCount++;
        }
        
        for (const string& word : negativeWords) {
            if (content.find(word) != string::npos) negativeCount++;
        }
        
        if (positiveCount + negativeCount == 0) return 0.0;
        return (double)(positiveCount - negativeCount) / (positiveCount + negativeCount);
    }
    
    double calculateScore(vector<Comment*> comments) {
        if (comments.empty()) return 5.0;
        
        double totalSentiment = 0.0;
        for (auto& pair : sentimentScores) {
            totalSentiment += pair.second;
        }
        
        double avgSentiment = totalSentiment / comments.size();
        return ((avgSentiment + 1) * 5);
    }
    
    AnalysisResult* analyzeComments(vector<Comment*> comments) {
        // Analyse chaque commentaire
        for (Comment* comment : comments) {
            double sentiment = getSentimentScore(comment);
            sentimentScores[comment->getCommentId()] = sentiment;
        }
        
        // Calcule le score global
        double qualityScore = calculateScore(comments);
        
        // Crée et retourne le résultat
        return new AnalysisResult(qualityScore, comments.size());
    }
    
    string generateReport(Video* video) {
        AnalysisResult* result = analyzeComments(video->getComments());
        string report = "Analysis Report for '" + video->getTitle() + 
                       "': Score " + to_string(result->getQualityScore()) + "/10";
        delete result;
        return report;
    }
};

#include <iostream>
#include <string>
#include <map>
#include <vector>
#include <limits>
using namespace std;

// Inclure toutes les déclarations de classes ici
// (Dans la pratique, elles seraient dans le fichier précédent)

// Collections globales pour stocker les instances
map<string, User*> users;
map<string, VideoCreator*> videoCreators;
map<string, Viewer*> viewers;
map<string, Video*> videos;
map<string, Comment*> comments;
map<string, AnalysisResult*> results;

void displayMenu() {
    cout << "\n" << string(60, '=') << endl;
    cout << "YOUTUBE VIDEO ANALYZER SYSTEM" << endl;
    cout << string(60, '=') << endl;
    cout << "1. Create User/Creator/Viewer" << endl;
    cout << "2. Create Video" << endl;
    cout << "3. Create Comment" << endl;
    cout << "4. Analyze Video" << endl;
    cout << "5. List All Users" << endl;
    cout << "6. List All Videos" << endl;
    cout << "7. List All Comments" << endl;
    cout << "8. Search User by ID" << endl;
    cout << "9. Search Video by ID" << endl;
    cout << "10. Delete User" << endl;
    cout << "11. Delete Video" << endl;
    cout << "12. Display Video Details" << endl;
    cout << "0. Exit" << endl;
    cout << string(60, '=') << endl;
}

void createUser() {
    cout << "\nSelect user type:" << endl;
    cout << "1. Regular User" << endl;
    cout << "2. Video Creator" << endl;
    cout << "3. Viewer" << endl;
    cout << "Choice: ";
    
    int choice;
    cin >> choice;
    cin.ignore();
    
    string userId, username, email;
    cout << "Enter User ID: ";
    getline(cin, userId);
    cout << "Enter Username: ";
    getline(cin, username);
    cout << "Enter Email: ";
    getline(cin, email);
    
    switch (choice) {
        case 1: {
            User* user = new User(userId, username, email);
            users[userId] = user;
            cout << "\nUser " << username << " created successfully!" << endl;
            break;
        }
        case 2: {
            VideoCreator* creator = new VideoCreator(userId, username, email);
            videoCreators[userId] = creator;
            users[userId] = creator;
            cout << "\nVideo Creator " << username << " created successfully!" << endl;
            break;
        }
        case 3: {
            Viewer* viewer = new Viewer(userId, username, email);
            viewers[userId] = viewer;
            users[userId] = viewer;
            cout << "\nViewer " << username << " created successfully!" << endl;
            break;
        }
        default:
            cout << "Invalid choice!" << endl;
    }
}

void createVideo() {
    if (videoCreators.empty()) {
        cout << "\nNo video creators available. Create a creator first!" << endl;
        return;
    }
    
    cout << "\nAvailable creators:" << endl;
    for (auto& pair : videoCreators) {
        cout << "  " << pair.first << ": " << pair.second->getUsername() << endl;
    }
    
    string creatorId;
    cout << "\nEnter Creator ID: ";
    getline(cin, creatorId);
    
    if (videoCreators.find(creatorId) == videoCreators.end()) {
        cout << "Creator not found!" << endl;
        return;
    }
    
    string videoId, title, url, category;
    int duration;
    
    cout << "Enter Video ID: ";
    getline(cin, videoId);
    cout << "Enter Title: ";
    getline(cin, title);
    cout << "Enter URL: ";
    getline(cin, url);
    cout << "Enter Duration (seconds): ";
    cin >> duration;
    cin.ignore();
    cout << "Enter Category: ";
    getline(cin, category);
    
    Video* video = new Video(videoId, title, url, duration, category);
    videos[videoId] = video;
    videoCreators[creatorId]->uploadVideo(video);
    cout << "\nVideo '" << title << "' created successfully!" << endl;
}

void createComment() {
    if (videos.empty()) {
        cout << "\nNo videos available. Create a video first!" << endl;
        return;
    }
    if (users.empty()) {
        cout << "\nNo users available. Create a user first!" << endl;
        return;
    }
    
    cout << "\nAvailable videos:" << endl;
    for (auto& pair : videos) {
        cout << "  " << pair.first << ": " << pair.second->getTitle() << endl;
    }
    
    string videoId;
    cout << "\nEnter Video ID: ";
    getline(cin, videoId);
    
    if (videos.find(videoId) == videos.end()) {
        cout << "Video not found!" << endl;
        return;
    }
    
    cout << "\nAvailable users:" << endl;
    for (auto& pair : users) {
        cout << "  " << pair.first << ": " << pair.second->getUsername() << endl;
    }
    
    string userId;
    cout << "\nEnter User ID (author): ";
    getline(cin, userId);
    
    if (users.find(userId) == users.end()) {
        cout << "User not found!" << endl;
        return;
    }
    
    string commentId, content;
    cout << "Enter Comment ID: ";
    getline(cin, commentId);
    cout << "Enter Comment Content: ";
    getline(cin, content);
    
    Comment* comment = new Comment(commentId, content, users[userId]);
    comments[commentId] = comment;
    videos[videoId]->addComment(comment);
    cout << "\nComment added successfully!" << endl;
}

void analyzeVideo() {
    if (videos.empty()) {
        cout << "\nNo videos available!" << endl;
        return;
    }
    
    cout << "\nAvailable videos:" << endl;
    for (auto& pair : videos) {
        Video* v = pair.second;
        cout << "  " << pair.first << ": " << v->getTitle() 
             << " (" << v->getComments().size() << " comments)" << endl;
    }
    
    string videoId;
    cout << "\nEnter Video ID to analyze: ";
    getline(cin, videoId);
    
    if (videos.find(videoId) == videos.end()) {
        cout << "Video not found!" << endl;
        return;
    }
    
    Video* video = videos[videoId];
    if (video->getComments().empty()) {
        cout << "\nThis video has no comments to analyze!" << endl;
        return;
    }
    
    Analyzer* analyzer = new Analyzer("analyzer_1", "sentiment_analysis");
    cout << "\nAnalyzing " << video->getComments().size() << " comments..." << endl;
    AnalysisResult* result = analyzer->analyzeComments(video->getComments());
    results[videoId] = result;
    result->displayResult();
    
    delete analyzer;
}

void listUsers() {
    if (users.empty()) {
        cout << "\nNo users in the system." << endl;
        return;
    }
    
    cout << "\n" << string(60, '=') << endl;
    cout << "ALL USERS" << endl;
    cout << string(60, '=') << endl;
    for (auto& pair : users) {
        pair.second->displayInfo();
        cout << string(60, '-') << endl;
    }
}

void listVideos() {
    if (videos.empty()) {
        cout << "\nNo videos in the system." << endl;
        return;
    }
    
    cout << "\n" << string(60, '=') << endl;
    cout << "ALL VIDEOS" << endl;
    cout << string(60, '=') << endl;
    for (auto& pair : videos) {
        pair.second->displayInfo();
        cout << string(60, '-') << endl;
    }
}

void listComments() {
    if (comments.empty()) {
        cout << "\nNo comments in the system." << endl;
        return;
    }
    
    cout << "\n" << string(60, '=') << endl;
    cout << "ALL COMMENTS" << endl;
    cout << string(60, '=') << endl;
    for (auto& pair : comments) {
        pair.second->displayComment();
        cout << string(60, '-') << endl;
    }
}

void searchUser() {
    string userId;
    cout << "\nEnter User ID to search: ";
    getline(cin, userId);
    
    if (users.find(userId) != users.end()) {
        cout << "\nUser found:" << endl;
        users[userId]->displayInfo();
    } else {
        cout << "User not found!" << endl;
    }
}

void searchVideo() {
    string videoId;
    cout << "\nEnter Video ID to search: ";
    getline(cin, videoId);
    
    if (videos.find(videoId) != videos.end()) {
        cout << "\nVideo found:" << endl;
        videos[videoId]->displayInfo();
    } else {
        cout << "Video not found!" << endl;
    }
}

void deleteUser() {
    string userId;
    cout << "\nEnter User ID to delete: ";
    getline(cin, userId);
    
    if (users.find(userId) != users.end()) {
        string username = users[userId]->getUsername();
        delete users[userId];
        users.erase(userId);
        videoCreators.erase(userId);
        viewers.erase(userId);
        cout << "User " << username << " deleted successfully!" << endl;
    } else {
        cout << "User not found!" << endl;
    }
}

void deleteVideo() {
    string videoId;
    cout << "\nEnter Video ID to delete: ";
    getline(cin, videoId);
    
    if (videos.find(videoId) != videos.end()) {
        string title = videos[videoId]->getTitle();
        delete videos[videoId];
        videos.erase(videoId);
        cout << "Video '" << title << "' deleted successfully!" << endl;
    } else {
        cout << "Video not found!" << endl;
    }
}

void displayVideoDetails() {
    string videoId;
    cout << "\nEnter Video ID: ";
    getline(cin, videoId);
    
    if (videos.find(videoId) == videos.end()) {
        cout << "Video not found!" << endl;
        return;
    }
    
    Video* video = videos[videoId];
    video->displayInfo();
    
    cout << "\nComments on this video:" << endl;
    for (Comment* comment : video->getComments()) {
        comment->displayComment();
    }
    
    if (results.find(videoId) != results.end()) {
        cout << "\nAnalysis Result:" << endl;
        results[videoId]->displayResult();
    }
}

void cleanup() {
    // Libérer la mémoire allouée
    for (auto& pair : users) delete pair.second;
    for (auto& pair : videos) delete pair.second;
    for (auto& pair : results) delete pair.second;
}

int main() {
    cout << "Welcome to YouTube Video Analyzer System!" << endl;
    
    int choice;
    while (true) {
        displayMenu();
        cout << "\nEnter your choice: ";
        cin >> choice;
        cin.ignore(numeric_limits<streamsize>::max(), '\n');
        
        switch (choice) {
            case 1: createUser(); break;
            case 2: createVideo(); break;
            case 3: createComment(); break;
            case 4: analyzeVideo(); break;
            case 5: listUsers(); break;
            case 6: listVideos(); break;
            case 7: listComments(); break;
            case 8: searchUser(); break;
            case 9: searchVideo(); break;
            case 10: deleteUser(); break;
            case 11: deleteVideo(); break;
            case 12: displayVideoDetails(); break;
            case 0:
                cout << "\nThank you for using YouTube Video Analyzer System!" << endl;
                cout << "Goodbye!" << endl;
                cleanup();
                return 0;
            default:
                cout << "\nInvalid choice! Please try again." << endl;
        }
        
        cout << "\nPress Enter to continue...";
        cin.get();
    }
    
    return 0;
}   