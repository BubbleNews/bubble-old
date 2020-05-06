from __future__ import print_function

import json
import re
from newspaper import Article
from newspaper.article import ArticleException
from newsapi import NewsApiClient

# key for accessing google news api
API_KEY = '77d7cb4756d44e13aea4a50d033d27e3'
NEWS_API = NewsApiClient(api_key=API_KEY)
MINIMUM_ARTICLE_CHAR_LENGTH = 650
# note that this is order-dependent ('s must come before ' and ... comes before ..)
BAD_CHARS = ['\r', '\n', '"', '`', '\'s', '\'', '...', '..']
DOMAINS = 'fortune.com,time.com,cnn.com,cbsnews.com,cnbc.com,' \
          'huffingtonpost.com,msnbc.com,nbcnews.com,usatoday.com/news/,wsj.com,abcnews.go.com,' \
          'apnews.com,news.google.com,politico.com,washingtonpost.com,washingtontimes.com' \
          'latimes.com,nytimes.com,theatlantic.com,npr.org,nypost.com,chicago-tribune.com' \
          'wired.com,vox.com'


def get_news(start_date, end_date, num_articles):
    print("Connecting to Google News API...")

    top_headlines = NEWS_API.get_everything(from_param=start_date,
                                            to=end_date,
                                            language='en',
                                            domains=DOMAINS,
                                            page_size=num_articles)

    articles_json = []
    articles = top_headlines['articles']
    print('Searching for ' + str(len(articles)) + ' articles between ' + str(start_date)
          + ' and ' + str(end_date))
    # loop through articles and scrape article text with scraper
    for i, article in enumerate(articles):
        url = article['url']
        print(url)
        scraped_title, scraped_authors, scraped_text = scrape_text(url)
        # threshold
        if len(scraped_text) < MINIMUM_ARTICLE_CHAR_LENGTH:
            print('Article is too short: ' + url)
            # text is too short so delete article
            with open('deleted_articles.txt', 'w') as f:
                f.write(url)
        else:
            articles_json.append(
                make_article_json(article, scraped_title, scraped_authors, scraped_text))
    return json.dumps(articles_json)


def make_article_json(article, scraped_title, scraped_authors, scraped_text):
    return {
        'sourceName': article['source']['name'],
        'authors': scraped_authors,
        'title': clean_text(scraped_title),
        'description': None,
        'url': article['url'],
        'timePublished': article['publishedAt'],
        'content': clean_text(scraped_text)
    }


def scrape_text(url):
    try:
        article = Article(url)
        article.download()
        article.parse()
        return article.title, article.authors, article.text
    except ArticleException:
        print("couldn't parse: " + url)


def clean_text(text):
    for char in BAD_CHARS:
        text = text.replace(char, '')
    text = re.sub(r'([a-z])\.([A-Z])', r'\1. \2', text)
    return text
