<!DOCTYPE html>
<head>
    <meta charset="utf-8">
    <title>Bubble</title>
    <meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1">
    <link rel="stylesheet" href="../css/bootstrap.min.css">
    <link rel="stylesheet" href="../css/mdb.css">
    <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
    <link rel="stylesheet" href="../css/home.css">
</head>
<body>
<div class="header">
    <div id="particles-js"></div>
    <div id="titleWrapper">
        <h1>Bubble</h1>
    </div>

    <div id="dateWrapper">
        <div id="dateAndButton">
            <div class="row">
            <input type="date" id="date" readonly>
            </div>
            <div class="row">
                <div class="col my-auto">
            <button class="btn btn-primary btn-lg"  id="dateButton" type="button"
            style="margin-top: 1em;">Show</button>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="container" id="graphWrapper">
    <div class="row">
        <div class="col-lg-2">
            <div id="sourcesWrapper" class="card sticky-top sidePanel">
                <div class="card-header"><h4>Toggle Sources</h4></div>
                <div class="card-body" id="sources">
                    <#list sourceList as source>
                        <button type="button" class="btn btn-success
                        sourceToggle">${source}</button>
                    </#list>
                </div>
            </div>
            <div class="hidden-lg hidden-md hidden-sm">&nbsp;</div>
        </div>
        <div class="col-lg-2 order-lg-last">
            <div id="reclusterWrapper" class="card sticky-top sidePanel">
                <div class="card-header"><h4>Grouping Parameters</h4>
                    <button type="button" class="btn btn-outline-blue-grey btn-sm waves-effect"
                     style="margin-bottom:10px;" data-toggle="modal" data-target="#infoModal">
                        Info
                    </button></div>
                <div class="card-body" id="clusterParameters">
                    <form id="reclusterParams">
                        <div class="form-group">
                            <label for="textWeight">Text Weight</label>
                            <input name="textWeight" id="textWeight" class="custom-range" type="range" min="0" max="1"
                            value=".5" step="0.01">
                            <label for="titleWeight">Title Weight</label>
                            <input name="titleWeight" id="titleWeight" class="custom-range" type="range" min="0"
                                   max="1"
                                   value=".5" step="0.01">
                            <label for="entityWeight">Key Word Weight</label>
                            <input name="entityWeight" id="entityWeight" class="custom-range" type="range" min="0"
                                   max="1"
                                   value=".5" step="0.01">

                            <label for="numArticles">Maximum Number of Articles</label>
                            <input name="numArticles" type="number" class="form-control" id="numArticles" min="0"
                                   max="200"
                                   value="100" style="margin-bottom: 5px;">
                            <button class="btn btn-primary btn-sm" type="submit" style="margin:
                            5px auto;">Regroup</button>
                            <button id="resetButton" class="btn btn-blue-grey btn-sm"
                                    type="button" style="margin: 5px auto;">Reset</button>
                        </div>
                    </form>
                </div>
            </div>
            <div class="hidden-lg hidden-md hidden-sm">&nbsp;</div>
        </div>
        <div id="mainWindow" class="col-lg-8">
            <div id="chartMessageWrapper">
                <div id="chartMessage"></div>
            </div>
            <div id="mainLoader" class="spinner-border text-primary" role="status">
                <span class="sr-only">Loading...</span>
            </div>
            <div class="accordion" id="clusters"></div>
            <div class="hidden-lg hidden-md hidden-sm">&nbsp;</div>
        </div>
        <div class="modal fade right" tabindex="-1" role="dialog" id="infoModal">
            <div class="modal-dialog modal-full-height modal-right" role="document">
                <div class="modal-content">
                    <div class="modal-header align-self-center">
                        <h5 class="modal-title">How Bubble Works</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        </button>
                    </div>
                    <div class="modal-body">
                        <h2>How We Group News Articles</h2>
                        <p>We pull in news articles hourly from a variety of sources. We then
                            calculate the similarity between each article and every other
                            article through a combination of metrics based on shared words in
                            the content, shared words in the title, and shared key words of each
                            article. From this, we are able to calculate a similarity score for
                            each pair of articles. We are then able to use these similartiy
                            scores to find clusters of similar articles!</p>
                        <h2>Not satisfied with this grouping of news topics?</h2>
                            <p>You can tweak our algorithm and recalculate the groupings by
                                adjusting the weighting of the title, main text of each article,
                                and certain key words that we extract from the text. Below is a
                                list describing each parameter in more detail:</p>
                        <uL><li><h3>Text, Title, & Key Word Weight</h3>
                        <p>We use the text, titles, and important keywords in each article
                            to compare each article to other articles and calculate a
                            similarity score. By adjusting these sliders, you can adjust
                            their relative weight in the overall similarity calculations!</p></li>
                        <li><h3>Maximum Number of Articles</h3>
                        <p>Our algorithm for grouping articles can take a long time if we run it
                            on too many articles. Therefore, with this parameter you can set a
                            maximum number of articles to use to make the algorithm run faster.
                            Lowering this number will result in less articles showing up on the
                            final page, but also the algorithm will run faster. To run regrouping
                            quickly, we recommend setting this number to 200 or below.
                        </p></li></ul>
                        <h2>Credits</h2>
                        <p>This project was developed by John Graves, Kshitij Sachan, Ian Layzer,
                            and Ben Silverman as a final project for CSCI 0320 at Brown University.
                        </p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-primary" data-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>
        <div class="modal fade" tabindex="-1" role="dialog" id="vizInfoModal">
            <div class="modal-dialog modal-dialog-centered" role="document">
                <div class="modal-content">
                    <div class="modal-header align-self-center">
                        <h5 class="modal-title">Data Visualization Info</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        </button>
                    </div>
                    <div class="modal-body">
                        <h2>Overview</h2>
                        <p>The visualization explains why these articles are grouped together.
                            The diagrams show what factors (based on title, text, and key words
                            that we extract from the article) led to the overall connection
                            score  between each article, and which of these connections are the
                            strongest.</p>
                        <h2>Chord Diagram</h2>
                        <p>The chord diagram on top shows the relation between each pair of
                        articles. A thicker line indicates that the two articles are more closely
                        related. Less white space between articles in the outside circle
                        indicates that all of the articles are more closely related than in other
                        topics.</p>
                        <h2>Bar Chart</h2>
                        <p>The bar chart on the bottom shows the most important words in
                        this topic. Clicking on a connection in the chord diagram will update the
                         bars and display the relevant words between any two pairs of articles.
                            We use the title, text, and key words that we extract from the
                            article to calculate similarity. Click on the 'title', 'text', and
                            'key words' buttons to see the most important words of each type. </p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-primary" data-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!--- scripts --->
<script src="../js/particles.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1/jquery.min.js"></script>
<script src="https://d3js.org/d3.v5.js"></script>
<script src="../js/bootstrap.min.js"></script>
<script src="../js/mdb.min.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script src="https://d3js.org/d3.v5.js"></script>
<script type="module" src="../js/chord-diagram.js"></script>
<script type="module" src="../js/barchart.js"></script>
<script type="module" src="../js/d3.js"></script>
<script type="module" src="../js/home.js"></script>
</body>
</html>