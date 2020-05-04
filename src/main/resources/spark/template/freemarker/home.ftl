<!DOCTYPE html>
<head>
    <meta charset="utf-8">
    <title>Bubble</title>
    <meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1">
    <link rel="stylesheet" href="../css/bootstrap.min.css">
    <link rel="stylesheet" href="../css/mdb.css">
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
            <input type="date" id="date">
            </div>
            <div class="row">
                <div class="col my-auto">
            <button class="btn btn-primary btn-lg"  id="dateButton" type="button">Show</button>
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
                <div class="card-header"><h4>Clustering Parameters</h4></div>
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
                            <label for="entityWeight">Entity Weight</label>
                            <input name="entityWeight" id="entityWeight" class="custom-range" type="range" min="0"
                                   max="1"
                                   value=".5" step="0.01">

                            <select name="clusterMethod" class="browser-default custom-select">
                                <option selected value="1">Cluster Method 1</option>
                                <option value="2">Cluster Method 2</option>
                            </select>

                            <label for="edgeThreshold">Edge Percentage Threshold</label>
                            <input name="edgeThreshold" type="number" class="form-control" id="edgeThreshold" min="0"
                                   max="100"
                                   value="75">

                            <label for="numArticles">Maximum Number of Articles</label>
                            <input name="numArticles" type="number" class="form-control" id="numArticles" min="0"
                                   max="100"
                                   value="75">
                            <button class="btn btn-primary btn-sm" type="submit" style="margin:
                            auto; margin-bottom: 10px; margin-top: 10px;">Recluster</button>
                            <button id="resetButton" class="btn btn-blue-grey btn-sm" type="button" style="margin:auto; margin-bottom:10px;">Reset</button>
                        </div>
                    </form>
                </div>
            </div>
            <div class="hidden-lg hidden-md hidden-sm">&nbsp;</div>
            <div class="hidden-lg hidden-md hidden-sm">&nbsp;</div>
            <div class="hidden-lg hidden-md hidden-sm">&nbsp;</div>
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
    </div>
</div>

<!--- scripts --->
<script src="../js/particles.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1/jquery.min.js"></script>
<script src="https://d3js.org/d3.v5.js"></script>
<script src="../js/bootstrap.min.js"></script>
<script src="../js/mdb.min.js"></script>
<script src="../js/home.js"></script>
</body>
</html>