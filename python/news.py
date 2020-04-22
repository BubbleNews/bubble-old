from newsapi import NewsApiClient
from newspaper import Article
from newspaper.article import ArticleException
from datetime import datetime, timedelta
import json

# key for accessing google news api
API_KEY = '77d7cb4756d44e13aea4a50d033d27e3'
MINIMUM_ARTICLE_CHAR_LENGTH = 700
BAD_CHARS = ['\n', '\r\n']

def get_news():
    print("Connecting to Google News API...")
    # create news api connection
    newsapi = NewsApiClient(api_key=API_KEY)

    # get current time
    # today = datetime.now()
    # current_time = datetime(today.year, today.month, today.day,
    #                         today.hour, today.minute, today.minute)
    # hour_ago = current_time + timedelta(hours=-1)
    #
    # current_time = str(current_time).replace(' ', 'T')
    # hour_ago = str(hour_ago).replace(' ', 'T')
    #
    # print(current_time)
    # print(hour_ago)

    #
    num_articles = 1
    # get top headlines
    top_headlines = newsapi.get_top_headlines(language='en',
                                            page_size=num_articles)

    articles = top_headlines['articles']
    # loop through articles and scrape article text with scraper
    for i, article in enumerate(articles):
        print(i)
        url = article['url']
        scraped_text = scrape_text(url)
        # threshold
        if len(scraped_text) < MINIMUM_ARTICLE_CHAR_LENGTH:
            # text is too short so delete article
            del articles[i]
            with open('deleted_articles.txt', 'w') as f:
                f.write(url)
        else:
            # we want text
            article['content'] = clean_text(scraped_text, BAD_CHARS)

    return json.dumps(articles)

def scrape_text(url):
    try:
        article = Article(url)
        article.download()
        article.parse()
        return article.text
    except ArticleException:
        print("couldn't parse: " + url)

def clean_text(text, bad_chars):
    for char in bad_chars:
        text = text.replace(char, '')
    return text
