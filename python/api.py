import flask
from news import get_news, DOMAINS
from flask import request
import json
app = flask.Flask(__name__)
app.config["DEBUG"] = True


@app.route('/scrape', methods=['GET'])
def scrape():
    startTime = str(request.args.get('startTime'))
    endTime = str(request.args.get('endTime'))
    num_articles = int(request.args.get('numArticles'))
    return get_news(startTime, endTime, num_articles)

@app.route('/sources', methods=['GET'])
def get_sources():
    return DOMAINS


app.run()
