# This files contains your custom actions which can be used to run
# custom Python code.
#
# See this guide on how to implement these action:
# https://rasa.com/docs/rasa/custom-actions


# This is a simple example for a custom action which utters "Hello World!"

# from typing import Any, Text, Dict, List
#
# from rasa_sdk import Action, Tracker
# from rasa_sdk.executor import CollectingDispatcher
#
#
# class ActionHelloWorld(Action):
#
#     def name(self) -> Text:
#         return "action_hello_world"
#
#     def run(self, dispatcher: CollectingDispatcher,
#             tracker: Tracker,
#             domain: Dict[Text, Any]) -> List[Dict[Text, Any]]:
#
#         dispatcher.utter_message(text="Hello World!")
#
#         return []
# actions.py
# -----------------------------------------------------------------------------------------------------------------
from typing import Any, Text, Dict, List
from rasa_sdk import Action, Tracker
from rasa_sdk.executor import CollectingDispatcher
import spacy
from datetime import datetime
from datetime import date



# Here I'm getting 2 responses from the rasa..
"""
class ActionSearchFile(Action):
    def name(self) -> Text:
        return "action_search_file"

    def run(self, dispatcher: CollectingDispatcher,
            tracker: Tracker,
            domain: Dict[Text, Any]) -> List[Dict[Text, Any]]:
           
        keywords = list(tracker.get_latest_entity_values("keyword"))
        if keywords:
            for keyword in keywords:
                # Perform your file search or other actions using the extracted keyword
                dispatcher.utter_message(text=f"Your {keyword} file is searching..")
        else:
            dispatcher.utter_message(text="I couldn't find any keyword in your message.")
        return []

# -----------------------------------------------------------------------------------------------------------------
class ActionGetCityInfo(Action):
    def name(self) -> Text:
        return "action_get_city_info"

    def run(self, dispatcher: CollectingDispatcher, tracker: Tracker, domain: Dict[Text, Any]) -> List[Dict[Text, Any]]:
        city_name = tracker.get_slot("city_name")
        dispatcher.utter_message(f"Here's some information about {city_name}.")
        return []

# -----------------------------------------------------------------------------------------------------------------
class ActionPrintEntities(Action):

    def name(self) -> Text:
        return "action_print_entities"

    def run(self, dispatcher: CollectingDispatcher,
            tracker: Tracker,
            domain: Dict[Text, Any]) -> List[Dict[Text, Any]]:
       
        keywords = list(tracker.get_latest_entity_values("file_name"))
        if keywords:
            for keyword in keywords:
                # Perform your file search or other actions using the extracted keyword
                dispatcher.utter_message(text=f"Your {keyword} file is searching..*****")
        else:
            dispatcher.utter_message(text="I couldn't find any file_name in your message.*****")
        #entities = tracker.latest_message.get('entities', [])
        #for entity in entities:
          #  entity_name = entity['entity']
            #entity_value = entity['value']
            #dispatcher.utter_message(f"Entity '{entity_name}' extracted with value '{entity_value}'")

        return []

# -----------------------------------------------------------------------------------------------------------------
class ActionCountryName(Action):
    def name(self) -> Text:
        return "action_country_name_action"

    def run(self, dispatcher: CollectingDispatcher,
            tracker: Tracker,
            domain: Dict[Text, Any]) -> List[Dict[Text, Any]]:
       
        c_name = tracker.get_slot("c_name")
        if c_name:
            dispatcher.utter_message(f"Your country name is {c_name}.")
        else:
            dispatcher.utter_message("I couldn't extract the country name from your message.")

        return []

# -----------------------------------------------------------------------------------------------------------------
class ActionExtractCountry(Action):
    def name(self) -> Text:
        return "action_extract_country"

    def run(self, dispatcher: CollectingDispatcher, tracker: Tracker, domain: Dict[Text, Any]) -> List[Dict[Text, Any]]:
        country = tracker.get_slot("country")
        dispatcher.utter_message(f"The extracted country name is {country}.")
        return []

# -----------------------------------------------------------------------------------------------------------------
class ProcessCustomData(Action):
    def name(self) -> Text:
        return "action_process_custom_data"

    def run(self, dispatcher: CollectingDispatcher, tracker: Tracker, domain: Dict[Text, Any]) -> List[Dict[Text, Any]]:
        # Extract the user's message as the data to be analyzed
        user_data = tracker.latest_message.get("text")

        # Perform analysis on the user data (example: count the number of words)
        data_analysis = len(user_data.split())

        # Provide a response with the analysis result
        dispatcher.utter_message(text=f"The data you provided contains {data_analysis} words.")

        return []

#------------------------------------------------------------------------------------------------------------------------------
class SearchFileAction(Action):
    def name(self) -> Text:
        return "action_search"

    def run(self, dispatcher: CollectingDispatcher,
            tracker: Tracker,
            domain: Dict[Text, Any]) -> List[Dict[Text, Any]]:

        entities = tracker.latest_message.get('entities', [])
        if entities:
            file_name = entities[0].get('value')
            # Perform your file search logic here based on the extracted file name
            dispatcher.utter_message(f"Searching for file: {file_name}")
        else:
            dispatcher.utter_message("No file name found in the message.")

        return []

#-----------------------------------------------
class SearchFileContentAction(Action):

   def name(self) -> Text:
       return "action_search_file_content"

   def run(self, dispatcher: CollectingDispatcher,
           tracker: Tracker,
           domain: Dict[Text, Any]) -> List[Dict[Text, Any]]:

       # Extract the search keywords from the user message
       entities = tracker.latest_message.get('entities', [])
       search_keywords = [entity['value'] for entity in entities if entity['entity'] == 'search_keyword']

       # Perform file content search based on the extracted keywords (pseudo-code)

       # Provide a response
       if search_keywords:
           dispatcher.utter_message(f"Searching for files with keywords: {', '.join(search_keywords)}")
       else:
           dispatcher.utter_message("No search keyword provided.")

       return []

"""
#------------------------------------without called word---------------------------------------------------------------------------------------------

class ExtractMainPhrasesAction(Action):

    def name(self) -> Text:
        return "action_country_search"

    def run(self, dispatcher: CollectingDispatcher,
            tracker: Tracker,
            domain: Dict[Text, Any]) -> List[Dict[Text, Any]]:

        nlp = spacy.load("en_core_web_sm")
        user_message = tracker.latest_message.get("text", "")
        doc = nlp(user_message.lower())
        main_phrases = []
        for token in doc:
            if token.pos_ in ["NOUN", "PROPN"]:
                main_phrases.append(token.text)

        if main_phrases:
            #response = f" Random Searching with phrases : {', '.join(main_phrases)}"
            response = f" Sorry!, I can't get you"
        else:
            #response = "No main phrases found in your message."
            response = "I'm sorry, I didn't understand."

        dispatcher.utter_message(text=response)
        return []


#--------------------------------with called word-----------------------------------------
class ExtractMainPhrasesAction2(Action):

    def name(self) -> Text:
        return "action_explore_file"

    def run(self, dispatcher: CollectingDispatcher,
            tracker: Tracker,
            domain: Dict[Text, Any]) -> List[Dict[Text, Any]]:

        nlp = spacy.load("en_core_web_sm")
        user_message = tracker.latest_message.get("text", "")
        doc = nlp(user_message.lower())
        main_phrases = []
        flag = False
        for token in doc:
            if token.text == "called" or token.text == " named " or token.text == "file named" or token.text == "keyword" :
                flag = True
            elif flag and token.pos_ in ["NOUN", "PROPN", "ADJ"]:
                main_phrases.append(token.text)
            else:
                flag = False

        if main_phrases:
            response = f" Searching : {', '.join(main_phrases)}"
        else:
            #response = "No main phrases found. Please reframe and ask again."
            response = "I'm sorry, I didn't understand."


        dispatcher.utter_message(text=response)
        return []


#----------------------------------while enquiring user-------------------------------search with word------

class ExtractMainPhrasesAction3(Action):

    def name(self) -> Text:
        return "action_keyword"

    def run(self, dispatcher: CollectingDispatcher,
            tracker: Tracker,
            domain: Dict[Text, Any]) -> List[Dict[Text, Any]]:

        nlp = spacy.load("en_core_web_sm")
        user_message = tracker.latest_message.get("text", "")
        doc = nlp(user_message.lower())
        main_phrases = []
        for token in doc:
            if token.pos_ in ["NOUN", "PROPN", "ADJ"]:
                main_phrases.append(token.text)

        #main_phrases=main_phrases[1:]
        if "content" in main_phrases:
            main_phrases.remove("content")
        if "phrase" in main_phrases:
            main_phrases.remove("phrase")
        if "keyword" in main_phrases:
            main_phrases.remove("keyword")
        if "name" in main_phrases:
            main_phrases.remove("name")
        if main_phrases:
            response = f" Searching : {', '.join(main_phrases)}"
        else:
            response = "Please try again."

        dispatcher.utter_message(text=response)
        return []
#---------------------------------asking the wether------------------------------------
'''class ActionGetWeather(Action):
    def name(self):
        return "action_get_weather"

    def run(self, dispatcher, tracker, domain):
        city = tracker.latest_message.get("text", "")
        # Call weather API to get weather information for the city
        # Replace <API_KEY> with your actual API key
        response = requests.get(f"http://api.openweathermap.org/data/2.5/weather?q={city}&appid=<API_KEY>")
        weather_data = response.json()
        weather_description = weather_data["weather"][0]["description"]
        temperature = weather_data["main"]["temp"]
        dispatcher.utter_message(f"The weather in {city} is {weather_description}. The temperature is {temperature}°C.")
        return []
'''
#------------------------------asking time----------------------------------------------

class ActionGetTime(Action):
    def name(self) -> Text:
        return "action_get_time"

    def run(self, dispatcher: CollectingDispatcher,
            tracker: Tracker,
            domain: Dict[Text, Any]) -> List[Dict[Text, Any]]:

        now = datetime.now()
        current_time = now.strftime("%H:%M:%S")
        dispatcher.utter_message(template="utter_time", time=current_time)
        return []

#--------------------------------asking date-------------------------------------------------

class ActionGetDate(Action):
    def name(self) -> Text:
        return "action_get_date"

    def run(self, dispatcher: CollectingDispatcher,
            tracker: Tracker,
            domain: Dict[Text, Any]) -> List[Dict[Text, Any]]:

        today = date.today()
        current_date = today.strftime("%B %d, %Y")
        dispatcher.utter_message(template="utter_date", date=current_date)
        return []

#-----------------------------default------------------------------------------------
'''class ActionFallback(Action):
    def name(self) -> Text:
        return "action_clarify"

    def run(self, dispatcher: CollectingDispatcher,
            tracker: Tracker,
            domain: Dict[Text, Any]) -> List[Dict[Text, Any]]:
        dispatcher.utter_message(text="I'm sorry, I didn't understand. Could you please provide more details?")
        return []
'''

#--------------------------------fallback----------------------------------------------