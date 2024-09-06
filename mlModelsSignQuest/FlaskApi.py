import base64
import io
import logging

import base64
import numpy as np

from PIL import Image
from flask import Flask, request, redirect, jsonify
from flask_cors import CORS

from flask import Flask, flash, request, redirect, url_for
import main

app = Flask(__name__)
CORS(app)


@app.route('/classifySign', methods=['POST'])
def process_image2():
    logging.basicConfig(filename='app.log', level=logging.DEBUG)

    if 'image' in request.form:
        data_url = request.form['image']
        data_url = data_url.replace('data:image/jpeg;base64,', '')
        decoded_image = base64.b64decode(data_url)

        image = Image.open(io.BytesIO(decoded_image))
        image_array = np.array(image)

        predict = main.predict_mpg(image_array)

        return predict

    return jsonify('No image data found in the request.')


@app.route("/chatBotApi", methods=["POST"])
def chatBotApiFunction():
    msg = request.get_json().get("message")
    user = request.get_json().get("user")
    print(user)
    response = main.getResponse(msg)
    print(response)
    if response.endswith("jpg"):
        response = main.encodeImage(response)
        message = {"answer": response}
        return jsonify(message)

    elif response.endswith("makeSuggestionApiCall"):
        courseSugestions = main.getCourseSuggestions(user)
        responseText = ', '.join(courseSugestions)

        if responseText:
            responseText = f"You should study the following courses as you haven't passed them yet: {responseText}"
        else:
            responseText = "You haven't passed any courses yet. you should probably start from the beginning"

        message = {"answer": responseText}
        print(message)
        return jsonify(message)
    else:
        message = {"answer": response}

        print(response)
        return jsonify(message)


if __name__ == "__main__":
    app.run(host='134.83.226.33')  # change to hosts IP Address
    app.debug = True
    # app.run()
