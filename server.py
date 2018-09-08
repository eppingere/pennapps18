import base64
import csv
import datetime
import json
from random import randint

import IPython
import requests
from flask import (Flask, jsonify, redirect, render_template, request,
                   send_from_directory, url_for)
from werkzeug import secure_filename

app = Flask(__name__, template_folder='templates')
app.config.from_object('config')
app.config.from_pyfile('config.py')

@app.route("/updateLedger", methods=['GET', 'POST'])
def updateLedger():
    if request.method == 'POST':
        f = request.files['file']
        f.save(secure_filename(f.filename))
        return json.dump
