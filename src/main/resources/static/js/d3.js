import { renderChord } from './chord-diagram.js';
import { renderBarPlot, setDataStuff, updateDataAndRender } from './barchart.js';
export { getEdgeDetails};

export function getClusterDetails(clusterId, type) {

    $('.entityBut' + clusterId).click(function() {
        updateDataAndRender('entity')});
    $('.textBut' + clusterId).click(function() {
        updateDataAndRender('text')});
    $('.titleBut' + clusterId).click(function() {
        updateDataAndRender('title')});
    $('.allBut' + clusterId).click(function() {
        updateDataAndRender('all')});

    let clusterUrl = 'api/details';
    // add id to cluster base url
    clusterUrl += '?id=' + clusterId;
    // send get request
    $.get(clusterUrl, response => {
        const parsed = JSON.parse(response);
        renderChord(parsed);
        render(parsed, type);
    });
}

function getEdgeDetails(id1, id2) {
    let clusterUrl = 'api/edge';
    // add id to cluster base url
    clusterUrl += '?id1=' + id1 + '&id2=' + id2;
    // send get request
    $.get(clusterUrl, response => {
        const parsed = JSON.parse(response);
        render(parsed.edge, 'all');
    });
}

function render(data, type) {
    setDataStuff(data, type);
    // renderChord(data);
}

getClusterDetails(2);
getEdgeDetails(20, 61);

getEdgeDetails(1, 2);
