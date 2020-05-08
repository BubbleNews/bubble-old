import {getClusterDetails} from './d3.js';
import Cookies from './js.cookie.mjs';

let currentlyOpenClusterId = null;
const sourceMap = new Map();
const clusterMap = new Map();

const intro = new introJs();
const vizSet = new Set();

/**
 * Setup the webpage
 */
$(document).ready(() => {

    /* startup particlejs */
    particlesJS.load('particles-js', '../json/particles.json',
        function() {
    });

    // add date selector
    addDate();
    // // add on click to date button
    $('#dateButton').click(function() {
        dateClickHandler();
    });


    // get current chart
    getChart(new Date());

    // hide loading wheel
    $('#mainLoader').hide();

    // Add click functions for all the source buttons
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
    // add form submit for reclustering
    $('#reclusterParams').submit(function(event) {
            event.preventDefault();
            $("#clusters").empty();
            clusterMap.clear();
            vizSet.clear();
            // show loading wheel
            $('#mainLoader').show();
            let reclusterEndpoint = 'api/recluster';
            // get correct date to pull articles from
            const date = new Date($('#date').val());
            const isToday = isDateToday(date);
            const year = date.getFullYear();
            // months get returned as 0 - 11 so we add one
            const originalMonth = date.getMonth() + 1;
            // add 0 to front of month and day if needed (for correct formatting)
            const newMonth = (originalMonth < 10) ? '0' + originalMonth: originalMonth;
            const originalDay = date.getDate();
            const newDay = (originalMonth < 10) ? '0' + originalDay: originalDay;
            const offset = date.getTimezoneOffset() / 60;
            const serializedClusterParams = $('#reclusterParams').serialize();
            // send get request
            reclusterEndpoint += `?year=${year}&month=${newMonth}&day=${newDay}
                &offset=${offset}&isToday=${isToday}&${serializedClusterParams}`;

            $.get(reclusterEndpoint, function(data) {
                const parsed = JSON.parse(data);
                $('#mainLoader').hide();
                $('#chartMessage').hide();
                if (parsed.clusters.length == 0) {
                    // display message saying no clusters found with these parameters
                    $('#chartMessage').empty();
                    $('#chartMessage').append('<div class="alert alert-danger"><h2>No' +
                        ' clusters found with these parameters</h2></div>');
                    $('#chartMessage').show();
                }
                // show the new clusters
                let i;
                for (i = 0; i < parsed.clusters.length; i++) {
                    appendCluster(parsed.clusters[i], true);
                }
            });
    });
    // set all the source buttons to have their name
    $('.sourceToggle').each(function(index, element) {
        const source = cleanSourceName($(this).text());
        sourceMap.set(source, true);
    });
    // add click handler for reset button
    $('#resetButton').click(function() {
        addDate();
        dateClickHandler();
    });
    // set up tutorial
    setTutorial();
    // check cookies to see if user is new and show tutorial
    showTutorialIfNew();
    // tutorial click button
    $('#question-button').click(function() {
        intro.start();
    })
});

/**
 * Sets date to current date when loading website
 */
function addDate() {
    const today = new Date().toLocaleDateString()
    $('#date').val(today);
}

$('#date').datepicker();

/**
 * Handler for selecting a new date
 */
function dateClickHandler() {
    vizSet.clear();
    // get the date val
    let dateVal = $('#date').val();
    // if mobile datepickers are used, the date is returend in a different format split by dashes
    // and we must convert it
    const dateDashed = dateVal.split("-");
    if (dateDashed.length === 3) {
        const dateString = dateDashed[1] + '/' + dateDashed[2] + '/' + dateDashed[0]
        $('#date').val(dateString);
        dateVal = dateString;
    }
    // create a new date object with the date
    const dateArr = dateVal.split('/').map(x => parseInt(x));
    const date = new Date(dateArr[2], dateArr[0] - 1, dateArr[1]);
    // check if date is later than today
    $("#chartMessage").hide();
    if (date > new Date()) {
        // display message saying we can't display news from the future
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

/**
 * Given a date, checks if that date is the current day in the user's local time
 * @param date date the user selected
 * @returns {boolean} true if the selected date is the current day in the user's local time, false
 * otherwise
 */
function isDateToday(date) {
    const today = new Date();
    return date.getDate() === today.getDate() && date.getMonth() === today.getMonth()
        && date.getFullYear() === date.getFullYear();
}

/**
 * Gets the new clusters/articles when someone clicks the show button on a new date
 * @param date the date the user selected
 */
function getChart(date) {
    // clear existing clusters
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
    // send get request to get new clusters
    $.get(chartUrl, function(data) {
        const parsed = JSON.parse(data);
        const clusters = parsed.clusters;
        $('#chartMessage').hide();
        if (clusters.length == 0) {
            // if we get no articles display a message
            $('#chartMessage').empty();
            $('#chartMessage').append('<div class="alert alert-danger"><h2>No' +
                ' articles stored for' +
                ' this' +
                ' date.</h2></div>');
            $('#chartMessage').show();
            return;
        }
        // add new clusters to the page
        let i;
        for (i = 0; i < clusters.length; i++) {
            appendCluster(clusters[i], false);
        }
    })
}

/**
 * Adds a single cluster to the page
 * @param cluster the cluster object to add
 * @param reclustered boolean true if this cluster is from reclustering
 */
function appendCluster(cluster, reclustered) {
    clusterMap.set(cluster.clusterId, cluster);
    const classNum = Math.floor(Math.random() * 4);
    // add the card for the cluster
    const clusterHtml =
        "<div class='card text-center'>"
            + "<div id=" + cluster.clusterId + " class='card-header clusHead cluster" + classNum
        + "'>"
                +"<button class='btn btn-primary-outline' type='button' data-toggle='collapse'" +
        " data-target='#collapse" + cluster.clusterId + "'>"
                + "<h2>" + cluster.headline
                + "<div class='clusterSize'>" + cluster.size + "</div></h2>"
                + "</button>"
                + "</div>"
        + "</div>";
    $('#clusters').append(clusterHtml);
    // we must fill in the articles differently if they were reclustered because we get them from
    // the JSON, not a db request
    if (reclustered) {
        makeCluster(cluster.clusterId, cluster.articles, cluster.headline);
    } else {
        getClusterRequest(cluster.clusterId, cluster.headline);
    }
}

/**
 * Calls database to get and add articles for a given cluster (real not reclustered)
 * @param clusterId id of the cluster to add
 */
function getClusterRequest(clusterId, headline) {
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
        makeCluster(clusterId, parsed.articles, headline);
    });
}

/**
 * Adds the articles in for a cluster card that has already been added
 * @param clusterId id of the cluster
 * @param articles all the article objects for each article in the cluster
 */
function makeCluster(clusterId, articles, headline) {
    const divId = clusterId + 'articles';
    // Add html for the tabs within each cluster
    const articlesHtml = '<div id="collapse' + clusterId + '" class="collapse"' +
        ' data-parent="#clusters">'
        + '<div class="card-body article-card">'
        + '<button type="button" class="btn btn-primary" id="generate' + clusterId +'"' +
        ' data-toggle="modal" data-target="#vizModal' + divId + '" style="margin-bottom: 15px">'
        + ' Show Visualization</button>'
        + '<div id="articlesList' + divId + '" class=" articlesWrapper"' +
        ' role="tabpanel">'
        + '<ul class="list-group list-group-flush" id="' + divId + '">'
        + '</ul></div>'
        + '<div id="visualization' + divId + '">'
        + '<div class="modal fade" id="vizModal' + divId + '" tabindex="-1" role="dialog">'
        + '<div class="modal-dialog modal-fluid" role="document">'
        + '<div class="modal-content">'
        + '<div class="modal-header align-self-center">'
        + '<h5 class="modal-title" style="font-weight:bold">' + headline + '</h5></div>'
        + '<div class="modal-body">'
        + '<div class="spinner-border text-primary spin' + clusterId +'" role="status">'
        + '<span class="sr-only">Loading...</span>'
        + '</div>'
        + '<div class="diagram' + clusterId + '">'
        + '<button type="button" class="btn btn-info entityBut' + clusterId + '">Key' +
        ' Word</button>'
        + '<button type="button" class="btn btn-info textBut' + clusterId + '">Text</button>\n'
        + '<button type="button" class="btn btn-info titleBut' + clusterId + '">Title</button>\n'
        + '<button type="button" class="btn btn-info allBut' + clusterId + '">All</button>\n'
        + '<div class="charts row">'
        + '<div class="chord-chart col-lg-6" id="chord' + clusterId + '"></div>'
        + '<div class="box-plot" id="box' + clusterId + '"></div>'
        + '<div class="bar-chart col-lg-6 align-middle"  id="bar' + clusterId + '"></div></div>'
        + '<button type="button" class="btn btn-outline-blue-grey btn-sm waves-effect"'
        + 'style="margin-bottom:10px;" data-toggle="modal"'
        + ' data-target="#vizInfoModal">Info</button>'
        + '</div></div>'
        + '<div class="modal-footer">'
        + '<button type="button" class="btn btn-primary" data-dismiss="modal">Close</button></div>'
        + '</div></div></div></div>'
        + '</div></div>';
    $('#' + clusterId).append(articlesHtml);
    $('.spin' + clusterId).hide();
    $('.diagram' + clusterId).hide();
    // add click handler for generate visualization button
    $('#generate' + clusterId).click(function() {
        if (!vizSet.has(clusterId)) {
            vizSet.add(clusterId);
            $('.spin' + clusterId).show();
            const meanRadius = clusterMap.get(clusterId).meanRadius;
            const articleIds = articles.map(a => a.id);
            // create the visualization
            getClusterDetails(clusterId, meanRadius, clusterMap, articleIds);
            $('.spin' + clusterId).hide();
            $('.diagram' + clusterId).show();
        }

    });
    let i;
    // loop and add the html for each article
    for (i = 0; i < articles.length; i++) {
        const article = articles[i];
        const timePub = article.timePublished;
        // get converted time of publication for each article
        let articleDate = new Date(Date.UTC(timePub.slice(0, 4), timePub.slice(5, 7) - 1,
            timePub.slice(8, 10), timePub.slice(11, 13), timePub.slice(14, 16)))
        let cleanSource = cleanSourceName(article.sourceName);
        // add html for each article
        let articleHTML = '<li class="list-group-item ' + cleanSource + '"><div id="' + divId + i
            + '" class="article ' + '"'
            + '> <h3><a href="' + article.url + '" target="_blank">'
            + article.title + '</a></h3>'
            + '<span class="badge badge-info"><span>' + article.sourceName + ' <span class="badge'
            + ' badge-light ml-2">'
            + articleDate.toLocaleTimeString('en-us', {hour: 'numeric',
                minute: 'numeric', hour12: true, timeZoneName:'short'})+ '</span></h3></span>'
            + '</div></li>';
        $('#' + divId).append(articleHTML);
    }
    currentlyOpenClusterId = clusterId;
}

/**
 * Given a sourceName, get its clean readable version
 * @param sourceName non-clean sourcename
 * @returns {*} clean readable source name
 */
function cleanSourceName(sourceName) {
    return sourceName.replace(/[ .,\/#!$%\^&\*;:{}=\-_`~()]/g,"");
}

/**
 * Sets up a tutorial to introduce new users to Bubble, only showing the tutorial
 * if the user is new (check cookies).
 */
function showTutorialIfNew() {
    if (!Cookies.get('completedTutorial')) {
        setTimeout(function() {
            intro.start();
        }, 500);

    }
}

/**
 * Sets up a tutorial for the user.
 */
function setTutorial() {
    intro.setOption('scrollToElement', false);
    intro.setOptions({
        steps: [
            {
                element: '#mainWindow',
                intro: 'Welcome to Bubble! Bubble groups news articles by topic ' +
                    'to show you the most important headlines of the day. ' +
                    'Click on one of the topics to see ' +
                    'articles inside the topic as well as a visualization of the ' +
                    'grouping.',
                position: 'top',
            },
            {
                element: '#sourcesWrapper',
                intro: 'Click a source to hide or show articles from that source.',
                position: 'top',
            },
            {
                element: '#reclusterWrapper',
                intro: 'Try regrouping articles by changing our similarity calculation.' +
                    ' Click \'INFO\' to learn more!',
                position: 'top',
            },
            {
                element: '#date',
                intro: 'You can use the date selector to view topics from another day.',
                position: 'right',
            },
            {
                element: '#question-button',
                intro: 'If you want to see this tutorial again, click this button. Enjoy!',
                position: 'right',
            }
        ]
    });
    intro.oncomplete(function() {
        Cookies.set("completedTutorial", true, { expires: 7});
    });
}
