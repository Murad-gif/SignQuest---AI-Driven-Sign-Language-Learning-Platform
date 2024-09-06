import pickle

from sklearn.preprocessing import LabelEncoder

from sklearn.model_selection import train_test_split
import numpy as np
import mediapipe as mp
import cv2
import matplotlib.pyplot as plt

from tensorflow.keras import datasets, layers, models

dataFile = 'fulltrainingdata.pickle'
with open(dataFile, 'rb') as f:
    dataDictionary = pickle.load(f)

data = np.array(dataDictionary['data'])
labels = np.array(dataDictionary['labels'])

data = data / np.max(data)

numLandmarks = 21
dataReshaped = data.reshape(-1, numLandmarks, 3)

x_train, x_test, y_train, y_test = train_test_split(
    dataReshaped, labels, test_size=0.2, random_state=42)

model = models.Sequential()
model.add(layers.Conv1D(32, kernel_size=3, activation='relu', input_shape=(numLandmarks, 3)))
model.add(layers.MaxPooling1D(pool_size=2))
model.add(layers.Flatten())
model.add(layers.Dense(64, activation='relu'))
model.add(layers.Dense(8, activation='softmax'))
label_encoder = LabelEncoder()
yTrainEncoded = label_encoder.fit_transform(y_train)
yTestEncoded = label_encoder.transform(y_test)
model.compile(optimizer='adam',
              loss='sparse_categorical_crossentropy',
              metrics=['accuracy'])

history = model.fit(x_train, yTrainEncoded, epochs=10, validation_data=(x_test, yTestEncoded))
yTrainEncoded = label_encoder.fit_transform(y_train)
yTestEncoded = label_encoder.transform(y_test)
testLoss, testAccuracy = model.evaluate(x_test, yTestEncoded)
print(f'Test accuracy: {testAccuracy}')

model.save("signRecognitionTest4.h5")

import keras


def predict_mpg(img):  #duplicate method, main method for classification is in main class
    new_model = keras.models.load_model("signRecognitionLatest.h5")

    image2 = cv2.imread(img)

    word2 = ""
    mp_hands = mp.solutions.hands
    mp_drawing = mp.solutions.drawing_utils
    mp_drawing_styles = mp.solutions.drawing_styles

    hands = mp_hands.Hands(static_image_mode=True, min_detection_confidence=0.3)

    data_aux = []
    x_ = []
    y_ = []
    z_ = []

    results = hands.process(image2)
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
        dataReshaped = data_aux.reshape(-1, numLandmarks, 3)
        prediction = new_model.predict(dataReshaped)
        predictedClass = np.argmax(prediction)

        print(f'Predicted Class: {predictedClass}')

        class_names = ['1', '2', '3', '4', 'A', 'B', 'C', 'D']

        predictedClassName = class_names[predictedClass]
        word2 = predictedClassName
        print(f'Predicted Class ddd: {word2}')
    return word2

# print(predict_mpg("testC/C/0.jpg"))
