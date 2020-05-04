import { renderChord } from './chord-diagram.js';
import { renderBarPlot } from './barchart.js';

// assigns on click functionality to buttons
document.getElementsByClassName("btn btn-info")[0].addEventListener("click", () => {
    setData('winPercentage')});
document.getElementsByClassName("btn btn-info")[1].addEventListener("click", () => {
    setData('numWins')});
document.getElementsByClassName("btn btn-info")[2].addEventListener("click", () => {
    setData('numGames')});

function getClusterDetails(clusterId) {
    let clusterUrl = 'api/details';
    // add id to cluster base url
    clusterUrl += '?id=' + clusterId;
    // send get request
    $.get(clusterUrl, response => {
        const parsed = JSON.parse(response);
        render(parsed);
    });
}

function getEdgeDetails(id1, id2) {
    let clusterUrl = 'api/edge';
    // add id to cluster base url
    clusterUrl += '?id1=' + id1 + '&id2=' + id2;
    // send get request
    $.get(clusterUrl, response => {
        const parsed = JSON.parse(response);
        // renderBarPlot(parsed);
    });
}

function render(data) {
    renderBarPlot(data);
    renderChord(data);
}

getClusterDetails(7);
getEdgeDetails(20, 8);