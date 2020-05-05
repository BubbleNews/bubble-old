<!DOCTYPE html>
<head>
    <meta charset="utf-8">
    <title>Diagram</title>
    <meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1">
    <link rel="stylesheet" href="../css/home.css">
</head>
<body>
<h>Diagram!</h>
<button type="button" class="btn btn-info">Entity</button>
<button type="button" class="btn btn-info">Text</button>
<button type="button" class="btn btn-info">Title</button>
<button type="button" class="btn btn-info">All</button>
<div id="chords" class="chord-chart"></div>
<div id="box-plot"></div>
<div class="bar-chart"></div>


<script src="https://ajax.googleapis.com/ajax/libs/jquery/1/jquery.min.js"></script>
<script src="https://d3js.org/d3.v5.js"></script>
<script type="module" src="../js/chord-diagram.js"></script>
<script type="module" src="../js/barchart.js"></script>
<script type="module" src="../js/d3.js"></script>
</body>
</html>