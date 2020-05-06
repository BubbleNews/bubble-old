import { renderChord } from './chord-diagram.js';
import { updateBarChart, initializeBarChart } from './barchart.js';
export { getEdgeDetails, getClusterDetails};

let clusterIdState;
let clusterData;
let edgeData;

function getClusterDetails(clusterId, clusterMeanRadius, meanRadiusMap, articleIds, callback) {
    // this will be used to select the appropriate svg elements
    clusterIdState = clusterId;

    // set up buttons
    $('.entityBut' + clusterId).click(() => updateDashboardByType('entity'));
    $('.textBut' + clusterId).click(() => updateDashboardByType('text'));
    $('.titleBut' + clusterId).click(() => updateDashboardByType('title'));
    $('.allBut' + clusterId).click(() => updateDashboardByType('all'));

    const serializedArticleIds = JSON.stringify(articleIds);
    const serializedClusterParams = $('#reclusterParams').serialize();


    const clusterUrl = `api/details?clusterId=${clusterId}&clusterMeanRadius=${clusterMeanRadius}
        &articleIds=${serializedArticleIds}&${serializedClusterParams}`;

    clusterIdState = clusterId;
    // send get request
    $.get(clusterUrl, response => {
        clusterData = JSON.parse(response);
        renderChord(clusterData, meanRadiusMap);
        initializeBarChart(clusterData, clusterIdState, 'all');
    });
}

function getEdgeDetails(id1, id2) {
    let clusterUrl = 'api/edge';
    const serializedClusterParams = $('#reclusterParams').serialize();

    // add id to cluster base url
    clusterUrl += '?id1=' + id1 + '&id2=' + id2;
    clusterUrl += '&' + serializedClusterParams;
    // send get request
    $.get(clusterUrl, response => {
        const parsed = JSON.parse(response);
        edgeData = parsed.edge;

        updateBarChart(parsed.edge, 'all');
    });
}

function updateDashboardByType(type) {
    updateBarChart(clusterData, type);
}