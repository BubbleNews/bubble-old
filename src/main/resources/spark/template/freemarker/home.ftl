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
    <button id="dateButton" type="button">Show</button>
</div>

<div id="graphWrapper">
    <div id="sourcesWrapper">
        <h4>Visible Sources</h4>
        <div id="sources">
            <#list sourceList as source>
                <button type="button" class="sourceToggle">${source}</button>
            </#list>
        </div>
    </div>
    <div id="clusters">
    </div>
</div>

<!--- scripts --->
<script src="../js/particles.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1/jquery.min.js"></script>
<script src="https://d3js.org/d3.v5.js"></script>
<script src="../js/home.js"></script>
</body>
</html>