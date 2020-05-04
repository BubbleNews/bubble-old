export { renderBarPlot };

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
    const width = 500;
    const height = 800;
    const margin = {left: 60, right: 10, top: 10, bottom: 0};
    const innerWidth = width - margin.left - margin.right;
    const innerHeight = height - margin.top - margin.bottom;
    const numBarsToDisplayThresholdPercent = 0.9;
    const labelPadding = 10;
    const types = ['entity', 'title', 'text'];
    const colors = ['steelblue', 'red', 'orange'];
    const dotRadius = 10;
    const distanceBetweenDots = 25;
    const distanceBetweenLabelAndDot = 20;

    const words = formatBarPlotData(data, type);
    console.log(words);
    const relevantWords = sliceWords(words, numBarsToDisplayThresholdPercent);

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

    const color = d3.scaleOrdinal()
        .domain(types)
        .range(colors);

    // add labels for each bar
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

    // add one dot in the legend for each type
    const legend = svg.append('g')
        .attr('class', 'legend')
        .attr('transform', `translate(${innerWidth - 100},${innerHeight - 100})`)
        .selectAll('myLabels')
        .data(types)
        .enter()
        .append('g');

    legend.append('circle')
        .attr('cy', (d,i) => i * distanceBetweenDots)
        .attr('r', dotRadius)
        .attr('fill', d => color(d));

    legend.append('text')
        .attr('x', distanceBetweenLabelAndDot)
        .attr('y', (d,i) => i * distanceBetweenDots + dotRadius / 2)
        .attr('fill', d => color(d))
        .text(d => d)

    const rect = svg.append('g')
        .attr('transform', `translate(${margin.left},0)`)
        .selectAll('rect')
        .data(relevantWords)
        .join('rect')
        .attr('x', 0)
        .attr('y', (d, i) => y(i))
        .attr('width', d => x(d.value))
        .attr('height', y.bandwidth())
        .attr('fill', d => color(d.type));
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
    const entities = hashmaps.entitySim;
    const text = hashmaps.wordSim;
    const title = hashmaps.titleSim;
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
            console.log(entities);
            addToArray(entities, 'entity', data);
            console.log(data);
            addToArray(text, 'text', data);
            console.log(data);
            addToArray(title, 'title', data);
            console.log(data);
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

