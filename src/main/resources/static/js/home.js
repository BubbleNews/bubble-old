let currentlyOpenClusterId = null;

const sourceMap = new Map()

$(document).ready(() => {

    /* startup particlejs */
    particlesJS.load('particles-js', '../json/particles.json', function() {
        console.log('callback - particles.js config loaded');
    });
    // add date selector
    addDate();
    // add on click to date button
    $('#dateButton').click(function() {
        dateClickHandler(new Date());
    });
    // get current chart
    getChart(new Date());

    $('.sourceToggle').click(function() {
        const button = $(this);
        const cleanSource = cleanSourceName(button.text());
        // check if source currently being shown or not
        if (sourceMap.get(cleanSource)) {
            // filter out source
            $('.' + cleanSource).hide();
            sourceMap.set(cleanSource, false);
            button.css("background-color", "palevioletred");
        } else {
            // show results from source
            $('.' + cleanSource).show();
            sourceMap.set(cleanSource, true);
            button.css("background-color", "lightgreen");
        }
    });

    $('.sourceToggle').each(function(index, element) {
        const source = cleanSourceName($(this).text());
        sourceMap.set(source, true);
    });
});



function addDate() {
    const today = new Date().toISOString().split('T')[0];
    $('#date').val(today);
}

function dateClickHandler() {
    const dateVal = $('#date').val();
    const dateArr = dateVal.split('-').map(x => parseInt(x));
    const date = new Date(dateArr[0], dateArr[1] - 1, dateArr[2]);
    console.log(date);
    // check if date is later than today
    if (date > new Date()) {
        alert('Cannot view news from the future.')
        return;
    } else {
        $("#clusters").empty();
        getChart(date);
    }
}

function stringifyDate(date) {
    const originalMonth = date.getMonth() + 1;
    const month = (originalMonth < 10) ? '0' + originalMonth: originalMonth;
    return date.getFullYear() + '-' + month + '-' + date.getDate();
}

function getChart(date) {
    // clear messages
    $('.message').hide();
    const dateStr = stringifyDate(date);
    console.log(dateStr);
    let chartUrl = 'api/chart';
    let colors = ["LightCoral", "LightCyan", "LightGreen", "LightYellow", "LightSalmon", "LightPink",
    "LightSkyBlue", "LavenderBlush", "LightSeaGreen"];
    // update request url with date if needed
    chartUrl += '?date=' + dateStr;
    // send get request
    $.get(chartUrl, function(data) {
        const parsed = JSON.parse(data);
        // TODO: do something with parsed chart response
        const clusters = parsed.clusters;

        if (clusters.length == 0) {
            $('#chartMessage').empty();
            $('#chartMessage').append('<p>No data found for this date.</p>');
            $('#chartMessage').show();
            return;
        }

        let colorInd = 0;
        let i;
        for (i = 0; i < clusters.length; i++) {
            if (colorInd >= colors.length) {
                colorInd = 0;
            }
            appendCluster(clusters[i], colors[colorInd]);
            colorInd++;
        }
    })
}

function appendCluster(cluster, color) {
    const classNum = Math.floor(Math.random() * 4);
    const clusterHtml =
        "<div id=" + cluster.clusterId
        + " class='cluster cluster" + classNum + "'"
        + "style='background-color:" + color + ";'>"
        + "<h2>" + cluster.headline
        + "<div class='clusterSize'>" + cluster.size + "</div></h2>"
        + "</div>";
    $('#clusters').append(clusterHtml);
    // add a click function to get clusters
    $('#' + cluster.clusterId).click(function() {
        getCluster(cluster.clusterId);
    })
}

function getCluster(clusterId) {
    $('.articlesWrapper').remove();
    if (clusterId == currentlyOpenClusterId) {
        currentlyOpenClusterId = null;
        return;
    }
    let clusterUrl = 'api/cluster';
    // add id to cluster base url
    clusterUrl += '?id=' + clusterId;
    // send get request
    $.get(clusterUrl, response => {
        const parsed = JSON.parse(response);
        // TODO: do something with parsed cluster response
        const divId = clusterId + 'articles';
        const articlesHtml = '<div id="' + divId + '" class="articlesWrapper"></div>';
        $('#' + clusterId).append(articlesHtml);
        const articles = parsed.articles;
        let i;
        for (i = 0; i < articles.length; i++) {
            const article = articles[i];
            let cleanSource = cleanSourceName(article.sourceName);
            let articleHTML = '<div id="' + divId + i  + '" class="article ' + cleanSource + '"';
            if (!sourceMap.get(cleanSource)) {
                articleHTML += "style='display: none;'";
            }
            articleHTML += '> <h3><a href="' + article.url + '" target="_blank">'
                + article.title + '</a></h3>'
                + '<div class="sourceDate"><h3>' + article.sourceName + ' | '
                + article.timePublished.slice(0, 16) + ' UTC</h3></div></div>';
            $('#' + clusterId + 'articles').append(articleHTML);
        }
        currentlyOpenClusterId = clusterId;
    });
}

function cleanSourceName(sourceName) {
    return sourceName.replace(/[ .,\/#!$%\^&\*;:{}=\-_`~()]/g,"");
}


