import { renderChord } from './chord-diagram.js';
import { renderBarPlot, setDataStuff, reRender } from './barchart.js';

// assigns on click functionality to buttons
document.getElementsByClassName("btn btn-info")[0].addEventListener("click", () => {
    getClusterDetails(2,'entity')});
document.getElementsByClassName("btn btn-info")[1].addEventListener("click", () => {
    getClusterDetails(2,'text')});
document.getElementsByClassName("btn btn-info")[2].addEventListener("click", () => {
    getClusterDetails(2,'title')});
document.getElementsByClassName("btn btn-info")[3].addEventListener("click", () => {
    getClusterDetails(2,'all')});

function getClusterDetails(clusterId, type) {
    let clusterUrl = 'api/details';
    // add id to cluster base url
    clusterUrl += '?id=' + clusterId;
    // send get request
    $.get(clusterUrl, response => {
        const parsed = JSON.parse(response);
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
        setDataStuff(parsed.edge, 'all');
    });
}

function render(data, type) {
    console.log('test');
    setDataStuff(data, type);
    reRender();

    // renderChord(data);
}

getClusterDetails(2);
// getEdgeDetails(20, 61);
