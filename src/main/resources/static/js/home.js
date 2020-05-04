let currentlyOpenClusterId = null;

const sourceMap = new Map()

$(document).ready(() => {

    /* startup particlejs */
    particlesJS.load('particles-js', '../json/particles.json', function() {
        console.log('callback - particles.js config loaded');
    });

    // add date selector
    addDate();
    // // add on click to date button
    $('#dateButton').click(function() {
        dateClickHandler(new Date());
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
            $('#mainLoader').show();
            const reclusterEndpoint = 'api/recluster';
            const serialized = $('#reclusterParams').serialize();
            console.log(serialized);
            $.post(reclusterEndpoint, serialized, function(data) {
                const parsed = JSON.parse(data);
                $('#mainLoader').hide();
                console.log(parsed);
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
        dateClickHandler();
    });
});


function addDate() {
    const today = new Date().toISOString().split('T')[0];
    $('#date').val(today);
}

function dateClickHandler() {
    const dateVal = $('#date').val();
    const dateArr = dateVal.split('-').map(x => parseInt(x));
    const date = new Date(dateArr[0], dateArr[1] - 1, dateArr[2]);
    console.log(date);
    // check if date is later than today
    $("#chartMessage").hide();
    if (date > new Date()) {
        $("#clusters").empty();
        $('#chartMessage').empty();
        $('#chartMessage').append('<div class="alert alert-danger"><h2>Cannot find articles from' +
            ' the future.</h2></div>');
        $('#chartMessage').show();
        return;
    } else {
        getChart(date);
    }
}

function stringifyDate(date) {
    const originalMonth = date.getMonth() + 1;
    const month = (originalMonth < 10) ? '0' + originalMonth: originalMonth;
    const originalDay = date.getDate();
    const day = (originalDay < 10) ? '0' + originalDay: originalDay;
    return date.getFullYear() + '-' + month + '-' + day;
}

function getChart(date) {
    $("#clusters").empty();
    // clear messages
    $('.message').hide();
    const dateStr = stringifyDate(date);
    console.log(dateStr);
    let chartUrl = 'api/chart';
    // update request url with date if needed
    chartUrl += '?date=' + dateStr;
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
    const classNum = Math.floor(Math.random() * 4);
    const clusterHtml =
        "<div class='card text-center'>"
            + "<div id=" + cluster.clusterId + " class='card-header cluster" + classNum + "'>"
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
        ' Visualization</button></div>'
        + '</div></div></div>';
    $('#' + clusterId).append(articlesHtml);
    $('#generate' + clusterId).click(function() {
        let element = $('#visualization' + divId);
        $('#generate' + clusterId).hide();
        element.append('<div class="spinner-border text-primary" role="status">'
            + '<span class="sr-only">Loading...</span>'
            + '</div>');
    })
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
            + articleDate.getHours() + ":" + articleDate.getMinutes() + '</span></h3></span></div></li>';
        $('#' + divId).append(articleHTML);
    }
    currentlyOpenClusterId = clusterId;
}

function cleanSourceName(sourceName) {
    return sourceName.replace(/[ .,\/#!$%\^&\*;:{}=\-_`~()]/g,"");
}

