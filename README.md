# Disclaimer

This is just the code of the Java application that I created to automate Salesmate sales call bulk downloading, not the full application. The full application and demo video are not being uploaded for privacy security purposes. 

# Salesmate Sales Call Bulk Download Automation

This project is created to automate rather boring and time-consuming recurring task of downloading sales calls from Salesmate in bulk. The reason that this project was written in Java is to practice Java for a programming subject I'm taking in school.<br />

1. Salesmate is the main class of the application where the main method is stored.<br />
2. SalesmateAutomation is the class that handles the automated screening part of the application.<br />
3. Controller is the class that handles events & triggers of the application GUI.<br />
4. Download is the class that handles the downloading and file handling part of the application.<br />
5. The FXML file stores the code of the application GUI.

# How does it work

<img width="598" alt="Screenshot 2023-02-01 at 10 21 28 PM" src="https://user-images.githubusercontent.com/92936025/216068652-048b5e0e-3d4c-41e5-a82d-e3d6750b9cdd.png">
1. Once you've keyed in all the neccessary info, a browser window will pop up.<br />
2. You'll be logged in to Salesmate automatically based on the credentials you provided.<br />
3. You'll be logged in to Salesmate automatically based on the credentials you provided.<br />
4. The script will go through the list of the calls and save the call logs that meet the requirements set.<br />
5. Once the script has reached the end of the call logs, the qualified sales calls will be downloaded and saved to the selected folder with this naming convention: DATE-DURATION-NAME-SALES PERSON'S NAME

