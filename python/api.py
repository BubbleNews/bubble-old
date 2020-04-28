import flask
from news import get_news

app = flask.Flask(__name__)
app.config["DEBUG"] = True


@app.route('/scrape', methods=['GET'])
def home():
    start_date = '2020-04-27'
    end_date = '2020-04-28'
    num_articles = 10
    return get_news(start_date, end_date, num_articles)


app.run()
