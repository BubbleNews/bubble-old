import argparse
import newspaper


def get_text(url):
    try:
        article = newspaper.Article(url)
        article.download()
        article.parse()
        print('finished parsing')
        return article.text
    except newspaper.article.ArticleException:
        print("couldn't parse: " + url)
        pass


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description='Get the text of an article from a URL.')
    parser.add_argument('path', help='path to text file containing list of article URLs')
    args = parser.parse_args()

    with open(args.path) as f:
        urls = f.read().splitlines()

    for url_link in urls:
        print(get_text(url_link))
