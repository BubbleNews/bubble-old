let currentlyOpenClusterId = null;

$(document).ready(() => {
    // add date selector
    addDate();
    // add on click to date button
    $('#dateButton').click(function() {
        dateClickHandler(new Date());
    });
    // get current chart
    getChart(new Date());
});

function addDate() {
    const today = new Date().toISOString().split('T')[0];
    const dateHtml = '<input type="date" id="date" name="trip-start"'
        + ' value="' + today + '">';
    $('#dateWrapper').prepend(dateHtml);
}

function dateClickHandler() {
    const date = new Date($('#date').val());
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
    const dateStr = stringifyDate(date);
    let chartUrl = 'api/chart';
    // update request url with date if needed
    chartUrl += '?date=' + dateStr;
    // send get request
    $.get(chartUrl, function(data) {
        const parsed = JSON.parse(data);
        // TODO: do something with parsed chart response
        const clusters = parsed.clusters;
        let i;
        for (i = 0; i < clusters.length; i++) {
            appendCluster(clusters[i]);
        }
    })
}

function appendCluster(cluster) {
    const classNum = Math.floor(Math.random() * 4);
    const clusterHtml =
        "<div id=" + cluster.clusterId
        + " class='cluster cluster" + classNum + "'>"
        + "<p>" + cluster.headline + "</p>"
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
            const article = articles[i]
            const articleHTML = '<div id="' + divId + i  + '" class="article">'
                + ' <a href="' + article.url + '" target="_blank">'
                + article.title + '</a></div>';
            $('#' + clusterId + 'articles').append(articleHTML);
        }
        currentlyOpenClusterId = clusterId;
    });
}


