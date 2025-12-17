from datetime import datetime
from typing import List

# Classe de base User
class User:
    def __init__(self, user_id: str, username: str, email: str):
        self._user_id = user_id
        self._username = username
        self._email = email
        self._registration_date = datetime.now()
    
    def get_id(self) -> str:
        return self._user_id
    
    def get_username(self) -> str:
        return self._username
    
    def get_email(self) -> str:
        return self._email
    
    def set_username(self, username: str):
        self._username = username
    
    def set_email(self, email: str):
        self._email = email
    
    def display_info(self):
        print(f"User ID: {self._user_id}")
        print(f"Username: {self._username}")
        print(f"Email: {self._email}")
        print(f"Registration Date: {self._registration_date.strftime('%Y-%m-%d')}")


# Classe VideoCreator hérite de User
class VideoCreator(User):
    def __init__(self, user_id: str, username: str, email: str):
        super().__init__(user_id, username, email)
        self._subscriber_count = 0
        self._total_videos = 0
        self._is_verified = False
        self._videos = []  # Liste des vidéos publiées
    
    def get_subscriber_count(self) -> int:
        return self._subscriber_count
    
    def get_total_videos(self) -> int:
        return self._total_videos
    
    def is_verified(self) -> bool:
        return self._is_verified
    
    def upload_video(self, video):
        self._videos.append(video)
        self._total_videos += 1
        print(f"Video '{video.get_title()}' uploaded successfully")
    
    def delete_video(self, video_id: str):
        self._videos = [v for v in self._videos if v.get_video_id() != video_id]
        self._total_videos -= 1
        print(f"Video {video_id} deleted")
    
    def display_info(self):
        super().display_info()
        print(f"Subscribers: {self._subscriber_count}")
        print(f"Total Videos: {self._total_videos}")
        print(f"Verified: {self._is_verified}")


# Classe Viewer hérite de User
class Viewer(User):
    def __init__(self, user_id: str, username: str, email: str):
        super().__init__(user_id, username, email)
        self._watch_history = []
        self._favorite_categories = []
    
    def add_to_watch_history(self, video_id: str):
        self._watch_history.append(video_id)
    
    def get_favorite_categories(self) -> List[str]:
        return self._favorite_categories
    
    def watch_video(self, video):
        video.add_view()
        self.add_to_watch_history(video.get_video_id())
        print(f"{self._username} is watching: {video.get_title()}")


# Classe Video
class Video:
    def __init__(self, video_id: str, title: str, url: str, duration: int, category: str):
        self._video_id = video_id
        self._title = title
        self._url = url
        self._duration = duration
        self._view_count = 0
        self._upload_date = datetime.now()
        self._category = category
        self._comments = []  # Liste des commentaires
    
    def get_video_id(self) -> str:
        return self._video_id
    
    def get_title(self) -> str:
        return self._title
    
    def get_url(self) -> str:
        return self._url
    
    def get_duration(self) -> int:
        return self._duration
    
    def get_view_count(self) -> int:
        return self._view_count
    
    def add_view(self):
        self._view_count += 1
    
    def add_comment(self, comment):
        self._comments.append(comment)
    
    def get_comments(self) -> List:
        return self._comments
    
    def display_info(self):
        print(f"\n--- Video Information ---")
        print(f"ID: {self._video_id}")
        print(f"Title: {self._title}")
        print(f"URL: {self._url}")
        print(f"Duration: {self._duration} seconds")
        print(f"Views: {self._view_count}")
        print(f"Category: {self._category}")
        print(f"Comments: {len(self._comments)}")


# Classe Comment
class Comment:
    def __init__(self, comment_id: str, content: str, author: User):
        self._comment_id = comment_id
        self._content = content
        self._post_date = datetime.now()
        self._likes_count = 0
        self._dislikes_count = 0
        self._author = author
    
    def get_comment_id(self) -> str:
        return self._comment_id
    
    def get_content(self) -> str:
        return self._content
    
    def get_post_date(self) -> datetime:
        return self._post_date
    
    def get_likes_count(self) -> int:
        return self._likes_count
    
    def add_like(self):
        self._likes_count += 1
    
    def add_dislike(self):
        self._dislikes_count += 1
    
    def display_comment(self):
        print(f"\nComment by {self._author.get_username()}: {self._content}")
        print(f"Likes: {self._likes_count}, Dislikes: {self._dislikes_count}")


# Classe Analyzer
class Analyzer:
    def __init__(self, analyzer_id: str, analysis_method: str):
        self._analyzer_id = analyzer_id
        self._analysis_method = analysis_method
        self._sentiment_scores = {}
    
    def analyze_comments(self, comments: List) -> 'AnalysisResult':
        # Analyse chaque commentaire pour déterminer le sentiment
        for comment in comments:
            sentiment = self.get_sentiment_score(comment)
            self._sentiment_scores[comment.get_comment_id()] = sentiment
        
        # Calcule le score global
        quality_score = self.calculate_score(comments)
        
        # Crée et retourne le résultat
        result = AnalysisResult(quality_score, len(comments))
        return result
    
    def get_sentiment_score(self, comment) -> float:
        # Analyse simple basée sur les mots positifs et négatifs
        content = comment.get_content().lower()
        positive_words = ['good', 'great', 'excellent', 'amazing', 'love', 'best', 'awesome']
        negative_words = ['bad', 'terrible', 'awful', 'hate', 'worst', 'poor', 'disappointing']
        
        positive_count = sum(1 for word in positive_words if word in content)
        negative_count = sum(1 for word in negative_words if word in content)
        
        # Score entre -1 et 1
        if positive_count + negative_count == 0:
            return 0.0
        return (positive_count - negative_count) / (positive_count + negative_count)
    
    def calculate_score(self, comments: List) -> float:
        # Calcule la moyenne des sentiments et convertit en score sur 10
        if not comments:
            return 5.0
        
        total_sentiment = sum(self._sentiment_scores.values())
        avg_sentiment = total_sentiment / len(comments)
        
        # Convertit de [-1, 1] vers [0, 10]
        score = (avg_sentiment + 1) * 5
        return round(score, 2)
    
    def generate_report(self, video) -> str:
        comments = video.get_comments()
        result = self.analyze_comments(comments)
        return f"Analysis Report for '{video.get_title()}': Score {result.get_quality_score()}/10"


# Classe AnalysisResult
class AnalysisResult:
    def __init__(self, quality_score: float, total_comments: int):
        self._result_id = f"result_{datetime.now().timestamp()}"
        self._quality_score = quality_score
        self._total_comments_analyzed = total_comments
        self._analysis_date = datetime.now()
        self._recommendation = self._generate_recommendation()
    
    def get_quality_score(self) -> float:
        return self._quality_score
    
    def get_recommendation(self) -> str:
        return self._recommendation
    
    def _generate_recommendation(self) -> str:
        # Génère une recommandation basée sur le score
        if self._quality_score >= 8.0:
            return "Highly recommended! This video has excellent reviews."
        elif self._quality_score >= 6.0:
            return "Recommended. This video has good reviews overall."
        elif self._quality_score >= 4.0:
            return "Mixed reviews. Watch at your own discretion."
        else:
            return "Not recommended. This video has poor reviews."
    
    def display_result(self):
        print(f"\n{'='*50}")
        print(f"ANALYSIS RESULT")
        print(f"{'='*50}")
        print(f"Quality Score: {self._quality_score}/10")
        print(f"Total Comments Analyzed: {self._total_comments_analyzed}")
        print(f"Recommendation: {self._recommendation}")
        print(f"Analysis Date: {self._analysis_date.strftime('%Y-%m-%d %H:%M:%S')}")
        print(f"{'='*50}\n")
    
    def save_result(self):
        # Sauvegarde le résultat dans un fichier
        with open(f"{self._result_id}.txt", "w") as f:
            f.write(f"Quality Score: {self._quality_score}/10\n")
            f.write(f"Recommendation: {self._recommendation}\n")
            f.write(f"Total Comments: {self._total_comments_analyzed}\n")
        print(f"Result saved to {self._result_id}.txt")


if __name__ == '__main__':
    # Petit scénario de démonstration pour rendre le script exécutable
    creator = VideoCreator('u1', 'alice', 'alice@example.com')
    viewer = Viewer('u2', 'bob', 'bob@example.com')

    video = Video('v1', 'Demo Video', 'http://example.com/v1', 120, 'Education')
    creator.upload_video(video)

    # Ajouter quelques commentaires
    c1 = Comment('c1', 'This is great, I love it!', viewer)
    c2 = Comment('c2', 'Not bad but could be better', viewer)
    c3 = Comment('c3', 'Terrible content, worst ever', viewer)

    video.add_comment(c1)
    video.add_comment(c2)
    video.add_comment(c3)

    # Exécuter l'analyse
    analyzer = Analyzer('a1', 'simple')
    report = analyzer.generate_report(video)
    print(report)

    # Afficher le détail du résultat
    result = analyzer.analyze_comments(video.get_comments())
    result.display_result()

    # Importer toutes les classes (supposées dans le même fichier ou module)
# from youtube_classes import *

# Stockage des instances
users = {}
video_creators = {}
viewers = {}
videos = {}
comments = {}
analyzers = {}
results = {}

def display_menu():
    """Affiche le menu principal"""
    print("\n" + "="*60)
    print("YOUTUBE VIDEO ANALYZER SYSTEM")
    print("="*60)
    print("1. Create User/Creator/Viewer")
    print("2. Create Video")
    print("3. Create Comment")
    print("4. Analyze Video")
    print("5. List All Users")
    print("6. List All Videos")
    print("7. List All Comments")
    print("8. Search User by ID")
    print("9. Search Video by ID")
    print("10. Delete User")
    print("11. Delete Video")
    print("12. Display Video Details")
    print("0. Exit")
    print("="*60)

def create_user():
    """Créer un nouvel utilisateur, créateur ou spectateur"""
    print("\nSelect user type:")
    print("1. Regular User")
    print("2. Video Creator")
    print("3. Viewer")
    choice = input("Choice: ")
    
    user_id = input("Enter User ID: ")
    username = input("Enter Username: ")
    email = input("Enter Email: ")
    
    if choice == "1":
        user = User(user_id, username, email)
        users[user_id] = user
        print(f"\nUser {username} created successfully!")
    elif choice == "2":
        creator = VideoCreator(user_id, username, email)
        video_creators[user_id] = creator
        users[user_id] = creator  # Aussi dans users car c'est un User
        print(f"\nVideo Creator {username} created successfully!")
    elif choice == "3":
        viewer = Viewer(user_id, username, email)
        viewers[user_id] = viewer
        users[user_id] = viewer  # Aussi dans users car c'est un User
        print(f"\nViewer {username} created successfully!")
    else:
        print("Invalid choice!")

def create_video():
    """Créer une nouvelle vidéo"""
    if not video_creators:
        print("\nNo video creators available. Create a creator first!")
        return
    
    print("\nAvailable creators:")
    for creator_id, creator in video_creators.items():
        print(f"  {creator_id}: {creator.get_username()}")
    
    creator_id = input("\nEnter Creator ID: ")
    if creator_id not in video_creators:
        print("Creator not found!")
        return
    
    video_id = input("Enter Video ID: ")
    title = input("Enter Title: ")
    url = input("Enter URL: ")
    duration = int(input("Enter Duration (seconds): "))
    category = input("Enter Category: ")
    
    video = Video(video_id, title, url, duration, category)
    videos[video_id] = video
    
    # Associer la vidéo au créateur
    video_creators[creator_id].upload_video(video)
    print(f"\nVideo '{title}' created successfully!")

def create_comment():
    """Créer un nouveau commentaire sur une vidéo"""
    if not videos:
        print("\nNo videos available. Create a video first!")
        return
    if not users:
        print("\nNo users available. Create a user first!")
        return
    
    print("\nAvailable videos:")
    for vid, video in videos.items():
        print(f"  {vid}: {video.get_title()}")
    
    video_id = input("\nEnter Video ID: ")
    if video_id not in videos:
        print("Video not found!")
        return
    
    print("\nAvailable users:")
    for uid, user in users.items():
        print(f"  {uid}: {user.get_username()}")
    
    user_id = input("\nEnter User ID (author): ")
    if user_id not in users:
        print("User not found!")
        return
    
    comment_id = input("Enter Comment ID: ")
    content = input("Enter Comment Content: ")
    
    comment = Comment(comment_id, content, users[user_id])
    comments[comment_id] = comment
    
    # Ajouter le commentaire à la vidéo
    videos[video_id].add_comment(comment)
    print(f"\nComment added successfully!")

def analyze_video():
    """Analyser une vidéo et ses commentaires"""
    if not videos:
        print("\nNo videos available!")
        return
    
    print("\nAvailable videos:")
    for vid, video in videos.items():
        print(f"  {vid}: {video.get_title()} ({len(video.get_comments())} comments)")
    
    video_id = input("\nEnter Video ID to analyze: ")
    if video_id not in videos:
        print("Video not found!")
        return
    
    video = videos[video_id]
    if not video.get_comments():
        print("\nThis video has no comments to analyze!")
        return
    
    # Créer un analyseur
    analyzer = Analyzer("analyzer_1", "sentiment_analysis")
    
    # Analyser les commentaires
    print(f"\nAnalyzing {len(video.get_comments())} comments...")
    result = analyzer.analyze_comments(video.get_comments())
    
    # Sauvegarder le résultat
    results[video_id] = result
    
    # Afficher le résultat
    result.display_result()

def list_users():
    """Lister tous les utilisateurs"""
    if not users:
        print("\nNo users in the system.")
        return
    
    print("\n" + "="*60)
    print("ALL USERS")
    print("="*60)
    for user_id, user in users.items():
        user.display_info()
        print("-" * 60)

def list_videos():
    """Lister toutes les vidéos"""
    if not videos:
        print("\nNo videos in the system.")
        return
    
    print("\n" + "="*60)
    print("ALL VIDEOS")
    print("="*60)
    for video_id, video in videos.items():
        video.display_info()
        print("-" * 60)

def list_comments():
    """Lister tous les commentaires"""
    if not comments:
        print("\nNo comments in the system.")
        return
    
    print("\n" + "="*60)
    print("ALL COMMENTS")
    print("="*60)
    for comment_id, comment in comments.items():
        comment.display_comment()
        print("-" * 60)

def search_user():
    """Rechercher un utilisateur par ID"""
    user_id = input("\nEnter User ID to search: ")
    if user_id in users:
        print("\nUser found:")
        users[user_id].display_info()
    else:
        print("User not found!")

def search_video():
    """Rechercher une vidéo par ID"""
    video_id = input("\nEnter Video ID to search: ")
    if video_id in videos:
        print("\nVideo found:")
        videos[video_id].display_info()
    else:
        print("Video not found!")

def delete_user():
    """Supprimer un utilisateur"""
    user_id = input("\nEnter User ID to delete: ")
    if user_id in users:
        username = users[user_id].get_username()
        del users[user_id]
        # Supprimer aussi des autres dictionnaires si applicable
        if user_id in video_creators:
            del video_creators[user_id]
        if user_id in viewers:
            del viewers[user_id]
        print(f"User {username} deleted successfully!")
    else:
        print("User not found!")

def delete_video():
    """Supprimer une vidéo"""
    video_id = input("\nEnter Video ID to delete: ")
    if video_id in videos:
        title = videos[video_id].get_title()
        del videos[video_id]
        print(f"Video '{title}' deleted successfully!")
    else:
        print("Video not found!")

def display_video_details():
    """Afficher les détails complets d'une vidéo avec ses commentaires"""
    video_id = input("\nEnter Video ID: ")
    if video_id not in videos:
        print("Video not found!")
        return
    
    video = videos[video_id]
    video.display_info()
    
    print("\nComments on this video:")
    for comment in video.get_comments():
        comment.display_comment()
    
    # Afficher le résultat d'analyse s'il existe
    if video_id in results:
        print("\nAnalysis Result:")
        results[video_id].display_result()

def main():
    """Fonction principale avec boucle de menu"""
    print("Welcome to YouTube Video Analyzer System!")
    
    while True:
        display_menu()
        choice = input("\nEnter your choice: ")
        
        if choice == "1":
            create_user()
        elif choice == "2":
            create_video()
        elif choice == "3":
            create_comment()
        elif choice == "4":
            analyze_video()
        elif choice == "5":
            list_users()
        elif choice == "6":
            list_videos()
        elif choice == "7":
            list_comments()
        elif choice == "8":
            search_user()
        elif choice == "9":
            search_video()
        elif choice == "10":
            delete_user()
        elif choice == "11":
            delete_video()
        elif choice == "12":
            display_video_details()
        elif choice == "0":
            print("\nThank you for using YouTube Video Analyzer System!")
            print("Goodbye!")
            break
        else:
            print("\nInvalid choice! Please try again.")
        
        input("\nPress Enter to continue...")

# Point d'entrée du programme
if __name__ == "__main__":
    main()