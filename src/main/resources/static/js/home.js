import {getClusterDetails} from './d3.js';
let currentlyOpenClusterId = null;

const sourceMap = new Map();

const clusterMap = new Map();

$(document).ready(() => {

    /* startup particlejs */
    particlesJS.load('particles-js', '../json/particles.json', function() {
        console.log('callback - particles.js config loaded');
    });

    // add date selector
    addDate();
    // // add on click to date button
    $('#dateButton').click(function() {
        dateClickHandler();
    });


    // get current chart
    getChart(new Date());

    $('#mainLoader').hide();

    $('.sourceToggle').click(function() {
        const button = $(this);
        const cleanSource = cleanSourceName(button.text());
        // check if source currently being shown or not
        if (sourceMap.get(cleanSource)) {
            // filter out source
            $('.' + cleanSource).hide();
            sourceMap.set(cleanSource, false);
            button.removeClass("btn-success");
            button.addClass("btn-danger");
        } else {
            // show results from source
            $('.' + cleanSource).show();
            sourceMap.set(cleanSource, true);
            button.removeClass("btn-danger");
            button.addClass("btn-success");
        }
    });
    // add form submit
    $('#reclusterParams').submit(function(event) {
            event.preventDefault();
            $("#clusters").empty();
            clusterMap.clear();
            $('#mainLoader').show();
            let reclusterEndpoint = 'api/recluster';
            const date = new Date($('#date').val());
            const isToday = isDateToday(date);
            const year = date.getFullYear();
            const originalMonth = date.getMonth() + 1;
            const newMonth = (originalMonth < 10) ? '0' + originalMonth: originalMonth;
            const originalDay = date.getDate();
            const newDay = (originalMonth < 10) ? '0' + originalDay: originalDay;
            const offset = date.getTimezoneOffset() / 60;
            const serializedClusterParams = $('#reclusterParams').serialize();

            reclusterEndpoint += `?year=${year}&month=${newMonth}&day=${newDay}
                &offset=${offset}&isToday=${isToday}&${serializedClusterParams}`;

            $.get(reclusterEndpoint, function(data) {
                const parsed = JSON.parse(data);
                $('#mainLoader').hide();
                console.log(parsed);
                $('#chartMessage').hide();
                if (parsed.clusters.length == 0) {
                    $('#chartMessage').empty();
                    $('#chartMessage').append('<div class="alert alert-danger"><h2>No' +
                        ' clusters found with these parameters</h2></div>');
                    $('#chartMessage').show();
                }
                let i;
                for (i = 0; i < parsed.clusters.length; i++) {
                    appendCluster(parsed.clusters[i], true);
                }
            });
    });

    $('.sourceToggle').each(function(index, element) {
        const source = cleanSourceName($(this).text());
        sourceMap.set(source, true);
    });

    $('#resetButton').click(function() {
        addDate();
        dateClickHandler();
    });
});


function addDate() {
    const today = new Date().toLocaleDateString()
    $('#date').val(today);
}

$('#date').datepicker();

function dateClickHandler() {
    let dateVal = $('#date').val();
    const dateDashed = dateVal.split("-");
    if (dateDashed.length === 3) {
        const dateString = dateDashed[1] + '/' + dateDashed[2] + '/' + dateDashed[0]
        $('#date').val(dateString);
        dateVal = dateString;
    }
    const dateArr = dateVal.split('/').map(x => parseInt(x));
    const date = new Date(dateArr[2], dateArr[0] - 1, dateArr[1]);
    console.log(date);
    // check if date is later than today
    $("#chartMessage").hide();
    if (date > new Date()) {
        $("#clusters").empty();
        clusterMap.clear();
        $('#chartMessage').empty();
        $('#chartMessage').append('<div class="alert alert-danger"><h2>Cannot find articles from' +
            ' the future.</h2></div>');
        $('#chartMessage').show();
        return;
    } else {
        getChart(date);
    }
}
// function stringifyDate(date) {
//     const originalMonth = date.getMonth() + 1;
//     const month = (originalMonth < 10) ? '0' + originalMonth: originalMonth;
//     const originalDay = date.getDate();
//     const day = (originalDay < 10) ? '0' + originalDay: originalDay;
//     return date.getFullYear() + '-' + month + '-' + day;
// }

function isDateToday(date) {
    const today = new Date();
    return date.getDate() === today.getDate() && date.getMonth() === today.getMonth() && date.getFullYear() === date.getFullYear();
}

function getChart(date) {
    $("#clusters").empty();
    clusterMap.clear();
    // clear messages
    $('.message').hide();
    let chartUrl = 'api/chart';
    // update request url with date if needed
    const isToday = isDateToday(date);


    chartUrl += '?year=' + date.getFullYear();
    const originalMonth = date.getMonth() + 1;
    const newMonth = (originalMonth < 10) ? '0' + originalMonth: originalMonth;
    chartUrl += ('&month=' + newMonth);
    const originalDay = date.getDate();
    const newDay = (originalMonth < 10) ? '0' + originalDay: originalDay;
    chartUrl += ('&day=' + newDay);
    chartUrl += '&offset=' + date.getTimezoneOffset() / 60;
    chartUrl += '&isToday=' + isToday;
    // send get request
    $.get(chartUrl, function(data) {
        const parsed = JSON.parse(data);
        // TODO: do something with parsed chart response
        const clusters = parsed.clusters;
        console.log(parsed);
        $('#chartMessage').hide();
        if (clusters.length == 0) {
            $('#chartMessage').empty();
            $('#chartMessage').append('<div class="alert alert-danger"><h2>No' +
                ' articles stored for' +
                ' this' +
                ' date.</h2></div>');
            $('#chartMessage').show();
            return;
        }
        let i;
        for (i = 0; i < clusters.length; i++) {
            appendCluster(clusters[i], false);
        }
    })
}

function appendCluster(cluster, reclustered) {
    clusterMap.set(cluster.clusterId, cluster);
    const classNum = Math.floor(Math.random() * 4);
    const clusterHtml =
        "<div class='card text-center'>"
            + "<div id=" + cluster.clusterId + " class='card-header clusHead cluster" + classNum + "'>"
                +"<button class='btn btn-primary-outline' type='button' data-toggle='collapse'" +
        " data-target='#collapse" + cluster.clusterId + "'>"
                + "<h2>" + cluster.headline
                + "<div class='clusterSize'>" + cluster.size + "</div></h2>"
                + "</button>"
                + "</div>"
        + "</div>";
    $('#clusters').append(clusterHtml);
    if (reclustered) {
        makeCluster(cluster.clusterId, cluster.articles);
    } else {
        getClusterRequest(cluster.clusterId);
    }
}

function getClusterRequest(clusterId) {
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
        makeCluster(clusterId, parsed.articles);
    });
}

function makeCluster(clusterId, articles) {
    const divId = clusterId + 'articles';
    const articlesHtml = '<div id="collapse' + clusterId + '" class="collapse"' +
        ' data-parent="#clusters">'
        + '<div class="card-body"><ul class="nav nav-tabs nav-fill" role="tablist">'
        + '<li class="nav-item"><a class="nav-link active" id="articlesList-tab"' +
        ' data-toggle="tab" href="#articlesList' + divId + '" role="tab">Articles</a></li>'
        + '<li class="nav-item"><a class="nav-link" id="visualization-tab"' +
        ' data-toggle="tab"' +
        ' href="#visualization' + divId + '" role="tab">Data' +
        ' Visualization</a></li></ul>'
        + '<div class="tab-content">'

        + '<div id="articlesList' + divId + '" class="tab-pane fade show active' +
        ' articlesWrapper"' +
        ' role="tabpanel">'
        + '<ul class="list-group list-group-flush" id="' + divId + '">'
        + '</ul></div>'
        + '<div class="tab-pane fade viz" id="visualization' + divId + '" role="tabpanel">'
        + '<button type="button" id="generate' + clusterId +'" class="btn btn-primary">' +
        ' Render' +
        ' Visualization</button>'
        + '<div class="spinner-border text-primary spin' + clusterId +'" role="status">'
        + '<span class="sr-only">Loading...</span>'
        + '</div>'
        + '<div class="diagram' + clusterId + '">'
        + '<button type="button" class="btn btn-info entityBut' + clusterId + '">Key' +
        ' Word</button>'
        + '<button type="button" class="btn btn-info textBut' + clusterId + '">Text</button>\n'
        + '<button type="button" class="btn btn-info titleBut' + clusterId + '">Title</button>\n'
        + '<button type="button" class="btn btn-info allBut' + clusterId + '">All</button>\n'
        + '<div class="chord-chart" id="chord' + clusterId + '"></div>'
        + '<div class="box-plot" id="box' + clusterId + '"></div>'
        + '<div class="bar-chart"  id="bar' + clusterId + '"></div>'
        + '<button type="button" class="btn btn-outline-blue-grey btn-sm waves-effect"'
        + 'style="margin-bottom:10px;" data-toggle="modal"'
        + ' data-target="#vizModal">Info</button></div>'
        + '</div>'
        + '</div></div></div>';
    $('#' + clusterId).append(articlesHtml);
    $('.spin' + clusterId).hide();
    $('.diagram' + clusterId).hide();
    $('#generate' + clusterId).click(function() {
        $('#generate' + clusterId).hide();
        $('.spin' + clusterId).show();
        const meanRadius = clusterMap.get(clusterId).meanRadius;
        const articleIds = articles.map(a => a.id);
        getClusterDetails(clusterId, meanRadius, clusterMap, articleIds);
        $('.spin' + clusterId).hide();
        $('.diagram' + clusterId).show();

    });
    let i;
    console.log(articles);
    for (i = 0; i < articles.length; i++) {
        const article = articles[i];
        const timePub = article.timePublished;
        let articleDate = new Date(Date.UTC(timePub.slice(0, 4), timePub.slice(5, 7) - 1, timePub.slice(8, 10), timePub.slice(11, 13), timePub.slice(14, 16)))
        let cleanSource = cleanSourceName(article.sourceName);
        let articleHTML = '<li class="list-group-item ' + cleanSource + '"><div id="' + divId + i  + '" class="article ' + '"'
            + '> <h3><a href="' + article.url + '" target="_blank">'
            + article.title + '</a></h3>'
            + '<span class="badge badge-info"><span>' + article.sourceName + ' <span class="badge'
            + ' badge-light ml-2">'
            + articleDate.toLocaleTimeString('en-us', {hour: 'numeric', minute: 'numeric', hour12: true, timeZoneName:'short'})+ '</span></h3></span></div></li>';
        $('#' + divId).append(articleHTML);
    }
    currentlyOpenClusterId = clusterId;
}

function cleanSourceName(sourceName) {
    return sourceName.replace(/[ .,\/#!$%\^&\*;:{}=\-_`~()]/g,"");
}

