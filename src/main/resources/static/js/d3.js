import { renderChord } from './chord-diagram.js';
import { renderBarPlot, setDataStuff, updateDataAndRender, renderFirst } from './barchart.js';
export { getEdgeDetails};

export function getClusterDetails(clusterId, clusterMeanRadius, articleIds, type) {

    $('.entityBut' + clusterId).click(function() {
        updateDataAndRender('entity')});
    $('.textBut' + clusterId).click(function() {
        updateDataAndRender('text')});
    $('.titleBut' + clusterId).click(function() {
        updateDataAndRender('title')});
    $('.allBut' + clusterId).click(function() {
        updateDataAndRender('all')});

    const serializedArticleIds = JSON.stringify(articleIds);
    const serializedClusterParams = $('#reclusterParams').serialize();

    let clusterUrl = 'api/details';
    // add id to cluster base url
    clusterUrl += '?clusterId=' + clusterId;
    clusterUrl += '&clusterMeanRadius=' + clusterMeanRadius;
    clusterUrl += '&articleIds=' + serializedArticleIds;
    clusterUrl += '&' + serializedClusterParams;
    // send get request
    $.get(clusterUrl, response => {
        const parsed = JSON.parse(response);
        render(parsed, type);
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
        setDataStuff(parsed.edge, 'all');
    });
}

function render(data, type) {
    console.log('hello');
    renderChord(data);
    renderFirst(data, data.clusterId, type);
    //setDataStuff(data, type);
    // renderChord(data);
}

// getClusterDetails(2);
// getEdgeDetails(20, 61);
//
// getEdgeDetails(1, 2);
