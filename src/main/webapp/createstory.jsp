<%@ page import="java.util.*" %>
<html>
  <head>
    <title>My Create Story Page</title>
    <style type="text/css">
      a:link {text-decoration:none; color:#FF704D;;}
      a:visited {text-decoration:none; color:#FF704D;}
      a:hover {text-decoration:underline; color:#FF704D;}
      a:active {text-decoration:underline; color:#FF704D;}
    </style>
  </head>
  <body>
    <div id='fb-root'></div>
    <script> 
      var fbAppId = '185972621567770';
      var fbChannelUrl = '//ming-app-site-01.herokuapp.com/channel.jsp';
    
      // Additional JS functions here
      window.fbAsyncInit = function() {
        FB.init({
          appId      : fbAppId,        // App ID
          channelUrl : fbChannelUrl,
          status     : true,           // check login status
          cookie     : true,           // enable cookies to allow the server to access the session
          xfbml      : true            // parse page for xfbml or html5 social plugins like login button below
        });
  
        // Put additional init code here
      };
  
      // Load the SDK Asynchronously
      (function(d, s, id){
         var js, fjs = d.getElementsByTagName(s)[0];
         if (d.getElementById(id)) {return;}
         js = d.createElement(s); js.id = id;
         js.src = "//connect.facebook.net/en_US/all.js";
         fjs.parentNode.insertBefore(js, fjs);
       }(document, 'script', 'facebook-jssdk'));
    
      //FB.init({appId: "1375400869340796", status: true, cookie: true});

      function postLike(link) {
        FB.api(
           'https://graph.facebook.com/me/og.likes',
           'post',
           { object: link,
             privacy: {'value': 'SELF'} },
           function(response) {
             if (!response) {
               alert('Error occurred.');
             } else if (response.error) {
               alert('Error occurred. Error: ' + response.error.message);
             } else {
               document.getElementById('result').innerHTML =
                 '<a href=\"https://www.facebook.com/me/activity/' + response.id + '\">' +
                 'Story created.  ID is ' + response.id + '</a>';
             }
           }
        );
      }

    </script>
    <div
      class="fb-login-button"
      data-show-faces="true"
      data-width="200"
      data-max-rows="1"
      data-scope="publish_actions">
    </div>
    <div id='result'></div>
    <h1>Atex Press Release RSS Feed</h1>
    <div>
      <%
      List<Map<String, String>> rssItems = (List<Map<String, String>>)request.getAttribute("rssItems");
      if( rssItems != null && !rssItems.isEmpty() ) {
      %>
      <ul>
        <%
        String title = null;
        String description = null;
        String link = null;
        String pubDate = null;
        for(Map<String, String> rssItem : rssItems) {
          title = rssItem.get("title");
          description = rssItem.get("description");
          link = rssItem.get("link");
          pubDate = rssItem.get("pubDate");
        %>
        <li style="padding: 0 0 10px 0;">
          <div>
            <h3>
              <a href="<%=link%>" target="_blank">
                <%=title%>
              </a>
            </h3>
          </div>
          <div><%=description%></div>
          <%
          if( pubDate != null ) {
          %>
          <div style="padding: 10px 0 10px 0; font-style:italic;"><%=pubDate%></div>
          <%    
          }
          %>
          <input type="button" value="Like" onclick='postLike("<%=link%>"); return false;' />
        </li>
        <%
        }
        %>
      </ul>
      <%
      }
      %>
    </div>
  </body>
</html>