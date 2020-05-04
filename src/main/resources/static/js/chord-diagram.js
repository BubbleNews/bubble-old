export { renderChord };

function renderChord(parsed) {
    console.log(parsed);
    const width = 1500
    const height = 700;
    const margin = {top:40, left: 500, right: 500, bottom: 10};
    const innerWidth = width - margin.left - margin.right;
    const innerHeight = height - margin.top - margin.bottom;
    const aspect = width / height;
    const textGap = 20;
    const outerRadius = Math.min(innerWidth, innerHeight) * 0.5;
    const innerRadius = outerRadius - 20;
    const edgeOpacity = 0.67;
    const max = 5/parsed.numVertices;
    const min = .05
    const padAngle = min + (max - min)*parsed.clusterRadius;
    const textWidth = 250;
    let total = 0;
    let i;
    for (i = 0; i < parsed.edges.length; i++) {  //loop through the array
        total += parsed.edges[i].totalDistance;  //Do the math!
    }

    // get data
    const arr = getChordDataMatrix(parsed);
    const titles = arr[0];
    const matrix = arr[1];

    const svg = d3.select("#chords")
        .append("svg")
            .attr("preserveAspectRatio", "xMinYMid")
            //.attr("width", width)
            //.attr("height", height)
            .attr("viewBox", [0, 0, width, height])
        .append('g')
        .attr('transform', `translate(${margin.left + outerRadius},${margin.top + outerRadius})`);

    let tooltip = d3.select("#chords")     // HINT: div id for div containing scatterplot
        .append("div")
        .attr("class", "tooltip")
        .style("opacity", 0);

    const chords = d3.chord()
        .padAngle(padAngle)
        .sortSubgroups(d3.descending)(matrix);

    const arc = d3.arc()
        .innerRadius(innerRadius)
        .outerRadius(outerRadius);

    const color = d3.scaleOrdinal(d3.schemeCategory10);
    const ribbon = d3.ribbon().radius(innerRadius);


    let mouseover = function(d) {
        let color_span = `<span style="color: white;">`;
        let html = `Edge:<br/>
               Distance: ${color_span}${getDistance(d, total, padAngle, parsed.numVertices)}</span>`;

        tooltip.html(html)
            .style("left", `${(d3.event.pageX)}px`)
            .style("top", `${(d3.event.pageY) - margin.top}px`)   // OPTIONAL for students
            .style("box-shadow", `2px 2px 5px red`)
            .style("background-color", "grey")
            .style("text-align", "center")
            .style("font-weight", "bold")
            .transition()
            .duration(200)
            .style("opacity", 0.9)
    };


    // Mouseout function to hide the tool on exit
    let mouseout = function (d) {
        // Set opacity back to 0 to hide
        tooltip.transition()
            .duration(200)
            .style("opacity", 0);
    };

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

    group.selectAll('text')
        .call(wrap, textWidth);

    //make paths between articles
    svg.append('g')
        .attr('fill-opacity', edgeOpacity)
    .selectAll('path')
        .data(chords)
        .join('path')
        .attr('d', ribbon)
        .attr('fill', d => interpolateColor(d, color))
        .attr('stroke', d => d3.rgb(color(d.target.index)).darker())
        .on("mouseover", function(d) {
            mouseover(d);
            d3.select(this)
               .attr("fill", "red")
                .attr("stroke", "red");
        })
        .on("mouseout", function(d) {
            mouseout(d);
            d3.select(this)
                .attr("fill", d => interpolateColor(d, color))
                .attr("stroke", d => d3.rgb(color(d.target.index)).darker());
        })

    // TODO: Get resizing working
    window.onresize = () => {
        console.log('test')
        const targetWidth = (window.innerWidth < width) ? window.innerWidth : width;
        d3.select(".chord-chart")
            .attr("width", targetWidth)
            .attr("height", targetWidth / aspect);
    }
}



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

function getHeight(d, lines) {
    console.log("height" + lines)
        if (Math.cos(d.angle) > 0) {
            return -12 * Math.cos(d.angle) * (lines);
        } else {
            return -20 * Math.cos(d.angle);
        }
    }


function getWidth(d, lines, textWidth) {
    console.log("width" + lines);
    if (Math.cos(d.angle) > 0) {
        return Math.sin(d.angle)*(textWidth/2) + Math.sin(d.angle)*Math.cos(d.angle)*15*(lines);
    } else {
        return Math.sin(d.angle) * (textWidth / 2) - + Math.sin(d.angle)*Math.cos(d.angle)*3;
    }
}


function getTextShift(d, width) {
    let shift = width/2;
    if (d.angle > Math.PI) {
        return -shift;
    }
    return shift;
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

function getDistance(d, total, pad, num) {
    console.log(d);
    console.log(total);
    const toReturn = ((2* (d.source.endAngle - d.source.startAngle))*total/(2*Math.PI - num*pad)).toFixed(2);
    console.log(toReturn);
    return toReturn;
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