
# Bubble README  
## Overview  
With Bubble we set out to make a site that would show the current most popular news stories, and  
 show the what was being written about those stories from across the spectrum. Our project allows  
  you to see groupings of related articles on different days to track the news. We also provide a  
   means of understanding why articles have been grouped the way they have.   
## How to Run  
#### Getting the Databases
* All the databases referenced are located [here](https://drive.google.com/drive/folders/18EIoz_7WVWED-k-nqFDHW0W8ZyW_Smzv?usp=sharing)
* You should have all of these databases downloaded and placed in the data folder to correctly compile and run the project
#### Running the GUI  
* First compile the code by running `mvn package`
* Make sure you have a final_data.db file located in the data folder  
* Run the GUI by typing `./run` into terminal  
* You can then access the GUI at **http://localhost:4567/bubble/home**  
#### Installing the Python Virtual Environment  
Unfortunately, we rely on Python 2.7 because the best web scraping library we found in our tests, 
[Newspaper3K](https://newspaper.readthedocs.io/en/latest/index.html) has a 
[bug](https://github.com/codelucas/newspaper/issues/485) in the Python3 version.
* Make a virtual environment (but this needs to be for python2) with the command 
`virtualenv -p /usr/bin/python2.7 venv`. Make sure to do this outside of the git repo to 
prevent this from getting added.
* Activate the virtual environment with `source venv/bin/activate` and install packages from requirements.txt
#### Load & Cluster New Articles
* First run the virtual environment. Instructions on how to get and run this are above.
* Next run the python server by typing `python api.py` make sure you are running python 2.7 or this will not work.
* Make sure you have all of the databases from our google drive in the data folder.
* First run the site from `./run`
* Then visit `http://localhost:4567/bubble/api/update` to run the news loader and updater.
## Using the GUI  
#### Main Page  
* On the homepage of the website you will see all the most popular headlines of the day.  
* You can click on any headline to see all the articles we've grouped into this cluster.  
* Clicking the headlines of these articles will take you to the actual article.  
#### Changing Date  
* At the top center you have the ability to select another date. Clicking show will show you  
 the clusters from this new date.  
#### Showing/Hiding Sources  
* In the panel on the left side you can toggle which news sources are visible.  
#### Reclustering  
* In the panel on the right side you can recluster the articles from any day with your own  
 parameters. To find more info on this, click the info button on the panel.  
#### Data Visualization  
* For any cluster you can view a data visualization representing why its articles were  
 clustered by opening the cluster and clicking on the Visualization Tab. More info on these  
  visualizations is available by clicking the Info button.  
## How it Works  
* First, articles are loaded in from a news API, we gather articles from many sources.  
* Article content is then passed through a natural language processor that extracts entities, which are important key words. At this stage we also lemmatize the content of the article (which is similar to   
stemming) and title and encode it with stop words.  
 * We then compare all the articles from one day to all the other articles from one day by using  
  term frequency and inverse document frequency of the text, titles, and entities of each article. This allows us to calculate edge weights between articles representing their similarity.   
* Lastly, we run this newly constructed graph through our clustering algorithm to find the final  
 clusters which we store and display on the webpage.  
## Project Structure: Packages  
#### API  
* Handlers  
    * Contains all Spark HTTP request handlers, the structure of the web app.
* Pipeline  
    *  Classes to load new news from Python server and then trigger clustering
    of that news.
* Response  
  * Easily serializable versions of some of the more complex article and cluster
  classes.
#### Bubble  
*  Classes for representing information about articles and their relations with
each other.
#### Clustering  
*  Package containing clustering functionality.
#### Database  
*  Classes interfacing with the Bubble database.
#### Graph  
*  Graph structure interfaces and classes.
#### Language  
* Utility classes for removing stop words and processing text, uses Stanford CoreNLP
for some parts.
#### Main  
* Contains main class to run the app.
#### Similarity
* Classes pertaining to the similarity measurements between articles.
#### Python  
* Scrapes articles using Newspaper3K library
* ## Team Members & Division of Labor  
* Kshitij Sachan  
  * Scraping articles in Python  
  * D3.js visualizations
* John Graves  
  * Clustering algorithms
  * D3.js visualizations  
  * Database Maintenance
* Ben Silverman  
  * Similarity between articles  
  * Front-end
* Ian Layzer  
  * Reclustering functionality
  * Front-end
  * Pipeline for loading new news
