<#assign content>

<head>
    <style>
        table, th, td {
            border: 0px solid black;
            border-collapse: collapse;
        }
        th, td {
            padding: 5px;
        }
        table, th , td {
            text-align: center;
        }
    </style>
</head>

<body class="stars">

<h1 align="center" class="enter"> Stars </h1>

  <table align="center" style="width:80%">
    <tr class="star-enter">
      <th><h2>Neighbors</h2></th>
      <th><h2>Radius</h2></th>
    </tr>
    <tr>
      <td><form method="POST" action="/neighbors1">
          <label class="star-enter" for="nK1">Number of Stars:</label><br>
          <textarea name="nK1" id="nK1"></textarea><br>
          <label class="star-enter" for="nX">X Position:</label><br>
          <textarea name="nX" id="nX"></textarea><br>
          <label class="star-enter" for="nY">Y Position:</label><br>
          <textarea name="nY" id="nY"></textarea><br>
          <label class="star-enter" for="nZ">Z Position:</label><br>
          <textarea name="nZ" id="nZ"></textarea><br>
          <input type="submit">
        </form></td>
        <td><form method="POST" action="/radius1">
                <label class="star-enter" for="rK1">Radius:</label><br>
                <textarea name="rK1" id="rK1"></textarea><br>
                <label class="star-enter" for="rX">X Position:</label><br>
                <textarea name="rX" id="rX"></textarea><br>
                <label class="star-enter" for="rY">Y Position:</label><br>
                <textarea name="rY" id="rY"></textarea><br>
                <label class="star-enter" for="rZ">Z Position:</label><br>
                <textarea name="rZ" id="rZ"></textarea><br>
                <input type="submit">
            </form></td>
    </tr>
      <tr>
          <td><form method="POST" action="/neighbors2">
                  <label class="star-enter" for="nK2">Number of Stars:</label><br>
                  <textarea name="nK2" id="nK2"></textarea><br>
                  <label class="star-enter" for="nName">Star to Search:</label><br>
                  <textarea name="nName" id="nName"></textarea><br>
                  <input type="submit">
              </form></td>
          <td><form method="POST" action="/radius2">
                  <label class="star-enter" for="rK2">Radius:</label><br>
                  <textarea name="rK2" id="rK2"></textarea><br>
                  <label class="star-enter" for="rName">Star to Search:</label><br>
                  <textarea name="rName" id="rName"></textarea><br>
                  <input type="submit">
              </form></td>
      </tr>
      <tr>
          <td colspan="2"><h1 class="star-enter">Results</h1></td>
      </tr>
      <tr>
          <td colspan="2"><h3 class="star-enter">${result}</h3></td>
      </tr>
      <tr><td colspan="2"><a class="clicked" href="/index"><h2> Go back to the index </h2></a></td></tr>
  </table>

</body>


</#assign>
<#include "main.ftl">