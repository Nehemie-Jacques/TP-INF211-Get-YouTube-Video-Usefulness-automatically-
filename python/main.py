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