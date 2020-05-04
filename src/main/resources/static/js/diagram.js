function renderChord(parsed) {
    const width = 500
    const height = Math.min(640, width);
    const textGap = 20;
    const outerRadius = Math.min(width, height) * 0.5 - 30;
    const innerRadius = outerRadius - 20;
    const edgeOpacity = 0.67;
    const padAngle = 0.15;

    // get data
    const arr = getChordDataMatrix(parsed);
    const titles = arr[0];
    const matrix = arr[1];

    const svg = d3.select(".chord-chart")
        .append("svg")
        .attr('viewBox', [-width / 2, -height / 2, width, height]);

    const chords = d3.chord()
        .padAngle(padAngle)
        .sortSubgroups(d3.descending)(matrix);

    const arc = d3.arc()
        .innerRadius(innerRadius)
        .outerRadius(outerRadius);

    const color = d3.scaleOrdinal(d3.schemeCategory10);
    const ribbon = d3.ribbon().radius(innerRadius);

    const group = svg.append('g').attr('class', 'node')
        .selectAll('g')
        .data(chords.groups)
        .join('g')

    group.append('path')
        .attr('fill', d => color(d.index))
        .attr('stroke', d => d3.rgb(color(d.index)).darker())
        .attr('d', arc);

    group.selectAll('g')
        .data(d => labels(d, titles))
        .join('g')
        .attr('transform', d => `rotate(${d.angle * 180 / Math.PI - 90}) 
            translate(${outerRadius + textGap},0) rotate(${-1 * (d.angle * 180 / Math.PI - 90)})`)
        .attr('text-anchor', d => getTextAnchor(d))
        .append('text')
            .text(d => d.title);


    svg.append('g')
        .attr('fill-opacity', edgeOpacity)
    .selectAll('path')
        .data(chords)
        .join('path')
        .attr('d', ribbon)
        .attr('fill', d => interpolateColor(d, color))
        .attr('stroke', d => d3.rgb(color(d.target.index)).darker());
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






////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////

/**
 *
 * @param data Object of the form:
 * {
 *     totalEntities: {},
 *     totalTitle: {},
 *     totalWords: {}
 * }
 * @param type
 */
function renderBarPlot(data, type) {
    console.log(data);
    const width = 1000;
    const height = 800;
    const margin = {left: 40, right: 10, top: 10, bottom: 0};
    const innerWidth = width - margin.left - margin.right;
    const innerHeight = height - margin.top - margin.bottom;
    const numBarsToDisplayThresholdPercent = 0.9;
    const labelPadding = 10;
    const fillColor = {
        entity: 'steelblue',
        title: 'red',
        text: 'orange'
    };

    const words = formatBarPlotData(data, type);
    const relevantWords = sliceWords(words, numBarsToDisplayThresholdPercent);
    console.log(relevantWords);


    const svg = d3.select(".bar-chart")
        .append("svg")
        .attr('width', width)
        .attr('height', height)
        .append('g')
        .attr('transform', `translate(${margin.left}, ${margin.top})`);

    const x = d3.scaleLinear()
        .domain(d3.extent(relevantWords.map(d => d.value)))
        .range([0, innerWidth])
        .nice();

    const y = d3.scaleBand()
        .domain(d3.range(relevantWords.length))
        .range([0, innerHeight])
        .padding(0.1);

    svg.append('g')
        .attr('class', 'bar-labels')
        .attr('transform', `translate(${margin.left},0)`)
        .selectAll('text')
        .data(relevantWords)
        .join('text')
        .attr('y', (d, i) => y(i))
        .attr('text-anchor', 'end')
        .attr('dy', y.bandwidth()/2)
        .attr('dx', -labelPadding)
        .text(d => d.word);


    const rect = svg.append('g')
        .attr('transform', `translate(${margin.left},0)`)
        .selectAll('rect')
        .data(relevantWords)
        .join('rect')
            .attr('x', 0)
            .attr('y', (d, i) => y(i))
            .attr('width', d => x(d.value))
            .attr('height', y.bandwidth())
            .attr('fill', d => fillColor[d.type]);

    rect.append()
}

function sliceWords(words, thresholdPercent) {
    let sum = 0;
    words.forEach(w => sum += w.value);
    const threshold = sum * thresholdPercent;

    let numItems = 0;
    let increasingSum = 0;
    while (increasingSum < threshold) {
        increasingSum += words[numItems].value;
        numItems++;
    }

    return words.slice(0, numItems);
}

/**
 *
 * @param hashmaps
 * @param type
 * @returns []
 */
function formatBarPlotData(hashmaps, type) {
    const entities = hashmaps.totalEntities;
    const text = hashmaps.totalWords;
    const title = hashmaps.totalTitle;
    const data = [];

    switch (type) {
        case 'entity':
            addToArray(entities, type, data);
            break;

        case 'text':
            addToArray(text, type, data);
            break;

        case 'title':
            addToArray(title, type, data);
            break;

        default:
            addToArray(entities, 'entity', data);
            addToArray(text, 'text', data);
            addToArray(title, 'title', data);
    }

    data.sort((a,b) => b.value - a.value);
    return data;
}

function addToArray(map, type, arr) {
    // console.log('called', arr.length);
    for (let word in map) {
        arr.push({
            type: type,
            word: word,
            value: map[word]
        })
    }
}







function getClusterDetails(clusterId) {
    let clusterUrl = 'api/details';
    // add id to cluster base url
    clusterUrl += '?id=' + clusterId;
    // send get request
    $.get(clusterUrl, response => {
        const parsed = JSON.parse(response);
        renderChord(parsed);
        renderBarPlot(parsed, 'all');
    });
}

function getEdgeDetails(id1, id2) {
    let clusterUrl = 'api/edge';
    // add id to cluster base url
    clusterUrl += '?id1=' + id1 + '&id2=' + id2;
    // send get request
    $.get(clusterUrl, response => {
        const parsed = JSON.parse(response);
        // renderBarPlot(parsed);
    });
}

getClusterDetails(6);
getEdgeDetails(23, 40);