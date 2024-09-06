import nltk
import numpy as np
from nltk.stem.porter import PorterStemmer

stemmer = PorterStemmer()

#nltk.download('punkt')


def wordtoken(sentence):
    return nltk.word_tokenize(sentence)


def breakWords(word):
    return stemmer.stem(word.lower())


def words_list(broken_up_sentence, all_words):
    broken_up_sentence=[breakWords(w) for w in broken_up_sentence]
    bag=np.zeros(len(all_words), dtype=np.float32)
    for idx, w in enumerate(all_words):
        if w in broken_up_sentence:
            bag[idx] = 1

    return bag



#a = "testing if it works"
#print(a)

#a = wordtoken(a)
#print(a)



