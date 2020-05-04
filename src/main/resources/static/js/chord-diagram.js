export { renderChord };

function renderChord(parsed) {
    console.log(parsed);
    const width = 1500
    const height = 600;
    const margin = {top: 10, left: 500, right: 500, bottom: 10};
    const innerWidth = width - margin.left - margin.right;
    const innerHeight = height - margin.top - margin.bottom;
    const aspect = width / height;
    const textGap = 20;
    const outerRadius = Math.min(innerWidth, innerHeight) * 0.5;
    const innerRadius = outerRadius - 20;
    const edgeOpacity = 0.67;
    const padAngle = 0.15;

    // get data
    const arr = getChordDataMatrix(parsed);
    const titles = arr[0];
    const matrix = arr[1];

    const svg = d3.select(".chord-chart")
        .append("svg")
            .attr("preserveAspectRatio", "xMinYMid")
            .attr("width", width)
            .attr("height", height)
        .append('g')
        .attr('transform', `translate(${margin.left + outerRadius},${margin.top + outerRadius})`);

    const chords = d3.chord()
        .padAngle(padAngle)
        .sortSubgroups(d3.descending)(matrix);

    const arc = d3.arc()
        .innerRadius(innerRadius)
        .outerRadius(outerRadius);

    const color = d3.scaleOrdinal(d3.schemeCategory10);
    const ribbon = d3.ribbon().radius(innerRadius);

    const group = svg.selectAll('g')
        .data(chords.groups)
        .join('g')

    // make outer article nodes
    group.append('path')
        .attr('fill', d => color(d.index))
        .attr('stroke', d => d3.rgb(color(d.index)).darker())
        .attr('d', arc);

    // display article titles
    group.selectAll('g')
        .data(d => labels(d, titles))
        .join('g')
        .attr('transform', d => `rotate(${d.angle * 180 / Math.PI - 90}) 
            translate(${outerRadius + textGap},0) rotate(${-1 * (d.angle * 180 / Math.PI - 90)})`)
        .attr('text-anchor', d => getTextAnchor(d))
        .append('text')
            .text(d => d.title);

    // make paths between articles
    svg.append('g')
        .attr('fill-opacity', edgeOpacity)
    .selectAll('path')
        .data(chords)
        .join('path')
        .attr('d', ribbon)
        .attr('fill', d => interpolateColor(d, color))
        .attr('stroke', d => d3.rgb(color(d.target.index)).darker());

    // TODO: Get resizing working
    window.onresize = () => {
        console.log('test')
        const targetWidth = (window.innerWidth < width) ? window.innerWidth : width;
        d3.select(".chord-chart")
            .attr("width", targetWidth)
            .attr("height", targetWidth / aspect);
    }
}



function getTextAnchor(d) {
    if (d.angle > Math.PI) {
        return 'end'
    }
    return null;
}

function labels(d, titles) {
    const midAngle = (d.startAngle + d.endAngle) / 2;
    return [{
        angle: midAngle,
        title: titles[d.index]
    }];
}

function interpolateColor(d, color) {
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
    // map from article ID -> index in matrix array
    const indices = {};

    // this will be data matrix for the chord diagram
    const matrix = [];
    // initialize matrix with empty values
    for(let i = 0; i < numVertices; i++) {
        matrix[i] = new Array(numVertices);
        // set weight from article to itself to 0 because articles can't be similar to themselves
        matrix[i][i] = 0;
    }

    console.log(edges);
    // fill in matrix with distances
    edges.forEach(edge => {
        // get index of source and destination articles in matrix
        const srcIndex = getIndex(edge.articleId1, indices);
        const destIndex = getIndex(edge.articleId2, indices);

        // get distance values of each edge
        // need to take reciprocal because clustering algorithm treats a lower weight as better
        const distance = 1 / edge.totalDistance;
        matrix[srcIndex][destIndex] = distance;
        matrix[destIndex][srcIndex] = distance;

        // store title of each article
        const srcTitle = edge.articleTitle1;
        const destTitle = edge.articleTitle2;
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