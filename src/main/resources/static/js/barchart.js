export { renderBarPlot, setDataStuff, reRender };

const numBarsToDisplayThresholdPercent = 0.95;
const maxBars = 20;
const margin = {left: 60, right: 10, top: 10, bottom: 0};
const labelPadding = 10;
const types = ['entity', 'title', 'text'];
const colors = ['steelblue', 'red', 'orange'];
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

const svg = d3.select(".bar-chart")
    .append("svg")
        .attr('width', width)
        .attr('height', height)
        .append('g')
        .attr('transform', `translate(${margin.left}, ${margin.top})`)
    .append('g')
        .attr('class', 'bar-labels')
        .attr('transform', `translate(${margin.left},0)`);

let dataouter;
let words;
let relevantWords;

function setDataStuff(data, type) {
    dataouter = data;
    words = formatBarPlotData(data, type);
    relevantWords = sliceWords(words, numBarsToDisplayThresholdPercent, maxBars);
    renderBarPlot();
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
function renderBarPlot() {
    const x = d3.scaleLinear()
        .domain(d3.extent(words.map(d => d.value)))
        .range([0, innerWidth]);

    const y = d3.scaleBand()
        .domain(d3.range(relevantWords.length))
        .range([0, innerHeight])
        .padding(0.1);

    console.log(relevantWords);
    // add labels for each bar
    svg.selectAll('rect')
        .data(relevantWords)
        .join('rect'
            // enter => {
            //     enter.append('g').attr('class', 'test123').call(
            //         gbar => gbar.append('rect')
            //             .attr('x', 0)
            //             .attr('y', (d, i) => y(i))
            //             .attr('width', d => x(d.value))
            //             .attr('height', y.bandwidth())
            //             .attr('fill', d => color(d.type))
            //     );
            // }
        )
        .attr('x', 0)
        .attr('y', (d, i) => y(i))
        .attr('width', d => x(d.value))
        .attr('height', y.bandwidth())
        .attr('fill', d => color(d.type));
            // .attr('y', (d, i) => y(i))
            // .attr('text-anchor', 'end')
            // .attr('dy', y.bandwidth()/2)
            // .attr('dx', -labelPadding)
            // .text(d => d.word);

    // // make bars
    // const rect = svg.append('g')
    //     .attr('transform', `translate(${margin.left},0)`)
    //     .selectAll('rect')
    //     .data(relevantWords)
    //     .join('rect')
    //     ;

    // make a legend group
    const legend = svg.append('g')
        .attr('class', 'legend')
        .attr('transform', `translate(${innerWidth - 100},${innerHeight - 100})`)
        .selectAll('myLabels')
        .data(types)
        .enter()
        .append('g')
        .on('click', d => {
            console.log(d);
            setDataStuff(dataouter, d);
        });

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
        .text(d => d)
}

function sliceWords(words, thresholdPercent, maxBars) {
    let sum = 0;
    words.forEach(w => sum += w.value);
    const threshold = sum * thresholdPercent;

    let numItems = 0;
    let increasingSum = 0;
    while (increasingSum < threshold) {
        increasingSum += words[numItems].value;
        numItems++;
    }

    return words.slice(0, Math.min(numItems, maxBars));
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
            addToArray(entities, 'entity', data);
            addToArray(text, 'text', data);
            addToArray(title, 'title', data);
    }

    data.sort((a,b) => b.value - a.value);
    return data;
}

function addToArray(map, type, arr) {
    for (let word in map) {
        arr.push({
            type: type,
            word: word,
            value: map[word]
        })
    }
}

function reRender() {
    relevantWords.pop();
    setTimeout(() => {
        renderBarPlot()
    }, 1000);
}

