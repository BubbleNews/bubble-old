import newspaper

def newspaper_package(url):
	try:
		article = newspaper.Article(url)
		article.download()
		article.parse()
		print('finished parsing')
		return article.text
	except newspaper.article.ArticleException:
		print("couldn't parse: " + url)
		pass
