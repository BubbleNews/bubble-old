import flask
from news import get_news

app = flask.Flask(__name__)
app.config["DEBUG"] = True


@app.route('/scrape', methods=['GET'])
def home():
    try:
        start_date = str(request.args.get('startDate'))
        end_date = str(request.args.get('endDate'))
        num_articles = int(request.args.get('numArticles'))
        get_news(start_date, end_date, num_articles)
    except Exception as e:
        return e

app.run()
