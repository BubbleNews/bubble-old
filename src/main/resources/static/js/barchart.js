export { initializeBarChart, updateBarChart };

//decimal percent of how much cluster to reveal (higher number -> more bars displayed; 1 -> all
// bars displayed)
const numBarsToDisplayThresholdPercent = 0.95;
// maximum number of bars to show (sometimes less bars than this will be shown depending on
// thresholdPercent)
const maxBars = 20;
const margin = {left: 60, right: 100, top: 10, bottom: 0};
const labelPadding = 10;
const types = ['Key Word', 'Title', 'Text'];
const colors = ['steelblue', 'red', 'orange'];
const barHeight = 20;
const dotRadius = 10;
const distanceBetweenDots = 25;
const distanceBetweenLabelAndDot = 20;

const width = 500;
const height = 500;
const innerWidth = width - margin.left - margin.right;
const innerHeight = height - margin.top - margin.bottom;

const color = d3.scaleOrdinal()
    .domain(types)
    .range(colors);

let svgTransformed;

function initializeBarChart(data, id, type) {
    // make svg
    const svg = d3.select("#bar" + id)
        .append("svg")
        .attr("preserveAspectRatio", "xMinYMid")
        .attr("viewBox", [0, 0, width, height])
        .append('g')
        .attr('transform', `translate(${margin.left}, ${margin.top})`);

    // make a legend group
    const legend = svg.append('g')
        .attr('class', 'legend')
        .attr('transform', `translate(${innerWidth - 100},${innerHeight / 2})`)
        .selectAll('myLabels')
        .data(types)
        .enter()
        .append('g')

    // add dots to legend
    legend.append('circle')
        .attr('cy', (d,i) => i * distanceBetweenDots)
        .attr('r', dotRadius)
        .attr('fill', d => color(d));

    // add labels to legend
    legend.append('text')
        .attr('x', distanceBetweenLabelAndDot)
        .attr('y', (d,i) => i * distanceBetweenDots + dotRadius / 2)
        .attr('fill', d => color(d))
        .text(d => d);

    // translate to accommodate margins
    svgTransformed = svg.append('g')
        .attr('class', 'bar-labels')
        .attr('transform', `translate(${margin.left},0)`);

    updateBarChart(data, type);
}

function updateBarChart(data, type) {
    const words = formatBarPlotData(data, type);
    renderBarPlot(words);
}

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
function renderBarPlot(words) {
    const x = d3.scaleLinear()
        .domain([0,d3.max(words.map(d => d.value))])
        .range([0, innerWidth]);

    const y = d3.scaleBand()
        .domain(d3.range(maxBars))
        .range([0, innerHeight])
        .padding(0.1);

    // does the data join with a group
    // TODO: use the new selection.join syntax
    const groups = svgTransformed.selectAll('g')
        .data(words);
    const groupsEnter = groups.enter().append('g');
    groupsEnter.merge(groups)
            .attr('transform', (d, i) => `translate(0,${y(i)})`);

    groups.exit().remove();
    // make bars
    groupsEnter.append('rect')
        .merge(groups.select('rect'))
            .attr('width', d => x(d.value))
            .attr('height', y.bandwidth())
            .attr('fill', d => color(d.type));
    // labels for bars
    groupsEnter.append('text')
        .merge(groups.select('text'))
        .attr('x', -labelPadding)
        .attr('y', y.bandwidth() / 2)
        .attr('text-anchor', 'end')
        .text(d => d.word);
}

/**
 * Only display the appropriate number of bars which is the minimum of:
 * - number of bars needed to sum up to thresholdPercent of the total edge/article/cluster score
 * - maxBars
 * @param words - input array of words
 * @param thresholdPercent -
 * @param maxBars -
 * @returns {*}
 */
function sliceWords(words) {
    let sum = 0;
    words.forEach(w => sum += w.value);
    const threshold = sum * numBarsToDisplayThresholdPercent;

    let numItems = 0;
    let increasingSum = 0;
    while (increasingSum < threshold) {
        increasingSum += words[numItems].value;
        numItems++;
    }


    return words.slice(0, Math.min(numItems, maxBars));
}

/**
 * Convert data from the format received from the Java Handler to arrays for d3
 * @param hashmaps - object received from DetailClusterHandler or EdgeHandler
 * @param type - the kind of words to show. One of: 'all','text','entity','title'
 * @returns []
 */
function formatBarPlotData(hashmaps, type) {
    const entities = hashmaps.entitySim;
    const text = hashmaps.wordSim;
    const title = hashmaps.titleSim;
    const data = [];

    switch (type) {
        case 'Key Word':
            addToArray(entities, type, data);
            break;

        case 'Text':
            addToArray(text, type, data);
            break;

        case 'Title':
            addToArray(title, type, data);
            break;

        default:
            addToArray(entities, 'Key Word', data);
            addToArray(text, 'Text', data);
            addToArray(title, 'Title', data);
    }

    data.sort((a,b) => b.value - a.value);
    return sliceWords(data, maxBars);
}

/**
 * Converts JS object to array of smaller JS objects where each position in the array
 * is a key, value pair of the original map
 * @param map - object to convert
 * @param type - one of: 'all','text','entity','title'
 * @param arr - array to add objects to
 */
function addToArray(map, type, arr) {
    for (let word in map) {
        arr.push({
            type: type,
            word: word,
            value: map[word]
        })
    }
}

