import flask
from news import get_news

app = flask.Flask(__name__)
app.config["DEBUG"] = True

@app.route('/scrape', methods=['GET'])
def home():
    return get_news()

app.run()