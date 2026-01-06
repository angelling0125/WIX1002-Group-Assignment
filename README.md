# Smart Journaling Project 📓

This project requires a Hugging Face access token to enable sentiment analysis.
Please complete the setup steps below before running the program.

---

## Hugging Face Token Setup

### Step 1: Create a Hugging Face Account
Create an account or log in at the [Hugging Face website.](https://huggingface.co)

---

### Step 2: Generate an Access Token
1. Click **Profile icon**
2. Go to **Settings**
3. Select **Access Tokens**
4. Click **Create New Token**
5. Choose **Fine-grained** and give this token any name
6. Check all boxes in the **Inference** section
7. Scroll to the bottom and click **Create token**.
8. Copy the token **(“hf_xxxxxxxxxxxxxxxxx”)**

---
### Step 3: Clone the Repository

Open a terminal and run the following command to clone the project repository:

```bash
git clone https://github.com/angelling0125/WIX1002-Group-Assignment
```

---

### Step 4: Create the `.env` File
Create a file named `.env` in the root project directory and add:

```
BEARER_TOKEN=hf_your_token_here
```

---

### Step 5: Place the `.env` File in the Project Root
Ensure the .env file is placed in the project root directory (same level as src).
Correct project structure:

```bash
WIX1002-Group-Assignment/
├── src/
├── .env
├── .gitignore
└── README.md
```
