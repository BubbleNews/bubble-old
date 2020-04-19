import argparse, requests, nltk, newspaper
from bs4 import BeautifulSoup
import breadability
from breadability.readable import Article
from boilerpipe.extract import Extractor

def newspaper_package(url):
	article = newspaper.Article(url, keep_article_html=True)
	article.download()
	article.parse()
	return article.text

## Would require us to make a custom web scraper for each news site so not feasible
def beautiful_soup_package(url):
	html = requests.get(url)
	soup = BeautifulSoup(html, features="lxml").get_text()
	print(soup)

def nltk(url):
	return

def readibility_package(url):
	html = requests.get(url).text
	document = breadability.readable.Article(html, url=url, return_fragment=False)
	return document.readable

def readibility_and_boiler_package(url):
	html = requests.get(url).text
	document = breadability.readable.Article(html, url=url, return_fragment=False)
	# return document.readable
	print(Extractor(extractor='ArticleExtractor', html=document.readable).getText())

def boilerpipe_package(url):
	print(Extractor(extractor='ArticleExtractor', url=url).getText())

if __name__ == "__main__":
	parser = argparse.ArgumentParser(description='Get the text of an article from a URL.')
	parser.add_argument('path', help='path to text file containing list of article URLs')
	args = parser.parse_args()

	with open(args.path) as f:
		urls = f.read().splitlines()

	url = urls[0]
	print("----------------------NEWSPAPER----------------------")
	print(newspaper_package(url))

	print("----------------------READABILTY----------------------")
	print(readibility_package(url))

	print("----------------------BOILERPIPE----------------------")
	boilerpipe_package(url)

	print("----------------------READABILITY AND BOILER----------------------")
	readibility_and_boiler_package(url)
	

	# html = requests.get(url)
	# soup = BeautifulSoup(html).get_text()


	# raw = nltk.clean_html(soup)  
	# print(raw)

# BEATIFUL SOUP
# 	soup = BeautifulSoup(html_as_text)

# 	# kill all script and style elements
# 	for script in soup(["script", "style"]):
# 	    script.extract()    # rip it out

# 	# get text
# 	text = soup.get_text()

# 	# break into lines and remove leading and trailing space on each
# 	lines = (line.strip() for line in text.splitlines())
# 	# break multi-headlines into a line each
# 	chunks = (phrase.strip() for line in lines for phrase in line.split("  "))
# 	# drop blank lines
# 	text = '\n'.join(chunk for chunk in chunks if chunk)

# 	print(text)

	# document = Article(html_as_text, url=url)
	# print(document.readable)
