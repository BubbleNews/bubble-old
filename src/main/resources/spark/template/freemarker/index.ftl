<#assign content>
    <head>
        <style>
            /*h1, h2 {*/
            /*    font-size: 150%;*/
            /*}*/
            table, th, td {
                border: 0px solid white;
                border-collapse: collapse;
            }
            th, td {
                padding: 2%;
            }
            table, th , td, ul, li {
                text-align: center;
            }
            .submit {
                font-size: 200%;
                font-family: Courier;
                color: navy;
                height: 40px;
                width: 180px;
                border-radius: 4px;
            }
        </style>
    </head>

    <body class="index">

    <table align="center" width="80%">
        <tr><th colspan="2" class="enter">Choose your Querier!</th></tr>
        <tr>
            <td><a href="/stars" class="enter"> Stars </a></td>
            <td>
                <br><a href="/timdb" class="enter"> Timdb </a></td>
        </tr>
    </table>
    </body>

</#assign>
<#include "main.ftl">