from newsapi import NewsApiClient
from datetime import datetime, timedelta
import json
from parser import get_text

# key for accessing google news api
api_key = '77d7cb4756d44e13aea4a50d033d27e3'

# create 
newsapi = NewsApiClient(api_key=api_key)

# get current time
today = datetime.now()
current_time = datetime(today.year, today.month, today.day, 
                        today.hour, today.minute, today.minute)
hour_ago = current_time + timedelta(hours=-1)

current_time = str(current_time).replace(' ', 'T')
hour_ago = str(hour_ago).replace(' ', 'T')

print(current_time)
print(hour_ago)



# get top headlines
top_headlines = newsapi.get_top_headlines(language='en',
                                        page_size=100)

print(len(top_headlines['articles']))

urls = set(map(lambda a: a['url'], top_headlines['articles']))

for article in top_headlines['articles']:
    article['content'] = get_text(article['url'])



# for article in top_headlines['articles']:
#     print(article['url'])
    
