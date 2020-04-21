$(document).ready(() => {
    // get current chart
    getChart(null);
})

function getChart(date) {
    let chartUrl = '/chart';
    // update request url with date if needed
    if (date != null && date != '') {
        chartUrl += '?date=' + date;
    }
    // send get request
    $.get(chartUrl, response => {
        const parsed = JSON.parse(response);
        // TODO: do something with parsed chart response
        console.log(parsed);
    })
}

function getCluster(clusterId) {
    let clusterUrl = '/cluster';
    // add id to cluster base url
    clusterUrl += '?id=' + clusterId;
    // send get request
    $.get(clusterUrl, response => {
        const parsed = JSON.parse(response);
        // TODO: do something with parsed cluster response
        console.log(parsed);
    })
}