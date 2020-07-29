import flask
from news import get_news, DOMAINS
from flask import request
import os
import json
app = flask.Flask(__name__)



@app.route('/scrape', methods=['GET'])
def scrape():
    startTime = str(request.args.get('startTime'))
    endTime = str(request.args.get('endTime'))
    num_articles = int(request.args.get('numArticles'))
    return get_news(startTime, endTime, num_articles)

@app.route('/sources', methods=['GET'])
def get_sources():
    return DOMAINS

if __name__ == '__main__':
    print(os.environ.get('PORT', 8080))
    app.run(debug = True, host='0.0.0.0', port=int(os.environ.get('PORT', 8080)))
