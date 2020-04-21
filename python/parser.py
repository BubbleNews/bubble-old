from __future__ import print_function
import argparse
import json
from newspaper import Article
from newspaper.article import ArticleException


def get_text(url):
    try:
        article = Article(url)
        article.download()
        article.parse()
        return make_json(article)
    except ArticleException:
        print("couldn't parse: " + url)
        pass


def make_json(article):
    return json.dumps({
        'url': article.url,
        'title': article.title,
        'authors': article.authors,
        'date': article.publish_date,
        'text': article.text
    })


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description='Get the text of an article from a URL.')
    parser.add_argument('path', help='path to text file containing list of article URLs')
    args = parser.parse_args()

    with open(args.path) as f:
        urls = f.read().splitlines()

    for url_link in urls:
        print(get_text(url_link))
