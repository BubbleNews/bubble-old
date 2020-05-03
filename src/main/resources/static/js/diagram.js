const MAX_WIDTH = Math.max(1080, window.innerWidth);
const MAX_HEIGHT = 720;
const margin = {top: 40, right: 100, bottom: 40, left: 175, top2: 25, left2: 75, left3: 75, right3: 40, bottom3: 120};

// Assumes the same graph width, height dimensions as the example dashboard. Feel free to change these if you'd like
let graph_1_width = (MAX_WIDTH / 2) - 10, graph_1_height = 250;
let graph_2_width = (MAX_WIDTH / 2) - 10, graph_2_height = 325;
let graph_3_width = MAX_WIDTH / 2, graph_3_height = 655;


let svg1 = d3.select("#chord")
    .append("svg")
    .attr("width", graph_1_width)
    .attr("height", graph_1_height)
    .append("g")
    .attr("transform", `translate(${margin.left}, ${margin.top})`);

function makeChord(parsed) {

    console.log("hello");

    console.log(parsed.edges[0]);

    filterData(data.edges)



            // data = filterDate(data);
            //
            // data = d3.nest()
            //     .key(function(d) {
            //         return new Date(Date.parse(d.date)).getFullYear(); })
            //     .rollup( function(a) {
            //         return a.length})
            //     .entries(data)
            //     .map(function(d) {
            //         return {year: d.key, count: d.value}});
            //
            //
            // let x = d3.scaleLinear()
            //     .domain([d3.min(data, d => d.year), d3.max(data, d => d.year )])
            //     .range([0, graph_1_width - margin.left - margin.right]);
            //
            // let y = d3.scaleLinear()
            //     .domain([d3.max(data, function(d) { return d.count; }), 0])
            //     .range([0, graph_1_height - margin.top - margin.bottom]);
            //
            // svg1.append('g')
            //     .attr("transform", `translate(0, ${graph_1_height - margin.bottom-margin.top})`)
            //     .call(d3.axisBottom(x).tickSize(0).tickPadding(10).tickFormat(d3.format("d")))
            //     .attr("class", "axis");
            //
            //
            // svg1.append('g')
            //     .call(d3.axisLeft(y).tickSize(0).tickPadding(10))
            //     .attr("class", "axis");
            //
            // let points = svg1.selectAll("circle").data(data);
            //
            //
            // points.enter()
            //     .append("circle")
            //     .merge(points)
            //     .attr("fill", "red")
            //     .attr("cx", d => x(d.year))
            //     .attr("cy", d => y(d.count))
            //     .attr("r", 2);
            //
            // svg1.append("text")
            //     .attr("transform", `translate(${(graph_1_width - margin.left - margin.right) / 2},
            //                                     ${(graph_1_height - margin.top - margin.bottom) + 40})`)
            //     .style("text-anchor", "middle")
            //     .text("Year")
            //     .attr("class", "label");
            //
            // svg1.append('g')
            //     .append("text")
            //     // HINT: Place this at the center left edge of the graph
            //     .style("text-anchor", "middle")
            //     .text("Count")
            //     .attr("transform", "rotate(-90)")
            //     .attr("y", -50)
            //     .attr("x", -((graph_1_height-margin.top-margin.bottom)/2))
            //     .attr("class", "label");
            //
            // // Add chart title
            // svg1.append("text")
            //     .attr("transform", `translate(${(graph_1_width - margin.left - margin.right) / 2}, ${-10})`)       // HINT: Place this at the top middle edge of the graph
            //     .style("text-anchor", "middle")
            //     .attr("class", "title")
            //     .text("Football Games Per Year since 1900")
        //});

}




function filterData(data) {
    let data1 = data.map(function(d) {
        return {
            src: d.src.article.id,
            dest: d.dst.article.id,
            distance: 1/d.distance,
        }});


    let data2 = data.map(function(d) {
        let country = d.away_team;
        return {country: country, score: d.away_score,
            opponent: d.home_team, opponent_score: d.home_score,
            year: new Date(Date.parse(d.date)).getFullYear()
        }});
}



function getClusterDetails(clusterId) {
    let clusterUrl = 'api/details';
    // add id to cluster base url
    clusterUrl += '?id=' + clusterId;
    // send get request
    $.get(clusterUrl, response => {
        const parsed = JSON.parse(response);
        console.log(parsed);
        //makeChord(parsed)
    });
}

getClusterDetails(1);

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

getEdgeDetails(1, 343);