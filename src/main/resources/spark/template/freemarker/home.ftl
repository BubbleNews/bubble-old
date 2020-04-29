<!DOCTYPE html>
<head>
    <meta charset="utf-8">
    <title>Bubble</title>
    <meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1">
    <link rel="stylesheet" href="../css/home.css">
</head>
<body>
<div id="particles-js"></div>
<div id="titleWrapper">
    <h1 style="color: LightCoral">Bubble</h1>
</div>

<div id="dateWrapper">
    <div id="dateAndButton">
        <input type="date" id="date">
        <button id="dateButton" type="button">Show</button>
    </div>
</div>

<div id="graphWrapper">
    <div id="sourcesWrapper" class="sidePanel">
        <h4>Visible Sources</h4>
        <div id="sources">
            <#list sourceList as source>
                <button type="button" class="sourceToggle">${source}</button>
            </#list>
        </div>
    </div>
    <div id="chartMessageWrapper">
        <div id="chartMessage" class="message">

        </div>
    </div>
    <div id="clusters">
    </div>
    <div id="reclusterWrapper" class="sidePanel">
        <h4>Clustering Parameters</h4>
        <div id="clusterParameters">
            <p>Edge Percentage Threshold</p>
            <input type="number" id="edgeThreshold" min="0" max="100" value="75">
            <p>Entity vs Vocab Weight</p>
            <input type="range" min="0" max="1" step="0.1">
            <button type="button" style="margin: auto;">Recluster</button>
        </div>
    </div>
</div>

<!--- scripts --->
<script src="../js/particles.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1/jquery.min.js"></script>
<script src="https://d3js.org/d3.v5.js"></script>
<script src="../js/home.js"></script>
</body>
</html>