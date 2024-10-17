# AI Bot App which Integration with RASA for Content Search

## Overview

This project integrates an Android client with a RASA-based AI bot running on a server. The bot can respond to user queries and facilitate content searches using the information stored in specific directories.

---

## Steps and Guidelines to Make It Work

### Server Setup

1. **Install Python**:
   - Ensure Python 3.7, 3.8, 3.9, or 3.10 is installed. Recommended version: 3.9.13.
   - Download Python from the [official site](https://www.python.org/downloads/).

2. **Install RASA Open Source**:
   - Run the following command to install RASA using pip:
     ```bash
     pip install rasa
     ```
   - Verify the installation:
     ```bash
     rasa --version
     ```

3. **Create a New RASA Project**:
   - Run the following command to initialize a new RASA project:
     ```bash
     rasa init --no-prompt
     ```
   - Navigate to your RASA project directory.

4. **Train the RASA Model**:
   - To train your RASA model, use:
     ```bash
     rasa train
     ```

5. **Test and Run the RASA Assistant**:
   - Run the bot in the terminal using:
     ```bash
     rasa shell
     ```
   - Or, run the server with API support using:
     ```bash
     rasa run --enable-api
     ```

6. **Training Data**:
   - The model can be trained using files located within your project directory such as `nlu.md`, `domain.yml`, `stories.md`, etc.

7. **Install spaCy for NLU**:
   - Install the `spaCy` library using pip:
     ```bash
     pip install spacy
     ```

8. **Install Other Required Packages**:
   - Install any additional dependencies using pip as required by your project.

---

### Client Setup (Android App)

1. **Install the Application**:
   - Download and install the Android application on the device.

2. **Grant Permissions**:
   - Ensure the app has the required permissions to access the device's storage.

3. **Network Security Configuration**:
   - The server system must be configured to allow cleartext traffic using the server's IP address. This is crucial for enabling communication between the Android app and the server over HTTP.

---

### Guidelines

- Both the client (Android app) and server (RASA bot) should be able to communicate over the network.
- The bot can provide services regardless of how much it has been trained.
- All files needed for content search must be placed in a folder named `Rasabot` in your device's internal storage.

---

### Working Process

1. **User Interaction**:
   - The user communicates with the AI bot (RASA) through the Android app for queries.

2. **Content Search**:
   - The bot provides instructions for content search, and the user's request is sent via an HTTP call to the server.
   - On the server side, the bot processes the request and sends the appropriate response back through the same communication path.

---

## Tools Used

### Client-Side

- **Android Studio**
- **Java**
- **ITextpdf Library** (for text extraction)

### Server-Side

- **Python**
- **RASA Framework**
- **spaCy Library** (for NLU)

---

## Contact

For further information, feel free to contact.

