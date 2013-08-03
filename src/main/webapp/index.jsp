<html>
  <head>
    <title>My Feed Dialog Page</title>
    <script src='https://connect.facebook.net/en_US/all.js'></script>
  </head>
  <body>
    <div id="fb-root"></div>
    <script>
      var fbAppId = '185972621567770';
      var fbChannelUrl = '//ming-app-site-01.herokuapp.com/channel.jsp';
    
      FB.init({
        appId      : fbAppId,        // App ID
        channelUrl : fbChannelUrl,
        status     : true,           // check login status
        cookie     : true,           // enable cookies to allow the server to access the session
        xfbml      : true            // parse page for xfbml or html5 social plugins like login button below
      });
      
      FB.getLoginStatus(function(response) {
        if (response.status === 'connected') {
          checkPermission();
        } else if (response.status === 'not_authorized') {
          checkPermission();
        } else {
          var fbOauthUrl = 'https://www.facebook.com/dialog/oauth?'
            + 'client_id=' + fbAppId
            + '&redirect_uri=' + 'https://apps.facebook.com/' + fbAppId + '/'
            + '&scope=publish_actions';
          if (top === self) { 
            window.location.href = fbOauthUrl;
          } else { 
            parent.location.href = fbOauthUrl;
          }
        }
      });
  
      function checkPermission() {
        FB.api(
            "/me/permissions",
            function(response){
              if ( response && response.data && response.data[0].publish_actions == 1 ) {
                window.location.href = "/feed?target=createstory";
              } else {
                tryLoginFB();
              }
            }
        );
      }
      
      function tryLoginFB() {
        FB.login( function(response) {
          if (response.authResponse) {
            FB.api('/me', function(response) {
              window.location.href = "/feed?target=createstory";
            });
          } else {
            location.reload();
          }
        }, {scope:"publish_actions"} );
      }
    </script>
  </body>
</html>