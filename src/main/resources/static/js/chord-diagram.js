import { getEdgeDetails } from './d3.js';
export { renderChord };


// map from article ID -> index in matrix array
let indices = {};


/**
 * Render the chord diagram
 * @param parsed - data includes list of edges, number of nodes, cluster percentile
 */
function renderChord(parsed) {
    console.log(parsed);
    const margin = {top:70, left: 500, right: 500, bottom: 10};
    const width = 1500
    const height = 700;
    const innerWidth = width - margin.left - margin.right;
    const innerHeight = height - margin.top - margin.bottom;
    const aspect = width / height;
    const textGap = 20;
    const outerRadius = Math.min(innerWidth, innerHeight) * 0.5;
    const innerRadius = outerRadius - 20;
    const edgeOpacity = 0.67;
    const max = 5/parsed.numVertices;
    const min = .05
    const padAngle = min + (max - min) * parsed.clusterRadius;
    const textWidth = 125;
    let total = 0;
    let i;
    for (i = 0; i < parsed.edges.length; i++) {  //loop through the array
        total += parsed.edges[i].totalDistance;  //Do the math!
    }

    // turns data into a matrix
    const arr = getChordDataMatrix(parsed);
    const titles = arr[0];
    const matrix = arr[1];

    // Creates svg for chord diagram
    const svg = d3.select("#chord" + parsed.clusterId)
        .append("svg")
            .attr("id", "mysvg")
            .attr("preserveAspectRatio", "xMinYMid")
            //.attr("width", width)
            //.attr("height", height)
            .attr("viewBox", [0, 0, width, height])
        .append('g')
        .attr('transform', `translate(${margin.left + outerRadius},${margin.top + outerRadius})`);

    //
    const chords = d3.chord()
        .padAngle(padAngle)
        .sortSubgroups(d3.descending)(matrix);

    //
    const arc = d3.arc()
        .innerRadius(innerRadius)
        .outerRadius(outerRadius);

    //
    const color = d3.scaleOrdinal(d3.schemeCategory10);
    const ribbon = d3.ribbon().radius(innerRadius);

    //
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
        .attr('text-anchor', "middle")
        .append('text')
            .text(d => d.title);

    // Wraps text to a specific text width
    group.selectAll('text')
        .call(wrap, textWidth);

    //make paths between articles
    //initializes mouse events to turn red on hover, and update bar plot on click
    svg.append('g')
        .attr('fill-opacity', edgeOpacity)
    .selectAll('path')
        .data(chords)
        .join('path')
        .attr('d', ribbon)
        .attr('fill', d => interpolateColor(d, color))
        .attr('stroke', d => d3.rgb(color(d.target.index)).darker())
        .on("mouseover", function(d) {
            d3.select(this)
               .attr("fill", "red")
                .attr("stroke", "red");
        })
        .on("mouseout", function(d) {
            d3.select(this)
                .attr("fill", d => interpolateColor(d, color))
                .attr("stroke", d => d3.rgb(color(d.target.index)).darker());
        })
        .on("click", d => getEdge(d));
}

/**
 * Finds the article ids from a chord
 * @param d - data representing a chord
 */
function getEdge(d) {
    let a1, a2;
    for (let key in indices) {
        if (indices[key] === d.source.index) {
            a1 = key
        } else if (indices[key] === d.target.index) {
            a2 = key
        }
    }
    getEdgeDetails(a1,a2);
}


/**
 * Function to wrap text
 * @param text - the text to wrap
 * @param width - width of text after wrapping
 */
function wrap(text, width) {
    text.each(function() {
        var text = d3.select(this),
            words = text.text().split(/\s+/).reverse(),
            word,
            lines = 1,
            line = [],
            lineHeight = 1.1, // ems
            y = text.attr("y"),
            dy = parseFloat(text.attr("dy")),
            tspan = text.text(null).append("tspan").attr("x", 0).attr("y", y).attr("dy", 0 + "em");

        // while words remain, test to see if line is longer than width, if so start new line
        while (word = words.pop()) {
            line.push(word);
            tspan.text(line.join(" "));
            if (tspan.node().getComputedTextLength() > width) {
                line.pop();
                tspan.text(line.join(" "));
                lines++;
                line = [word];
                tspan = text.append("tspan").attr("x", 0).attr("y", y).attr("dy", lineHeight + "em").text(word);
            }
        }
        text.attr('transform', d => `translate(${getWidth(d, lines, width)},${getHeight(d, lines)})`);
    });
}

/**
 * finds the height to display the article height
 * @param d - the article chord data
 * @param lines - number of lines in the title
 * @returns {number}
 */
function getHeight(d, lines) {
        if (Math.cos(d.angle) > 0) {
            return -12 * Math.cos(d.angle) * (lines);
        } else {
            return -20 * Math.cos(d.angle);
        }
    }

/**
 * finds the width to display the article height
 * @param d - the article chord data
 * @param lines - number of lines in the title
 * @param textWidth - the width of the text
 * @returns {number}
 */
function getWidth(d, lines, textWidth) {
    if (Math.cos(d.angle) > 0) {
        return Math.sin(d.angle)*(textWidth/2) + Math.sin(d.angle)*Math.cos(d.angle)*15*(lines);
    } else {
        return Math.sin(d.angle) * (textWidth / 2) - + Math.sin(d.angle)*Math.cos(d.angle)*3;
    }
}

/**
 *
 * @param d
 * @param titles
 * @returns {{angle: number, title: *}[]}
 */
function labels(d, titles) {
    const midAngle = (d.startAngle + d.endAngle) / 2;
    return [{
        angle: midAngle,
        title: titles[d.index]
    }];
}

/**
 * function to find the color of the chords halfway between the nodes
 * @param d - data
 * @param color - colorScheme
 * @returns {*}
 */
function interpolateColor(d, color) {
    const srcIndex = d.source.index;
    const targetIndex = d.target.index;
    const srcColor = color(srcIndex);
    const targetColor = color(targetIndex);
    return d3.interpolateRgb(srcColor, targetColor)(0.5);
}

/**
 * Returns the distance of a chord in Similarity units - possibly for a tooltip
 * @param d
 * @param total
 * @param pad
 * @param num
 */
function getDistance(d, total, pad, num) {
    console.log(d);
    console.log(total);
    const toReturn = ((2* (d.source.endAngle - d.source.startAngle))*total/(2*Math.PI - num*pad)).toFixed(2);
    console.log(toReturn);
    return toReturn;
}

/**
 * Finds the matrix of edge weight values for the chords
 * @param parsed
 * @returns {[][]}
 */
function getChordDataMatrix(parsed) {
    indices = {};
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

/**
 * Makes a box plot of the mean radius of clusters
 * @param data - the data to of clusters with radius
 * @param point - the radius of current cluster
 */
function boxPlot(data, point) {
    const margin = {top:40, left: 50, right: 50, bottom: 10};
       const width = 400 - margin.left -margin.right;
       const height = 150 - margin.top - margin.bottom;

// append the svg object to the body of the page
    let svg = d3.select("#box2")
        .append("svg")
        .attr("width", width + margin.left + margin.right)
        .attr("height", height + margin.top + margin.bottom)
        .append("g")
        .attr("transform",
            "translate(" + margin.left + "," + margin.top + ")");

    console.log(data);

// Compute summary statistics used for the box:
    let sumstat = d3.nest() // nest function allows to group the calculation per level of a factor
        .key(d => "true")
        .rollup(function(d) {
            const q1 = d3.quantile(d.map(function(g) { return g.radius;}).sort(d3.ascending),.25)
            const median = d3.quantile(d.map(function(g) { return g.radius;}).sort(d3.ascending),.5)
            const q3 = d3.quantile(d.map(function(g) { return g.radius;}).sort(d3.ascending),.75)
            const interQuantileRange = q3 - q1
            const min = d3.min(d, a => a.radius);
            const max = d3.max(d, a => a.radius);
            return({q1: q1, median: median, q3: q3, interQuantileRange: interQuantileRange, min: min, max: max})
        })
        .entries(data);

    console.log("hi");
    console.log(sumstat);

    // Show the Y scale
    const y = d3.scaleBand()
        .range([ height, 0 ])
        .domain(["similarities"])
        .padding(.4);

    // Show the X scale
    const x = d3.scaleLinear()
        .domain([d3.min(data, d => d.radius) - 0.1, d3.max(data, d => d.radius ) + 0.1])
        .range([0, width]);


    // Add X axis label:
    svg.append("text")
        .attr("text-anchor", "left")
        .attr("x", 0)
        .attr("y", margin.top - 20)
        .text("Mean Radius of Clusters");

    // legend
    svg.append("text")
        .attr("text-anchor", "end")
        .attr("x", width)
        .attr("y", height + 6)
        .text("This Cluster");

    // legend dot
    svg.append("circle")
        .attr("cx", width - 90)
        .attr("cy", height)
        .attr("r", 6)
        .style("fill", "red");

    // the two main horizontal lines
    svg
        .selectAll("vertLines")
        .data(sumstat)
        .enter()
        .append("line")
        .attr("x1", function(d){return(x(d.value.min))})
        .attr("x2", function(d){return(x(d.value.q1))})
        .attr("y1", function(d){return(margin.top + y.bandwidth()/2)})
        .attr("y2", function(d){return(margin.top + y.bandwidth()/2)})
        .attr("stroke", "black")
        .style("width", 40);

    svg
        .selectAll("vertLines")
        .data(sumstat)
        .enter()
        .append("line")
        .attr("x1", function(d){return(x(d.value.q3))})
        .attr("x2", function(d){return(x(d.value.max))})
        .attr("y1", function(d){return(margin.top + y.bandwidth()/2)})
        .attr("y2", function(d){return(margin.top + y.bandwidth()/2)})
        .attr("stroke", "black")
        .style("width", 40);

    // rectangle for the main box

    console.log(x(sumstat[0].value.q3))//-x(sumstat[0].value.q1))
    svg
        .selectAll("boxes")
        .data(sumstat)
        .enter()
        .append("rect")
        .attr("x", function(d){return(x(d.value.q1))}) // console.log(x(d.value.q1)) ;
        .attr("width", function(d){ return(x(d.value.q3)-x(d.value.q1))}) //console.log(x(d.value.q3)-x(d.value.q1))
        .attr("y", margin.top)
        .attr("height", y.bandwidth())
        .attr("stroke", "black")
        .style("fill", "#69b3a2")
        .style("opacity", 0.3);

    // Show the median line
    svg
        .selectAll("medianLines")
        .data(sumstat)
        .enter()
        .append("line")
        .attr("y1", function(d){return(margin.top)})
        .attr("y2", function(d){return(margin.top + y.bandwidth())})
        .attr("x1", function(d){return(x(d.value.median))})
        .attr("x2", function(d){return(x(d.value.median))})
        .attr("stroke", "black")
        .style("width", 80);


    // Add individual points with jitter
    let jitterWidth = y.bandwidth()/1.5;

    // plot the points
    svg
        .selectAll("indPoints")
        .data(data)
        .enter()
        .append("circle")
        .merge(svg)
        .attr("cx", function(d){ return(x(d.radius))})
        .attr("cy", function(d){ return(  (margin.top + y.bandwidth()/2) - jitterWidth/2 + Math.random()*jitterWidth )})
        .attr("r", 4)
        .style("fill", "black")
        .attr("stroke", "black");

    // plot the current cluster
    svg
        .selectAll("#cluster_dot")
        .enter()
        .append("circle")
        .merge(svg)
        .attr("id", "cluster_dot")
        .attr("cx", x(point.radius))
        .attr("cy", margin.top + y.bandwidth()/2)
        .attr("r", 6)
        .style("fill", "red");
}

boxPlot([{radius: 5}, {radius: 6}, {radius: 7}, {radius: 5.3}, {radius: 6.6}, {radius: 7.2}, {radius: 4}, {radius: 6.5}, {radius: 7.9}, {radius: 5.1}, {radius: 5.8}, {radius: 7.5}], {radius: 6.5});
//
// setTimeout(function () {
//     boxPlot([{radius: 5}, {radius: 6}, {radius: 7}, {radius: 5.3}, {radius: 6.6}, {radius: 7.2}, {radius: 4}, {radius: 6.5}, {radius: 7.9}, {radius: 5.1}, {radius: 5.8}, {radius: 7.5}], {radius: 6.5});
//
// }, 2000);