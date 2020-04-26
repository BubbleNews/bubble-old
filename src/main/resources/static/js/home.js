$(document).ready(() => {
    // get current chart
    getChart();
})

function getChart(date) {
    let chartUrl = 'api/chart';
    // update request url with date if needed
    if (date != null && date != '') {
        chartUrl += '?date=' + date;
    }
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
        + " class='cluster" + classNum + "' style='height: "
        + 15*cluster.size + "px;'>"
        + "<p>" + cluster.headline + "</p>"
        + "</div>";
    $('#clusters').append(clusterHtml)
    // add a click function to get clusters
    $('#' + cluster.clusterId).click(function() {
        getCluster(cluster.clusterId);
        // mock alert
        let articles = '';
        let i;
        for (i = 0; i < cluster.size; i++) {
            articles += 'Article ' + i + '\n';
        }
        console.log(articles);
    })
}

function getCluster(clusterId) {
    let clusterUrl = 'api/cluster';
    // add id to cluster base url
    clusterUrl += '?id=' + clusterId;
    // send get request
    $.get(clusterUrl, response => {
        const parsed = JSON.parse(response);
        // TODO: do something with parsed cluster response
        console.log(parsed);
    })
}

