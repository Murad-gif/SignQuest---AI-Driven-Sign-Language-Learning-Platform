import os

import cv2


DATA_DIR = './trainingpicturesallgestures'
class_names = ['A','B','C','D','1','2','3','4']

if not os.path.exists(DATA_DIR):
    os.makedirs(DATA_DIR)


number_of_classes = len(class_names)
dataset_size = 300

cap = cv2.VideoCapture(0)
for class_name in class_names:
    class_dir = os.path.join(DATA_DIR, class_name)

    if not os.path.exists(class_dir):
        os.makedirs(class_dir)

    print('Collecting data sign of letter = '.format(class_name))

    finished = False
    while True:
        ret, frame = cap.read()
        cv2.putText(frame, 'Press S to start', (100, 50), cv2.FONT_HERSHEY_SIMPLEX, 1.3, (0, 255, 0), 3,
                    cv2.LINE_AA)
        cv2.imshow('frame', frame)
        if cv2.waitKey(25) == ord('s'):
            break

    counter = 0
    while counter < dataset_size:
        ret, frame = cap.read()
        cv2.imshow('frame', frame)
        cv2.waitKey(25)
        img_path = os.path.join(class_dir, '{}.jpg'.format(counter))
        cv2.imwrite(img_path, frame)
        counter += 1

cap.release()
cv2.destroyAllWindows()
