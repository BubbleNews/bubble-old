import flask
from news import get_news
from flask import request

app = flask.Flask(__name__)
app.config["DEBUG"] = True


@app.route('/scrape', methods=['GET'])
def home():
    startTime = str(request.args.get('startTime'))
    endTime = str(request.args.get('endTime'))
    num_articles = int(request.args.get('numArticles'))
    return get_news(startTime, endTime, num_articles)

app.run()
