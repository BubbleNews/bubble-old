function getClusterDetails(clusterId) {
    let clusterUrl = 'api/details';
    // add id to cluster base url
    clusterUrl += '?id=' + clusterId;
    // send get request
    $.get(clusterUrl, response => {
        const parsed = JSON.parse(response);
        console.log(parsed);
    });
}

getClusterDetails(3);