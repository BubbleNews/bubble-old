function renderChord(parsed) {
    const margin = {top: 40, right: 100, bottom: 40, left: 175};
    const width = 500, height = Math.min(640, width);
    const inner_width = width - margin.left - margin.right;
    const inner_height = height - margin.top - margin.bottom;
    const outerRadius = Math.min(width, height) * 0.5 - 30;
    const innerRadius = outerRadius - 20;

    // get data
    const arr = getChordDataMatrix(parsed);
    const titles = arr[0];
    const matrix = arr[1];

    console.log(titles);

    const svg = d3.select(".chord-chart")
        .append("svg")
        .attr('viewBox', [-width / 2, -height / 2, width, height]);

    const chords = d3.chord()
        .padAngle(.15)
        .sortSubgroups(d3.descending)(matrix);

    const group = svg.append('g').attr('class', 'node')
        .selectAll('g')
        .data(chords.groups)
        .join('g');

    const arc = d3.arc()
        .innerRadius(innerRadius)
        .outerRadius(outerRadius);

    const color = d3.scaleOrdinal(d3.schemeCategory10);

    const ribbon = d3.ribbon().radius(innerRadius);

    console.log(chords);

    const labels = group.append('g').selectAll('g')
        .data(titles)
        .join('g')
        .attr('class', (d, i) => i);

    group.append('path')
        .attr('fill', d => color(d.index))
        .attr('stroke', d => d3.rgb(color(d.index)).darker())
        .attr('d', arc);

    svg.append('g')
        .attr('fill-opacity', .67)
    .selectAll('path')
        .data(chords)
        .join('path')
        .attr('d', ribbon)
        .attr('fill', d => interpolateColor(d, color, matrix))
        .attr('stroke', d => d3.rgb(color(d.target.index)).darker());
}

function color() {

}

function interpolateColor(d, color, matrix) {
    const srcIndex = d.source.index;
    const targetIndex = d.target.index;
    const srcColor = color(srcIndex);
    const targetColor = color(targetIndex);
    return d3.interpolateRgb(srcColor, targetColor)(0.5);
}

function getChordDataMatrix(parsed) {
    const edges = parsed['edges'];
    const numVertices = parsed['numVertices'];
    const titles = [];

    // this will be data matrix for the chord diagram
    const matrix = [];
    // initialize matrix with empty values
    for(let i = 0; i < numVertices; i++) {
        matrix[i] = new Array(numVertices);

        // set weight from article to itself to 0 because articles can't be similar to themselves
        matrix[i][i] = 0;
    }

    // map from article ID -> index in matrix array
    const indices = {};

    // fill in matrix with distances
    edges.forEach(edge => {
        // get index of source and destination articles in matrix
        const srcIndex = getIndex(edge.src.article.id, indices);
        const destIndex = getIndex(edge.dst.article.id, indices);

        // get distance values of each edge
        // need to take reciprocal because clustering algorithm treats a lower weight as better
        const distance = 1 / edge.distance;
        matrix[srcIndex][destIndex] = distance;
        matrix[destIndex][srcIndex] = distance;

        // store title of each article
        const srcTitle = `${edge.src.article.sourceName}: ${edge.src.article.title}`;
        const destTitle = `${edge.dst.article.sourceName}: ${edge.dst.article.title}`;
        titles[srcIndex] = srcTitle;
        titles[destIndex] = destTitle;
    })

    return [titles, matrix];
}

function getIndex(id, indices) {
    if (!indices.hasOwnProperty(id)) {
        indices[id] = Object.keys(indices).length;
    }
    return indices[id];
}

function getClusterDetails(clusterId) {
    let clusterUrl = 'api/details';
    // add id to cluster base url
    clusterUrl += '?id=' + clusterId;
    // send get request
    $.get(clusterUrl, response => {
        const parsed = JSON.parse(response);
        renderChord(parsed)
    });
}

getClusterDetails(9);

function getEdgeDetails(id1, id2) {
    let clusterUrl = 'api/edge';
    // add id to cluster base url
    clusterUrl += '?id1=' + id1 + '&id2=' + id2;
    // send get request
    $.get(clusterUrl, response => {
        const parsed = JSON.parse(response);
        console.log(parsed);
    });
}

getEdgeDetails(1, 2);
