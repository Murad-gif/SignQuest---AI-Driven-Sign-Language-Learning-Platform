import base64

import keras
import mediapipe as mp
import numpy as np

mp_hands = mp.solutions.hands
hands = mp_hands.Hands(static_image_mode=True, max_num_hands=2, min_detection_confidence=0.3)


def predict_mpg(img):
    newModel = keras.models.load_model("signRecognitionTest4.h5")

    word2 = ""
    mp_hands = mp.solutions.hands
    mp_drawing = mp.solutions.drawing_utils
    mp_drawing_styles = mp.solutions.drawing_styles

    hands = mp_hands.Hands(static_image_mode=True, min_detection_confidence=0.3)

    data_aux = []
    x_ = []
    y_ = []
    z_ = []

    results = hands.process(img)
    if results.multi_hand_landmarks:

        for hand_landmarks in results.multi_hand_landmarks:
            for i in range(len(hand_landmarks.landmark)):
                x = hand_landmarks.landmark[i].x
                y = hand_landmarks.landmark[i].y
                z = hand_landmarks.landmark[i].z

                x_.append(x)
                y_.append(y)
                z_.append(z)

            for i in range(len(hand_landmarks.landmark)):
                x = hand_landmarks.landmark[i].x
                y = hand_landmarks.landmark[i].y
                z = hand_landmarks.landmark[i].z

                data_aux.append(x - min(x_))
                data_aux.append(y - min(y_))
                data_aux.append(z - min(z_))
        data_aux = data_aux / np.max(data_aux)
        numLandmarks = 21
        dataRehsaped = data_aux.reshape(-1, numLandmarks, 3)
        prediction = newModel.predict(dataRehsaped)
        predictedClass = np.argmax(prediction)

        classNames = ['1', '2', '3', '4', 'A', 'B', 'C', 'D']

        predictedCLassName = classNames[predictedClass]
        word2 = predictedCLassName
        print(f'Predicted Class: {word2}')
    return word2


# print(predict_mpg("trainigDataCnn/B/5.jpg"))



import random
import json
import torch
from chatBotModel import NeuralNet
from chatbotUtils import wordtoken, breakWords, words_list


device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')

with open('trainTestDataChatbot.json', 'r') as json_data:
    datas = json.load(json_data)

FILE = "data3.pth"
data = torch.load(FILE)

input_size = data["input_size"]
hidden_size = data["hidden_size"]
output_size = data["output_size"]
all_words = data['all_words']
tags = data['tags']
model_state = data["model_state"]

model = NeuralNet(input_size, hidden_size, output_size).to(device)
model.load_state_dict(model_state)
model.eval()


#
#
def getResponse(sentence):
    sentence = wordtoken(sentence)
    X = words_list(sentence, all_words)
    X = X.reshape(1, X.shape[0])
    X = torch.from_numpy(X).to(device)

    output = model(X)
    _, predicted = torch.max(output, dim=1)

    tag = tags[predicted.item()]

    probs = torch.softmax(output, dim=1)
    prob = probs[0][predicted.item()]
    if prob.item() > 0.75:
        for intent in datas['data']:
            if tag == intent["word"]:
                return random.choice(intent['responses'])

    return "I dont Understand"


def encodeImage(imagepath):
    with open(imagepath, "rb") as image_file:
        encodedImages = base64.b64encode(image_file.read()).decode("utf-8")
    return encodedImages


import requests

api_url = "http://localhost:8080/getCoursesSuggestion"


def getCourseSuggestions(user):
    response = requests.get(f"{api_url}/{user}")

    if response.status_code == 200:
        course_suggestions = response.json()
        print(course_suggestions)
        return course_suggestions
    else:
        print(f"Error: {response.status_code}")
        return "error in this api call"
