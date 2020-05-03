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

<div id="titleWrapper">
    <h1 style="color: LightCoral">Bubble</h1>
</div>

<div id="dateWrapper">
    <div id="particles-js"></div>
    <div id="dateAndButton">
        <input type="date" id="date">
        <button class="btn btn-primary btn-lg" id="dateButton" type="button">Show</button>
    </div>
</div>

<div class="container" id="graphWrapper">
    <div class="row">
        <div class="col-lg-2">
            <div id="sourcesWrapper" class="card sticky-top sidePanel">
                <div class="card-header"><h4>Visible Sources</h4></div>
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
                    <form>
                        <div class="form-group">
                            <label for="edgeThreshold">Edge Percentage Threshold</label>
                            <input type="number" class="form-control" id="edgeThreshold" min="0"
                            max="100"
                                   value="75">
                            <label for="textWeight">Text Weight</label>
                            <input id="textWeight" class="custom-range" type="range" min="0" max="1"
                            value=".5" step="0.01">
                            <label for="titleWeight">Title Weight</label>
                            <input id="titleWeight" class="custom-range" type="range" min="0"
                                   max="1"
                                   value=".5" step="0.01">
                            <label for="entityWeight">Entity Weight</label>
                            <input id="entityWeight" class="custom-range" type="range" min="0"
                                   max="1"
                                   value=".5" step="0.01">
                            <button class="btn btn-primary btn-sm" type="button" style="margin:
                            auto; margin-bottom:10px;
                            ">Recluster</button>
                            <button class="btn btn-blue-grey btn-sm" type="button" style="margin:
                            auto;
                            ">Reset</button>
                        </div>
                    </form>
                </div>
            </div>
            <div class="hidden-lg hidden-md hidden-sm">&nbsp;</div>
            <div class="hidden-lg hidden-md hidden-sm">&nbsp;</div>
            <div class="hidden-lg hidden-md hidden-sm">&nbsp;</div>
            <div class="hidden-lg hidden-md hidden-sm">&nbsp;</div>
        </div>
        <div class="col-lg-8">
            <div id="chartMessageWrapper">
                <div id="chartMessage"></div>
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