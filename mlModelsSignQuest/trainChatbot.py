import json

from matplotlib import pyplot as plt
from scipy.optimize._trustregion_constr import report
from sklearn.metrics import accuracy_score, f1_score, classification_report

from chatbotUtils import wordtoken, breakWords, words_list
import numpy as np
import random
import json
import numpy as np
import torch
import torch.nn as nn
from chatBotModel import NeuralNet
from torch.utils.data import Dataset, DataLoader

with open('trainTestDataChatbot.json', 'r') as f:
    data = json.load(f)


allWords = []
word = []

xy = []
for datas in data['data']:
    wordLists = datas['word']
    word.append(wordLists)
    for phrase in datas['phrases']:
        w = wordtoken(phrase)
        allWords.extend(w)
        xy.append((w, wordLists))

ignore_words = ['?', '.', '!']

allWords = [breakWords(w) for w in allWords if w not in ignore_words]
allWords = sorted(set(allWords))
word = sorted(set(word))

X_train = []
y_train = []
for (pattern_sentence, tag) in xy:
    bag = words_list(pattern_sentence, allWords)
    X_train.append(bag)
    label = word.index(tag)
    y_train.append(label)

X_train = np.array(X_train)
y_train = np.array(y_train)

num_epochs = 1000
batch_size = 8
learning_rate = 0.001
input_size = len(X_train[0])
hidden_size = 8
output_size = len(word)


class createChatData(Dataset):
    def __init__(self):
        self.n_samples = len(X_train)
        self.x_data = X_train
        self.y_data = y_train

    def __getitem__(self, index):
        return self.x_data[index], self.y_data[index]

    def __len__(self):
        return self.n_samples


dataset = createChatData()
train_loader = DataLoader(dataset=dataset, batch_size=8, shuffle=True, num_workers=0)
device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')

model = NeuralNet(input_size, hidden_size, output_size).to(device)

criterion = nn.CrossEntropyLoss()
optimizer = torch.optim.Adam(model.parameters(), lr=learning_rate)

for epoch in range(num_epochs):
    for (words, labels) in train_loader:
        words = words.to(device)
        labels = labels.to(dtype=torch.long).to(device)

        outputs = model(words)
        loss = criterion(outputs, labels)

        optimizer.zero_grad()
        loss.backward()
        optimizer.step()

    if (epoch + 1) % 100 == 0:
        print(f'Epoch [{epoch + 1}/{num_epochs}], Loss: {loss.item():.4f}')

print(f'final loss: {loss.item():.4f}')

data = {
    "model_state": model.state_dict(),
    "input_size": input_size,
    "hidden_size": hidden_size,
    "output_size": output_size,
    "all_words": allWords,
    "tags": word
}


FILE = "data3.pth"
torch.save(data, FILE)

model.eval()

true_labels = []
predicted_labels = []
test_loader = DataLoader(dataset=dataset, batch_size=8, shuffle=True, num_workers=0)

for words, labels in train_loader:
    words = words.to(device)
    labels = labels.to(device)

    # Forward pass the data
    outputs = model(words)

    # get predicted labels
    _, predicted = torch.max(outputs, 1)


    true_labels.extend(labels.cpu().numpy())
    predicted_labels.extend(predicted.cpu().numpy())

accuracy = accuracy_score(true_labels, predicted_labels)

f1 = f1_score(true_labels, predicted_labels, average='macro')

print(f'Accuracy: {accuracy}')
print(f'F1 Score: {f1}')

