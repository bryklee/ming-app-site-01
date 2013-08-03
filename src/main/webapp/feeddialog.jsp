<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html>
  <head>
    <title>My Feed Dialog Page</title>
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
      var fbAppId = '1375400869340796';
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

      function postToFeed(link, name, caption, description) {

        // calling the API ...
        var obj = {
          method: 'feed',
          //redirect_uri: 'https://apps.facebook.com/1375400869340796/',
          link: link,
          //picture: 'http://fbrell.com/f8.jpg',
          name: name,
          caption: caption,
          description: description
        };

        function callback(response) {
          alert("Post ID: " + response['post_id']);
        }

        FB.ui(obj, callback);
      }

    </script>
    <h1>Feed Dialog FB App</h1>
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
          <input type="button" value="Post to FB wall" onclick='postToFeed("<%=link%>", "<%=title%>", "<%=title%>", "<%=description%>"); return false;' />
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