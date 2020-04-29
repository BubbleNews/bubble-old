from __future__ import print_function

import json
import dateutil.parser

from newspaper import Article
from newspaper.article import ArticleException
from newsapi import NewsApiClient

# key for accessing google news api
API_KEY = '77d7cb4756d44e13aea4a50d033d27e3'
NEWS_API = NewsApiClient(api_key=API_KEY)
MINIMUM_ARTICLE_CHAR_LENGTH = 700
BAD_CHARS = ['\r', '\n']
DOMAINS = 'apnews.com,time.com,wsj.com,politico.com,washingtonpost.com,' \
          'nytimes.com,vox.com,usatoday.com/news,npr.org,theatlantic.com'


# return comma separated list of sources
# def get_sources():
#     source_ids = []
#     sources = NEWS_API.get_sources(language='en', country='us')
#     for source in sources['sources']:
#         source_ids.append(source['id'].encode('ascii'))
#     return ','.join(source_ids)


def get_news(start_date, end_date, num_articles):
    print("Connecting to Google News API...")

    top_headlines = NEWS_API.get_everything(from_param=start_date,
                                            to=end_date,
                                            language='en',
                                            domains=DOMAINS,
                                            page_size=num_articles)

    articles_json = []
    articles = top_headlines['articles']
    print('Retrieved ' + str(len(articles)) + ' articles between ' + str(start_date)
          + ' and ' + str(end_date))
    # loop through articles and scrape article text with scraper
    for i, article in enumerate(articles):
        url = article['url']
        scraped_title, scraped_authors, scraped_text = scrape_text(url)
        # threshold
        if len(scraped_text) < MINIMUM_ARTICLE_CHAR_LENGTH:
            print('Article is too short: ' + url)
            # text is too short so delete article
            with open('deleted_articles.txt', 'w') as f:
                f.write(url)
        else:
            articles_json.append(
                make_article_json(article, scraped_title, scraped_authors,
                                  clean_text(scraped_text, BAD_CHARS)))
    return json.dumps(articles_json)


def make_article_json(article, scraped_title, scraped_authors, scraped_text):
    return {
        'sourceName': article['source']['name'],
        'authors': scraped_authors,
        'title': scraped_title,
        'description': article['description'],
        'url': article['url'],
        'timePublished': article['publishedAt'],
        'content': scraped_text
    }


def scrape_text(url):
    try:
        article = Article(url)
        article.download()
        article.parse()
        return article.title, article.authors, article.text
    except ArticleException:
        print("couldn't parse: " + url)


def clean_text(text, bad_chars):
    for char in bad_chars:
        text = text.replace(char, '')
    return text

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