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
                padding: 0%;
            }
            table, th , td, ul, li {
                text-align: center;
            }
            /*.enter{*/
            /*    margin-left: 10%;*/
            /*    !*margin-right: 20px;*!*/
            /*}*/
            /*textarea {*/
            /*    margin-right: 10%;*/
            /*}*/
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
    <body class="timdb">

    <h1 align="center" class="enter"a style="font-size: 500%"> tIMDB </h1>

    <form method="POST" action="/connectActors">
    <table align="center" style="width:90%">
<#--        <tr class="enter">-->
<#--            <th><h2>Start Actor</h2></th>-->
<#--            <th><h2>End Actor</h2></th>-->
<#--        </tr>-->

        <tr>

            <td>
                    <label class="enter2" for="a1">Start Actor:</label><br>
                    <textarea name="a1" id="a1"></textarea>
                </td>
            <td>
                <label class="enter2" for="a2">End Actor:</label><br>
                <textarea name="a2" id="a2"></textarea><br>
            </td>
        <tr>
            <td colspan="2"><input class="submit" type="submit"></td>
        </tr>
        <tr>
            <td colspan="2"><h2 class="enter">Results</h2></td>
        </tr>
        <tr style="background-color: #1f389c">
            <td colspan="2"><h3 class="enter" style="font-size: 180%">${result}</h3></td>
        </tr>
        <tr><td colspan="2"><a class="clicked" href="/index"><h2> Go back to the index </h2></a></td></tr>
    </table>
    </form>


    </body>



</#assign>
<#include "main.ftl">