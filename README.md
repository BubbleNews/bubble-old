#Bubble README

##Overview
With Bubble we set out to make a site that would show the current most popular news stories, and
 show the what was being written about those stories from across the spectrum. Our project allows
  you to see groupings of related articles on different days to track the news. We also provide a
   means of understanding why articles have been grouped the way they have. 
##How to Run
* Running the GUI
    * First compile the code by running **mvn package**
    * Make sure you have a bubble.db file located in the data folder
    * Run the GUI by typing **./run** into terminal
    * You can then access the GUI at **http://localhost:4567/bubble/home**
* Installing the Python Virtual Environment
    * 
* Getting the Database
    * 
* Add Articles to the Database
    *
* Cluster Articles in the Database
    * 
## Using the GUI
* Main Page
    * On the homepage of the website you will see all the most popular headlines of the day.
    * You can click on any headline to see all the articles we've grouped into this cluster.
    * Clicking the headlines of these articles will take you to the actual article.
* Changing Date
    * At the top center you have the ability to select another date. Clicking show will show you
     the clusters from this new date.
* Showing/Hiding Sources
    * In the panel on the left side you can toggle which news sources are visible.
* Reclustering
    * In the panel on the right side you can recluster the articles from any day with your own
     parameters. To find more info on this, click the info button on the panel.
* Data Visualization
    * For any cluster you can view a data visualization representing why its articles were
     clustered by opening the cluster and clicking on the Visualization Tab. More info on these
      visualizations is available by clicking the Info button.
## How it Works
* First, articles are loaded in from a news API, we gather articles from many sources.
* Article content is then passed through a natural language processor that extracts entities
, which are important key words. At this stage we also lemmize the content of the article and
 title and encode it with stop words.
 * We then compare all the articles from one day to all the other articles from one day by using
  term frequency and inverse document frequency of the text, titles, and entities of each article
  . This allows us to calculate edge weights between articles representing their similarity. 
* Lastly, we run this newly constructed graph through our clustering algorithm to find the final
 clusters which we store and display on the webpage.
## Project Structure
* API
    * Handlers
        *
    * Pipeline
        *
    * Response
* Bubble
    *
* Clustering
    *
* Database
    *
* Graph
    *
* Language
    *
* Main
    *
* Similarity
    *
* Python
    *
## Team Members & Division of Labor
- Kshitij Sachan
    - 
- John Graves
    - 
- Ben Silverman
    - Edge weight calculation via term frequency and inverse document frequency.
    - GUI
- Ian Layzer
    - 
